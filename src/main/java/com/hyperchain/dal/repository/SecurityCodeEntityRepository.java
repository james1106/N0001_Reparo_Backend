package com.hyperchain.dal.repository;

import com.hyperchain.dal.entity.SecurityCodeEntity;
import com.hyperchain.dal.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by ldy on 2017/4/5.
 */
public interface SecurityCodeEntityRepository extends
        PagingAndSortingRepository<SecurityCodeEntity, Long>,
        JpaSpecificationExecutor<SecurityCodeEntity> {

    SecurityCodeEntity save(SecurityCodeEntity securityCodeEntity);

    SecurityCodeEntity findById(Long id);

}
