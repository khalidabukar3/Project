import java.io.Serializable;
import java.util.Comparator;

import components.map.Map;
import components.map.Map1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.sortingmachine.SortingMachine;
import components.sortingmachine.SortingMachine1L;

/**
 * This program is designed to create a tag-cloud generator, as described in
 * Project: Tag Cloud Generator.
 *
 * @author Khalid Abukar
 *
 */
public final class TagCloudGenerator {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private TagCloudGenerator() {
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
         * of separator set
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
    public static Map<String, Integer> mapFromInputLines(SimpleReader input) {
        assert input != null : "Violation of: input is not null";
        assert input.isOpen() : "Violation of: input.is_open";

        // Initialize new set to hold all input lines.
        Map<String, Integer> inputLinesMap = new Map1L<>();

        // Initialize separators set. Add various different separators of words.
        Set<Character> separators = new Set1L<>();
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
         * While we're not at the end of stream, initialize currentLine to the
         * current line of the file. Start at position 0, and go all the way
         * until you reach the length of currentLine. Repeat this for all the
         * lines in the input file.
         */
        while (!input.atEOS()) {
            String currentLine = input.nextLine();
            int position = 0;
            while (position < currentLine.length()) {
                /*
                 * Call wordOrSeparator method repeatedly to return either a
                 * full word or full separator from the description/value, as a
                 * lower case value.
                 */
                String wordOrSeparator = nextWordOrSeparator(currentLine,
                        position, separators).toLowerCase();
                /*
                 * If the wordOrSeparator is not a separator, then check if the
                 * term is already in inputLinesMap. If it isn't, add new
                 * Map.Pair with Integer count of 1.
                 */
                if (!separators.contains(wordOrSeparator.charAt(0))) {
                    if (!inputLinesMap.hasKey(wordOrSeparator)) {
                        inputLinesMap.add(wordOrSeparator, 1);
                        /*
                         * If term is already in inputLinesMap, then remove the
                         * pair with that term name from the Map, increment the
                         * Integer value of the pair, and re-add it to the Map.
                         */
                    } else {
                        Map.Pair<String, Integer> currentTerm = inputLinesMap
                                .remove(wordOrSeparator);
                        inputLinesMap.add(currentTerm.key(),
                                currentTerm.value() + 1);
                    }
                }
                /*
                 * Increment position to traverse through each term and
                 * separator on each line.
                 */
                position += wordOrSeparator.length();
            }
        }
        // Return the Map inputLinesMap.
        return inputLinesMap;

    }

    /**
     *
     * Returns a SortingMachine, holding each Map.Pair<String, Integer> of
     * mapToSort. This is based on the alphabetical order of the keys in each
     * Map.Pair of the input Map. This order is generated by using a created
     * String comparator.
     *
     * @param mapToSort
     *            The map of keys (terms) and values (counts) that is used for
     *            sorting.
     *
     * @return The generated SortingMachine<Map.Pair<<String, Integer>>
     *         (keysAndValues) from the Map mapToSort.
     *
     * @requires mapToSort is not null.
     *
     * @ensures Output SortingMachine contains the keys and values of mapToSort.
     */
    public static SortingMachine<Map.Pair<String, Integer>> sortingKeys(
            Map<String, Integer> mapToSort) {
        // Create temp Map variable as new instance, and transferFrom mapToSort
        Map<String, Integer> temp = mapToSort.newInstance();
        temp.transferFrom(mapToSort);
        /*
         * Initialize keysAndValues to be a new SortingMachine which will hold
         * each Map.Pair of mapToSort.
         */
        SortingMachine<Map.Pair<String, Integer>> keysAndValues = new SortingMachine1L<>(
                new StringLT());
        /*
         * While temp still has elements, remove random Map.Pair from temp and
         * initialize termPlusDef. Add the pair to keysAndValues. Add the keys
         * and values back to mapToSort to restore it.
         */
        while (temp.size() > 0) {
            Map.Pair<String, Integer> termPlusDef = temp.removeAny();
            keysAndValues.add(termPlusDef);
            mapToSort.add(termPlusDef.key(), termPlusDef.value());
        }
        /*
         * Change to extraction mode so you can already extract pairs initially.
         */
        keysAndValues.changeToExtractionMode();
        return keysAndValues;
    }

