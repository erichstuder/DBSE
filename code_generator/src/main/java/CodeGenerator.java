// import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.omg.sysml.interactive.SysMLInteractive;
import org.omg.sysml.interactive.SysMLInteractiveResult;
// import org.omg.sysml.interactive.VizResult;
// import org.omg.sysml.lang.sysml.Element;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Template;

public class CodeGenerator {
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

    public CodeGenerator() throws Exception {
        sysml = SysMLInteractive.getInstance();
        sysml.loadLibrary("/SysML-v2-Pilot-Implementation/sysml.library/");

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_34);
        cfg.setDirectoryForTemplateLoading(new File("src/test/resources/templates"));
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

        Template temp = cfg.getTemplate("main.ftl");
        System.out.println(temp);

        System.out.println("Result:\n");
        Writer out = new OutputStreamWriter(System.out);
        temp.process(root, out);
    }

    public void run() {
        try(Scanner in = new Scanner(System.in)) {
            while(true) {
                System.out.print("> ");
                String input = in.nextLine().trim();

                sysml.next(".sysml");
                try {
                    sysml.parse(input);
                } catch (Exception e) {
                    sysml.removeResource();
                    System.out.println(new SysMLInteractiveResult(e));
                }
                sysml.addResourceToIndex(sysml.getResource());

                // Element rootElement = sysml.getRootElement();
                // SysMLInteractiveResult result = new SysMLInteractiveResult(rootElement, Collections.emptyList());
                // System.out.println("syntax: " + result.getSyntaxErrors());
                // System.out.println("semantic: " + result.getSemanticErrors());
                // System.out.println("exception: " + result.getException());
                // System.out.println("format root element: " + result.formatRootElement());
                // System.out.println("qualified name: " + result.getRootElement().getName());
                // System.out.println("has error: " + result.hasErrors());

                // System.out.println("resources: " + sysml.getInputResources().getFirst().getURI());
                String first_element = sysml.getRootElement().getOwnedElement().getFirst().getName();
                System.out.println("first element: " + first_element);
                System.out.flush();
            }
        }
    }

    public static void main(String[] args) {
        try {
            CodeGenerator cg = new CodeGenerator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
