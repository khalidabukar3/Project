import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;

/**
 * @author Khalid Abukar
 *
 */
public class CryptoUtilitiesTest {

    /*
     * Tests of reduceToGCD
     */

    @Test
    public void testReduceToGCD_0_0() {
        NaturalNumber n = new NaturalNumber2(0);
        NaturalNumber nExpected = new NaturalNumber2(0);
        NaturalNumber m = new NaturalNumber2(0);
        NaturalNumber mExpected = new NaturalNumber2(0);
        CryptoUtilities.reduceToGCD(n, m);
        assertEquals(nExpected, n);
        assertEquals(mExpected, m);
    }

    @Test
    public void testReduceToGCD_30_21() {
        NaturalNumber n = new NaturalNumber2(30);
        NaturalNumber nExpected = new NaturalNumber2(3);
        NaturalNumber m = new NaturalNumber2(21);
        NaturalNumber mExpected = new NaturalNumber2(0);
        CryptoUtilities.reduceToGCD(n, m);
        assertEquals(nExpected, n);
        assertEquals(mExpected, m);
    }

    /*
     * Tests of isEven
     */

    @Test
    public void testIsEven_0() {
        NaturalNumber n = new NaturalNumber2(0);
        NaturalNumber nExpected = new NaturalNumber2(0);
        boolean result = CryptoUtilities.isEven(n);
        assertEquals(nExpected, n);
        assertEquals(true, result);
    }

    @Test
    public void testIsEven_1() {
        NaturalNumber n = new NaturalNumber2(1);
        NaturalNumber nExpected = new NaturalNumber2(1);
        boolean result = CryptoUtilities.isEven(n);
        assertEquals(nExpected, n);
        assertEquals(false, result);
    }

    /*
     * Tests of powerMod
     */

    @Test
    public void testPowerMod_0_0_2() {
        NaturalNumber n = new NaturalNumber2(0);
        NaturalNumber nExpected = new NaturalNumber2(1);
        NaturalNumber p = new NaturalNumber2(0);
        NaturalNumber pExpected = new NaturalNumber2(0);
        NaturalNumber m = new NaturalNumber2(2);
        NaturalNumber mExpected = new NaturalNumber2(2);
        CryptoUtilities.powerMod(n, p, m);
        assertEquals(nExpected, n);
        assertEquals(pExpected, p);
        assertEquals(mExpected, m);
    }

    @Test
    public void testPowerMod_17_18_19() {
        NaturalNumber n = new NaturalNumber2(17);
        NaturalNumber nExpected = new NaturalNumber2(1);
        NaturalNumber p = new NaturalNumber2(18);
        NaturalNumber pExpected = new NaturalNumber2(18);
        NaturalNumber m = new NaturalNumber2(19);
        NaturalNumber mExpected = new NaturalNumber2(19);
        CryptoUtilities.powerMod(n, p, m);
        assertEquals(nExpected, n);
        assertEquals(pExpected, p);
        assertEquals(mExpected, m);
    }

    /*
     * Test case to check if 2 is a witness to the compositeness of 5 using
     * CryptoUtilities.isWitnessToCompositeness method. Expected result is
     * false.
     */

    @Test
    public void isWitnessToCompositeness_1() {
        NaturalNumber w = new NaturalNumber2(2);
        NaturalNumber n = new NaturalNumber2(5);

        boolean result = CryptoUtilities.isWitnessToCompositeness(w, n);

        assertEquals(false, result);

    }

    /*
     * Test case to verify if the number 5 is prime using the
     * CryptoUtilities.isPrime1 method. Expected result is true because 5 is
     * prime number.
     */

    @Test
    public void testIsPrime1_1() {
        NaturalNumber n = new NaturalNumber2(5);
        boolean result = CryptoUtilities.isPrime1(n);

        assertEquals(true, result);

    }

    /*
     * Test case to verify if the number 4 is prime using the
     * CryptoUtilities.isPrime1 method. Expected result is false because 4 is
     * not prime number.
     */

    @Test
    public void testIsPrime1_2() {
        NaturalNumber n = new NaturalNumber2(4);
        boolean result = CryptoUtilities.isPrime1(n);

        assertEquals(false, result);
    }

    /*
     * Test case to verify if the number 5 is prime using the
     * CryptoUtilities.isPrime2 method. Expected result is true because 5 is
     * prime number.
     */

    @Test
    public void testIsPrime2_1() {
        NaturalNumber n = new NaturalNumber2(5);
        boolean result = CryptoUtilities.isPrime2(n);

        assertEquals(true, result);
    }

    /*
     * Test case to verify if the number 4 is prime using the
     * CryptoUtilities.isPrime2 method. Expected result is false because 4 is
     * prime number.
     */

    @Test
    public void testIsPrime2_2() {
        NaturalNumber n = new NaturalNumber2(4);
        boolean result = CryptoUtilities.isPrime2(n);

        assertEquals(false, result);
    }

}
