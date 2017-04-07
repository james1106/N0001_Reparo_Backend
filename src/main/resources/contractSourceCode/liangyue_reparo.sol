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
    enum OrderState {
    UNCOMFIRMED,  //0, 待卖方确认
    COMFIRMED     //1, 卖方已确认
    }
    enum ResponseType {
    YES,  //0, 同意
    NO,   //1, 拒绝
    NULL  //2, 无
    }
    enum ReceivableState{
    RB000001,   //已结清
    RB000002,   //已作废
    RB000003,   //签收拒绝
    RB020001,   //承兑待签收
    RB020006,   //承兑已签收
    RB030001,   //已兑付
    RB030006,   //已部分兑付
    RB030009,   //兑付失败
    B070001,    //贴现待签收
    RB070006,   //贴现已签收
    RB070008,   //已部分贴现
    RB070009    //已全额贴现
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

//帐户信息
    struct Account{
    address publicKey;//公钥
    bytes32 accountName;//用户名
    bytes32 companyName;//企业名称
    RoleCode roleCode;//角色
    AccountState accountState;//
    }

//订单
    struct Order{
    bytes32 orderId;//订单编号
    address payerAccount;//买方id
    address payeeAccount;//卖方（供应商id）
    bytes32 productName;//货品名称. -->test productName，unitPrice，productNum待确认是否需要使用数组，若需要的话addOrder中的参数如何传
    uint productPrice;//货品单价
    uint productNum;//货品数量
    uint totalPrice;//订单总价
    uint timeStamp;//订单生成时间
    bytes32 payerBank;//付款银行
    bytes32 payerBankClss;//开户行别
    bytes32 payerBankAccount;//付款人开户行
    PayingMethod payingMethod;//付款方式
    OrderState orderState;//订单状态
    }

//操作记录
    struct ReceivableRecord {
    bytes32 receivableId;//应收款编号
    bytes32 serialNum;//流水号
    address applicantPubkey;//申请人账号
    address replyerPubkey;//回复人账号
    ResponseType responseType;//回复意见
    uint time;//时间戳
    bytes32 operateType; //操作类型 enum申请、确认
    uint dealAmount;//操作金额
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
    mapping(bytes32 => ReceivableRecord) recordDetailMap;

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
    bytes32 orderId,                //订单编号
    address payeeAccount,           //卖方ID
    bytes32 productName,            //货品名称
    uint productPrice,              //货品单价
    uint productNum,                //货品数量
    uint totalPrice,                //货品总价
    bytes32 payerBank,              //付款银行
    bytes32 payerBankClss,          //开户行别
    bytes32 payerBankAccount,       //付款账户
    PayingMethod payingMethod,      //付款方式
    uint timeStamp) returns(uint){  //生成订单时间
        if (!isValidUser()) {
            return 1;
        }
        orderDetailMap[orderId].orderId = orderId;
        orderDetailMap[orderId].payerAccount = msg.sender; //买方公钥
        orderDetailMap[orderId].payeeAccount = payeeAccount;//卖方公钥
        orderDetailMap[orderId].productName = productName;
        orderDetailMap[orderId].productPrice = productPrice;
        orderDetailMap[orderId].productNum = productNum;
        orderDetailMap[orderId].totalPrice = totalPrice;
        orderDetailMap[orderId].payerBank = payerBank;
        orderDetailMap[orderId].payerBankClss = payerBankClss;
        orderDetailMap[orderId].payerBankAccount = payerBankAccount;
        orderDetailMap[orderId].payingMethod = payingMethod;
        orderDetailMap[orderId].timeStamp = timeStamp;
        orderDetailMap[orderId].orderState = OrderState.UNCOMFIRMED;

        allPayerOrderMap[msg.sender].push(orderId);//买方的所有订单
        allPayeeOrderMap[payeeAccount].push(orderId);//卖方的所有订单
        return (0);
    }

    function orderExists(bytes32 orderId) returns(bool){
        Order order = orderDetailMap[orderId];
        if(order.orderId == 0 ) return(false);
        return(true);
    }

//根据订单id获取订单详情。--test 待补充判断订单是否存在
    function queryOrderDetail(bytes32 orderId) returns(uint, address resultPayerAccount, address resultPayeeAccount, string resultStr, uint[] resultUint, PayingMethod resultMethod, OrderState resultState){
    //如果调用者账户无效，返回错误代码1（无效的用户）
        if (!isValidUser()) {
            return (1, resultPayerAccount, resultPayeeAccount, resultStr, resultUint, resultMethod, resultState);
        }
        Order order = orderDetailMap[orderId];
    //如果订单不存在，返回错误代码101（订单不存在)************************************
        if(!orderExists(orderId)){
            return (100, resultPayerAccount, resultPayeeAccount, resultStr, resultUint, resultMethod, resultState);
        }
    //如果订单与合约调用者无关，返回错误代码22（权限拒绝）
        if (order.payerAccount != msg.sender && order.payeeAccount != msg.sender) {
            return (22, resultPayerAccount, resultPayeeAccount, resultStr, resultUint, resultMethod, resultState);
        }
        bytes32[] memory value = new bytes32[](5);
        value = getReceivableValue(orderId);
        resultStr = SewingBytes32ArrayToString(value);
        resultUint = new uint[](4);
        resultUint[0] = order.productPrice;
        resultUint[1] = order.productNum;
        resultUint[2] = order.totalPrice;
        resultUint[3] = order.timeStamp;
        return (0, order.payerAccount, order.payeeAccount, resultStr, resultUint, order.payingMethod, order.orderState);
    }

//将需要返回的各bytes32参数转化为bytes32[]
    function getReceivableValue(bytes32 orderId) returns(bytes32[]){
        Order order = orderDetailMap[orderId];
        bytes32[] memory value = new bytes32[](5);
        value[0] = order.orderId;
        value[1] = order.productName;
        value[2] = order.payerBank;
        value[3] = order.payerBankClss;
        value[4] = order.payerBankAccount;
        return value;
    }
//将bytes32[]转化为string
    function SewingBytes32ArrayToString(bytes32[] value) internal returns(string){
        string  memory TheString ;
        string memory symbol1 = ",";

        uint j=0;
        for(uint i=0;i<value.length;i++){
            string memory temp1 = bytes32ToString(value[i]);
            TheString = sewingTwoString(TheString,temp1);
            if(i < value.length-1){
                TheString = sewingTwoString(TheString,symbol1);
            }
        }
        return TheString;
    }
//单个bytes32转化为string
    function bytes32ToString(bytes32 x) internal returns (string) {
        bytes memory bytesString = new bytes(32);
        uint charCount = 0;
        for (uint j = 0; j < 32; j++) {
            byte char = byte(bytes32(uint(x) * 2 ** (8 * j)));
            if (char != 0) {
                bytesString[charCount] = char;
                charCount++;
            }
        }
        bytes memory bytesStringTrimmed = new bytes(charCount);
        for (j = 0; j < charCount; j++) {
            bytesStringTrimmed[j] = bytesString[j];
        }
        return string(bytesStringTrimmed);
    }
//合并两个string为一个string
    function sewingTwoString(string a,string b) internal returns(string){
        bytes memory a_ = bytes(a);
        bytes memory b_ = bytes(b);
        bytes memory c = new bytes(a_.length+b_.length);
        uint j = 0;
        for(uint i=0;i< c.length;i++){
            if(i<a_.length){
                c[i] = a_[i];
            }
            else{
                c[i] = b_[j];
                j++;
            }
        }
        return string(c);
    }

//买方查询相关订单编号列表
    function queryAllOrderListForPayer(bytes32 orderId) returns (uint, bytes32[] resultList){
        if (!isValidUser()) {
            return (1, resultList);
        }
        return (0, allPayerOrderMap[msg.sender]);
    }

//买方查询相关订单编号列表
    function queryAllOrderListForPayee(bytes32 orderId) returns (uint, bytes32[] resultList){
        if (!isValidUser()) {
            return (1, resultList);
        }
        return (0, allPayeeOrderMap[msg.sender]);
    }
//卖方确认订单
    function confirmOrder(bytes32 orderId) returns(uint){
        if (!isValidUser()) {
            return (1);
        }
        Order order = orderDetailMap[orderId];
        if (order.payeeAccount != msg.sender) {return (22);}//仅订单的供应商可进行确认操作
        orderDetailMap[orderId].orderState = OrderState.COMFIRMED;//修改订单详情map中的订单状态
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
