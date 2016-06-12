package com.gdwii.util.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class MybatisUtil {
    private static SqlSessionFactory sqlSessionFactory;

    public MybatisUtil(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public static <T,G> void batchOperator(MybatisOperator<T,G> operator, Class<T> mapperClass, List<G> values){
        if(values == null || values.isEmpty()){
            return;
        }

        // 获取批量操作Mapper
        SqlSession session = sqlSessionFactory.openSession(false);
        T mapper = session.getMapper(mapperClass);
        for(G value : values){
            operator.operate(mapper, value);
        }
        session.close();
    }
}
