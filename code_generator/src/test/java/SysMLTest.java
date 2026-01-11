import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URISyntaxException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Map;

public class SysMLTest {
    private static final String MODEL_PATH = "../../resources/test/LedControl.sysml";

    @Nested
    class ParsingTest {
        private static SysML sysml;

        @BeforeAll
        static void setUp() throws Exception {
            sysml = new SysML();
        }

        @Test
        void parse_null() throws URISyntaxException, IOException {
            assertThrows(NullPointerException.class, () -> {
                sysml.parse(null);
            });
        }

        @Test
        void parse_empty() throws URISyntaxException, IOException {
            assertThrows(IOException.class, () -> {
                sysml.parse("");
            });
        }

        @Test
        void parse_invalid() throws URISyntaxException, IOException {
            assertThrows(NoSuchFileException.class, () -> {
                sysml.parse("non-sense");
            });
        }

        @Test
        void parse_valid() throws URISyntaxException, IOException {
            sysml.parse(MODEL_PATH);
        }
    }

    @Nested
    class DataModelTest {
        private SysML sysml;

        @BeforeEach
        void setUp() throws URISyntaxException, IOException {
            sysml = new SysML();
            sysml.parse(MODEL_PATH);
        }

        @Test
        void firstElementName() {
            SysML.DataModelElement dataModel = sysml.getDataModel();
            assertEquals("Namespace", dataModel.getClass().getSimpleName());
            assertEquals(null, dataModel.getName());
        }

        @Test
        void secondElementName() {
            SysML.DataModelElement dataModel = sysml.getDataModel();
            String firstPackageName = ((SysML.Namespace)dataModel).getPackages().getFirst().getName();
            assertEquals("LedControl", firstPackageName);
        }
    }
}
