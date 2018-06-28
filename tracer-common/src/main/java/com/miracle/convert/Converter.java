package com.miracle.convert;

/**
 * Description:数据转换器,实现两种功能:
 * 1.将源数据转化成目的类型的数据
 * 2.将源数据中的属性映射到已有目的数据中的对应属性
 *
 * @author guobin On date 2018/6/27.
 * @version 1.0
 * @since jdk 1.8
 */
public interface Converter {

    /**
     * 将{@link SRC}类型的源数据映射到已有的{@link DEST}类型数据的对应属性中
     * @param src 源数据
     * @param dest 目的数据
     * @param <SRC> 源数据类型
     * @param <DEST> 目的数据类型
     */
    <SRC, DEST> void mapTo(SRC src, DEST dest);

    /**
     * 将{@link SRC}类型的数据转化成{@link DEST}类型的数据之中
     * @param src 源数据
     * @param destClass 目的数据的类型对象
     * @param <SRC> 源数据类型
     * @param <DEST> 目的数据的类型
     * @return
     */
    <SRC, DEST> DEST convert(SRC src, Class<DEST> destClass);
}
