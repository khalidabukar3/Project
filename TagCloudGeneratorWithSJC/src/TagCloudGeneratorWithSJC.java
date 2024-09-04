import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * This program is designed to create a tag-cloud generator, as described in
 * Project: Tag Cloud Generator.
 *
 * @author K. Abukar
 *
 */
public final class TagCloudGeneratorWithSJC {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private TagCloudGeneratorWithSJC() {
    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    public static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        int p = position;
        /*
         * isSeperator determines if the character at position p in text is part
         * of separator set.
         */
        boolean isSeparator = separators.contains(text.charAt(p));
        /*
         * While within text length and each character is still its initial
         * condition (either a separator or not), keep incrementing p.
         */
        while (p < text.length()
                && separators.contains(text.charAt(p)) == isSeparator) {
            p++;
        }
        /*
         * Return substring of text from starting position to position reached
         * after ending loop p.
         */
        return text.substring(position, p);
    }

    /**
     * Returns a Map of the lines read from {@code input}, where the key (term)
     * is a 1-word, 1-line String, and the value (count) is an Integer. The map
     * cannot have duplicate keys (terms).
     *
     * @param input
     *            source of strings
     * @return Map of several terms and their corresponding number of
     *         occurrences read from the lines of {@code input}, where each term
     *         is separated by a separator character in the input file.
     * @requires input.is_open
     * @ensures <pre>
     * input.is_open  and  input.content = <>  and
     * linesFromInput = [maximal Map of different lines from #input.content such that
     *                   CONTAINS_NO_DUPLICATE_KEYS(linesFromInput)]
     * </pre>
     */
    public static Map<String, Integer> mapFromInputLines(BufferedReader input) {
        assert input != null : "Violation of: input is not null";

        // Initialize new Map to hold all input lines.
        Map<String, Integer> inputLinesMap = new HashMap<>();
        try {
            // Initialize separators set. Add various different separators of words.
            Set<Character> separators = new HashSet<>();
            separators.add(' ');
            separators.add('\t');
            separators.add('\n');
            separators.add('\r');
            separators.add(',');
            separators.add('-');
            separators.add('.');
            separators.add('!');
            separators.add('?');
            separators.add('[');
            separators.add(']');
            separators.add('\'');
            separators.add(';');
            separators.add(':');
            separators.add('/');
            separators.add('\\');
            separators.add('(');
            separators.add(')');
            separators.add('_');
            /*
             * First, initialize currentLine to the current line of the file.
             * Then, while currentLine is not null, start at position 0, and go
             * all the way until you reach the length of currentLine. Repeat
             * this for all the lines in the input file.
             */
            String currentLine = input.readLine();
            while (currentLine != null) {
                int position = 0;
                while (position < currentLine.length()) {
                    /*
                     * Call wordOrSeparator method repeatedly to return either a
                     * full word or full separator from the description/value,
                     * as a lower case value to ensure only one key is created
                     * per letter.
                     */
                    String wordOrSeparator = nextWordOrSeparator(currentLine,
                            position, separators).toLowerCase();
                    /*
                     * If the wordOrSeparator is not a separator, then check if
                     * the term is already in inputLinesMap. If it isn't, add a
                     * new Map.Entry with Integer count of 1.
                     */
                    if (!separators.contains(wordOrSeparator.charAt(0))) {
                        if (!inputLinesMap.containsKey(wordOrSeparator)) {
                            inputLinesMap.put(wordOrSeparator, 1);
                            /*
                             * If term is already in inputLinesMap, then remove
                             * the entry with that term name from the Map, and
                             * initialize an Integer to hold the old value
                             * returned. Then, re-put the entry into the Map and
                             * increment the old value.
                             */
                        } else {
                            Integer oldValue = inputLinesMap
                                    .remove(wordOrSeparator);
                            inputLinesMap.put(wordOrSeparator, oldValue + 1);
                        }
                    }
                    /*
                     * Increment position to traverse through each term and
                     * separator on each line.
                     */
                    position += wordOrSeparator.length();
                }
                // Reinitialize currentLine to be the next line.
                currentLine = input.readLine();
            }
            // Catch any IO exception from reading the input file.
        } catch (IOException e) {
            System.err.println("ERROR: File could not be read.");
        }
        return inputLinesMap;
    }