    /**
     *
     * Modifies initialMap to only hold the top "numOfWords" Map.Pairs from the
     * original initialMap. This is based on the order of the values in each
     * Map.Pair of the initialMap. This order is generated by using a created
     * Integer comparator.
     *
     * @param initialMap
     *            The map of keys (terms) and values (counts) that is used
     *            initially, to be modified so it only has the top "numOfWords"
     *            Map.Pairs, based on an Integer comparator of each pairs'
     *            values.
     * @param numOfWords
     *            The number of terms (keys) to keep in the initialMap upon the
     *            method's completion.
     *
     * @requires mapToSort is not null
     *
     * @ensures Upon method completion, initialMap contains the top "numOfWords"
     *          Map.Pairs from the input Map.
     */
    public static void findTopNKeys(Map<String, Integer> initialMap,
            int numOfWords) {
        /*
         * SortingMachine of Map.Pairs is instantiated, using an integer
         * comparator for order.
         */
        SortingMachine<Map.Pair<String, Integer>> sort = new SortingMachine1L<>(
                new IntegerLT());
        // Add each Map.Pair to the SortingMachine.
        while (initialMap.size() > 0) {
            sort.add(initialMap.removeAny());
        }
        sort.changeToExtractionMode();
        /*
         * For "numOfWords" times, remove the first Map.Pair (the largest
         * value/count) and initialize termPlusDef. Then, add the key and value
         * back to initialMap.
         */
        for (int i = 0; i < numOfWords; i++) {
            Map.Pair<String, Integer> termPlusDef = sort.removeFirst();
            initialMap.add(termPlusDef.key(), termPlusDef.value());
        }
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader inFromConsole = new SimpleReader1L();
        SimpleWriter outToConsole = new SimpleWriter1L();

        // Ask for input file and initialize inputFile
        outToConsole.print("Please enter the name of an input file: ");
        String inputFile = inFromConsole.nextLine();
        // Ask for output folder and initialize outputFile.
        outToConsole.print("Please enter the name of an output file: ");
        String outputFile = inFromConsole.nextLine();
        /*
         * Ask for number of words to be included in tag cloud repeatedly until
         * valid input is given, and initialize numberOfWords.
         */
        int numberOfWords = 0;
        boolean isDigit = false;
        while (!isDigit) {
            outToConsole.print(
                    "Please enter the number of words to be included in the tag cloud: ");
            String numWordsStr = inFromConsole.nextLine();
            isDigit = true;
            for (int i = 0; i < numWordsStr.length(); i++) {
                if (!Character.isDigit(numWordsStr.charAt(i))) {
                    isDigit = false;
                }
            }
            if (isDigit) {
                numberOfWords = Integer.parseInt(numWordsStr);
            } else {
                outToConsole.print("Invalid! Try again. ");
            }
        }

        /*
         * inFromFile reads input from specified file, and outToFile writes
         * output to specified html file.
         */
        SimpleReader inFromFile = new SimpleReader1L(inputFile);
        SimpleWriter outToFile = new SimpleWriter1L(outputFile);
        /*
         * Initialize termAndCount to the resulting map from a call to
         * mapFromInputLines, which reads from specified input file.
         */
        Map<String, Integer> termAndCount = mapFromInputLines(inFromFile);
        // Call findTopNKeys to reduce termAndCount to a total of "numberOfWords" pairs.
        findTopNKeys(termAndCount, numberOfWords);
        // Instantiate a SortingMachine of Map.Pairs by calling sortingKeys.
        SortingMachine<Map.Pair<String, Integer>> sorted = sortingKeys(
                termAndCount);

        // Opening tag of an HTML document
        outToFile.println("<html>");

        // Opening tag of a head
        outToFile.println("<head>");

        // Opens title, prints name of input file and additional text, and closes title.
        outToFile.println("<title>Top " + numberOfWords + " words in "
                + inputFile + "</title>");

        // Additional formatting adjustments in correspondence to OSU.
        outToFile.println(
                "<link href=\"http://web.cse.ohio-state.edu/software/2231/web-sw2/"
                        + "assignments/projects/tag-cloud-generator/data/tagcloud.css\" "
                        + "rel=\"stylesheet\" type=\"text/css\">");
        outToFile.print(
                "<link href=\"tagcloud.css\" rel=\"stylesheet\" type=\"text/css\">");
        // Closes the header
        outToFile.println("</head>");
        // Opens the body
        outToFile.println("<body>");
        // Opens h2, prints name of input file and additional text, and closes title.
        outToFile.println("<h2>Top " + numberOfWords + " words in " + inputFile
                + "</h2>");
        // Open second section with horizontal line divider
        outToFile.println("<hr>");
        // Opening a div to format words correctly.
        outToFile.println("<div class=\"cdiv\">");
        // A paragraph enclosed in a box.
        outToFile.println("<p class=\"cbox\">");
        // Establish max and min font sizes for words.
        final int maxSize = 48;
        final int minSize = 11;
        /*
         * Determine the total maximum and minimum count of any word, which
         * corresponds to a Map.Pair, in the SortingMachine.
         */
        int min = 0;
        int max = 0;
        for (Map.Pair<String, Integer> pair : sorted) {
            if (min == 0) {
                min = pair.value();
            }
            if (pair.value() > max) {
                max = pair.value();
            } else if (pair.value() < min) {
                min = pair.value();
            }
        }
        while (sorted.size() > 0) {
            /*
             * Single Map.Pair, starting from smallest alphabetically is a
             * result of removingFirst from sorted.
             */
            Map.Pair<String, Integer> single = sorted.removeFirst();
            /*
             * Multipler that determines what percentile a particular word's
             * count is, relative to the count of the other words.
             */
            double multiplier = (double) (single.value() - min) / (max - min);
            /*
             * Font size uses the multipler to determine what font the word
             * should have, based on all possible font sizes.
             */
            int fontSize = (int) (multiplier * (maxSize - minSize)) + minSize;
            // Print word with correct font and additional formatting concerns.
            outToFile.println("<span style=\"cursor:default\" class=\"f"
                    + fontSize + "\" title=\"count: " + single.value() + "\">"
                    + single.key().toLowerCase() + "</span>");
        }
        // Close paragraph
        outToFile.println("</p>");
        // Close div
        outToFile.println("</div>");
        // Close body
        outToFile.println("</body>");
        // Close HTML file
        outToFile.println("</html>");
        // Print success generation message.
        outToConsole.println("HTML file successfully generated!");
        /*
         * Close input and output streams
         */
        inFromConsole.close();
        outToConsole.close();
        inFromFile.close();
        outToFile.close();
    }

    /**
     * Comparator for Strings, implementing Comparator<Map.Pair<String,
     * Integer>> and overriding compare method.
     */
    @SuppressWarnings("serial")
    private static class StringLT
            implements Comparator<Map.Pair<String, Integer>>, Serializable {
        @Override
        public int compare(Map.Pair<String, Integer> o1,
                Map.Pair<String, Integer> o2) {
            return o1.key().compareToIgnoreCase(o2.key());
        }
    }

    /**
     * Comparator for Integer, implementing Comparator<Map.Pair<String,
     * Integer>> and overriding compare method.
     */
    @SuppressWarnings("serial")
    private static class IntegerLT
            implements Comparator<Map.Pair<String, Integer>>, Serializable {
        @Override
        public int compare(Map.Pair<String, Integer> o1,
                Map.Pair<String, Integer> o2) {
            return o2.value().compareTo(o1.value());
        }
    }
}
