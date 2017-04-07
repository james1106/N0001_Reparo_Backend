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
    public void insert() throws Exception {
        SecurityCodeEntity securityCodeEntity = new SecurityCodeEntity();
        securityCodeEntity.setPhone("188188188");
        securityCodeEntity.setCreateTime(new Long(10000));
        securityCodeEntity.setErrorCodeCount(0);
        securityCodeEntity.setSecurityCode("663366");
        securityCodeEntityRepository.save(securityCodeEntity);

        SecurityCodeEntity securityCodeEntity1 = securityCodeEntityRepository.findById(new Long(1));
        System.out.println(securityCodeEntity1.toString());
        Assert.assertEquals("188188188", securityCodeEntity1.getPhone());

    }

    @Test
    public void update() {
        SecurityCodeEntity securityCodeEntity0 = new SecurityCodeEntity();
        securityCodeEntity0.setPhone("188188188");
        securityCodeEntity0.setCreateTime(new Long(10000));
        securityCodeEntity0.setErrorCodeCount(0);
        securityCodeEntity0.setSecurityCode("663366");
        securityCodeEntityRepository.save(securityCodeEntity0);

        SecurityCodeEntity securityCodeEntity = securityCodeEntityRepository.findById(securityCodeEntity0.getId());
        System.out.println("更新前：" + securityCodeEntity.toString());
        securityCodeEntity.setErrorCodeCount(securityCodeEntity.getErrorCodeCount() + 1);
        SecurityCodeEntity securityCodeEntity1 = securityCodeEntityRepository.save(securityCodeEntity);
        System.out.println("更新后：" + securityCodeEntity1.toString());
        Assert.assertNotEquals(securityCodeEntity.getErrorCodeCount(), securityCodeEntity1.getErrorCodeCount());
    }

    @Test
    public void delete() {
        SecurityCodeEntity securityCodeEntity0 = new SecurityCodeEntity();
        securityCodeEntity0.setPhone("188188188");
        securityCodeEntity0.setCreateTime(new Long(10000));
        securityCodeEntity0.setErrorCodeCount(0);
        securityCodeEntity0.setSecurityCode("663366");
        SecurityCodeEntity securityCodeEntity1 = securityCodeEntityRepository.save(securityCodeEntity0);
        System.out.println("插入： " + securityCodeEntity1.toString());

        long id = securityCodeEntity1.getId();
        securityCodeEntityRepository.delete(securityCodeEntity1);
        SecurityCodeEntity securityCodeEntity2 = securityCodeEntityRepository.findById(id);
        Assert.assertEquals(securityCodeEntity2, null);
        if (securityCodeEntity2 == null) {
            System.out.println("删除成功！");
        }
    }
}