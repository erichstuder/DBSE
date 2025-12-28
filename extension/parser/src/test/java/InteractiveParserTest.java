import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.*;

public class InteractiveParserTest {
    @Test
    void testInteractiveParserProcess() throws Exception {
        try (InteractiveParserSession session = startInteractiveParserSession()) {
            String sysmlv2_example = readResourceAsLine("led_state_machine.sysml");
            session.writer.write(sysmlv2_example + "\n");
            session.writer.flush();

            java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();
            String line = null;
            boolean startFound = false;
            int startIndex = -1;
            int endIndex = -1;
            try {
                while (true) {
                    java.util.concurrent.Future<String> future = executor.submit(session.reader::readLine);
                    try {
                        line = future.get(20, java.util.concurrent.TimeUnit.SECONDS);
                    } catch (java.util.concurrent.TimeoutException e) {
                        future.cancel(true);
                        throw new AssertionError("Timeout: svg content not found in output within 20 seconds");
                    }
                    if (line == null) {
                        break;
                    }
                    if (!startFound) {
                        startIndex = line.indexOf("<svg ");
                    }
                    if (startIndex != -1) {
                        startFound = true;
                        endIndex = line.indexOf("</svg>");
                    }
                    if (startIndex != -1 && endIndex != -1) {
                        break;
                    }
                }
            } finally {
                executor.shutdownNow();
            }

            assertTrue(startIndex != -1 && endIndex != -1, "svg content not found in output");
        }
    }

    private static InteractiveParserSession startInteractiveParserSession() throws IOException {
        String interactive = "/SysML-v2-Pilot-Implementation/org.omg.sysml.interactive/target/org.omg.sysml.interactive-0.55.0-SNAPSHOT-all.jar";
        String classes = "build/classes/java/main";
        String resources = "build/resources/main";

        ProcessBuilder pb = new ProcessBuilder(
            "java",
            "-cp",
            classes + ":" + resources + ":" + interactive,
            "InteractiveParser"
        );
        pb.redirectErrorStream(true);
        Process process = pb.start();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        // Wait for "Startup complete." with a 20s timeout
        java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();
        String line = null;
        boolean found = false;
        try {
            while (true) {
                java.util.concurrent.Future<String> future = executor.submit(reader::readLine);
                try {
                    line = future.get(20, java.util.concurrent.TimeUnit.SECONDS);
                } catch (java.util.concurrent.TimeoutException e) {
                    future.cancel(true);
                    throw new IOException("Timeout: Did not receive 'Startup complete.' from parser within 20 seconds");
                } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
                    future.cancel(true);
                    throw new IOException("Error waiting for parser startup message", e);
                }
                if (line == null) {
                    break;
                }
                if (line.contains("Startup complete.")) {
                    found = true;
                    break;
                }
            }
        } finally {
            executor.shutdownNow();
        }
        if (!found) {
            try { writer.close(); } catch (IOException ignored) {}
            try { reader.close(); } catch (IOException ignored) {}
            process.destroy();
            throw new IOException("Timeout: Did not receive 'Startup complete.' from parser within 20 seconds");
        }
        return new InteractiveParserSession(process, writer, reader);
    }

    private static class InteractiveParserSession implements AutoCloseable {
        final Process process;
        final BufferedWriter writer;
        final BufferedReader reader;

        InteractiveParserSession(Process process, BufferedWriter writer, BufferedReader reader) {
            this.process = process;
            this.writer = writer;
            this.reader = reader;
        }

        @Override
        public void close() throws IOException {
            try {
                writer.close();
            } catch (IOException ignored) {}
            try {
                reader.close();
            } catch (IOException ignored) {}
            process.destroy();
        }
    }

    private static String readResourceAsLine(String filename) throws Exception {
        String resourcPath = "src/test/resources/" + filename;
        BufferedReader reader = new BufferedReader(new FileReader(resourcPath));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }
}
