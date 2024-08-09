package com.github.mstepan.app17;

import static org.junit.jupiter.api.Assertions.assertTrue;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

/** Simple example of property-based testing. */
public class StringConcatenationTest {

    @Property
    void concatenatedStringLength(@ForAll String left, @ForAll String right) {
        assertTrue((left + right).length() >= (left.length() + right.length()));
    }
}
