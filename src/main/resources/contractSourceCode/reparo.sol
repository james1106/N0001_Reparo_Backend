/*
 * ==========运单状态=======
 * 0 UNDEFINED  未定义
 1 WAITING 待发货
 2 REQUESTING 发货待响应
 3 REJECTED   发货被拒绝
 4 SENDING    已发货
 5 RECEIVED   已送达
 ==========运单状态=======

 *=========仓储状态=======
 *  0 UNDEFINED                 未定义
 1 WATING_INCOME_RESPONSE    入库待响应
 2 WATING_INCOME             待入库
 3 INCOMED                   已入库
 --取消 4 WATING_OUTCOME_RESPONSE   出库待响应
 5 WATING_OUTCOME            待出库
 6 OUTCOMED                  已出库
 ==========仓储状态=======

 *=========仓单状态=======
 0 UNDEFINED  未定义
 1 TRANSABLE 可流转
 2 FREEZED冻结中
 3 INVALID已失效
 ==========仓单状态=======

 ==========订单交易状态=======
 0  未定义
 1  待确认
 2  已确认
 ==========订单交易状态=======

 ==========应收款状态=======
 0      未定义
 1      已结清 SETTLED
 2      已作废 CANCELLED
 3      签收拒绝 SIGNOUT_REFUSED
 10     待签发 WAITING_SIGNOUT
 21     承兑待签收 WAITING_ACCEPT
 26     承兑已签收 ACCEPTED
 31     已兑付 CASHED
 36     已部分兑付 PART_CASHED
 39     兑付失败 CASH_REFUSED
 41     贴现待签收 WAITING_DISCOUNT
 46     贴现已签收 DISCOUNTED
 48     已部分贴现 PART_DISCOUNTED
 49     已全额贴现 ALL_DISCOUNTED
 ==========应收款状态=======
 * */


contract AccountContract {

// enum RoleCode {COMPANY, LOGISTICS, REPOSITORY, FINANCIAL}
//RCOMPANY融资企业, LOGISTICS物流公司,REPOSITORY仓储公司,FINANCIAL金融机构
    uint ROLE_COMPANY = 0;
    uint ROLE_LOGISTICS = 1;
    uint ROLE_REPOSITORY = 2;
    uint ROLE_FINANCIAL = 3;

// enum AccountStatus {VALID, INVALID,FROZEN}
//账户状态，有效、无效、冻结
    uint CODE_SUCCESS = 0;
    uint CODE_PERMISSION_DENIED = 1;
    uint CODE_INVALID_USER = 2;
    uint CODE_ACCOUNT_ALREADY_EXIST = 5002;

//帐户信息
    struct Account {
        address accountAddress;//用户地址
        bytes32 accountName;//用户名
        bytes32 enterpriseName;//企业名称
        uint roleCode;//角色
        uint accountStatus;//账户状态
        bytes32 certType; //证件类型
        bytes32 certNo; //证件号码
        bytes32[] acctId; //账号 多个
        bytes32 svcrClass; //开户行别
        bytes32 acctSvcr; //开户行行号
        bytes32 acctSvcrName; //开户行名称
        bytes32 rate; //金融机构利率
    }


// 用户地址 => 结构体Account
    mapping (address => Account) accountMap;
// 账号acctId => 用户地址
    mapping (bytes32 => address) acctIdToAddress;


    function newAccount(bytes32 _accountName, bytes32 _enterpriseName, uint _roleCode, uint _accountStatus, bytes32 _certType, bytes32 _certNo, bytes32[] _acctId, bytes32 _svcrClass, bytes32 _acctSvcr, bytes32 _acctSvcrName, bytes32 rate) returns (uint code){
        if(accountMap[msg.sender].accountName != ""){
            return CODE_ACCOUNT_ALREADY_EXIST; //账户已存在
        }
        accountMap[msg.sender] = Account(msg.sender, _accountName, _enterpriseName, _roleCode, _accountStatus, _certType, _certNo, _acctId, _svcrClass, _acctSvcr, _acctSvcrName, rate);
        for (uint i = 0; i < _acctId.length; i++){
            acctIdToAddress[_acctId[i]] = msg.sender;
        }
        return CODE_SUCCESS; //成功
    }


    function getAccount() returns (uint code, bytes32 _accountName, bytes32 _enterpriseName, uint _roleCode, uint _accountStatus, bytes32 _certType, bytes32 _certNo, bytes32[] _acctId, bytes32 _class, bytes32 _acctSvcr, bytes32 _acctSvcrName){
        return getAccountByAddress(msg.sender);
    }

    function getAccountByAddress(address addr) returns (uint code, bytes32 _accountName, bytes32 _enterpriseName, uint _roleCode, uint _accountStatus, bytes32 _certType, bytes32 _certNo, bytes32[] _acctId, bytes32 _class, bytes32 _acctSvcr, bytes32 _acctSvcrName){
        if(accountMap[addr].accountName == ""){
            return (CODE_INVALID_USER, _accountName, _enterpriseName, _roleCode, _accountStatus, _certType, _certNo, _acctId, _class, _acctSvcr, _acctSvcrName); //账户不存在，该用户可能未注册或已失效
        }
        Account account = accountMap[addr];
        return (CODE_SUCCESS, account.accountName, account.enterpriseName, account.roleCode, account.accountStatus, account.certType, account.certNo, account.acctId, account.svcrClass, account.acctSvcr, account.acctSvcrName); //成功
    }

    function getRate() returns (uint code, bytes32 rate){
        return getRateByAddress(msg.sender);
    }

    function getRateByAddress(address addr) returns (uint code, bytes32 rate){
        if(accountMap[addr].accountName == ""){
            return (CODE_INVALID_USER,"0");
        }
        Account account = accountMap[addr];
        return (CODE_SUCCESS, account.rate);
    }

    function getRateByAcctId(bytes32 acctId) returns (bytes32 rate){
        address addr = acctIdToAddress[acctId];
        Account account = accountMap[addr];
        return (account.rate);
    }

    function setRate(bytes32 rate) returns (uint code){
        return setRateByAddress(msg.sender, rate);
    }

    function setRateByAddress(address addr, bytes32 rate) returns (uint code){
        if(accountMap[addr].accountName == ""){
            return (CODE_INVALID_USER);
        }
        Account account = accountMap[addr];
        account.rate = rate;
        return (CODE_SUCCESS);
    }

    function isAccountExist(address accountAddress) returns (bool){
        if(accountMap[accountAddress].accountName == ""){
            return false;
        }else{
            return true;
        }
    }

    function checkRoleCode(address accountAddress, uint targetRoleCode)returns (bool){
        if(accountMap[accountAddress].roleCode == targetRoleCode){
            return true;
        }else{
            return false;
        }
    }

    function queryRoleCode(address accountAddress)returns (uint){
        return accountMap[accountAddress].roleCode;
    }

    function getEnterpriseNameByAcctId(bytes32 acctId) returns (bytes32 enterpriseName){
        address addr = acctIdToAddress[acctId];
        return accountMap[addr].enterpriseName;
    }

    function getAddressByAcctId(bytes32 acctId) returns (address addr){
        return acctIdToAddress[acctId];
    }

    function getEnterpriseNameByAddress(address accountAddress) returns (bytes32 EnterpriseNameByAddress){
        return accountMap[accountAddress].enterpriseName;
    }
}


