package com.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.BeforeClass;

import java.io.IOException;
import java.io.InputStream;

public class BaseTest {
    private static SqlSessionFactory sqlSessionFactory;
    private SqlSession session;

    @BeforeClass
    public static void init(){
        try {
            InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SqlSession getSqlSession(){
        session = sqlSessionFactory.openSession();
        return session;
    }

    @After  //用于在测试方法之后执行
    public void destory() {
        session.close();
    }


}
