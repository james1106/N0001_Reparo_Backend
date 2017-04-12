package com.hyperchain.dal.entity;

/**
 * Created by liangyue on 2017/4/12.
 */
public class OperationRecord {
    int state;
    long operateTime;

    public OperationRecord(int state, long operateTime) {
        this.state = state;
        this.operateTime = operateTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(long operateTime) {
        this.operateTime = operateTime;
    }

}
