import components.queue.Queue;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.statement.Statement;
import components.statement.Statement1;
import components.utilities.Reporter;
import components.utilities.Tokenizer;

/**
 * Layered implementation of secondary methods {@code parse} and
 * {@code parseBlock} for {@code Statement}.
 *
 * @author K. Abukar
 *
 */
public final class Statement1Parse1 extends Statement1 {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Converts {@code c} into the corresponding {@code Condition}.
     *
     * @param c
     *            the condition to convert
     * @return the {@code Condition} corresponding to {@code c}
     * @requires [c is a condition string]
     * @ensures parseCondition = [Condition corresponding to c]
     */
    private static Condition parseCondition(String c) {
        assert c != null : "Violation of: c is not null";
        assert Tokenizer
                .isCondition(c) : "Violation of: c is a condition string";
        return Condition.valueOf(c.replace('-', '_').toUpperCase());
    }

    /**
     * Parses an IF or IF_ELSE statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires <pre>
     * [<"IF"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [an if string is a proper prefix of #tokens] then
     *  s = [IF or IF_ELSE Statement corresponding to if string at start of #tokens]  and
     *  #tokens = [if string at start of #tokens] * tokens
     * else
     *  [reports an appropriate error message to the console and terminates client]
     * </pre>
     */
    private static void parseIf(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && tokens.front().equals("IF") : ""
                + "Violation of: <\"IF\"> is proper prefix of tokens";
        // Remove initial IF token, already checked in the assert.
        tokens.dequeue();
        // New statement
        Statement s1 = s.newInstance();
        /*
         * Make sure next token is a condition to follow IF pattern; if not,
         * report an error. If so, parse correctly.
         */
        String condition = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isCondition(condition),
                "ERROR: IF statement does not have a valid condition");
        Condition c = parseCondition(condition);
        /*
         * Make sure next token is "THEN" to follow IF pattern; if not, report
         * an error.
         */
        String then = tokens.dequeue();
        Reporter.assertElseFatalError(then.equals("THEN"),
                "ERROR: expected THEN, but found " + then);
        // Parse IF statement block and check what the proceeding token is
        s1.parseBlock(tokens);
        String nextToken = tokens.dequeue();
        String endToken = "";
        // If nextToken is "ELSE", then parse the second block and assemble IfElse.
        if (nextToken.equals("ELSE")) {
            Statement s2 = s.newInstance();
            s2.parseBlock(tokens);
            s.assembleIfElse(c, s1, s2);
            endToken = tokens.dequeue();
            // Otherwise, assemble If.
        } else {
            s.assembleIf(c, s1);
            endToken = tokens.dequeue();
        }
        // endToken should be "END", and final token should be "IF".
        Reporter.assertElseFatalError(endToken.equals("END"),
                "ERROR: expected END, but found: " + endToken);
        String postIf = tokens.dequeue();
        Reporter.assertElseFatalError(postIf.equals("IF"),
                "ERROR: expected IF, but found: " + postIf);
    }

    /**
     * Parses a WHILE statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires <pre>
     * [<"WHILE"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [a while string is a proper prefix of #tokens] then
     *  s = [WHILE Statement corresponding to while string at start of #tokens]  and
     *  #tokens = [while string at start of #tokens] * tokens
     * else
     *  [reports an appropriate error message to the console and terminates client]
     * </pre>
     */
    private static void parseWhile(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && tokens.front().equals("WHILE") : ""
                + "Violation of: <\"WHILE\"> is proper prefix of tokens";
        // Dequeue because already checked in assert
        tokens.dequeue();
        // New statement
        Statement s1 = s.newInstance();
        /*
         * Make sure next token is a condition to follow WHILE pattern; if not,
         * report an error. If so, parse correctly.
         */
        String condition = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isCondition(condition),
                "ERROR: expected CONDITION, but found: " + condition);
        Condition c = parseCondition(condition);
        String doWhile = tokens.dequeue();
        // Check that next token is "DO", otherwise report error
        Reporter.assertElseFatalError(doWhile.equals("DO"),
                "ERROR: expected DO, but found " + doWhile);
        // Parse while statement block
        s1.parseBlock(tokens);
        s.assembleWhile(c, s1);
        // Check for correct last two while tokens, otherwise report errors
        String endWhile = tokens.dequeue();
        Reporter.assertElseFatalError(endWhile.equals("END"),
                "ERROR: expected END, but found: " + endWhile);
        String postWhile = tokens.dequeue();
        Reporter.assertElseFatalError(postWhile.equals("WHILE"),
                "ERROR: expected WHILE, but found: " + postWhile);
    }

    /**
     * Parses a CALL statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires [identifier string is a proper prefix of tokens]
     * @ensures <pre>
     * s =
     *   [CALL Statement corresponding to identifier string at start of #tokens]  and
     *  #tokens = [identifier string at start of #tokens] * tokens
     * </pre>
     */
    private static void parseCall(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0
                && Tokenizer.isIdentifier(tokens.front()) : ""
                        + "Violation of: identifier string is proper prefix of tokens";
        /*
         * Simply assemble the call using the next token as the name, since
         * validity is already checked in the assert.
         */
        s.assembleCall(tokens.dequeue());
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Statement1Parse1() {
        super();
    }

    /*
     * Public methods ---------------------------------------------------------
     */

    @Override
    public void parse(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";
        switch (tokens.front()) {
            case "WHILE": {
                // Parse WHILE statement
                Statement s1 = new Statement1();
                parseWhile(tokens, s1);
                this.transferFrom(s1);
                break;
            }
            case "IF": {
                // Parse IF statement
                Statement s1 = new Statement1();
                parseIf(tokens, s1);
                this.transferFrom(s1);
                break;
            }
            default: {
                // Default: Parse CALL statement
                Statement s1 = new Statement1();
                parseCall(tokens, s1);
                this.transferFrom(s1);
                break;
            }
        }
    }

    @Override
    public void parseBlock(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";
        // Initially clear this so it can hold the result of the parsed block.
        this.clear();
        // While end of input has not been reached, continually parse tokens.
        while (!tokens.front().equals("### END OF INPUT ###")) {
            Statement current = this.newInstance();
            current.parse(tokens);
            this.addToBlock(this.lengthOfBlock(), current);
        }
    }

    /*
     * Main test method -------------------------------------------------------
     */

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
         * Get input file name
         */
        out.print("Enter valid BL statement(s) file name: ");
        String fileName = in.nextLine();
        /*
         * Parse input file
         */
        out.println("*** Parsing input file ***");
        Statement s = new Statement1Parse1();
        SimpleReader file = new SimpleReader1L(fileName);
        Queue<String> tokens = Tokenizer.tokens(file);
        file.close();
        s.parse(tokens); // replace with parseBlock to test other method
        /*
         * Pretty print the statement(s)
         */
        out.println("*** Pretty print of parsed statement(s) ***");
        s.prettyPrint(out, 0);

        in.close();
        out.close();
    }

}
