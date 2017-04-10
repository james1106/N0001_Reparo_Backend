package com.hyperchain.dal.repository;

import com.hyperchain.dal.entity.SecurityCodeEntity;
import com.hyperchain.test.base.SpringBaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by ldy on 2017/4/5.
 */
public class SecurityCodeEntityRepositoryTest extends SpringBaseTest {

    @Autowired
    SecurityCodeEntityRepository securityCodeEntityRepository;

    @Test
    public void testSecurityCode() throws Exception {
        //插入
        SecurityCodeEntity securityCodeEntity = new SecurityCodeEntity();
        securityCodeEntity.setPhone("188188188");
        securityCodeEntity.setCreateTime(new Long(10000));
        securityCodeEntity.setErrorCodeCount(0);
        securityCodeEntity.setSecurityCode("663366");
        SecurityCodeEntity insertedSecurityCodeEntity = securityCodeEntityRepository.save(securityCodeEntity);

        //查询
        long insertedId = insertedSecurityCodeEntity.getId();
        SecurityCodeEntity securityCodeEntity1 = securityCodeEntityRepository.findById(insertedId);
        System.out.println("更新前：" + securityCodeEntity1.toString());
        Assert.assertEquals("188188188", securityCodeEntity1.getPhone());

        //更新
        int pre = securityCodeEntity1.getErrorCodeCount();
        securityCodeEntity1.setErrorCodeCount( pre+ 1);
        SecurityCodeEntity savedSecurityCodeEntity = securityCodeEntityRepository.save(securityCodeEntity1);
        int after = savedSecurityCodeEntity.getErrorCodeCount();
        System.out.println("更新后：" + savedSecurityCodeEntity.toString());
        Assert.assertEquals(pre + 1, after);


        //删除
        long id = savedSecurityCodeEntity.getId();
        securityCodeEntityRepository.delete(savedSecurityCodeEntity);
        SecurityCodeEntity deleted = securityCodeEntityRepository.findById(id);
        Assert.assertEquals(null, deleted);
    }

}