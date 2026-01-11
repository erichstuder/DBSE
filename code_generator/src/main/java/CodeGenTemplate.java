import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.core.ParseException;

public class CodeGenTemplate {
    private Configuration configuration;

    public CodeGenTemplate(String templateDirectory) throws URISyntaxException, IOException  {
        // NOTE: for latest version see: https://freemarker.apache.org/freemarkerdownload.html
        configuration = new Configuration(Configuration.VERSION_2_3_34);

        // TODO: are all of these settings necessary?
        String jarDir = new File(SysML.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        String fullPath = Paths.get(jarDir, templateDirectory).toString();
        configuration.setDirectoryForTemplateLoading(new File(fullPath));
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);
        configuration.setFallbackOnNullLoopVariable(false);
        // cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault()); // necessary?
    }

    public String process(Object dataModel, String templateFile)
    throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
        Template template = configuration.getTemplate(templateFile);
        StringWriter result = new StringWriter();
        template.process(dataModel, result);
        return result.toString();
    }
}
