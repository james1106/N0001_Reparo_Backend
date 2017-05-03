package com.hyperchain.contract;

import com.hyperchain.ESDKUtil;
import com.hyperchain.common.constant.BaseConstant;
import com.hyperchain.dal.entity.UserEntity;
import com.hyperchain.dal.repository.UserEntityRepository;
import com.hyperchain.test.base.SpringBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.hyperchain.common.constant.BaseConstant.CONTRACT_NAME_ACCOUNT;

/**
 * Created by s on 2017/5/2.
 */
public class RepositoryContractTest extends SpringBaseTest {
    //买方公私钥
    String payerPublicKey = "b792b2defcc31f77949049f1e8132211ac9cba27";
    String payerPrivateKey = "{\"address\":\"b792b2defcc31f77949049f1e8132211ac9cba27\",\"encrypted\":\"31ee2c6ce22903675672d98c125122c3b47d7b40359149bed7a34b32b2723ffa\",\"version\":\"2.0\",\"algo\":\"0x03\"}";

    //卖方公私钥
    String payeePublicKey = "59a919333413f214131e1cb29a65b17ba42ffd86";
    String payeePrivateKey = "{\"address\":\"59a919333413f214131e1cb29a65b17ba42ffd86\",\"encrypted\":\"779bbf3e3779156f9b95928c1ac6ef9d1c3b2ffc72fc6f618e53cbee0fe1cc89\",\"version\":\"2.0\",\"algo\":\"0x03\"}";

    @Autowired
    private UserEntityRepository userEntityRepository;



    @Test
    public void incomeApplyTest() throws Exception {

        UserEntity payerUserEntity = userEntityRepository.findByAddress("0e1b81184266eaa1bbb19dabcefe78faeae11895"); //参数为 买家地址
        String payerPrivateKeyThis = payerUserEntity.getPrivateKey();
        String payerAccountName = payerUserEntity.getAccountName();
        ContractKey contractKey = new ContractKey(payerPrivateKeyThis, BaseConstant.SALT_FOR_PRIVATE_KEY + payerAccountName);

        //2.1
        String contractMethodName = "incomeApply";
        //2.2
        Object[] contractMethodParams = new Object[11];

        String orderContractAddress = ESDKUtil.getHyperchainInfo("OrderContract");//订单合约地址，用来更改仓储状态
        String repoBusinessNo = "";     //仓储业务编号
        String businessTransNo = "";    //业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 1
        String orderNo = "100"+"20170405205903117"+"200";//订单编号 21位
        String storerAddress = "";      //存货人(卖家)地址
        String repoEnterpriseAddress = "";//仓储公司地址
        long orderGenerateTime = System.currentTimeMillis();//操作时间(时间戳)
        String productName = "";        //仓储物名称
        long productQuantitiy = 1;      //仓储物数量
        long productUnitPrice = 1;      //货品单价(分)
        long productTotalPrice = 1;     //货品合计金额(分)

        contractMethodParams[0] = orderContractAddress;
        contractMethodParams[1] = repoBusinessNo;
        contractMethodParams[2] = businessTransNo;
        contractMethodParams[3] = orderNo;
        contractMethodParams[4] = storerAddress;
        contractMethodParams[5] = repoEnterpriseAddress;
        contractMethodParams[6] = orderGenerateTime;
        contractMethodParams[7] = productName;
        contractMethodParams[8] = productQuantitiy;
        contractMethodParams[9] = productUnitPrice;
        contractMethodParams[10] = productTotalPrice;
        //2.3若合约中的返回值有n个(初始的uint不计算在内)，则在String数组中填写n个名字，作为要查看的返回信息的key，对应合约中的各个返回值
        String[] contractReturnsMapKey = new String[]{"repoBusinessNo", "businessTransNo"};

        //3.调用
        ContractResult contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractMethodParams, contractReturnsMapKey, BaseConstant.CONTRACT_NAME_REPOSITORY);

