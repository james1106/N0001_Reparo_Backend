package com.hyperchain.contract;

import com.hyperchain.common.constant.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangjiexie on 2017/3/15.
 */
public class ContractResult {

    private Code code;

    private List<Object> value = new ArrayList<>();

    private Map<String, Object> valueMap = new HashMap();

    public ContractResult(Code code) {
        this.code = code;
    }

    public ContractResult(Code code, List<Object> value, String[] resultMapKey) {
        this.code = code;
        this.value = value;
        if (value.size() > 1) {
            for (int i = 0; i < resultMapKey.length; ++i) {
                valueMap.put(resultMapKey[i], value.get(i));
            }
        }
    }

    public Map<String, Object> getValueMap() {
        return valueMap;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public List<Object> getValue() {
        return value;
    }

    public void setValue(List<Object> value) {
        this.value = value;
    }
}