contract ReceivableContract{
//==============================test===add by liangyue=================================//
    function getReceInfo(bytes32 orderNo) returns(uint[5] resultUint, bytes32[4] resultBytes){
        resultUint[0] = 111111;
        resultUint[1] = 111111;
        resultUint[2] = 111111;
        resultUint[3] = 111111;
        resultUint[4] = 111111;
        resultBytes[0] = "hhhhh";
        resultBytes[1] = "hhhhh";
        resultBytes[2] = "hhhhh";
        resultBytes[3] = "hhhhh";
        return(resultUint, resultBytes);
    }


//给订单部分提供的接口,根据订单编号查询应收账款详情
    function getReceivableSimpleInfoByOrderNo(bytes32 orderNo) returns (uint, bytes32[4], uint[5]){
        bytes32 receivableNo = orderNoToReceivableNoMap[orderNo];
        Receivable receivable = receivableDetailMap[receivableNo];
        bytes32[] memory receivableSerials = receivableTransferHistoryMap[receivableNo];
        if(receivableSerials.length  > 0){
            ReceivableRecord receivaleRecord1 = receivableRecordMap[receivableSerials[0]];//第一笔流水号，对应签发申请
            ReceivableRecord receivaleRecord2 = receivableRecordMap[receivableSerials[receivableSerials.length - 1]];//最后一笔流水号，对应最新状态更新时间
            uint time1 = receivaleRecord1.time;
            uint time2 = receivaleRecord2.time;
            bytes32[4] memory bytesList;
            uint[5] memory uintList;
            bytesList[0] = receivableNo;
            bytesList[1] = receivable.pyee;
            bytesList[2] = receivable.pyer;
            bytesList[3] = receivable.rate;
            uintList[0] = time1;
            uintList[1] = receivable.isseAmt;
            uintList[2] = receivable.status;
            uintList[3] = time2;
            uintList[4] = receivable.dueDt;
        }
        return (0, bytesList, uintList);
    }
//==============================test====================================
//应收款
    struct Receivable {
    bytes32 receivableNo;//应收款编号
    bytes32 orderNo;//订单编号
    bytes32 signer; //签发人账号
    bytes32 accptr; //承兑人账号
    bytes32 pyer;//付款人账号
    bytes32 pyee;//收款人账号
    bytes32 firstOwner;//本手持有人账号
    bytes32 secondOwner;//下手持有人账号
    uint isseAmt; //票面金额
    uint cashedAmount;//已兑付金额
    uint status;//应收款状态
    uint lastStatus;//上一状态
    uint isseDt; //签发日
    uint signInDt;//签收日
    uint dueDt; //到期日
    bytes32 rate;//利率
    bytes32 contractNo;//合同号
    bytes32 invoiceNo;//发票号
    DiscountedStatus discounted;//是否贴现标志
    uint discountInHandAmount;//贴现回复时的到手金额
    uint discountApplyAmount;//贴现申请金额
    bytes32 discountedRate;//贴现利率
    bytes note;//备注，暂时不用
    }

//操作记录
    struct ReceivableRecord {
    bytes32 receivableNo;//应收款编号
    bytes32 serialNo;//流水号
    bytes32 applicantAcctId;//申请人账号
    bytes32 replyerAcctId;//回复人账号
    ResponseType responseType;//回复意见
    uint time;//时间戳
    bytes32 operateType; //操作类型
    uint dealAmount;//操作金额
    uint receivableStatus;//应收款状态
    }

//帐户信息
    struct Account {
    address accountAddress;//用户地址
    bytes32 accountName;//用户名
    bytes32 enterpriseName;//企业名称
    RoleCode roleCode;//角色
    AccountStatus accountStatus;//账户状态
    bytes32 certType; //证件类型
    bytes32 certNo; //证件号码
    bytes32[] acctId; //账号 多个
    bytes32 svcrClass; //开户行别
    bytes32 acctSvcr; //开户行行号
    bytes32 acctSvcrName; //开户行名称
    }

    enum RoleCode { RC00, RC01,RC02,RC03 } //RC00融资企业, RC01物流公司,RC02仓储公司,RC03金融机构
    enum AccountStatus { VALID, INVALID, FROZEN } //账户状态，有效、无效、冻结
    enum ResponseType { YES, NO, NULL } //YES-同意，NO-拒绝，NULL-无
    enum DiscountedStatus {NO, YES} //贴现标志位

//记录所有应收款编号数组
    bytes32[] allReceivableNos;

//账户信息
    mapping(address => Account) accountMap;

//存所有的应收款，应收款编号 => 应收款结构体
    mapping(bytes32 => Receivable) receivableDetailMap;

//账号acctId -> 公钥
    mapping(bytes32 => address) acctIdToAddressMap;

//付款人账号pyer -> 应收款编号
    mapping(bytes32 => bytes32[]) pyerToReceivableMap;

//收款人账号pyee -> 应收款编号
    mapping(bytes32 => bytes32[]) pyeeToReceivableMap;

//承兑人账号accptr -> 应收款编号
    mapping(bytes32 => bytes32[]) accptrToReceivableMap;

//签发人账号signer -> 应收款编号
    mapping(bytes32 => bytes32[]) signerToReceivableMap;

//金融机构acctID =》 应收款编号
    mapping(bytes32 => bytes32[]) bankToReceivableMap;

//订单编号 -> 应收款编号
    mapping(bytes32 => bytes32) orderNoToReceivableNoMap;

//用户交易记录, 操作人acctId => 交易记录流水号
    mapping(bytes32 => bytes32[]) accountReceivableRecords;

//所有交易记录,交易记录流水号 => ReceivableRecord
    mapping(bytes32 => ReceivableRecord) receivableRecordMap;

//用户持有应收款列表, 用户acctId=>应收款编号列表
    mapping(bytes32 => bytes32[]) holdingReceivablesMap;

//用户已兑付列表,用户账号=>已兑付应收款编号列表
    mapping(bytes32 => bytes32[]) cashedReceivablesMap;

//应收款的操作历史，应收款编号 => 交易记录流水号
    mapping(bytes32 => bytes32[]) receivableTransferHistoryMap;

//贴现申请时间，应收款编号 =》 贴现申请时间
    mapping(bytes32 => uint[]) discountApplyTime;

//判断账号是否存在
    function judgeAccount(address publicKey) internal returns (bool) {
        Account account = accountMap[publicKey];
        if(account.enterpriseName == 0x0) {
            return true;
        }
        if(account.accountStatus != AccountStatus.VALID) {
            return true;
        }
        return false;
    }

//判断流水号是否重复
    function judgeRepetitiveSerialNo(bytes32 serialNo) internal returns (bool){
        ReceivableRecord receivableRecord = receivableRecordMap[serialNo];
        if(receivableRecord.serialNo != 0x0){
            return true;
        }
        return false;
    }

//签发申请时判断所传应收款编号是否已存在
    function judgeRepetitiveReceivableNo(bytes32 receivableNo) internal returns (bool) {
        for(uint i = 0; i < allReceivableNos.length; i++){
            if(receivableNo == allReceivableNos[i]){
                return true;
            }
        }
        return false;
    }

//判断账号与公钥是否匹配
    function judgeAcctIdMatchAddress(bytes32 acctId, address newAddress) internal returns (bool){
        address oldAddress = acctIdToAddressMap[acctId];
        if(oldAddress != newAddress) {
            return true;
        }
        return false;
    }

    function judgeReplyerAddressEmpty(bytes32 acctId) internal returns (bool){
        address oldAddress = acctIdToAddressMap[acctId];
        if(oldAddress == 0x0){
            return true;
        }
        return false;
    }

//判断回复人账户是否有效
    function judgeReplyerAccount(bytes32 acctId) internal returns (bool){
        address replyerAddress = acctIdToAddressMap[acctId];
        Account replyerAccount = accountMap[replyerAddress];
        if(replyerAccount.accountStatus == AccountStatus.INVALID){
            return true;
        }
        if(replyerAccount.enterpriseName == 0x0){
            return true;
        }
        return false;
    }

//记录操作详情
    function newReceivableRecord(bytes32 serialNo, bytes32 receivableNo, bytes32 applicantAcctId, bytes32 replyerAcctId, ResponseType response, uint time, bytes32 operateType, uint dealAmount, uint receivableStatus) internal {
        receivableRecordMap[serialNo].serialNo = serialNo;
        receivableRecordMap[serialNo].receivableNo = receivableNo;
        receivableRecordMap[serialNo].applicantAcctId = applicantAcctId;
        receivableRecordMap[serialNo].replyerAcctId = replyerAcctId;
        receivableRecordMap[serialNo].responseType = response;
        receivableRecordMap[serialNo].time = time;
        receivableRecordMap[serialNo].operateType = operateType;
        receivableRecordMap[serialNo].dealAmount = dealAmount;
        receivableRecordMap[serialNo].receivableStatus = receivableStatus;
    }

    function updateOrderStateByReceivable(address orderAddress, bytes32 orderNo, bytes32 stateType, uint newState) returns (uint){
        OrderContract orderCon = OrderContract(orderAddress);
        return orderCon.updateOrderState(orderNo, stateType, newState, "", 0);
    }

    OrderContract orderCon;
    WayBillContract wayBillCon;

//签发申请。签发人是卖家（收款人），承兑人是买家（付款人）
    function signOutApply(bytes32 receivableNo, bytes32 orderNo, bytes32 signer, bytes32 accptr, bytes32 pyee, bytes32 pyer, uint isseAmt, uint dueDt, bytes32 rate, bytes32[] contractAndInvoiceNoAndSerialNo, uint time, address orderAddress) returns(uint code){
        if(receivableNo == "" || orderNo == "" || signer == "" || accptr == "" || pyer == "" || pyee == "" || rate == "" ){
            return (3);
        }
    /*        if(judgeRepetitiveSerialNo(serialNo)){
     return (1032);
     }
     if(isseAmt <= 0){
     return (1019);
     }
     if(judgeAccount(msg.sender)){
     return (2);
     }
     if(judgeAcctIdMatchAddress(signer, msg.sender)){
     return (1007);
     }
     if(judgeReplyerAddressEmpty(accptr)){
     return (1004);
     }
     if(judgeReplyerAccount(accptr)){
     return (1031);
     }
     */
        if(judgeRepetitiveReceivableNo(receivableNo)){//判断该应收款编号是否已经存在
            return (1030);
        }

        allReceivableNos.push(receivableNo);
        giveReceivableInfo(receivableNo, orderNo, signer, accptr, pyer, pyee, isseAmt, dueDt, rate, contractAndInvoiceNoAndSerialNo, time);
//        orderCon = OrderContract(orderAddress);
//        orderCon.updateOrderState(orderNo, "receState", 21);
//        if(orderCon.updateOrderState(orderNo, "receState", 21) == 3){
//            return (3);
//        }
        holdingReceivablesMap[signer].push(receivableNo);
        orderNoToReceivableNoMap[orderNo] = receivableNo;
        pyerToReceivableMap[pyer].push(receivableNo);
        pyeeToReceivableMap[pyee].push(receivableNo);

        updateOrderStateByReceivable(orderAddress, orderNo, "receState", 21);
        return (0);
    }

    function giveReceivableInfo(bytes32 receivableNo, bytes32 orderNo, bytes32 signer, bytes32 accptr, bytes32 pyer, bytes32 pyee, uint isseAmt, uint dueDt, bytes32 rate, bytes32[] contractAndInvoiceNo, uint time) internal {
        Receivable receivable = receivableDetailMap[receivableNo];
        receivable.receivableNo = receivableNo;
        receivable.orderNo = orderNo;
        receivable.signer = signer;
        receivable.accptr = accptr;
        receivable.firstOwner = signer;
        receivable.pyer = pyer;
        receivable.pyee = pyee;
        receivable.isseAmt = isseAmt;
        receivable.isseDt = time;
        receivable.dueDt = dueDt;
        receivable.rate = rate;
        receivable.status = 21;
        receivable.contractNo = contractAndInvoiceNo[0];
        receivable.invoiceNo = contractAndInvoiceNo[1];

        newReceivableRecord(contractAndInvoiceNo[2], receivableNo, signer, accptr, ResponseType.NULL, time, "signOutApply", isseAmt, receivable.status);
        accountReceivableRecords[signer].push(contractAndInvoiceNo[2]);
        receivableTransferHistoryMap[receivableNo].push(contractAndInvoiceNo[2]);
    }

//签发回复
    function signOutReply(bytes32 receivableNo, bytes32 replyerAcctId, ResponseType response, bytes32 serialNo, uint time, address orderAddress, address wayBillContractAddress) returns (uint){
    /*        if(receivableNo == "" || replyerAcctId == "" || serialNo == ""){
     return (3);
     }
     if(judgeRepetitiveSerialNo(serialNo)){
     return (1032);
     }
     if(response != ResponseType.NO && response != ResponseType.YES){
     return (1020);
     }
     if(judgeAccount(msg.sender)){
     return (2);
     }
     if(judgeAcctIdMatchAddress(replyerAcctId, msg.sender)){
     return (1007);
     }
     */
        Receivable receivable = receivableDetailMap[receivableNo];
        if(receivable.status != 21){
            return (1006);
        }
        if(replyerAcctId != receivable.accptr){
            return (1);
        }
        receivable.lastStatus = receivable.status;
        if(response == ResponseType.NO){
            receivable.status = 3;
        }else{
            receivable.status = 26;
            address[] memory resultAddress;
            bytes32[] memory resultBytes32;
            uint[] memory resultUint;

            (resultAddress, resultBytes32, resultUint) = callOrderContractByOrderNoForWayBill(receivable.orderNo, orderAddress);
            //begin  modify by ldy
            resultUint[2] = resultUint[1];
            resultUint[1] = resultUint[0];
            resultUint[0] = time;
            //end  modify by ldy

            WayBillContract wayBillCon = WayBillContract(wayBillContractAddress);
            wayBillCon.initWayBillStatus(resultAddress, resultBytes32, resultUint);
        }
        receivable.signInDt = time;

    //pyerToReceivableMap[receivable.pyer].push(receivableNo);
    //pyeeToReceivableMap[receivable.pyee].push(receivableNo);
        accptrToReceivableMap[receivable.accptr].push(receivableNo);
        signerToReceivableMap[receivable.signer].push(receivableNo);
        accountReceivableRecords[replyerAcctId].push(serialNo);
        newReceivableRecord(serialNo, receivableNo, receivable.signer, replyerAcctId, response, time, "signOutReply", receivable.isseAmt, receivable.status);
        receivableTransferHistoryMap[receivableNo].push(serialNo);
        updateOrderStateByReceivable(orderAddress, receivable.orderNo, "receState", receivable.status);
        return (0);
    }

    function callAccountContractGetAddressByAcctId(bytes32 acctId, address accountAddress) returns (address){
        AccountContract accountCon = AccountContract(accountAddress);
        return accountCon.getAddressByAcctId(acctId);
    }

    function callOrderContractByOrderNoForWayBill(bytes32 orderNo, address orderAddress) returns (address[] param1, bytes32[] param2, uint[] param3){
        OrderContract orderCon = OrderContract(orderAddress);
        address[4] memory resultAddress;
        bytes32[4] memory resultBytes32;
        uint[2] memory resultUint;
        (resultAddress, resultBytes32, resultUint) = orderCon.queryOrderInfoForRece(orderNo);
        param1 = new address[](5);
        param2 = new bytes32[](4);
        param3 = new uint[](3);  //modify by ldy
        param1[0] = resultAddress[0];
        param1[1] = resultAddress[1];
        param1[2] = resultAddress[2];
        param1[3] = resultAddress[3];
        param1[4] = orderAddress;
        param2[0] = resultBytes32[0];
        param2[1] = resultBytes32[1];
        param2[2] = resultBytes32[2];
        param2[3] = resultBytes32[3];
        param3[0] = resultUint[0];
        param3[1] = resultUint[1];
        param3[2] = 0; //modify by ldy
        return (param1, param2, param3);
    }

    //新贴现列表
    function getDiscountBankList(address accountAddress) returns (uint, bytes32[], bytes32[], bytes32[]){
        bytes32[] memory bankSvcr = new bytes32[](4);
        bytes32[] memory bankName = new bytes32[](4);
        bytes32[] memory bankDiscountRate = new bytes32[](4);
        bankSvcr[0] = "316";
        bankSvcr[1] = "102";
        bankSvcr[2] = "103";
        bankSvcr[3] = "104";

        bankName[0] = "浙商银行";
        bankName[1] = "中国工商银行";
        bankName[2] = "中国农业银行";
        bankName[3] = "中国银行";

        AccountContract accountCon = AccountContract(accountAddress);
        if(accountCon.getRateByAcctId(bankSvcr[0]) == "" || accountCon.getRateByAcctId(bankSvcr[1]) == "" || accountCon.getRateByAcctId(bankSvcr[2]) == "" || accountCon.getRateByAcctId(bankSvcr[3]) == "") {
            return (1041, bankSvcr, bankName, bankDiscountRate);
        }

        bankDiscountRate[0] = accountCon.getRateByAcctId(bankSvcr[0]);
        bankDiscountRate[1] = accountCon.getRateByAcctId(bankSvcr[1]);
        bankDiscountRate[2] = accountCon.getRateByAcctId(bankSvcr[2]);
        bankDiscountRate[3] = accountCon.getRateByAcctId(bankSvcr[3]);
//                bankDiscountRate[0] = "0.1";
//                bankDiscountRate[1] = "0.2";
//                bankDiscountRate[2] = "0.3";
//                bankDiscountRate[3] = "0.4";
        return (0, bankSvcr, bankName, bankDiscountRate);

    }

//贴现申请
    function discountApply(bytes32 receivableNo, bytes32 applicantAcctId, bytes32 replyerAcctId, bytes32 serialNo, uint time, uint discountApplyAmount, address orderAddress, bytes32 discountedRate) returns(uint) {
        if(receivableNo == "" || applicantAcctId == "" || replyerAcctId == "" || serialNo == ""){
            return (3);
        }
        if(judgeRepetitiveSerialNo(serialNo)){
            return (1032);
        }
    /*
     if(judgeAccount(msg.sender)){
     return(2);
     }
     if(judgeAcctIdMatchAddress(applicantAcctId, msg.sender)){
     return (1007);
     }
     if(judgeReplyerAddressEmpty(replyerAcctId)){
     return (1004);
     }
     if(judgeReplyerAccount(replyerAcctId)){
     return (1031);
     }
     */
        Receivable receivable = receivableDetailMap[receivableNo];
        if(receivable.status == 1){
            return (1038);
        }
        receivable.lastStatus = receivable.status;
        receivable.status = 41;//贴现待响应
        receivable.secondOwner = replyerAcctId;
        receivable.discountApplyAmount = discountApplyAmount;
        receivable.discountedRate = discountedRate;
        newReceivableRecord(serialNo, receivableNo, applicantAcctId, replyerAcctId, ResponseType.NULL, time, "discountApply", discountApplyAmount, receivable.status);
        accountReceivableRecords[applicantAcctId].push(serialNo);
        receivableTransferHistoryMap[receivableNo].push(serialNo);
        bankToReceivableMap[replyerAcctId].push(receivableNo);
        discountApplyTime[receivableNo].push(time);
        updateOrderStateByReceivable(orderAddress, receivable.orderNo, "receState", receivable.status);
        return(0);
    }

//贴现回复
    function discountReply(bytes32 receivableNo, bytes32 replyerAcctId, ResponseType responseType, bytes32 serialNo, uint time, uint discountInHandAmount, address orderAddress) returns(uint) {
        if(receivableNo == "" || replyerAcctId == "" || serialNo == ""){
            return (3);
        }
        if(responseType != ResponseType.NO && responseType != ResponseType.YES){
            return (1020);
        }
        if(judgeRepetitiveSerialNo(serialNo)){
            return (1032);
        }
//        if(judgeRepetitiveReceivableNo(newReceivableNo)){//判断新应收款编号是否已经存在
//            return (1030);
//        }
    /*
     if(judgeAccount(msg.sender)){
     return(2);
     }
     if(judgeAcctIdMatchAddress(replyerAcctId, msg.sender)){
     return (1007);
     }
     */
        Receivable receivable = receivableDetailMap[receivableNo];
        if(receivable.secondOwner != replyerAcctId){
            return (1);
        }
        //if((receivable.isseAmt - receivable.isseAmt * receivable.)//todo 校验到手金额

        receivable.discountInHandAmount = discountInHandAmount;
        receivable.firstOwner = replyerAcctId;
        receivable.pyee = replyerAcctId;
        receivable.lastStatus = receivable.status;
        receivable.status = 46;// 贴现已响应
        receivable.discounted = DiscountedStatus.YES;
        receivable.signInDt = time;
        //subDiscount(receivableNo, serialNo, responseType, time, newReceivableNo);
        //holdingReceivablesMap[replyerAcctId].push(newReceivableNo);
        accountReceivableRecords[replyerAcctId].push(serialNo);
        newReceivableRecord(serialNo, receivableNo, receivable.firstOwner, replyerAcctId, responseType, time, "discountResponse", discountInHandAmount, receivable.status);
        receivableTransferHistoryMap[receivableNo].push(serialNo);
        updateOrderStateByReceivable(orderAddress, receivable.orderNo, "receState", receivable.status);
        return (0);
    }
/*
    function subDiscount(bytes32 receivableNo, bytes32 serialNo, ResponseType responseType, uint time, bytes32 newReceivableNo) internal {
        Receivable receivable = receivableDetailMap[receivableNo];
        uint oriAmount = receivable.isseAmt;
        if(responseType == ResponseType.NO){
            receivable.status = receivable.lastStatus;
        }else{
            Receivable newReceivable = receivableDetailMap[newReceivableNo];
            copyValue(receivableNo, newReceivableNo);
            newReceivable.receivableNo = newReceivableNo;
            receivable.lastStatus = receivable.status;
            receivable.status = 49;//已全额贴现
            receivable.discounted = DiscountedStatus.YES;
            receivable.signInDt = time;
            newReceivable.lastStatus = 41;
            newReceivable.status = 46;//贴现已签收
            newReceivable.signInDt = time;
            newReceivable.firstOwner = receivable.secondOwner;
            newReceivable.secondOwner = "";
            newReceivable.discounted = DiscountedStatus.YES;
            newReceivable.signInDt = time;
        }
    }
*/
/*
 function subDiscount(bytes32 receivableNo, bytes32 serialNo, ResponseType responseType, uint time, bytes32 newReceivableNo, bytes32 discountApplySerialNo) internal {
 Receivable receivable = receivableDetailMap[receivableNo];
 ReceivableRecord receivableRecord = receivableRecordMap[discountApplySerialNo];
 uint discountApplyAmount = receivableRecord.dealAmount;
 uint oriAmount = receivable.isseAmt;
 if(responseType == ResponseType.NO){
 receivable.status = receivable.lastStatus;
 }else{
 Receivable newReceivable = receivableDetailMap[newReceivableNo];
 if(judgeOperateOption(receivableNo, discountApplyAmount)){
 copyValue(receivableNo, newReceivableNo);
 newReceivable.receivableNo = newReceivableNo;
 receivable.lastStatus = receivable.status;
 receivable.status = 49;//已全额贴现
 receivable.discounted = DiscountedStatus.YES;
 receivable.signInDt = time;
 newReceivable.lastStatus = 41;
 newReceivable.status = 46;//贴现已签收
 newReceivable.signInDt = time;
 newReceivable.firstOwner = receivable.secondOwner;
 newReceivable.secondOwner = "";
 newReceivable.discounted = DiscountedStatus.YES;
 newReceivable.signInDt = time;
 newReceivable.isseAmt = discountApplyAmount;
 }else{
 copyValue(receivableNo, newReceivableNo);
 newReceivable.receivableNo = newReceivableNo;
 newReceivable.lastStatus = newReceivable.status;
 newReceivable.status = 46;//贴现已签收
 newReceivable.isseAmt = discountApplyAmount;
 newReceivable.firstOwner = receivable.secondOwner;
 newReceivable.secondOwner = "";
 newReceivable.discounted = DiscountedStatus.YES;
 newReceivable.signInDt = time;

 receivable.lastStatus = receivable.status;
 receivable.status = 48;//已部分贴现
 receivable.isseAmt = oriAmount - discountApplyAmount;
 receivable.signInDt = time;
 receivable.firstOwner = receivable.firstOwner;
 //receivable.secondOwner = "";
 }

 }
 }
 */
//判断全额 或 部分
    function judgeOperateOption(bytes32 receivableNum, uint dealAmount) internal returns (bool){
        Receivable receivable = receivableDetailMap[receivableNum];
        if(dealAmount < receivable.isseAmt){
            return false;  //部分
        }
        return true;   //全额
    }

    function copyValue(bytes32 originReceivableNum, bytes32 subReceivableNum) internal {
        Receivable oriReceivable = receivableDetailMap[originReceivableNum];
        Receivable subReceivable = receivableDetailMap[subReceivableNum];
        subReceivable.orderNo = oriReceivable.orderNo;
        subReceivable.signer = oriReceivable.signer;
        subReceivable.accptr = oriReceivable.accptr;
        subReceivable.pyer = oriReceivable.pyer;
        subReceivable.pyee = oriReceivable.pyee;
        subReceivable.firstOwner = oriReceivable.firstOwner;
        subReceivable.secondOwner = oriReceivable.secondOwner;
        subReceivable.isseAmt = oriReceivable.isseAmt;
        subReceivable.cashedAmount = oriReceivable.cashedAmount;
        subReceivable.status = oriReceivable.status;
        subReceivable.lastStatus = oriReceivable.lastStatus;
        subReceivable.isseDt = oriReceivable.isseDt;
        subReceivable.signInDt = oriReceivable.signInDt;
        subReceivable.dueDt = oriReceivable.dueDt;
        subReceivable.rate = oriReceivable.rate;
        subReceivable.contractNo = oriReceivable.contractNo;
        subReceivable.invoiceNo = oriReceivable.invoiceNo;
        subReceivable.note = oriReceivable.note;
    }

//从数组中删除某个元素
    function deleteArrayElement(bytes32[] storage a, bytes32 receivableNo) internal {
        uint position;
        for(uint i = 0; i < a.length; i++) {
            if(a[i] == receivableNo) {
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

//兑付
    function cash(bytes32 receivableNo, uint cashedAmount, uint time, bytes32 serialNo, ResponseType responseType, address orderAddress)returns(uint){
        if(receivableNo == "" || serialNo == ""){
            return (3);
        }
        if(judgeRepetitiveSerialNo(serialNo)){
            return (1032);
        }
        if(cashedAmount <= 0){
            return (1016);
        }
        Receivable receivable = receivableDetailMap[receivableNo];
        if(receivable.receivableNo == 0x0) {
            return(1005);
        }

     if(receivable.status != 26){
        return(1006);
     }
     /*
     //到期日才能兑付
     if(time < receivable.dueDt){
     return(1010);
     }
     */

        receivable.lastStatus = receivable.status;
        receivable.cashedAmount = cashedAmount;
        receivable.status = 1;
        cashedReceivablesMap[receivable.accptr].push(receivableNo);
        receivableTransferHistoryMap[receivableNo].push(serialNo);
        newReceivableRecord(serialNo, receivableNo, receivable.signer, receivable.accptr, ResponseType.YES, time, "Cash", cashedAmount, receivable.status);
        updateOrderStateByReceivable(orderAddress, receivable.orderNo, "receState", receivable.status);
        return (0);
    }

    //带有应收款流水信息的应收款更具体详情
    function getReceivableAllInfoWithSerial(bytes32[] receivableNoAndAcctId, address[] orderAndWayBillAndAccountAddress) returns (uint, bytes32[], uint[], DiscountedStatus discounted){
        Account account = accountMap[msg.sender];
        Receivable receivable = receivableDetailMap[receivableNoAndAcctId[0]];
        bytes32[] memory historySerialNos = receivableTransferHistoryMap[receivableNoAndAcctId[0]];
        orderCon = OrderContract(orderAndWayBillAndAccountAddress[0]);
        wayBillCon = WayBillContract(orderAndWayBillAndAccountAddress[1]);

        uint[] memory uintInfo = new uint[](historySerialNos.length * 2 + 8);
        bytes32[] memory bytesInfo1 = new bytes32[](18);
        /*
         if(judgeAccount(msg.sender)){
         return (2,
         bytesInfo1,
         //bytesInfo2,
         uintInfo,
         uintSerials,
         discounted
         );
         }
         */
        if(receivableNoAndAcctId[0] == ""){
            return (3,
            bytesInfo1,
            uintInfo,
            discounted
            );
        }

        if(receivable.receivableNo == 0x0) {
            return(1005,
            bytesInfo1,
            uintInfo,
            discounted
            );
        }


        if(receivable.signer != receivableNoAndAcctId[1] && receivable.accptr != receivableNoAndAcctId[1] && receivable.pyer != receivableNoAndAcctId[1] && receivable.pyee != receivableNoAndAcctId[1] && receivable.firstOwner != receivableNoAndAcctId[1] && receivable.secondOwner != receivableNoAndAcctId[1]) {
            return(1,
            bytesInfo1,
            uintInfo,
            discounted
            );
        }


        ReceivableRecord memory receivableRecord;
        for(uint i = 0; i < historySerialNos.length; i++){
            receivableRecord = receivableRecordMap[historySerialNos[i]];
            uintInfo[i*2] = receivableRecord.receivableStatus;
            uintInfo[i*2+1] = receivableRecord.time;
        }
        uintInfo[historySerialNos.length*2] = receivable.isseAmt;
        uintInfo[historySerialNos.length*2 + 1] = receivable.cashedAmount;
        uintInfo[historySerialNos.length*2 + 2] = receivable.isseDt;
        uintInfo[historySerialNos.length*2 + 3] = receivable.signInDt;
        uintInfo[historySerialNos.length*2 + 4] = receivable.dueDt;
        uintInfo[historySerialNos.length*2 + 5] = receivable.discountInHandAmount;
        uintInfo[historySerialNos.length*2 + 6] = receivable.status;
        uintInfo[historySerialNos.length*2 + 7] = receivable.lastStatus;

        bytesInfo1[0] = receivableNoAndAcctId[0];
        bytesInfo1[1] = receivable.orderNo;
        bytesInfo1[2] = receivable.signer;
        bytesInfo1[3] = receivable.accptr;
        bytesInfo1[4] = receivable.pyer;
        bytesInfo1[5] = receivable.pyee;
        bytesInfo1[6] = receivable.firstOwner;
        bytesInfo1[7] = receivable.secondOwner;
        bytesInfo1[8] = receivable.rate;
        bytesInfo1[9] = receivable.contractNo;
        bytesInfo1[10] = receivable.invoiceNo;
        bytesInfo1[11] = orderCon.queryPayerRepoCertNo(receivable.orderNo);
        bytesInfo1[12] = orderCon.queryPayeeRepoCertNo(receivable.orderNo);
        bytesInfo1[13] = orderCon.queryPayerRepoEnterpriseName(receivable.orderNo, orderAndWayBillAndAccountAddress[2]);
        bytesInfo1[14] = orderCon.queryPayeeRepoEnterpriseName(receivable.orderNo, orderAndWayBillAndAccountAddress[2]);
        (bytesInfo1[15], bytesInfo1[16]) = wayBillCon.getWaybillMsgForReceviable(receivable.orderNo, orderAndWayBillAndAccountAddress[2]);
        bytesInfo1[17] = receivable.discountedRate;

        return (0,
        bytesInfo1,
        uintInfo,
        receivable.discounted
        );
    }

//买家、卖家应收款列表
    function getReceivableAllList(bytes32 receivableNo, bytes32 acctId) returns (uint, bytes32[], uint[], DiscountedStatus discounted, bytes note){
        Account account = accountMap[msg.sender];
        Receivable receivable = receivableDetailMap[receivableNo];

        uint[] memory uintInfo = new uint[](8);
        bytes32[] memory bytesInfo1 = new bytes32[](11);
    //bytes32[] memory bytesInfo2 = new bytes32[](4);
    /*
     if(judgeAccount(msg.sender)){
     return (2,
     bytesInfo1,
     //bytesInfo2,
     uintInfo,
     discounted,
     note
     );
     }
     */
        if(receivableNo == ""){
            return (3,
            bytesInfo1,
            //bytesInfo2,
            uintInfo,
            discounted,
            note
            );
        }

        if(receivable.receivableNo == 0x0) {
            return(1005,
            bytesInfo1,
            //bytesInfo2,
            uintInfo,
            discounted,
            note
            );
        }

    /*
     if(receivable.signer != acctId && receivable.accptr != acctId && receivable.pyer != acctId && receivable.pyee != acctId) {
     return(1,
     bytesInfo1,
     //bytesInfo2,
     uintInfo,
     discounted,
     note
     );
     }
     */
        uintInfo[0] = receivable.isseAmt;
        uintInfo[1] = receivable.cashedAmount;
        uintInfo[2] = receivable.isseDt;
        uintInfo[3] = receivable.signInDt;
        uintInfo[4] = receivable.dueDt;
        uintInfo[5] = receivable.discountInHandAmount;
        uintInfo[6] = receivable.status;
        uintInfo[7] = receivable.lastStatus;

        bytesInfo1[0] = receivableNo;
        bytesInfo1[1] = receivable.orderNo;
        bytesInfo1[2] = receivable.signer;
        bytesInfo1[3] = receivable.accptr;
        bytesInfo1[4] = receivable.pyer;
        bytesInfo1[5] = receivable.pyee;
        bytesInfo1[6] = receivable.firstOwner;
        bytesInfo1[7] = receivable.secondOwner;
        bytesInfo1[8] = receivable.rate;
        bytesInfo1[9] = receivable.contractNo;
        bytesInfo1[10] = receivable.invoiceNo;



        return (0,
        bytesInfo1,
        //acctSvcrNameAndEnterpriseName(receivableNo),
        uintInfo,
        discounted,
        note
        );
    }

//获取买卖方、贴现金融机构列表
    function receivableSimpleDetailList(uint roleCode, bytes32 acctId, address orderAddress, address accountAddress) returns (uint, bytes32[], uint[]) {
        bytes32[] memory receivableNos;
        if(roleCode == 0){//买家(付款人)
            receivableNos = pyerToReceivableMap[acctId];
        }else if(roleCode == 1){//卖家
            receivableNos = pyeeToReceivableMap[acctId];
        }else if(roleCode == 3){//贴现金融机构
            receivableNos = bankToReceivableMap[acctId];
        }

        uint receivableNosLength = receivableNos.length;
        bytes32[] memory list1 = new bytes32[](receivableNosLength * 5);//receivableNo，productName,收款人企业名enterpriseName,本手持有人acctId，承兑人acctId
        uint[] memory list2 = new uint[](receivableNosLength * 5);//productQuantity,isseAmt, dueDt,status,贴现申请时间


        for(uint i = 0; i < receivableNosLength; i++){
            list1[i*3] = receivableNos[i];
            list1[i*3+1] = callOrderContractGetProductName(orderAddress, receivableNos[i]);
            if(roleCode == 0){
                list1[i*3+2] = callAccountContractGetPyeeEnterpriseName(accountAddress, receivableNos[i]);
            }else if(roleCode ==1){
                list1[i*3+2] = callAccountContractGetPyerEnterpriseName(accountAddress, receivableNos[i]);
            }
            list1[i*3+3] = receivableDetailMap[receivableNos[i]].firstOwner;
            list1[i*3+4] = receivableDetailMap[receivableNos[i]].accptr;

            list2[i*4] = callOrderContractGetProductQuantity(orderAddress, receivableNos[i]);
            list2[i*4+1] = receivableDetailMap[receivableNos[i]].isseAmt;
            list2[i*4+2] = receivableDetailMap[receivableNos[i]].dueDt;
            list2[i*4+3] = receivableDetailMap[receivableNos[i]].status;
            if(discountApplyTime[receivableNos[i]].length == 0){
                list2[i*4+4] = 0;
            }else{
                list2[i*4+4] = discountApplyTime[receivableNos[i]][discountApplyTime[receivableNos[i]].length - 1];
            }

        }

        return (0, list1, list2);

    }

    function callOrderContractGetProductName(address orderAddress, bytes32 receivableNo) returns (bytes32 productName){
        Receivable receivable = receivableDetailMap[receivableNo];
        bytes32 orderNo = receivable.orderNo;
        OrderContract orderCon = OrderContract(orderAddress);
        return orderCon.queryProductNameByOrderNo(orderNo);
    }

    function callOrderContractGetProductQuantity(address orderAddress, bytes32 receivableNo) returns (uint productQuantity){
        Receivable receivable = receivableDetailMap[receivableNo];
        bytes32 orderNo = receivable.orderNo;
        OrderContract orderCon = OrderContract(orderAddress);
        return orderCon.queryProductQuantityByOrderNo(orderNo);
    }
    function callAccountContractGetPyerEnterpriseName(address accountAddress, bytes32 receivableNo) returns (bytes32){
        AccountContract accountCon = AccountContract(accountAddress);
        Receivable receivable = receivableDetailMap[receivableNo];
        bytes32 acctId = receivable.pyer;
        return accountCon.getEnterpriseNameByAcctId(acctId);
    }
    function callAccountContractGetPyeeEnterpriseName(address accountAddress, bytes32 receivableNo) returns (bytes32){
        AccountContract accountCon = AccountContract(accountAddress);
        Receivable receivable = receivableDetailMap[receivableNo];
        bytes32 acctId = receivable.pyee;
        return accountCon.getEnterpriseNameByAcctId(acctId);
    }

/*
 function acctSvcrNameAndEnterpriseName(bytes32 receivableNo) returns (bytes32[]){
 Receivable receivable = receivableDetailMap[receivableNo];
 address pyerAddress = acctIdToAddressMap[receivable.pyer];
 address pyeeAddress = acctIdToAddressMap[receivable.pyee];
 Account pyerAccount = accountMap[pyerAddress];
 Account pyeeAccount = accountMap[pyeeAddress];
 bytes32 pyerEnterpriseName = pyerAccount.enterpriseName;
 bytes32 pyerAcctSvcrName = pyerAccount.acctSvcrName;
 bytes32 pyeeEnterpriseName = pyeeAccount.enterpriseName;
 bytes32 pyeeAcctSvcrName = pyeeAccount.acctSvcrName;

 bytes32[] memory bytesInfo = new bytes32[](4);
 bytesInfo[0] = pyerEnterpriseName;
 bytesInfo[1] = pyerAcctSvcrName;
 bytesInfo[2] = pyeeEnterpriseName;
 bytesInfo[3] = pyeeAcctSvcrName;
 return bytesInfo;
 }
 */

/*
 function pyerAndPyeeAccountNameAndAcctSvcrName(bytes32 receivableNo) returns (bytes32[]){
 Receivable receivable = receivableDetailMap[receivableNo];
 Account account = accountMap[receivable.py];
 bytesInfo1[13] = account.lastStatus;
 bytesInfo1[14] = account.rate;
 bytesInfo1[15] = account.contractNo;
 bytesInfo1[16] = account.invoiceNo;
 }
 */
/*
 function getReceivableMostInfo(bytes32 receivableNo) returns(string){
 bytes32[] memory value = new bytes32[](12);
 value = getReceivableValue(receivableNo);
 return SewingBytes32ArrayToString(value);
 }

 function getReceivableValue(bytes32 receivableNo) returns(bytes32[]){
 Receivable receivable = receivableDetailMap[receivableNo];
 bytes32[] memory value = new bytes32[](12);

 value[0] = receivableNo;
 value[1] = receivable.orderNo;
 value[2] = receivable.signer;
 value[3] = receivable.accptr;
 value[4] = receivable.pyer;
 value[5] = receivable.pyee;
 value[6] = receivable.firstOwner;
 value[7] = receivable.secondOwner;
 value[8] = receivable.status;
 value[9] = receivable.lastStatus;
 value[10] = receivable.rate;
 value[11] = receivable.contractNo;
 value[12] = receivable.invoiceNo;
 return value;
 }

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
 */

/*
 //查找应收款的交易历史，返回流水号
 =======
 /*
 function pyerAndPyeeAccountNameAndAcctSvcrName(bytes32 receivableNo) returns (bytes32[]){
 Receivable receivable = receivableDetailMap[receivableNo];
 Account account = accountMap[receivable.py];
 bytesInfo1[13] = account.lastStatus;
 bytesInfo1[14] = account.rate;
 bytesInfo1[15] = account.contractNo;
 bytesInfo1[16] = account.invoiceNo;
 }
 */
/*
 function getReceivableMostInfo(bytes32 receivableNo) returns(string){
 bytes32[] memory value = new bytes32[](12);
 value = getReceivableValue(receivableNo);
 return SewingBytes32ArrayToString(value);
 }

 function getReceivableValue(bytes32 receivableNo) returns(bytes32[]){
 Receivable receivable = receivableDetailMap[receivableNo];
 bytes32[] memory value = new bytes32[](12);

 value[0] = receivableNo;
 value[1] = receivable.orderNo;
 value[2] = receivable.signer;
 value[3] = receivable.accptr;
 value[4] = receivable.pyer;
 value[5] = receivable.pyee;
 value[6] = receivable.firstOwner;
 value[7] = receivable.secondOwner;
 value[8] = receivable.status;
 value[9] = receivable.lastStatus;
 value[10] = receivable.rate;
 value[11] = receivable.contractNo;
 value[12] = receivable.invoiceNo;
 return value;
 }

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
 */

/*
 //查找应收款的交易历史，返回流水号
 function getReceivableHistorySerialNo(bytes32 receivableNo) returns (uint,bytes32[],uint[],ResponseType[]){
 //return (0, receivableTransferHistoryMap[receivableNo]);
 bytes32[] memory historyList1;
 historyList1 = receivableTransferHistoryMap[receivableNo];
 uint len = historyList1.length;
 bytes32[] memory bytesList = new bytes32[](len * 5);//5个值
 uint[] memory intList   = new uint[](len * 2);//2 ge
 ResponseType[] memory responseTypeList = new ResponseType[](len);//1 ge

 for (uint index = 0; index < len; index++) {
 bytesList[index * 5] = receivableRecordMap[historyList1[index]].receivableNo;
 bytesList[index * 5 + 1] = receivableRecordMap[historyList1[index]].serialNo;
 bytesList[index * 5 + 2] = receivableRecordMap[historyList1[index]].applicantAcctId;
 bytesList[index * 5 + 3] = receivableRecordMap[historyList1[index]].replyerAcctId;
 bytesList[index * 5 + 4] = receivableRecordMap[historyList1[index]].operateType;

 intList[index * 2] = receivableRecordMap[historyList1[index]].time;
 intList[index * 2 + 1] = receivableRecordMap[historyList1[index]].dealAmount;

 responseTypeList[index] = receivableRecordMap[historyList1[index]].responseType;
 }

 return (0,bytesList,intList,responseTypeList);
 }
 */
    function getReceivableHistorySerialNo(bytes32 receivableNo) returns (uint, bytes32[]){
        return (0, receivableTransferHistoryMap[receivableNo]);
    }

//流水号查询，自己查自己
    function getRecordBySerialNo(bytes32 serialNm) returns(uint, bytes32 serialNo, bytes32 receivableNo, bytes32 applicantAcctId, bytes32 replyerAcctId, ResponseType, uint, bytes32 operateType, uint, uint receivableStatus){
        Account account = accountMap[msg.sender];
        ReceivableRecord receivableRecord = receivableRecordMap[serialNm];
        if(serialNm == ""){
            return (3,
            serialNo,
            receivableNo,
            applicantAcctId,
            replyerAcctId,
            ResponseType.NULL,
            0,
            operateType,
            0,
            receivableStatus);
        }

        if(receivableRecord.serialNo == 0x0) {
            return(1013,
            serialNo,
            receivableNo,
            applicantAcctId,
            replyerAcctId,
            ResponseType.NULL,
            0,
            operateType,
            0,
            receivableStatus);
        }
        return(0,
        receivableRecord.serialNo,
        receivableRecord.receivableNo,
        receivableRecord.applicantAcctId,
        receivableRecord.replyerAcctId,
        receivableRecord.responseType,
        receivableRecord.time,
        receivableRecord.operateType,
        receivableRecord.dealAmount,
        receivableRecord.receivableStatus);

    }

}
contract RepositoryContract{
    OrderContract orderContract;

    uint REPO_BUSI_WATING_INCOME_RESPONSE = 1;//入库待响应
    uint REPO_BUSI_WATING_INCOME = 2;//待入库
    uint REPO_BUSI_INCOMED = 3;//已入库
    uint REPO_BUSI_WATING_OUTCOME = 5;//待出库
    uint REPO_BUSI_OUTCOMED = 6;//已出库

    uint REPO_CERT_TRANSABLE = 1;//可流转
    uint REPO_CERT_FREEZED = 2;//冻结中
    uint REPO_CERT_INVALID = 3;//已失效
//仓单结构体
    struct RepoCert{
        bytes32 incomeCert  ;// 入库凭证
        bytes32 repoCertNo  ;// 仓单编码（待确认生成规则）
        bytes32 repoBusinessNo  ;// 仓储业务编号
        address repoEnterpriseAddress   ;// 保管人(仓储公司)
        address storerAddress   ;// 存货人(用户名)
        address holderAddress   ;// 持有人：最初为存货人，经过背书转让后，即为受让人(公司名称)
        uint repoCreateDate  ;// 仓单填发日期
        bytes32 productName ;// 仓储物名称
        uint    productQuantitiy    ;// 仓储物数量
        bytes32 measureUnit ;// 仓储物计量单位
        bytes32 norms   ;// 仓储物规格(类似10cm*10cm)
        uint    productTotalPrice   ;// 货品合计金额(分)
        bytes32 productLocation ;// 仓储物场所（地址，前台填）
        uint    repoCertStatus  ;// 仓单状态
    }
//仓储结构体
    struct RepoBusiness{
        bytes32 repoBusinessNo  ;// 仓储业务编号
        bytes32 businessTransNo ;// 业务流转编号（仓储业务编号仓储状态）
        uint    repoBusiStatus  ;// 仓储状态（0-未定义,1-入库待响应,2-待入库,3-已入库,4（取消）-出库待响应,5-待出库,6-已出库）
        bytes32 orderNo ;// 订单号
        bytes32 inWayBillNo   ;// 入库运单号
        bytes32 repoCertNo  ;// 仓单编号
        address inLogisticsEnterpriseAddress  ;// 入库物流公司
        bytes32 outWayBillNo   ;// 入库运单号
        address outLogisticsEnterpriseAddress  ;// 出库物流公司
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
        uint operateOperateTime  ;// 操作时间(时间戳)
    }

    struct RepoCertOperationRecord{
        uint[] repoCertState;
        uint[] operationTime;
    }

    AccountContract accountContract;

//仓单编号 => 仓单操作记录列表
    mapping(bytes32=> RepoCertOperationRecord) repoCertRecordMap;


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

//仓储业务编号 => 仓单编号
    mapping(bytes32=> bytes32) repoBusiToCertMap;


    function repoCertNoExsit(bytes32 repoCertNo) internal returns (bool) {
        // 仓单不存在
        if (repoCertRecordMap[repoCertNo].repoCertState.length == 0) {
            return false;
        }
        //仓单存在
        else {
            return true;
        }
    }
    function repoBusinessNoExsit(bytes32 repoBusinessNo) internal returns (bool) {
        // 仓储业务编号不存在
        if (businessTransNoMap[repoBusinessNo].length == 0) {
            return false;
        }
        //仓储业务编号已存在
        else {
            return true;
        }
    }
    function businessTransNoExsit(bytes32 businessTransNo) internal returns (bool) {
        // 业务流转编号不存在
        if(businessDetailMap[businessTransNo].repoBusinessNo == "") {
            return false;
        }
        //业务流转编号已存在
        else {
            return true;
        }
    }

    //更新仓储结构体中的物流信息,入库物流公司address，入库运单号，出库物流公司address，出库运单号
    /*
     function updateLogisInfo(bytes32 repoCertNo,//仓单编号
     address logisticsEnterpriseAddress,//物流公司address
     bytes32 wayBillNo //运单号
     ) returns (uint){

     //判断仓单编号是否存在，不存在返回“仓单不存在”
     if(repoCertNoExsit(repoCertNo) == false){
     return 4001;//“仓单不存在”
     }

     RepoCert repoCert = repoCertDetailMap[repoCertNo];
     bytes32 repoBusinessNo = repoCert.repoBusinessNo;
     //
     bytes32[]  repoBusinessTransNoList = businessTransNoMap[repoBusinessNo];
     uint length = repoBusinessTransNoList.length;
     bytes32 newestBusinessTransNo = repoBusinessTransNoList[length - 1];

     //更新出库物流公司，出库运单号
     businessDetailMap[newestBusinessTransNo].logisticsEnterpriseAddress = logisticsEnterpriseAddress;
     businessDetailMap[newestBusinessTransNo].wayBillNo = wayBillNo;
     return (0);
     }
     */

    //更新仓储结构体中的物流信息,入库物流公司address，入库运单号，出库物流公司address，出库运单号
    function updateLogisInfo2(
        bytes32 payerRepoBusinessNo,//买家仓储业务编号
        bytes32 payeeRepoBusinessNo,//卖家仓储业务编号
        address logisticsEnterpriseAddress,//物流公司address
        bytes32 wayBillNo //运单号
) returns (uint){
        bytes32[]  payerRepoBusinessTransNoList = businessTransNoMap[payerRepoBusinessNo];
        uint payerlength = payerRepoBusinessTransNoList.length;
        bytes32 payerNewestBusinessTransNo = payerRepoBusinessTransNoList[payerlength - 1];

        bytes32[]  payeeRepoBusinessTransNoList = businessTransNoMap[payeeRepoBusinessNo];
        uint payeelength = payeeRepoBusinessTransNoList.length;
        bytes32 payeeNewestBusinessTransNo = payeeRepoBusinessTransNoList[payeelength - 1];

        //更新买家入库物流公司，入库运单号
        businessDetailMap[payerNewestBusinessTransNo].inLogisticsEnterpriseAddress = logisticsEnterpriseAddress;
        businessDetailMap[payerNewestBusinessTransNo].inWayBillNo = wayBillNo;

        //更新卖家出库物流公司，出库运单号
        businessDetailMap[payeeNewestBusinessTransNo].outLogisticsEnterpriseAddress = logisticsEnterpriseAddress;
        businessDetailMap[payeeNewestBusinessTransNo].outWayBillNo = wayBillNo;
        return (0);
    }

//查询我的仓单列表
    function getRepoCertInfoList(address userAddress)returns (uint, bytes32[] bytesResult,uint[] uintResult, address[] resultAddress){
        //通过用户地址查询该用户的仓储业务编号列表
        bytes32[] repoCertList =  usrRepoBusinessMap[userAddress];
        uint length = repoCertList.length;
        if(length == 0){
            return (0, bytesResult, uintResult, resultAddress);
        }
        bytesResult = new bytes32[](length * 2);
        uintResult = new uint[](length * 2);
        resultAddress = new address[](length);
        for(uint i = 0; i < repoCertList.length; i ++){
            //对于每个仓储流水号，找到业务流转编号列表
            bytes32[] busiTransNoList = businessTransNoMap[repoCertList[i]];
            //对于每个业务流转编号，找到对应的仓储结构体
            RepoBusiness repoBusiess = businessDetailMap[busiTransNoList[busiTransNoList.length - 1]];
            bytesResult[i*2] = repoBusiess.repoCertNo;
            bytesResult[i*2+1] = repoBusiess.productName;
            uintResult[i*2] = repoBusiess.productQuantitiy;

            //uintResult[i*2+1] = repoCertDetailMap[repoCertList[i]].repoCertStatus;
            bytes32 repoCertNo = repoBusiToCertMap[repoCertList[i]];
            uintResult[i*2+1] = repoCertDetailMap[repoCertNo].repoCertStatus;
            resultAddress[i] = repoBusiess.repoEnterpriseAddress;
        }
        return(0, bytesResult, uintResult, resultAddress);
    }


//查询我的仓储列表 1-入库管理(仓储状态为1-入库待响应,2-待入库,3-已入库)，2-出库管理（5-待出库,6-已出库），3-仓储机构（不区分状态）
    function getRepoBusiList(address userAddress)returns (uint,bytes32[] repoBusiDetail1,uint[] repoBusiDetail2,address[] repoBusiDetail3){
        bytes32[]  repoBusiNoList_2 =usrRepoBusinessMap[userAddress];

        RepoBusiness memory repoBusinsess ;
        RepoCert memory repoCert;

        repoBusiDetail1 = new bytes32[](repoBusiNoList_2.length * 5);
        repoBusiDetail2 = new uint[](repoBusiNoList_2.length * 4);
        repoBusiDetail3 = new address[](repoBusiNoList_2.length * 2);
        for(uint index = 0; index < repoBusiNoList_2.length; index++){
            uint n = index;

            uint returnNum = index;
            bytes32 repoBusinessNo  = repoBusiNoList_2[index];// 获取仓储业务号
            bytes32 repoBusiTransNo = getNewstTransNo(repoBusinessNo);

            repoBusinsess = businessDetailMap[repoBusiTransNo];//获取仓储结构体


            repoCert      = repoCertDetailMap[repoBusiToCertMap[repoBusinessNo]]; //仓单信息

            repoBusiDetail1[returnNum * 5]     = repoBusinsess.repoBusinessNo;
            repoBusiDetail1[returnNum * 5 + 1] = repoBusinsess.orderNo;
            repoBusiDetail1[returnNum * 5 + 2] = repoBusinsess.repoCertNo;
            repoBusiDetail1[returnNum * 5 + 3] = repoBusinsess.productName;
            repoBusiDetail1[returnNum * 5 + 4] = repoBusinsess.measureUnit;

            repoBusiDetail2[returnNum * 4 ]    = repoBusinsess.repoBusiStatus;
            repoBusiDetail2[returnNum * 4 + 1] = repoCert.repoCertStatus;
            repoBusiDetail2[returnNum * 4 + 2] = repoBusinsess.productQuantitiy;
            repoBusiDetail2[returnNum * 4 + 3] = repoBusinsess.operateOperateTime;

            repoBusiDetail3[returnNum * 2] = repoBusinsess.repoEnterpriseAddress;
            repoBusiDetail3[returnNum * 2 + 1] = repoBusinsess.holderAddress;

        }
        return(0, repoBusiDetail1, repoBusiDetail2, repoBusiDetail3);

    }



    function getNewstTransNo(bytes32 repoBusiNo) returns(bytes32){
        bytes32[] repoBusiTransNoList = businessTransNoMap[repoBusiNo];//获取仓储流转号

        bytes32 repoBusiTransNo = repoBusiTransNoList[repoBusiTransNoList.length-1];
        return repoBusiTransNo;
    }

//入库申请  1111,11111,0,"3434","4545",201010100101,"productName",100,100,10000
    function  incomeApply(
        address orderContractAddress,//订单合约地址，用来更改仓储状态
        bytes32 repoBusinessNo,       //  仓储业务编号
        bytes32 businessTransNo,      //  业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 1
        bytes32 orderNo,              //  订单编号
        address storerAddress,        //  存货人
        address repoEnterpriseAddress,//  保管人(仓储公司)
        uint operateOperateTime,   //  操作时间(时间戳)
        bytes32 productName,  //  仓储物名称
        uint    productQuantitiy,     //  仓储物数量
        uint    productUnitPrice,     //  货品单价(分)
        uint    productTotalPrice     //  货品合计金额(分)
) returns(uint,bytes32,bytes32) {
        //waittodo待补充验证存货人，仓储公司是否有效，账户合约提供接口

        //判断repoBusinessNo是否存在，若存在，返回“仓储业务编号已存在”
        if(repoBusinessNoExsit(repoBusinessNo) == true){
            return (4002, repoBusinessNo, "");
        }
        //判断businessTransNo是否存在，若存在，返回“业务流转编号已存在”
        if(businessTransNoExsit(businessTransNo) == true){
            return (4003, repoBusinessNo, "");
        }
        //加入存货人的列表
        usrRepoBusinessMap[storerAddress].push(repoBusinessNo);
        //加入仓储公司的列表
        usrRepoBusinessMap[repoEnterpriseAddress].push(repoBusinessNo);
        //加入业务流转编号列表
        businessTransNoMap[repoBusinessNo].push(businessTransNo);
        //更改仓储状态为1（入库待响应）
        orderContract = OrderContract(orderContractAddress);
        orderContract.updateOrderState(orderNo, "payerRepoBusiState", REPO_BUSI_WATING_INCOME_RESPONSE, "", 0);

        //仓储业务详情
        businessDetailMap[businessTransNo].repoBusinessNo = repoBusinessNo;
        businessDetailMap[businessTransNo].repoBusiStatus = REPO_BUSI_WATING_INCOME_RESPONSE;//RepoBusiStatus.WATING_INCOME_RESPONSE;
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
        businessDetailMap[businessTransNo].repoCertNo = "";

        return (0,repoBusinessNo,businessDetailMap[businessTransNo].businessTransNo);
    }

    function copyStruct(bytes32 originBusinessTransNo, bytes32 destBusinessTransNo) internal {
        RepoBusiness sorce = businessDetailMap[originBusinessTransNo];
        RepoBusiness dest = businessDetailMap[destBusinessTransNo];
        dest.repoBusinessNo             = sorce.repoBusinessNo      ;
        dest.repoBusiStatus             = sorce.repoBusiStatus      ;
        dest.businessTransNo        = sorce.businessTransNo     ;
        dest.orderNo                = sorce.orderNo             ;
        dest.repoCertNo                = sorce.repoCertNo             ;

        dest.inWayBillNo                = sorce.inWayBillNo             ;
        dest.inLogisticsEnterpriseAddress                = sorce.inLogisticsEnterpriseAddress             ;

        dest.outWayBillNo                = sorce.outWayBillNo             ;
        dest.outLogisticsEnterpriseAddress                = sorce.outLogisticsEnterpriseAddress             ;

        dest.incomeCert                = sorce.incomeCert             ;
        dest.measureUnit                = sorce.measureUnit             ;
        dest.norms                = sorce.norms             ;
        dest.productLocation                = sorce.productLocation             ;
        dest.storerAddress          = sorce.storerAddress       ;
        dest.holderAddress          = sorce.holderAddress       ;
        dest.repoEnterpriseAddress  = sorce.repoEnterpriseAddress;
        dest.operateOperateTime         = sorce.operateOperateTime;
        dest.productName            = sorce.productName         ;
        dest.productQuantitiy       = sorce.productQuantitiy    ;
        dest.productUnitPrice       = sorce.productUnitPrice    ;
        dest.productTotalPrice      = sorce.productTotalPrice   ;
    }

//入库响应-同意入库
    function incomeResponse(
        address orderContractAddress,//订单合约地址，用来更改仓储状态
        bytes32 repoBusinessNo,       //  仓储业务编号
        bytes32 lastBusinessTransNo,      //  上一个业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 0
        bytes32 currBusinessTransNo,      //  当前业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 1
        uint operateOperateTime,   //  操作时间(时间戳)
        address accountContractAddress
) returns(uint){
        //waittodo 待补充，仅允许仓储机构进行入库响应，同时必须是该仓储机构下单仓储业务

        //如果用户不存在，返回"账户不存在，该用户可能未注册或已失效"
        accountContract = AccountContract(accountContractAddress);
        if(!accountContract.isAccountExist(msg.sender)){
            return 2;
        }
        //如果用户不是仓储企业，返回"权限拒绝"
        if(accountContract.queryRoleCode(msg.sender) != 2){
            return 1;
        }

        if(businessTransNoExsit(lastBusinessTransNo) == false){
            return 4006;
        }
        //businessDetailMap[businessTransNo].
        copyStruct(lastBusinessTransNo,currBusinessTransNo);
        RepoBusiness repoBusinsess = businessDetailMap[currBusinessTransNo];
        repoBusinsess.repoBusiStatus = REPO_BUSI_WATING_INCOME;//
        repoBusinsess.businessTransNo = currBusinessTransNo;
        repoBusinsess.operateOperateTime = operateOperateTime;
        businessDetailMap[currBusinessTransNo] = repoBusinsess;

        //将新的操作记录加入业务流转编号列表
        businessTransNoMap[repoBusinessNo].push(currBusinessTransNo);
        //更改仓储状态为2（待入库）
        orderContract = OrderContract(orderContractAddress);
        orderContract.updateOrderState(repoBusinsess.orderNo, "payerRepoBusiState", REPO_BUSI_WATING_INCOME, "", 0);


        //waittodo 待补充 修改订单中的买家仓储状态
        return (0);
    }

//入库确认-已入库
    function incomeConfirm(
        address orderContractAddress,//订单合约地址，用来更改仓储状态
        bytes32 repoBusinessNo,       //  仓储业务编号
        bytes32 repoCertNo,
        bytes32 lastBusinessTransNo,      //  上一个业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 2
        bytes32 currBusinessTransNo,      //  当前业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 3
        uint operateTime,   //  操作时间(时间戳)
        address accountContractAddress,
        bytes32 txSerialNo
) returns(uint,bytes32){
        //waittodo 待补充，仅允许仓储机构进行入库响应，同时必须是该仓储机构下单仓储业务
        //如果用户不存在，返回"账户不存在，该用户可能未注册或已失效"
        accountContract = AccountContract(accountContractAddress);
        if(!accountContract.isAccountExist(msg.sender)){
            return (2, "");
        }
        //如果用户不是仓储企业，返回"权限拒绝"
        if(accountContract.queryRoleCode(msg.sender) != 2){
            return (1, "");
        }

        if(repoCertNoExsit(repoCertNo) == true){
            return (4004, "");
        }
        //businessDetailMap[businessTransNo].


        copyStruct(lastBusinessTransNo,currBusinessTransNo);
        RepoBusiness repoBusinsess = businessDetailMap[currBusinessTransNo];
        repoBusinsess.repoBusiStatus = REPO_BUSI_INCOMED;//RepoBusiStatus.INCOMED;
        repoBusinsess.businessTransNo = currBusinessTransNo;
        repoBusinsess.operateOperateTime = operateTime;
        repoBusinsess.repoCertNo = repoCertNo;
        businessDetailMap[currBusinessTransNo] = repoBusinsess;

        //将新的操作记录加入业务流转编号列表
        businessTransNoMap[repoBusinessNo].push(currBusinessTransNo);

        //记录仓单历史
        RepoCertOperationRecord opRecode = repoCertRecordMap[repoCertNo];
        opRecode.repoCertState.push(REPO_CERT_TRANSABLE);
        opRecode.operationTime.push(operateTime);


        //更改订单仓储状态为3（已入库）
        orderContract = OrderContract(orderContractAddress);
        orderContract.updateOrderState(repoBusinsess.orderNo, "payerRepoBusiState", REPO_BUSI_INCOMED, txSerialNo, operateTime);


        return repoCertNoApply( repoBusinessNo, repoCertNo, operateTime) ;

    }

//仓储机构生成仓单, 货品入库时生成，与订单无关,注意与incomeConfirm()的区别
    function  createRepoCertForRepoEnter(
        bytes32 repoBusinessNo,       //  仓储业务编号
        bytes32[2] noList ,//noList[0]=repoCertNo, //仓单编码,noList[1]=inWayBillNo //入库运单编号
        bytes32 businessTransNo,      //  仓储业务业务流转编号
        address storerAddress,        //  存货人=持有人holderAddress
        address repoEnterpriseAddress,//  保管人(仓储公司)
        bytes32 measureUnit ,// 仓储物计量单位
        bytes32 productName,  //  仓储物名称
        bytes32 productLocation,//
        uint[4] productIntInfo,  //0-productQuantitiy ,1-productUnitPrice ,2-productTotalPrice ,3-operateOperateTime
        address[2] addrList //addrList[0]=accountContractAddress,/addrList[1]=inLogisticsEnterpriseAddress 入库物流公司
) returns(uint) {
        //waittodo待补充验证存货人，仓储公司是否有效，账户合约提供接口
        if(repoCertNoExsit(noList[0]) == true){
            return 4004;//“仓单已存在”
        }
        //如果用户不存在，返回"账户不存在，该用户可能未注册或已失效"
        accountContract = AccountContract(addrList[0]);
        if(!accountContract.isAccountExist(msg.sender)){
            return (2);
        }
        //如果用户不是仓储企业，返回"权限拒绝"
        if(accountContract.queryRoleCode(msg.sender) != 2){
            return (1);
        }
        repoBusiToCertMap[repoBusinessNo] = noList[0];//repoCertNo;
        //加入存货人的列表
        usrRepoBusinessMap[storerAddress].push(repoBusinessNo);
        //加入仓储公司的列表
        usrRepoBusinessMap[repoEnterpriseAddress].push(repoBusinessNo);
        //加入业务流转编号列表
        businessTransNoMap[repoBusinessNo].push(businessTransNo);

        usrRepoCertListMap[storerAddress].push(noList[0]);//持有人仓单列表
        usrRepoCertListMap[repoEnterpriseAddress].push(noList[0]);//仓储公司仓单列表



        repoCertDetailMap[noList[0]] = RepoCert("",
            noList[0],
            repoBusinessNo,
            repoEnterpriseAddress,
            storerAddress,
            storerAddress,
            productIntInfo[3],
            productName,
            productIntInfo[0],
            measureUnit,
            "",
            productIntInfo[2],
            productLocation,
            REPO_CERT_TRANSABLE
        );
        //记录仓单历史
        RepoCertOperationRecord opRecode = repoCertRecordMap[noList[0]];
        opRecode.repoCertState.push(REPO_CERT_TRANSABLE);
        opRecode.operationTime.push(productIntInfo[3]);

        //仓储业务详情
        businessDetailMap[businessTransNo].repoBusinessNo = repoBusinessNo;
        businessDetailMap[businessTransNo].repoBusiStatus = REPO_BUSI_INCOMED;
        businessDetailMap[businessTransNo].businessTransNo = businessTransNo;
        businessDetailMap[businessTransNo].storerAddress = storerAddress;
        businessDetailMap[businessTransNo].holderAddress = storerAddress;
        businessDetailMap[businessTransNo].repoEnterpriseAddress = repoEnterpriseAddress;
        businessDetailMap[businessTransNo].operateOperateTime = productIntInfo[3];
        businessDetailMap[businessTransNo].productName = productName;
        businessDetailMap[businessTransNo].productQuantitiy = productIntInfo[0];
        businessDetailMap[businessTransNo].productUnitPrice = productIntInfo[1];
        businessDetailMap[businessTransNo].productTotalPrice = productIntInfo[2];
        businessDetailMap[businessTransNo].repoCertNo = noList[0];
        businessDetailMap[businessTransNo].inWayBillNo = noList[1];
        businessDetailMap[businessTransNo].inLogisticsEnterpriseAddress = addrList[1];

        return (0);
    }

//出库申请-企业
    function outcomeApply(
        address orderContractAddress,//订单合约地址，用来更改仓储状态
        bytes32 repoBusinessNo,       //  仓储业务编号
        bytes32 lastBusinessTransNo,      //  上一个业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 2
        bytes32 currBusinessTransNo,      //  当前业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 3
        uint operateOperateTime   //  操作时间(时间戳)
) returns(uint){

        //waittodo 待补充

        //businessDetailMap[businessTransNo].
        copyStruct(lastBusinessTransNo,currBusinessTransNo);
        RepoBusiness repoBusinsess = businessDetailMap[currBusinessTransNo];
        repoBusinsess.repoBusiStatus = REPO_BUSI_WATING_OUTCOME;//
        repoBusinsess.businessTransNo = currBusinessTransNo;
        repoBusinsess.operateOperateTime = operateOperateTime;
        businessDetailMap[currBusinessTransNo] = repoBusinsess;

        //将新的操作记录加入业务流转编号列表
        businessTransNoMap[repoBusinessNo].push(currBusinessTransNo);
        return (0);
    }

    function bytes32ToString(bytes32 x)returns (string) {
        bytes memory bytesString = new bytes(32);
        uint charCount = 0;
        for (uint j = 0; j < 32; j++) {
            byte char = byte(bytes32(uint(x) * 2 ** (8 * j)));
            if (char != 0) {
                bytesString[charCount] = char;
                charCount++;
            }
        }
    }

//通过仓单编号找到仓储业务编号
    function getRepoBusinessByRepoCert(bytes32 repoCertNo//  仓单编号
) returns (uint,bytes32) {
        RepoCert repoCert = repoCertDetailMap[repoCertNo];
        bytes32 repoBusinessNo = repoCert.repoBusinessNo;
        return (0,repoBusinessNo);
    }

//出库回复-待出库,卖家确认订单时仓储状态改为待出库
    function outcomeResponse(
        address orderContractAddress,//订单合约地址，用来更改仓储状态
        bytes32 repoCertNo,       //  仓单编号
        bytes32 lastBusinessTransNo,      //  上一个业务流转编号（仓储业务编号仓储状态）:仓储业务编号
        bytes32 currBusinessTransNo,      //  当前业务流转编号（仓储业务编号仓储状态）:仓储业务编号
        uint operateOperateTime  , //  操作时间(时间戳)
        bytes32 orderNo //订单编号，//陈晓阳。20170507修改。在订单确认时，将订单编号存到卖家仓储结构体中（因为通过swagger的api直接生成的仓储结构体中订单编号是空的）
) returns(uint,bytes32){
        //waittodo 待补充，仅允许仓储机构进行入库响应，同时必须是该仓储机构下单仓储业务

        if(repoCertNoExsit(repoCertNo) == false){
            return (4001, "");
        }
        //获取仓储业务编号
        RepoCert repoCert =  repoCertDetailMap[repoCertNo];
        bytes32  repoBusinessNo = repoCert.repoBusinessNo;
        /*
         //获取上一个流转号
         string memory s1 = bytes32ToString(repoBusinessNo);
         string memory s2 = bytes32ToString(bytes32(REPO_BUSI_INCOMED));

         string memory conStr = concatString(s1, s2);
         bytes32 lastBusinessTransNo = stringToBytes32(conStr);

         //获取新的流转号
         string memory s3 = bytes32ToString(bytes32(REPO_BUSI_WATING_OUTCOME));
         conStr = concatString(s1, s3);
         bytes32 currBusinessTransNo = stringToBytes32(conStr);
         */
        //bytes32 lastBusinessTransNo;
        //bytes32 currBusinessTransNo;
        //更新仓储状态为待出库
        copyStruct(lastBusinessTransNo,currBusinessTransNo);
        RepoBusiness repoBusinsess = businessDetailMap[currBusinessTransNo];
        repoBusinsess.orderNo = orderNo;//陈晓阳。20170507修改
        repoBusinsess.repoBusiStatus = REPO_BUSI_WATING_OUTCOME;//RepoBusiStatus.INCOMED;
        repoBusinsess.businessTransNo = currBusinessTransNo;
        repoBusinsess.operateOperateTime = operateOperateTime;
        businessDetailMap[currBusinessTransNo] = repoBusinsess;

        //将新的操作记录加入业务流转编号列表
        businessTransNoMap[repoBusinessNo].push(currBusinessTransNo);

        //记录仓单操作历史
        RepoCertOperationRecord opRecode = repoCertRecordMap[repoCertNo];
        opRecode.repoCertState.push(REPO_CERT_FREEZED);
        opRecode.operationTime.push(operateOperateTime);
        //更新仓单状态为冻结中
        //RepoCert repoCert =  repoCertDetailMap[repoCertNo];
        repoCert.repoCertStatus = REPO_CERT_FREEZED;


        //更新订单中的卖家状态为 REPO_BUSI_WATING_OUTCOME
        orderContract = OrderContract(orderContractAddress);
        orderContract.updateOrderState(repoBusinsess.orderNo, "payeeRepoBusiState", REPO_BUSI_WATING_OUTCOME, "", 0);

        return (0,lastBusinessTransNo);
    }

    struct slice {
        uint _len;
        uint _ptr;
    }

    function toSlice(string self) internal returns (slice) {
        uint ptr;
        assembly {
            ptr := add(self, 0x20)
        }
        return slice(bytes(self).length, ptr);
    }

    function memcpy(uint dest, uint src, uint len) private {
        for(; len >= 32; len -= 32) {
            assembly {
                mstore(dest, mload(src))
            }
            dest += 32;
            src += 32;
        }

        uint mask = 256 ** (32 - len) - 1;
        assembly {
            let srcpart := and(mload(src), not(mask))
            let destpart := and(mload(dest), mask)
            mstore(dest, or(destpart, srcpart))
        }
    }

    function concat(slice self, slice other) internal returns (string) {
        var ret = new string(self._len + other._len);
        uint retptr;
        assembly { retptr := add(ret, 32) }
        memcpy(retptr, self._ptr, self._len);
        memcpy(retptr + self._len, other._ptr, other._len);
        return ret;
    }
    //（1）两个字符串拼接
    function concatString(string _a,
        string _b) internal returns (string) {
        return (concat(toSlice(_a), toSlice(_b)));
    }


    function stringToBytes32(string memory source)returns (bytes32 result) {
        assembly {
            result := mload(add(source, 32))
        }
    }



    //出库确认-已出库
    function outcomeConfirm(
        address orderContractAddress,//
        bytes32 repoBusinessNo,       //  仓储业务编号
        bytes32 lastBusinessTransNo,      //  上一个业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 4
        bytes32 currBusinessTransNo,      //  当前业务流转编号（仓储业务编号仓储状态）:仓储业务编号 + 5
        uint operateOperateTime,   //  操作时间(时间戳)
        address accountContractAddress
) returns(uint){
        //repoBusinessNo存在才允许后续操作
        if(repoBusinessNoExsit(repoBusinessNo) == false){
            return (4005);
        }
        //如果用户不存在，返回"账户不存在，该用户可能未注册或已失效"
        accountContract = AccountContract(accountContractAddress);
        if(!accountContract.isAccountExist(msg.sender)){
            return (2);
        }
        //如果用户不是仓储企业，返回"权限拒绝"
        if(accountContract.queryRoleCode(msg.sender) != 2){
            return (1);
        }
        //businessDetailMap[businessTransNo].
        /*
         RepoBusiness repoBusinsess = businessDetailMap[lastBusinessTransNo];
         repoBusinsess.repoBusiStatus = 6;//RepoBusiStatus.OUTCOMED;
         repoBusinsess.businessTransNo = currBusinessTransNo;
         repoBusinsess.operateOperateTime = operateOperateTime;
         businessDetailMap[currBusinessTransNo] = repoBusinsess;
         */
        copyStruct(lastBusinessTransNo,currBusinessTransNo);
        RepoBusiness repoBusinsess = businessDetailMap[currBusinessTransNo];
        repoBusinsess.repoBusiStatus = REPO_BUSI_OUTCOMED;//RepoBusiStatus.INCOMED;
        repoBusinsess.businessTransNo = currBusinessTransNo;
        repoBusinsess.operateOperateTime = operateOperateTime;
        businessDetailMap[currBusinessTransNo] = repoBusinsess;

        //将新的操作记录加入业务流转编号列表
        businessTransNoMap[repoBusinessNo].push(currBusinessTransNo);

        //记录仓单操作历史
        bytes32 repoCertNo = repoBusiToCertMap[repoBusinessNo];
        RepoCertOperationRecord opRecode = repoCertRecordMap[repoCertNo];
        opRecode.repoCertState.push(REPO_CERT_INVALID);
        opRecode.operationTime.push(operateOperateTime);
        //更新仓单状态为失效
        RepoCert repoCert =  repoCertDetailMap[repoCertNo];
        repoCert.repoCertStatus = REPO_CERT_INVALID;
        //更新订单中的卖家状态为 REPO_CERT_INVALID
        orderContract = OrderContract(orderContractAddress);
        orderContract.updateOrderState(repoBusinsess.orderNo, "payeeRepoBusiState", REPO_BUSI_OUTCOMED, "", 0);
        return (0);
    }

    //仓单生成
    function repoCertNoApply(bytes32 repoBusinessNo,bytes32 repoCertNo,uint operateTime) returns (uint,bytes32){
        repoBusiToCertMap[repoBusinessNo] = repoCertNo;

        //users.length
        bytes32[] memory transList = businessTransNoMap[repoBusinessNo];
        uint index = transList.length - 1;
        bytes32 businessTransNo = transList[index];

        RepoBusiness currentRepoBusinsess =  businessDetailMap[businessTransNo];
        //设置仓单编号
        currentRepoBusinsess.repoCertNo = repoCertNo;

        address holderAddress = currentRepoBusinsess.holderAddress;
        address repoEnterpriseAddress = currentRepoBusinsess.repoEnterpriseAddress;

        usrRepoCertListMap[holderAddress].push(repoCertNo);//持有人仓单列表
        usrRepoCertListMap[repoEnterpriseAddress].push(repoCertNo);//仓储公司仓单列表


        //repoCertDetailMap[repoCertNo] = repoCert;//仓单编号 -> 仓单详情
        repoCertDetailMap[repoCertNo] =  RepoCert("",
            repoCertNo,
            repoBusinessNo,
            repoEnterpriseAddress,
            holderAddress,
            holderAddress,
            operateTime,
            currentRepoBusinsess.productName,
            currentRepoBusinsess.productQuantitiy,
            currentRepoBusinsess.measureUnit,
            currentRepoBusinsess.norms,
            currentRepoBusinsess.productTotalPrice,
            currentRepoBusinsess.productLocation,
            REPO_CERT_TRANSABLE
        );
        return (0,repoCertNo);
    }
    //仓储业务历史列表
    /* bytes32 businessTransNo ;// 业务流转编号（仓储业务编号仓储状态）
     uint    repoBusiStatus  ;// 仓储状态（0-未定义,1-入库待响应,2-待入库,3-已入库,4-出库待响应,5-待出库,6-已出库）

     bytes32 operateOperateTime  ;// 操作时间(时间戳)*/
    function getRepoBusiHistoryList(bytes32 repoBusinessNo) returns (uint,bytes32[],uint[]){
        bytes32[] memory historyList1;
        historyList1 = businessTransNoMap[repoBusinessNo];
        uint len = historyList1.length;

        bytes32[] memory bytesList = new bytes32[](len);//1个值
        uint[] memory intList   = new uint[](len*2);//2 ge

        for (uint index = 0; index < len; index++) {
            bytesList[index * 2] = historyList1[index];//liu shui hao
            //bytesList[index * 2 + 1] = businessDetailMap[historyList1[index]].operateOperateTime;

            intList[index* 2 + 1] = businessDetailMap[historyList1[index]].repoBusiStatus;
            intList[index* 2 ] = businessDetailMap[historyList1[index]].operateOperateTime;
        }

        return (0,bytesList,intList);
    }

    function getRepoBusiDtlAndHistoryList(bytes32 repoBusinessNo) returns (uint,uint[],bytes32[],uint[],address[]){
        //===================获取操作历史列表，包含操作流水号，操作时间=================／／
        bytes32[] memory historyList1;
        historyList1 = businessTransNoMap[repoBusinessNo];
        uint len = historyList1.length;

        //bytes32[] memory bytesList = new bytes32[](len * 2);//2个值
        uint[] memory historyList   = new uint[](len * 2);//2 ge

        //bytes32 repoBusiTranNo;
        RepoBusiness memory repoBusinsess;
        for (uint index = 0; index < len; index++) {
            //repoBusiTranNo  = historyList1[index];
            repoBusinsess = businessDetailMap[historyList1[index]];
            /*
             historyList[index * 2] = historyList1[index];//流水号
             historyList[index * 2 + 1] = businessDetailMap[historyList1[index]].operateOperateTime;
             */

            historyList[index * 2] = repoBusinsess.repoBusiStatus;//
            historyList[index * 2 + 1] = repoBusinsess.operateOperateTime;

            //intList[index] = businessDetailMap[historyList1[index]].repoBusiStatus;
        }

        //===================获取仓储详情=================
        bytes32[]   memory detailInfoList1 = new bytes32[](6);//6个值
        uint[]      memory detailInfoList2 = new uint[](4);//4个值
        address[]   memory detailInfoList3 = new address[](4);//4个值

        detailInfoList1[0] = repoBusinsess.repoBusinessNo;
        detailInfoList1[1] = repoBusinsess.inWayBillNo;
        detailInfoList1[2] = repoBusinsess.repoCertNo;
        detailInfoList1[3] = repoBusinsess.productName;
        detailInfoList1[4] = repoBusinsess.measureUnit;
        detailInfoList1[5] = repoBusinsess.outWayBillNo;

        detailInfoList2[0] = repoBusinsess.repoBusiStatus;
        detailInfoList2[1] = repoBusinsess.productQuantitiy;
        detailInfoList2[2] = repoBusinsess.productTotalPrice;
        detailInfoList2[3] = repoBusinsess.operateOperateTime;

        detailInfoList3[0] = repoBusinsess.inLogisticsEnterpriseAddress;
        detailInfoList3[1] = repoBusinsess.repoEnterpriseAddress;
        detailInfoList3[2] = repoBusinsess.storerAddress;
        detailInfoList3[3] = repoBusinsess.outLogisticsEnterpriseAddress;

        return (0,historyList,detailInfoList1,detailInfoList2,detailInfoList3);
    }

    //查询仓储业务详情详情
    function getRepoBusinessDetail(bytes32 businessTransNo) returns(uint,
        uint,/// 仓储状态
        address,//存货人(申请人)
        bytes32[] ,//2个
        uint[]   //4个
    ) {
        //waittodo 校验
        RepoBusiness busDtl =  businessDetailMap[businessTransNo];
        bytes32[] memory  outBytesList = new bytes32[](3);
        uint[] memory  outUintList = new uint[](3);
        /**/
        outBytesList[0] = busDtl.productName;
        outBytesList[1] = busDtl.measureUnit;
        //outBytesList[2] = busDtl.operateOperateTime;

        /**/
        outUintList[0] = busDtl.productQuantitiy;
        outUintList[1] = busDtl.productUnitPrice;
        outUintList[2] = busDtl.productTotalPrice;
        outUintList[3] = busDtl.operateOperateTime;

        return (0,
                busDtl.repoBusiStatus,
                busDtl.storerAddress,
                outBytesList,
                outUintList
        );

    }

    //查询仓单详情
    function getRepoCertDetail(bytes32 repoCertNo) returns(uint,bytes32[] ,address[] ,uint[]) {
        //waittodo 校验

        RepoCert repoCertDtl =  repoCertDetailMap[repoCertNo];
        bytes32[] memory  outBytesList = new bytes32[](7);
        address[] memory  outAddressList = new address[](3);
        uint recordLength = repoCertRecordMap[repoCertNo].repoCertState.length;
        uint totalLength = 3 + recordLength*2;
        uint[] memory outUintList = new uint[](totalLength);

        outBytesList[0] = repoCertDtl.incomeCert;
        outBytesList[1] = repoCertDtl.repoCertNo;
        outBytesList[2] = repoCertDtl.repoBusinessNo;
        //outBytesList[3] = repoCertDtl.repoCreateDate;
        outBytesList[3] = repoCertDtl.productName;
        outBytesList[4] = repoCertDtl.measureUnit;
        outBytesList[5] = repoCertDtl.norms;
        outBytesList[6] = repoCertDtl.productLocation;

        outAddressList[0] = repoCertDtl.repoEnterpriseAddress;
        outAddressList[1] = repoCertDtl.storerAddress;
        outAddressList[2] = repoCertDtl.holderAddress;

        outUintList[0] = repoCertDtl.productQuantitiy;
        outUintList[1] = repoCertDtl.productTotalPrice;
        outUintList[2] = repoCertDtl.repoCreateDate;

        for(uint i = 3; i < totalLength; i ++){
            if(i < recordLength + 3){
                outUintList[i] = repoCertRecordMap[repoCertNo].repoCertState[i - 3];
            }
            if(i >= recordLength + 3){
                outUintList[i] = repoCertRecordMap[repoCertNo].operationTime[i - recordLength - 3];
            }
        }

        return (0,
            outBytesList,
            outAddressList,
            outUintList);

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
contract OrderContract{
address owner;
//==========运单状态=======
uint UNDEFINED = 0;           //未定义
uint WAITSEND = 1;            //待发货
uint REQUESTING = 2;          //发货待响应
uint REJECTED = 3;            //发货被拒绝
uint SENDING = 4;             //已发货
uint RECEIVED = 5;            //已送达
//=========仓储状态=======
uint WATING_INCOME_RESPONSE = 1;    //入库待响应
uint WATING_INCOME = 2;             //待入库
uint INCOMED = 3;                   //已入库
uint WATING_OUTCOME_RESPONSE = 4;   //出库待响应
uint WATING_OUTCOME = 5;            //待出库
uint OUTCOMED = 6;                  //已出库

    //=========仓单状态=======
    uint TRANSABLE = 1;                //可流转
    uint FREED = 2;                    //冻结中
    uint INVALID = 3;                  //已失效

    //==========订单交易状态=======
    uint UNCONFIRMED = 1;    //待确认
    uint CONFIRMED = 2;      //已确认
    uint COMPLETED = 3;

    //==========应收款状态=======
    uint SETTLED = 1; //已结清
    uint CANCELLED = 2;//已作废
    uint SIGNOUT_REFUSED= 3;//签收拒绝
    uint WAITING_SIGNOUT = 10;//待签发
    uint WAITING_ACCEPT = 21;//承兑待签收
    uint ACCEPTED = 26;//承兑已签收
    uint CASHED = 31;//已兑付
    uint PART_CASHED = 36;//已部分兑付
    uint CASH_REFUSED = 39;//兑付失败
    uint WAITING_DISCOUNT = 41;//贴现待签收
    uint DISCOUNTED = 46;//贴现已签收
    uint PART_DISCOUNTED = 48;//已部分贴现
    uint ALL_DISCOUNTED = 49;//已全额贴现

    //==========应收款状态=======
    /*1      已结清
     2      已作废
     3      签收拒绝
     10     待签发
     21     承兑待签收
     26     承兑已签收
     31     已兑付
     36     已部分兑付
     39     兑付失败
     41     贴现待签收
     46     贴现已签收
     48     已部分贴现
     49     已全额贴现*/
    AccountContract accountContract;
    WayBillContract wayBillContract;
    ReceivableContract receivableContract;
    RepositoryContract repositoryContract;
    function Reparo(){
        owner = msg.sender;
    }

    enum PayingMethod {
        RECEIVABLE,   //0, 应收账款方式
        CASH          //1, 现金方式
    }

    struct OrderState {
        uint txState;       //
        uint payerRepoBusiState; //
        uint payeeRepoBusiState;
        uint wayBillState;  //
        uint receState;     //
    }
    function queryTxStateOfOrderState(bytes32 orderNo) returns(uint txState){
        Order order = orderDetailMap[orderNo];
        return order.orderState.txState;
    }
    function queryPayerRepoBusiStateOfOrderState(bytes32 orderNo) returns(uint txState){
        Order order = orderDetailMap[orderNo];
        return order.orderState.payerRepoBusiState;
    }
    function queryPayeeRepoBusiStateOfOrderState(bytes32 orderNo) returns(uint txState){
        Order order = orderDetailMap[orderNo];
        return order.orderState.payeeRepoBusiState;
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
        address payerRepoAddress;//买家仓储公司
        address payeeRepoAddress;//卖家仓储公司
        bytes32 payerRepoBusinessNo;//仓储业务流水号 --买家申请订单时生成仓储业务号
        bytes32 payeeRepoBusinessNo;//仓储业务流水号 --卖家
        bytes32 payerRepoCertNo;// 买家仓单编号  --买家填仓单编号
        bytes32 payeeRepoCertNo;//卖家仓单编号  --卖家填仓单编号
        uint orderGenerateTime;//订单生成时间
        bytes32 payerBank;//付款银行
        bytes32 payerBankClss;//开户行别
        bytes32 payerAccount;//付款人开户行
        PayingMethod payingMethod;//付款方式
        OrderState orderState;//订单状态
    }
    //for yf
    function queryPayerRepoEnterpriseName(bytes32 orderNo, address accountAddress) returns(bytes32){
        Order order = orderDetailMap[orderNo];
        accountContract = AccountContract(accountAddress);
        return accountContract.getEnterpriseNameByAddress(order.payerRepoAddress);
    }
    function queryPayeeRepoEnterpriseName(bytes32 orderNo, address accountAddress) returns(bytes32){
        Order order = orderDetailMap[orderNo];
        accountContract = AccountContract(accountAddress);
        return accountContract.getEnterpriseNameByAddress(order.payeeRepoAddress);
    }
    function queryPayerRepoCertNo(bytes32 orderNo) returns(bytes32){
        Order order = orderDetailMap[orderNo];
        return order.payerRepoCertNo;
    }
    function queryPayeeRepoCertNo(bytes32 orderNo) returns(bytes32){
        Order order = orderDetailMap[orderNo];
        return order.payeeRepoCertNo;
    }
    //操作记录
    struct TransactionRecord {
        bytes32 orderNo;//订单编号
        bytes32 txSerialNo;//交易流水号
        uint txState;
        uint time;
    }

    // 订单id => 处理订单详情
    mapping( bytes32 => Order ) orderDetailMap;

    // 账号 => 所有作为买方的订单列表（包含现在持有和历史订单）
    mapping(address => bytes32[]) allPayerOrderMap;

    // 账号 => 所有作为卖方的订单列表（包含现在持有和历史订单）
    mapping(address => bytes32[]) allPayeeOrderMap;

    //操作记录流水号 => 操作记录详情
    mapping(bytes32 => TransactionRecord) txRecordDetailMap;

    //订单编号 => 交易流水号数组
    mapping(bytes32 => bytes32[]) txSerialNoList;

    // 根据特定需要，该数组用于返回某种订单数组
    bytes32[] tempOrderList;

    function isValidUser(uint accountState) internal returns (bool) {
        // 无效用户(包括未注册的用户)
        if (accountState != 0) {
            return false;
        }
        else {
            return true;
        }
    }


    /****************************买方新建订单**************************************/

    function createOrder (
    address acctContractAddress,    //账户合约地址
    address payeeAddress,           //卖方地址
    address payerRepoAddress,       //买方仓储公司地址
    uint productUnitPrice,          //货品单价
    uint productQuantity,           //货品数量
    uint productTotalPrice,         //货品总价
    bytes32[] bytes32Params,        //数组里有7个参数，orderNo，productName，repoBusinessNo
    //payerBank, payerBankClss, payerAccount, txSerialNo
    PayingMethod payingMethod,      //付款方式
    uint orderGenerateTime) returns(uint){  //生成订单时间

        //此处从公用合约处获取调用者的角色和账户状态以做权限控制
        //如果用户不存在，返回"账户不存在，该用户可能未注册或已失效"
        accountContract = AccountContract(acctContractAddress);
        if(!accountContract.isAccountExist(msg.sender)){
            return 2;
        }

        //如果用户不是融资企业，返回"权限拒绝"
        if(accountContract.queryRoleCode(msg.sender) != 0){
            return 1;
        }
        bytes32 orderNo = bytes32Params[0];
        //如果订单号已经存在，返回"该订单号已经存在"
        if(orderDetailMap[orderNo].orderNo != 0){
            return 2004;
        }
        orderDetailMap[orderNo].orderNo = orderNo;
        orderDetailMap[orderNo].productName = bytes32Params[1];
        orderDetailMap[orderNo].payerRepoBusinessNo = bytes32Params[2];
        orderDetailMap[orderNo].payerBank = bytes32Params[3];
        orderDetailMap[orderNo].payerBankClss = bytes32Params[4];
        orderDetailMap[orderNo].payerAccount = bytes32Params[5];
        orderDetailMap[orderNo].payerAddress = msg.sender; //买方公钥
        orderDetailMap[orderNo].payeeAddress = payeeAddress;//卖方公钥
        orderDetailMap[orderNo].payerRepoAddress = payerRepoAddress;
        orderDetailMap[orderNo].productUnitPrice = productUnitPrice;
        orderDetailMap[orderNo].productQuantity = productQuantity;
        orderDetailMap[orderNo].productTotalPrice = productTotalPrice;
        orderDetailMap[orderNo].payingMethod = payingMethod;
        orderDetailMap[orderNo].orderGenerateTime = orderGenerateTime;
        updateOrderState(orderNo, "txState", 1, txSerialNo, orderGenerateTime);      //更新买方的所有订单和卖方所有的订单
        allPayerOrderMap[msg.sender].push(orderNo);
        allPayeeOrderMap[payeeAddress].push(orderNo);
        //提取交易流水号，添加操作记录
        bytes32 txSerialNo = bytes32Params[6];
        txRecordDetailMap[txSerialNo].orderNo = orderNo;
        txRecordDetailMap[txSerialNo].txSerialNo = txSerialNo;
        txRecordDetailMap[txSerialNo].time = orderGenerateTime;
        txRecordDetailMap[txSerialNo].txState = 1;
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
    function searchReceAndWayBillInfo(address receAddress, address wbillContractAddress, address accountContractAddr,  bytes32 orderNo) returns(
    bytes32[] param1,
    uint[] param2,
    address[] param3){
        //此处获取应收款信息
        receivableContract = ReceivableContract(receAddress);
        bytes32[4] memory receBytesList;
        uint[5] memory receUintList;
        uint result;
        (result, receBytesList, receUintList) = receivableContract.getReceivableSimpleInfoByOrderNo(orderNo);
        //此处获取物流信息
        wayBillContract  = WayBillContract(wbillContractAddress);
        bytes32 wayBillNo;
        uint requestTime;
        address logisticsAddress;
        uint waybillStatus;
        uint operateTime;
        (wayBillNo, requestTime, logisticsAddress, waybillStatus, operateTime) = wayBillContract.getWayBillOverview(orderNo, accountContract);

        param1 = new bytes32[](5);
        param2 = new uint[](8);
        param3 = new address[](1);
        param1[0] = receBytesList[0];
        param1[1] = receBytesList[1];
        param1[2] = receBytesList[2];
        param1[3] = receBytesList[3];

        param2[0] = receUintList[0];
        param2[1] = receUintList[1];
        param2[2] = receUintList[2];
        param2[3] = receUintList[3];
        param2[4] = receUintList[4];

        //此处为运单信息
        param1[4] = wayBillNo;//运单号
        param2[5] = requestTime; //下单时间
        param2[6] = waybillStatus;  //当前状态
        param2[7] = operateTime;  //更新时间
        param3[0] = logisticsAddress;  //物流公司地址
        return(param1, param2, param3);
    }

    /*******************************根据订单编号买家，卖家仓储我业务编号***********************************/
    function qryRepoBusiNos(bytes32 orderNo) returns (uint exeFlag,bytes32 payerRepoBusinessNo,bytes32 payeeRepoBusinessNo){
        //如果订单不存在，返回"订单不存在"
        if(!orderExists(orderNo)){
            return (2001, "0", "0");
        }
        Order order = orderDetailMap[orderNo];
        return (0,order.payerRepoBusinessNo,order.payeeRepoBusinessNo);

    }

    /*******************************根据订单编号获取订单详情***********************************/
    function queryOrderDetail(address receAddress, address wbillContractAddress, address accountContractAddr, bytes32 orderNo) returns(
    uint,
    address[] resultAddress,    //
    bytes32[] resultBytes32,    //
    uint[] resultUint,          //
    PayingMethod resultMethod,  //
    uint txState){              //交易状态
        //如果用户不存在，返回"账户不存在，该用户可能未注册或已失效"
        accountContract = AccountContract(accountContractAddr);
        if(!accountContract.isAccountExist(msg.sender)){
            return (2, resultAddress, resultBytes32, resultUint, resultMethod, txState);
        }

        //如果用户不是融资企业，返回"权限拒绝"
        if(accountContract.queryRoleCode(msg.sender) != 0){
            return (1, resultAddress, resultBytes32, resultUint, resultMethod, txState);
        }

        //如果订单不存在，返回"订单不存在"
        if(!orderExists(orderNo)){
            return (2001, resultAddress, resultBytes32, resultUint, resultMethod, txState);
        }


        Order order = orderDetailMap[orderNo];
        //如果订单与合约调用者无关，"权限拒绝"
        if (order.payerAddress != msg.sender && order.payeeAddress != msg.sender) {
            return (2005, resultAddress, resultBytes32, resultUint, resultMethod, txState);
        }


        bytes32[] memory param1 = new bytes32[](5);
        uint[] memory param2 = new uint[](8);
        address[] memory param3 = new address[](1);

        (param1, param2, param3) = searchReceAndWayBillInfo(receAddress, wbillContractAddress, accountContractAddr, orderNo);
        resultUint = new uint[](16);
        resultBytes32 = new bytes32[](14);
        resultAddress = new address[](5);

        resultAddress[0] = order.payerAddress;
        resultAddress[1] = order.payeeAddress;
        resultAddress[2] = order.payerRepoAddress;
        resultAddress[3] = order.payeeRepoAddress;
        //交易信息
        resultBytes32[0] = order.orderNo;
        resultBytes32[1] = order.productName;
        resultBytes32[2] = order.payerBank;
        resultBytes32[3] = order.payerBankClss;
        resultBytes32[4] = order.payerAccount;

        resultUint[0] = order.productUnitPrice;
        resultUint[1] = order.productQuantity;
        resultUint[2] = order.productTotalPrice;
        resultUint[3] = order.orderGenerateTime;
        if(txSerialNoList[orderNo].length >= 2){
            resultUint[4] = txRecordDetailMap[txSerialNoList[orderNo][1]].time;
        }
        else{
            resultUint[4] = 0;
        }
        //以下为仓储详情
        resultBytes32[5] = order.payerRepoBusinessNo;//买家仓储流水号
        resultBytes32[6] = order.payeeRepoBusinessNo;//卖家仓储流水号
        resultBytes32[7] = order.payerRepoCertNo;    //买家仓单编号
        resultBytes32[8] = order.payeeRepoCertNo;    //卖家仓单编号

        resultUint[5] = order.orderState.payerRepoBusiState;   //买家仓储最新状态
        resultUint[6] = order.orderState.payeeRepoBusiState;   //卖家仓储最新状态

        //物流信息bytes32
        resultBytes32[9] = param1[4];//运单号
        resultUint[7] = param2[5];//下单时间
        //resultUint[8] = param2[6];//当前状态
        resultUint[8] = order.orderState.wayBillState;//当前状态 修改by zgj.sol
        resultUint[9] = param2[7];//更新时间
        resultAddress[4] = param3[0];//物流公司地址

        //应收账款信息uint
        resultUint[10] = param2[0];
        resultUint[11] = param2[1];
        resultUint[12] = param2[2];
        resultUint[13] = param2[3];
        resultUint[14] = param2[4];
        if(txSerialNoList[orderNo].length == 3){
            resultUint[15] = txRecordDetailMap[txSerialNoList[orderNo][2]].time;
        }
        else{
            resultUint[15] = 0;
        }


        resultBytes32[10] = param1[0];
        resultBytes32[11] = param1[1];
        resultBytes32[12] = param1[2];
        resultBytes32[13] = param1[3];
        return (0, resultAddress, resultBytes32, resultUint, order.payingMethod, order.orderState.txState);
    }

    function queryAllOrderOverViewInfoList(address acctContractAddress,uint role)returns(
    uint,
    bytes32[] partList1,        //4值 orderNo, productName，payerBank，payerBankAccount
    address[] partList2,        //4值 payerAddress, payeeAddress, payerRepoAddress, payerRepoAddress
    uint[] partList3,           //5值 productQuantity,productUnitPrice,productTotalPrice,orderGenerateTime,orderConfirmTime, 某time(没看懂)
    PayingMethod[] methodList,  //付款方式
    uint[] stateList){          //仓储状态
        //如果用户不存在，返回"账户不存在，该用户可能未注册或已失效"
        accountContract = AccountContract(acctContractAddress);
        if(!accountContract.isAccountExist(msg.sender)){
            return (2, partList1, partList2, partList3, methodList, stateList);
        }

        //如果用户不是融资企业，返回"权限拒绝"
        if(accountContract.queryRoleCode(msg.sender) != 0){
            return (1, partList1, partList2, partList3, methodList, stateList);
        }


        bytes32[] memory orderList1;
        if(role == 0){
            orderList1 = allPayerOrderMap[msg.sender];
        }
        else if (role == 1){
            orderList1 = allPayeeOrderMap[msg.sender];
        }
        uint length = orderList1.length;
        partList1 = new bytes32[](length*4);
        partList2 = new address[](length*4);
        partList3 = new uint[](length*5);
        methodList = new PayingMethod[](length);
        stateList = new uint[](length*4);

        for(uint k = 0; k < orderList1.length; k++){
            partList1[k*4] = orderDetailMap[orderList1[k]].orderNo;
            partList1[k*4+1] = orderDetailMap[orderList1[k]].productName;
            partList1[k*4+2] = orderDetailMap[orderList1[k]].payerBank;
            partList1[k*4+3] = orderDetailMap[orderList1[k]].payerAccount;
            partList2[k*4] = orderDetailMap[orderList1[k]].payerAddress;
            partList2[k*4+1] = orderDetailMap[orderList1[k]].payeeAddress;
            partList2[k*4+2] = orderDetailMap[orderList1[k]].payerRepoAddress;
            partList2[k*4+3] = orderDetailMap[orderList1[k]].payeeRepoAddress;
            partList3[k*5] = orderDetailMap[orderList1[k]].productQuantity;
            partList3[k*5+1] = orderDetailMap[orderList1[k]].productUnitPrice;
            partList3[k*5+2] = orderDetailMap[orderList1[k]].productTotalPrice;
            partList3[k*5+3] = orderDetailMap[orderList1[k]].orderGenerateTime;
            if(txSerialNoList[orderList1[k]].length == 2){
                partList3[k*5+4] = txRecordDetailMap[txSerialNoList[orderList1[k]][1]].time;
            }
            else{
                partList3[k*5+4] = 0;
            }
            methodList[k] = orderDetailMap[orderList1[k]].payingMethod;
            stateList[k*4] = orderDetailMap[orderList1[k]].orderState.txState;
            if(role == 0){
                stateList[k*4+1] = orderDetailMap[orderList1[k]].orderState.payerRepoBusiState;
            }
            else{
                stateList[k*4+1] = orderDetailMap[orderList1[k]].orderState.payeeRepoBusiState;
            }
            stateList[k*4+2] = orderDetailMap[orderList1[k]].orderState.wayBillState;
            stateList[k*4+3] = orderDetailMap[orderList1[k]].orderState.receState;
        }
        return(0, partList1,partList2, partList3, methodList, stateList);
    }

    //买方查询相关订单编号列表
    function queryAllOrderListForPayer() returns (uint, bytes32[] resultList){
        /*if (!isValidUser()) {
         return (1, resultList);
         }*/
        return (0, allPayerOrderMap[msg.sender]);
    }

    //买方查询相关订单编号列表
    function queryAllOrderListForPayee() returns (uint, bytes32[] resultList){
        /*if (!isValidUser()) {
         return (1, resultList);
         }*/
        return (0, allPayeeOrderMap[msg.sender]);
    }

    /****************************卖方确认订单**************************************/

    function confirmOrder(address acctContractAddress, bytes32 orderNo, address payeeRepoAddress, bytes32 payeeRepoCertNo, bytes32 txSerialNo, uint orderConfirmTime,bytes32 payeeRepoBusinessNo) returns(
    uint){
        //如果用户不存在，返回"账户不存在，该用户可能未注册或已失效"
        accountContract = AccountContract(acctContractAddress);
        if(!accountContract.isAccountExist(msg.sender)){
            return 2;
        }

        //如果用户不是融资企业，返回"权限拒绝"
        if(accountContract.queryRoleCode(msg.sender) != 0){
            return 1;
        }
        Order order = orderDetailMap[orderNo];

        //如果订单不存在，返回"订单不存在"
        if(!orderExists(orderNo)){
            return 2001;
        }
        //若操作者不是订单的卖方，返回"用户不是订单的卖方"
        if (order.payeeAddress != msg.sender){
            return 2007;
        }
        //如果订单已经确认过，返回"该订单已经确认"
        if(order.orderState.txState == 2){
            return 2006;
        }

        //更新订单的状态为"已确认"，添加卖方指定的仓储公司和仓单编号
        updateOrderState(orderNo, "txState", CONFIRMED, txSerialNo, orderConfirmTime);//修改订单详情map中的订单状态
        orderDetailMap[orderNo].payeeRepoAddress = payeeRepoAddress;
        orderDetailMap[orderNo].payeeRepoCertNo = payeeRepoCertNo;
        orderDetailMap[orderNo].payeeRepoBusinessNo = payeeRepoBusinessNo;
        //orderDetailMap[orderNo].orderState.payeeRepoBusiState = 5;
        updateOrderState(orderNo, "payeeRepoBusiState", 5, txSerialNo, orderConfirmTime);
        //确认订单后，检查仓储状态，如果为"待入库",则修改应收账款状态为"待签发"
        if(orderDetailMap[orderNo].orderState.payerRepoBusiState == 2){
            updateOrderState(orderNo, "receState", 10, txSerialNo, orderConfirmTime);
        }

        //添加操作记录
        txRecordDetailMap[txSerialNo].orderNo = orderNo;
        txRecordDetailMap[txSerialNo].txSerialNo = txSerialNo;
        txRecordDetailMap[txSerialNo].time = orderConfirmTime;
        txRecordDetailMap[txSerialNo].txState = 2;


        //添加该订单对应的流水号
        txSerialNoList[orderNo].push(txSerialNo);
        return 0;
    }
    /*============================================================================
     其他合约接口方法
     ============================================================================*/

    /***********************根据订单编号查询货品名称*********************************/
    function queryProductNameByOrderNo(bytes32 orderNo)returns(bytes32){
        return orderDetailMap[orderNo].productName;
    }

    /***********************根据订单编号查询货品数量*********************************/
    function queryProductQuantityByOrderNo(bytes32 orderNo)returns(uint){
        return orderDetailMap[orderNo].productQuantity;
    }

    /***********************根据订单编号查询交易信息给应收账款合约*********************************/
    function queryOrderInfoForRece(bytes32 orderNo)returns (
    address[4],
    bytes32[4],
    uint[2]){
        //如果订单不存在，返回"订单不存在"
        if(!orderExists(orderNo)){
        return (resultAddress, resultBytes32, resultUint);
        }


        address[4] memory resultAddress;
        bytes32[4] memory resultBytes32;
        uint[2] memory resultUint;

        Order order = orderDetailMap[orderNo];
        resultAddress[0] = order.payeeAddress;
        resultAddress[1] = order.payerAddress;
        resultAddress[2] = order.payeeRepoAddress;
        resultAddress[3] = order.payerRepoAddress;
        resultBytes32[0] = order.orderNo;
        resultBytes32[1] = order.payeeRepoCertNo;
        resultBytes32[2] = order.payerRepoBusinessNo;
        resultBytes32[3] = order.productName;
        resultUint[0] = order.productQuantity;
        resultUint[1] = order.productTotalPrice;

        return(resultAddress, resultBytes32, resultUint);
    }

    /***********************根据订单编号更新订单的某状态******************************/
    function updateOrderState(bytes32 orderNo, bytes32 stateType, uint newState, bytes32 txSerialNo, uint orderCompleteTime)returns(uint){
        Order order = orderDetailMap[orderNo];
        if(stateType == "txState"){
            order.orderState.txState = newState;
            return 0;
        }
        if(stateType == "payerRepoBusiState"){
            order.orderState.payerRepoBusiState = newState;
            if(newState == INCOMED){
                if(order.orderState.wayBillState == RECEIVED){
                    order.orderState.txState = COMPLETED;
                    //添加操作记录
                    txRecordDetailMap[txSerialNo].orderNo = orderNo;
                    txRecordDetailMap[txSerialNo].txSerialNo = txSerialNo;
                    txRecordDetailMap[txSerialNo].time = orderCompleteTime;
                    txRecordDetailMap[txSerialNo].txState = COMPLETED;
                    //添加该订单对应的流水号
                    txSerialNoList[orderNo].push(txSerialNo);
                }
            }
            return 0;
        }
        if(stateType == "payeeRepoBusiState"){
            order.orderState.payeeRepoBusiState = newState;
            return 0;
        }

        if(stateType == "wayBillState"){
            order.orderState.wayBillState = newState;
            if(newState == RECEIVED){
                if(order.orderState.payerRepoBusiState == INCOMED){
                    order.orderState.txState = COMPLETED;
                    //添加操作记录
                    txRecordDetailMap[txSerialNo].orderNo = orderNo;
                    txRecordDetailMap[txSerialNo].txSerialNo = txSerialNo;
                    txRecordDetailMap[txSerialNo].time = orderCompleteTime;
                    txRecordDetailMap[txSerialNo].txState = COMPLETED;
                    //添加该订单对应的流水号
                    txSerialNoList[orderNo].push(txSerialNo);
                }
            }
            return 0;
        }
        if(stateType == "receState"){
            order.orderState.receState = newState;
            return 0;
        }
    }
}

contract WayBillContract {
    //运单状态
    uint WAYBILL_UNDEFINED = 0; //未定义
    uint WAYBILL_WAITING = 1; //等待发货
    uint WAYBILL_REQUESTING = 2; //发货待响应
    uint WAYBILL_REJECTED = 3; //发货被拒绝
    uint WAYBILL_SENDING = 4; //已发货
    uint WAYBILL_RECEIVED = 5; //已送达

    //仓储状态
    uint REPO_WATING_INCOME_RESPONSE = 1;    //入库待响应
    uint REPO_WATING_INCOME = 2;             //待入库
    uint REPO_INCOMED = 3;                   //已入库
    uint REPO_WATING_OUTCOME_RESPONSE = 4;   //出库待响应
    uint REPO_WATING_OUTCOME = 5;            //待出库
    uint REPO_OUTCOMED = 6;                  //已出库

    //用户角色
    uint ROLE_COMPANY = 0; //融资企业
    uint ROLE_LOGISTICS = 1; //物流公司
    uint ROLE_REPOSITORY = 2; //仓储公司
    uint ROLE_FINANCIAL = 3; //金融机构

    //合约
    AccountContract accountContract;
    WayBillContract wayBillContract;
    ReceivableContract receivableContract;
    OrderContract orderContract;
    RepositoryContract repositoryContract;

    //业务状态
    uint CODE_SUCCESS = 0; //成功
    uint CODE_PERMISSION_DENIED = 1; //用户无权限
    uint CODE_INVALID_USER = 2; //用户不存在
    uint CODE_WAY_BILL_ALREADY_EXIST = 3000; //运单已经存在
    uint CODE_WAY_BILL_NO_DATA = 3001; //该用户暂无数据
    uint CODE_STATUS_TRANSFER_DENIED = 3002; //状态转换拒绝

    //运单信息
    struct WayBill{
        bytes32 orderNo;//订单编号
        bytes32 statusTransId;//状态流转编号列表(订单号_运单状态)
        bytes32 wayBillNo;//运单号
        address logisticsAddress;//物流公司
        address senderAddress;//发货人
        address receiverAddress; //收货人
        bytes32 productName; //货品名称
        uint productQuantity; //货品数量
        uint productValue; //货品价值（分）
        uint operateTime; //操作时间（毫秒）（待发货、发货待响应、发货被拒绝、已发货、已送达）
        address senderRepoAddress; //发货所在仓储公司
        bytes32 senderRepoCertNo;//发货货品仓单编号
        address receiverRepoAddress; //收货仓储公司
        bytes32 receiverRepoBusinessNo; //收货货品仓储业务编号
        bytes32[] logisticsInfo; //物流信息
        uint waybillStatus; //运单状态（待发货、发货待响应、发货被拒绝、已发货、已送达）
    }

    //（发货企业、收货企业、物流公司）address => 物流订单编号列表
    mapping(address=> bytes32[]) addressToOrderNoList;
    //订单编号 => 状态流转编号列表
    mapping(bytes32 =>bytes32[]) orderNoToStatusTransIdList;
    //状态流转编号—> 运单详情(完整/不完整)
    mapping(bytes32 => WayBill) statusTransIdToWayBillDetail;

    //内部调用：供应收账款模块调用（当应收款状态为承兑已签收时调用）
    //生成待发货运单（初始化 状态为待发货、待发货时间）
    function initWayBillStatus(
        address[] addrs, /*senderAddress, receiverAddress, senderRepoAddress, receiverRepoAddress, orderAddress*/
        bytes32[] strs, /*orderNo, senderRepoCertNo, receiverRepoBusinessNo, productName*/
        uint[] integers /*waitTime, productQuantity, productValue*/
    ) returns (uint code)
    {
        orderContract = OrderContract (addrs[4]);

        //拼接statusTransId
        string memory s1 = bytes32ToString(strs[0]);
        string memory s2 = bytes32ToString(bytes32(WAYBILL_WAITING));
        string memory conStr = concatString(s1, s2);
        bytes32 statusTransId = stringToBytes32(conStr);
        //其他参数
        address logisticsAddress;
        bytes32[] memory logisticsInfo;

        //生成初始化运单
        statusTransIdToWayBillDetail[statusTransId] = WayBill(strs[0], statusTransId, "", logisticsAddress, addrs[0], addrs[1], strs[3], integers[1], integers[2], integers[0], addrs[2], strs[1], addrs[3], strs[2], logisticsInfo, WAYBILL_WAITING);
        //
        orderNoToStatusTransIdList[strs[0]].push(statusTransId);
        //
        addressToOrderNoList[addrs[0]].push(strs[0]);
        addressToOrderNoList[addrs[1]].push(strs[0]);

        //TODO 更新订单中的运单状态为待发货
        orderContract.updateOrderState(strs[0], "wayBillState", WAYBILL_WAITING, "", 0);

        return CODE_SUCCESS;
    }

    //生成待确认运单
    function generateUnConfirmedWayBill(
        uint[] integers,  /*requestTime, productValue, productQuantity, */
        address[] addrs,  /*logisticsAddress, senderAddress, receiverAddress, receiverRepoAddress, senderRepoAddress*/
        bytes32[] strs,  /*orderNo, productName, senderRepoCertNo, receiverRepoBusinessNo, statusTransId, waybillNo*/
        address accountContractAddr,
        address repositoryContractAddr)
        returns (uint code)
    {
        accountContract = AccountContract (accountContractAddr);
        repositoryContract = RepositoryContract (repositoryContractAddr);
        // uint requestTime = integers[0];
        // uint productValue = integers[1];
        // uint productQuantity = integers[2];
        // address logisticsAddress = addrs[0];
        // address senderAddress = addrs[1];
        // address receiverAddress = addrs[2];
        // address receiverRepoAddress = addrs[3];
        // address senderRepoAddress = addrs[4];
        // bytes32 orderNo = strs[0];
        // bytes32 productName = strs[1];
        // bytes32 senderRepoCertNo = strs[2];
        // bytes32 receiverRepoBusinessNo = strs[3];
        // bytes32 statusTransId = strs[4];
        // bytes32 waybillNo = strs[5];
        bytes32[] memory logisticsInfo;

        //权限控制
        if(accountContract.isAccountExist(msg.sender) == false){ //用户不存在
            return CODE_INVALID_USER;
        }
        if(accountContract.checkRoleCode(msg.sender, ROLE_COMPANY) == false || msg.sender != addrs[1]){ //用户无权限
            return CODE_PERMISSION_DENIED;
        }
        if(statusTransIdToWayBillDetail[strs[4]].productName != ""){ //运单已经存在
            return CODE_WAY_BILL_ALREADY_EXIST;
        }

        //生成未确认运单
        statusTransIdToWayBillDetail[strs[4]] = WayBill(strs[0], strs[4], strs[5], addrs[0], addrs[1], addrs[2], strs[1], integers[2], integers[1], integers[0], addrs[4], strs[2], addrs[3], strs[3], logisticsInfo, WAYBILL_REQUESTING);
        //
        orderNoToStatusTransIdList[strs[0]].push(strs[4]);
        //
        addressToOrderNoList[addrs[0]].push(strs[0]);

        //TODO 更新仓储结构体中的物流信息
        /*repositoryContract.updateLogisInfo(
         strs[2],//仓单编号
         addrs[0],//物流公司address
         strs[5] //运单号
         );*/

        var( tmpcode, payerRepoBusinessNo, payeeRepoBusinessNo) = orderContract.qryRepoBusiNos(strs[0]);
        repositoryContract.updateLogisInfo2(
            payerRepoBusinessNo,//买家仓储业务编号
            payeeRepoBusinessNo,//卖家仓储业务编号
            addrs[0],//物流公司address
            strs[5] //运单号
        );
        //TODO 更新订单中的运单状态为发货待响应
        orderContract.updateOrderState(strs[0], "wayBillState", WAYBILL_REQUESTING, "", 0);

        return CODE_SUCCESS; //成功
    }

//生成已确认运单
    function generateWayBill(bytes32 orderNo, bytes32 statusTransId, uint sendTime, address accountContractAddr) returns (uint code){
        accountContract = AccountContract (accountContractAddr);

        bytes32[] memory statusTransIdList = orderNoToStatusTransIdList[orderNo];
        WayBill memory oldWaybill = statusTransIdToWayBillDetail[statusTransIdList[statusTransIdList.length - 1]];

        //权限控制
        if(accountContract.isAccountExist(msg.sender) == false){ //用户不存在
            return CODE_INVALID_USER;
        }
        if(accountContract.checkRoleCode(msg.sender, ROLE_LOGISTICS) == false || msg.sender != oldWaybill.logisticsAddress){ //用户无权限
            return CODE_PERMISSION_DENIED;
        }
        if(oldWaybill.waybillStatus != WAYBILL_REQUESTING){ //无状态转换权限
            return CODE_STATUS_TRANSFER_DENIED;
        }

        //生成已确认运单
        statusTransIdToWayBillDetail[statusTransId] = WayBill(orderNo, statusTransId, oldWaybill.wayBillNo, oldWaybill.logisticsAddress, oldWaybill.senderAddress, oldWaybill.receiverAddress, oldWaybill.productName, oldWaybill.productQuantity, oldWaybill.productValue, sendTime, oldWaybill.senderRepoAddress, oldWaybill.senderRepoCertNo, oldWaybill.receiverRepoAddress, oldWaybill.receiverRepoBusinessNo, oldWaybill.logisticsInfo, WAYBILL_SENDING);
        //
        orderNoToStatusTransIdList[orderNo].push(statusTransId);
        //

        //TODO 更新订单中的运单状态为已发货
        orderContract.updateOrderState(orderNo, "wayBillState", WAYBILL_SENDING, "", 0);

        return CODE_SUCCESS;
    }

    //获取所有用户相关运单的订单号列表
    function listWayBillOrderNo(address accountContractAddr) returns (uint code, bytes32[] orderNoList){
        accountContract = AccountContract (accountContractAddr);
        //权限控制
        if(accountContract.isAccountExist(msg.sender) == false){ //用户不存在
            return (CODE_INVALID_USER, orderNoList);
        }
        if(accountContract.checkRoleCode(msg.sender, ROLE_LOGISTICS) == false && accountContract.checkRoleCode(msg.sender, ROLE_COMPANY) == false){ //用户无权限
            return (CODE_PERMISSION_DENIED, orderNoList);
        }

        return (CODE_SUCCESS,addressToOrderNoList[msg.sender]);
    }

    //根据订单号获取运单详情
    function getWayBill(bytes32 orderNo, address accountContractAddr) returns (uint code, uint[] ints, bytes32[] strs, address[] addrs, bytes32[] logisticsInfo) {
        accountContract = AccountContract (accountContractAddr);
        //权限控制
        if(accountContract.isAccountExist(msg.sender) == false){ //用户不存在
            return (CODE_INVALID_USER,ints, strs, addrs, logisticsInfo);
        }
        //if(accountContract.checkRoleCode(msg.sender, ROLE_LOGISTICS) == false && accountContract.checkRoleCode(msg.sender, ROLE_COMPANY) == false){ //用户无权限
        //    return (CODE_PERMISSION_DENIED,ints, strs, addrs, logisticsInfo);
        //}

        //获取运单最新信息
        bytes32[] memory statusTransIdList = orderNoToStatusTransIdList[orderNo];
        if (statusTransIdList.length == 0) {
            return (CODE_WAY_BILL_NO_DATA,ints, strs, addrs, logisticsInfo);
        }
        WayBill memory waybill = statusTransIdToWayBillDetail[statusTransIdList[statusTransIdList.length - 1]]; //取最新状态的运单信息

        var(waitTime, requestTime, sendTime, receiveTime, rejectTime) = getTime(waybill.waybillStatus, statusTransIdList);

        ints = new uint[](8);
        strs = new bytes32[](5);
        addrs = new address[](5);

        ints[0] = waybill.productQuantity;
        ints[1] = waybill.productValue;
        ints[2] = requestTime;
        ints[3] = receiveTime;
        ints[4] = sendTime;
        ints[5] = rejectTime;
        ints[6] = waitTime;
        ints[7] = waybill.waybillStatus;

        strs[0] = orderNo;
        strs[1] = waybill.wayBillNo;
        strs[2] = waybill.productName;
        strs[3] = waybill.senderRepoCertNo;
        strs[4] = waybill.receiverRepoBusinessNo;

        addrs[0] = waybill.logisticsAddress;
        addrs[1] = waybill.senderAddress;
        addrs[2] = waybill.receiverAddress;
        addrs[3] = waybill.senderRepoAddress;
        addrs[4] = waybill.receiverRepoAddress;

        return (CODE_SUCCESS, ints, strs, addrs, waybill.logisticsInfo);

    }


    function getTime(uint status, bytes32[] statusTransIdList) returns(uint waitTime, uint requestTime, uint sendTime, uint receiveTime, uint rejectTime){
        if (status == WAYBILL_WAITING) {
            waitTime = statusTransIdToWayBillDetail[statusTransIdList[0]].operateTime;
        }else if(status == WAYBILL_REQUESTING){
            waitTime = statusTransIdToWayBillDetail[statusTransIdList[0]].operateTime;
            requestTime = statusTransIdToWayBillDetail[statusTransIdList[1]].operateTime;
        }else if(status== WAYBILL_SENDING){
            waitTime = statusTransIdToWayBillDetail[statusTransIdList[0]].operateTime;
            requestTime = statusTransIdToWayBillDetail[statusTransIdList[1]].operateTime;
            sendTime = statusTransIdToWayBillDetail[statusTransIdList[2]].operateTime;
        }else if(status == WAYBILL_RECEIVED){
            waitTime = statusTransIdToWayBillDetail[statusTransIdList[0]].operateTime;
            requestTime = statusTransIdToWayBillDetail[statusTransIdList[1]].operateTime;
            sendTime = statusTransIdToWayBillDetail[statusTransIdList[2]].operateTime;
            receiveTime = statusTransIdToWayBillDetail[statusTransIdList[3]].operateTime;
        }else{ //waybill.waybillStatus == WAYBILL_REJECTED
            waitTime = statusTransIdToWayBillDetail[statusTransIdList[0]].operateTime;
            requestTime = statusTransIdToWayBillDetail[statusTransIdList[1]].operateTime;
            rejectTime = statusTransIdToWayBillDetail[statusTransIdList[2]].operateTime;
        }
    }


    //获取运单概要信息。供其他合约调用
    function getWayBillOverview(bytes32 orderNo, address accountContractAddr)
        returns(bytes32 wayBillNo, uint requestTime, address logisticsAddress, uint waybillStatus, uint operateTime)
    {
        /*运单号, 下单时间, 物流公司, 物流当前状态, 更新时间*/
        accountContract = AccountContract (accountContractAddr);

        //获取运单最新信息
        bytes32[] memory statusTransIdList = orderNoToStatusTransIdList[orderNo];
        if (statusTransIdList.length == 0) {
            return (wayBillNo,requestTime, logisticsAddress, waybillStatus, operateTime);
        }
        WayBill memory waybill = statusTransIdToWayBillDetail[statusTransIdList[statusTransIdList.length - 1]]; //取最新状态的运单信息
        requestTime = statusTransIdToWayBillDetail[statusTransIdList[0]].operateTime;
        return (waybill.wayBillNo, requestTime, waybill.logisticsAddress, waybill.waybillStatus, waybill.operateTime);
    }


    //更新运单状态为已送达
    function updateWayBillStatusToReceived(bytes32 orderNo, bytes32 statusTransId, uint operateTime, address accountContractAddr, address repoContractAddr, address orderContractAddr, bytes32 txSerialNo) returns (uint code){
        accountContract = AccountContract (accountContractAddr);
        repositoryContract = RepositoryContract (repoContractAddr);
        orderContract = OrderContract (orderContractAddr);

        //权限控制
        if(accountContract.isAccountExist(msg.sender) == false){ //用户不存在
            return CODE_INVALID_USER;
        }
        if(accountContract.checkRoleCode(msg.sender, ROLE_LOGISTICS) == false){ //用户无权限
            return CODE_PERMISSION_DENIED;
        }
        //TODO 权限控制：卖家仓储状态为已出库 买家仓储状态为待入库 状态 物流才能更新为已送达
        if (orderContract.queryPayeeRepoBusiStateOfOrderState(orderNo) != REPO_OUTCOMED || orderContract.queryPayerRepoBusiStateOfOrderState(orderNo) != REPO_WATING_INCOME){ //无状态流转权限
            return CODE_STATUS_TRANSFER_DENIED;
        }

        //获取运单最新信息
        bytes32[] memory statusTransIdList = orderNoToStatusTransIdList[orderNo];
        if (statusTransIdList.length == 0) {
            return CODE_WAY_BILL_NO_DATA;
        }
        WayBill oldWaybill = statusTransIdToWayBillDetail[statusTransIdList[statusTransIdList.length - 1]]; //取最新状态的运单信息

        if(oldWaybill.waybillStatus != WAYBILL_SENDING){//无状态流转权限
            return CODE_STATUS_TRANSFER_DENIED;
        }

        statusTransIdToWayBillDetail[statusTransId] = WayBill(orderNo, statusTransId, oldWaybill.wayBillNo, oldWaybill.logisticsAddress, oldWaybill.senderAddress, oldWaybill.receiverAddress, oldWaybill.productName, oldWaybill.productQuantity, oldWaybill.productValue, operateTime, oldWaybill.senderRepoAddress, oldWaybill.senderRepoCertNo, oldWaybill.receiverRepoAddress, oldWaybill.receiverRepoBusinessNo, oldWaybill.logisticsInfo, WAYBILL_RECEIVED);
        //
        orderNoToStatusTransIdList[orderNo].push(statusTransId);
        //
        //TODO 更新订单中的运单状态为已完成
        orderContract.updateOrderState(orderNo, "wayBillState", WAYBILL_RECEIVED, txSerialNo, operateTime);

        return (CODE_SUCCESS);
    }

    //更新运单状态为申请发货被拒绝
    function updateWayBillStatusToRejected(bytes32 orderNo, bytes32 statusTransId, uint operateTime, address accountContractAddr) returns (uint code){
        accountContract = AccountContract (accountContractAddr);

        //权限控制
        if(accountContract.isAccountExist(msg.sender) == false){ //用户不存在
            return CODE_INVALID_USER;
        }
        if(accountContract.checkRoleCode(msg.sender, ROLE_LOGISTICS) == false){ //用户无权限
            return CODE_PERMISSION_DENIED;
        }

        //获取运单最新信息
        bytes32[] memory statusTransIdList = orderNoToStatusTransIdList[orderNo];
        WayBill memory oldWaybill = statusTransIdToWayBillDetail[statusTransIdList[statusTransIdList.length - 1]]; //取最新状态的运单信息

        if(oldWaybill.waybillStatus != WAYBILL_REQUESTING){//无状态流转权限
            return CODE_STATUS_TRANSFER_DENIED;
        }

        statusTransIdToWayBillDetail[statusTransId] = WayBill(orderNo, statusTransId, oldWaybill.wayBillNo, oldWaybill.logisticsAddress, oldWaybill.senderAddress, oldWaybill.receiverAddress, oldWaybill.productName, oldWaybill.productQuantity, oldWaybill.productValue, operateTime, oldWaybill.senderRepoAddress, oldWaybill.senderRepoCertNo, oldWaybill.receiverRepoAddress, oldWaybill.receiverRepoBusinessNo, oldWaybill.logisticsInfo, WAYBILL_REJECTED);
        //
        orderNoToStatusTransIdList[orderNo].push(statusTransId);
        //

        //TODO 更新订单中的运单状态为已拒绝
        orderContract.updateOrderState(orderNo, "wayBillState", WAYBILL_REJECTED, "", 0);

        return (CODE_SUCCESS);
    }

    //根据订单号查询运单号和物流企业address，供其他合约调用
    function getWaybillMsgForReceviable(bytes32 orderNo, address accountContractAddr) returns (bytes32 waybillNo, bytes32 logisticsEnterpriseName){
        accountContract = AccountContract (accountContractAddr);

        //获取运单最新信息
        bytes32[] memory statusTransIdList = orderNoToStatusTransIdList[orderNo];
        if (statusTransIdList.length == 0) {
            return (waybillNo, logisticsEnterpriseName);
        }
        WayBill memory waybill = statusTransIdToWayBillDetail[statusTransIdList[statusTransIdList.length - 1]]; //取最新状态的运单信息
        logisticsEnterpriseName = accountContract.getEnterpriseNameByAddress(waybill.logisticsAddress);
        return (waybill.wayBillNo, logisticsEnterpriseName);
    }

//备注：用户权限控制：用户是否存在，用户身份操作权限，业务状态流转权限

///////////////////////开始字符串拼接///////////////////////
    struct slice {
        uint _len;
        uint _ptr;
    }

    function toSlice(string self) internal returns (slice) {
        uint ptr;
        assembly {
            ptr := add(self, 0x20)
        }
        return slice(bytes(self).length, ptr);
    }

    function memcpy(uint dest, uint src, uint len) private {
        for(; len >= 32; len -= 32) {
            assembly {
                mstore(dest, mload(src))
            }
            dest += 32;
            src += 32;
        }

        uint mask = 256 ** (32 - len) - 1;
        assembly {
            let srcpart := and(mload(src), not(mask))
            let destpart := and(mload(dest), mask)
            mstore(dest, or(destpart, srcpart))
        }
    }

    function concat(slice self, slice other) internal returns (string) {
        var ret = new string(self._len + other._len);
        uint retptr;
        assembly { retptr := add(ret, 32) }
        memcpy(retptr, self._ptr, self._len);
        memcpy(retptr + self._len, other._ptr, other._len);
        return ret;
    }
    //（1）两个字符串拼接
    function concatString(string _a,
        string _b) internal returns (string) {
        return (concat(toSlice(_a), toSlice(_b)));
    }


    function stringToBytes32(string memory source)returns (bytes32 result) {
        assembly {
            result := mload(add(source, 32))
        }
    }

    function bytes32ToString(bytes32 x)returns (string) {
        bytes memory bytesString = new bytes(32);
        uint charCount = 0;
        for (uint j = 0; j < 32; j++) {
            byte char = byte(bytes32(uint(x) * 2 ** (8 * j)));
            if (char != 0) {
                bytesString[charCount] = char;
                charCount++;
            }
        }
    }
//////////////////////结束字符串拼接///////////////////
}
