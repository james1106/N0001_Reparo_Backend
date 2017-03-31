package com.hyperchain.dal.repository;
import com.hyperchain.dal.entity.EntityDemo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by chenxiaoyang on 2017/3/30.
 */
public interface EntityDemoRepository extends
        PagingAndSortingRepository<EntityDemo, Long>,
        JpaSpecificationExecutor<EntityDemo> {

    EntityDemo findByid(Long id);


}
