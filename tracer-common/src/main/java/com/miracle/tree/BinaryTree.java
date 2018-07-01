package com.miracle.tree;

import java.util.List;
import java.util.function.Function;

/**
 * Description:二叉树接口
 *
 * @author guobin On date 2018/6/30.
 * @version 1.0
 * @since jdk 1.8
 */
public interface BinaryTree<K extends Comparable<K>, V> {

    /**
     * 向树中插入一条数据
     * @param value 数据
     * @param keySelector key选择器
     * @return true表示操作成功,false失败
     */
    boolean insert(V value, Function<V, K> keySelector);

    /**
     * 从树中删除一条数据
     * @param value 数据
     * @param keySelector key选择器
     * @return true表示操作成功,false失败
     */
    boolean remove(V value, Function<V, K> keySelector);

    /**
     * 根据key来查找key所在节点中所有的数据
     * @param key 查询的key
     * @return 查询出来的数据列表
     */
    List<V> getValuesByKey(K key);

    /**
     * 根据key进行查找,弹出所有较key值小的节点
     * @param key 查询的key
     * @return 查找出来并被移除的节点list,不会为null,{@code size}最多为2:父节点(一颗树)+左子节点(一颗树)
     */
    List<Node<K, V>> popPreNodes(K key);

    /**
     * 根据key进行查找,弹出所有较key值大的节点
     * @param key 查询的key
     * @return 查找出来的节点list,不会为null,{@code size}最多为2:父节点+右子节点
     */
    List<Node<K, V>> popPostNodes(K key);

    /**
     * 存储在树种的节点
     * @param <K> key值类型
     * @param <V> value值类型
     */
    interface Node<K, V> {

        /**
         * 获取父节点
         * @return 父节点
         */
        Node<K, V> parent();

        /**
         * 获取左子节点
         * @return 左子节点
         */
        Node<K, V> left();

        /**
         * 获取右子节点
         * @return 右子节点
         */
        Node<K, V> right();

        /**
         * 获取节点中存储的所有数据
         * @return 数据列表
         */
        List<V> values();

        /**
         * 获取节点的key值
         * @return key值
         */
        K key();
    }
}
