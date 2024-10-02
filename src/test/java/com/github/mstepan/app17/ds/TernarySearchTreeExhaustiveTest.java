package com.github.mstepan.app17.ds;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class TernarySearchTreeExhaustiveTest {

    private static final Random RAND = new Random();

    @Test
    void addLotsOfRandomNumericStrings() {
        Set<String> hashSet = new HashSet<>();
        Set<String> tree = new TernarySearchTree();

        for (int it = 0; it < 100; ++it) {
            for (int i = 0; i < 1000; ++i) {
                String randStr = randomNumericString(1 + RAND.nextInt(20));
                assertEquals(hashSet.add(randStr), tree.add(randStr));
            }

            assertThat(hashSet).isEqualTo(tree);
        }
    }

    private static String randomNumericString(int length) {
        assert length > 0;

        StringBuilder buf = new StringBuilder(length);

        for (int i = 0; i < length; ++i) {
            buf.append((char) ('0' + RAND.nextInt(10)));
        }

        return buf.toString();
    }
}
