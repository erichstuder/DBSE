import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import freemarker.template.TemplateException;
import java.net.URISyntaxException;
import java.io.IOException;

public class CodeGenTemplateTest {
    private static final String RESOURCES_FOLDER = "../../resources/test/";

    @Test
    void process() throws URISyntaxException, IOException, TemplateException {
        SysML sysml = new SysML();
        sysml.parse(RESOURCES_FOLDER + "LedControl.sysml");
        Object dataModel = sysml.getDataModel();

        CodeGenTemplate codeGenTemplate = new CodeGenTemplate(RESOURCES_FOLDER);
        String result = codeGenTemplate.process(dataModel, "code-gen-template.ftl");
        assertTrue(result.startsWith("// Package: LedControl\n"));
    }
}
