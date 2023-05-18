package leetcode.hard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 30. Substring with Concatenation of All Words
 * https://leetcode.com/problems/substring-with-concatenation-of-all-words/
 */
public class SubstringWithConcatenationOfAllWords {

    public static void main(String[] args) throws Exception {

        List<Integer> res = findSubstring("bcabbcaabbccacacbabccacaababcbb", new String[]{"c", "b", "a", "c", "a", "a", "a", "b", "c"});

        System.out.println(res);

        System.out.println("Main done...");
    }

    public static List<Integer> findSubstring(String str, String[] words) {

        List<String> wordsSorted = new ArrayList<>();
        Collections.addAll(wordsSorted, words);
        Collections.sort(wordsSorted);

        final int singleWordSize = words[0].length();
        final int windowSize = words.length * words[0].length();

        if (windowSize > str.length()) {
            return List.of();
        }

        final int allWordsHash = hashWords(words);
        int curWindowHash = hashRange(str, 0, windowSize - 1);

        List<Integer> res = new ArrayList<>();
        if (allWordsHash == curWindowHash && isPermutation(str, 0, windowSize - 1, wordsSorted, singleWordSize)) {
            res.add(0);
        }

        for (int last = windowSize; last < str.length(); ++last) {

            int charToRemIndex = last - windowSize;

            curWindowHash = curWindowHash ^ str.charAt(charToRemIndex) ^ str.charAt(last);

            if (allWordsHash == curWindowHash && isPermutation(str, charToRemIndex + 1, last, wordsSorted, singleWordSize)) {
                res.add(charToRemIndex + 1);
            }
        }

        return res;
    }

    private static boolean isPermutation(String str, int from, int to, List<String> wordsSorted, int singleWordSize) {
        List<String> other = new ArrayList<>();

        for (int index = from; index <= to; index += singleWordSize) {
            other.add(str.substring(index, index + singleWordSize));
        }

        Collections.sort(other);

        return wordsSorted.equals(other);
    }

    private static int hashWords(String[] words) {

        int hashVal = 0;

        for (String singleWord : words) {
            for (int i = 0; i < singleWord.length(); ++i) {
                hashVal ^= singleWord.charAt(i);
            }
        }

        return hashVal;
    }

    private static int hashRange(String str, int from, int to) {
        int hashVal = 0;

        for (int i = from; i <= to; ++i) {
            hashVal ^= str.charAt(i);
        }

        return hashVal;
    }
}
