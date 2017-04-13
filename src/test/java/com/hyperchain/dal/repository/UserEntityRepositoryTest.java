package com.hyperchain.dal.repository;

import com.hyperchain.dal.entity.UserEntity;
import com.hyperchain.test.TestUtil;
import com.hyperchain.test.base.SpringBaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by ldy on 2017/4/5.
 */
public class UserEntityRepositoryTest extends SpringBaseTest{

    @Autowired
    UserEntityRepository userEntityRepository;

    @Test
    public void test() throws Exception {

        //插入
        UserEntity userEntity = new UserEntity();
        String randomString = TestUtil.getRandomString();
        userEntity.setCompanyName("企业名称" + randomString);
        userEntity.setPhone("188188188" + randomString);
        userEntity.setAccountName("用户名" + randomString);
        userEntity.setUserStatus(0);
        userEntity.setPassword("123");
        userEntity.setRoleCode(0);
        userEntity.setErrorPasswordCount(0);
        userEntity.setPrivateKey("privateKey");
        userEntity.setAddress("address" + randomString);
        userEntity.setLockTime(new Long(10000));
//        userEntity.setCertType("身份证");
//        userEntity.setCertNo("1111");
//        userEntity.setAcctIds("1111");
//        userEntity.setSvcrClass("1111");
//        userEntity.setAcctSvcr("1111");
//        userEntity.setAcctSvcrName("中国银行");
        userEntityRepository.save(userEntity);

        //查找
        UserEntity userEntity1 = userEntityRepository.findByAccountName("用户名" + randomString);
        Assert.assertEquals(userEntity1.getAddress(), "address" + randomString);
        System.out.println(userEntity1.toString());


        UserEntity userEntity2 = userEntityRepository.findByPhone("188188188" + randomString);
        Assert.assertEquals(userEntity2.getAddress(), "address" + randomString);
        System.out.println(userEntity2.toString());

        UserEntity userEntity3 = userEntityRepository.findByAddress("address" + randomString);
        Assert.assertEquals(userEntity3.getAddress(), "address" + randomString);
        System.out.println(userEntity3.toString());

        //更新
        userEntity2.setPassword("111" + randomString);
        UserEntity updatedUserEntity = userEntityRepository.save(userEntity2);
        Assert.assertEquals(updatedUserEntity.getPassword(), "111" + randomString);

        //删除
        userEntityRepository.delete(updatedUserEntity);
        UserEntity deleted = userEntityRepository.findByAddress("address" + randomString);
        Assert.assertEquals(null, deleted);
    }

    @Test
    public void findByRoleCode() {
        List<UserEntity> companyUserEntityList = userEntityRepository.findByRoleCode(0);
        for (UserEntity companyUserEntity : companyUserEntityList) {
            System.out.println("企业名称：" + companyUserEntity.getCompanyName());
        }
        List<UserEntity> logisticsUserEntityList = userEntityRepository.findByRoleCode(1);
        for (UserEntity logisticsUser : logisticsUserEntityList) {
            System.out.println("物流名称：" + logisticsUser.getCompanyName());
        }
        List<UserEntity> repoUserEntityList = userEntityRepository.findByRoleCode(2);
        for (UserEntity repoUserEntity : repoUserEntityList) {
            System.out.println("仓储名称：" + repoUserEntity.getCompanyName());
        }
        List<UserEntity> financialUserEntityList = userEntityRepository.findByRoleCode(3);
        for (UserEntity financialUserEntity : financialUserEntityList) {
            System.out.println("银行名称：" + financialUserEntity.getCompanyName());
        }
    }


}