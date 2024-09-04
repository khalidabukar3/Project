import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.naturalnumber.NaturalNumber;

/**
 * JUnit test fixture for {@code NaturalNumber}'s constructors and kernel
 * methods.
 *
 * @author K. Abukar
 *
 */
public abstract class NaturalNumberTest {

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @return the new number
     * @ensures constructorTest = 0
     */
    protected abstract NaturalNumber constructorTest();

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param i
     *            {@code int} to initialize from
     * @return the new number
     * @requires i >= 0
     * @ensures constructorTest = i
     */
    protected abstract NaturalNumber constructorTest(int i);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param s
     *            {@code String} to initialize from
     * @return the new number
     * @requires there exists n: NATURAL (s = TO_STRING(n))
     * @ensures s = TO_STRING(constructorTest)
     */
    protected abstract NaturalNumber constructorTest(String s);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param n
     *            {@code NaturalNumber} to initialize from
     * @return the new number
     * @ensures constructorTest = n
     */
    protected abstract NaturalNumber constructorTest(NaturalNumber n);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @return the new number
     * @ensures constructorRef = 0
     */
    protected abstract NaturalNumber constructorRef();

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param i
     *            {@code int} to initialize from
     * @return the new number
     * @requires i >= 0
     * @ensures constructorRef = i
     */
    protected abstract NaturalNumber constructorRef(int i);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param s
     *            {@code String} to initialize from
     * @return the new number
     * @requires there exists n: NATURAL (s = TO_STRING(n))
     * @ensures s = TO_STRING(constructorRef)
     */
    protected abstract NaturalNumber constructorRef(String s);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param n
     *            {@code NaturalNumber} to initialize from
     * @return the new number
     * @ensures constructorRef = n
     */
    protected abstract NaturalNumber constructorRef(NaturalNumber n);

    /**
     * Test int constructor with zero.
     */
    @Test
    public void testConstructorIntEdge() {
        NaturalNumber test = this.constructorTest(0);
        NaturalNumber testExpected = this.constructorRef(0);
        assertEquals(testExpected, test);

    }

    /**
     * Test int constructor with max int value.
     */
    @Test
    public void testConstructorIntHard() {
        NaturalNumber test = this.constructorTest(Integer.MAX_VALUE);
        NaturalNumber testExpected = this.constructorRef(Integer.MAX_VALUE);
        assertEquals(testExpected, test);
    }

    /**
     * Test int constructor with a normal int value.
     */
    @Test
    public void testConstructorIntEasy() {
        final int number = 12345;
        NaturalNumber test = this.constructorTest(number);
        NaturalNumber testExpected = this.constructorRef(number);
        assertEquals(testExpected, test);
    }

    /**
     * Test string constructor with huge string value.
     */
    @Test
    public void testConstructorStringHard() {
        NaturalNumber test = this
                .constructorTest("123456789012345678901234567890");
        NaturalNumber testExpected = this
                .constructorRef("123456789012345678901234567890");
        assertEquals(testExpected, test);
    }

    /**
     * Test empty string constructor.
     */
    @Test
    public void testConstructorStringEdge() {
        NaturalNumber test = this.constructorTest("0");
        NaturalNumber testExpected = this.constructorRef("0");
        assertEquals(testExpected, test);
    }

    /**
     * Test NaturalNumber constructor with 0.
     */
    @Test
    public void testConstructorNNEdge() {
        NaturalNumber testExpected = this.constructorRef();
        NaturalNumber test = this.constructorTest(testExpected);
        assertEquals(testExpected, test);
    }

    /**
     * Test NaturalNumber constructor with a normal int value.
     */
    @Test
    public void testConstructorNNHard() {
        final int number = 53063;
        NaturalNumber testExpected = this.constructorRef(number);
        NaturalNumber test = this.constructorTest(testExpected);
        assertEquals(testExpected, test);
    }

    /**
     * Test isZero with max int value.
     */
    @Test
    public void testIsZeroIntNotEmpty() {
        NaturalNumber test = this.constructorTest(Integer.MAX_VALUE);
        NaturalNumber testExpected = this.constructorRef(Integer.MAX_VALUE);
        assertEquals(testExpected.isZero(), test.isZero());
    }

