package com.hyperchain.dal.repository;

import com.hyperchain.dal.entity.AccountEntity;
import com.hyperchain.dal.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by ldy on 2017/4/9.
 */
public interface AccountEntityRepository extends
        PagingAndSortingRepository<AccountEntity, Long>,
        JpaSpecificationExecutor<AccountEntity> {

    AccountEntity save(AccountEntity accountEntity);

    List<AccountEntity> findByAddress(String address);
}
