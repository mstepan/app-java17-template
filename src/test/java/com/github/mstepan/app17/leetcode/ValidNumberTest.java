package com.github.mstepan.app17.leetcode;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.max.app17.leetcode.hard.ValidNumber;
import org.junit.jupiter.api.Test;

public class ValidNumberTest {

    @Test
    public void isValidNumberInteger() {
        assertTrue(ValidNumber.isNumber("2"));
        assertTrue(ValidNumber.isNumber("0089"));
        assertTrue(ValidNumber.isNumber("+22"));
        assertTrue(ValidNumber.isNumber("-33"));
    }

    @Test
    public void isValidNumberFloatNumber() {
        assertTrue(ValidNumber.isNumber("-0.1"));
        assertTrue(ValidNumber.isNumber("+3.14"));
        assertTrue(ValidNumber.isNumber("4."));
        assertTrue(ValidNumber.isNumber("-.9"));
    }

    @Test
    public void isValidNumberDecimal() {
        assertTrue(ValidNumber.isNumber("2e10"));
        assertTrue(ValidNumber.isNumber("-90E3"));
        assertTrue(ValidNumber.isNumber("3e+7"));
        assertTrue(ValidNumber.isNumber("+6e-1"));
        assertTrue(ValidNumber.isNumber("53.5e93"));
        assertTrue(ValidNumber.isNumber("-123.456e789"));
        assertTrue(ValidNumber.isNumber(".0e7"));
    }

    @Test
    public void isValidNumberShouldFailValidation() {
        assertFalse(ValidNumber.isNumber("abc"));
        assertFalse(ValidNumber.isNumber("1a"));
        assertFalse(ValidNumber.isNumber("1e"));
        assertFalse(ValidNumber.isNumber("e3"));
        assertFalse(ValidNumber.isNumber("99e2.5"));
        assertFalse(ValidNumber.isNumber("--6"));
        assertFalse(ValidNumber.isNumber("-+3"));
        assertFalse(ValidNumber.isNumber("95a54e53"));
        assertFalse(ValidNumber.isNumber("."));
        assertFalse(ValidNumber.isNumber("7e69e"));
    }
}
