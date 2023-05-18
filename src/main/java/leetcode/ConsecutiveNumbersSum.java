package leetcode;

public class ConsecutiveNumbersSum {



    public static void main(String[] args) throws Exception {

        int val = 246_854_111;
        long startTime = System.nanoTime();
        int cnt = consecutiveNumbersSum(val);
        long endTime = System.nanoTime();

        System.out.printf("cnt: %d, time: %d ms\n", cnt, (endTime - startTime) / 1_000_000);

        System.out.println("ConsecutiveNumbersSum done...");
    }


    /**
     * time: O(N), where N = val
     * space: O(1)
     */
    public static int consecutiveNumbersSum(int val) {

        assert val >= 1;

        if( val < 3 ){
            return 1;
        }

        int left = 1;
        int right = 1;

        int sum = 1;
        int cnt = 1;

        while (right <= (val/2) + 1) {
            if (sum == val) {
                ++cnt;

                sum -= left;
                ++left;

                ++right;
                sum += right;
            }
            else if (sum < val) {
                ++right;
                sum += right;
            }
            else {
                sum -= left;
                ++left;
            }
        }

        return cnt;
    }
}
