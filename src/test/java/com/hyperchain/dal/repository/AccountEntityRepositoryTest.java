package com.hyperchain.dal.repository;

import com.hyperchain.dal.entity.AccountEntity;
import com.hyperchain.test.TestUtil;
import com.hyperchain.test.base.SpringBaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by ldy on 2017/4/9.
 */
public class AccountEntityRepositoryTest extends SpringBaseTest{

    @Autowired
    AccountEntityRepository accountEntityRepository;

    @Test
    public void testAccount() throws Exception {
        //插入
        AccountEntity accountEntity = new AccountEntity();
        String randomString = TestUtil.getRandomString();
        accountEntity.setAddress("12345678");
        accountEntity.setAcctSvcrName("svcrname");
        accountEntity.setAcctSvcr("svcr");
        accountEntity.setAcctId("acctId");
        accountEntity.setCertNo("111111");
        accountEntity.setCertType("身份证");
        accountEntity.setSvcrClass("112233");
        AccountEntity accountEntityResult = accountEntityRepository.save(accountEntity);
        System.out.println("插入：" + accountEntityResult.toString());
        Assert.assertEquals("身份证", accountEntityResult.getCertType());

        //查询
        AccountEntity accountEntity1 = accountEntityRepository.findByAddress("12345678").get(0);
        System.out.println("查询结果：" + accountEntity1.toString());

        //更新
        accountEntity1.setCertType("新型身份证");
        AccountEntity accountEntityUpdatedResult = accountEntityRepository.save(accountEntity1);
        System.out.println("更新后："+ accountEntityUpdatedResult.toString());
        Assert.assertEquals("新型身份证", accountEntityUpdatedResult.getCertType());

        //删除
        accountEntityRepository.delete(accountEntityUpdatedResult);
        List<AccountEntity> deleted = accountEntityRepository.findByAddress("12345678");
        Assert.assertEquals(0, deleted.size());
    }

}