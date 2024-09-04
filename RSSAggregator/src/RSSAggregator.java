import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.xmltree.XMLTree;
import components.xmltree.XMLTree1;

/**
 * Program to convert an XML RSS (version 2.0) feed from a given URL into the
 * corresponding HTML output file.
 *
 * @author Khalid Abukar
 *
 */
public final class RSSAggregator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private RSSAggregator() {
    }

    /**
     * Outputs the "opening" tags in the generated HTML file. These are the
     * expected elements generated by this method:
     *
     * <html> <head> <title>the channel tag title as the page title</title>
     * </head> <body>
     * <h1>the page title inside a link to the <channel> link</h1>
     * <p>
     * the channel description
     * </p>
     * <table border="1">
     * <tr>
     * <th>Date</th>
     * <th>Source</th>
     * <th>News</th>
     * </tr>
     *
     * @param channel
     *            the channel element XMLTree
     * @param out
     *            the output stream
     * @updates out.content
     * @requires [the root of channel is a <channel> tag] and out.is_open
     * @ensures out.content = #out.content * [the HTML "opening" tags]
     */
    private static void outputHeader(XMLTree channel, SimpleWriter out) {
        assert channel != null : "Violation of: channel is not null";
        assert out != null : "Violation of: out is not null";
        assert channel.isTag() && channel.label().equals("channel") : ""
                + "Violation of: the label root of channel is a <channel> tag";
        assert out.isOpen() : "Violation of: out.is_open";

        // initializing title, link, and description
        String title = " Empty Title";
        String link = "";
        String description = "Empty Description ";

        // retrieving the index of title, link, and description
        int indexTitle = getChildElement(channel, "title");
        int indexLink = getChildElement(channel, "link");
        int indexDescription = getChildElement(channel, "description");

        // update the corresponding variables if it exist in the channel

        if (indexTitle != -1
                && channel.child(indexTitle).numberOfChildren() > 0) {
            title = channel.child(indexTitle).child(0).label();
        }

        if (indexLink != -1
                && channel.child(indexLink).numberOfChildren() > 0) {
            link = channel.child(indexLink).child(0).label();
        }

        if (indexDescription != -1
                && channel.child(indexDescription).numberOfChildren() > 0) {
            description = channel.child(indexDescription).child(0).label();
        }

        // printing HTML strings to the output stream
        out.println("<html>");
        out.println("<head>");
        out.println("<title>" + title + "</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1><a href=\"" + link + "\">" + title + "</a></h1>");
        out.println("<p>" + description + "</p>");
        out.println("<table border=\"1\">");
        out.println(" <tr>");
        out.println("  <th>Date</th>");
        out.println("  <th>Source</th>");
        out.println("  <th>News</th>");
        out.println(" </tr>");

    }

    /**
     * Outputs the "closing" tags in the generated HTML file. These are the
     * expected elements generated by this method:
     *
     * </table>
     * </body> </html>
     *
     * @param out
     *            the output stream
     * @updates out.contents
     * @requires out.is_open
     * @ensures out.content = #out.content * [the HTML "closing" tags]
     */
    private static void outputFooter(SimpleWriter out) {
        assert out != null : "Violation of: out is not null";
        assert out.isOpen() : "Violation of: out.is_open";

        // closing HTML tags to the output stream
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");

    }

    /**
     * Finds the first occurrence of the given tag among the children of the
     * given {@code XMLTree} and return its index; returns -1 if not found.
     *
     * @param xml
     *            the {@code XMLTree} to search
     * @param tag
     *            the tag to look for
     * @return the index of the first child of type tag of the {@code XMLTree}
     *         or -1 if not found
     * @requires [the label of the root of xml is a tag]
     * @ensures <pre>
     * getChildElement =
     *  [the index of the first child of type tag of the {@code XMLTree} or
     *   -1 if not found]
     * </pre>
     */
    private static int getChildElement(XMLTree xml, String tag) {
        assert xml != null : "Violation of: xml is not null";
        assert tag != null : "Violation of: tag is not null";
        assert xml.isTag() : "Violation of: the label root of xml is a tag";

        // Initialize the variables
        int indexFound = -1;
        int childrenNum = xml.numberOfChildren();
        boolean isFound = false;

        // Looping through children of xml to find the first occurrence of the given tag
        for (int i = 0; i < childrenNum && !isFound; i++) {
            if (xml.child(i).label().equals(tag)) {
                indexFound = i;
                isFound = true;
            }
        }
        // returns the index
        return indexFound;
    }

    /**
     * Processes one news item and outputs one table row. The row contains three
     * elements: the publication date, the source, and the title (or
     * description) of the item.
     *
     * @param item
     *            the news item
     * @param out
     *            the output stream
     * @updates out.content
     * @requires [the label of the root of item is an <item> tag] and
     *           out.is_open
     * @ensures <pre>
     * out.content = #out.content *
     *   [an HTML table row with publication date, source, and title of news item]
     * </pre>
     */
    private static void processItem(XMLTree item, SimpleWriter out) {
        assert item != null : "Violation of: item is not null";
        assert out != null : "Violation of: out is not null";
        assert item.isTag() && item.label().equals("item") : ""
                + "Violation of: the label root of item is an <item> tag";
        assert out.isOpen() : "Violation of: out.is_open";

        out.println("<tr>");

        // Writing the opening tag for table row to the output stream
        int indexDate = getChildElement(item, "pubDate");
        int indexSource = getChildElement(item, "source");
        int indexTitle = getChildElement(item, "title");
        int indexLink = getChildElement(item, "link");
        int indexDescription = getChildElement(item, "description");

        // Checks if the indexDate is not -1
        if (indexDate != -1) {
            out.println(
                    "<td>" + item.child(indexDate).child(0).label() + "</td>");
        } else {
            out.println("<td>No date available</td> ");
        }

        // If indexSource is valid then print the source as a hyperlink
        if (indexSource != -1) {
            String source = item.child(indexSource).attributeValue("url");
            String sourceName = item.child(indexSource).child(0).label();
            out.println(
                    "<td><a href=" + source + ">" + sourceName + "</a></td>");
        } else {
            out.println("<td>No source available</td>");
        }
        // If indexTitle and indexLink are valid then print the title as a hyperlink

        if (indexTitle != -1
                && item.child(indexTitle).numberOfChildren() == 0) {

            if (indexLink != -1) {
                out.println("<td><a href=\""
                        + item.child(indexLink).child(0).label() + "\">"
                        + "No title" + "</a></td>");
            } else {
                out.println("<td>No title available</td>");
            }

        } else if (indexTitle != -1
                && item.child(indexTitle).numberOfChildren() > 0) {
            String titleName = item.child(indexTitle).child(0).label();
            if (indexLink != -1) {
                out.println("<td><a href=\""
                        + item.child(indexLink).child(0).label() + "\">"
                        + titleName + "</a></td>");
            } else {
                out.println("<td>" + titleName + "</td>");
            }

        } else if (indexDescription != -1
                && item.child(indexDescription).numberOfChildren() > 0) {
            String descriptionName = item.child(indexDescription).child(0)
                    .label();
            if (indexLink != -1) {
                out.println("<td><a href=\""
                        + item.child(indexLink).child(0).label() + "\">"
                        + descriptionName + "</a></td>");
            } else {
                out.println("<td>" + descriptionName + "</td>");
            }
        } else if (indexDescription != -1
                && item.child(indexDescription).numberOfChildren() == 0) {

            if (indexLink != -1) {
                out.println("<td><a href=\""
                        + item.child(indexLink).child(0).label() + "\">"
                        + "No title" + "</a></td>");
            } else {
                out.println("<td>No title available</td>");
            }
        }

        out.println("</tr>");
    }

    /**
     * Processes one XML RSS (version 2.0) feed from a given URL converting it
     * into the corresponding HTML output file.
     *
     * @param url
     *            the URL of the RSS feed
     * @param file
     *            the name of the HTML output file
     * @param out
     *            the output stream to report progress or errors
     * @updates out.content
     * @requires out.is_open
     * @ensures <pre>
     * [reads RSS feed from url, saves HTML document with table of news items
     *   to file, appends to out.content any needed messages]
     * </pre>
     */
    private static void processFeed(String url, String file, SimpleWriter out) {

        XMLTree xml = new XMLTree1(url);

        int indexChannel = getChildElement(xml, "channel");

        // Checks to see if it's an RSS 2.0 feed with a "channel" child.

        if (xml.isTag() && xml.label().equals("rss")
                && "2.0".equals(xml.attributeValue("version"))) {
            SimpleWriter fileOut = new SimpleWriter1L(file);
            outputHeader(xml.child(indexChannel), fileOut);

            // Process each item within the channel.
            for (int i = 0; i < xml.child(indexChannel)
                    .numberOfChildren(); i++) {
                if (xml.child(indexChannel).child(i).label().equals("item")) {
                    processItem(xml.child(indexChannel).child(i), fileOut);
                }
            }
            outputFooter(fileOut);
            fileOut.close();
        } else {
            out.println("The RSS feed is not valid.");
        }
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        // Ask the user to input the URL of the RSS feed
        out.println("Enter the URL of an RSS 2.0 feed: ");
        String url = in.nextLine();

        // Ask the user to input the name of the output file
        out.println(
                "Enter the name of the output file (including .html extension): ");
        String outputFile = in.nextLine();

        // Create SimpleWriter object to write to the output file
        SimpleWriter fileOut = new SimpleWriter1L(outputFile);
        XMLTree xml = new XMLTree1(url); // Create XMLTree object from the provided URL

        // Initiate the HTML output.
        fileOut.println("<html>");
        fileOut.println("<head>");
        fileOut.println("<title>" + xml.attributeValue("title") + "</title>");
        fileOut.println("</head>");
        fileOut.println("<body>");
        fileOut.println("<h2>" + xml.attributeValue("title") + "</h2>");
        fileOut.println("<ul>");

        // Loop through XML child elements, retrieve attributes, and create HTML links.
        for (int i = 0; i < xml.numberOfChildren(); i++) {
            XMLTree feed = xml.child(i);
            String feedURL = feed.attributeValue("url");
            String feedName = feed.attributeValue("name");
            String feedFile = feed.attributeValue("file");

            fileOut.println("<li><a href=\"" + feedFile + "\">" + feedName
                    + "</a></li>");

            processFeed(feedURL, feedFile, out);

        }

        fileOut.println("</ul>");
        fileOut.println("</body>");
        fileOut.println("</html>");

        // Close the SimpleWriter objects
        fileOut.close();
        in.close();
        out.close();

    }

}
