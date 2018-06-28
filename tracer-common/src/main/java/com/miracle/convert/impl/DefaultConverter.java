package com.miracle.convert.impl;

import com.miracle.convert.Converter;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;

import java.util.List;
import java.util.Optional;

/**
 * Description:数据转化器的默认实现
 * 默认采用dozer实现
 *
 * @author guobin On date 2018/6/27.
 * @version 1.0
 * @since jdk 1.8
 */
public class DefaultConverter implements Converter {

    private final Mapper mapper;

    public DefaultConverter(List<String> mappingFiles) {
        final int size = Optional.ofNullable(mappingFiles)
                .map(List::size)
                .orElse(0);
        String[] fileArr = new String[size];
        this.mapper = DozerBeanMapperBuilder.create()
                .withMappingFiles(mappingFiles.toArray(fileArr))
                .build();
    }

    @Override
    public <SRC, DEST> void mapTo(SRC src, DEST dest) {
        this.mapper.map(src, dest);
    }

    @Override
    public <SRC, DEST> DEST convert(SRC src, Class<DEST> destClass) {
        return this.mapper.map(src, destClass);
    }
}
