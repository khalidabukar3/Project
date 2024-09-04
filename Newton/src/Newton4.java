import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * This program allows users to specify desired precision and continuously
 * accept new inputs until a negative number is entered.
 *
 *
 * @author Khalid Abukar
 *
 */
public final class Newton4 {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private Newton4() {
    }

    /**
     * Computes estimate of square root of x to within relative error 0.01%.
     *
     * @param x
     *            positive number to compute square root of
     * @param error
     *            positive number to error
     * @return estimate of square root
     */
    private static double sqrt(double x, double error) {

        double beta = x;

        // Iterative process to refine the square root estimate
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

        // It inputs new real number to square root method.
        out.print("Enter a positive real number to compute its square root: ");
        double result = in.nextDouble();

        // Loop to continuously accept user input until a negative number is entered.
        while (result >= 0) {

            out.print("Enter value of ε: ");
            double error = in.nextDouble();

            // Compute and display the square root
            double squareRoot = sqrt(result, error);
            if (squareRoot >= 0) {
                out.println(
                        "The Square Root of " + result + " = " + squareRoot);

            }

            // It inputs new real number to square root method.
            out.println();
            out.print(
                    "Enter a positive real number to compute its square root: ");
            result = in.nextDouble();

        }

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
