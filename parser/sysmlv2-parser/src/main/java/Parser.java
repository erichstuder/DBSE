import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.omg.sysml.interactive.SysMLInteractive;
import org.omg.sysml.interactive.SysMLInteractiveResult;
import org.omg.sysml.interactive.VizResult;
// import org.omg.sysml.interactive.VizResult;
// import org.omg.sysml.interactive.Resource;
import org.omg.sysml.lang.sysml.Element;

// import java.util.Collections;
// import java.util.List;

public class Parser {
    public static void main(String[] args) {
        System.out.println("Hello, World! \n");

        SysMLInteractive sysml = SysMLInteractive.getInstance();
        // SysMLInteractiveResult result = sysml.process(
        //     // "package P {part definition PD {} part p is a PD {} }", true);
        //     "package myRoot { \n" +
        //             "part test {} \n" +
        //             "}",
        //     true);

        sysml.next(".sysml");
        try {
            sysml.parse(
                "package myRoot {" +
                    "part test {}" +
                "}");
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
        VizResult viz_result = sysml.viz(Collections.singletonList("myRoot"), Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        System.out.println("\n\n\nsvg:\n " + viz_result.getSVG());
    }
}
