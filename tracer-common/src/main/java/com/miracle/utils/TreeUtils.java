package com.miracle.utils;

import com.miracle.tree.BinaryTree;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Description:{@link com.miracle.tree.BinaryTree}的工具类
 *
 * @author guobin On date 2018/7/1.
 * @version 1.0
 * @since jdk 1.8
 */
public class TreeUtils {

    /**
     * 不允许实例化
     */
    private TreeUtils() {}

    /**
     * 对指定的节点进行前遍历,依据指定的数据转换器返回结果
     * @param node 需要前遍历的节点
     * @param mapper 数据转化器,将节点中的数据转化成指定的数据
     * @param <U> 指定的数据类型
     * @param <K> node中key的类型
     * @param <V> node中value的类型
     * @return 指定的数据列表
     */
    public static <K, V, U> List<U> applyPreOrderTraversal(BinaryTree.Node<K, V> node,
                                                           Function<BinaryTree.Node<K, V>, U> mapper) {
        if (node == null) {
            return Collections.emptyList();
        }
        final List<U> result = new LinkedList<>();
        result.add(mapper.apply(node));
        result.addAll(applyPreOrderTraversal(node.left(), mapper));
        result.addAll(applyPreOrderTraversal(node.right(), mapper));

        return result;
    }

    /**
     * 对指定的节点进行前遍历消费
     * @param node 需要前遍历的节点
     * @param consumer 节点数据消费者
     * @param <K> node中key的类型
     * @param <V> node中value的类型
     */
    public static <K, V> void acceptPreOrderTraversal(BinaryTree.Node<K, V> node,
                                                      Consumer<BinaryTree.Node<K, V>> consumer) {
        if (node == null) {
            return;
        }
        consumer.accept(node);
        acceptPreOrderTraversal(node.left(), consumer);
        acceptPreOrderTraversal(node.right(), consumer);
    }

    /**
     * 递归查找一颗树的根节点
     * @param node 当前节点
     * @return 根节点
     */
    public static <K, V> BinaryTree.Node<K, V> findRootNode(BinaryTree.Node<K, V> node) {
        return node.parent() == null ? node : findRootNode(node.parent());
    }
}