    /**
     * Test isZero with empty constructor, value of 0.
     */
    @Test
    public void testIsZeroIntEmpty() {
        NaturalNumber test = this.constructorTest();
        NaturalNumber testExpected = this.constructorRef();

        assertEquals(testExpected.isZero(), test.isZero());
    }

    /**
     * Test multiplyBy10 with int constructor.
     */
    @Test
    public void testMultiplyBy10IntEasy() {
        final int a = 439;
        final int b = 4;
        NaturalNumber test = this.constructorTest(a);
        NaturalNumber testExpected = this.constructorRef(a);
        testExpected.multiplyBy10(b);
        test.multiplyBy10(b);
        assertEquals(testExpected, test);
    }

    /**
     * Test multiplyBy10 with int constructor.
     */
    @Test
    public void testMultiplyBy10IntHard() {
        final int a = 231232;
        final int b = 5;
        NaturalNumber test = this.constructorTest(a);
        NaturalNumber testExpected = this.constructorRef(a);
        testExpected.multiplyBy10(b);
        test.multiplyBy10(b);
        assertEquals(testExpected, test);
    }

    /**
     * Test multiplyBy10 with int constructor.
     */
    @Test
    public void testMultiplyBy10IntEdge() {
        final int seven = 7;
        NaturalNumber test = this.constructorTest(0);
        NaturalNumber testExpected = this.constructorRef(0);
        testExpected.multiplyBy10(seven);
        test.multiplyBy10(seven);
        assertEquals(testExpected, test);
    }

    /**
     * Test multiplyBy10 with int constructor.
     */
    @Test
    public void testMultiplyBy10IntEdge2() {
        NaturalNumber test = this.constructorTest(0);
        NaturalNumber testExpected = this.constructorRef(0);
        testExpected.multiplyBy10(0);
        test.multiplyBy10(0);
        assertEquals(testExpected, test);
    }

    /**
     * Test multiplyBy10 with int constructor.
     */
    @Test
    public void testMultiplyBy10IntEdge3() {
        final int seven = 7;
        NaturalNumber test = this.constructorTest(seven);
        NaturalNumber testExpected = this.constructorRef(seven);
        testExpected.multiplyBy10(0);
        test.multiplyBy10(0);
        assertEquals(testExpected, test);
    }

    /**
     * Test multiplyBy10 with int constructor.
     */
    @Test
    public void testMultiplyBy10IntEdge4() {
        final int eight = 8;
        NaturalNumber test = this.constructorTest(0);
        NaturalNumber testExpected = this.constructorRef(0);
        testExpected.multiplyBy10(eight);
        test.multiplyBy10(eight);
        assertEquals(testExpected, test);
    }

    /**
     * Test divideBy10 with int constructor.
     */
    public void testDivideBy10IntEdge() {
        NaturalNumber test = this.constructorTest(0);
        NaturalNumber testExpected = this.constructorRef(0);
        int returned = test.divideBy10();
        int returnedExpected = testExpected.divideBy10();
        assertEquals(testExpected, test);
        assertEquals(returnedExpected, returned);

    }

    /**
     * Test divideBy10 with int constructor.
     */
    public void testDivideBy10IntEasy() {
        final int six = 6;
        NaturalNumber test = this.constructorTest(six);
        NaturalNumber testExpected = this.constructorRef(six);
        int returned = test.divideBy10();
        int returnedExpected = testExpected.divideBy10();
        assertEquals(testExpected, test);
        assertEquals(returnedExpected, returned);

    }

    /**
     * Test divideBy10 with int constructor.
     */
    public void testDivideBy10IntHard() {
        final int number = 50535;
        NaturalNumber test = this.constructorTest(number);
        NaturalNumber testExpected = this.constructorRef(number);
        int returned = test.divideBy10();
        int returnedExpected = testExpected.divideBy10();
        assertEquals(testExpected, test);
        assertEquals(returnedExpected, returned);

    }
}
