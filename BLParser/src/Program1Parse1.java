import components.map.Map;
import components.map.Map1L;
import components.program.Program;
import components.program.Program1;
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
 * Layered implementation of secondary method {@code parse} for {@code Program}.
 *
 * @author K. Abukar
 *
 */
public final class Program1Parse1 extends Program1 {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Parses a single BL instruction from {@code tokens} returning the
     * instruction name as the value of the function and the body of the
     * instruction in {@code body}.
     *
     * @param tokens
     *            the input tokens
     * @param body
     *            the instruction body
     * @return the instruction name
     * @replaces body
     * @updates tokens
     * @requires <pre>
     * [<"INSTRUCTION"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [an instruction string is a proper prefix of #tokens]  and
     *    [the beginning name of this instruction equals its ending name]  and
     *    [the name of this instruction does not equal the name of a primitive
     *     instruction in the BL language] then
     *  parseInstruction = [name of instruction at start of #tokens]  and
     *  body = [Statement corresponding to the block string that is the body of
     *          the instruction string at start of #tokens]  and
     *  #tokens = [instruction string at start of #tokens] * tokens
     * else
     *  [report an appropriate error message to the console and terminate client]
     * </pre>
     */
    private static String parseInstruction(Queue<String> tokens,
            Statement body) {
        assert tokens != null : "Violation of: tokens is not null";
        assert body != null : "Violation of: body is not null";
        assert tokens.length() > 0 && tokens.front().equals("INSTRUCTION") : ""
                + "Violation of: <\"INSTRUCTION\"> is proper prefix of tokens";
        // Remove initial token since already checked by assert.
        tokens.dequeue();
        /*
         * Check if next token (name) is not a primitive instruction name,
         * reporting error otherwise.
         */
        String name = tokens.dequeue();
        boolean isPrimitiveInstruction = name.equals("move")
                || name.equals("turnright") || name.equals("turnleft")
                || name.equals("infect") || name.equals("skip");
        Reporter.assertElseFatalError(!isPrimitiveInstruction,
                "ERROR: name cannot match primitive instruction name");
        // Next token should be IS keyword.
        String instructionIs = tokens.dequeue();
        Reporter.assertElseFatalError(instructionIs.equals("IS"),
                "ERROR: expected \"IS\", but found: " + instructionIs);
        /*
         * The next part of tokens should be a block, so clear body and replace
         * body with the result of the block parse.
         */
        body.clear();
        body.parseBlock(tokens);
        // Next token should be END keyword.
        String instructionEnd = tokens.dequeue();
        Reporter.assertElseFatalError(instructionEnd.equals("END"),
                "ERROR: expected END, but found: " + instructionEnd);
        // Next token should be the instruction name repeated. If not, report error.
        String endName = tokens.dequeue();
        Reporter.assertElseFatalError(name.equals(endName),
                "ERROR: expected " + name + ", but found: " + endName);
        return name;
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Program1Parse1() {
        super();
    }

    /*
     * Public methods ---------------------------------------------------------
     */

    @Override
    public void parse(SimpleReader in) {
        assert in != null : "Violation of: in is not null";
        assert in.isOpen() : "Violation of: in.is_open";
        Queue<String> tokens = Tokenizer.tokens(in);
        this.parse(tokens);
    }

    @Override
    public void parse(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";
        String programKeyword = tokens.dequeue();
        Reporter.assertElseFatalError(programKeyword.equals("PROGRAM"),
                "ERROR: expected \"PROGRAM\", but found: " + programKeyword);
        String preProgramName = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isIdentifier(preProgramName),
                "ERROR: Program must begin with an identifier");
        String programIs = tokens.dequeue();
        Reporter.assertElseFatalError(programIs.equals("IS"),
                "ERROR: expected \"IS\", but found: " + programKeyword);
        Map<String, Statement> context = new Map1L<String, Statement>();
        Statement body = new Statement1();
        while (!tokens.front().equals("BEGIN")
                && !tokens.front().equals("### END OF INPUT ###")) {
            Statement currentBody = new Statement1();
            Reporter.assertElseFatalError(tokens.front().equals("INSTRUCTION"),
                    "ERROR: expected \"INSTRUCTION\", but found: "
                            + tokens.front());
            String currentName = parseInstruction(tokens, currentBody);
            Reporter.assertElseFatalError(!context.hasKey(currentName),
                    "ERROR: Duplicate instruction name found");
            context.add(currentName, currentBody);
        }
        String beginKeyword = tokens.dequeue();
        Reporter.assertElseFatalError(beginKeyword.equals("BEGIN"),
                "ERROR: expected \"BEGIN\", but found: " + beginKeyword);
        body.parseBlock(tokens);
        String endKeyword = tokens.dequeue();
        Reporter.assertElseFatalError(endKeyword.equals("END"),
                "ERROR: expected \"END\", but found: " + endKeyword);
        String postProgramName = tokens.dequeue();
        Reporter.assertElseFatalError(postProgramName.equals(preProgramName),
                "ERROR: expected " + preProgramName + ", but found: "
                        + postProgramName);
        String endOfInput = tokens.dequeue();
        Reporter.assertElseFatalError(endOfInput.equals("### END OF INPUT ###"),
                "ERROR: expected \"### END OF INPUT ###\", but found: "
                        + endOfInput);
        this.setName(preProgramName);
        this.swapContext(context);
        this.swapBody(body);
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
        out.print("Enter valid BL program file name: ");
        String fileName = in.nextLine();
        /*
         * Parse input file
         */
        out.println("*** Parsing input file ***");
        Program p = new Program1Parse1();
        SimpleReader file = new SimpleReader1L(fileName);
        Queue<String> tokens = Tokenizer.tokens(file);
        file.close();
        p.parse(tokens);
        /*
         * Pretty print the program
         */
        out.println("*** Pretty print of parsed program ***");
        p.prettyPrint(out);

        in.close();
        out.close();
    }

}
