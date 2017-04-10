pragma solidity ^0.4.0;
contract Reparo{
    address owner;
    function Reparo(){
        owner = msg.sender;
    }

    enum RoleCode {
    RC00,   //0, 融资企业
    RC01,   //1, 物流公司
    RC02,   //2, 仓储公司
    RC03    //3, 金融机构
    }
    enum AccountState {
    VALID,    //0, 有效
    INVALID,  //1, 无效
    FROZEN    //2, 冻结
    }

    enum WayBillState {
    UNCOMFIRMED,  //0, 待卖方确认
    COMFIRMED     //1, 卖方已确认
    }
    enum RepoCertState {
    UNCOMFIRMED,  //0, 待卖方确认
    COMFIRMED     //1, 卖方已确认
    }
    enum TransactionState {
    UNCOMFIRMED,  //0, 待卖方确认
    COMFIRMED     //1, 卖方已确认
    }
    enum ResponseType {
    YES,  //0, 同意
    NO,   //1, 拒绝
    NULL  //2, 无
    }
    enum ReceivableState{
    RB000001,   //1, 已结清
    RB000002,   //2, 已作废
    RB000003,   //3, 签收拒绝
    RB020001,   //4, 承兑待签收
    RB020006,   //5, 承兑已签收
    RB030001,   //6, 已兑付
    RB030006,   //7, 已部分兑付
    RB030009,   //8, 兑付失败
    B070001,    //9, 贴现待签收
    RB070006,   //10, 贴现已签收
    RB070008,   //11, 已部分贴现
    RB070009    //12, 已全额贴现
    }
    enum PayingMethod {
    RECEIVABLE,   //0, 应收账款方式
    CASH          //1, 现金方式
    }
//应收款
    struct Receivable {
    bytes32 receivableId;//应收款id，
    address payerPubkey; //付款人（签收人）
    address payeePubkey; //收款人（签发人）

    address OwnerPubkey;//本手持有人
    address nextOwnerPubkey;//下手持有人
    uint isseAmt; //票面金额
    uint cashedAmount;//已兑付金额
    ReceivableState status;//应收款状态
    ReceivableState lastStatus;//上一状态
    uint isseDat; //签发日
    uint signInDat;//签收日，根据时间筛选的参照对象
    uint expiryDate;//签收有效期
    uint dueDt; //到期日
    bytes note;//备注
    }

//订单相关状态
    struct OrderState {
    TransactionState txState;
    RepoCertState repoCertState;
    WayBillState wayBillState;
    ReceivableState receState;
    }

//帐户信息
    struct Account{
    address publicKey;//公钥
    bytes32 accountName;//用户名
    bytes32 companyName;//企业名称
    RoleCode roleCode;//角色
    AccountState accountState;
    }

//订单
    struct Order{
    bytes32 orderNo;//订单编号
    address payerAddress;//买方id
    address payeeAddress;//卖方（供应商id）
    bytes32 productName;//货品名称. -->test productName，unitPrice，productNum待确认是否需要使用数组，若需要的话addOrder中的参数如何传
    uint productUnitPrice;//货品单价
    uint productQuantity;//货品数量
    uint productTotalPrice;//订单总价
    bytes32 payerRepo;//买家仓储公司
    bytes32 repoBusinessNo;//仓储业务流水号
    bytes32 payeeRepo;//卖家仓储公司
    bytes32 repoCertNo;//卖家仓单编号
    uint orderGenerateTime;//订单生成时间
    bytes32 payerBank;//付款银行
    bytes32 payerBankClss;//开户行别
    bytes32 payerAccount;//付款人开户行
    PayingMethod payingMethod;//付款方式
    OrderState orderState;//订单状态
    }

//操作记录
    struct TransactionRecord {
    bytes32 orderNo;//订单编号
    bytes32 txSerialNo;//交易流水号
    TransactionState txState;
    uint time;
    }

// 用户公钥 => 结构体Account
    mapping( address => Account) accountMap;

// 订单id => 处理订单详情
    mapping( bytes32 => Order ) orderDetailMap;

// 账号 => 所有作为买方的订单列表（包含现在持有和历史订单）
    mapping(address => bytes32[]) allPayerOrderMap;

// 账号 => 所有作为卖方的订单列表（包含现在持有和历史订单）
    mapping(address => bytes32[]) allPayeeOrderMap;

// 应收款编号 => 应收款详情
    mapping(address => Receivable) receivableDetailMap;

// 用户公钥 => 所有应收款列表(包含现在持有和历史上曾经持有的)
    mapping( bytes32 => bytes32[]) allReceivableMap;

//用户持有应收款列表, 用户address =>用户当前持有的应收款编号列表
    mapping(bytes32 => bytes32[]) holdingReceivables;

//  用户公钥 => 待处理应收款列表
    mapping( bytes32 => bytes32[]) pendingReceivables;

//操作记录流水号 => 操作记录详情
    mapping(bytes32 => TransactionRecord) txRecordDetailMap;
//订单编号 => 操作记录流水号
//mapping(bytes32 => bytes32[]) txRecordDetailMap;

//用户已兑付列表,用户公钥 => 已兑付应收款编号列表
    mapping(address => bytes32[]) cashedReceivables;

// 根据特定需要，该数组用于返回某种订单数组
    bytes32[] tempOrderList;

    function isValidUser() internal returns (bool) {
    // 无效用户(包括未注册的用户)
        if (accountMap[msg.sender].accountState != AccountState.VALID) {
            return false;
        }
        else {
            return true;
        }
    }

//买方新建订单
    function createOrder (
    address payeeAddress,           //卖方地址
    uint productUnitPrice,              //货品单价
    uint productQuantity,                //货品数量
    uint productTotalPrice,                //货品总价
    bytes32[] bytes32Params,
    PayingMethod payingMethod,      //付款方式
    uint orderGenerateTime) returns(uint){  //生成订单时间
        if (!isValidUser()) {
            return 1;
        }
        bytes32 orderNo = bytes32Params[0];
        orderDetailMap[orderNo].orderNo = orderNo;
        orderDetailMap[orderNo].productName = bytes32Params[1];
        orderDetailMap[orderNo].payerRepo = bytes32Params[2];
        orderDetailMap[orderNo].repoBusinessNo = bytes32Params[3];
        orderDetailMap[orderNo].payerBank = bytes32Params[4];
        orderDetailMap[orderNo].payerBankClss = bytes32Params[5];
        orderDetailMap[orderNo].payerAccount = bytes32Params[6];

        orderDetailMap[orderNo].payerAddress = msg.sender; //买方公钥
        orderDetailMap[orderNo].payeeAddress = payeeAddress;//卖方公钥
        orderDetailMap[orderNo].productUnitPrice = productUnitPrice;
        orderDetailMap[orderNo].productQuantity = productQuantity;
        orderDetailMap[orderNo].productTotalPrice = productTotalPrice;

        orderDetailMap[orderNo].payingMethod = payingMethod;
        orderDetailMap[orderNo].orderGenerateTime = orderGenerateTime;
        orderDetailMap[orderNo].orderState.txState = TransactionState.UNCOMFIRMED;


        allPayerOrderMap[msg.sender].push(orderNo);//买方的所有订单
        allPayeeOrderMap[payeeAddress].push(orderNo);//卖方的所有订单
        txRecordDetailMap[bytes32Params[7]].orderNo = orderNo;
        txRecordDetailMap[bytes32Params[7]].txSerialNo = bytes32Params[7];
        txRecordDetailMap[bytes32Params[7]].time = orderGenerateTime;
        txRecordDetailMap[bytes32Params[7]].txState = TransactionState.UNCOMFIRMED;
        return (0);
    }

    function orderExists(bytes32 orderNo) returns(bool){
        Order order = orderDetailMap[orderNo];
        if(order.orderNo == 0 ) return(false);
        return(true);
    }
/*receNo
receGenerateTime
receivingSide
payingSide
receAmount
coupon
dueDate
receLatestStatus
receUpdateTime*/

//根据orderNo查询应收款概要信息
    function searchReceGeneInfo(bytes32 orderNo) returns(
    bytes32[] param1,
    uint[] param2){
        param1 = new bytes32[](4);
        param2 = new uint[](5);
        param1[0] = "1111";
        param1[1] = "11";
        param1[2] = "11";
        param1[3] = "1111";
        param2[0] = 1111;
        param2[1] = 11;
        param2[2] = 11;
        param2[3] = 1111;
        param2[4] = 11;
        return(param1, param2);
    }

//根据订单id获取订单详情。--test 待补充判断订单是否存在
    function queryOrderDetail(bytes32 orderNo) returns(uint, address resultPayerAccount, address resultPayeeAccount, bytes32[] resultBytes32, uint[] resultUint, PayingMethod resultMethod, TransactionState txState){
    //如果调用者账户无效，返回错误代码1（无效的用户）
        if (!isValidUser()) {
            return (1, resultPayerAccount, resultPayeeAccount, resultBytes32, resultUint, resultMethod, txState);
        }
        Order order = orderDetailMap[orderNo];
    //如果订单不存在，返回错误代码2000（订单不存在)
        if(!orderExists(orderNo)){
            return (2001, resultPayerAccount, resultPayeeAccount, resultBytes32, resultUint, resultMethod, txState);
        }
    //如果订单与合约调用者无关，返回错误代码2001（权限拒绝）
        if (order.payerAddress != msg.sender && order.payeeAddress != msg.sender) {
            return (2002, resultPayerAccount, resultPayeeAccount, resultBytes32, resultUint, resultMethod, txState);
        }
        bytes32[] memory receParam1 = new bytes32[](4);
        uint[] memory receParam2 = new uint[](5);

        (receParam1, receParam2) = searchReceGeneInfo(orderNo);
        resultUint = new uint[](9);
        resultBytes32 = new bytes32[](13);
        resultBytes32[0] = order.orderNo;
        resultBytes32[1] = order.productName;
        resultBytes32[2] = order.payerBank;
        resultBytes32[3] = order.payerBankClss;
        resultBytes32[4] = order.payerAccount;
        resultBytes32[5] = order.payerRepo;
        resultBytes32[6] = order.payeeRepo;
        resultBytes32[7] = order.repoCertNo;
        resultBytes32[8] = order.repoBusinessNo;

        resultBytes32[8] = receParam1[0];
        resultBytes32[8] = receParam1[1];
        resultBytes32[8] = receParam1[2];
        resultBytes32[8] = receParam1[3];

        resultUint[0] = order.productUnitPrice;
        resultUint[1] = order.productQuantity;
        resultUint[2] = order.productTotalPrice;
        resultUint[3] = order.orderGenerateTime;

        resultUint[4] = receParam2[0];
        resultUint[5] = receParam2[1];
        resultUint[6] = receParam2[2];
        resultUint[7] = receParam2[3];
        resultUint[8] = receParam2[4];

        return (0, order.payerAddress, order.payeeAddress, resultBytes32, resultUint, order.payingMethod, order.orderState.txState);
    }
    function queryAllOrderOverViewInfoList(uint role)returns(
    uint,
    bytes32[] partList1,//5值 orderNo, productName，payerRepo，payerBank，payerBankAccount
    address[] partList2, //2值 payerAddress, payeeAddress
    uint[] partList3,//4值productQuantity,productUnitPrice,productTotalPrice,orderGenerateTime,orderConfirmTime
    PayingMethod[] methodList
    ){
        bytes32[] memory orderList1;
        if(role == 0){
            orderList1 = allPayerOrderMap[msg.sender];
        }
        else if (role == 1){
            orderList1 = allPayeeOrderMap[msg.sender];
        }
        uint length = orderList1.length;
        partList1 = new bytes32[](length*5);
        partList2 = new address[](length*2);
        partList3 = new uint[](length*5);
        methodList = new PayingMethod[](length);

        for(uint k = 0; k < orderList1.length; k++){
            partList1[k*5] = orderDetailMap[orderList1[k]].orderNo;
            partList1[k*5+1] = orderDetailMap[orderList1[k]].productName;
            partList1[k*5+2] = orderDetailMap[orderList1[k]].payerRepo;
            partList1[k*5+3] = orderDetailMap[orderList1[k]].payerBank;
            partList1[k*5+4] = orderDetailMap[orderList1[k]].payerAccount;
            partList2[k*2] = orderDetailMap[orderList1[k]].payerAddress;
            partList2[k*2+1] = orderDetailMap[orderList1[k]].payeeAddress;
            partList3[k*5] = orderDetailMap[orderList1[k]].productQuantity;
            partList3[k*5+1] = orderDetailMap[orderList1[k]].productUnitPrice;
            partList3[k*5+2] = orderDetailMap[orderList1[k]].productTotalPrice;
            partList3[k*5+3] = orderDetailMap[orderList1[k]].orderGenerateTime;
            partList3[k*5+4] = 12345;
            methodList[k] = orderDetailMap[orderList1[k]].payingMethod;
        }
        return(0, partList1,
        partList2, partList3, methodList);
    }

//买方查询相关订单编号列表
    function queryAllOrderListForPayer() returns (uint, bytes32[] resultList){
        if (!isValidUser()) {
            return (1, resultList);
        }
        return (0, allPayerOrderMap[msg.sender]);
    }

//买方查询相关订单编号列表
    function queryAllOrderListForPayee() returns (uint, bytes32[] resultList){
        if (!isValidUser()) {
            return (1, resultList);
        }
        return (0, allPayeeOrderMap[msg.sender]);
    }
//卖方确认订单
    function confirmOrder(bytes32 orderNo, bytes32 payeeRepo, bytes32 payeeRepoCertNo, bytes32 txSerialNo, uint orderConfirmTime) returns(uint){
        if (!isValidUser()) {
            return (1);
        }
        Order order = orderDetailMap[orderNo];
        if (order.payeeAddress != msg.sender) {return (2002);}//仅订单的供应商可进行确认操作
        orderDetailMap[orderNo].orderState.txState = TransactionState.COMFIRMED;//修改订单详情map中的订单状态
        orderDetailMap[orderNo].payeeRepo = payeeRepo;
        orderDetailMap[orderNo].repoCertNo = payeeRepoCertNo;
        txRecordDetailMap[txSerialNo].orderNo = orderNo;
        txRecordDetailMap[txSerialNo].txSerialNo = txSerialNo;
        txRecordDetailMap[txSerialNo].time = orderConfirmTime;
        txRecordDetailMap[txSerialNo].txState = TransactionState.COMFIRMED;

        return (0);
    }

//从数组中删除某个元素
    function deleteArrayElement(bytes32[] storage a, bytes32 receivableNum) internal {
        uint position;
        for(uint i = 0; i < a.length; i++) {
            if(a[i] == receivableNum) {
                position = i;
                break;
            }
            position++;
        }
        if(position != a.length) {
            a[position] = a[a.length-1];
            a.length = a.length-1;
        }
    }
}
