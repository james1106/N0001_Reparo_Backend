package com.hyperchain.dal.repository;
import com.hyperchain.dal.entity.BankCard;
import com.hyperchain.dal.entity.Demo_UserEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
/**
 * Created by chenxiaoyang on 2017/3/30.
 */
public interface Demo_UserEntityRepository extends
        PagingAndSortingRepository<Demo_UserEntity, Long>,
        JpaSpecificationExecutor<BankCard> {

    Demo_UserEntity findByid(Long id);


}
