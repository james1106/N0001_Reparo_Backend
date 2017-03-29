package com.hyperchain.common.listener;

import cn.hyperchain.sdk.rpc.returns.CompileReturn;
import com.hyperchain.ESDKConnection;
import com.hyperchain.ESDKUtil;
import com.hyperchain.exception.ESDKException;
import org.junit.Test;

import java.util.List;

/**
 * Created by martin on 2017/3/20.
 */
public class StartupListenerTest {
    @Test
    public void build() throws Exception {
        System.out.println(this.getClass().getResource("hyperchain.properties").getPath());
    }


    @Test
    public void compile() throws ESDKException {
        //通过合约名（从配置文件hyperchian.propertries读取）,从配置文件读取合约源代码
        CompileReturn compileReturn = ESDKConnection.compileContract();
        String bin = compileReturn.getBin().get(0);
        String abi = compileReturn.getAbi().get(0);
    }


    @Test
    public void depoly() throws Exception {
        //获取服务
//        HyperchainAPI hyperchain = ContractUtil.getHyperchain();
        //创建账号
        String pwd = "123";
        List<String> list = ESDKUtil.newAccount(pwd);
        System.out.println("privateKey:" + list.get(1));

//        Transaction transaction = new Transaction(list.get(0), ESDKUtil.getContractBin(), false);
//        transaction.sign(list.get(1), pwd);
//        //deploy
//        String contractAddr = ContractInvoker.deployContract(transaction);

        String contractAddress = ESDKConnection.deployContract(list.get(0), list.get(1), "123");

        System.out.println("address:" + contractAddress);
//        FileUtil.writeContractAdress(contractAddress);
    }

////    @Test
//    public void getCode() throws Exception {
//        String privateKey = "{\"encrypted\":\"3756443e7128bd09dd0f3d647d39db5c634a317a6cdd2fbcd9837b5f35ef1e08c6f431ea0e0e22ac\",\"algo\":\"0x02\",\"address\":\"22aec712ba13be0fbba2238551456cd9d2df2cac\",\"version\":\"1.0\"}";
//        String password = "123";
//        LogUtil.info("000000000000");
//        String methodName = "getResult";
//
//        String publicKey = new DESKeyJSON(privateKey).getAddress();
//        Transaction transaction1 = ContractInvoker.getTxHash(publicKey, methodName, new Object[0]);
//        transaction1.sign(privateKey, password);
//        String contractRet = ContractInvoker.invokeContract(transaction1);
//        List<String> lists = com.hyperchain.ContractUtil.retDecode(methodName, contractRet);
//        System.out.println(lists);
//        LogUtil.info(lists);
//    }


}