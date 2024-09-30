package com.github.mstepan.app17.ds;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TSTreeTest {

    @Test
    void addAndContains() {

        TSTree tree = new TSTree();

        String[] values = new String[] {"cute", "cup", "at", "as", "he", "us", "i"};

        for (String singleValue : values) {
            tree.add(singleValue);
        }

        for (String singleValue : values) {
            assertTrue(
                    tree.contains(singleValue),
                    "TSTree doesn't contain value '%s'".formatted(singleValue));
        }

        assertFalse(tree.contains("apple'"));
        assertFalse(tree.contains("cat'"));
        assertFalse(tree.contains("usa'"));
    }
}
