package com.max.app17.ds;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.max.app17.ds.SkipListSet;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

final class SkipListSetTest {

    @Test
    void addAndContainsRandomValues() {
        ThreadLocalRandom rand = ThreadLocalRandom.current();

        for (int it = 0; it < 100; ++it) {
            Set<Integer> actualSet = new HashSet<>();
            Set<Integer> skipSet = new SkipListSet<>();

            for (int i = 0; i < 1000; ++i) {
                int randVal = rand.nextInt();
                assertThat(skipSet.add(randVal)).isEqualTo(actualSet.add(randVal));
            }

            for (int actualValue : actualSet) {
                assertThat(skipSet.contains(actualValue)).isEqualTo(true);
            }

            for (int i = 0; i < 1000; ++i) {
                int randVal = rand.nextInt();
                assertThat(skipSet.contains(randVal)).isEqualTo(actualSet.contains(randVal));
            }
        }
    }

    @Test
    void addAndContains() {
        Set<Integer> set = new SkipListSet<>();

        set.add(10);
        assertThat(set.contains(10)).isTrue();
        assertThat(set.contains(20)).isFalse();
        assertThat(set.contains(7)).isFalse();
        assertThat(set.contains(3)).isFalse();
        assertThat(set.contains(17)).isFalse();
        assertThat(set.contains(31)).isFalse();

        set.add(20);
        assertThat(set.contains(10)).isTrue();
        assertThat(set.contains(20)).isTrue();
        assertThat(set.contains(7)).isFalse();
        assertThat(set.contains(3)).isFalse();
        assertThat(set.contains(17)).isFalse();
        assertThat(set.contains(31)).isFalse();

        set.add(7);
        assertThat(set.contains(10)).isTrue();
        assertThat(set.contains(20)).isTrue();
        assertThat(set.contains(7)).isTrue();
        assertThat(set.contains(3)).isFalse();
        assertThat(set.contains(17)).isFalse();
        assertThat(set.contains(31)).isFalse();

        set.add(3);
        assertThat(set.contains(10)).isTrue();
        assertThat(set.contains(20)).isTrue();
        assertThat(set.contains(7)).isTrue();
        assertThat(set.contains(3)).isTrue();
        assertThat(set.contains(17)).isFalse();
        assertThat(set.contains(31)).isFalse();

        set.add(17);
        assertThat(set.contains(10)).isTrue();
        assertThat(set.contains(20)).isTrue();
        assertThat(set.contains(7)).isTrue();
        assertThat(set.contains(3)).isTrue();
        assertThat(set.contains(17)).isTrue();
        assertThat(set.contains(31)).isFalse();

        set.add(31);
        assertThat(set.contains(10)).isTrue();
        assertThat(set.contains(20)).isTrue();
        assertThat(set.contains(7)).isTrue();
        assertThat(set.contains(3)).isTrue();
        assertThat(set.contains(17)).isTrue();
        assertThat(set.contains(31)).isTrue();

        assertThat(set.contains(-10)).isFalse();
        assertThat(set.contains(0)).isFalse();
        assertThat(set.contains(1)).isFalse();
        assertThat(set.contains(11)).isFalse();
        assertThat(set.contains(18)).isFalse();
        assertThat(set.contains(32)).isFalse();
        assertThat(set.contains(133)).isFalse();
    }

    @Test
    void addWithDuplicatedValues() {
        Set<Integer> set = new SkipListSet<>();

        assertThat(set.add(177)).isTrue();
        assertThat(set.add(177)).isFalse();

        assertThat(set.add(-100)).isTrue();
        assertThat(set.add(-100)).isFalse();
        assertThat(set.add(177)).isFalse();
    }

    @Test
    void checkToString() {
        SkipListSet<Integer> set = new SkipListSet<>();
        assertThat(set.toString()).isEqualTo("[]");

        set.add(10);
        assertThat(set.toString()).isEqualTo("[10]");

        set.add(20);
        assertThat(set.toString()).isEqualTo("[10, 20]");

        set.add(7);
        assertThat(set.toString()).isEqualTo("[7, 10, 20]");

        set.add(3);
        assertThat(set.toString()).isEqualTo("[3, 7, 10, 20]");

        set.add(17);
        assertThat(set.toString()).isEqualTo("[3, 7, 10, 17, 20]");

        set.add(31);
        assertThat(set.toString()).isEqualTo("[3, 7, 10, 17, 20, 31]");
    }

    @Test
    void checkToStringReverse() {
        SkipListSet<Integer> set = new SkipListSet<>();
        assertThat(set.toStringReverse()).isEqualTo("[]");

        set.add(10);
        assertThat(set.toStringReverse()).isEqualTo("[10]");

        set.add(20);
        assertThat(set.toStringReverse()).isEqualTo("[20, 10]");

        set.add(7);
        assertThat(set.toStringReverse()).isEqualTo("[20, 10, 7]");

        set.add(3);
        assertThat(set.toStringReverse()).isEqualTo("[20, 10, 7, 3]");

        set.add(17);
        assertThat(set.toStringReverse()).isEqualTo("[20, 17, 10, 7, 3]");

        set.add(31);
        assertThat(set.toStringReverse()).isEqualTo("[31, 20, 17, 10, 7, 3]");
    }

    @Test
    void checkSizeAndIsEmpty() {
        Set<Integer> set = new SkipListSet<>();

        assertThat(set.isEmpty()).isTrue();
        assertThat(set.size()).isEqualTo(0);

        set.add(133);
        assertThat(set.isEmpty()).isFalse();
        assertThat(set.size()).isEqualTo(1);

        set.add(-100);
        set.add(0);
        set.add(777);
        assertThat(set.isEmpty()).isFalse();
        assertThat(set.size()).isEqualTo(4);

        // add duplicated values should not change set size
        set.add(-100);
        set.add(0);
        set.add(777);
        assertThat(set.isEmpty()).isFalse();
        assertThat(set.size()).isEqualTo(4);
    }

    @Test
    void checkIteratorReturnsValuesInSortedOrder() {
        Set<Integer> set = new SkipListSet<>();
        set.add(5);
        set.add(37);
        set.add(-10);
        set.add(0);
        set.add(777);
        set.add(32);

        Iterator<Integer> it = set.iterator();

        assertThat(it.hasNext()).isTrue();
        assertThat(it.next()).isEqualTo(-10);

        assertThat(it.hasNext()).isTrue();
        assertThat(it.next()).isEqualTo(0);

        assertThat(it.hasNext()).isTrue();
        assertThat(it.next()).isEqualTo(5);

        assertThat(it.hasNext()).isTrue();
        assertThat(it.next()).isEqualTo(32);

        assertThat(it.hasNext()).isTrue();
        assertThat(it.next()).isEqualTo(37);

        assertThat(it.hasNext()).isTrue();
        assertThat(it.next()).isEqualTo(777);

        assertThat(it.hasNext()).isFalse();
        assertThatThrownBy(it::next)
            .isInstanceOf(NoSuchElementException.class)
            .hasMessageContaining("No elements left in SkipListSet iterator.");

    }

}
