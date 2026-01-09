import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.FileNotFoundException;
import java.util.Properties;

public class CodeGeneratorTest {
    @Nested
    class GetConfigTests {
        @Test
        void getConfig_null() throws Exception {
            assertThrows(NullPointerException.class, () -> {
                CodeGenerator.getProperties(null);
            });
        }

        @Test
        void getConfig_invalidPath() throws Exception {
            assertThrows(FileNotFoundException.class, () -> {
                CodeGenerator.getProperties("123");
            });
        }

        @Test
        void getConfig_emptyPath() throws Exception {
            assertThrows(FileNotFoundException.class, () -> {
                CodeGenerator.getProperties("");
            });
        }

        @Test
        void getConfig() throws Exception {
            Properties p = CodeGenerator.getProperties("../../resources/test/code-gen.properties");
            assertNotNull(p, "Expected non-null for valid config path");
        }
    }

    @Nested
    class RunTests {
        @Test
        void run_null_config() {
            CodeGenerator cg = new CodeGenerator();
            cg.run(null);
        }
    }
}
