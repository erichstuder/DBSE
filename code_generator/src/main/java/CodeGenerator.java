// import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.Properties;
import java.io.IOException;

import org.omg.sysml.interactive.SysMLInteractive;
import org.omg.sysml.interactive.SysMLInteractiveResult;
// import org.omg.sysml.interactive.VizResult;
// import org.omg.sysml.lang.sysml.Element;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Template;

public class CodeGenerator {
    private Properties config;
    private SysMLInteractive sysml;

    public class Enum {
        private String name;
        private HashMap<String, String> members;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public HashMap<String, String> getMembers() {
            return members;
        }

        public void setMembers(HashMap<String, String> members) {
            this.members = members;
        }
    }

    public CodeGenerator(Properties config) {
        this.config = config;


        sysml = SysMLInteractive.getInstance();
        sysml.loadLibrary("/SysML-v2-Pilot-Implementation/sysml.library/");

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_34);
        try {
            cfg.setDirectoryForTemplateLoading(new File(config.getProperty("template.path")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
        // cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault()); // notwendig? vorangehende auch alle notwendig?

        // simple data model

        Map<String, Object> root = new HashMap<>();
        List<Enum> enums = new ArrayList<>();
        Enum enum1 = new Enum();
        HashMap<String, String> members1 = new HashMap<>();
        members1.put("On", "");
        members1.put("Off", "");
        enum1.setName("enum1");
        enum1.setMembers(members1);
        Enum enum2 = new Enum();
        HashMap<String, String> members2 = new HashMap<>();
        members2.put("Red", "");
        members2.put("Green", "");
        enum2.setName("enum2");
        enum2.setMembers(members2);
        enums.add(enum1);
        enums.add(enum2);
        root.put("enums", enums);

        try {
            Template temp = cfg.getTemplate(config.getProperty("template.file"));
            System.out.println(temp);
            System.out.println("Result:\n");
            Writer out = new OutputStreamWriter(System.out);
            temp.process(root, out);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        // try(Scanner in = new Scanner(System.in)) {
        //     while(true) {
        //         System.out.print("> ");
        //         String input = in.nextLine().trim();

        //         sysml.next(".sysml");
        //         try {
        //             sysml.parse(input);
        //         } catch (Exception e) {
        //             sysml.removeResource();
        //             System.out.println(new SysMLInteractiveResult(e));
        //         }
        //         sysml.addResourceToIndex(sysml.getResource());

        //         // Element rootElement = sysml.getRootElement();
        //         // SysMLInteractiveResult result = new SysMLInteractiveResult(rootElement, Collections.emptyList());
        //         // System.out.println("syntax: " + result.getSyntaxErrors());
        //         // System.out.println("semantic: " + result.getSemanticErrors());
        //         // System.out.println("exception: " + result.getException());
        //         // System.out.println("format root element: " + result.formatRootElement());
        //         // System.out.println("qualified name: " + result.getRootElement().getName());
        //         // System.out.println("has error: " + result.hasErrors());

        //         // System.out.println("resources: " + sysml.getInputResources().getFirst().getURI());
        //         String first_element = sysml.getRootElement().getOwnedElement().getFirst().getName();
        //         System.out.println("first element: " + first_element);
        //         System.out.flush();
        //     }
        // }
    }

    public static Properties getConfig(String configPath) throws IOException, URISyntaxException {
        Properties config = new Properties();
        String jarDir = new File(CodeGenerator.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        File configFile = new File(jarDir, configPath);
        InputStream in = new FileInputStream(configFile);
        config.load(in);
        return config;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        String configPath = "code-gen.properties";
        if (args.length > 0) {
            String arg = args[0];
            if (arg.equals("--help") || arg.equals("-h")) {
                System.out.println("Usage: java -jar CodeGenerator.jar [--config-file <path>] [--help]");
                System.exit(0);
            }
            else if (arg.equals("--config-file")) {
                if (args.length > 1) {
                    configPath = args[1];
                }
                else {
                    System.err.println("Error: --config-file requires a file path.");
                    System.exit(1);
                }
            }
            else {
                System.err.println("Error: Unknown argument '" + arg + "'. Use --help for usage information.");
                System.exit(1);
            }
        }

        Properties config = getConfig(configPath);
        new CodeGenerator(config).run();
    }
}
