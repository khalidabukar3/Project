import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;
import components.random.Random;
import components.random.Random1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Utilities that could be used with RSA cryptosystems.
 *
 * @author Khalid Abukar
 *
 */
public final class CryptoUtilities {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private CryptoUtilities() {
    }

    /**
     * Useful constant, not a magic number: 3.
     */
    private static final int THREE = 3;

    /**
     * Pseudo-random number generator.
     */
    private static final Random GENERATOR = new Random1L();

    /**
     * Returns a random number uniformly distributed in the interval [0, n].
     *
     * @param n
     *            top end of interval
     * @return random number in interval
     * @requires n > 0
     * @ensures <pre>
     * randomNumber = [a random number uniformly distributed in [0, n]]
     * </pre>
     */
    public static NaturalNumber randomNumber(NaturalNumber n) {
        assert !n.isZero() : "Violation of: n > 0";
        final int base = 10;
        NaturalNumber result;
        int d = n.divideBy10();
        if (n.isZero()) {
            /*
             * Incoming n has only one digit and it is d, so generate a random
             * number uniformly distributed in [0, d]
             */
            int x = (int) ((d + 1) * GENERATOR.nextDouble());
            result = new NaturalNumber2(x);
            n.multiplyBy10(d);
        } else {
            /*
             * Incoming n has more than one digit, so generate a random number
             * (NaturalNumber) uniformly distributed in [0, n], and another
             * (int) uniformly distributed in [0, 9] (i.e., a random digit)
             */
            result = randomNumber(n);
            int lastDigit = (int) (base * GENERATOR.nextDouble());
            result.multiplyBy10(lastDigit);
            n.multiplyBy10(d);
            if (result.compareTo(n) > 0) {
                /*
                 * In this case, we need to try again because generated number
                 * is greater than n; the recursive call's argument is not
                 * "smaller" than the incoming value of n, but this recursive
                 * call has no more than a 90% chance of being made (and for
                 * large n, far less than that), so the probability of
                 * termination is 1
                 */
                result = randomNumber(n);
            }
        }
        return result;
    }

    /**
     * Finds the greatest common divisor of n and m.
     *
     * @param n
     *            one number
     * @param m
     *            the other number
     * @updates n
     * @clears m
     * @ensures n = [greatest common divisor of #n and #m]
     */
    public static void reduceToGCD(NaturalNumber n, NaturalNumber m) {

        // Create a new instance of NaturalNumber and copy the current value of n into it.
        NaturalNumber temp = n.newInstance();
        temp.copyFrom(n);

        // if m is zero, then reset n to its original value.
        if (m.isZero()) {
            n.copyFrom(temp);
        } else {

            // If m is not zero, create another temporary NaturalNumber.
            NaturalNumber temp2 = n.newInstance();
            temp2.copyFrom(n);

            // Divide temp2 by m and store the result back in temp2.
            NaturalNumber result = temp2.divide(m);

            if (result.isZero()) {
                n.transferFrom(m);
            } else {
                // If the result is not zero, compute the GCD of m and result.
                reduceToGCD(m, result);
                n.transferFrom(m); // transfer the GCD value from m to n.
            }
        }

    }

    /**
     * Reports whether n is even.
     *
     * @param n
     *            the number to be checked
     * @return true iff n is even
     * @ensures isEven = (n mod 2 = 0)
     */
    public static boolean isEven(NaturalNumber n) {

        // Initialize a boolean variable to hold the result.
        boolean num = false;

        // Divide the number by 10
        int result = n.divideBy10();

        // If the last digit of the number is even, set to true.
        if (result % 2 == 0) {
            num = true;
        }

        // Multiply the number by 10
        n.multiplyBy10(result);

        // Return the boolean result and shows whether the number is even or not.
        return num;
    }

