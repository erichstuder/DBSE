
import java.util.Collections;
import java.util.Scanner;

import org.omg.sysml.interactive.SysMLInteractive;
import org.omg.sysml.interactive.SysMLInteractiveResult;
import org.omg.sysml.interactive.VizResult;
import org.omg.sysml.lang.sysml.Element;

public class InteractiveParser {
    public static void main(String[] args) {
        SysMLInteractive sysml = SysMLInteractive.getInstance();
        sysml.loadLibrary("/home/vscode/SysML-v2-Pilot-Implementation/sysml.library/");

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
                Element rootElement = sysml.getRootElement();
                SysMLInteractiveResult result = new SysMLInteractiveResult(rootElement, Collections.emptyList());
                sysml.addResourceToIndex(sysml.getResource());

                System.out.println("syntax: " + result.getSyntaxErrors());
                System.out.println("semantic: " + result.getSemanticErrors());
                System.out.println("exception: " + result.getException());
                System.out.println("format root element: " + result.formatRootElement());
                System.out.println("qualified name: " + result.getRootElement().getName());
                System.out.println("has error: " + result.hasErrors());

                System.out.println("resources: " + sysml.getInputResources().getFirst().getURI());
                String first_element = sysml.getRootElement().getOwnedElement().getFirst().getName();
                VizResult viz_result = sysml.viz(
                        Collections.singletonList(first_element),
                        Collections.emptyList(),
                        Collections.singletonList("STDCOLOR"),
                        Collections.emptyList());
                System.out.println("\n\n\nsvg:\n" + viz_result.getSVG());
            }
        }
    }
}
