
contract receivableContract{
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
        bytes32 status;//应收款状态
        bytes32 lastStatus;//上一状态
        uint isseDt; //签发日
        uint signInDt;//签收日
        uint dueDt; //到期日
        bytes32 rate;//利率
        bytes32 contractNo;//合同号
        bytes32 invoiceNo;//发票号
        DiscountedStatus discounted;//是否贴现标志
        uint discountInHandAmount;//贴现回复时的到手金额
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
        bytes32 receivableStatus;//应收款状态
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
    enum DiscountedStatus {NO, YES, NULL} //贴现标志位

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
    function newReceivableRecord(bytes32 serialNo, bytes32 receivableNo, bytes32 applicantAcctId, bytes32 replyerAcctId, ResponseType response, uint time, bytes32 operateType, uint dealAmount, bytes32 receivableStatus) internal {
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

    //签发申请。签发人是卖家（收款人），承兑人是买家（付款人）
    function signOutApply(bytes32 receivableNo, bytes32 orderNo, bytes32 signer, bytes32 accptr, bytes32 pyee, bytes32 pyer, uint isseAmt, uint dueDt, bytes32 rate, bytes32[] contractAndInvoiceNo, bytes32 serialNo, uint time) returns(uint code){
        if(receivableNo == "" || orderNo == "" || signer == "" || accptr == "" || pyer == "" || pyee == "" || rate == "" || serialNo == ""){
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
        if(judgeRepetitiveReceivableNo(receivableNo)){//判断该应收款编号是否已经存在
            return (1030);
        }
*/
        allReceivableNos.push(receivableNo);
        giveReceivableInfo(receivableNo, serialNo, orderNo, signer, accptr, pyer, pyee, isseAmt, dueDt, rate, contractAndInvoiceNo, time);

        //newReceivableRecord(serialNo, receivableNo, signer, accptr, ResponseType.NULL, time, "signOutApply", isseAmt);

        accountReceivableRecords[signer].push(serialNo);
        holdingReceivablesMap[signer].push(receivableNo);
        orderNoToReceivableNoMap[orderNo] = receivableNo;

        receivableTransferHistoryMap[receivableNo].push(serialNo);

        return (0);
    }

    function giveReceivableInfo(bytes32 receivableNo, bytes32 serialNo, bytes32 orderNo, bytes32 signer, bytes32 accptr, bytes32 pyer, bytes32 pyee, uint isseAmt, uint dueDt, bytes32 rate, bytes32[] contractAndInvoiceNo, uint time) internal {
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
          receivable.status = "020001";
          receivable.contractNo = contractAndInvoiceNo[0];
          receivable.invoiceNo = contractAndInvoiceNo[1];
          newReceivableRecord(serialNo, receivableNo, signer, accptr, ResponseType.NULL, time, "signOutApply", isseAmt, receivable.status);
    }

    //签发回复
    function signOutReply(bytes32 receivableNo, bytes32 replyerAcctId, ResponseType response, bytes32 serialNo, uint time) returns (bytes32){
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
        if(receivable.status != "020001"){
            return (1006);
        }
/*        if(replyerAcctId != receivable.accptr){
            return (1);
        }*/
        receivable.lastStatus = receivable.status;
        if(response == ResponseType.NO){
            receivable.status = "000003";
        }else{
            receivable.status = "020006";
        }
        receivable.signInDt = time;

        pyerToReceivableMap[receivable.pyer].push(receivableNo);
        pyeeToReceivableMap[receivable.pyee].push(receivableNo);
        accptrToReceivableMap[receivable.accptr].push(receivableNo);
        signerToReceivableMap[receivable.signer].push(receivableNo);
        accountReceivableRecords[replyerAcctId].push(serialNo);
        newReceivableRecord(serialNo, receivableNo, receivable.signer, replyerAcctId, response, time, "signOutReply", receivable.isseAmt, receivable.status);
        receivableTransferHistoryMap[receivableNo].push(serialNo);
        return (0);
    }

    //贴现申请
    function discountApply(bytes32 receivableNo, bytes32 applicantAcctId, bytes32 replyerAcctId, bytes32 serialNo, uint time, uint discountApplyAmount) returns(uint) {
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
        receivable.lastStatus = receivable.status;
        receivable.status = "070001";
        receivable.secondOwner = replyerAcctId;
        newReceivableRecord(serialNo, receivableNo, applicantAcctId, replyerAcctId, ResponseType.NULL, time, "discountApply", discountApplyAmount, receivable.status);
        accountReceivableRecords[applicantAcctId].push(serialNo);
        receivableTransferHistoryMap[receivableNo].push(serialNo);
        return(0);
    }

    //贴现回复
    function discountResponse(bytes32 receivableNo, bytes32 replyerAcctId, ResponseType responseType, bytes32 serialNo, uint time, bytes32 newReceivableNo, uint discountInHandAmount, bytes32 discountApplySerialNo) returns(uint) {
         if(receivableNo == "" || replyerAcctId == "" || serialNo == ""){
            return (3);
         }
        if(responseType != ResponseType.NO && responseType != ResponseType.YES){
            return (1020);
        }
         if(judgeRepetitiveSerialNo(serialNo)){
            return (1032);
         }
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
        receivable.discountInHandAmount = discountInHandAmount;
        subDiscount(receivableNo, serialNo, responseType, time, newReceivableNo, discountApplySerialNo);
        holdingReceivablesMap[replyerAcctId].push(newReceivableNo);
        accountReceivableRecords[replyerAcctId].push(serialNo);
        newReceivableRecord(serialNo, receivableNo, receivable.firstOwner, replyerAcctId, responseType, time, "discountResponse", discountInHandAmount, receivable.status);
        receivableTransferHistoryMap[receivableNo].push(serialNo);
        return (0);
    }

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
                receivable.status = "070009";//已全额贴现
                receivable.discounted = DiscountedStatus.YES;
                receivable.signInDt = time;
                newReceivable.lastStatus = "070001";
                newReceivable.status = "070006";//贴现已签收
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
                newReceivable.status = "070006";//贴现已签收
                newReceivable.isseAmt = discountApplyAmount;
                newReceivable.firstOwner = receivable.secondOwner;
                newReceivable.secondOwner = "";
                newReceivable.discounted = DiscountedStatus.YES;
                newReceivable.signInDt = time;

                receivable.lastStatus = receivable.status;
                receivable.status = "070008";//已部分贴现
                receivable.isseAmt = oriAmount - discountApplyAmount;
                receivable.signInDt = time;
                receivable.firstOwner = receivable.firstOwner;
                //receivable.secondOwner = "";
            }

        }
    }

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
	function cash(bytes32 receivableNo, uint cashedAmount, uint time,bytes32 serialNo)returns(uint){
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
        if(receivable.status != "020006" && receivable.status != "070006"){
            return(1006);
        }
        //到期日才能兑付
        if(time < receivable.dueDt){
            return(1010);
        }

        receivable.lastStatus = receivable.status;
        receivable.cashedAmount = cashedAmount;
        receivable.status = "000001";
        cashedReceivablesMap[receivable.accptr].push(receivableNo);
        newReceivableRecord(serialNo, receivableNo, receivable.signer, receivable.accptr, ResponseType.YES, time, "Cash", cashedAmount, receivable.status);
        return (0);
	}

    //根据应收款编号查询单张应收款具体信息
    //根据应收款编号查询单张应收款具体信息
    function getReceivableAllInfo(bytes32 receivableNo, bytes32 acctId) returns (uint, bytes32[], bytes32[], uint[], DiscountedStatus discounted, bytes note){
         Account account = accountMap[msg.sender];
         Receivable receivable = receivableDetailMap[receivableNo];

         uint[] memory uintInfo = new uint[](6);
         bytes32[] memory bytesInfo1 = new bytes32[](13);
         bytes32[] memory bytesInfo2 = new bytes32[](4);
/*
         if(judgeAccount(msg.sender)){
            return (2,
                 bytesInfo1,
                 bytesInfo2,
                 uintInfo,
                 discounted,
                 note
                 );
         }
*/
         if(receivableNo == ""){
            return (3,
                 bytesInfo1,
                 bytesInfo2,
                 uintInfo,
                 discounted,
                 note
                 );
         }

         if(receivable.receivableNo == 0x0) {
             return(1005,
                 bytesInfo1,
                 bytesInfo2,
                 uintInfo,
                 discounted,
                 note
                 );
         }

/*
         if(receivable.signer != acctId && receivable.accptr != acctId && receivable.pyer != acctId && receivable.pyee != acctId) {
             return(1,
                 bytesInfo1,
                 bytesInfo2,
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

        bytesInfo1[0] = receivableNo;
        bytesInfo1[1] = receivable.orderNo;
        bytesInfo1[2] = receivable.signer;
        bytesInfo1[3] = receivable.accptr;
        bytesInfo1[4] = receivable.pyer;
        bytesInfo1[5] = receivable.pyee;
        bytesInfo1[6] = receivable.firstOwner;
        bytesInfo1[7] = receivable.secondOwner;
        bytesInfo1[8] = receivable.status;
        bytesInfo1[9] = receivable.lastStatus;
        bytesInfo1[10] = receivable.rate;
        bytesInfo1[11] = receivable.contractNo;
        bytesInfo1[12] = receivable.invoiceNo;


         return (0,
                 bytesInfo1,
                 acctSvcrNameAndEnterpriseName(receivableNo),
                 uintInfo,
                 discounted,
                 note
                 );
     }

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

    //查找应收款的交易历史，返回流水号
    function getReceivableHistorySerialNo(bytes32 receivableNo) returns (uint, bytes32[]){
        return (0, receivableTransferHistoryMap[receivableNo]);
    }

    //流水号查询，自己查自己
    function getRecordBySerialNo(bytes32 serialNm) returns(uint, bytes32 serialNo, bytes32 receivableNo, bytes32 applicantAcctId, bytes32 replyerAcctId, ResponseType, uint, bytes32 operateType, uint, bytes32 receivableStatus){
        Account account = accountMap[msg.sender];
        ReceivableRecord receivableRecord = receivableRecordMap[serialNm];
        if(serialNo == ""){
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