package com.hyperchain.dal.repository;

import com.hyperchain.dal.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by ldy on 2017/4/5.
 */
public interface UserEntityRepository extends
        PagingAndSortingRepository<UserEntity, Long>,
        JpaSpecificationExecutor<UserEntity> {

    UserEntity save(UserEntity userEntity);

    UserEntity findByAccountName(String accountName);

    UserEntity findByAddress(String address);

    UserEntity findByPhone(String phone);

    UserEntity findByCompanyName(String companyName);

    List<UserEntity> findByRoleCode(int roleCode);
}