    /**
     * Updates n to its p-th power modulo m.
     *
     * @param n
     *            number to be raised to a power
     * @param p
     *            the power
     * @param m
     *            the modulus
     * @updates n
     * @requires m > 1
     * @ensures n = #n ^ (p) mod m
     */
    public static void powerMod(NaturalNumber n, NaturalNumber p,
            NaturalNumber m) {
        assert m.compareTo(new NaturalNumber2(1)) > 0 : "Violation of: m > 1";

        // Initialize NaturalNumber instances and set values for one and two.
        NaturalNumber one = n.newInstance();
        one.setFromInt(1);
        NaturalNumber two = n.newInstance();
        two.setFromInt(2);

        // Copy n to temp for later use.
        NaturalNumber temp = n.newInstance();
        temp.copyFrom(n);

        // Check if the power 'p' is zero.
        if (p.isZero()) {
            n.setFromInt(1);

            // If p is greater than one, perform the powerMod operation.
        } else if (p.compareTo(one) > 0) {

            // Create a new instance to hold the halved exponent.
            NaturalNumber result = p.newInstance();
            result.copyFrom(p);
            result.divide(two);
            powerMod(n, result, m); // Recursively call powerMod.

            // Calculate n squared.
            NaturalNumber half = n.newInstance();
            half.copyFrom(n);
            n.multiply(half);

            // If p is odd, multiply by n again.
            if (!isEven(p)) {
                n.multiply(temp);
            }
        }

        // Divide n by m and update n to the remainder.
        NaturalNumber copyNum = n.divide(m);
        n.copyFrom(copyNum);

    }

    /**
     * Reports whether w is a "witness" that n is composite, in the sense that
     * either it is a square root of 1 (mod n), or it fails to satisfy the
     * criterion for primality from Fermat's theorem.
     *
     * @param w
     *            witness candidate
     * @param n
     *            number being checked
     * @return true iff w is a "witness" that n is composite
     * @requires n > 2 and 1 < w < n - 1
     * @ensures <pre>
     * isWitnessToCompositeness =
     *     (w ^ 2 mod n = 1)  or  (w ^ (n-1) mod n /= 1)
     * </pre>
     */
    public static boolean isWitnessToCompositeness(NaturalNumber w,
            NaturalNumber n) {
        assert n.compareTo(new NaturalNumber2(2)) > 0 : "Violation of: n > 2";
        assert (new NaturalNumber2(1)).compareTo(w) < 0 : "Violation of: 1 < w";
        n.decrement();
        assert w.compareTo(n) < 0 : "Violation of: w < n - 1";
        n.increment();

        // Initialize result as false.
        boolean result = false;

        // NaturalNumber instances for constant values one and two.
        NaturalNumber one = n.newInstance();
        one.setFromInt(1);
        NaturalNumber two = n.newInstance();
        two.setFromInt(2);

        // unit will store the value n mines 1.
        NaturalNumber unit = n.newInstance();
        unit.copyFrom(n);
        unit.decrement();

        // Make copies of w for the modular calculations.
        NaturalNumber temp = n.newInstance();
        temp.copyFrom(w);
        NaturalNumber tempTwo = n.newInstance();
        tempTwo.copyFrom(w);

        // Perform powerMod operations.
        powerMod(temp, unit, n);
        powerMod(tempTwo, two, n);

        // Check if w is a witness.
        if (temp.compareTo(one) != 0 || tempTwo.compareTo(one) == 0) {
            result = true;
        }

        // Return true if w is a witness, otherwise false.
        return result;

    }

    /**
     * Reports whether n is a prime; may be wrong with "low" probability.
     *
     * @param n
     *            number to be checked
     * @return true means n is very likely prime; false means n is definitely
     *         composite
     * @requires n > 1
     * @ensures <pre>
     * isPrime1 = [n is a prime number, with small probability of error
     *         if it is reported to be prime, and no chance of error if it is
     *         reported to be composite]
     * </pre>
     */
    public static boolean isPrime1(NaturalNumber n) {
        assert n.compareTo(new NaturalNumber2(1)) > 0 : "Violation of: n > 1";
        boolean isPrime;
        if (n.compareTo(new NaturalNumber2(THREE)) <= 0) {
            /*
             * 2 and 3 are primes
             */
            isPrime = true;
        } else if (isEven(n)) {
            /*
             * evens are composite
             */
            isPrime = false;
        } else {
            /*
             * odd n >= 5: simply check whether 2 is a witness that n is
             * composite (which works surprisingly well :-)
             */
            isPrime = !isWitnessToCompositeness(new NaturalNumber2(2), n);
        }
        return isPrime;
    }

