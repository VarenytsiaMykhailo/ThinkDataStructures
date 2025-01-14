package com.github.varenytsiamykhailo.thinkdast;

import java.util.*;

/**
 * Implementation of a Map using a binary search tree.
 *
 * @param <K>
 * @param <V>
 */
public class MyTreeMap<K, V> implements Map<K, V> {

    private int size = 0;

    private Node root = null;

    /**
     * Represents a node in the tree.
     */
    protected class Node {
        public K key;
        public V value;

        public Node left = null;
        public Node right = null;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public boolean containsKey(Object target) {
        return findNode(target) != null;
    }

    /**
     * Returns the entry that contains the target key, or null if there is none.
     *
     * @param target
     */
    private Node findNode(Object target) {
        // some implementations can handle null as a key, but not this one
        if (target == null) {
            throw new IllegalArgumentException();
        }

        // something to make the compiler happy
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) target;

        Node currentNode = root;

        while (currentNode != null) {
            int compareValue = k.compareTo(currentNode.key);
            if (compareValue < 0)
                currentNode = currentNode.left;
            else if (compareValue > 0)
                currentNode = currentNode.right;
            else
                return currentNode;
        }

        return null;
    }

    /**
     * Compares two keys or two values, handling null correctly.
     *
     * @param target
     * @param obj
     * @return
     */
    private boolean equals(Object target, Object obj) {
        if (target == null) {
            return obj == null;
        }
        return target.equals(obj);
    }

    @Override
    public boolean containsValue(Object target) {
        return containsValueHelper(root, target);
    }

    /* my Realisation
    private boolean containsValueHelper(Node node, Object target) {
        boolean returnValueOfRecursiveCall = false;

        if (equals(target, node.value)) {
            return true;
        } else {
            if (node.left != null) {
                returnValueOfRecursiveCall = containsValueHelper(node.left, target);
            }
            if (node.right != null && returnValueOfRecursiveCall != true) {
                returnValueOfRecursiveCall = containsValueHelper(node.right, target);
            }
        }

        return returnValueOfRecursiveCall;
    }
     */

    private boolean containsValueHelper(Node node, Object target) {
        if (node == null) {
            return false;
        }
        if (equals(target, node.value)) {
            return true;
        }
        if (containsValueHelper(node.left, target)) {
            return true;
        }
        if (containsValueHelper(node.right, target)) {
            return true;
        }
        return false;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V get(Object key) {
        Node node = findNode(key);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new LinkedHashSet<K>();
        addInOrder(root, set);
        return set;
    }

    /* Walks the tree and adds the keys to `set`.
     *
     * node: root of the tree
     * set: set to add the nodes to
     */
    private void addInOrder(Node node, Set<K> set) {
        if (node == null)
            return;
        // Добавление ключей в LinkedHashSet в порядке возрастания
        // Используется реализация LinkedHashSet т.к. она поддерживает порядок элементов
        addInOrder(node.left, set);
        set.add(node.key);
        addInOrder(node.right, set);
    }

    @Override
    public V put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        return putHelper(root, key, value);
    }

    // My iterative realisation
    private V putHelper(Node node, K key, V value) {
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;

        Node currentNode = node;

        while (currentNode != null) {
            int compareValue = k.compareTo(currentNode.key);
            if (compareValue < 0)
                if (currentNode.left != null) {
                    currentNode = currentNode.left;
                } else {
                    currentNode.left = new Node(key, value);
                    size++;
                    return null;
                }
            else if (compareValue > 0)
                if (currentNode.right != null) {
                    currentNode = currentNode.right;
                } else {
                    currentNode.right = new Node(key, value);
                    size++;
                    return null;
                }
            else {
                V oldValue = currentNode.value;
                currentNode.value = value;
                return oldValue;
            }
        }

        return null;
    }

    /* Dawney recursive realisation:
    private V putHelper(Node node, K key, V value) {
		@SuppressWarnings("unchecked")
		Comparable<? super K> k = (Comparable<? super K>) key;
		int cmp = k.compareTo(node.key);

		if (cmp < 0) {
			if (node.left == null) {
				node.left = new Node(key, value);
				size++;
				return null;
			} else {
				return putHelper(node.left, key, value);
			}
		}
		if (cmp > 0) {
			if (node.right == null) {
				node.right = new Node(key, value);
				size++;
				return null;
			} else {
				return putHelper(node.right, key, value);
			}
		}
		V oldValue = node.value;
		node.value = value;
		return oldValue;
	}

     */

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public V remove(Object key) {
        // OPTIONAL TODO: FILL THIS IN!
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Collection<V> values() {
        Set<V> set = new HashSet<V>();
        Deque<Node> stack = new LinkedList<Node>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node node = stack.pop();
            if (node == null)
                continue;
            set.add(node.value);
            stack.push(node.left);
            stack.push(node.right);
        }
        return set;
    }


    // ------------------------- for junit tests

    /**
     * Makes a node.
     * <p>
     * This is only here for testing purposes.  Should not be used otherwise.
     *
     * @param key
     * @param value
     * @return
     */
    public MyTreeMap<K, V>.Node makeNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Sets the instance variables.
     * <p>
     * This is only here for testing purposes.  Should not be used otherwise.
     *
     * @param node
     * @param size
     */
    public void setTree(Node node, int size) {
        this.root = node;
        this.size = size;
    }

    /**
     * Returns the height of the tree.
     * <p>
     * This is only here for testing purposes.  Should not be used otherwise.
     *
     * @return
     */
    public int height() {
        return heightHelper(root);
    }

    private int heightHelper(Node node) {
        if (node == null) {
            return 0;
        }
        int left = heightHelper(node.left);
        int right = heightHelper(node.right);
        return Math.max(left, right) + 1;
    }

}
