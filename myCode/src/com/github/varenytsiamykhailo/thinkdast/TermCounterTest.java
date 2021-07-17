package com.github.varenytsiamykhailo.thinkdast;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

/**
 * @author downey
 * Updated
 */
public class TermCounterTest {

    private TermCounter counter;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";

        WikiFetcher wf = WikiFetcher.getInstance();
        Elements paragraphs = wf.fetchWikipedia(url);

        counter = new TermCounter(url.toString());
        counter.processElements(paragraphs);
    }

    @Test
    public void testSize() {
        assertThat(counter.size(), is(4649));
    }
}
