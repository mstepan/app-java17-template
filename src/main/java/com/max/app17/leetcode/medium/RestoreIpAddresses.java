package com.max.app17.leetcode.medium;

import java.util.ArrayList;
import java.util.List;

/** 93. Restore IP Addresses https://leetcode.com/problems/restore-ip-addresses/ */
public class RestoreIpAddresses {

    public static void main(String[] args) throws Exception {

        String[] testCases = new String[] {"25525511135", "0000", "101023"};

        for (String singleTestCase : testCases) {

            List<String> allIps = new RestoreIpAddresses().restoreIpAddresses(singleTestCase);

            System.out.println();
            System.out.printf("IP to decompose '%s'", singleTestCase);
            System.out.println();

            for (String singleIp : allIps) {
                System.out.println(singleIp);
            }
        }

        System.out.println("RestoreIpAddresses done...");
    }

    public List<String> restoreIpAddresses(String str) {
        return restoreIpRec(str.toCharArray(), str.length() - 1, 4);
    }

    private List<String> restoreIpRec(char[] arr, int offset, int leftBytesCnt) {
        if (offset < 0 || leftBytesCnt < 1) {
            return List.of();
        }

        if (leftBytesCnt == 1) {
            int digitsCnt = offset + 1;

            if (digitsCnt > 3) {
                return List.of();
            }

            int val = toInt(arr, 0, offset);
            if (val > 255 || (digitsCnt > 1 && arr[0] == '0')) {
                return List.of();
            }

            return List.of(String.valueOf(arr, 0, offset + 1));
        }

        int curVal = 0;
        int base = 1;
        int index = offset;

        List<String> res = new ArrayList<>();

        while (index >= 0) {
            int digit = arr[index] - '0';
            curVal += (digit * base);

            if (curVal > 255 || base > 100) {
                break;
            }

            // skip 0 started elements if this is not the 1-st iteration
            // values that will be skipped: '01', '005', '015'
            // not skipped values: '0'
            if (digit == 0 && base > 1) {
                --index;
                base *= 10;
                continue;
            }

            List<String> leftSideRes = restoreIpRec(arr, index - 1, leftBytesCnt - 1);

            for (String leftSide : leftSideRes) {
                res.add("%s.%d".formatted(leftSide, curVal));
            }

            --index;
            base *= 10;
        }

        return res;
    }

    private int toInt(char[] arr, int from, int to) {
        int val = 0;

        for (int i = from; i <= to; ++i) {
            int digit = arr[i] - '0';
            val = val * 10 + digit;
        }

        return val;
    }
}
