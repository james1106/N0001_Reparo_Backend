package com.hyperchain.service.impl;

import com.hyperchain.common.util.TokenUtil;
import com.hyperchain.controller.vo.BaseResult;
import com.hyperchain.dal.entity.UserEntity;
import com.hyperchain.dal.repository.UserEntityRepository;
import com.hyperchain.service.WayBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ldy on 2017/4/12.
 */
@Service
public class WayBillServiceImpl implements WayBillService {

    @Autowired
    UserEntityRepository userEntityRepository;

    @Override
    public BaseResult<Object> generateUnConfirmedWaybill(String orderNo, String logisticsEnterpriseName, String senderEnterpriseName, String receiverEnterpriseName, int productName, String productQuantity, long productValue, String senderRepoEnterpriseName, String senderRepoCertNo, String receiverRepoEnterpriseName, String receiverRepoBusinessNo, HttpServletRequest request) {
        String address = TokenUtil.getAddressFromCookie(request);
        UserEntity userEntity = userEntityRepository.findByAddress(address);

        return null;
    }

    @Override
    public BaseResult<Object> generateConfirmedWaybill(String orderNo, HttpServletRequest request) {
        return null;
    }

    @Override
    public BaseResult<Object> getAllRelatedWayBillDetail(HttpServletRequest request) {
        return null;
    }

    @Override
    public BaseResult<Object> updateWayBillStatusToReceived(String orderNo, HttpServletRequest request) {
        return null;
    }
}