    /**
     * Reports whether n is a prime; may be wrong with "low" probability.
     *
     * @param n
     *            number to be checked
     * @return true means n is very likely prime; false means n is definitely
     *         composite
     * @requires n > 1
     * @ensures <pre>
     * isPrime2 = [n is a prime number, with small probability of error
     *         if it is reported to be prime, and no chance of error if it is
     *         reported to be composite]
     * </pre>
     */
    public static boolean isPrime2(NaturalNumber n) {
        assert n.compareTo(new NaturalNumber2(1)) > 0 : "Violation of: n > 1";

        // Initialize result as false.
        boolean result = false;

        // Set number of iterations for the primality test.
        final int temp = 50;
        int low = temp;

        // Create a NaturalNumber instance of n-1.
        NaturalNumber base = n.newInstance();
        base.copyFrom(n);
        base.decrement();

        // Create a NaturalNumber instance with value one.
        NaturalNumber one = n.newInstance();
        one.setFromInt(1);

        // Check if n is less than or equal to 3.
        if (n.compareTo(new NaturalNumber2(THREE)) <= 0) {
            result = true;
            // If n is even, it's not prime.
        } else if (isEven(n)) {
            result = false;
        } else {
            // If n is greater than 3 and odd, test to check for primality.
            while (low >= 0 && !result) {

                // Generate a random number less than n-1
                NaturalNumber num = randomNumber(base);

                // Check if num is a witness to the compositeness of n
                if (num.compareTo(base) < 0 && num.compareTo(one) > 0) {
                    result = !isWitnessToCompositeness(num, n);
                }
                low = low - 1;

            }
        }

        // Return true if n is likely prime, otherwise false.
        return result;

    }

    /**
     * Generates a likely prime number at least as large as some given number.
     *
     * @param n
     *            minimum value of likely prime
     * @updates n
     * @requires n > 1
     * @ensures n >= #n and [n is very likely a prime number]
     */
    public static void generateNextLikelyPrime(NaturalNumber n) {
        assert n.compareTo(new NaturalNumber2(1)) > 0 : "Violation of: n > 1";

        // Initialize result as false.
        boolean result = false;

        // Increment n to start searching for the next likely prime.
        n.increment();

        // Loop until a likely prime is found.
        while (!result) {
            // If n is even, increment once to make it odd.
            if (isEven(n)) {
                n.increment();
            } else {
                // If n is already odd, increment by 2 to skip even numbers.
                n.increment();
                n.increment();
            }
            // Check if the current n is a likely prime.
            result = isPrime2(n);
        }

    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        /*
         * Sanity check of randomNumber method -- just so everyone can see how
         * it might be "tested"
         */
        final int testValue = 17;
        final int testSamples = 100000;
        NaturalNumber test = new NaturalNumber2(testValue);
        int[] count = new int[testValue + 1];
        for (int i = 0; i < count.length; i++) {
            count[i] = 0;
        }
        for (int i = 0; i < testSamples; i++) {
            NaturalNumber rn = randomNumber(test);
            assert rn.compareTo(test) <= 0 : "Help!";
            count[rn.toInt()]++;
        }
        for (int i = 0; i < count.length; i++) {
            out.println("count[" + i + "] = " + count[i]);
        }
        out.println("  expected value = "
                + (double) testSamples / (double) (testValue + 1));

        /*
         * Check user-supplied numbers for primality, and if a number is not
         * prime, find the next likely prime after it
         */
        while (true) {
            out.print("n = ");
            NaturalNumber n = new NaturalNumber2(in.nextLine());
            if (n.compareTo(new NaturalNumber2(2)) < 0) {
                out.println("Bye!");
                break;
            } else {
                if (isPrime1(n)) {
                    out.println(n + " is probably a prime number"
                            + " according to isPrime1.");
                } else {
                    out.println(n + " is a composite number"
                            + " according to isPrime1.");
                }
                if (isPrime2(n)) {
                    out.println(n + " is probably a prime number"
                            + " according to isPrime2.");
                } else {
                    out.println(n + " is a composite number"
                            + " according to isPrime2.");
                    generateNextLikelyPrime(n);
                    out.println("  next likely prime is " + n);
                }
            }
        }

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
