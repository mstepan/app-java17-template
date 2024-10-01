package com.github.mstepan.app17.ds;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterator;
import org.junit.jupiter.api.Test;

public class TernarySearchTreeTest {

    @Test
    void add() {
        Set<String> tree = new TernarySearchTree();

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
        Set<String> tree = new TernarySearchTree();

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
        Set<String> tree = new TernarySearchTree();
        Exception actualEx = assertThrows(NullPointerException.class, () -> tree.add(null));

        assertEquals("Can't save null value. Nulls are prohibited.", actualEx.getMessage());
    }

    @Test
    void addValuesWithCommonPrefixes() {
        Set<String> tree = new TernarySearchTree();

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
    void addThanRemoveThanAddAgain() {
        Set<String> tree = new TernarySearchTree();

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

    @SuppressWarnings("all")
    @Test
    void addThanClearThanAddAgain() {

        Set<String> tree = new TernarySearchTree();

        assertTrue(tree.add("mac"));
        assertTrue(tree.add("linux"));
        assertTrue(tree.add("unix"));
        assertTrue(tree.add("posix"));

        tree.clear();
        assertEquals(0, tree.size());
        assertTrue(tree.isEmpty());

        assertTrue(tree.add("mac"));
        assertTrue(tree.add("linux"));
        assertTrue(tree.add("unix"));
        assertTrue(tree.add("posix"));
        assertEquals(4, tree.size());
        assertFalse(tree.isEmpty());

        assertTrue(tree.contains("mac"));
        assertTrue(tree.contains("linux"));
        assertTrue(tree.contains("unix"));
        assertTrue(tree.contains("posix"));
    }

    @Test
    void contains() {

        Set<String> tree = new TernarySearchTree();

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
        Set<String> tree = new TernarySearchTree();

        assertFalse(tree.contains("cat"));
        assertFalse(tree.contains("dog"));
    }

    @SuppressWarnings("all")
    @Test
    void containsForNullShouldFail() {
        Set<String> tree = new TernarySearchTree();

        tree.add("cat");
        tree.add("dog");

        Exception actualEx = assertThrows(NullPointerException.class, () -> tree.contains(null));

        assertEquals("Can't search for null value. Nulls are prohibited.", actualEx.getMessage());
    }

    @SuppressWarnings("all")
    @Test
    void sizeAndEmpty() {
        Set<String> tree = new TernarySearchTree();

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
        Set<String> tree = new TernarySearchTree();

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
        Set<String> tree = new TernarySearchTree();

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
        Set<String> tree = new TernarySearchTree();

        assertEquals(0, tree.size());
        assertTrue(tree.isEmpty());

        assertFalse(tree.remove("macos"));
        assertFalse(tree.remove("linux"));

        assertEquals(0, tree.size());
        assertTrue(tree.isEmpty());
    }

    @SuppressWarnings("all")
    @Test
    void clear() {

        Set<String> tree = new TernarySearchTree();

        assertTrue(tree.add("mac"));
        assertTrue(tree.add("linux"));
        assertTrue(tree.add("unix"));
        assertTrue(tree.add("posix"));

        tree.clear();
        assertEquals(0, tree.size());
        assertTrue(tree.isEmpty());

        assertFalse(tree.contains("mac"));
        assertFalse(tree.contains("linux"));
        assertFalse(tree.contains("unix"));
        assertFalse(tree.contains("posix"));
    }

    @Test
    void iterator() {
        Set<String> tree = new TernarySearchTree();

        tree.add("unix");
        tree.add("unicode");
        tree.add("union");
        tree.add("unixoid");
        tree.add("unixyz");
        tree.add("posix");

        Iterator<String> it = tree.iterator();

        assertNotNull(it);

        assertTrue(it.hasNext());
        assertEquals("unix", it.next());

        assertTrue(it.hasNext());
        assertEquals("unicode", it.next());

        assertTrue(it.hasNext());
        assertEquals("union", it.next());

        assertTrue(it.hasNext());
        assertEquals("unixoid", it.next());

        assertTrue(it.hasNext());
        assertEquals("unixyz", it.next());

        assertTrue(it.hasNext());
        assertEquals("posix", it.next());

        assertFalse(it.hasNext());
    }

    @SuppressWarnings("all")
    @Test
    void iteratorForEmptyTree() {
        Set<String> tree = new TernarySearchTree();

        Iterator<String> it = tree.iterator();

        assertNotNull(it);
        assertFalse(it.hasNext());
    }

    @Test
    void iteratorThrowsExceptionIfAccessedOutOfBounds() {
        Set<String> tree = new TernarySearchTree();

        tree.add("unix");
        tree.add("linux");

        Iterator<String> it = tree.iterator();

        assertTrue(it.hasNext());
        it.next();

        assertTrue(it.hasNext());
        it.next();

        Exception actualEx = assertThrows(NoSuchElementException.class, it::next);

        assertEquals("No more element left to iterate over", actualEx.getMessage());
    }

    @Test
    void foreachUsingIterator() {
        Set<String> tree = new TernarySearchTree();

        assertTrue(tree.add("mac"));
        assertTrue(tree.add("linux"));
        assertTrue(tree.add("unix"));
        assertTrue(tree.add("posix"));

        assertThat(tree).isNotEmpty().hasSize(4).containsExactly("mac", "linux", "unix", "posix");
    }

    @Test
    void foreachAndRemoveOperations() {

        Set<String> tree = new TernarySearchTree();

        assertTrue(tree.add("mac"));
        assertTrue(tree.add("linux"));
        assertTrue(tree.add("unix"));
        assertTrue(tree.add("posix"));

        assertThat(tree).isNotEmpty().hasSize(4).containsExactly("mac", "linux", "unix", "posix");

        assertTrue(tree.remove("linux"));
        assertThat(tree).isNotEmpty().hasSize(3).containsExactly("mac", "unix", "posix");

        assertTrue(tree.remove("posix"));
        assertThat(tree).isNotEmpty().hasSize(2).containsExactly("mac", "unix");

        assertTrue(tree.remove("unix"));
        assertThat(tree).isNotEmpty().hasSize(1).containsExactly("mac");

        assertTrue(tree.remove("mac"));
        assertThat(tree).isEmpty();
    }

    @Test
    void spliterator() {
        Set<String> tree = new TernarySearchTree();

        tree.add("java");
        tree.add("rust");
        tree.add("golang");
        tree.add("python");
        tree.add("ruby");

        tree.add("c++");
        tree.add("zig");
        tree.add("fortran");
        tree.add("JavaScript");
        tree.add("TypeScript");
        tree.add("Kotlin");

        Spliterator<String> spliterator = tree.spliterator();
        assertNotNull(spliterator);

        Spliterator<String> prefixSpliterator = spliterator.trySplit();
        assertNotNull(prefixSpliterator);

        // can't split prefix or original spliterator any further
        assertNull(spliterator.trySplit());
        assertNull(prefixSpliterator.trySplit());

        // check 'prefixSpliterator'
        assertEquals(5, prefixSpliterator.estimateSize());
        assertTrue(prefixSpliterator.tryAdvance(value -> assertEquals("java", value)));
        assertEquals(4, prefixSpliterator.estimateSize());
        assertTrue(prefixSpliterator.tryAdvance(value -> assertEquals("rust", value)));
        assertEquals(3, prefixSpliterator.estimateSize());
        assertTrue(prefixSpliterator.tryAdvance(value -> assertEquals("golang", value)));
        assertEquals(2, prefixSpliterator.estimateSize());
        assertTrue(prefixSpliterator.tryAdvance(value -> assertEquals("python", value)));
        assertEquals(1, prefixSpliterator.estimateSize());
        assertFalse(prefixSpliterator.tryAdvance(value -> assertEquals("ruby", value)));
        assertEquals(0, prefixSpliterator.estimateSize());

        // check original 'spliterator'
        assertEquals(6, spliterator.estimateSize());
        assertTrue(spliterator.tryAdvance(value -> assertEquals("c++", value)));
        assertEquals(5, spliterator.estimateSize());
        assertTrue(spliterator.tryAdvance(value -> assertEquals("zig", value)));
        assertEquals(4, spliterator.estimateSize());
        assertTrue(spliterator.tryAdvance(value -> assertEquals("fortran", value)));
        assertEquals(3, spliterator.estimateSize());
        assertTrue(spliterator.tryAdvance(value -> assertEquals("JavaScript", value)));
        assertEquals(2, spliterator.estimateSize());
        assertTrue(spliterator.tryAdvance(value -> assertEquals("TypeScript", value)));
        assertEquals(1, spliterator.estimateSize());
        assertFalse(spliterator.tryAdvance(value -> assertEquals("Kotlin", value)));
        assertEquals(0, spliterator.estimateSize());
    }
}
