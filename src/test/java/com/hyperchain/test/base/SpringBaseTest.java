package com.hyperchain.test.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringBaseTest {
    @Test
    public void testDemo() throws Exception {
        System.out.print("待补充，需要改为assert写法");
    }

}
