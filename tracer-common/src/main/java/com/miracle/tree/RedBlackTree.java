package com.miracle.tree;

import java.util.List;
import java.util.function.Function;

/**
 * Description:红黑树
 *
 * @author guobin On date 2018/6/30.
 * @version 1.0
 * @since jdk 1.8
 */
public class RedBlackTree<K extends Comparable<K>, V> implements BinaryTree<K, V> {


    @Override
    public boolean insert(V value, Function<V, K> keySelector) {
        return false;
    }

    @Override
    public boolean remove(V value, Function<V, K> keySelector) {
        return false;
    }

    @Override
    public List<V> getValuesByKey(K key) {
        return null;
    }

    @Override
    public List<Node<K, V>> popPreNodes(K key) {
        return null;
    }

    @Override
    public List<Node<K, V>> popPostNodes(K key) {
        return null;
    }

    static class Entry<K, V> implements BinaryTree.Node<K, V> {

        @Override
        public Node<K, V> parent() {
            return null;
        }

        @Override
        public Node<K, V> left() {
            return null;
        }

        @Override
        public Node<K, V> right() {
            return null;
        }

        @Override
        public List<V> values() {
            return null;
        }

        @Override
        public K key() {
            return null;
        }
    }
}
