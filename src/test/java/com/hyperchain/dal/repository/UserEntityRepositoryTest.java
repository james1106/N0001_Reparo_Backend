package com.hyperchain.dal.repository;

import com.hyperchain.dal.entity.UserEntity;
import com.hyperchain.test.base.SpringBaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ldy on 2017/4/5.
 */
public class UserEntityRepositoryTest extends SpringBaseTest{

    @Autowired
    UserEntityRepository userEntityRepository;

    @Test
    public void save() throws Exception {

        UserEntity userEntity = new UserEntity();
        userEntity.setCompanyName("企业名称");
        userEntity.setPhone("188188188");
        userEntity.setAccount("用户名");
        userEntity.setUserStatus(0);
        userEntity.setPassword("123");
        userEntity.setRoleCode(0);
        userEntity.setErrorPasswordCount(0);
        userEntity.setPrivateKey("privateKey");
        userEntity.setAddress("address");
        userEntity.setLockTime(new Long(10000));

        userEntityRepository.save(userEntity);

        UserEntity userEntity1 = userEntityRepository.findByAccount("用户名");
        Assert.assertEquals(userEntity1.getAddress(), "address");
        System.out.println(userEntity1.toString());


        UserEntity userEntity2 = userEntityRepository.findByPhone("188188188");
        Assert.assertEquals(userEntity2.getAddress(), "address");
        System.out.println(userEntity2.toString());
    }

    @Test
    public void findByAccount() throws Exception {

    }

    @Test
    public void findByPhone() throws Exception {

    }

}