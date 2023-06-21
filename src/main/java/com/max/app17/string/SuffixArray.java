package com.max.app17.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class SuffixArray {

    private final StringSuffix[] suffixes;

    public SuffixArray(String originalStr) {
        Objects.requireNonNull(originalStr, "null 'originalStr' detected");
        this.suffixes = createSortedSuffixes(originalStr);
    }

    public int find(String pattern, int searchFrom) {
        if (searchFrom >= suffixes.length) {
            return -1;
        }

        int elemsInStr = suffixes.length - searchFrom;

        if (elemsInStr < pattern.length()) {
            return -1;
        }

        int foundLo = findBoundary(pattern, 0, suffixes.length - 1, true);

        if (foundLo == -1) {
            return -1;
        }

        int foundHi = findBoundary(pattern, 0, suffixes.length - 1, false);

        return findClosestTo(searchFrom, foundLo, foundHi);
    }

    private int findBoundary(String pattern, int initialLo, int initialHi, boolean lowerBoundary) {
        int lo = initialLo;
        int hi = initialHi;
        int foundSoFar = -1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            int cmp = suffixes[mid].compareWithPattern(pattern);

            if (cmp < 0) {
                lo = mid + 1;
            }
            else if (cmp > 0) {
                hi = mid - 1;
            }
            else {
                // match found
                foundSoFar = mid;

                if (lowerBoundary) {
                    hi = mid - 1;
                }
                else {
                    lo = mid + 1;
                }
            }
        }

        return foundSoFar;
    }

    private int findClosestTo(int searchFromIndex, int foundLo, int foundHi) {

        int indexToReturn = Integer.MAX_VALUE;

        for (int i = foundLo; i <= foundHi; ++i) {
            int suffixStart = suffixes[i].start;
            if (suffixStart >= searchFromIndex && suffixStart < indexToReturn) {
                indexToReturn = suffixStart;
            }
        }

        return (indexToReturn == Integer.MAX_VALUE) ? -1 : indexToReturn;
    }

    public int find(String pattern) {
        return find(pattern, 0);
    }

    @Override
    public String toString() {
        List<String> suffixesAsStr = new ArrayList<>(suffixes.length);

        for (StringSuffix singleSuffix : suffixes) {
            suffixesAsStr.add(singleSuffix + "(" + singleSuffix.start + ")");
        }

        return suffixesAsStr.toString();
    }

    private static StringSuffix[] createSortedSuffixes(String str) {
        StringSuffix[] suffixesFromStr = new StringSuffix[str.length()];

        for (int i = 0; i < str.length(); ++i) {
            suffixesFromStr[i] = new StringSuffix(i, str);
        }

        Arrays.sort(suffixesFromStr);

        return suffixesFromStr;
    }

    private record StringSuffix(int start, String str) implements Comparable<StringSuffix> {
        StringSuffix {
            checkIndexInRange(start, 0, str.length() - 1,
                    "start: %d, not in range [%d; %d]".formatted(start, 0, str.length() - 1));
            Objects.requireNonNull(str, "null 'str' detected");
        }

        private static void checkIndexInRange(int index, int from, int to, String errorMsg) {
            if (index < from || index > to) {
                throw new IllegalArgumentException(errorMsg);
            }
        }

        private String fullStr() {
            return str.substring(start);
        }

        @Override
        public int compareTo(StringSuffix other) {
            return fullStr().compareTo(other.fullStr());
        }

        @Override
        public String toString() {
            return fullStr();
        }

        public int compareWithPattern(String pattern) {
            int j = 0;
            for (int i = start; i < str.length() && j < pattern.length(); ++i, ++j) {
                int charCmp = Character.compare(str.charAt(i), pattern.charAt(j));
                if (charCmp != 0) {
                    return charCmp;
                }
            }

            return 0;
        }
    }

    //====================== MAIN ======================

    public static void main(String[] args) {

        final String text = "GATAGACA$";

        SuffixArray suffixArray = new SuffixArray(text);
//        System.out.println(suffixArray);

        final String pattern = "GA";

        int index = suffixArray.find(pattern);

        if (index == -1) {
            System.out.println("Not found at ALL");
        }
        else {
            while (index != -1) {
                System.out.printf("Found at %d inside str '%s'\n", index, text);
                index = suffixArray.find(pattern, index + pattern.length());
            }
        }
    }


}