    /**
     *
     * Returns an ArrayList, holding each Map.Entry<String, Integer> of
     * mapToSort. This is based on the alphabetical order of the keys in each
     * Map.Entry of the input Map. This order is generated by using a created
     * String comparator.
     *
     * @param mapToSort
     *            The map of keys (terms) and values (counts) that is used for
     *            sorting.
     *
     * @return The generated ArrayList<Map.Entry<<String, Integer>>
     *         (keysAndValues) from the Map mapToSort.
     *
     * @requires mapToSort is not null.
     *
     * @ensures The output ArrayList contains the keys and values of mapToSort.
     */
    public static ArrayList<Map.Entry<String, Integer>> sortingKeys(
            Map<String, Integer> mapToSort) {
        // Create temp Map variable as new instance, and transferFrom mapToSort
        Map<String, Integer> temp = new HashMap<>();
        temp.putAll(mapToSort);
        mapToSort.clear();
        /*
         * Initialize keysAndValues to be a new ArrayList which will hold each
         * Map.Entry of mapToSort.
         */
        ArrayList<Map.Entry<String, Integer>> keysAndValues = new ArrayList<>();
        /*
         * While temp still has elements, remove a random Map.Entry from temp
         * and initialize termPlusDef. Add the entry to keysAndValues. Add the
         * keys and values back to mapToSort to restore it.
         */
        for (Map.Entry<String, Integer> entry : temp.entrySet()) {
            keysAndValues.add(entry);
            mapToSort.put(entry.getKey(), entry.getValue());
        }
        keysAndValues.sort(new StringLT());
        return keysAndValues;
    }

