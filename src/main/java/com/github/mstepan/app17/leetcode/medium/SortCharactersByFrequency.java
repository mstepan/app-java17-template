package com.max.app17.leetcode.medium;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/** 451. Sort Characters By Frequency https://leetcode.com/problems/sort-characters-by-frequency/ */
public class SortCharactersByFrequency {

    public static void main(String[] args) throws Exception {

        String res = frequencySort("loveleetcode");

        System.out.println(res);

        System.out.println("SortCharactersByFrequency done...");
    }

    public static String frequencySort(String str) {

        Character[] arr = toCharArray(str);

        Map<Character, Integer> freq = calculateFreq(arr);

        Arrays.sort(arr, new CharFreqComparator(freq));

        return charArrayToString(arr);
    }

    private static String charArrayToString(Character[] arr) {
        StringBuilder buf = new StringBuilder(arr.length);

        for (Character ch : arr) {
            buf.append(ch);
        }

        return buf.toString();
    }

    private static Character[] toCharArray(String str) {
        Character[] arr = new Character[str.length()];

        for (int i = 0; i < str.length(); ++i) {
            arr[i] = str.charAt(i);
        }
        return arr;
    }

    static class CharFreqComparator implements Comparator<Character> {

        private final Map<Character, Integer> freq;

        public CharFreqComparator(Map<Character, Integer> freq) {
            this.freq = freq;
        }

        @Override
        public int compare(Character ch1, Character ch2) {
            int freq1 = freq.get(ch1);
            int freq2 = freq.get(ch2);

            int freqCmp = -Integer.compare(freq1, freq2);

            if (freqCmp != 0) {
                return freqCmp;
            }

            return Character.compare(ch1, ch2);
        }
    }

    private static Map<Character, Integer> calculateFreq(Character[] arr) {
        Map<Character, Integer> freq = new HashMap<>();

        for (char ch : arr) {
            freq.compute(ch, (chKey, chFreq) -> chFreq == null ? 1 : chFreq + 1);
        }

        return freq;
    }
}
