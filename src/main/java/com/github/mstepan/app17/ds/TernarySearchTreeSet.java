package com.github.mstepan.app17.ds;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Objects;

/**
 * Ternary search tree data structure implementation for String ONLY elements.
 *
 * <p><a href="https://en.wikipedia.org/wiki/Ternary_search_tree">wiki/Ternary_search_tree</a>
 *
 * <p>Not thread safe.
 */
public class TernarySearchTreeSet extends AbstractSet<String> {

    private TernaryNode root;

    private int size;

    @Override
    public boolean add(String value) {
        Objects.requireNonNull(value, "Can't save null value. Nulls are prohibited.");

        if (root == null) {
            root = insertPath(value, 0);
            ++size;
            return true;
        }

        TernaryNode cur = root;
        TernaryNode prev = root;

        MoveDirection lastMove = null;

        int idx = 0;

        while (idx < value.length() && cur != null) {
            prev = cur;

            char valueCh = value.charAt(idx);

            if (cur.ch == valueCh) {
                ++idx;
                cur = cur.mid;
                lastMove = MoveDirection.MIDDLE;
            } else if (valueCh > cur.ch) {
                cur = cur.right;
                lastMove = MoveDirection.RIGHT;
            } else {
                cur = cur.left;
                lastMove = MoveDirection.LEFT;
            }
        }

        if (idx < value.length()) {
            switch (lastMove) {
                case MIDDLE -> prev.mid = insertPath(value, idx);
                case LEFT -> prev.left = insertPath(value, idx);
                case RIGHT -> prev.right = insertPath(value, idx);
            }

            ++size;
            return true;
        } else if (prev.isIntermediate()) {
            // trying to insert a node that is a prefix of previously inserted value
            prev.markAsTerminal();
            ++size;
            return true;
        }

        return false;
    }

    private TernaryNode insertPath(String value, int from) {
        assert value != null;
        assert from >= 0;
        assert from < value.length();

        TernaryNode first = TernaryNode.intermediate(value.charAt(from));
        TernaryNode last = first;

        for (int i = from + 1; i < value.length(); ++i) {
            TernaryNode newNode = TernaryNode.intermediate(value.charAt(i));
            last.mid = newNode;
            last = newNode;
        }

        last.markAsTerminal();

        return first;
    }

    @Override
    public boolean contains(Object val) {
        Objects.requireNonNull(val, "Can't search for null value. Nulls are prohibited.");

        if (val instanceof String value) {

            if (root == null) {
                return false;
            }

            int idx = 0;

            TernaryNode cur = root;
            TernaryNode lastVisited = root;

            while (cur != null && idx < value.length()) {

                lastVisited = cur;

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

            // We found a value only if both conditions are true:
            // 1. we have consumed all characters from a string;
            // 2. the last visited TernaryNode was a TERMINAL one.
            return idx == value.length() && lastVisited.isTerminal();
        } else {
            throw new IllegalArgumentException(
                    "Can't search for a value that is not a 'java.lang.String' type");
        }
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Delete not supported yet");
    }

    @Override
    public Iterator<String> iterator() {
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    private enum MoveDirection {
        LEFT,
        MIDDLE,
        RIGHT
    }

    private static final class TernaryNode {
        final char ch;
        NodeType type;

        TernaryNode left;
        TernaryNode mid;
        TernaryNode right;

        TernaryNode(char ch, NodeType type) {
            this.ch = ch;
            this.type = type;
        }

        private static TernaryNode intermediate(char ch) {
            return new TernaryNode(ch, NodeType.INTERMEDIATE);
        }

        @Override
        public String toString() {
            return ch + ", " + type;
        }

        public void markAsTerminal() {
            this.type = NodeType.TERMINAL;
        }

        public boolean isTerminal() {
            return type == NodeType.TERMINAL;
        }

        public boolean isIntermediate() {
            return type == NodeType.INTERMEDIATE;
        }
    }

    private enum NodeType {
        TERMINAL,
        INTERMEDIATE
    }
}
