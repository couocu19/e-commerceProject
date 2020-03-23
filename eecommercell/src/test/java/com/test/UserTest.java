package com.test;


import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class UserTest extends BaseTest {

    @Test
    public void testFindAllUserAccount(){
        SqlSession session = getSqlSession();
        try {

            UserMapper userMapper = session.getMapper(UserMapper.class);

            List<User> list = userMapper.findAll();

            for(User u:list){
                System.out.println(u);
                if(u.getAccounts().size()>0){
                    System.out.println("该用户名下的账户有:");
                    for(Account a:u.getAccounts()){
                        System.out.println(a);
                    }
                }else{
                    System.out.println("该用户未开通账户");
                }
            }
        } finally {
            session.close();

        }
    }


}
