import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * This program allows users to input a number and their desired precision level
 * for the square root computation.
 *
 *
 * @author Khalid Abukar
 *
 */
public final class Newton3 {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private Newton3() {
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

        // Ask if the user wants to perform a calculation.
        out.print("Do you want to calculate square root (y/n)? ");
        String result = in.nextLine();

        while (result.charAt(0) == 'y') {

            out.print(
                    "Enter a positive real number to compute its square root: ");
            double answer = in.nextDouble();

            out.print("Enter value of ε: ");
            double error = in.nextDouble();

            // Compute and display the square root
            double squareRoot = sqrt(answer, error);
            out.println("The Square Root = " + squareRoot);

            // Ask if the user wants to perform another calculation.
            out.println();
            out.print("Do you want to calculate square root (y/n)? ");
            result = in.nextLine();

        }

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
