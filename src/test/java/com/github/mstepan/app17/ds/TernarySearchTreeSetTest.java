package com.github.mstepan.app17.ds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;

public class TernarySearchTreeSetTest {

    @Test
    void add() {
        Set<String> tree = new TernarySearchTreeSet();

        assertTrue(tree.add("cat"));
        assertTrue(tree.add("catapult"));
        assertTrue(tree.add("cactus"));

        assertTrue(tree.add("dog"));
        assertTrue(tree.add("unicorn"));
        assertTrue(tree.add("unix"));

        assertEquals(6, tree.size());
    }

    @Test
    void addWithDuplicates() {
        Set<String> tree = new TernarySearchTreeSet();

        assertTrue(tree.add("cute"));
        assertTrue(tree.add("cup"));
        assertFalse(tree.add("cute"));
        assertFalse(tree.add("cup"));
        assertEquals(2, tree.size());

        assertTrue(tree.add("at"));
        assertTrue(tree.add("as"));
        assertFalse(tree.add("at"));
        assertFalse(tree.add("as"));
        assertEquals(4, tree.size());
    }

    @SuppressWarnings("all")
    @Test
    void addNullShouldFail() {
        Set<String> tree = new TernarySearchTreeSet();
        Exception actualEx = assertThrows(NullPointerException.class, () -> tree.add(null));

        assertEquals("Can't save null value. Nulls are prohibited.", actualEx.getMessage());
    }

    @Test
    void addValuesWithCommonPrefixes() {
        Set<String> tree = new TernarySearchTreeSet();

        assertTrue(tree.add("cute"));
        assertTrue(tree.add("cut"));
        assertTrue(tree.add("cu"));
        assertTrue(tree.add("c"));

        assertFalse(tree.add("cute"));
        assertFalse(tree.add("cut"));
        assertFalse(tree.add("cu"));
        assertFalse(tree.add("c"));

        assertEquals(4, tree.size());

        assertTrue(tree.add("h"));
        assertTrue(tree.add("he"));
        assertTrue(tree.add("hel"));
        assertTrue(tree.add("hell"));
        assertTrue(tree.add("hello"));

        assertFalse(tree.add("h"));
        assertFalse(tree.add("he"));
        assertFalse(tree.add("hel"));
        assertFalse(tree.add("hell"));
        assertFalse(tree.add("hello"));

        assertEquals(9, tree.size());
    }

    @Test
    void addThanRemoveAndAddBackValues() {
        Set<String> tree = new TernarySearchTreeSet();

        assertTrue(tree.add("mac"));
        assertTrue(tree.add("linux"));
        assertTrue(tree.add("unix"));
        assertTrue(tree.add("posix"));

        // remove 'unix'
        assertTrue(tree.remove("unix"));
        assertFalse(tree.contains("unix"));
        assertEquals(3, tree.size());

        // remove 'linux'
        assertTrue(tree.remove("linux"));
        assertFalse(tree.contains("linux"));
        assertEquals(2, tree.size());

        // insert 'unix' again
        assertTrue(tree.add("unix"));
        assertTrue(tree.contains("unix"));
        assertEquals(3, tree.size());

        // insert 'linux' again
        assertTrue(tree.add("linux"));
        assertTrue(tree.contains("linux"));
        assertEquals(4, tree.size());
    }

    @Test
    void contains() {

        Set<String> tree = new TernarySearchTreeSet();

        String[] valuesToAdd = new String[] {"cute", "cup", "at", "as", "he", "us", "i"};

        for (int i = 0; i < valuesToAdd.length; ++i) {
            assertEquals(i, tree.size());
            assertTrue(tree.add(valuesToAdd[i]));
            assertEquals(i + 1, tree.size());
        }

        for (String singleValue : valuesToAdd) {
            assertTrue(
                    tree.contains(singleValue),
                    "TSTree doesn't contain value '%s'".formatted(singleValue));
        }

        assertFalse(tree.contains("apple'"));
        assertFalse(tree.contains("cat'"));
        assertFalse(tree.contains("usa'"));
    }

    @SuppressWarnings("all")
    @Test
    void containsFromEmptyTree() {
        Set<String> tree = new TernarySearchTreeSet();

        assertFalse(tree.contains("cat"));
        assertFalse(tree.contains("dog"));
    }

    @SuppressWarnings("all")
    @Test
    void containsForNullShouldFail() {
        Set<String> tree = new TernarySearchTreeSet();

        tree.add("cat");
        tree.add("dog");

        Exception actualEx = assertThrows(NullPointerException.class, () -> tree.contains(null));

        assertEquals("Can't search for null value. Nulls are prohibited.", actualEx.getMessage());
    }

    @SuppressWarnings("all")
    @Test
    void sizeAndEmpty() {
        Set<String> tree = new TernarySearchTreeSet();

        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());

        tree.add("unix");
        assertFalse(tree.isEmpty());
        assertEquals(1, tree.size());

        tree.add("unicode");
        assertFalse(tree.isEmpty());
        assertEquals(2, tree.size());

        tree.add("posix");
        assertFalse(tree.isEmpty());
        assertEquals(3, tree.size());
    }

    @Test
    void remove() {
        Set<String> tree = new TernarySearchTreeSet();

        tree.add("unix");
        tree.add("unicode");
        tree.add("union");
        tree.add("unixoid");
        tree.add("unixyz");
        tree.add("posix");

        assertEquals(6, tree.size());

        assertTrue(tree.remove("unix"));
        assertFalse(tree.remove("unix"));
        assertEquals(5, tree.size());

        assertTrue(tree.remove("union"));
        assertFalse(tree.remove("union"));
        assertEquals(4, tree.size());

        assertTrue(tree.remove("unixoid"));
        assertFalse(tree.remove("unixoid"));
        assertEquals(3, tree.size());

        assertTrue(tree.remove("unicode"));
        assertFalse(tree.remove("unicode"));
        assertEquals(2, tree.size());

        assertTrue(tree.remove("posix"));
        assertFalse(tree.remove("posix"));
        assertEquals(1, tree.size());

        assertTrue(tree.remove("unixyz"));
        assertFalse(tree.remove("unixyz"));
        assertEquals(0, tree.size());
        assertTrue(tree.isEmpty());
    }

    @Test
    void removeNotExistedValues() {
        Set<String> tree = new TernarySearchTreeSet();

        tree.add("unix");
        tree.add("unicode");
        tree.add("union");
        tree.add("unixoid");
        tree.add("unixyz");
        tree.add("posix");

        assertFalse(tree.remove("neovim"));
        assertFalse(tree.remove("ux"));
        assertFalse(tree.remove("u"));

        assertEquals(6, tree.size());
    }

    @SuppressWarnings("all")
    @Test
    void removeFromEmptySetShouldNotFail() {
        Set<String> tree = new TernarySearchTreeSet();

        assertEquals(0, tree.size());
        assertTrue(tree.isEmpty());

        assertFalse(tree.remove("macos"));
        assertFalse(tree.remove("linux"));

        assertEquals(0, tree.size());
        assertTrue(tree.isEmpty());
    }
}
