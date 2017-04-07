package com.hyperchain.dal.repository;

import com.hyperchain.dal.entity.PermissionEntity;
import com.hyperchain.dal.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by ldy on 2017/4/5.
 */
public interface PermissionEntityRepository extends
        PagingAndSortingRepository<PermissionEntity, Long>,
        JpaSpecificationExecutor<PermissionEntity> {

    PermissionEntity save(PermissionEntity permissionEntity);

    List<PermissionEntity> findByUrl(String url);

    List<PermissionEntity> findByRoleCode(int roleCode);

}
