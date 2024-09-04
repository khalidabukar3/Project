import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * @author Khalid Abukar
 */

public class StringReassemblyTest {

    // Test for combination():
    @Test
    public void testCombination0() {
        String str1 = "alpha";
        String str2 = "alphamale";
        String result = StringReassembly.combination(str1, str2, 5);
        assertEquals(result, str2);
    }

    // Test for combination1():
    @Test
    public void testCombination1() {
        String str1 = "";
        String str2 = "";

        String result = StringReassembly.combination(str1, str2, 0);

        assertEquals(result, str2);

    }

    // Test for combination2():
    @Test
    public void testCombination2() {
        String str1 = "alpha";
        String str2 = "alpha";
        String result = StringReassembly.combination(str1, str2, 5);
        assertEquals(result, str2);
    }

    // Test for printWithLineSeparator():
    public void testPrintWithLineSeparator() {
        SimpleReader in = new SimpleReader1L("data/Cheer-8-2.txt");
        SimpleWriter out = new SimpleWriter1L("data/Cheer-8-2.txt");
        StringReassembly.printWithLineSeparators("Bucks ~ Beat", out);

        String answer = in.nextLine();
        String answer2 = in.nextLine();
        assertEquals(answer, "Bucks ");
        assertEquals(answer2, " Beat");

        in.close();
        out.close();
    }

    // Test for printWithLineSeparator2():
    @Test
    public void testPrintWithLineSeparator2() {
        SimpleReader in = new SimpleReader1L("data/Cheer-8-2.txt");
        SimpleWriter out = new SimpleWriter1L("data/Cheer-8-2.txt");
        StringReassembly.printWithLineSeparators("Bucks Beat", out);

        String answer = in.nextLine();
        assertEquals(answer, "Bucks Beat");
        in.close();
        out.close();
    }

    // Test for testLinefrominput():
    @Test
    public void testLinefrominput() {
        SimpleReader in = new SimpleReader1L("data/declaration-50-8.txt");
        Set<String> setOne = StringReassembly.linesFromInput(in);
        Set<String> setTwo = new Set1L<>();
        setTwo.add("Khalid");
        setTwo.add("Abukar");
        assertEquals(setOne, setTwo);
    }

    // Test for testLinefrominput2():
    @Test
    public void testLinefrominput2() {
        SimpleReader in = new SimpleReader1L("data/BMW.txt");
        Set<String> setOne = StringReassembly.linesFromInput(in);
        Set<String> setTwo = new Set1L<>();
        setTwo.add("Khalid");
        assertEquals(setOne, setTwo);
    }

    // Test for testLinefrominput3():
    @Test
    public void testLinefrominput3() {
        SimpleReader in = new SimpleReader1L("data/gettysburg-30-4.txt");
        Set<String> setOne = StringReassembly.linesFromInput(in);
        Set<String> setTwo = new Set1L<>();
        setTwo.add("al");
        setTwo.add("Alpha");
        assertEquals(setOne, setTwo);
    }

    // Test for testAddToSetAvoidingSubstrings0():
    @Test
    public void testAddToSetAvoidingSubstrings0() {
        Set<String> setOne = new Set1L<>();
        setOne.add("alp");
        StringReassembly.addToSetAvoidingSubstrings(setOne, "Abukar");
        Set<String> setTwo = new Set1L<>();
        setTwo.add("Abukar");
        setTwo.add("alp");
        assertEquals(setOne, setTwo);
    }

    // Test for testAddToSetAvoidingSubstrings1():
    @Test
    public void testAddToSetAvoidingSubstrings1() {
        Set<String> setOne = new Set1L<>();
        setOne.add("Khalid");
        StringReassembly.addToSetAvoidingSubstrings(setOne, "id");
        Set<String> setTwo = new Set1L<>();
        setTwo.add("Khalid");
        assertEquals(setOne, setTwo);

    }

    // Test for testAddToSetAvoidingSubstrings2():
    @Test
    public void testAddToSetAvoidingSubstrings2() {
        Set<String> setOne = new Set1L<>();
        setOne.add("id");
        StringReassembly.addToSetAvoidingSubstrings(setOne, "Khalid");
        Set<String> setTwo = new Set1L<>();
        setTwo.add("Khalid");
        assertEquals(setOne, setTwo);
    }
}
