package facebook;

public class BinaryTreeToDoubleLinkedList {

    public static void main(String[] args) throws Exception {

        BinarySearchTree tree = new BinarySearchTree();

        tree.add(100);
        tree.add(50);
        tree.add(200);

        tree.add(25);
        tree.add(75);
        tree.add(125);
        tree.add(350);

        tree.add(30);
        tree.add(60);

        System.out.printf("TREE in-order: %s%n", tree.inOrderStr());

        tree.toListInPlace();

        System.out.printf("LIST order: %s%n", tree.toListStr());

        System.out.println("BinaryTreeToDoubleLinkedList done...");
    }

    enum NodeDirection {
        LEFT, RIGHT
    }

    static class BinarySearchTree {

        Node root;

        public boolean add(int value) {

            if (root == null) {
                root = new Node(value);
                return true;
            }

            Node foundNode = findNode(value);

            if (foundNode.value == value) {
                return false;
            }

            if (value < foundNode.value) {
                foundNode.left = new Node(value);
            }
            else {
                foundNode.right = new Node(value);
            }

            return true;
        }

        private Node findNode(int value) {

            Node cur = root;
            Node parent = cur;

            while (cur != null) {
                if (cur.value == value) {
                    return cur;
                }

                parent = cur;

                if (value < cur.value) {
                    cur = cur.left;
                }
                else {
                    cur = cur.right;
                }
            }

            return parent;
        }

        /**
         * time: O(h) ~ can be up to O(N)
         * space: O(1)
         */
        void toListInPlace() {
            if (root == null) {
                return;
            }

            root = buildListRec(root, NodeDirection.RIGHT);
        }

        private Node buildListRec(Node cur, NodeDirection direction) {
            if (cur == null) {
                return null;
            }

            Node leftSide = buildListRec(cur.left, NodeDirection.LEFT);
            cur.left = null;

            if (leftSide != null) {
                cur.left = leftSide;
                leftSide.right = cur;
            }

            Node rightSide = buildListRec(cur.right, NodeDirection.RIGHT);
            cur.right = null;

            if (rightSide != null) {
                cur.right = rightSide;
                rightSide.left = cur;
            }

            return (direction == NodeDirection.LEFT) ? findLast(cur) : findFirst(cur);
        }

        private Node findLast(Node cur) {
            assert cur != null : "cur is NULL";

            while (cur.right != null) {
                cur = cur.right;
            }

            return cur;
        }

        private Node findFirst(Node cur) {
            assert cur != null : "cur is NULL";

            while (cur.left != null) {
                cur = cur.left;
            }

            return cur;
        }

        public String inOrderStr() {

            StringBuilder buf = new StringBuilder();

            buf.append("[");

            appendInOrderRec(root, buf);

            buf.append("]");

            return buf.toString();
        }

        private void appendInOrderRec(Node cur, StringBuilder buf) {
            if (cur == null) {
                return;
            }

            if (cur.left != null) {
                appendInOrderRec(cur.left, buf);
            }

            buf.append(cur.value).append(", ");

            if (cur.right != null) {
                appendInOrderRec(cur.right, buf);
            }
        }

        public String toListStr() {
            StringBuilder res = new StringBuilder();

            res.append("[");

            Node cur = root;

            while (cur != null) {
                res.append(cur.value).append(", ");
                cur = cur.right;
            }

            res.append("]");

            return res.toString();
        }
    }


    static class Node {
        final int value;

        Node(int value) {
            this.value = value;
        }

        Node left;

        Node right;

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }


}
