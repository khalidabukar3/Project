import java.util.Comparator;

import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * A program that reads text, counts the word repeated, and also outputs HTML
 * table.
 *
 * @author Khalid Abukar
 *
 */

public final class WordCounter {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private WordCounter() {
        // no code needed here
    }

    /**
     * Comparator for strings that is case-insensitive.
     */

    public static class CaseInsensitiveComparator
            implements Comparator<String> {

        /**
         * Compares two strings, first ignoring case considerations. If two
         * strings are equal ignoring case, it then compares them with case
         * sensitivity.
         *
         * @param s1
         *            the string to be compared.
         * @param s2
         *            the second string to be compared
         * @return a negative integer, zero, or a positive integer.
         *
         */

        @Override
        public int compare(String s1, String s2) {
            int result = s1.compareToIgnoreCase(s2);

            if (result == 0) {
                result = s2.compareTo(s1);
            }
            return result;
        }

    }

    /**
     * Sorts the words in the given map by their words in a case-insensitive
     * manner.
     *
     * @param wordFrequencyMap
     *            the map containing words and their respective counts.
     * @return a queue containing the sorted words.
     */

    public static Queue<String> sortWords(
            Map<String, Integer> wordFrequencyMap) {

        Queue<String> sortedWordsQueue = new Queue1L<>();

        Map<String, Integer> duplicateMap = wordFrequencyMap.newInstance();

        duplicateMap.transferFrom(wordFrequencyMap);

        // Extract words from the map, and add them to the queue for sorting.

        while (duplicateMap.size() > 0) {

            Map.Pair<String, Integer> wordCountPair = duplicateMap.removeAny();

            sortedWordsQueue.enqueue(wordCountPair.key());

            wordFrequencyMap.add(wordCountPair.key(), wordCountPair.value());
        }

        // Sort the queue using a case-insensitive comparator.

        CaseInsensitiveComparator caseComp = new CaseInsensitiveComparator();

        sortedWordsQueue.sort(caseComp);

        return sortedWordsQueue;
    }

    /**
     * Counts the occurrences of each word from a given SimpleReader input. It
     * splits the input into words and tracks the frequency of each word in a
     * map.
     *
     * @param input
     *            source for reading textual content.
     * @return a map containing words and their corresponding counts.
     */

    public static Map<String, Integer> countWords(SimpleReader input) {

        Map<String, Integer> wordFrequencyMap = new Map1L<>();

        while (!input.atEOS()) {

            String line = input.nextLine();

            String[] wordsInLine = line.split("\\W+");

            // Process each line of the input to count word frequencies.

            for (String word : wordsInLine) {

                String trimmedWord = word.trim();

                if (!trimmedWord.isEmpty()) {
                    if (wordFrequencyMap.hasKey(trimmedWord)) {
                        int currentCount = wordFrequencyMap.value(trimmedWord);

                        wordFrequencyMap.replaceValue(trimmedWord,
                                currentCount + 1);

                    } else {
                        wordFrequencyMap.add(trimmedWord, 1);
                    }
                }
            }

        }

        return wordFrequencyMap;

    }

    /**
     * Outputs word counts as an HTML document. Generates an HTML file
     * displaying a table of words and their counts, sorted alphabetically.
     *
     * @param out
     *            Destination writer for HTML output.
     * @param wordFrequencyMap
     *            Word counts map.
     * @param inputFileName
     *            Name of the input file to be displayed in the HTML header.
     */

    public static void writeOutput(SimpleWriter out,
            Map<String, Integer> wordFrequencyMap, String inputFileName) {

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Word Counts</title>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h2>Words Counted in " + inputFileName + "</h2>");
        out.println("<hr />");

        out.println("<table border=\"1\">");
        out.println("<tr><th>Words</th><th>Counts</th></tr>");

        // Generate a table row for each word and its count.

        Queue<String> sortedWordsQueue = sortWords(wordFrequencyMap);

        while (sortedWordsQueue.length() > 0) {
            String currentWord = sortedWordsQueue.dequeue();
            int wordCount = wordFrequencyMap.value(currentWord);
            out.println("<tr>");
            out.println("<td>" + currentWord + "</td>");
            out.println("<td>" + wordCount + "</td>");
            out.println("</tr>");

        }

        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Main method for the word counting application.
     *
     * This method prompts the user for input and output file names, counts the
     * words in the input file, and writes the word counts to the output file in
     * HTML format.
     *
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        // User input for source and destination files.

        out.print("Enter the name of the input file: ");
        String fileNameInput = in.nextLine();

        Map<String, Integer> wordCounts = countWords(
                new SimpleReader1L(fileNameInput));

        out.print("Enter the name of the output file: ");
        String fileNameOutput = in.nextLine();

        // Generate and write output to the specified file.

        writeOutput(new SimpleWriter1L(fileNameOutput), wordCounts,
                fileNameInput);

        in.close();
        out.close();

    }

}
