import it.unimi.di.prog2.jubbiot.BlackBoxTestsGenerator;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

public class JubbiotTest {

  @TestFactory
  public List<? extends DynamicNode> testAll() throws IOException {
    return new BlackBoxTestsGenerator("tests").generate();
  }
}
