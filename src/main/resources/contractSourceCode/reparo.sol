contract Repository{
    //仓单结构体
    struct RepoCert{
        bytes32 incomeCert  ;// 入库凭证
        bytes32 repoCertNo  ;// 仓单编码（待确认生成规则）
        bytes32 repoBusinessNo  ;// 仓储业务编号
        address repoEnterpriseAddress   ;// 保管人(仓储公司)
        address storerAddress   ;// 存货人(用户名)
        address holderAddress   ;// 持有人：最初为存货人，经过背书转让后，即为受让人(公司名称)
        bytes32 repoCreateDate  ;// 仓单填发日期
        bytes32 productName ;// 仓储物名称
        uint    productQuantitiy    ;// 仓储物数量
        bytes32 measureUnit ;// 仓储物计量单位
        bytes32 norms   ;// 仓储物规格(类似10cm*10cm)
        uint    productTotalPrice   ;// 货品合计金额(分)
        bytes32 productLocation ;// 仓储物场所（地址，前台填）
        RepoCertStatus  repoCertStatus  ;// 仓单状态（质押，解质押…）
    }
    //仓储结构体
    struct RepoBusiness{
        bytes32 repoBusinessNo  ;// 仓储业务编号
        bytes32 businessTransNo ;// 业务流转编号（仓储业务编号仓储状态）
        RepoBusiStatus  repoBusiStatus  ;// 仓储状态（0-入库待响应，1-入库，2-已入库，3-待出库，4-已出库）
        bytes32 orderNo ;// 订单号
        bytes   wayBillNo   ;// 运单号
        bytes32 repoCertNo  ;// 仓单编号
        address logisticsEnterpriseAddress  ;// 物流公司
        address repoEnterpriseAddress   ;// 保管人(仓储公司)
        address storerAddress   ;// 存货人
        address holderAddress   ;// 持有人：最初为存货人，经过背书转让后，即为受让人(公司名称)
        bytes32 incomeCert  ;// 入库凭证
        bytes32 productName ;// 仓储物名称
        uint    productQuantitiy    ;// 仓储物数量
        bytes32 measureUnit ;// 仓储物计量单位(如箱)
        bytes32 norms   ;// 仓储物规格(类似10cm*10cm)暂无
        uint    productUnitPrice;//货品单价(分)
        uint    productTotalPrice   ;// 货品合计金额(分)
        bytes32 productLocation ;// 仓储物场所（地址，前台填）
        bytes32 operateOperateTime  ;// 操作时间(时间戳)
    }

    //仓单状态待定
    enum RepoCertStatus{wait_for_define}
    //仓储状态
    enum RepoBusiStatus{WATING_INCOME_RESPONSE  ,// 0-入库待响应
                            WATING_INCOME           ,// 1-待入库
                            INCOMED                 ,// 2-已入库
                            WATING_OUTCOME          ,// 3-待出库
                            OUTCOMED                //  4-已出库
    }

    //用户address  —> 仓储业务编号列表
    mapping( address => bytes32[]) usrRepoBusinessMap;

    //用户address  —> 仓单编码列表
    mapping(address => bytes32[])  usrRepoCertListMap;

    //运单编号 —> 仓储业务编号
    mapping(bytes32 => bytes32) logisticsBusinessMap;

    //仓储业务编号 —> 业务流转编号列表
    mapping(bytes32=> bytes32[]) businessTransNoMap;

    //业务流转编号 —> 业务流转详情
    mapping(bytes32=> RepoBusiness) businessDetailMap;

    //仓单编号 -> 仓单详情
    mapping(bytes32=> RepoCert) repoCertDetailMap;

    //入库申请  1111,11110,0,"3434","4545",201010100101,"productName",100,100,10000
    function  incomeApply(bytes32 repoBusinessNo,       //  仓储业务编号
                          bytes32 businessTransNo,      //  业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 0
                          bytes32 orderNo,              //  仓储状态
                          address storerAddress,        //  存货人
                          address repoEnterpriseAddress,//  保管人(仓储公司)
                          bytes32 operateOperateTime,   //  操作时间(时间戳)
                          bytes32 productName,  //  仓储物名称
                          uint    productQuantitiy,     //  仓储物数量
                          uint    productUnitPrice,     //  货品单价(分)
                          uint    productTotalPrice     //  货品合计金额(分)
                          ) returns(uint,bytes32) {
        //waittodo待补充验证存货人，仓储公司是否有效，账户合约提供接口

        //加入存货人的列表
        usrRepoBusinessMap[storerAddress].push(repoBusinessNo);
        //加入仓储公司的列表
        usrRepoBusinessMap[repoEnterpriseAddress].push(repoBusinessNo);
        //加入业务流转编号列表
        businessTransNoMap[repoBusinessNo].push(businessTransNo);

        //仓储业务详情
        businessDetailMap[businessTransNo].repoBusinessNo = repoBusinessNo;
        businessDetailMap[businessTransNo].repoBusiStatus = RepoBusiStatus.WATING_INCOME_RESPONSE;
        businessDetailMap[businessTransNo].businessTransNo = businessTransNo;
        businessDetailMap[businessTransNo].orderNo = orderNo;
        businessDetailMap[businessTransNo].storerAddress = storerAddress;
        businessDetailMap[businessTransNo].holderAddress = storerAddress;
        businessDetailMap[businessTransNo].repoEnterpriseAddress = repoEnterpriseAddress;
        businessDetailMap[businessTransNo].operateOperateTime = operateOperateTime;
        businessDetailMap[businessTransNo].productName = productName;
        businessDetailMap[businessTransNo].productQuantitiy = productQuantitiy;
        businessDetailMap[businessTransNo].productUnitPrice = productUnitPrice;
        businessDetailMap[businessTransNo].productTotalPrice = productTotalPrice;

        return (0,repoBusinessNo);
    }

    //入库响应-同意入库
    function incomeResponse(bytes32 repoBusinessNo,       //  仓储业务编号
                            bytes32 lastBusinessTransNo,      //  上一个业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 0
                            bytes32 currBusinessTransNo,      //  当前业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 1
                            bytes32 operateOperateTime   //  操作时间(时间戳)
                            ) returns(uint){
       //waittodo 待补充，仅允许仓储机构进行入库响应，同时必须是该仓储机构下单仓储业务

       //businessDetailMap[businessTransNo].
       RepoBusiness repoBusinsess = businessDetailMap[lastBusinessTransNo];
       repoBusinsess.repoBusiStatus = RepoBusiStatus.WATING_INCOME;
       repoBusinsess.businessTransNo = currBusinessTransNo;
       repoBusinsess.operateOperateTime = operateOperateTime;
       businessDetailMap[currBusinessTransNo] = repoBusinsess;

        //将新的操作记录加入业务流转编号列表
        businessTransNoMap[repoBusinessNo].push(currBusinessTransNo);
       return (0);
    }

    //入库确认-已入库
    function incomeConfirm(bytes32 repoBusinessNo,       //  仓储业务编号
                            bytes32 lastBusinessTransNo,      //  上一个业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 1
                            bytes32 currBusinessTransNo,      //  当前业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 2
                            bytes32 operateOperateTime   //  操作时间(时间戳)
                            ) returns(uint){
       //waittodo 待补充，仅允许仓储机构进行入库响应，同时必须是该仓储机构下单仓储业务

       //businessDetailMap[businessTransNo].
       RepoBusiness repoBusinsess = businessDetailMap[lastBusinessTransNo];
       repoBusinsess.repoBusiStatus = RepoBusiStatus.INCOMED;
       repoBusinsess.businessTransNo = currBusinessTransNo;
       repoBusinsess.operateOperateTime = operateOperateTime;
       businessDetailMap[currBusinessTransNo] = repoBusinsess;

        //将新的操作记录加入业务流转编号列表
        businessTransNoMap[repoBusinessNo].push(currBusinessTransNo);
       return (0);
    }

    //查询仓储业务详情详情
    function getRepoBusinessDetail(bytes32 businessTransNo) returns(uint,
                                                                    RepoBusiStatus,/// 仓储状态
                                                                    address//持有人
                                                                    ,bytes32[] /*,
                                                                    uint[]      outUintList*/
                                                                    ) {
        //waittodo 校验
        RepoBusiness busDtl =  businessDetailMap[businessTransNo];
        bytes32[] memory  outBytesList = new bytes32[](3);
        /**/
        outBytesList[0] = busDtl.productName;
        outBytesList[1] = busDtl.measureUnit;
        outBytesList[2] = busDtl.operateOperateTime;

        /*outUintList[0] = busDtl.productQuantitiy;
        outUintList[1] = busDtl.productUnitPrice;
        outUintList[2] = busDtl.productTotalPrice;*/

        return (0,
                busDtl.repoBusiStatus,
                busDtl.holderAddress,
                /*outBytesList,*/
                outBytesList
                );

    }

     //查询仓储业务流转编号列表
    function getRepoBusinessTransList(bytes32 repoBusinessNo) returns(uint,bytes32[] repoBusinessTransNoList) {
        //waittodo
        repoBusinessTransNoList = businessTransNoMap[repoBusinessNo];
        return (0,repoBusinessTransNoList);
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

    //将需要返回的各bytes32参数转化为bytes32[]
    function getReceivableValue(bytes32 orderId) returns(bytes32[]){
        /*Order order = orderDetailMap[orderId];
        bytes32[] memory value = new bytes32[](5);
        value[0] = order.orderId;
        value[1] = order.productName;
        value[2] = order.payerBank;
        value[3] = order.payerBankClss;
        value[4] = order.payerBankAccount;
        return value;*/
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
}

contract order_reparo{
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
    enum RepoBusinessState {
    A,  //0, 待入库
    B   //1, 已入库
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
    RB070009,    //12, 已全额贴现
    RB0700010   //12, 已全额贴现
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
    RepoBusinessState repoBusiState;
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

//订单编号 => 交易流水号数组
    mapping(bytes32 => bytes32[]) txSerialNoList;

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
    function modifyOrderState(){
        if(accountMap[msg.sender].roleCode == RoleCode.RC01){

        }
        if(accountMap[msg.sender].roleCode == RoleCode.RC00){

        }
    }


/****************************买方新建订单**************************************/

    function createOrder (
    address payeeAddress,           //卖方地址
    uint productUnitPrice,          //货品单价
    uint productQuantity,           //货品数量
    uint productTotalPrice,         //货品总价
    bytes32[] bytes32Params,        //数组里有7个参数，orderNo，productName，payerRepo，repoBusinessNo
    //payerBank, payerBankClss, payerAccount
    PayingMethod payingMethod,      //付款方式
    uint orderGenerateTime) returns(uint){  //生成订单时间
    //如果用户不存在，返回"账户不存在，该用户可能未注册或已失效"
        if (!isValidUser()) {
            return 2;
        }
    //如果用户不是融资企业，返回"权限拒绝"
        if(accountMap[msg.sender].roleCode != RoleCode.RC00){
            return 1;
        }
        bytes32 orderNo = bytes32Params[0];
    //如果订单号已经存在，返回"该订单号已经存在"
        if(orderDetailMap[orderNo].orderNo != 0){
            return 2004;
        }
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
    //更新买方的所有订单和卖方所有的订单
        allPayerOrderMap[msg.sender].push(orderNo);
        allPayeeOrderMap[payeeAddress].push(orderNo);
    //提取交易流水号，添加操作记录
        bytes32 txSerialNo = bytes32Params[7];
        txRecordDetailMap[txSerialNo].orderNo = orderNo;
        txRecordDetailMap[txSerialNo].txSerialNo = txSerialNo;
        txRecordDetailMap[txSerialNo].time = orderGenerateTime;
        txRecordDetailMap[txSerialNo].txState = TransactionState.UNCOMFIRMED;
    //添加该订单对应的流水号
        txSerialNoList[orderNo].push(txSerialNo);
        return 0;
    }

    function orderExists(bytes32 orderNo) returns(bool){
        Order order = orderDetailMap[orderNo];
        if(order.orderNo == 0) return(false);
        return(true);
    }




//根据orderNo查询应收款概要信息，物流信息
    function searchReceGeneInfo(bytes32 orderNo) returns(
    bytes32[] param1,
    uint[] param2){

        param1 = new bytes32[](10);
        param2 = new uint[](11);
        param1[0] = "1111";
        param1[1] = "11";
        param1[2] = "11";
        param1[3] = "1111";

        param2[0] = 1111;
        param2[1] = 11;
        param2[2] = 11;
        param2[3] = 1111;
        param2[4] = 11;

        param1[4] = "22222";//运单号
        param1[5] = "222";  //物流公司

        param2[5] = 222; //下单时间
        param2[6] = 22;  //当前状态
        param2[7] = 22;  //更新时间

        param1[6] = "3333";//仓储流水号
        param1[7] = "33";  //买方仓储公司
        param1[8] = "3333333";//卖方仓储公司
        param1[9] = "33";  //仓单编号
        param2[8] = 33; //入库申请时间
        param2[9] = 33333;  //仓储状态
        param2[10] = 3;  //更新时间

        return(param1, param2);
    }

/*******************************根据订单编号获取订单详情***********************************/

    function queryOrderDetail(bytes32 orderNo) returns(uint, address resultPayerAccount, address resultPayeeAccount, bytes32[] resultBytes32, uint[] resultUint, PayingMethod resultMethod, TransactionState txState){
    //如果用户不存在，返回"账户不存在，该用户可能未注册或已失效"
        if (!isValidUser()) {
            return (2, resultPayerAccount, resultPayeeAccount, resultBytes32, resultUint, resultMethod, txState);
        }

    //如果用户不是融资企业，返回"权限拒绝"
        if(accountMap[msg.sender].roleCode != RoleCode.RC00){
            return (1, resultPayerAccount, resultPayeeAccount, resultBytes32, resultUint, resultMethod, txState);
        }
        Order order = orderDetailMap[orderNo];

    //如果订单不存在，返回"订单不存在"
        if(!orderExists(orderNo)){
            return (2001, resultPayerAccount, resultPayeeAccount, resultBytes32, resultUint, resultMethod, txState);
        }

    //如果订单与合约调用者无关，"权限拒绝"
        if (order.payerAddress != msg.sender && order.payeeAddress != msg.sender) {
            return (2005, resultPayerAccount, resultPayeeAccount, resultBytes32, resultUint, resultMethod, txState);
        }
    //取出应收账款概要信息
    //param1:receNo, receivingSide, payingSide, dueDate
    //param2:receGenerateTime, receAmount, coupon, receLatestStatus, receUpdateTime
        bytes32[] memory paramPart1 = new bytes32[](10);
        uint[] memory paramPart2 = new uint[](11);
        (paramPart1, paramPart2) = searchReceGeneInfo(orderNo);
        resultUint = new uint[](16);
        resultBytes32 = new bytes32[](19);
    //交易信息
        resultBytes32[0] = order.orderNo;
        resultBytes32[1] = order.productName;
        resultBytes32[2] = order.payerBank;
        resultBytes32[3] = order.payerBankClss;
        resultBytes32[4] = order.payerAccount;
        resultBytes32[5] = order.payerRepo;
        resultBytes32[6] = order.payeeRepo;
        resultBytes32[7] = order.repoCertNo;
        resultBytes32[8] = order.repoBusinessNo;
    //交易信息
        resultUint[0] = order.productUnitPrice;
        resultUint[1] = order.productQuantity;
        resultUint[2] = order.productTotalPrice;
        resultUint[3] = order.orderGenerateTime;
        if(txSerialNoList[orderNo].length == 2){
            resultUint[4] = txRecordDetailMap[txSerialNoList[orderNo][2]].time;
        }
        else{
            resultUint[4] = 5555;
        }
    //应收账款信息bytes32
        resultBytes32[9] = paramPart1[0];
        resultBytes32[10] = paramPart1[1];
        resultBytes32[11] = paramPart1[2];
        resultBytes32[12] = paramPart1[3];
    //应收账款信息uint
        resultUint[5] = paramPart2[0];
        resultUint[6] = paramPart2[1];
        resultUint[7] = paramPart2[2];
        resultUint[8] = paramPart2[3];
        resultUint[9] = paramPart2[4];


    //物流信息bytes32
        resultBytes32[13] = paramPart1[4];
        resultBytes32[14] = paramPart1[5];

    //物流信息uint
        resultUint[10] = paramPart2[5];
        resultUint[11] = paramPart2[6];
        resultUint[12] = paramPart2[7];

    //仓储信息bytes32
        resultBytes32[15] = paramPart1[6];
        resultBytes32[16] = paramPart1[7];
        resultBytes32[17] = paramPart1[8];
        resultBytes32[18] = paramPart1[9];

    //仓储信息uint
        resultUint[13] = paramPart2[8];
        resultUint[14] = paramPart2[9];
        resultUint[15] = paramPart2[10];
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

/****************************卖方确认订单**************************************/

    function confirmOrder(bytes32 orderNo, bytes32 payeeRepo, bytes32 payeeRepoCertNo, bytes32 txSerialNo, uint orderConfirmTime) returns(uint){
    //如果用户不存在，返回"账户不存在，该用户可能未注册或已失效"
    /*if (!isValidUser()) {
        return 2;
    }*/

    //如果用户不是融资企业，返回"权限拒绝"
    /*if(accountMap[msg.sender].roleCode != RoleCode.RC00){
        return 1;
    }
    Order order = orderDetailMap[orderNo];*/

    //如果订单不存在，返回"订单不存在"
    /*if(!orderExists(orderNo)){
        return 2001;
    }*/
    //若操作者不是订单的卖方，返回"用户不是订单的卖方"
    /*if (order.payeeAddress != msg.sender){
        return 2007;
    }*/
    //如果订单已经确认过，返回"该订单已经确认"
    /*if(order.orderState.txState == TransactionState.COMFIRMED){
        return 2006;
    }*/

    //更新订单的状态为"已确认"，添加卖方指定的仓储公司和仓单编号
        orderDetailMap[orderNo].orderState.txState = TransactionState.COMFIRMED;//修改订单详情map中的订单状态
        orderDetailMap[orderNo].payeeRepo = payeeRepo;
        orderDetailMap[orderNo].repoCertNo = payeeRepoCertNo;

    //确认订单后，检查仓储状态，如果为"待入库",则修改应收账款状态为"待签发"
        if(orderDetailMap[orderNo].orderState.repoBusiState == RepoBusinessState.B){
            orderDetailMap[orderNo].orderState.receState = ReceivableState.RB0700010;
        }

    //添加操作记录
        txRecordDetailMap[txSerialNo].orderNo = orderNo;
        txRecordDetailMap[txSerialNo].txSerialNo = txSerialNo;
        txRecordDetailMap[txSerialNo].time = orderConfirmTime;
        txRecordDetailMap[txSerialNo].txState = TransactionState.COMFIRMED;

    //添加该订单对应的流水号
        txSerialNoList[orderNo].push(txSerialNo);
        return 0;
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
