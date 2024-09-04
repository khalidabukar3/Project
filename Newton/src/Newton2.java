import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * This program calculates the estimated square root of x to within relative
 * error 0.01%.
 *
 * @author Khalid Abukar
 *
 */
public final class Newton2 {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private Newton2() {
    }

    /**
     * Computes estimate of square root of x to within relative error 0.01%.
     *
     * @param x
     *            positive number to compute square root of
     * @return estimate of square root
     */
    private static double sqrt(double x) {

        double beta = x;
        final double error = 0.001; // Relative error threshold.

        // Iterative computation of the square root.

        if (beta != 0) {
            while (!(Math.abs(beta * beta - x) / x < error * error)) {
                beta = (beta + x / beta) / 2;

            }

        }

        return beta;

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

        // Ask if the user wants to perform a calculation.
        out.print("Do you want to calculate square root (y/n)? ");
        String result = in.nextLine();

        while (result.charAt(0) == 'y') {

            out.print(
                    "Enter a positive real number to compute its square root: ");
            double answer = in.nextDouble();

            // Compute and display the square root.

            double squareRoot = sqrt(answer);
            out.println("The Square Root of " + answer + " = " + squareRoot);

            // Ask if the user wants to perform another calculation.
            out.print("Do you want to calculate another square root (y/n)? ");
            result = in.nextLine();

        }

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
