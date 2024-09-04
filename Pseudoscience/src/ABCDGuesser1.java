import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.utilities.FormatChecker;

/**
 * This program approximates a universal constant u using the deJager formula
 * with four user-provided numbers. It also identifies the exponents a, b, c,
 * and d that minimize the relative error of the approximation.
 *
 * @author Khalid Abukar
 *
 */

public final class ABCDGuesser1 {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private ABCDGuesser1() {
        // no code needed here
    }

    /**
     * Repeatedly asks the user for a positive real number until the user enters
     * one. Returns the positive real number.
     *
     * @param in
     *            the input stream
     * @param out
     *            the output stream
     * @return a positive real number entered by the user
     */

    private static double getPositiveDouble(SimpleReader in, SimpleWriter out) {

        // The loops continues looping until a positive number is entered.

        String usersNumber = "";
        boolean isNumber = false;
        boolean isPositive = false;
        double doubleNumber = 0.0;

        while (!isNumber || !isPositive) {
            out.print("Enter a positive real number:");
            usersNumber = in.nextLine();
            // Check if the input can be parsed as double.

            if (FormatChecker.canParseDouble(usersNumber)) {
                doubleNumber = Double.parseDouble(usersNumber);
                isNumber = true;
                if (doubleNumber >= 0.0) {
                    isPositive = true;
                }
            }
        }

        return doubleNumber; // Return The Positive Real Number Entered by the user.
    }

    /**
     * Repeatedly asks the user for a positive real number not equal to 1.0
     * until the user enters one. Returns the positive real number.
     *
     * @param in
     *            the input stream
     * @param out
     *            the output stream
     * @return a positive real number not equal to 1.0 entered by the user
     */
    private static double getPositiveDoubleNotOne(SimpleReader in,
            SimpleWriter out) {

        /*
         * The loop continues looping until a positive number greater than 0 and
         * not equal to one is entered.
         */

        double doubleNumber = getPositiveDouble(in, out);

        while (Double.compare(doubleNumber, 1.0) == 0) {
            out.println("Enter a positive real number other than 1:");
            doubleNumber = getPositiveDouble(in, out);
        }

        return doubleNumber; // Return a positive double not equal to 1.0
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

        // Get a positive real number from the user for mu.

        double mu = getPositiveDouble(in, out);
        // Getting a positive real number that is not equal to  1.0 for w,x,y,z.
        double w = getPositiveDoubleNotOne(in, out);
        double x = getPositiveDoubleNotOne(in, out);
        double y = getPositiveDoubleNotOne(in, out);
        double z = getPositiveDoubleNotOne(in, out);

        // Array to store deJager values.

        final double[] deJager = { -5, -4, -3, -2, -1, -1.0 / 2.0, -1.0 / 3.0,
                -1.0 / 4.0, 0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 2.0, 1, 2, 3, 4, 5 };

        // initializes the variables

        double bestApproximation = Double.MAX_VALUE;
        // Declare variables

        double pro1 = 0.0;
        double pro2 = 0.0;
        double pro3 = 0.0;
        double pro4 = 0.0;
        int indexA = 0;
        int indexB = 0;
        int indexC = 0;
        int indexD = 0;

        // Loop over all possible values of exponent a, b, c, d in the deJager array

        while (indexA < deJager.length) {

            while (indexB < deJager.length) {

                while (indexC < deJager.length) {

                    while (indexD < deJager.length) {

                        // calculate the approximation
                        double approx = Math.pow(w, deJager[indexA])
                                * Math.pow(x, deJager[indexB])
                                * Math.pow(y, deJager[indexC])
                                * Math.pow(z, deJager[indexD]);

                        // calculate the relative error
                        double difference = Math.abs(mu - approx) / mu;
                        // check the approximation
                        if (difference < bestApproximation) {

                            // updating the best approximation
                            bestApproximation = difference;
                            pro1 = deJager[indexA];
                            pro2 = deJager[indexB];
                            pro3 = deJager[indexC];
                            pro4 = deJager[indexD];
                        }
                        indexD++;
                    }
                    indexC++;
                    indexD = 0;
                }
                indexB++;
                indexC = 0;
            }
            indexA++;
            indexB = 0;
        }
        indexA = 0;

        // Calculating relative error as a percentage
        final double relativeError = Math
                .round((bestApproximation / mu) * 10000) / 100.0;

        // Printing the results
        out.println("Exponents: a= " + pro1 + ", b = " + pro2 + ", c = " + pro3
                + ", d = " + pro4);

        out.println("Approximation: " + Math.pow(w, pro1) * Math.pow(x, pro2)
                * Math.pow(y, pro3) * Math.pow(z, pro4));

        out.println("Relative Error: " + relativeError + "%");

        in.close(); // Close the Input Stream
        out.close(); // Close the output Stream

    }

}
