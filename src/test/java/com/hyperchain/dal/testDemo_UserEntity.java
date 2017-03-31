package com.hyperchain.dal;


import com.hyperchain.dal.entity.Demo_UserEntity;
import com.hyperchain.dal.repository.Demo_UserEntityRepository;
import com.hyperchain.test.base.SpringBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by chenxiaoyang on 2017/3/30.
 */
public class testDemo_UserEntity extends SpringBaseTest {
    @Autowired
    private Demo_UserEntity demo_userEntity;

    @Test
    public void testQuery() {
        //Demo_UserEntity usr = Demo_UserEntityRepository


       // System.out.println(list);
    }
}
