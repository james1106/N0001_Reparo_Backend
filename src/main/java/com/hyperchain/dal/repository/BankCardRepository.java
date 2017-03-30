package com.hyperchain.dal.repository;

import com.hyperchain.dal.entity.BankCard;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @Author Lizhong Kuang
 * @Modify Date 16/11/3 下午6:28
 */
public interface BankCardRepository extends
        PagingAndSortingRepository<BankCard, Long>,
        JpaSpecificationExecutor<BankCard> {
    BankCard findByTidAndStatusNot(Long id, Integer status);

    /**
     * 查询签收的银行卡号
     *
     * @param accountId 账户id
     * @param draftId   汇票id
     * @return
     */
    @Query(value = "select * from zb_bank_card where id=" +
            "(select to_bank_account_id from zb_draft_record where to_draft_id=?2" +
            " and (operate_type='2' or operate_type='3') and to_account_id=?1 order by operate_time desc limit 1)", nativeQuery = true)
    BankCard findSignedBankCard(Long accountId, Long draftId);

    /**
     * 查询用户
     * @param accountId
     * @param type
     * @return
     */
    @Query(value = "SELECT * FROM zb_bank_card where account_id =?1 and type =?2 and status = 3 order by modify_time desc limit 1", nativeQuery = true)
    BankCard findOnceBindedBankCard(Long accountId, String type);

    /**
     *
     * @param accountId
     * @param type
     * @return
     */
    @Query(value = "SELECT * FROM zb_bank_card where account_id =?1 and type =?2 order by modify_time desc limit 1", nativeQuery = true)
    BankCard findByAccountIdAndType(Long accountId, String type);

    /**
     * 查询银行卡，限定状态不为指定status
     * @param accountId
     * @param type
     * @param status
     * @return
     */
    BankCard findByAccountIdAndTypeAndStatusNot(Long accountId, String type, Integer status);

    List<BankCard> findByAccountIdAndBankCardNumber(Long accountId,String bankCardNumber);

    List<BankCard> findByBankCardNumber(String bankCardNumber);

    /**
     * 查询最近签收的银行卡
     *
     * @param accountId
     * @return
     */
    @Query(value="select * from zb_bank_card where account_id =?1 and type =?3 order by last_used_time desc limit 0,?2", nativeQuery = true)
    public List<BankCard> findRecentSignedBankCard(Long accountId, Integer num, String type);
}
