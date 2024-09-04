import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;
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
public final class XMLTreeNNExpressionEvaluator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private XMLTreeNNExpressionEvaluator() {
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
    private static NaturalNumber evaluate(XMLTree exp) {
        assert exp != null : "Violation of: exp is not null";

        // Create a NaturalNumber instance to hold the result of the expression.
        NaturalNumber resultNum = new NaturalNumber2(0);

        // Check if the current XMLTree node represents a number.
        if (exp.label().equals("number") && exp.hasAttribute("value")) {
            // set resultNum to the value of this number.
            resultNum.setFromString(exp.attributeValue("value"));
        }

        // Check if the current XMLTree node represents an addition operation
        if (exp.label().equals("plus")) {
            // evaluate both operands and add the results
            NaturalNumber valueNum = resultNum.newInstance();
            valueNum.copyFrom(evaluate(exp.child(0)));
            valueNum.add(evaluate(exp.child(1)));

            // Copy the addition result back to resultNum
            resultNum.copyFrom(valueNum);
        }

        // Check if the current XMLTree node represents a subtraction operation
        if (exp.label().equals("minus")) {
            // evaluate both operands and perform the subtraction.
            NaturalNumber valueNum = resultNum.newInstance();
            NaturalNumber valueNum1 = resultNum.newInstance();

            valueNum.copyFrom(evaluate(exp.child(0)));
            valueNum1.copyFrom(evaluate(exp.child(1)));

            /*
             * If the second operand is greater, report a fatal error since we
             * can't have a negative NaturalNumber.
             *
             * Perform the subtraction and update resultNum.
             */

            if (valueNum1.compareTo(valueNum) > 0) {
                Reporter.fatalErrorToConsole(
                        "NaturalNumber can't be negative.");
            }

            resultNum.add(valueNum);
            valueNum.subtract(valueNum1);

        }

        // Check if the current XMLTree node represents a multiplication operation.
        if (exp.label().equals("times")) {
            //  evaluate both operands and multiply the results.
            NaturalNumber valueNum = resultNum.newInstance();
            valueNum.copyFrom(evaluate(exp.child(0)));
            valueNum.multiply(evaluate(exp.child(1)));

            // Update resultNum.
            resultNum.add(valueNum);
        }

        // Check if the current XMLTree node represents a division operation.
        if (exp.label().equals("divide")) {
            // evaluate both operands and perform the division.
            NaturalNumber valueNum = resultNum.newInstance();
            NaturalNumber zeroNum = resultNum.newInstance();
            NaturalNumber divisor = resultNum.newInstance();

            valueNum.copyFrom(evaluate(exp.child(0)));
            divisor.copyFrom(evaluate(exp.child(1)));

            // If attempting to divide by zero, report a fatal error.
            if (divisor.compareTo(zeroNum) == 0) {
                Reporter.fatalErrorToConsole("Cannot divide by 0");
            }

            // Perform the division and update resultNum.
            valueNum.divide(divisor);
            resultNum.add(valueNum);
        }

        // return the NaturalNumber result.
        return resultNum;
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