    /**
     *
     * Modifies initialMap to only hold the top "numOfWords" Map.Entry from the
     * original initialMap. This is based on the order of the values in each
     * Map.Entry of the initialMap. This order is generated by using a created
     * Integer comparator.
     *
     * @param initialMap
     *            The map of keys (terms) and values (counts) that is used
     *            initially, to be modified so it only has the top "numOfWords"
     *            Map.Entry(s), based on an Integer comparator of each entrys'
     *            values.
     * @param numOfWords
     *            The number of terms (keys) to keep in the initialMap upon the
     *            method's completion.
     *
     * @requires initialMap is not null
     *
     * @ensures Upon method completion, initialMap contains the top "numOfWords"
     *          Map.Entry(s) from the input Map.
     */
    public static void findTopNKeys(Map<String, Integer> initialMap,
            int numOfWords) {
        /*
         * ArrayList of Map.Entry is instantiated, using an integer comparator
         * for order.
         */
        ArrayList<Map.Entry<String, Integer>> keysAndValues = new ArrayList<>();
        // Add each Map.Entry to the ArrayList.
        for (Map.Entry<String, Integer> entry : initialMap.entrySet()) {
            keysAndValues.add(entry);
        }
        initialMap.clear();
        /*
         * For "numOfWords" times, remove the first Map.Entry (the largest
         * value/count) and initialize termPlusDef. Then, add the key and value
         * back to initialMap.
         */
        keysAndValues.sort(new IntegerLT());
        for (int i = 0; i < numOfWords; i++) {
            if (keysAndValues.size() > 0) {
                Map.Entry<String, Integer> termPlusDef = keysAndValues
                        .remove(0);
                initialMap.put(termPlusDef.getKey(), termPlusDef.getValue());
            }
        }
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        // Declare input and output reader and writers using java IO.
        Scanner inFromConsole = new Scanner(System.in);
        BufferedReader inFromFile;
        PrintWriter outToFile;
        /*
         * Initialize variable to hold number of words (terms) user wants in
         * final tag cloud.
         */
        int numWords = 0;
        // Ask for input file and initialize inputFile, catching opening exceptions.
        System.out.print("Please enter the name of an input file: ");
        String inputFile = inFromConsole.nextLine();
        try {
            inFromFile = new BufferedReader(new FileReader(inputFile));
        } catch (IOException e) {
            System.err.println("ERROR: File cannot be opened.");
            inFromConsole.close();
            return;
        }
        // Ask for number and initialize numWords, catching non-integer exceptions.
        System.out.print(
                "Please enter the number of words to be included in the tag cloud: ");
        try {
            numWords = inFromConsole.nextInt();
        } catch (InputMismatchException e1) {
            System.err.print("ERROR: Non-integer number entered.");
            // Catch more exceptions in trying to close the readers.
            try {
                inFromConsole.close();
                inFromFile.close();
            } catch (IOException e2) {
                System.err.println("ERROR: Reader cannot be closed.");
                return;
            }
            return;
        }

        /*
         * Initialize termAndCount to the resulting map from a call to
         * mapFromInputLines, which reads from specified input file.
         */
        Map<String, Integer> termAndCount = mapFromInputLines(inFromFile);
        // Call findTopNKeys to reduce termAndCount to a total of "numberOfWords" entrys.
        findTopNKeys(termAndCount, numWords);
        // Instantiate an ArrayList of Map.Entry by calling sortingKeys.
        ArrayList<Map.Entry<String, Integer>> sortedList = sortingKeys(
                termAndCount);
        // Establish max and min font sizes for words.
        final int maxSize = 48;
        final int minSize = 11;
        // Determine the total max and min count (value) of any word (term) in sortedList.
        int min = 0;
        int max = 0;
        for (Map.Entry<String, Integer> entry : sortedList) {
            if (min == 0) {
                min = entry.getValue();
            }
            if (entry.getValue() > max) {
                max = entry.getValue();
            } else if (entry.getValue() < min) {
                min = entry.getValue();
            }
        }
        // Get output folder and initialize outputFile, catching closing exceptions.
        System.out.print("Please enter the name of an output file: ");
        inFromConsole.nextLine();
        String outputFile = inFromConsole.nextLine();
        try {
            outToFile = new PrintWriter(
                    new BufferedWriter(new FileWriter(outputFile)));
        } catch (IOException e) {
            System.err.println("ERROR: File cannot be written to.");
            inFromConsole.close();
            return;
        }

        // Opening tag of an HTML document.
        outToFile.println("<html>");
        // Opening tag of a head.
        outToFile.println("<head>");
        // Opens title, prints name of input file and additional text, and closes title.
        outToFile.println("<title>Top " + numWords + " words in " + inputFile
                + "</title>");
        // Additional formatting adjustments in correspondence to OSU.
        outToFile.println(
                "<link href=\"http://web.cse.ohio-state.edu/software/2231/web-sw2/"
                        + "assignments/projects/tag-cloud-generator/data/tagcloud.css\" "
                        + "rel=\"stylesheet\" type=\"text/css\">");
        outToFile.print(
                "<link href=\"tagcloud.css\" rel=\"stylesheet\" type=\"text/css\">");
        // Closes the header.
        outToFile.println("</head>");
        // Opens the body.
        outToFile.println("<body>");
        // Opens h2, prints name of input file and additional text, and closes h2.
        outToFile.println(
                "<h2>Top " + numWords + " words in " + inputFile + "</h2>");
        // Add a horizontal line divider.
        outToFile.println("<hr>");
        // Opening a div to format words correctly.
        outToFile.println("<div class=\"cdiv\">");
        // A paragraph enclosed in a box.
        outToFile.println("<p class=\"cbox\">");

        while (sortedList.size() > 0) {
            /*
             * Single Map.Entry, starting from smallest alphabetically; a result
             * of removing the first element from sortedList.
             */
            Map.Entry<String, Integer> single = sortedList.remove(0);
            /*
             * Multipler that determines what percentile a particular word's
             * count is, relative to the count of the other words.
             */
            double multiplier = (double) (single.getValue() - min)
                    / (max - min);
            /*
             * fontSize uses the multipler to determine what font the word
             * should have, based on all possible font sizes.
             */
            int fontSize = (int) (multiplier * (maxSize - minSize)) + minSize;
            // Print word with correct font and additional formatting concerns.
            outToFile.println("<span style=\"cursor:default\" class=\"f"
                    + fontSize + "\" title=\"count: " + single.getValue()
                    + "\">" + single.getKey() + "</span>");
        }
        // Closes paragraph.
        outToFile.println("</p>");
        // Closes div.
        outToFile.println("</div>");
        // Closes body.
        outToFile.println("</body>");
        // Closes HTML file.
        outToFile.println("</html>");
        // Print successful generation message.
        System.out.println("HTML file successfully generated!");
        /*
         * Close input and output streams, catching reader closing exceptions.
         */
        try {
            inFromConsole.close();
            inFromFile.close();
            outToFile.close();
        } catch (IOException e) {
            System.err.println("ERROR: Reader could not be closed.");
            outToFile.close();
            return;
        }
    }

    /**
     * Comparator for Strings, implementing Comparator<Map.Entry<String,
     * Integer>> and overriding compare method.
     */
    @SuppressWarnings("serial")
    private static class StringLT
            implements Comparator<Map.Entry<String, Integer>>, Serializable {
        @Override
        public int compare(Map.Entry<String, Integer> o1,
                Map.Entry<String, Integer> o2) {
            return o1.getKey().compareToIgnoreCase(o2.getKey());
        }
    }

    /**
     * Comparator for Integer, implementing Comparator<Map.Entry<String,
     * Integer>> and overriding compare method.
     */
    @SuppressWarnings("serial")
    private static class IntegerLT
            implements Comparator<Map.Entry<String, Integer>>, Serializable {
        @Override
        public int compare(Map.Entry<String, Integer> o1,
                Map.Entry<String, Integer> o2) {
            return o2.getValue().compareTo(o1.getValue());
        }
    }
}
