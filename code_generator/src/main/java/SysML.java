import org.omg.sysml.interactive.SysMLInteractive;
import org.omg.sysml.lang.sysml.impl.NamespaceImpl;
import org.omg.sysml.lang.sysml.impl.PackageImpl;
import org.omg.sysml.lang.sysml.impl.EnumerationDefinitionImpl;
import org.omg.sysml.lang.sysml.impl.EnumerationUsageImpl;

import org.omg.sysml.lang.sysml.Element;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SysML {
    private SysMLInteractive sysml;

    public SysML() {
        sysml = SysMLInteractive.getInstance();
        sysml.loadLibrary("/SysML-v2-Pilot-Implementation/sysml.library/");
    }

    public void parse(String sysmlFile) throws URISyntaxException, IOException {
        String jarDir = new File(SysML.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        String fullPath = Paths.get(jarDir, sysmlFile).toString();
        String content = new String(Files.readAllBytes(Paths.get(fullPath)));

        sysml.next(".sysml");
        try {
            sysml.parse(content);
        } catch (IOException e) {
            sysml.removeResource();
            throw e;
        }
        sysml.addResourceToIndex(sysml.getResource());
    }

    public DataModelElement getDataModel() {
        Element rootElement = sysml.getRootElement();
        DataModelElement dataModelRootElement = toDataModelElement(rootElement);
        return dataModelRootElement;
    }

    private DataModelElement toDataModelElement(Element element) {
        DataModelElement dataModelElement;

        if (element.getClass() == NamespaceImpl.class) {
            dataModelElement = new Namespace();
        } else if (element.getClass() == PackageImpl.class) {
            dataModelElement = new Package();
        } else if (element.getClass() == EnumerationDefinitionImpl.class) {
            dataModelElement = new Enum();
        } else if (element.getClass() == EnumerationUsageImpl.class) {
            dataModelElement = new EnumMember();
        } else {
            return null;
        }

        dataModelElement.setName(element.getName());

        element.getOwnedElement().forEach(child -> {
            DataModelElement e = toDataModelElement(child);
            dataModelElement.add(e);
        });

        return dataModelElement;
    }

    public abstract class DataModelElement {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public abstract void add(DataModelElement element);
    }

    public class Namespace extends DataModelElement {
        private List<Package> packages = new ArrayList<>();

        public List<Package> getPackages() {
            return packages;
        }

        public void add(DataModelElement element) {
            if (element instanceof Package) {
                this.packages.add((Package) element);
            } else {
                throw new IllegalArgumentException("Invalid element type: " + element.getClass().getName());
            }
        }
    }

    public class Package extends DataModelElement {
        private List<Enum> enums = new ArrayList<>();

        public List<Enum> getEnums() {
            return enums;
        }

        public void add(DataModelElement element) {
            if (element instanceof Enum) {
                this.enums.add((Enum) element);
            } else {
                throw new IllegalArgumentException("Invalid element type: " + element.getClass().getName());
            }
        }
    }

    public class Enum extends DataModelElement {
        private List<EnumMember> members = new ArrayList<>();

        public List<EnumMember> getMembers() {
            return members;
        }

        public void add(DataModelElement element) {
            if (element instanceof EnumMember) {
                this.members.add((EnumMember) element);
            } else {
                throw new IllegalArgumentException("Invalid element type: " + element.getClass().getName());
            }
        }
    }

    public class EnumMember extends DataModelElement {
        public void add(DataModelElement element) {
            throw new IllegalArgumentException("Invalid element type: " + element.getClass().getName());
        }
    }

    // // Element rootElement = sysml.getRootElement();
    // // SysMLInteractiveResult result = new SysMLInteractiveResult(rootElement,
    // Collections.emptyList());
    // // System.out.println("syntax: " + result.getSyntaxErrors());
    // // System.out.println("semantic: " + result.getSemanticErrors());
    // // System.out.println("exception: " + result.getException());
    // // System.out.println("format root element: " + result.formatRootElement());
    // // System.out.println("qualified name: " + result.getRootElement().getName());
    // // System.out.println("has error: " + result.hasErrors());

    // // System.out.println("resources: " + sysml.getInputResources().getFirst().getURI());
    // String first_element = sysml.getRootElement().getOwnedElement().getFirst().getName();
    // System.out.println("first element: " + first_element);
    // System.out.flush();
}
