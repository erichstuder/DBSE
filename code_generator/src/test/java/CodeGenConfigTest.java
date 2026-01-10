import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.FileNotFoundException;

public class CodeGenConfigTest {
    private static final String CONFIG_PATH = "../../resources/test/code-gen.config";

    @Nested
    class ConstrutorTest {
        @Test
        void configPathNull() throws Exception {
            assertThrows(NullPointerException.class, () -> {
                new CodeGenConfig(null);
            });
        }

        @Test
        void configPathEmptyString() throws Exception {
            assertThrows(FileNotFoundException.class, () -> {
                new CodeGenConfig("");
            });
        }

        @Test
        void configPathInvalid() throws Exception {
            assertThrows(FileNotFoundException.class, () -> {
                new CodeGenConfig("invalid_path");
            });
        }

        @Test
        void configPathValid() throws Exception {
            CodeGenConfig config = new CodeGenConfig(CONFIG_PATH);
            assertNotNull(config, "Expected non-null for valid config path");
        }
    }

    @Nested
    class GettersTest {
        private static CodeGenConfig config;

        @BeforeAll
        static void setUp() throws Exception {
            config = new CodeGenConfig(CONFIG_PATH);
        }

        @Test
        void getTemplatePath() throws Exception {
            String templatePath = config.getTemplatePath();
            assertEquals("src/test/resources/templates", templatePath);
        }

        @Test
        void getTemplateFile() throws Exception {
            String templateFile = config.getTemplateFile();
            assertEquals("main.ftl", templateFile);
        }

        @Test
        void getOutputPath() throws Exception {
            String outputPath = config.getOutputPath();
            assertEquals(".", outputPath);
        }
    }
}
