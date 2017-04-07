package com.hyperchain.dal.repository;

import com.hyperchain.dal.entity.PermissionEntity;
import com.hyperchain.test.base.SpringBaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by ldy on 2017/4/5.
 */
public class PermissionEntityRepositoryTest extends SpringBaseTest {

    @Autowired
    PermissionEntityRepository permissionEntityRepository;

    @Test
    public void save() throws Exception {
        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setUrl("http://1111");
        permissionEntity.setRoleCode(0);
        permissionEntityRepository.save(permissionEntity);

        List<PermissionEntity> permissionEntityList1 = permissionEntityRepository.findByRoleCode(0);
        Assert.assertEquals("http://1111", permissionEntityList1.get(0).getUrl());
        System.out.println(permissionEntityList1.get(0).toString());
        System.out.println("list大小： " + permissionEntityList1.size());

        List<PermissionEntity> permissionEntityList2 = permissionEntityRepository.findByUrl("http://1111");
        Assert.assertEquals(new Integer(0), permissionEntityList2.get(0).getRoleCode());
        System.out.println(permissionEntityList2.get(0).toString());
        System.out.println("list大小： " + permissionEntityList2.size());

    }

}