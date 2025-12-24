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
        if (args.length < 1) {
            System.out.println("Usage: java Parser <path-to-file.sysml>");
            return;
        }

        String filePath = args[0];
        StringBuilder sysmlContent = new StringBuilder();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sysmlContent.append(line).append("\n");
            }
        } catch (java.io.IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return;
        }

        SysMLInteractive sysml = SysMLInteractive.getInstance();
        sysml.loadLibrary("/home/vscode/SysML-v2-Pilot-Implementation/sysml.library/");
        sysml.next(".sysml");
        try {
            sysml.parse(sysmlContent.toString());
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
