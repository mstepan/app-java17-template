package com.github.mstepan.app17.leetcode.medium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class HtmlEntityParserTest {

    @Test
    void entityParserCase1() {
        assertEquals(
                "& is an HTML entity but &ambassador; is not.",
                new HtmlEntityParser()
                        .entityParser("&amp; is an HTML entity but &ambassador; is not."));
    }

    @Test
    void entityParserCase2() {
        assertEquals("x &< y", new HtmlEntityParser().entityParser("x &&lt; y"));
    }

    @Test
    void entityParserCase3() {
        assertEquals(
                "and I quote: \"...\"",
                new HtmlEntityParser().entityParser("and I quote: &quot;...&quot;"));
    }

    @Test
    void entityParserNotReplacementAtAll() {
        assertEquals("x && y == z", new HtmlEntityParser().entityParser("x && y == z"));
    }
}
