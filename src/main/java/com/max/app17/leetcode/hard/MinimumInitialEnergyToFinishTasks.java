package com.max.app17.leetcode.hard;


import java.util.Arrays;
import java.util.Comparator;

/*
1665. Minimum Initial Energy to Finish Tasks

https://leetcode.com/problems/minimum-initial-energy-to-finish-tasks/
 */
public class MinimumInitialEnergyToFinishTasks {

    public static void main(String[] args) throws Exception {

        // expected = 8
        //int[][] tasks = {{1, 2}, {2, 4}, {4, 8}};

        //expected = 32
        //int[][] tasks = {{1, 3}, {2, 4}, {10, 11}, {10, 12}, {8, 9}};

        //expected = 27
        int[][] tasks = {{1, 7}, {2, 8}, {3, 9}, {4, 10}, {5, 11}, {6, 12}};

        int res = new MinimumInitialEnergyToFinishTasks().minimumEffort(tasks);

        System.out.printf("res: %d%n", res);

        System.out.println("MinimumInitialEnergyToFinishTasks done...");
    }

    /*
     * Binary search the answer.
     * N = arr.length K = (10^4*10^5) = 10^9
     * time: O(N*lgN + N*lgK)
     * space: O(N)
     */
    public int minimumEffort(int[][] arr) {
        Task[] tasks = toTaskEntities(arr);

        // sort array according to the criteria: ('minimum' - 'actual') ASC
        Arrays.sort(
                tasks,
                Comparator.comparing((Task task) -> task.minimum() - task.actual()).reversed());

        int from = taskWithMaxMinValue(tasks);
        int to = sumOfMins(tasks);

        int answer = to;

        while (from <= to) {
            int mid = from + (to - from) / 2;

            if (isSolutionForProblem(mid, tasks)) {
                answer = mid;
                to = mid - 1;
            } else {
                from = mid + 1;
            }
        }

        return answer;
    }

    private boolean isSolutionForProblem(int value, Task[] tasks) {
        for (Task singleTask : tasks) {
            if (value < singleTask.minimum()) {
                return false;
            }

            value -= singleTask.actual();
        }

        return value >= 0;
    }

    private int sumOfMins(Task[] tasks) {
        // can't overflow here, so we can use 'int' instead of 'long'
        // maximum possible value (10^5 * 10^4) = 10^9
        int sum = 0;

        for (Task singleTask : tasks) {
            sum += singleTask.minimum();
        }

        return sum;
    }

    private int taskWithMaxMinValue(Task[] tasks) {

        int maxVal = tasks[0].minimum;

        for (Task singleTask : tasks) {
            maxVal = Math.max(maxVal, singleTask.minimum);
        }

        return maxVal;
    }

    private Task[] toTaskEntities(int[][] arr) {
        Task[] res = new Task[arr.length];
        for (int i = 0; i < arr.length; ++i) {
            res[i] = new Task(arr[i][0], arr[i][1]);
        }
        return res;
    }

    record Task(int actual, int minimum) {}
}
