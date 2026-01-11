// import java.util.Collections;
import java.util.HashMap;
// import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.omg.sysml.interactive.SysMLInteractive;
import org.omg.sysml.interactive.SysMLInteractiveResult;
// import org.omg.sysml.interactive.VizResult;

public class CodeGenerator {
    private SysML sysml;

    public CodeGenerator() {
        sysml = new SysML();
    }

    public void run(CodeGenConfig config) {
        // // simple data model
        // Map<String, Object> root = new HashMap<>();
        // List<Enum> enums = new ArrayList<>();
        // Enum enum1 = new Enum();
        // HashMap<String, String> members1 = new HashMap<>();
        // members1.put("On", "");
        // members1.put("Off", "");
        // enum1.setName("enum1");
        // enum1.setMembers(members1);
        // Enum enum2 = new Enum();
        // HashMap<String, String> members2 = new HashMap<>();
        // members2.put("Red", "");
        // members2.put("Green", "");
        // enum2.setName("enum2");
        // enum2.setMembers(members2);
        // enums.add(enum1);
        // enums.add(enum2);
        // root.put("enums", enums);

        // try {
        // Template temp = cfg.getTemplate(properties.getProperty("template.file"));
        // System.out.println(temp);
        // System.out.println("Result:\n");
        // Writer out = new OutputStreamWriter(System.out);
        // temp.process(root, out);
        // } catch (IOException e) {
        // e.printStackTrace();
        // } catch (Exception e) {
        // e.printStackTrace();
        // }


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

    public static void main(String[] args) throws URISyntaxException, FileNotFoundException, IOException {
        String codeGenConfigPath = "code-gen.config";
        if (args.length > 0) {
            String arg = args[0];
            if (arg.equals("--help") || arg.equals("-h")) {
                System.out.println("Usage: java -jar CodeGenerator.jar [--config-file <path>] [--help]");
                System.exit(0);
            }
            else if (arg.equals("--config-file")) {
                if (args.length > 1) {
                    codeGenConfigPath = args[1];
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

        CodeGenConfig codeGenConfig = new CodeGenConfig(codeGenConfigPath);
        new CodeGenerator().run(codeGenConfig);
    }
}
