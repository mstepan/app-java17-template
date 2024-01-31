package com.max.app17.geeks;


/**
 *
 * Complete the Insert and Search functions for a Trie Data Structure.
 *
 * Insert: Accepts the Trie's root and a string, modifies the root in-place, and returns nothing.
 * Search: Takes the Trie's root and a string, returns true if the string is in the Trie, otherwise false.
 * Note: To test the correctness of your code, the code-judge will be inserting a list of N strings called into 
 * the Trie, and then will search for the string key in the Trie. The code-judge will generate 1 if the key is 
 * present in the Trie, else 0.
 *
 * https://www.geeksforgeeks.org/problems/trie-insert-and-search0651/1?utm_source=geeksforgeeks&utm_medium=newui_home&utm_campaign=potd
 */
public class TrieInsertAndSearch {
    public static void main(String[] args) throws Exception {

        String[] words = {"the", "a", "there", "answer", "any", "by", "bye", "their"};
        String[] notExistedWords = {"this", "them", "at", "answers"};

        TrieNode root = new TrieNode();

        for (String singleWord : words) {
            insert(root, singleWord);
        }

        for (String singleWord : words) {
            System.out.printf("search(%s): %b%n", singleWord, search(root, singleWord));
        }

        System.out.println("==================");

        for (String notExistedSingleWord : notExistedWords) {
            System.out.printf("search(%s): %b%n", notExistedSingleWord, search(root, notExistedSingleWord));
        }

        System.out.println("TrieInsertAndSearch done...");
    }

    static void insert(TrieNode root, String key) {
        assert root != null;
        char[] keyArr = key.toCharArray();

        TrieNode last = root;

        for (char ch : keyArr) {

            int chIdx = toIndex(ch);

            if( last.children[chIdx] == null ){
                last.children[chIdx] = new TrieNode();
            }

            last = last.children[chIdx];
        }

        last.isEndOfWord = true;
    }

    static boolean search(TrieNode root, String key) {
        assert root != null;
        char[] keyArr = key.toCharArray();

        TrieNode cur = root;

        for( char ch : keyArr ){
            if( cur == null){
                return false;
            }

            cur = cur.children[toIndex(ch)];
        }

        return cur != null && cur.isEndOfWord;
    }

    private static int toIndex(char ch){
        int chIdx = ch - 'a';
        assert chIdx >= 0 && chIdx < ALPHABET_SIZE;
        return chIdx;
    }

    // ===== do not copy below code ===

    private static final int ALPHABET_SIZE = 26;

    // trie node
    private static class TrieNode {
        TrieNode[] children = new TrieNode[ALPHABET_SIZE];

        // isEndOfWord is true if the node represents
        // end of a word
        boolean isEndOfWord;

        TrieNode() {
            isEndOfWord = false;
            for (int i = 0; i < ALPHABET_SIZE; i++) {
                children[i] = null;
            }
        }
    }
}