        //4.此处是选择打印出来返回信息
        System.out.println("调用RepositoryContract合约中的incomeApply方法的返回code：" + contractResult.getCode());      //code为0表示成功
        System.out.println("调用RepositoryContract合约中的incomeApply方法的返回content：" + contractResult.toString());  //把整个返回内容展现出来
    }

    @Test
    public void incomeResponseTest() throws Exception {
        UserEntity repoUserEntity = userEntityRepository.findByAddress("0e1b81184266eaa1bbb19dabcefe78faeae11895"); //参数为 仓储地址，必须是仓储地址
        String repoPrivateKeyThis = repoUserEntity.getPrivateKey();
        String repoAccountName = repoUserEntity.getAccountName();
        ContractKey contractKey = new ContractKey(repoPrivateKeyThis, BaseConstant.SALT_FOR_PRIVATE_KEY + repoAccountName);

        //2.1
        String contractMethodName = "incomeResponse";
        //2.2
        Object[] contractMethodParams = new Object[6];

        String orderContractAddress = ESDKUtil.getHyperchainInfo("OrderContract");//订单合约地址，用来更改仓储状态
        String repoBusinessNo = "";     //仓储业务编号(待填写)
        String lastBusinessTransNo = repoBusinessNo+"0";    //上一个业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 0
        String currBusinessTransNo = repoBusinessNo+"1";    //当前业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 1
        long orderGenerateTime = System.currentTimeMillis();//操作时间(时间戳)
        String acctContractAddress = ESDKUtil.getHyperchainInfo(CONTRACT_NAME_ACCOUNT);

        contractMethodParams[0] = orderContractAddress;
        contractMethodParams[1] = repoBusinessNo;
        contractMethodParams[2] = lastBusinessTransNo;
        contractMethodParams[3] = currBusinessTransNo;
        contractMethodParams[4] = orderGenerateTime;
        contractMethodParams[5] = acctContractAddress;

        //2.3若合约中的返回值有n个(初始的uint不计算在内)，则在String数组中填写n个名字，作为要查看的返回信息的key，对应合约中的各个返回值
        String[] contractReturnsMapKey = new String[]{};

        //3.调用
        ContractResult contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractMethodParams, contractReturnsMapKey, BaseConstant.CONTRACT_NAME_REPOSITORY);

        //4.此处是选择打印出来返回信息
        System.out.println("调用RepositoryContract合约中的incomeResponse方法的返回code：" + contractResult.getCode());      //code为0表示成功
        System.out.println("调用RepositoryContract合约中的incomeResponse方法的返回content：" + contractResult.toString());  //把整个返回内容展现出来

    }

    @Test
    public void incomeConfirmTest() throws Exception {
        UserEntity repoUserEntity = userEntityRepository.findByAddress("0e1b81184266eaa1bbb19dabcefe78faeae11895"); //参数为 仓储地址，必须是仓储地址。
        String repoPrivateKeyThis = repoUserEntity.getPrivateKey();
        String repoAccountName = repoUserEntity.getAccountName();
        ContractKey contractKey = new ContractKey(repoPrivateKeyThis, BaseConstant.SALT_FOR_PRIVATE_KEY + repoAccountName);

        //2.1
        String contractMethodName = "incomeConfirm";
        //2.2
        Object[] contractMethodParams = new Object[7];

        String orderContractAddress = ESDKUtil.getHyperchainInfo("OrderContract");//订单合约地址，用来更改仓储状态
        String repoBusinessNo = "";     //仓储业务编号(待填写)
        String repoCertNo = "";         //仓单编号(待填写)
        String lastBusinessTransNo = repoBusinessNo+"0";    //上一个业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 0
        String currBusinessTransNo = repoBusinessNo+"1";    //当前业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 1
        long orderGenerateTime = System.currentTimeMillis();//操作时间(时间戳)
        String acctContractAddress = ESDKUtil.getHyperchainInfo(CONTRACT_NAME_ACCOUNT);

        contractMethodParams[0] = orderContractAddress;
        contractMethodParams[1] = repoBusinessNo;
        contractMethodParams[2] = repoCertNo;
        contractMethodParams[3] = lastBusinessTransNo;
        contractMethodParams[4] = currBusinessTransNo;
        contractMethodParams[5] = orderGenerateTime;
        contractMethodParams[6] = acctContractAddress;

        //2.3若合约中的返回值有n个(初始的uint不计算在内)，则在String数组中填写n个名字，作为要查看的返回信息的key，对应合约中的各个返回值
        String[] contractReturnsMapKey = new String[]{"repoCertNo"};

        //3.调用
        ContractResult contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractMethodParams, contractReturnsMapKey, BaseConstant.CONTRACT_NAME_REPOSITORY);

        //4.此处是选择打印出来返回信息
        System.out.println("调用RepositoryContract合约中的incomeConfirm方法的返回code：" + contractResult.getCode());      //code为0表示成功
        System.out.println("调用RepositoryContract合约中的incomeConfirm方法的返回content：" + contractResult.toString());  //把整个返回内容展现出来

    }

    @Test
    public void outcomeResponseTest() throws Exception {
        UserEntity repoUserEntity = userEntityRepository.findByAddress("0e1b81184266eaa1bbb19dabcefe78faeae11895"); //参数为 仓储地址，必须是仓储地址。(待填写)
        String repoPrivateKeyThis = repoUserEntity.getPrivateKey();
        String repoAccountName = repoUserEntity.getAccountName();
        ContractKey contractKey = new ContractKey(repoPrivateKeyThis, BaseConstant.SALT_FOR_PRIVATE_KEY + repoAccountName);

        //2.1
        String contractMethodName = "outcomeResponse";
        //2.2
        Object[] contractMethodParams = new Object[5];

        String orderContractAddress = ESDKUtil.getHyperchainInfo("OrderContract");//订单合约地址，用来更改仓储状态
        String repoBusinessNo = "";     //仓储业务编号(待填写)
        String repoCertNo = "";         //仓单编号(待填写)
        String lastBusinessTransNo = repoBusinessNo+"0";    //上一个业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 0
        String currBusinessTransNo = repoBusinessNo+"1";    //当前业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 1
        long orderGenerateTime = System.currentTimeMillis();//操作时间(时间戳)
        //String acctContractAddress = ESDKUtil.getHyperchainInfo(CONTRACT_NAME_ACCOUNT);

        contractMethodParams[0] = orderContractAddress;
        contractMethodParams[1] = repoCertNo;
        contractMethodParams[2] = lastBusinessTransNo;
        contractMethodParams[3] = currBusinessTransNo;
        contractMethodParams[4] = orderGenerateTime;

        //2.3若合约中的返回值有n个(初始的uint不计算在内)，则在String数组中填写n个名字，作为要查看的返回信息的key，对应合约中的各个返回值
        String[] contractReturnsMapKey = new String[]{"lastBusinessTransNo"};

        //3.调用
        ContractResult contractResult = ContractUtil.invokeContract(contractKey, contractMethodName, contractMethodParams, contractReturnsMapKey, BaseConstant.CONTRACT_NAME_REPOSITORY);

        //4.此处是选择打印出来返回信息
        System.out.println("调用RepositoryContract合约中的outcomeResponse方法的返回code：" + contractResult.getCode());      //code为0表示成功
        System.out.println("调用RepositoryContract合约中的outcomeResponse方法的返回content：" + contractResult.toString());  //把整个返回内容展现出来

    }
    @Test
    public void ThinkTest() throws Exception {
        System.out.println("hello world!");
    }


}
