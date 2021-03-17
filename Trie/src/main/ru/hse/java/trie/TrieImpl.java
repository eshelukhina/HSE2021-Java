package ru.hse.java.trie;

import java.util.HashMap;
import java.util.Map;

public class TrieImpl implements Trie {
    private final Node root = new Node();

    @Override
    public boolean add(String element) {
        isStringOnlyAlphabet(element);

        if (contains(element)) {
            return false;
        }
        Node node = root;
        for (char i : element.toCharArray()) {
            node.size++;
            node = node.createNode(i);
        }
        node.size++;

        node.isEnd = true;
        return true;
    }

    @Override
    public boolean contains(String element) {
        isStringOnlyAlphabet(element);

        Node node = root;
        for (char i : element.toCharArray()) {
            if (node.canMove(i)) {
                return false;
            }
            node = node.getNode(i);
        }
        return node.isEnd;
    }

    @Override
    public boolean remove(String element) {
        isStringOnlyAlphabet(element);

        if (!contains(element)) {
            return false;
        }

        Node node = root;

        for (char i : element.toCharArray()) {
            if (node.getNode(i).size == 1) {
                node.child.remove(i);
                node.size--;
                return true;
            } else {
                node.size--;
                node = node.getNode(i);
            }
        }

        node.size--;
        node.isEnd = false;
        return true;
    }


    @Override
    public int size() {
        return root.size;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        isStringOnlyAlphabet(prefix);

        Node node = root;
        for (char i : prefix.toCharArray()) {
            if (node.canMove(i)) return 0;
            node = node.getNode(i);
        }
        return node.size;
    }

    @Override
    public String nextString(String element, int k) {
        isStringOnlyAlphabet(element);

        if (k == 0) {
            if (contains(element)) {
                return element;
            } else {
                return null;
            }
        }
        return getKthString(getNumber(element) + k, root);
    }

    private int getNumber(String element) {
        int number = 0;
        Node node = root;
        boolean wordContains = false;
        int elemLength = 0;
        for (char i : element.toCharArray()) {
            for (Map.Entry<Character, Node> e : node.child.entrySet()) {
                if (e.getKey() < i) {
                    number += e.getValue().size;
                } else if (e.getValue().isEnd && e.getKey() == i) {
                    if (elemLength != element.length() - 1) {
                        number++;
                    } else if (elemLength == element.length() - 1) {
                        wordContains = true;
                    }
                }
            }
            if (node.child.containsKey(i)) {
                node = node.getNode(i);
            } else {
                break;
            }
            elemLength++;
        }

        return number + 1 - (wordContains ? 0 : 1);
    }

    private String getKthString(int k, Node node) {
        StringBuilder string = new StringBuilder();
        while (k > 0 && node.size > 0) {
            int sum = 0;
            int prevSum;
            for (Map.Entry<Character, Node> e : node.child.entrySet()) {
                prevSum = sum;
                sum += e.getValue().size;
                if (sum >= k) {
                    string.append(e.getKey());
                    if (e.getValue().isEnd && k == sum - e.getValue().size + 1) {
                        return string.toString();
                    }
                    k -= prevSum + (e.getValue().isEnd ? 1 : 0);
                    node = node.child.get(e.getKey());
                    break;
                }
            }
            if (sum < k) {
                break;
            }
        }
        return null;
    }

    private static void isStringOnlyAlphabet(String string) throws IllegalArgumentException {
        if (!((string != null) && (string.matches("^[a-zA-Z]*$")))) {
            throw new IllegalArgumentException("Argument must contains only latin alphabet!");
        }
    }

    private static class Node {
        private final HashMap<Character, Node> child = new HashMap<>();
        private boolean isEnd = false;
        private int size = 0;

        private Node createNode(char symbol) {
            if (!child.containsKey(symbol)) {
                child.put(symbol, new Node());
            }
            return child.get(symbol);
        }

        private Node getNode(char symbol) {
            return child.get(symbol);
        }

        private boolean canMove(char symbol) {
            return !child.containsKey(symbol);
        }
    }
}
