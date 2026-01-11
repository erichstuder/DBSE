import org.omg.sysml.interactive.SysMLInteractive;
import org.omg.sysml.lang.sysml.impl.NamespaceImpl;
import org.omg.sysml.lang.sysml.impl.PackageImpl;
import org.omg.sysml.lang.sysml.impl.EnumerationDefinitionImpl;
import org.omg.sysml.lang.sysml.impl.EnumerationUsageImpl;
import org.omg.sysml.lang.sysml.impl.ActionUsageImpl;
import org.omg.sysml.lang.sysml.impl.ReferenceUsageImpl;
import org.omg.sysml.lang.sysml.impl.StateUsageImpl;
import org.omg.sysml.lang.sysml.impl.SuccessionAsUsageImpl;
import org.omg.sysml.lang.sysml.impl.PerformActionUsageImpl;
import org.omg.sysml.lang.sysml.impl.FeatureReferenceExpressionImpl;
import org.omg.sysml.lang.sysml.impl.FeatureImpl;
import org.omg.sysml.lang.sysml.impl.TransitionUsageImpl;
import org.omg.sysml.lang.sysml.impl.AcceptActionUsageImpl;
import org.omg.sysml.lang.sysml.impl.TriggerInvocationExpressionImpl;
import org.omg.sysml.lang.sysml.impl.OperatorExpressionImpl;
import org.omg.sysml.lang.sysml.impl.LiteralRationalImpl;
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
        } else if (element.getClass() == ActionUsageImpl.class) {
            dataModelElement = new Action();
        } else if (element.getClass() == ReferenceUsageImpl.class) {
            ReferenceUsageImpl e = (ReferenceUsageImpl)element;
            Reference r = new Reference();
            if (e.getDirection() != null) {
                r.setDirection(e.getDirection().getName());
            }
            r.setType(e.getType().getFirst().getName());
            dataModelElement = r;
        } else if (element.getClass() == StateUsageImpl.class) {
            dataModelElement = new State();
        } else if (element.getClass() == SuccessionAsUsageImpl.class) {
            dataModelElement = new SuccessionAs();
        } else if (element.getClass() == PerformActionUsageImpl.class) {
            dataModelElement = new PerformAction();
        } else if (element.getClass() == FeatureReferenceExpressionImpl.class) {
            dataModelElement = new FeatureReferenceExpression();
        } else if (element.getClass() == FeatureImpl.class) {
            dataModelElement = new Feature();
        } else if (element.getClass() == TransitionUsageImpl.class) {
            dataModelElement = new Transition();
        } else if (element.getClass() == AcceptActionUsageImpl.class) {
            dataModelElement = new AcceptAction();
        } else if (element.getClass() == TriggerInvocationExpressionImpl.class) {
            dataModelElement = new TriggerInvocationExpression();
        } else if (element.getClass() == OperatorExpressionImpl.class) {
            dataModelElement = new OperatorExpression();
        } else if (element.getClass() == LiteralRationalImpl.class) {
            dataModelElement = new LiteralRational();
        } else {
            throw new IllegalArgumentException("Invalid element type: " + element.getClass().getName());
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

        public void add(DataModelElement element) {
            throw new IllegalArgumentException("Invalid element type: " + element.getClass().getName());
        }
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
        private List<Action> actions = new ArrayList<>();
        private List<State> states = new ArrayList<>();

        public List<Enum> getEnums() {
            return enums;
        }

        public List<Action> getActions() {
            return actions;
        }

        public void add(DataModelElement element) {
            if (element instanceof Enum) {
                this.enums.add((Enum) element);
            } else if (element instanceof Action) {
                this.actions.add((Action) element);
            } else if (element instanceof State) {
                this.states.add((State) element);
            }else {
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

    public class EnumMember extends DataModelElement {}

    public class Action extends DataModelElement {
        private List<Reference> references = new ArrayList<>();

        public List<Reference> getReferences() {
            return references;
        }

        public void add(DataModelElement element) {
            if (element instanceof Reference) {
                this.references.add((Reference) element);
            } else {
                throw new IllegalArgumentException("Invalid element type: " + element.getClass().getName());
            }
        }
    }

    public class Reference extends DataModelElement {
        private String direction;
        private String type;
        private List<FeatureReferenceExpression> featureReferenceExpressions = new ArrayList<>();
        private List<TriggerInvocationExpression> triggerInvocationExpressions = new ArrayList<>();

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<FeatureReferenceExpression> getFeatureReferenceExpressions() {
            return featureReferenceExpressions;
        }

        public List<TriggerInvocationExpression> getTriggerInvocationExpressions() {
            return triggerInvocationExpressions;
        }

        public void add(DataModelElement element) {
            if (element instanceof FeatureReferenceExpression) {
                this.featureReferenceExpressions.add((FeatureReferenceExpression) element);
            } else if (element instanceof TriggerInvocationExpression) {
                this.triggerInvocationExpressions.add((TriggerInvocationExpression) element);
            } else {
                throw new IllegalArgumentException("Invalid element type: " + element.getClass().getName());
            }
        }
    }

    public class State extends DataModelElement {
        private List<Action> actions = new ArrayList<>();
        private List<SuccessionAs> successions = new ArrayList<>();
        private List<State> states = new ArrayList<>();
        private List<PerformAction> performActions = new ArrayList<>();
        private List<Transition> transitions = new ArrayList<>();

        public List<Action> getActions() {
            return actions;
        }

        public List<SuccessionAs> getSuccessions() {
            return successions;
        }

        public List<State> getStates() {
            return states;
        }

        public List<PerformAction> getPerformActions() {
            return performActions;
        }

        public List<Transition> getTransitions() {
            return transitions;
        }

        public void add(DataModelElement element) {
            if (element instanceof Action) {
                this.actions.add((Action) element);
            } else if (element instanceof SuccessionAs) {
                this.successions.add((SuccessionAs) element);
            } else if (element instanceof State) {
                this.states.add((State) element);
            } else if (element instanceof PerformAction) {
                this.performActions.add((PerformAction) element);
            } else if (element instanceof Transition) {
                this.transitions.add((Transition) element);
            } else {
                throw new IllegalArgumentException("Invalid element type: " + element.getClass().getName());
            }
        }
    }

    public class SuccessionAs extends DataModelElement {
        private List<Reference> references = new ArrayList<>();

        public void add(DataModelElement element) {
            if (element instanceof Reference) {
                this.references.add((Reference) element);
            } else {
                throw new IllegalArgumentException("Invalid element type: " + element.getClass().getName());
            }
        }
    }

    public class PerformAction extends DataModelElement {
        private List<Reference> references = new ArrayList<>();

        public List<Reference> getReferences() {
            return references;
        }

        public void add(DataModelElement element) {
            if (element instanceof Reference) {
                this.references.add((Reference) element);
            } else {
                throw new IllegalArgumentException("Invalid element type: " + element.getClass().getName());
            }
        }
    }

    public class FeatureReferenceExpression extends DataModelElement {
        private List<Feature> features = new ArrayList<>();

        public List<Feature> getFeatures() {
            return features;
        }

        public void add(DataModelElement element) {
            if (element instanceof Feature) {
                this.features.add((Feature) element);
            } else {
                throw new IllegalArgumentException("Invalid element type: " + element.getClass().getName());
            }
        }
    }

    public class Feature extends DataModelElement {
        private List<LiteralRational> literalRationals = new ArrayList<>();
        private List<FeatureReferenceExpression> featureReferenceExpressions = new ArrayList<>();
        private List<OperatorExpression> operatorExpressions = new ArrayList<>();

        public List<LiteralRational> getLiteralRationals() {
            return literalRationals;
        }

        public List<FeatureReferenceExpression> getFeatureReferenceExpressions() {
            return featureReferenceExpressions;
        }

        public List<OperatorExpression> getOperatorExpressions() {
            return operatorExpressions;
        }

        public void add(DataModelElement element) {
            if (element instanceof LiteralRational) {
                this.literalRationals.add((LiteralRational) element);
            } else if (element instanceof FeatureReferenceExpression) {
                this.featureReferenceExpressions.add((FeatureReferenceExpression) element);
            } else if (element instanceof OperatorExpression) {
                this.operatorExpressions.add((OperatorExpression) element);
            } else {
                throw new IllegalArgumentException("Invalid element type: " + element.getClass().getName());
            }
        }
    }

    public class Transition extends DataModelElement {
        private List<Reference> references = new ArrayList<>();
        private List<AcceptAction> acceptActions = new ArrayList<>();
        private List<SuccessionAs> successions = new ArrayList<>();

        public List<Reference> getReferences() {
            return references;
        }

        public List<AcceptAction> getAcceptActions() {
            return acceptActions;
        }

        public List<SuccessionAs> getSuccessions() {
            return successions;
        }

        public void add(DataModelElement element) {
            if (element instanceof Reference) {
                this.references.add((Reference) element);
            } else if (element instanceof AcceptAction) {
                this.acceptActions.add((AcceptAction) element);
            } else if (element instanceof SuccessionAs) {
                this.successions.add((SuccessionAs) element);
            } else {
                throw new IllegalArgumentException("Invalid element type: " + element.getClass().getName());
            }
        }
    }

    public class AcceptAction extends DataModelElement {
        private List<Reference> references = new ArrayList<>();

        public List<Reference> getReferences() {
            return references;
        }

        public void add(DataModelElement element) {
            if (element instanceof Reference) {
                this.references.add((Reference) element);
            } else {
                throw new IllegalArgumentException("Invalid element type: " + element.getClass().getName());
            }
        }
    }

    public class TriggerInvocationExpression extends DataModelElement {
        private List<Feature> features = new ArrayList<>();

        public List<Feature> getFeatures() {
            return features;
        }

        public void add(DataModelElement element) {
            if (element instanceof Feature) {
                this.features.add((Feature) element);
            } else {
                throw new IllegalArgumentException("Invalid element type: " + element.getClass().getName());
            }
        }
    }

    public class OperatorExpression extends DataModelElement {
        private List<Feature> features = new ArrayList<>();

        public List<Feature> getFeatures() {
            return features;
        }

        public void add(DataModelElement element) {
            if (element instanceof Feature) {
                this.features.add((Feature) element);
            } else {
                throw new IllegalArgumentException("Invalid element type: " + element.getClass().getName());
            }
        }
    }

    public class LiteralRational extends DataModelElement {
        // TODO: remove?
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
