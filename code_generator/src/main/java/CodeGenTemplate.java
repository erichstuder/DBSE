import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import freemarker.template.Template;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.core.ParseException;

public class CodeGenTemplate {
    private Configuration configuration;

    public CodeGenTemplate(String templatePath) throws IOException {
        // NOTE: for latest version see: https://freemarker.apache.org/freemarkerdownload.html
        configuration = new Configuration(Configuration.VERSION_2_3_34);

        // TODO: are all of these settings necessary?
        configuration.setDirectoryForTemplateLoading(new File(templatePath));
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);
        configuration.setFallbackOnNullLoopVariable(false);
        // cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault()); // necessary?
    }

    public void process(String templateFile) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
        // Template template = configuration.getTemplate(templateFile);
        // Writer out = new OutputStreamWriter(System.out);
        // template.process(root, out); // Note: root is the model
    }
}
