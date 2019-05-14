package com.gdwii.util.mybatis;

/**
 * mybatis的数据操作
 */
@FunctionalInterface
public interface MybatisOperator<T,G> {
    void operate(T mapper, G value);
}
