import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.utilities.Reporter;
import components.xmltree.XMLTree;
import components.xmltree.XMLTree1;

/**
 * Program to evaluate XMLTree expressions of {@code int}.
 *
 * @author Khalid Abukar
 *
 */
public final class XMLTreeIntExpressionEvaluator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private XMLTreeIntExpressionEvaluator() {
    }

    /**
     * Evaluate the given expression.
     *
     * @param exp
     *            the {@code XMLTree} representing the expression
     * @return the value of the expression
     * @requires <pre>
     * [exp is a subtree of a well-formed XML arithmetic expression]  and
     *  [the label of the root of exp is not "expression"]
     * </pre>
     * @ensures evaluate = [the value of the expression]
     */
    private static int evaluate(XMLTree exp) {
        assert exp != null : "Violation of: exp is not null";

        // Initial result.
        int result = 0;

        /*
         * Check if the element is a number, if so parse it directly to result.
         * Then assigns the numeric value.
         */

        if (exp.label().equals("number") && exp.hasAttribute("value")) {
            result = Integer.parseInt(exp.attributeValue("value"));
        }

        // Evaluate addition if the element's label is "plus" and .
        if (exp.label().equals("plus")) {
            // Recursively evaluate the children of the 'plus' node and add the results.
            result = evaluate(exp.child(0)) + evaluate(exp.child(1));
        }

        /*
         * Evaluate subtraction if the element's label is "minus".
         *
         * Recursively evaluate the children of the 'minus' node and updates the
         * results.
         */

        if (exp.label().equals("minus")) {
            result += evaluate(exp.child(0)) - evaluate(exp.child(1));
        }

        /*
         * Evaluate multiplication if the element's label is times.
         *
         * Recursively evaluate the children of the 'times' node and updates the
         * results.
         */

        if (exp.label().equals("times")) {
            result += evaluate(exp.child(0)) * evaluate(exp.child(1));
        }

        /*
         * Evaluate division if the element's label is divide.
         *
         * If the divisor is non-zero, perform the division operation.
         */

        if (exp.label().equals("divide")) {

            int divisor = evaluate(exp.child(1));
            if (divisor == 0) {
                Reporter.fatalErrorToConsole("Cannot divide by 0");
            }

            result = evaluate(exp.child(0)) / evaluate(exp.child(1));
        }

        // Return the computed result.
        return result;

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

        out.print("Enter the name of an expression XML file: ");
        String file = in.nextLine();
        while (!file.equals("")) {
            XMLTree exp = new XMLTree1(file);
            out.println(evaluate(exp.child(0)));
            out.print("Enter the name of an expression XML file: ");
            file = in.nextLine();
        }

        in.close();
        out.close();
    }

}
