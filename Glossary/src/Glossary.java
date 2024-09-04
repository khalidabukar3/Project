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
 * Glossary application for generating HTML glossary pages from a text input.
 *
 * @author Khalid Abukar
 *
 */
public final class Glossary {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private Glossary() {
    }

    /**
     * Comparator class for comparing strings alphabetically.
     */
    public static class TermComparator implements Comparator<String> {

        /**
         * Compares two strings alphabetically, ignoring case considerations.
         *
         * @param term1
         *            the first string to be compared.
         * @param term2
         *            the second string to be compared.
         * @return a negative integer, zero, or a positive integer.
         */

        @Override
        public int compare(String term1, String term2) {
            return term1.compareToIgnoreCase(term2);
        }

    }

    /**
     * Reads terms and their definitions from a file and stores them in a map.
     *
     * @param glossaryMap
     *            the map to store term and definition pairs.
     * @param fileRead
     *            the reader to read the input file.
     */

    public static void parseInputToMap(Map<String, String> glossaryMap,
            SimpleReader fileRead) {
        // Loop until the end of the file is reached.

        while (!fileRead.atEOS()) {
            String term = fileRead.nextLine().trim();
            StringBuilder defBuilder = new StringBuilder();
            String line = fileRead.nextLine();
            // Read definition lines until an empty line or end of file is reached.
            while (!line.isEmpty() && !fileRead.atEOS()) {
                defBuilder.append(line);
                line = fileRead.nextLine();
            }

            if (!line.isEmpty()) {
                defBuilder.append(line);

            }

            String definition = defBuilder.toString().trim();
            glossaryMap.add(term, definition);
        }

    }

    /**
     * Populates a queue with terms from the glossary map. Terms are added in
     * the order they appear in the map.
     *
     * @param glossaryMap
     *            the map containing term-definition pairs.
     * @param termsQueue
     *            the queue to be populated with terms.
     */

    public static void populateTermQueue(Map<String, String> glossaryMap,
            Queue<String> termsQueue) {
        for (Map.Pair<String, String> termPair : glossaryMap) {
            termsQueue.enqueue(termPair.key());
        }
    }

    /**
     * Replaces terms in the definition with hyperlinks.
     *
     * @param termsQueue
     *            the queue containing all the terms.
     *
     * @param definitionText
     *            the text of the definition where terms need to be hyperlinked.
     * @return a string with terms replaced by hyperlinks.
     */

    public static String formatDefinitionWithHyperlinks(
            Queue<String> termsQueue, String definitionText) {
        // Splits the definition into words.
        String[] wordsInDefinition = definitionText.split("\\s+");

        StringBuilder linkedWords = new StringBuilder();
        // Checks each word against all terms in the queue.
        for (String word : wordsInDefinition) {
            boolean isTerm = false;
            for (String term : termsQueue) {
                // If the word is a term, replace it with a hyperlink.
                if (word.equalsIgnoreCase(term)) {

                    linkedWords.append("<a href=\"").append(term)
                            .append(".html\">").append(word).append("</a> ");

                    isTerm = true;
                    break;
                }
            }
            if (!isTerm) {
                linkedWords.append(word).append(" ");
            }
        }
        return linkedWords.toString().trim();
    }

    /**
     * Generates HTML pages for each term in the glossary.
     *
     * @param outputDirectory
     *            the directory where the HTML files will be saved.
     * @param termsQueue
     *            the queue containing all the terms.
     * @param glossaryMap
     *            the map containing term-definition pairs.
     */

    public static void generateGlossaryPage(String outputDirectory,
            Queue<String> termsQueue, Map<String, String> glossaryMap) {
        for (String term : termsQueue) {
            String definition = formatDefinitionWithHyperlinks(termsQueue,
                    glossaryMap.value(term));
            SimpleWriter out = new SimpleWriter1L(
                    outputDirectory + "/" + term + ".html");

            // Writes the HTML content for each term's page.
            out.println("<!DOCTYPE html>");

            out.println("<html>");

            out.println("<head>");

            out.println("<title>" + term + "</title>");

            out.println("</head>");

            out.println("<body>");

            out.println("<h2><b><i><font color=\"red\">" + term
                    + "</font></i></b></h2>");

            out.print("<blockquote>");

            out.print(definition);

            out.println("</blockquote>");

            out.println("<hr/>");

            out.println("</body>\n</html>");
            out.close();

        }

    }

    /**
     * Generates the index HTML page for the glossary.
     *
     * @param indexFile
     *            the file path for the index HTML page.
     * @param termsQueue
     *            the queue containing all the terms to be listed in the index.
     */

    public static void generateIndexPage(String indexFile,
            Queue<String> termsQueue) {

        SimpleWriter out = new SimpleWriter1L(indexFile);

        // Writes the HTML content for the index page
        out.println("<!DOCTYPE html>");

        out.println("<html>");

        out.println("<head>");

        out.println("<title>Sample Glossary</title>");

        out.println("</head>");

        out.println("<body>");

        out.println("<h2>Sample Glossary</h2>");

        out.println("<hr/>");

        out.println("<h3>Index</h3>");

        out.println("<ul>");

        for (String result : termsQueue) {

            out.println("<li>");

            out.println("<a href=\"" + result + ".html\">" + result + "</a>");

            out.println("</li>");
        }

        out.println("</ul>");

        out.println("</body>");

        out.println("</html>");

        out.close();

    }

    /**
     * Main method to run the Glossary program.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        // Map and queue to store glossary terms and their definitions.
        Map<String, String> glossaryMap = new Map1L<>();
        Queue<String> glossaryQueue = new Queue1L<>();

        // Tells the user to input file path.
        out.print("Enter the input file: ");
        String result = in.nextLine();

        SimpleReader fileTemp = new SimpleReader1L(result);

        // Parse input file to extract terms and definitions.
        parseInputToMap(glossaryMap, fileTemp);

        // Populate terms queue with terms from the glossary map.
        populateTermQueue(glossaryMap, glossaryQueue);

        // Comparator to sort terms alphabetically.
        Comparator<String> glossaryComparator = new TermComparator();
        glossaryQueue.sort(glossaryComparator);

        // Tells the user to input the output directory.
        out.print("Enter the folder location you want to save: ");
        String result1 = in.nextLine();

        String indexTemp = result1 + "/index.html";

        // Generates the index page and glossary page.
        generateIndexPage(indexTemp, glossaryQueue);
        generateGlossaryPage(result1, glossaryQueue, glossaryMap);

        // Close all opened streams.
        in.close();
        out.close();
        fileTemp.close();

    }

}
