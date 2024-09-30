package com.github.mstepan.app17.ds;

import java.util.Objects;

/** Ternary search tree. */
public class TSTree {

    private TernaryNode root;

    public void add(String value) {
        Objects.requireNonNull(value, "Can't save null value in TSTree");

        if (root == null) {
            root = insertPath(value, 0);
            return;
        }

        TernaryNode cur = root;
        TernaryNode prev = null;
        int idx = 0;

        while (idx < value.length() && cur != null) {
            prev = cur;

            char valueCh = value.charAt(idx);

            if (cur.ch == valueCh) {
                ++idx;
                cur = cur.mid;
            } else if (valueCh > cur.ch) {
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }

        if (idx < value.length()) {
            char valueCh = value.charAt(idx);
            if (prev.ch == valueCh) {
                prev.mid = insertPath(value, idx);
            } else if (valueCh > prev.ch) {
                prev.right = insertPath(value, idx);
            } else {
                prev.left = insertPath(value, idx);
            }
        }
    }

    private TernaryNode insertPath(String value, int from) {
        assert value != null;
        assert from >= 0;
        assert from < value.length();

        TernaryNode first = new TernaryNode(value.charAt(from));
        TernaryNode last = first;

        for (int i = from + 1; i < value.length(); ++i) {
            TernaryNode newNode = new TernaryNode(value.charAt(i));
            last.mid = newNode;
            last = newNode;
        }

        return first;
    }

    public boolean contains(String value) {
        Objects.requireNonNull(value, "Can't search for null value in TSTree");

        int idx = 0;

        TernaryNode cur = root;

        while (cur != null && idx < value.length()) {
            char valueCh = value.charAt(idx);
            if (cur.ch == valueCh) {
                cur = cur.mid;
                ++idx;
            } else if (valueCh > cur.ch) {
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }

        return idx == value.length();
    }

    private static final class TernaryNode {
        final char ch;

        TernaryNode left;
        TernaryNode mid;
        TernaryNode right;

        TernaryNode(char ch) {
            this.ch = ch;
        }

        @Override
        public String toString() {
            return String.valueOf(ch);
        }
    }
}
