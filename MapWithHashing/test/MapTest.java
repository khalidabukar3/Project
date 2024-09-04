import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.map.Map;

/**
 * JUnit test fixture for {@code Map<String, String>}'s constructor and kernel
 * methods.
 *
 * @author K. Abukar
 *
 */
public abstract class MapTest {

    /**
     * Invokes the appropriate {@code Map} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new map
     * @ensures constructorTest = {}
     */
    protected abstract Map<String, String> constructorTest();

    /**
     * Invokes the appropriate {@code Map} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new map
     * @ensures constructorRef = {}
     */
    protected abstract Map<String, String> constructorRef();

    /**
     *
     * Creates and returns a {@code Map<String, String>} of the implementation
     * under test type with the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]
     * </pre>
     * @ensures createFromArgsTest = [pairs in args]
     */
    private Map<String, String> createFromArgsTest(String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        Map<String, String> map = this.constructorTest();
        for (int i = 0; i < args.length; i += 2) {
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], args[i + 1]);
        }
        return map;
    }

    /**
     *
     * Creates and returns a {@code Map<String, String>} of the reference
     * implementation type with the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]
     * </pre>
     * @ensures createFromArgsRef = [pairs in args]
     */
    private Map<String, String> createFromArgsRef(String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        Map<String, String> map = this.constructorRef();
        for (int i = 0; i < args.length; i += 2) {
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], args[i + 1]);
        }
        return map;
    }

    /**
     * Test case for hasKey.
     */
    @Test
    public void hasKeyTest() {
        Map<String, String> test = this.createFromArgsTest("hello", "world",
                "this", "is", "a", "test");
        assertEquals(true, test.hasKey("hello"));
    }

    /**
     * Test case for hasKey.
     */
    @Test
    public void hasKeyTest2() {
        Map<String, String> m = this.createFromArgsTest("key", "val", "key1",
                "val1");

        assertEquals(true, m.hasKey("key"));
        assertEquals(true, m.hasKey("key1"));
        assertEquals(false, m.hasKey("key5"));
    }

    /**
     * Test case for hasKey.
     */
    @Test
    public void hasKeyTest3() {
        Map<String, String> test = this.createFromArgsTest("hello", "world",
                "this", "is");
        assertEquals(false, test.hasKey("helllo"));
    }

    /**
     * Test case for hasKey.
     */
    @Test
    public void hasKeyTestEmpty4() {
        Map<String, String> test = this.createFromArgsTest();
        assertEquals(false, test.hasKey("helllo"));
    }

    /**
     * Test for value.
     */
    @Test
    public void valueTest() {
        Map<String, String> test = this.createFromArgsTest("hello", "world",
                "this", "is");
        assertEquals("world", test.value("hello"));
    }

    /**
     * 2nd test for value.
     */
    @Test
    public void valueTest2() {
        Map<String, String> m = this.createFromArgsTest("key", "val", "key1",
                "val1");
        String vEx1 = "val";
        String vEx2 = "val1";
        assertEquals(vEx1, m.value("key"));
        assertEquals(vEx2, m.value("key1"));
    }

    /**
     * Test for remove.
     */
    @Test
    public void removeTest1() {
        Map<String, String> test = this.createFromArgsTest("hello", "world",
                "this", "is");
        Map<String, String> testExpected = this.createFromArgsRef("hello",
                "world", "this", "is");
        Map.Pair<String, String> removed = test.remove("this");
        Map.Pair<String, String> removedExpected = testExpected.remove("this");
        assertEquals(testExpected, test);
        assertEquals(removed, removedExpected);
    }

    /**
     * Test for remove.
     */
    @Test
    public void removeTest2() {
        Map<String, String> m = this.createFromArgsTest("key", "val", "key1",
                "val1");
        Map<String, String> mEx = this.createFromArgsRef("key", "val");

        m.remove("key1");

        assertEquals(mEx, m);
    }

    /**
     * Test for remove.
     */
    @Test
    public void removeTest3() {
        Map<String, String> test = this.createFromArgsTest("hello", "world",
                "this", "is", "a", "test");
        Map<String, String> testExpected = this.createFromArgsRef("hello",
                "world", "this", "is", "a", "test");
        Map.Pair<String, String> removed = test.remove("this");
        Map.Pair<String, String> removedExpected = testExpected.remove("this");
        Map.Pair<String, String> removed2 = test.remove("a");
        Map.Pair<String, String> removedExpected2 = testExpected.remove("a");

        assertEquals(removed, removedExpected);
        assertEquals(removed2, removedExpected2);

        assertEquals(testExpected, test);

    }

    /**
     * Test for remove (empty).
     */
    @Test
    public void removeTest4ToEmpty() {
        Map<String, String> m = this.createFromArgsTest("key", "val", "key1",
                "val1");
        Map<String, String> mEx = this.createFromArgsRef();
        m.remove("key");
        m.remove("key1");

        assertEquals(mEx, m);
    }

    /**
     * Test for removeAny.
     */
    @Test
    public void removeAnyTest1() {
        Map<String, String> test = this.createFromArgsTest("hello", "world",
                "this", "is", "a", "test");
        Map<String, String> testExpected = this.createFromArgsRef("hello",
                "world", "this", "is", "a", "test");
        Map.Pair<String, String> removed = test.removeAny();
        assertEquals(true, testExpected.hasKey(removed.key()));
        testExpected.remove(removed.key());
        assertEquals(testExpected, test);
    }

    /**
     * Test for removeAny.
     */
    @Test
    public void removeAnyTest2() {
        Map<String, String> m = this.createFromArgsTest("key", "val", "key1",
                "val1", "key2", "val2");
        Map<String, String> mEx = this.createFromArgsRef("key", "val", "key1",
                "val1", "key2", "val2");

        Map.Pair<String, String> p1 = m.removeAny();
        Map.Pair<String, String> p2 = m.removeAny();
        assertEquals(true, mEx.hasKey(p1.key()));
        assertEquals(true, mEx.hasKey(p2.key()));

        mEx.remove(p1.key());
        mEx.remove(p2.key());
        assertEquals(mEx, m);
    }

    /**
     * Test for removeAny.
     */
    @Test
    public void removeAnyTest3ToEmpty() {
        Map<String, String> test = this.createFromArgsTest("hello", "world");
        Map<String, String> testExpected = this.createFromArgsRef("hello",
                "world");
        Map.Pair<String, String> removed = test.removeAny();
        assertEquals(true, testExpected.hasKey(removed.key()));
        testExpected.remove(removed.key());
        assertEquals(testExpected, test);
    }

    /**
     * Test for size.
     */
    @Test
    public void sizeTest() {
        final int three = 3;
        Map<String, String> test = this.createFromArgsTest("hello", "world",
                "this", "is", "a", "test");
        assertEquals(three, test.size());
    }

    /**
     * Test for size.
     */
    @Test
    public void sizeTest2() {
        Map<String, String> m = this.createFromArgsTest("key", "val", "key1",
                "val1");
        assertEquals(2, m.size());
    }

    /**
     * Test for size.
     */
    @Test
    public void sizeTestEmpty3() {
        Map<String, String> test = this.createFromArgsTest();
        assertEquals(0, test.size());
    }

    /**
     * Test for add.
     */
    @Test
    public void addTest1() {
        Map<String, String> test = this.createFromArgsTest("hello", "world",
                "this", "is");
        Map<String, String> testExpected = this.createFromArgsRef("hello",
                "world", "this", "is", "a", "test", "imya", "welcome");
        test.add("a", "test");
        test.add("imya", "welcome");
        assertEquals(testExpected, test);

    }

    /**
     * Second test for add.
     */
    @Test
    public void addTest2() {
        Map<String, String> m = this.createFromArgsTest("hello", "world",
                "this", "is");
        Map<String, String> mEx = this.createFromArgsRef("key", "val", "key1",
                "val1");
        m.add("key", "val");
        m.add("key1", "val1");
        mEx.add("hello", "world");
        mEx.add("this", "is");
        assertEquals(mEx, m);
    }

    /**
     * Third test for add.
     */
    @Test
    public void addTest3() {
        Map<String, String> m = this.createFromArgsTest();
        Map<String, String> mEx = this.createFromArgsRef("key", "val", "key1",
                "val1");
        m.add("key", "val");
        m.add("key1", "val1");
        assertEquals(mEx, m);
    }
}
