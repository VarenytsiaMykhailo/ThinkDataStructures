package com.github.varenytsiamykhailo.thinkdast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WikiPhilosophy {

    final static List<String> visited = new ArrayList<String>();
    final static WikiFetcher wf = WikiFetcher.getInstance();

    /**
     * Tests a conjecture about Wikipedia and Philosophy.
     * <p>
     * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
     * <p>
     * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     * that does not exist, or when a loop occurs
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        System.err.println("MAIN.");
        String destination = "https://en.wikipedia.org/wiki/Philosophy";
        String source = "https://en.wikipedia.org/wiki/Java_(programming_language)";

        testConjecture(destination, source, 15);
    }

    /**
     * Starts from given URL and follows first link until it finds the destination or exceeds the limit.
     *
     * @param destination
     * @param source
     * @throws IOException
     */
    public static void testConjecture(String destination, String source, int limit) throws IOException {
        String url = source;

        for (int i = 0; i < limit; i++) {
            if (visited.contains(url)) {
                System.err.println("We're in a loop, exiting.");
                return;
            } else {
                visited.add(url);
            }

            Element nextLinkElement = getFirstValidLink(url);

            if (nextLinkElement == null) {
                System.err.println("Got to a page with no valid links.");
                return;
            }

            System.out.println("**" + nextLinkElement.text() + "**");
            url = nextLinkElement.attr("abs:href");
            if (url.equals(destination)) {
                System.out.println("Eureka!");
                break;
            }


            if (i == limit - 1) {
                System.err.println("LIMIT " + limit + "!!!");
            }

        }

    }

    /**
     * Loads and parses a URL, then extracts the first link.
     *
     * @param url
     * @return the Element of the first link, or null.
     * @throws IOException
     */
    public static Element getFirstValidLink(String url) throws IOException {
        print("Fetching %s...", url);
        Elements paragraphs = wf.fetchWikipedia(url);
        WikiParser wp = new WikiParser(paragraphs);
        Element elt = wp.findFirstLink();
        return elt;
    }

    /**
     * Formats and print the arguments.
     *
     * @param msg
     * @param args
     */
    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }
}
