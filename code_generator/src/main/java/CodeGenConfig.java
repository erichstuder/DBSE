import java.util.Properties;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

public class CodeGenConfig {
    private Properties properties;

    public CodeGenConfig(String configPath) throws URISyntaxException, FileNotFoundException, IOException {
        this.properties = new Properties();
        String jarDir = new File(CodeGenConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        File propertiesFile = new File(jarDir, configPath);
        InputStream in = new FileInputStream(propertiesFile);
        properties.load(in);
    }

    public String getTemplatePath() {
        return properties.getProperty("template.path");
    }

    public String getTemplateFile() {
        return properties.getProperty("template.file");
    }

    public String getOutputPath() {
        return properties.getProperty("output.path");
    }
}
