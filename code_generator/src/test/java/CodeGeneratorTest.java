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
                CodeGenerator.getConfig(null);
            });
        }

        @Test
        void getConfig_invalidPath() throws Exception {
            assertThrows(FileNotFoundException.class, () -> {
                CodeGenerator.getConfig("123");
            });
        }

        @Test
        void getConfig_emptyPath() throws Exception {
            assertThrows(FileNotFoundException.class, () -> {
                CodeGenerator.getConfig("");
            });
        }

        @Test
        void getConfig() throws Exception {
            Properties p = CodeGenerator.getConfig("../../resources/test/code-gen.properties");
            assertNotNull(p, "Expected non-null for valid config path");
        }
    }
}
