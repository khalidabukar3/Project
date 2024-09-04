import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.util.Comparator;

import org.junit.Test;

import components.sortingmachine.SortingMachine;

/**
 * JUnit test fixture for {@code SortingMachine<String>}'s constructor and
 * kernel methods.
 *
 * @author K. Abukar
 *
 */
public abstract class SortingMachineTest {

    /**
     * Invokes the appropriate {@code SortingMachine} constructor for the
     * implementation under test and returns the result.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @return the new {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures constructorTest = (true, order, {})
     */
    protected abstract SortingMachine<String> constructorTest(
            Comparator<String> order);

    /**
     * Invokes the appropriate {@code SortingMachine} constructor for the
     * reference implementation and returns the result.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @return the new {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures constructorRef = (true, order, {})
     */
    protected abstract SortingMachine<String> constructorRef(
            Comparator<String> order);

    /**
     *
     * Creates and returns a {@code SortingMachine<String>} of the
     * implementation under test type with the given entries and mode.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @param insertionMode
     *            flag indicating the machine mode
     * @param args
     *            the entries for the {@code SortingMachine}
     * @return the constructed {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * createFromArgsTest = (insertionMode, order, [multiset of entries in args])
     * </pre>
     */
    private SortingMachine<String> createFromArgsTest(Comparator<String> order,
            boolean insertionMode, String... args) {
        SortingMachine<String> sm = this.constructorTest(order);
        for (int i = 0; i < args.length; i++) {
            sm.add(args[i]);
        }
        if (!insertionMode) {
            sm.changeToExtractionMode();
        }
        return sm;
    }

    /**
     *
     * Creates and returns a {@code SortingMachine<String>} of the reference
     * implementation type with the given entries and mode.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @param insertionMode
     *            flag indicating the machine mode
     * @param args
     *            the entries for the {@code SortingMachine}
     * @return the constructed {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * createFromArgsRef = (insertionMode, order, [multiset of entries in args])
     * </pre>
     */
    private SortingMachine<String> createFromArgsRef(Comparator<String> order,
            boolean insertionMode, String... args) {
        SortingMachine<String> sm = this.constructorRef(order);
        for (int i = 0; i < args.length; i++) {
            sm.add(args[i]);
        }
        if (!insertionMode) {
            sm.changeToExtractionMode();
        }
        return sm;
    }

    /**
     * Comparator<String> implementation to be used in all test cases. Compare
     * {@code String}s in lexicographic order.
     */

    private static class StringLT implements Comparator<String>, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public int compare(String s1, String s2) {
            return s1.compareToIgnoreCase(s2);
        }

    }

    /**
     * Comparator instance to be used in all test cases.
     */
    private static final StringLT ORDER = new StringLT();

    /**
     * Sample test cases.
     */
    @Test
    public final void testConstructor() {
        SortingMachine<String> m = this.constructorTest(ORDER);
        SortingMachine<String> mExpected = this.constructorRef(ORDER);
        assertEquals(mExpected, m);
    }

    /**
     * Add test with initially empty SortingMachine in insertion mode.
     */
    @Test
    public final void testAddEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green");
        m.add("green");
        assertEquals(mExpected, m);
    }

    /**
     * Verifies that multiple elements can be added to the sorting machine in
     * insertion mode.
     */
    @Test
    public final void testAddMany() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green", "ah", "oh", "blue", "red");
        m.add("green");
        m.add("ah");
        m.add("oh");
        m.add("blue");
        m.add("red");
        assertEquals(mExpected, m);
    }

    /**
     * Tests adding an element to a non-empty sorting machine.
     */
    @Test
    public final void testAddMethod() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true,
                "green");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green", "blue");
        m.add("blue");
        assertEquals(mExpected, m);
    }

    /**
     * Tests changing the machine from insertion to extraction mode with no
     * elements.
     */
    @Test
    public final void testChangeToExtractionMode() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        m.changeToExtractionMode();
        assertEquals(mExpected, m);
    }

    /**
     * Tests changing to extraction mode with elements present.
     */
    @Test
    public final void testChangeToExtractionMode2() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "ah",
                "oh");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "oh", "ah");
        m.changeToExtractionMode();
        assertEquals(mExpected.isInInsertionMode(), m.isInInsertionMode());
        assertEquals(mExpected, m);
    }

    /**
     * Tests removing the first element in extraction mode with multiple
     * elements.
     */
    @Test
    public final void testRemoveFirstSome() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "green", "blue", "purple");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "blue", "purple");
        m.removeFirst();
        assertEquals(mExpected, m);
    }

    /**
     * Tests removing the first element in extraction mode with a lot of
     * elements, checking edge case of comparison.
     */
    @Test
    public final void testRemoveFirstMany() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "green", "blue", "purple", "dog", "apple", "able", "adam",
                "aab", "aaa");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "green", "blue", "purple", "dog", "apple", "able", "adam",
                "aab", "aaa");
        String mRemoved = m.removeFirst();
        String mExpectedRemoved = mExpected.removeFirst();
        assertEquals(mExpectedRemoved, mRemoved);
        assertEquals(mExpected, m);

    }

    /**
     * Tests removing the first element when only one element is present.
     */
    @Test
    public final void testRemoveFirstEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "green");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "green");
        String mRemoved = m.removeFirst();
        String mExpectedRemoved = mExpected.removeFirst();
        assertEquals(mExpectedRemoved, mRemoved);
        assertEquals(mExpected, m);

    }

    /**
     * Test the sorting machine correctly reports being in insertion mode.
     */
    @Test
    public final void testInsertionMode1() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true);
        assertEquals(true, m.isInInsertionMode());
        assertEquals(mExpected, m);
    }

    /**
     * Confirms the sorting machine correctly identifies it is not in insertion
     * mode.
     */
    @Test
    public final void insertionModeTest2() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false);
        assertEquals(false, m.isInInsertionMode());

    }

    /**
     * Tests the size method on an empty machine in extraction mode.
     */
    @Test
    public final void testSizeEmptyExtraction() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        assertEquals(0, m.size());
        assertEquals(mExpected, m);

    }

    /**
     * Tests the size method on an empty machine in insertion mode.
     */
    @Test
    public final void testSizeEmptyInsertion() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        assertEquals(0, m.size());
    }

    /**
     * Tests the size method on a machine with elements in extraction mode.
     */
    @Test
    public final void testSizeExtraction() {
        final int five = 5;
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "green",
                "blue", "red", "purple", "gray");
        m.changeToExtractionMode();
        assertEquals(five, m.size());
    }

    /**
     * Tests the size method on a machine with elements in insertion mode.
     */
    @Test
    public final void testSizeInsertion() {
        final int five = 5;
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "green",
                "blue", "red", "purple", "gray");
        assertEquals(five, m.size());

    }

    /**
     * Tests that the order method returns the correct comparator for non-empty
     * SortingMachine.
     */
    @Test
    public final void testOrderNonEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "green", "blue");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "green", "blue", "red", "purple", "gray");
        assertEquals(ORDER, m.order());
        assertEquals(mExpected, m);
    }

    /**
     * Tests that the order method returns the correct comparator for non-empty
     * SortingMachine.
     */
    @Test
    public final void testOrderEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        assertEquals(mExpected.order(), m.order());
    }

    /**
     * Tests that the order method returns the correct comparator for non-empty
     * SortingMachine.
     */
    @Test
    public final void testOrderInsertion() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true);
        assertEquals(mExpected.order(), m.order());
    }
}
