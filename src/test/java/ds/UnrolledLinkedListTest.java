package ds;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class UnrolledLinkedListTest {

    @Test
    void checkAddAndPollFirst() {
        UnrolledLinkedList<Integer> unrolledList = new UnrolledLinkedList<>();

        for (int i = 0; i < 11; ++i) {
            unrolledList.addFirst(i);
        }

        assertThat(unrolledList).
            hasSize(11).
            isNotEmpty().
            isEqualTo(List.of(10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0));
    }

    @Test
    void checkStackPushAndPopOperations() {
        UnrolledLinkedList<Integer> list = new UnrolledLinkedList<>();

        list.push(111);
        list.push(222);
        list.push(333);

        assertThat(list).
            hasSize(3).
            isNotEmpty().
            isEqualTo(List.of(111, 222, 333));

        assertThat(list.pop()).isEqualTo(333);
        assertThat(list).
            hasSize(2).
            isNotEmpty().
            isEqualTo(List.of(111, 222));

        assertThat(list.pop()).isEqualTo(222);
        assertThat(list).
            hasSize(1).
            isNotEmpty().
            isEqualTo(List.of(111));

        assertThat(list.pop()).isEqualTo(111);
        assertThat(list).
            hasSize(0).
            isEmpty();
    }

    @Test
    void checkListIterator() {
        List<Integer> unrolledList = new UnrolledLinkedList<>();

        final int elemsCount = 10;

        for (int i = 0; i < elemsCount; ++i) {
            unrolledList.add(i);
        }

        Iterator<Integer> it = unrolledList.iterator();

        for (int i = 0; i < elemsCount; ++i) {
            assertThat(it).hasNext();
            assertThat(it.next()).isEqualTo(i);
        }
    }

    @Test
    void checkListIteratorReverseOrder() {
        List<Integer> unrolledList = new UnrolledLinkedList<>();

        final int elemsCount = 111;

        for (int i = 0; i <= elemsCount; ++i) {
            unrolledList.add(i);
        }

        ListIterator<Integer> it = unrolledList.listIterator(elemsCount);

        for (int i = elemsCount; i >= 0; --i) {
            assertThat(it.hasPrevious()).isTrue();
            assertThat(it.previous()).isEqualTo(i);
        }
    }

    @Test
    void checkListIteratorWithOffset() {
        List<Integer> unrolledList = new UnrolledLinkedList<>();

        final int elemsCount = 13;

        for (int i = 0; i < elemsCount; ++i) {
            unrolledList.add(i);
        }

        Iterator<Integer> it = unrolledList.listIterator(7);

        for (int i = 7; i < elemsCount; ++i) {
            assertThat(it).hasNext();
            assertThat(it.next()).isEqualTo(i);
        }
    }

    @Test
    void checkListIteratorWithAllPossibleOffsets() {

        final ThreadLocalRandom rand = ThreadLocalRandom.current();

        List<Integer> unrolledList = new UnrolledLinkedList<>();
        List<Integer> linkedList = new LinkedList<>();

        final int elemsCount = rand.nextInt(137);

        for (int i = 0; i < elemsCount; ++i) {
            int randValue = rand.nextInt();
            unrolledList.add(randValue);
            linkedList.add(randValue);
        }

        for(int i = 0; i < elemsCount; ++i){
            Iterator<Integer> unrolledIt = unrolledList.listIterator(i);
            Iterator<Integer> listIt = linkedList.listIterator(i);
            assertIteratorsEquals(unrolledIt, listIt);
        }
    }

    private void assertIteratorsEquals(Iterator<Integer> unrolledIt, Iterator<Integer> listIt) {

        while( unrolledIt.hasNext() ){
            assertThat(listIt).hasNext();
            assertThat(unrolledIt.next()).isEqualTo(listIt.next());
        }

        assertThat(listIt.hasNext()).isFalse();
    }

    @Test
    void checkAddLastWithRandomValues() {

        Deque<Integer> linkedList = new LinkedList<>();
        UnrolledLinkedList<Integer> unrolledList = new UnrolledLinkedList<>();

        ThreadLocalRandom rand = ThreadLocalRandom.current();

        for (int it = 0; it < 10; ++it) {
            final int length = 100 + rand.nextInt(1000);

            for (int i = 0; i < length; ++i) {
                int randValue = rand.nextInt();

                unrolledList.addLast(randValue);
                linkedList.addLast(randValue);

                assertThat(unrolledList).
                    hasSize(linkedList.size()).
                    isEqualTo(linkedList);
            }
        }
    }

    @Test
    void checkQueueOperations() {
        UnrolledLinkedList<Integer> list = new UnrolledLinkedList<>();

        list.enqueue(111);
        list.enqueue(222);
        list.enqueue(333);

        assertThat(list).
            isNotEmpty().
            hasSize(3).
            isEqualTo(List.of(111, 222, 333));

        assertThat(list.dequee()).isEqualTo(111);
        assertThat(list).
            hasSize(2).
            isNotEmpty().
            isEqualTo(List.of(222, 333));

        assertThat(list.dequee()).isEqualTo(222);
        assertThat(list).
            hasSize(1).
            isNotEmpty().
            isEqualTo(List.of(333));

        assertThat(list.dequee()).isEqualTo(333);
        assertThat(list).
            hasSize(0).
            isEmpty();
    }

    @Test
    void getValueByIndex() {
        List<Integer> list = new UnrolledLinkedList<>();

        final int lastValue = 1111;
        for (int i = 0; i <= lastValue; ++i) {
            list.add(i);
        }

        for (int i = 0; i <= lastValue; ++i) {
            assertThat(list.get(i)).isEqualTo(i);
        }
    }


    @Test
    void addAndPollLast() {
        UnrolledLinkedList<Integer> unrolledList = new UnrolledLinkedList<>();

        final int boundary = 113;

        for (int i = 0; i <= boundary; ++i) {
            unrolledList.addLast(i);
        }

        assertThat(unrolledList).
            hasSize(boundary + 1).
            isNotEmpty();

        for (int i = boundary; i >= 0; --i) {
            assertThat(unrolledList.pollLast()).
                isEqualTo(i);
        }

    }

}
