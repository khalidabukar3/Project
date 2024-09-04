import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumberSecondary;

/**
 * {@code NaturalNumber} represented as a {@code String} with implementations of
 * primary methods.
 *
 * @convention <pre>
 * [all characters of $this.rep are '0' through '9']  and
 * [$this.rep does not start with '0']
 * </pre>
 * @correspondence <pre>
 * this = [if $this.rep = "" then 0
 *         else the decimal number whose ordinary depiction is $this.rep]
 * </pre>
 *

 * @author K. Abukar
 *
 */
public class NaturalNumber3 extends NaturalNumberSecondary {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Representation of {@code this}.
     */
    private String rep;

    /**
     * Creator of initial representation.
     */
    private void createNewRep() {
        this.rep = "";
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public NaturalNumber3() {
        /*
         * Call createNewRep to make String representation of NaturalNumber with
         * default value.
         */
        this.createNewRep();
    }

    /**
     * Constructor from {@code int}.
     *
     * @param i
     *            {@code int} to initialize from
     */
    public NaturalNumber3(int i) {
        assert i >= 0 : "Violation of: i >= 0";
        /*
         * If not zero, then represent NaturalNumber as the int in String form.
         * Otherwise, represent NaturalNumber by an empty String (createNewRep).
         */
        if (i != 0) {
            this.rep = String.valueOf(i);
        } else {
            this.createNewRep();
        }
    }

    /**
     * Constructor from {@code String}.
     *
     * @param s
     *            {@code String} to initialize from
     */
    public NaturalNumber3(String s) {
        assert s != null : "Violation of: s is not null";
        assert s.matches("0|[1-9]\\d*") : ""
                + "Violation of: there exists n: NATURAL (s = TO_STRING(n))";
        /*
         * If the string is not "0", then represent it as the string. Otherwise,
         * createNewRep to make an empty String representation.
         */
        if (!s.equals("0")) {
            this.rep = s;
        } else {
            this.createNewRep();
        }
    }

    /**
     * Constructor from {@code NaturalNumber}.
     *
     * @param n
     *            {@code NaturalNumber} to initialize from
     */
    public NaturalNumber3(NaturalNumber n) {
        assert n != null : "Violation of: n is not null";
        /*
         * If the NaturalNumber isn't zero, then represent it by its number in
         * String form. Otherwise, represent it as an empty String.
         */
        if (!n.isZero()) {
            this.rep = n.toString();
        } else {
            this.createNewRep();
        }
    }

    /*
     * Standard methods -------------------------------------------------------
     */

    @Override
    public final NaturalNumber newInstance() {
        try {
            return this.getClass().getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Cannot construct object of type " + this.getClass());
        }
    }

    @Override
    public final void clear() {
        this.createNewRep();
    }

    @Override
    public final void transferFrom(NaturalNumber source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof NaturalNumber3 : ""
                + "Violation of: source is of dynamic type NaturalNumberExample";
        /*
         * This cast cannot fail since the assert above would have stopped
         * execution in that case.
         */
        NaturalNumber3 localSource = (NaturalNumber3) source;
        this.rep = localSource.rep;
        localSource.createNewRep();
    }

    /*
     * Kernel methods ---------------------------------------------------------
     */

    @Override
    public final void multiplyBy10(int k) {
        assert 0 <= k : "Violation of: 0 <= k";
        assert k < RADIX : "Violation of: k < 10";
        /*
         * Multiply by 10 by concatenating k to end of String iff. k and the
         * current String are not both zero.
         */
        if (!(k == 0 && this.rep.isEmpty())) {
            this.rep += k;
        }
    }

    @Override
    public final int divideBy10() {
        // Default return value of 0.
        int returned = 0;
        /*
         * If the String is not empty, then returned is set equal to the last
         * character in the String (last digit), coverted to an integer using
         * Character.getNumericValue.
         */
        if (!this.rep.isEmpty()) {
            returned = Character
                    .getNumericValue(this.rep.charAt(this.rep.length() - 1));
            /*
             * The String becomes a substring of itself, not including the last
             * character (the final digit).
             */
            this.rep = this.rep.substring(0, this.rep.length() - 1);
        }
        return returned;
    }

    @Override
    public final boolean isZero() {
        // Check if String is empty, which represents zero.
        return this.rep.isEmpty();
    }
}
