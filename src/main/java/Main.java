import java.util.Objects;

public class Main {

    public static void main(String[] args) throws Exception {


        System.out.println("Main done...");
    }

    static class UnrolledLinkedList<E> {

    }

    /*

    https://confluence.oraclecorp.com/confluence/display/BMCS/First+Not+Repeating+Character

    Given a string s consisting of small English letters, find and return the first instance of a non-repeating character in it.
    If there is no such character, return '_'.

    Example:

    For s = "abacabad", the output should be
    firstNotRepeatingCharacter(s) = 'c'.

    There are 2 non-repeating characters in the string: 'c' and 'd'. Return 'c' since it appears in the string first.

    For s = "abacabaabacaba", the output should be
    firstNotRepeatingCharacter(s) = '_'.

    There are no characters in this string that do not repeat.
     */

    /**
     * time: O(N)
     * space: O(1)
     */
    private static final int LOWER_CASE_LETTERS_COUNT = 'z' - 'a' + 1;

    private static final char FIRST_CH = 'a';

    private static final char NOT_FOUNT_CH = '_';

    static char firstNotRepeatingCharacter(String str) {
        Objects.requireNonNull(str);

        int[] charsFreq = new int[LOWER_CASE_LETTERS_COUNT];

        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);

            if (!isLowercaseEnglishChar(ch)) {
                throw new IllegalArgumentException(
                    "Not a lowercase letter detected inside string '%s', ch = %c, position = %d" .
                        formatted(str, ch, i));
            }

            int index = ch - FIRST_CH;

            assert index >= 0 && index < charsFreq.length;

            charsFreq[index] += 1;
        }

        for (int i = 0; i < str.length(); ++i) {
            char curCh = str.charAt(i);
            int index = curCh - FIRST_CH;

            assert index >= 0 && index < charsFreq.length;

            if (charsFreq[index] == 1) {
                return curCh;
            }
        }

        return NOT_FOUNT_CH;
    }

    private static boolean isLowercaseEnglishChar(char ch) {
        return ch >= 'a' && ch <= 'z';
    }
}
