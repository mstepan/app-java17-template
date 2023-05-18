package leetcode.medium;

import java.util.ArrayDeque;

/**
 * 946. Validate Stack Sequences
 * https://leetcode.com/problems/validate-stack-sequences/
 */
public class ValidateStackSequences {


    public static void main(String[] args) throws Exception {

        boolean res = validateStackSequences(new int[] {2, 1, 0}, new int[] {1, 2, 0});

        System.out.println(res);

        System.out.println("ValidateStackSequences done...");
    }

    public static boolean validateStackSequences(int[] pushed, int[] popped) {

        if (pushed.length != popped.length) {
            return false;
        }

        int i = 0;
        int j = 0;

        ArrayDeque<Integer> stack = new ArrayDeque<>();

        while (i < pushed.length) {

            while (pushed[i] != popped[j]) {
                stack.push(pushed[i]);
                ++i;

                if (i == pushed.length) {
                    return false;
                }
            }

            // move second
            ++j;

            while (!stack.isEmpty() && popped[j] == stack.peek()) {
                stack.pop();
                ++j;
            }

            ++i;
        }

        return isStackMatched(stack, popped, j);
    }

    private static boolean isStackMatched(ArrayDeque<Integer> stack, int[] arr2, int j) {

        int leftCount = arr2.length - j;

        if (stack.size() != leftCount) {
            return false;
        }

        for (int index = j; index < arr2.length && !stack.isEmpty(); ++index) {
            if (stack.pop() != arr2[index]) {
                return false;
            }
        }

        return true;
    }
}
