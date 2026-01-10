import org.junit.jupiter.api.Test;

public class CodeGeneratorTest {
    @Test
    void run_null_config() {
        CodeGenerator cg = new CodeGenerator();
        cg.run(null);
    }
}
