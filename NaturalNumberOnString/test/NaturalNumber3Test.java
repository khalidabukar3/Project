import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber1L;

/**
 * Customized JUnit test fixture for {@code NaturalNumber3}.
 *

 * @author K. Abukar
 */

public class NaturalNumber3Test extends NaturalNumberTest {

    @Override
    protected final NaturalNumber constructorTest() {
        // Using NaturalNumber3 constructor.
        return new NaturalNumber3();

    }

    @Override
    protected final NaturalNumber constructorTest(int i) {
        return new NaturalNumber3(i);
    }

    @Override
    protected final NaturalNumber constructorTest(String s) {
        return new NaturalNumber3(s);
    }

    @Override
    protected final NaturalNumber constructorTest(NaturalNumber n) {
        return new NaturalNumber3(n);
    }

    @Override
    protected final NaturalNumber constructorRef() {
        // Using NaturalNumber1L constructor.
        return new NaturalNumber1L();

    }

    @Override
    protected final NaturalNumber constructorRef(int i) {
        return new NaturalNumber1L(i);

    }

    @Override
    protected final NaturalNumber constructorRef(String s) {
        return new NaturalNumber1L(s);
    }

    @Override
    protected final NaturalNumber constructorRef(NaturalNumber n) {
        return new NaturalNumber1L(n);
    }

}
