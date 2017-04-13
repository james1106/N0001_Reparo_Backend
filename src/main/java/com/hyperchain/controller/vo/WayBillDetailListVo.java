package com.hyperchain.controller.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldy on 2017/4/13.
 */
public class WayBillDetailListVo {
    private List<WayBillDetailVo> wayBillDetailVoList;

    public WayBillDetailListVo() {
    }

    public WayBillDetailListVo(List<WayBillDetailVo> wayBillDetailVoList) {
        this.wayBillDetailVoList = wayBillDetailVoList;
    }

    public List<WayBillDetailVo> getWayBillDetailVoList() {
        return wayBillDetailVoList;
    }

    public void setWayBillDetailVoList(List<WayBillDetailVo> wayBillDetailVoList) {
        this.wayBillDetailVoList = wayBillDetailVoList;
    }

    @Override
    public String toString() {
        return "WayBillDetailListVo{" +
                "wayBillDetailVoList=" + wayBillDetailVoList +
                '}';
    }
}
