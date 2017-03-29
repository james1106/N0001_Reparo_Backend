package com.hyperchain.common.listener;

import cn.hyperchain.common.log.LogUtil;
import com.hyperchain.ESDKConnection;
import com.hyperchain.ESDKUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;



/**
 * @Author Lizhong Kuang
 * @Date 16/11/18 上午11:07
 */
@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            if (event.getApplicationContext().getParent() == null) {
                ESDKConnection.compileContract();
                List<String> keys = ESDKUtil.newAccount("123");
                LogUtil.info("超级私钥："+keys.get(1));
                ESDKConnection.deployContract(keys.get(0),keys.get(1),"123");
                LogUtil.info("合约编译部署成功");
            }
        } catch (Exception e) {
            LogUtil.error("合约编译部署异常：" + e.getMessage());
        }
    }
}
