package com.hyperchain.dal.repository;

import com.hyperchain.dal.entity.EntityDemo;
import com.hyperchain.dal.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.Entity;

/**
 * Created by ldy on 2017/4/5.
 */
public interface UserEntityRepository extends
        PagingAndSortingRepository<UserEntity, Long>,
        JpaSpecificationExecutor<UserEntity> {

    UserEntity save(UserEntity userEntity);

    UserEntity findByAccount(String account);

    UserEntity findByPhone(String phone);
}
