package com.hyperchain.dal;


import com.hyperchain.dal.entity.EntityDemo;
import com.hyperchain.dal.repository.EntityDemoRepository;
import com.hyperchain.test.base.SpringBaseTest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;


/**
 * Created by chenxiaoyang on 2017/3/30.
 */
public class DemoEntityTest extends SpringBaseTest {

    //private static Log demoLog = LogFactory.getLog("Reparo");

    @Autowired
    private EntityDemoRepository demo_entity;

    @Test
    public void testInsert() {

        EntityDemo entity = new EntityDemo();
        entity.setId(2L);
        entity.setNick_name("this is a demo of insert");
        entity.setPassword("123");
        entity.setPhone_number("123345678");
        entity = demo_entity.save(entity);
        System.out.println(entity);
        //demoLog.info(entity.toString());
        assertEquals(entity.getNick_name(), "this is a demo of insert");

    }

    @Test
    public void testQuery() {
        EntityDemo entity = demo_entity.findByid(2L);
        System.out.println(entity);
        //demoLog.info(entity.toString());
        assertEquals(entity.getNick_name(), "this is a demo of insert");
    }
}
