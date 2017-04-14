contract
AccountContract
{
    uint
    ROLE_COMPANY = 0;
    uint
    ROLE_LOGISTICS = 1;
    uint
    ROLE_REPOSITORY = 2;
    uint
    ROLE_FINANCIAL = 3;
    uint
    STATUS_VALID = 0;
    uint
    STATUS_INVALID = 1;
    uint
    STATUS_FROZEN = 2;
    struct
    Account
    {
        address
        accountAddress;
        bytes32
        accountName;
        bytes32
        enterpriseName;
        uint
        roleCode;
        uint
        accountStatus;
        bytes32
        certType;
        bytes32
        certNo;
        bytes32[]
        acctId;
        bytes32
        svcrClass;
        bytes32
        acctSvcr;
        bytes32
        acctSvcrName;
    }
    mapping(address = > Account
)
    accountMap;
    mapping(bytes32 = > address
)
    acctIdToAddress;
    function newAccount(bytes32

    _accountName, bytes32
    _enterpriseName, uint
    _roleCode, uint
    _accountStatus, bytes32
    _certType, bytes32
    _certNo, bytes32[]
    _acctId, bytes32
    _svcrClass, bytes32
    _acctSvcr, bytes32
    _acctSvcrName
)
    returns(uint
    code
)
    {
        if (accountMap[msg.sender].accountName != "") {
            return 5002;
        }
        accountMap[msg.sender] = Account(msg.sender, _accountName, _enterpriseName, _roleCode, _accountStatus, _certType, _certNo, _acctId, _svcrClass, _acctSvcr, _acctSvcrName);
        for (uint i = 0;
        i < _acctId.length;
        i++
    )
        {
            acctIdToAddress[_acctId[i]] = msg.sender;
        }
        return 0;
    }
    function getAccount()

    returns(uint
    code, bytes32
    _accountName, bytes32
    _enterpriseName, uint
    _roleCode, uint
    _accountStatus, bytes32
    _certType, bytes32
    _certNo, bytes32[]
    _acctId, bytes32
    _class, bytes32
    _acctSvcr, bytes32
    _acctSvcrName
)
    {
        return getAccountByAddress(msg.sender);
    }
    function getAccountByAddress(address

    addr
)
    returns(uint
    code, bytes32
    _accountName, bytes32
    _enterpriseName, uint
    _roleCode, uint
    _accountStatus, bytes32
    _certType, bytes32
    _certNo, bytes32[]
    _acctId, bytes32
    _class, bytes32
    _acctSvcr, bytes32
    _acctSvcrName
)
    {
        if (accountMap[addr].accountName == "") {
            return (2, _accountName, _enterpriseName, _roleCode, _accountStatus, _certType, _certNo, _acctId, _class, _acctSvcr, _acctSvcrName);
        }
        Account
        account = accountMap[msg.sender];
        return (0, account.accountName, account.enterpriseName, account.roleCode, account.accountStatus, account.certType, account.certNo, account.acctId, account.svcrClass, account.acctSvcr, account.acctSvcrName);
    }
    function isAccountExist(address

    accountAddress
)
    returns(bool)
    {
        if (accountMap[accountAddress].accountName == "") {
            return false;
        } else {
            return true;
        }
    }
    function checkRoleCode(address

    accountAddress, uint
    targetRoleCode
)
    returns(bool)
    {
        if (accountMap[accountAddress].roleCode == targetRoleCode) {
            return true;
        } else {
            return false;
        }
    }
    function queryRoleCode(address

    accountAddress
)
    returns(uint)
    {
        return accountMap[accountAddress].roleCode;
    }
    function getEnterpriseNameByAcctId(bytes32

    acctId
)
    returns(bytes32
    enterpriseName
)
    {
        address
        addr = acctIdToAddress[acctId];
        return accountMap[addr].enterpriseName;
    }
}
contract
ReceivableContract
{
    function getReceInfo(bytes32

    orderNo
)
    returns(uint[5]
    resultUint, bytes32[4]
    resultBytes
)
    {
        resultUint[0] = 111111;
        resultUint[1] = 111111;
        resultUint[2] = 111111;
        resultUint[3] = 111111;
        resultUint[4] = 111111;
        resultBytes[0] = "hhhhh";
        resultBytes[1] = "hhhhh";
        resultBytes[2] = "hhhhh";
        resultBytes[3] = "hhhhh";
        return (resultUint, resultBytes);
    }
    struct
    Receivable
    {
        bytes32
        receivableNo;
        bytes32
        orderNo;
        bytes32
        signer;
        bytes32
        accptr;
        bytes32
        pyer;
        bytes32
        pyee;
        bytes32
        firstOwner;
        bytes32
        secondOwner;
        uint
        isseAmt;
        uint
        cashedAmount;
        uint
        status;
        uint
        lastStatus;
        uint
        isseDt;
        uint
        signInDt;
        uint
        dueDt;
        bytes32
        rate;
        bytes32
        contractNo;
        bytes32
        invoiceNo;
        DiscountedStatus
        discounted;
        uint
        discountInHandAmount;
        bytes
        note;
    }
    struct
    ReceivableRecord
    {
        bytes32
        receivableNo;
        bytes32
        serialNo;
        bytes32
        applicantAcctId;
        bytes32
        replyerAcctId;
        ResponseType
        responseType;
        uint
        time;
        bytes32
        operateType;
        uint
        dealAmount;
        uint
        receivableStatus;
    }
    struct
    Account
    {
        address
        accountAddress;
        bytes32
        accountName;
        bytes32
        enterpriseName;
        RoleCode
        roleCode;
        AccountStatus
        accountStatus;
        bytes32
        certType;
        bytes32
        certNo;
        bytes32[]
        acctId;
        bytes32
        svcrClass;
        bytes32
        acctSvcr;
        bytes32
        acctSvcrName;
    }
enum
    RoleCode
    {
        RC00, RC01, RC02, RC03
    }
enum
    AccountStatus
    {
        VALID, INVALID, FROZEN
    }
enum
    ResponseType
    {
        YES, NO, NULL
    }
enum
    DiscountedStatus
    {
        NO, YES
    }
    bytes32[]
    allReceivableNos;
    mapping(address = > Account
)
    accountMap;
    mapping(bytes32 = > Receivable
)
    receivableDetailMap;
    mapping(bytes32 = > address
)
    acctIdToAddressMap;
    mapping(bytes32 = > bytes32[]
)
    pyerToReceivableMap;
    mapping(bytes32 = > bytes32[]
)
    pyeeToReceivableMap;
    mapping(bytes32 = > bytes32[]
)
    accptrToReceivableMap;
    mapping(bytes32 = > bytes32[]
)
    signerToReceivableMap;
    mapping(bytes32 = > bytes32
)
    orderNoToReceivableNoMap;
    mapping(bytes32 = > bytes32[]
)
    accountReceivableRecords;
    mapping(bytes32 = > ReceivableRecord
)
    receivableRecordMap;
    mapping(bytes32 = > bytes32[]
)
    holdingReceivablesMap;
    mapping(bytes32 = > bytes32[]
)
    cashedReceivablesMap;
    mapping(bytes32 = > bytes32[]
)
    receivableTransferHistoryMap;
    function judgeAccount(address

    publicKey
)
    internal
    returns(bool)
    {
        Account
        account = accountMap[publicKey];
        if (account.enterpriseName == 0x0) {
            return true;
        }
        if (account.accountStatus != AccountStatus.VALID) {
            return true;
        }
        return false;
    }
    function judgeRepetitiveSerialNo(bytes32

    serialNo
)
    internal
    returns(bool)
    {
        ReceivableRecord
        receivableRecord = receivableRecordMap[serialNo];
        if (receivableRecord.serialNo != 0x0) {
            return true;
        }
        return false;
    }
    function judgeRepetitiveReceivableNo(bytes32

    receivableNo
)
    internal
    returns(bool)
    {
        for (uint i = 0;
        i < allReceivableNos.length;
        i++
    )
        {
            if (receivableNo == allReceivableNos[i]) {
                return true;
            }
        }
        return false;
    }
    function judgeAcctIdMatchAddress(bytes32

    acctId, address
    newAddress
)
    internal
    returns(bool)
    {
        address
        oldAddress = acctIdToAddressMap[acctId];
        if (oldAddress != newAddress) {
            return true;
        }
        return false;
    }
    function judgeReplyerAddressEmpty(bytes32

    acctId
)
    internal
    returns(bool)
    {
        address
        oldAddress = acctIdToAddressMap[acctId];
        if (oldAddress == 0x0) {
            return true;
        }
        return false;
    }
    function judgeReplyerAccount(bytes32

    acctId
)
    internal
    returns(bool)
    {
        address
        replyerAddress = acctIdToAddressMap[acctId];
        Account
        replyerAccount = accountMap[replyerAddress];
        if (replyerAccount.accountStatus == AccountStatus.INVALID) {
            return true;
        }
        if (replyerAccount.enterpriseName == 0x0) {
            return true;
        }
        return false;
    }
    function newReceivableRecord(bytes32

    serialNo, bytes32
    receivableNo, bytes32
    applicantAcctId, bytes32
    replyerAcctId, ResponseType
    response, uint
    time, bytes32
    operateType, uint
    dealAmount, uint
    receivableStatus
)
    internal
    {
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
    function signOutApply(bytes32

    receivableNo, bytes32
    orderNo, bytes32
    signer, bytes32
    accptr, bytes32
    pyee, bytes32
    pyer, uint
    isseAmt, uint
    dueDt, bytes32
    rate, bytes32[]
    contractAndInvoiceNo, bytes32
    serialNo, uint
    time
)
    returns(uint
    code
)
    {
        if (receivableNo == "" || orderNo == "" || signer == "" || accptr == "" || pyer == "" || pyee == "" || rate == "" || serialNo == "") {
            return (3);
        }
        if (judgeRepetitiveReceivableNo(receivableNo)) {
            return (1030);
        }
        allReceivableNos.push(receivableNo);
        giveReceivableInfo(receivableNo, serialNo, orderNo, signer, accptr, pyer, pyee, isseAmt, dueDt, rate, contractAndInvoiceNo, time);
        accountReceivableRecords[signer].push(serialNo);
        holdingReceivablesMap[signer].push(receivableNo);
        orderNoToReceivableNoMap[orderNo] = receivableNo;
        pyerToReceivableMap[pyer].push(receivableNo);
        pyeeToReceivableMap[pyee].push(receivableNo);
        receivableTransferHistoryMap[receivableNo].push(serialNo);
        return (0);
    }
    function giveReceivableInfo(bytes32

    receivableNo, bytes32
    serialNo, bytes32
    orderNo, bytes32
    signer, bytes32
    accptr, bytes32
    pyer, bytes32
    pyee, uint
    isseAmt, uint
    dueDt, bytes32
    rate, bytes32[]
    contractAndInvoiceNo, uint
    time
)
    internal
    {
        Receivable
        receivable = receivableDetailMap[receivableNo];
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
        newReceivableRecord(serialNo, receivableNo, signer, accptr, ResponseType.NULL, time, "signOutApply", isseAmt, receivable.status);
    }
    function signOutReply(bytes32

    receivableNo, bytes32
    replyerAcctId, ResponseType
    response, bytes32
    serialNo, uint
    time
)
    returns(bytes32)
    {
        Receivable
        receivable = receivableDetailMap[receivableNo];
        if (receivable.status != 21) {
            return (1006);
        }
        receivable.lastStatus = receivable.status;
        if (response == ResponseType.NO) {
            receivable.status = 3;
        } else {
            receivable.status = 26;
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
    function discountApply(bytes32

    receivableNo, bytes32
    applicantAcctId, bytes32
    replyerAcctId, bytes32
    serialNo, uint
    time, uint
    discountApplyAmount
)
    returns(uint)
    {
        if (receivableNo == "" || applicantAcctId == "" || replyerAcctId == "" || serialNo == "") {
            return (3);
        }
        if (judgeRepetitiveSerialNo(serialNo)) {
            return (1032);
        }
        Receivable
        receivable = receivableDetailMap[receivableNo];
        receivable.lastStatus = receivable.status;
        receivable.status = 41;
        receivable.secondOwner = replyerAcctId;
        newReceivableRecord(serialNo, receivableNo, applicantAcctId, replyerAcctId, ResponseType.NULL, time, "discountApply", discountApplyAmount, receivable.status);
        accountReceivableRecords[applicantAcctId].push(serialNo);
        receivableTransferHistoryMap[receivableNo].push(serialNo);
        return (0);
    }
    function discountResponse(bytes32

    receivableNo, bytes32
    replyerAcctId, ResponseType
    responseType, bytes32
    serialNo, uint
    time, bytes32
    newReceivableNo, uint
    discountInHandAmount, bytes32
    discountApplySerialNo
)
    returns(uint)
    {
        if (receivableNo == "" || replyerAcctId == "" || serialNo == "") {
            return (3);
        }
        if (responseType != ResponseType.NO && responseType != ResponseType.YES) {
            return (1020);
        }
        if (judgeRepetitiveSerialNo(serialNo)) {
            return (1032);
        }
        if (judgeRepetitiveReceivableNo(newReceivableNo)) {
            return (1030);
        }
        Receivable
        receivable = receivableDetailMap[receivableNo];
        if (receivable.secondOwner != replyerAcctId) {
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
    function subDiscount(bytes32

    receivableNo, bytes32
    serialNo, ResponseType
    responseType, uint
    time, bytes32
    newReceivableNo, bytes32
    discountApplySerialNo
)
    internal
    {
        Receivable
        receivable = receivableDetailMap[receivableNo];
        ReceivableRecord
        receivableRecord = receivableRecordMap[discountApplySerialNo];
        uint
        discountApplyAmount = receivableRecord.dealAmount;
        uint
        oriAmount = receivable.isseAmt;
        if (responseType == ResponseType.NO) {
            receivable.status = receivable.lastStatus;
        } else {
            Receivable
            newReceivable = receivableDetailMap[newReceivableNo];
            if (judgeOperateOption(receivableNo, discountApplyAmount)) {
                copyValue(receivableNo, newReceivableNo);
                newReceivable.receivableNo = newReceivableNo;
                receivable.lastStatus = receivable.status;
                receivable.status = 49;
                receivable.discounted = DiscountedStatus.YES;
                receivable.signInDt = time;
                newReceivable.lastStatus = 41;
                newReceivable.status = 46;
                newReceivable.signInDt = time;
                newReceivable.firstOwner = receivable.secondOwner;
                newReceivable.secondOwner = "";
                newReceivable.discounted = DiscountedStatus.YES;
                newReceivable.signInDt = time;
                newReceivable.isseAmt = discountApplyAmount;
            } else {
                copyValue(receivableNo, newReceivableNo);
                newReceivable.receivableNo = newReceivableNo;
                newReceivable.lastStatus = newReceivable.status;
                newReceivable.status = 46;
                newReceivable.isseAmt = discountApplyAmount;
                newReceivable.firstOwner = receivable.secondOwner;
                newReceivable.secondOwner = "";
                newReceivable.discounted = DiscountedStatus.YES;
                newReceivable.signInDt = time;
                receivable.lastStatus = receivable.status;
                receivable.status = 48;
                receivable.isseAmt = oriAmount - discountApplyAmount;
                receivable.signInDt = time;
                receivable.firstOwner = receivable.firstOwner;
            }
        }
    }
    function judgeOperateOption(bytes32

    receivableNum, uint
    dealAmount
)
    internal
    returns(bool)
    {
        Receivable
        receivable = receivableDetailMap[receivableNum];
        if (dealAmount < receivable.isseAmt) {
            return false;
        }
        return true;
    }
    function copyValue(bytes32

    originReceivableNum, bytes32
    subReceivableNum
)
    internal
    {
        Receivable
        oriReceivable = receivableDetailMap[originReceivableNum];
        Receivable
        subReceivable = receivableDetailMap[subReceivableNum];
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
    function deleteArrayElement(bytes32

    []
    storage
    a, bytes32
    receivableNo
)
    internal
    {
        uint
        position;
        for (uint i = 0;
        i < a.length;
        i++
    )
        {
            if (a[i] == receivableNo) {
                position = i;
                break;
            }
            position++;
        }
        if (position != a.length) {
            a[position] = a[a.length - 1];
            a.length = a.length - 1;
        }
    }
    function cash(bytes32

    receivableNo, uint
    cashedAmount, uint
    time, bytes32
    serialNo, ResponseType
    responseType
)
    returns(uint)
    {
        if (receivableNo == "" || serialNo == "") {
            return (3);
        }
        if (judgeRepetitiveSerialNo(serialNo)) {
            return (1032);
        }
        if (cashedAmount <= 0) {
            return (1016);
        }
        Receivable
        receivable = receivableDetailMap[receivableNo];
        if (receivable.receivableNo == 0x0) {
            return (1005);
        }
        receivable.lastStatus = receivable.status;
        receivable.cashedAmount = cashedAmount;
        receivable.status = 1;
        cashedReceivablesMap[receivable.accptr].push(receivableNo);
        newReceivableRecord(serialNo, receivableNo, receivable.signer, receivable.accptr, ResponseType.YES, time, "Cash", cashedAmount, receivable.status);
        return (0);
    }
    function getReceivableAllInfo(bytes32

    receivableNo, bytes32
    acctId
)
    returns(uint, bytes32[], uint[], DiscountedStatus
    discounted, bytes
    note
)
    {
        Account
        account = accountMap[msg.sender];
        Receivable
        receivable = receivableDetailMap[receivableNo];
        uint[]
        memory
        uintInfo = new uint[](8);
        bytes32[]
        memory
        bytesInfo1 = new bytes32[](11);
        if (receivableNo == "") {
            return (3, bytesInfo1, uintInfo, discounted, note            );
        }
        if (receivable.receivableNo == 0x0) {
            return (1005, bytesInfo1, uintInfo, discounted, note            );
        }
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
        return (0, bytesInfo1, uintInfo, discounted, note        );
    }
    function getReceivableAllList(bytes32

    receivableNo, bytes32
    acctId
)
    returns(uint, bytes32[], uint[], DiscountedStatus
    discounted, bytes
    note
)
    {
        Account
        account = accountMap[msg.sender];
        Receivable
        receivable = receivableDetailMap[receivableNo];
        uint[]
        memory
        uintInfo = new uint[](8);
        bytes32[]
        memory
        bytesInfo1 = new bytes32[](11);
        if (receivableNo == "") {
            return (3, bytesInfo1, uintInfo, discounted, note            );
        }
        if (receivable.receivableNo == 0x0) {
            return (1005, bytesInfo1, uintInfo, discounted, note            );
        }
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
        return (0, bytesInfo1, uintInfo, discounted, note        );
    }
    function receivableSimpleDeatilList(uint

    roleCode, bytes32
    acctId, address
    orderAddress, address
    accountAddress
)
    returns(uint, bytes32[], uint[])
    {
        bytes32[]
        memory
        receivableNos;
        if (roleCode == 0) {
            receivableNos = pyerToReceivableMap[acctId];
        } else if (roleCode == 1) {
            receivableNos = pyeeToReceivableMap[acctId];
        }
        uint
        receivableNosLength = receivableNos.length;
        bytes32[]
        memory
        list1 = new bytes32[](receivableNosLength * 3);
        uint[]
        memory
        list2 = new uint[](receivableNosLength * 4);
        for (uint i = 0;
        i < receivableNosLength;
        i++
    )
        {
            list1[i * 3] = receivableNos[i];
            list1[i * 3 + 1] = callOrderContractGetProductName(orderAddress, receivableNos[i]);
            if (roleCode == 0) {
                list1[i * 3 + 2] = callAccountContractGetPyeeEnterpriseName(accountAddress, receivableNos[i]);
            } else if (roleCode == 1) {
                list1[i * 3 + 2] = callAccountContractGetPyerEnterpriseName(accountAddress, receivableNos[i]);
            }
            list2[i * 4] = callOrderContractGetProductQuantity(orderAddress, receivableNos[i]);
            list2[i * 4 + 1] = receivableDetailMap[receivableNos[i]].isseAmt;
            list2[i * 4 + 2] = receivableDetailMap[receivableNos[i]].dueDt;
            list2[i * 4 + 3] = receivableDetailMap[receivableNos[i]].status;
        }
        return (0, list1, list2);
    }
    function callOrderContractGetProductName(address

    orderAddress, bytes32
    receivableNo
)
    returns(bytes32
    productName
)
    {
        Receivable
        receivable = receivableDetailMap[receivableNo];
        bytes32
        orderNo = receivable.orderNo;
        OrderContract
        orderCon = OrderContract(orderAddress);
        return orderCon.queryProductNameByOrderNo(orderNo);
    }
    function callOrderContractGetProductQuantity(address

    orderAddress, bytes32
    receivableNo
)
    returns(uint
    productQuantity
)
    {
        Receivable
        receivable = receivableDetailMap[receivableNo];
        bytes32
        orderNo = receivable.orderNo;
        OrderContract
        orderCon = OrderContract(orderAddress);
        return orderCon.queryProductQuantityByOrderNo(orderNo);
    }
    function callAccountContractGetPyerEnterpriseName(address

    accountAddress, bytes32
    receivableNo
)
    returns(bytes32)
    {
        AccountContract
        accountCon = AccountContract(accountAddress);
        Receivable
        receivable = receivableDetailMap[receivableNo];
        bytes32
        acctId = receivable.pyer;
        return accountCon.getEnterpriseNameByAcctId(acctId);
    }
    function callAccountContractGetPyeeEnterpriseName(address

    accountAddress, bytes32
    receivableNo
)
    returns(bytes32)
    {
        AccountContract
        accountCon = AccountContract(accountAddress);
        Receivable
        receivable = receivableDetailMap[receivableNo];
        bytes32
        acctId = receivable.pyee;
        return accountCon.getEnterpriseNameByAcctId(acctId);
    }
    function getReceivableHistorySerialNo(bytes32

    receivableNo
)
    returns(uint, bytes32[])
    {
        return (0, receivableTransferHistoryMap[receivableNo]);
    }
    function getRecordBySerialNo(bytes32

    serialNm
)
    returns(uint, bytes32
    serialNo, bytes32
    receivableNo, bytes32
    applicantAcctId, bytes32
    replyerAcctId, ResponseType, uint, bytes32
    operateType, uint, uint
    receivableStatus
)
    {
        Account
        account = accountMap[msg.sender];
        ReceivableRecord
        receivableRecord = receivableRecordMap[serialNm];
        if (serialNm == "") {
            return (3, serialNo, receivableNo, applicantAcctId, replyerAcctId, ResponseType.NULL, 0, operateType, 0, receivableStatus);
        }
        if (receivableRecord.serialNo == 0x0) {
            return (1013, serialNo, receivableNo, applicantAcctId, replyerAcctId, ResponseType.NULL, 0, operateType, 0, receivableStatus);
        }
        return (0, receivableRecord.serialNo, receivableRecord.receivableNo, receivableRecord.applicantAcctId, receivableRecord.replyerAcctId, receivableRecord.responseType, receivableRecord.time, receivableRecord.operateType, receivableRecord.dealAmount, receivableRecord.receivableStatus);
    }
}
contract
RepositoryContract
{
    struct
    RepoCert
    {
        bytes32
        incomeCert;
        bytes32
        repoCertNo;
        bytes32
        repoBusinessNo;
        address
        repoEnterpriseAddress;
        address
        storerAddress;
        address
        holderAddress;
        uint
        repoCreateDate;
        bytes32
        productName;
        uint
        productQuantitiy;
        bytes32
        measureUnit;
        bytes32
        norms;
        uint
        productTotalPrice;
        bytes32
        productLocation;
        uint
        repoCertStatus;
    }
    struct
    RepoBusiness
    {
        bytes32
        repoBusinessNo;
        bytes32
        businessTransNo;
        uint
        repoBusiStatus;
        bytes32
        orderNo;
        bytes32
        wayBillNo;
        bytes32
        repoCertNo;
        address
        logisticsEnterpriseAddress;
        address
        repoEnterpriseAddress;
        address
        storerAddress;
        address
        holderAddress;
        bytes32
        incomeCert;
        bytes32
        productName;
        uint
        productQuantitiy;
        bytes32
        measureUnit;
        bytes32
        norms;
        uint
        productUnitPrice;
        uint
        productTotalPrice;
        bytes32
        productLocation;
        uint
        operateOperateTime;
    }
    struct
    RepoCertOperationRecord
    {
        uint[]
        repoCertState;
        uint[]
        operationTime;
    }
    mapping(bytes32 = > RepoCertOperationRecord
)
    repoCertRecordMap;
    mapping(address = > bytes32[]
)
    usrRepoBusinessMap;
    mapping(address = > bytes32[]
)
    usrRepoCertListMap;
    mapping(bytes32 = > bytes32
)
    logisticsBusinessMap;
    mapping(bytes32 = > bytes32[]
)
    businessTransNoMap;
    mapping(bytes32 = > RepoBusiness
)
    businessDetailMap;
    mapping(bytes32 = > RepoCert
)
    repoCertDetailMap;
    mapping(bytes32 = > bytes32
)
    repoBusiToCertMap;
    function getRepoBusiList(address

    userAddress
)
    returns(uint, bytes32[]
    repoBusiDetail1, uint[]
    repoBusiDetail2, address[]
    repoBusiDetail3
)
    {
        bytes32[]
        repoBusiNoList_2 = usrRepoBusinessMap[userAddress];
        RepoBusiness
        memory
        repoBusinsess;
        RepoCert
        memory
        repoCert;
        repoBusiDetail1 = new bytes32[](repoBusiNoList_2.length * 5);
        repoBusiDetail2 = new uint[](repoBusiNoList_2.length * 4);
        repoBusiDetail3 = new address[](repoBusiNoList_2.length);
        for (uint index = 0;
        index < repoBusiNoList_2.length;
        index++
    )
        {
            uint
            n = index;
            uint
            returnNum = index;
            bytes32
            repoBusinessNo = repoBusiNoList_2[index];
            bytes32
            repoBusiTransNo = getNewstTransNo(repoBusinessNo);
            repoBusinsess = businessDetailMap[repoBusiTransNo];
            repoCert = repoCertDetailMap[repoBusiToCertMap[repoBusinessNo]];
            function getNewstTransNo(bytes32

            repoBusiNo
        )
            returns(bytes32)
            {
                bytes32[]
                repoBusiTransNoList = businessTransNoMap[repoBusiNo];
                bytes32
                repoBusiTransNo = repoBusiTransNoList[repoBusiTransNoList.length - 1];
                return repoBusiTransNo;
            }
            function getRepoCertInfoList(address

            userAddress
        )
            returns(uint, bytes32[]
            bytesResult, uint[]
            uintResult, address[]
            resultAddress
        )
            {
                bytes32[]
                repoCertList = usrRepoBusinessMap[userAddress];
                uint
                length = repoCertList.length;
                bytesResult = new bytes32[](length * 3);
                uintResult = new uint[](length * 2);
                resultAddress = new address[](length);
                for (uint i = 0;
                i < repoCertList.length;
                i++
            )
                {
                    bytes32[]
                    busiTransNoList = businessTransNoMap[repoCertList[i]];
                    RepoBusiness
                    repoBusiess = businessDetailMap[busiTransNoList[busiTransNoList.length - 1]];
                    bytesResult[i * 3] = repoBusiess.repoBusinessNo;
                    bytesResult[i * 3 + 1] = repoBusiess.productName;
                    uintResult[i * 2] = repoBusiess.productQuantitiy;
                    uintResult[i * 2 + 1] = repoBusiess.repoBusiStatus;
                    resultAddress[i] = repoBusiess.repoEnterpriseAddress;
                }
                return (0, bytesResult, uintResult, resultAddress);
            }
            function incomeApply(bytes32

            repoBusinessNo, bytes32
            businessTransNo, bytes32
            orderNo, address
            storerAddress, address
            repoEnterpriseAddress, uint
            operateOperateTime, bytes32
            productName, uint
            productQuantitiy, uint
            productUnitPrice, uint
            productTotalPrice
        )
            returns(uint, bytes32)
            {
                usrRepoBusinessMap[storerAddress].push(repoBusinessNo);
                usrRepoBusinessMap[repoEnterpriseAddress].push(repoBusinessNo);
                businessTransNoMap[repoBusinessNo].push(businessTransNo);
                businessDetailMap[businessTransNo].repoBusinessNo = repoBusinessNo;
                businessDetailMap[businessTransNo].repoBusiStatus = 1;
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
                return (0, repoBusinessNo);
            }
            function copyStruct(bytes32

            originBusinessTransNo, bytes32
            destBusinessTransNo
        )
            internal
            {
                RepoBusiness
                sorce = businessDetailMap[originBusinessTransNo];
                RepoBusiness
                dest = businessDetailMap[destBusinessTransNo];
                dest.repoBusinessNo = sorce.repoBusinessNo;
                dest.repoBusiStatus = sorce.repoBusiStatus;
                dest.businessTransNo = sorce.businessTransNo;
                dest.orderNo = sorce.orderNo;
                dest.storerAddress = sorce.storerAddress;
                dest.holderAddress = sorce.holderAddress;
                dest.repoEnterpriseAddress = sorce.repoEnterpriseAddress;
                dest.operateOperateTime = sorce.operateOperateTime;
                dest.productName = sorce.productName;
                dest.productQuantitiy = sorce.productQuantitiy;
                dest.productUnitPrice = sorce.productUnitPrice;
                dest.productTotalPrice = sorce.productTotalPrice;
            }
            function incomeResponse(bytes32

            repoBusinessNo, bytes32
            lastBusinessTransNo, bytes32
            currBusinessTransNo, uint
            operateOperateTime
        )
            returns(uint)
            {
                copyStruct(lastBusinessTransNo, currBusinessTransNo);
                RepoBusiness
                repoBusinsess = businessDetailMap[currBusinessTransNo];
                repoBusinsess.repoBusiStatus = 2;
                repoBusinsess.businessTransNo = currBusinessTransNo;
                repoBusinsess.operateOperateTime = operateOperateTime;
                businessDetailMap[currBusinessTransNo] = repoBusinsess;
                businessTransNoMap[repoBusinessNo].push(currBusinessTransNo);
                return (0);
            }
            function incomeConfirm(bytes32

            repoBusinessNo, bytes32
            repoCertNo, bytes32
            lastBusinessTransNo, bytes32
            currBusinessTransNo, uint
            operateTime
        )
            returns(uint, bytes32)
            {
                copyStruct(lastBusinessTransNo, currBusinessTransNo);
                RepoBusiness
                repoBusinsess = businessDetailMap[currBusinessTransNo];
                repoBusinsess.repoBusiStatus = 3;
                repoBusinsess.businessTransNo = currBusinessTransNo;
                repoBusinsess.operateOperateTime = operateTime;
                businessDetailMap[currBusinessTransNo] = repoBusinsess;
                businessTransNoMap[repoBusinessNo].push(currBusinessTransNo);
                repoBusiToCertMap[repoBusinessNo] = repoCertNo;
                return repoCertNoApply(repoBusinessNo, repoCertNo, operateTime);
            }
            function outcomeApply(bytes32

            repoBusinessNo, bytes32
            lastBusinessTransNo, bytes32
            currBusinessTransNo, uint
            operateOperateTime
        )
            returns(uint)
            {
                copyStruct(lastBusinessTransNo, currBusinessTransNo);
                RepoBusiness
                repoBusinsess = businessDetailMap[currBusinessTransNo];
                repoBusinsess.repoBusiStatus = 4;
                repoBusinsess.businessTransNo = currBusinessTransNo;
                repoBusinsess.operateOperateTime = operateOperateTime;
                businessDetailMap[currBusinessTransNo] = repoBusinsess;
                businessTransNoMap[repoBusinessNo].push(currBusinessTransNo);
                return (0);
            }
            function outcomeResponse(bytes32

            repoBusinessNo, bytes32
            lastBusinessTransNo, bytes32
            currBusinessTransNo, uint
            operateOperateTime
        )
            returns(uint)
            {
                copyStruct(lastBusinessTransNo, currBusinessTransNo);
                RepoBusiness
                repoBusinsess = businessDetailMap[currBusinessTransNo];
                repoBusinsess.repoBusiStatus = 5;
                repoBusinsess.businessTransNo = currBusinessTransNo;
                repoBusinsess.operateOperateTime = operateOperateTime;
                businessDetailMap[currBusinessTransNo] = repoBusinsess;
                businessTransNoMap[repoBusinessNo].push(currBusinessTransNo);
                return (0);
            }
            function outcomeConfirm(bytes32

            repoBusinessNo, bytes32
            lastBusinessTransNo, bytes32
            currBusinessTransNo, uint
            operateOperateTime
        )
            returns(uint)
            {
                copyStruct(lastBusinessTransNo, currBusinessTransNo);
                RepoBusiness
                repoBusinsess = businessDetailMap[currBusinessTransNo];
                repoBusinsess.repoBusiStatus = 3;
                repoBusinsess.businessTransNo = currBusinessTransNo;
                repoBusinsess.operateOperateTime = operateOperateTime;
                businessDetailMap[currBusinessTransNo] = repoBusinsess;
                businessTransNoMap[repoBusinessNo].push(currBusinessTransNo);
                return (0);
            }
            function repoCertNoApply(bytes32

            repoBusinessNo, bytes32
            repoCertNo, uint
            operateTime
        )
            returns(uint, bytes32)
            {
                repoBusiToCertMap[repoBusinessNo] = repoCertNo;
                bytes32[]
                memory
                transList = businessTransNoMap[repoBusinessNo];
                uint
                index = transList.length - 1;
                bytes32
                businessTransNo = transList[index];
                RepoBusiness
                currentRepoBusinsess = businessDetailMap[businessTransNo];
                currentRepoBusinsess.repoCertNo = repoCertNo;
                address
                holderAddress = currentRepoBusinsess.holderAddress;
                address
                repoEnterpriseAddress = currentRepoBusinsess.repoEnterpriseAddress;
                usrRepoCertListMap[holderAddress].push(repoCertNo);
                usrRepoCertListMap[repoEnterpriseAddress].push(repoCertNo);
                repoCertDetailMap[repoCertNo] = RepoCert("", repoCertNo, repoBusinessNo, repoEnterpriseAddress, holderAddress, holderAddress, operateTime, currentRepoBusinsess.productName, currentRepoBusinsess.productQuantitiy, currentRepoBusinsess.measureUnit, currentRepoBusinsess.norms, currentRepoBusinsess.productTotalPrice, currentRepoBusinsess.productLocation, 1);
                return (0, repoCertNo);
            }
            function getRepoBusiHistoryList(bytes32

            repoBusinessNo
        )
            returns(uint, bytes32[], uint[])
            {
                bytes32[]
                memory
                historyList1;
                historyList1 = businessTransNoMap[repoBusinessNo];
                uint
                len = historyList1.length;
                bytes32[]
                memory
                bytesList = new bytes32[](len);
                uint[]
                memory
                intList = new uint[](len * 2);
                for (uint index = 0;
                index < len;
                index++
            )
                {
                    bytesList[index * 2] = historyList1[index];
                    intList[index * 2 + 1] = businessDetailMap[historyList1[index]].repoBusiStatus;
                    intList[index * 2] = businessDetailMap[historyList1[index]].operateOperateTime;
                }
                return (0, bytesList, intList);
            }
            function getRepoBusiDtlAndHistoryList(bytes32

            repoBusinessNo
        )
            returns(uint, uint[], bytes32[], uint[], address[])
            {
                bytes32[]
                memory
                historyList1;
                historyList1 = businessTransNoMap[repoBusinessNo];
                uint
                len = historyList1.length;
                uint[]
                memory
                historyList = new uint[](len * 2);
                RepoBusiness
                memory
                repoBusinsess;
                for (uint index = 0;
                index < len;
                index++
            )
                {
                    repoBusinsess = businessDetailMap[historyList1[index]];
                    historyList[index * 2] = repoBusinsess.repoBusiStatus;
                    historyList[index * 2 + 1] = repoBusinsess.operateOperateTime;
                }
                bytes32[]
                memory
                detailInfoList1 = new bytes32[](5);
                uint[]
                memory
                detailInfoList2 = new uint[](4);
                address[]
                memory
                detailInfoList3 = new address[](2);
                detailInfoList1[0] = repoBusinsess.repoBusinessNo;
                detailInfoList1[1] = repoBusinsess.wayBillNo;
                detailInfoList1[2] = repoBusinsess.repoCertNo;
                detailInfoList1[3] = repoBusinsess.productName;
                detailInfoList1[4] = repoBusinsess.measureUnit;
                detailInfoList2[0] = repoBusinsess.repoBusiStatus;
                detailInfoList2[1] = repoBusinsess.productQuantitiy;
                detailInfoList2[2] = repoBusinsess.productTotalPrice;
                detailInfoList2[3] = repoBusinsess.operateOperateTime;
                detailInfoList3[0] = repoBusinsess.logisticsEnterpriseAddress;
                detailInfoList3[1] = repoBusinsess.repoEnterpriseAddress;
                return (0, historyList, detailInfoList1, detailInfoList2, detailInfoList3);
            }
            function getRepoBusinessDetail(bytes32

            businessTransNo
        )
            returns(uint, uint, address, bytes32[], uint[])
            {
                RepoBusiness
                busDtl = businessDetailMap[businessTransNo];
                bytes32[]
                memory
                outBytesList = new bytes32[](3);
                uint[]
                memory
                outUintList = new uint[](3);
                outUintList[0] = busDtl.productQuantitiy;
                outUintList[1] = busDtl.productUnitPrice;
                outUintList[2] = busDtl.productTotalPrice;
                outUintList[3] = busDtl.operateOperateTime;
                return (0, busDtl.repoBusiStatus, busDtl.holderAddress, outBytesList, outUintList        );
            }
            function getRepoCertDetail(bytes32

            repoCertNo
        )
            returns(uint, bytes32[], address[], uint, uint, uint)
            {
                RepoCert
                repoCertDtl = repoCertDetailMap[repoCertNo];
                bytes32[]
                memory
                outBytesList = new bytes32[](7);
                address[]
                memory
                outAddressList = new address[](3);
                outBytesList[0] = repoCertDtl.incomeCert;
                outBytesList[1] = repoCertDtl.repoCertNo;
                outBytesList[2] = repoCertDtl.repoBusinessNo;
                outBytesList[3] = repoCertDtl.productName;
                outBytesList[4] = repoCertDtl.measureUnit;
                outBytesList[5] = repoCertDtl.norms;
                outBytesList[6] = repoCertDtl.productLocation;
                outAddressList[0] = repoCertDtl.repoEnterpriseAddress;
                outAddressList[1] = repoCertDtl.storerAddress;
                outAddressList[2] = repoCertDtl.holderAddress;
                return (0, outBytesList, outAddressList, repoCertDtl.productQuantitiy, repoCertDtl.productTotalPrice, repoCertDtl.repoCreateDate        );
            }
            function getRepoBusinessTransList(bytes32

            repoBusinessNo
        )
            returns(uint, bytes32[]
            repoBusinessTransNoList
        )
            {
                repoBusinessTransNoList = businessTransNoMap[repoBusinessNo];
                return (0, repoBusinessTransNoList);
            }
            function deleteArrayElement(bytes32

            []
            storage
            a, bytes32
            receivableNum
        )
            internal
            {
                uint
                position;
                for (uint i = 0;
                i < a.length;
                i++
            )
                {
                    if (a[i] == receivableNum) {
                        position = i;
                        break;
                    }
                    position++;
                }
                if (position != a.length) {
                    a[position] = a[a.length - 1];
                    a.length = a.length - 1;
                }
            }
            function getReceivableValue(bytes32

            orderId
        )
            returns(bytes32[])
            {
            }
            function SewingBytes32ArrayToString(bytes32

            []
            value
        )
            internal
            returns(string)
            {
                string
                memory
                TheString;
                string
                memory
                symbol1 = ",";
                uint
                j = 0;
                for (uint i = 0;
                i < value.length;
                i++
            )
                {
                    string
                    memory
                    temp1 = bytes32ToString(value[i]);
                    TheString = sewingTwoString(TheString, temp1);
                    if (i < value.length - 1) {
                        TheString = sewingTwoString(TheString, symbol1);
                    }
                }
                return TheString;
            }
            function bytes32ToString(bytes32

            x
        )
            internal
            returns(string)
            {
                bytes
                memory
                bytesString = new bytes(32);
                uint
                charCount = 0;
                for (uint j = 0;
                j < 32;
                j++
            )
                {
                    byte
                    char = byte(bytes32(uint(x) * 2 * * (8 * j)));
                    if (char != 0) {
                        bytesString[charCount] = char;
                        charCount++;
                    }
                }
                bytes
                memory
                bytesStringTrimmed = new bytes(charCount);
                for (j = 0; j < charCount; j++) {
                    bytesStringTrimmed[j] = bytesString[j];
                }
                return string(bytesStringTrimmed);
            }
            function sewingTwoString(string

            a, string
            b
        )
            internal
            returns(string)
            {
                bytes
                memory
                a_ = bytes(a);
                bytes
                memory
                b_ = bytes(b);
                bytes
                memory
                c = new bytes(a_.length + b_.length);
                uint
                j = 0;
                for (uint i = 0;
                i < c.length;
                i++
            )
                {
                    if (i < a_.length) {
                        c[i] = a_[i];
                    } else {
                        c[i] = b_[j];
                        j++;
                    }
                }
                return string(c);
            }
        }
        contract
        OrderContract
        {
            address
            owner;
            uint
            UNDEFINED = 0;
            uint
            REQUESTING = 1;
            uint
            REJECTED = 2;
            uint
            SENDING = 3;
            uint
            RECEIVED = 4;
            uint
            WATING_INCOME_RESPONSE = 1;
            uint
            WATING_INCOME = 2;
            uint
            INCOMED = 3;
            uint
            WATING_OUTCOME_RESPONSE = 4;
            uint
            WATING_OUTCOME = 5;
            uint
            OUTCOMED = 6;
            uint
            TRANSABLE = 1;
            uint
            FREED = 2;
            uint
            INVALID = 3;
            uint
            UNCONFIRMED = 1;
            uint
            CONFIRMED = 2;
            uint
            COMPLETED = 3;
            AccountContract
            accountContract;
            ReceivableContract
            receivableContract;
            RepositoryContract
            repositoryContract;
            function Reparo() {
                owner = msg.sender;
            }

        enum
            PayingMethod
            {
                RECEIVABLE, CASH
            }
            struct
            OrderState
            {
                uint
                txState;
                uint
                payerRepoBusiState;
                uint
                payeeRepoBusiState;
                uint
                wayBillState;
                uint
                receState;
            }
            struct
            Order
            {
                bytes32
                orderNo;
                address
                payerAddress;
                address
                payeeAddress;
                bytes32
                productName;
                uint
                productUnitPrice;
                uint
                productQuantity;
                uint
                productTotalPrice;
                address
                payerRepoAddress;
                address
                payeeRepoAddress;
                bytes32
                payerRepoBusinessNo;
                bytes32
                payeeRepoBusinessNo;
                bytes32
                payerRepoCertNo;
                bytes32
                payeeRepoCertNo;
                uint
                orderGenerateTime;
                bytes32
                payerBank;
                bytes32
                payerBankClss;
                bytes32
                payerAccount;
                PayingMethod
                payingMethod;
                OrderState
                orderState;
            }
            struct
            TransactionRecord
            {
                bytes32
                orderNo;
                bytes32
                txSerialNo;
                uint
                txState;
                uint
                time;
            }
            mapping(bytes32 = > Order
        )
            orderDetailMap;
            mapping(address = > bytes32[]
        )
            allPayerOrderMap;
            mapping(address = > bytes32[]
        )
            allPayeeOrderMap;
            mapping(bytes32 = > TransactionRecord
        )
            txRecordDetailMap;
            mapping(bytes32 = > bytes32[]
        )
            txSerialNoList;
            bytes32[]
            tempOrderList;
            function isValidUser(uint

            accountState
        )
            internal
            returns(bool)
            {
                if (accountState != 0) {
                    return false;
                } else {
                    return true;
                }
            }
            function createOrder(address

            acctContractAddress, address
            payeeAddress, address
            payerRepoAddress, uint
            productUnitPrice, uint
            productQuantity, uint
            productTotalPrice, bytes32[]
            bytes32Params, PayingMethod
            payingMethod, uint
            orderGenerateTime
        )
            returns(uint)
            {
                bytes32
                orderNo = bytes32Params[0];
                if (orderDetailMap[orderNo].orderNo != 0) {
                    return 2004;
                }
                orderDetailMap[orderNo].orderNo = orderNo;
                orderDetailMap[orderNo].productName = bytes32Params[1];
                orderDetailMap[orderNo].payerRepoBusinessNo = bytes32Params[2];
                orderDetailMap[orderNo].payerBank = bytes32Params[3];
                orderDetailMap[orderNo].payerBankClss = bytes32Params[4];
                orderDetailMap[orderNo].payerAccount = bytes32Params[5];
                orderDetailMap[orderNo].payerAddress = msg.sender;
                orderDetailMap[orderNo].payeeAddress = payeeAddress;
                orderDetailMap[orderNo].payerRepoAddress = payerRepoAddress;
                orderDetailMap[orderNo].productUnitPrice = productUnitPrice;
                orderDetailMap[orderNo].productQuantity = productQuantity;
                orderDetailMap[orderNo].productTotalPrice = productTotalPrice;
                orderDetailMap[orderNo].payingMethod = payingMethod;
                orderDetailMap[orderNo].orderGenerateTime = orderGenerateTime;
                updateOrderState(orderNo, "txState", 1);
                allPayerOrderMap[msg.sender].push(orderNo);
                allPayeeOrderMap[payeeAddress].push(orderNo);
                bytes32
                txSerialNo = bytes32Params[6];
                txRecordDetailMap[txSerialNo].orderNo = orderNo;
                txRecordDetailMap[txSerialNo].txSerialNo = txSerialNo;
                txRecordDetailMap[txSerialNo].time = orderGenerateTime;
                txRecordDetailMap[txSerialNo].txState = 1;
                txSerialNoList[orderNo].push(txSerialNo);
                return 0;
            }
            function orderExists(bytes32

            orderNo
        )
            returns(bool)
            {
                Order
                order = orderDetailMap[orderNo];
                if (order.orderNo == 0) return (false);
                return (true);
            }
            function searchReceGeneInfo(address

            receAddress, bytes32
            orderNo
        )
            returns(bytes32[]
            param1, uint[]
            param2
        )
            {
                receivableContract = ReceivableContract(receAddress);
                uint[5]
                memory
                resultUint;
                bytes32[4]
                memory
                resultBytes;
                (resultUint, resultBytes) = receivableContract.getReceInfo(orderNo);
                param1 = new bytes32[](10);
                param2 = new uint[](11);
                param1[0] = resultBytes[0];
                param1[1] = resultBytes[1];
                param1[2] = resultBytes[2];
                param1[3] = resultBytes[3];
                param2[0] = resultUint[0];
                param2[1] = resultUint[1];
                param2[2] = resultUint[2];
                param2[3] = resultUint[3];
                param2[4] = resultUint[4];
                param1[4] = "22222";
                param1[5] = "222";
                param2[5] = 222;
                param2[6] = 22;
                param2[7] = 22;
                param1[6] = "3333";
                param1[7] = "33";
                param1[8] = "3333333";
                param1[9] = "33";
                param2[8] = 33;
                param2[9] = 33333;
                param2[10] = 3;
                return (param1, param2);
            }
            function queryOrderDetail(address

            acctContractAddress, address
            receAddress, bytes32
            orderNo
        )
            returns(uint, address[]
            resultAddress, bytes32[]
            resultBytes32, uint[]
            resultUint, PayingMethod
            resultMethod, uint
            txState
        )
            {
                Order
                order = orderDetailMap[orderNo];
                if (!orderExists(orderNo)) {
                    return (2001, resultAddress, resultBytes32, resultUint, resultMethod, txState);
                }
                if (order.payerAddress != msg.sender && order.payeeAddress != msg.sender) {
                    return (2005, resultAddress, resultBytes32, resultUint, resultMethod, txState);
                }
                resultUint = new uint[](7);
                resultBytes32 = new bytes32[](9);
                resultAddress = new address[](4);
                resultAddress[0] = order.payerAddress;
                resultAddress[1] = order.payeeAddress;
                resultAddress[2] = order.payerRepoAddress;
                resultAddress[3] = order.payeeRepoAddress;
                resultBytes32[0] = order.orderNo;
                resultBytes32[1] = order.productName;
                resultBytes32[2] = order.payerBank;
                resultBytes32[3] = order.payerBankClss;
                resultBytes32[4] = order.payerAccount;
                resultUint[0] = order.productUnitPrice;
                resultUint[1] = order.productQuantity;
                resultUint[2] = order.productTotalPrice;
                resultUint[3] = order.orderGenerateTime;
                if (txSerialNoList[orderNo].length == 2) {
                    resultUint[4] = txRecordDetailMap[txSerialNoList[orderNo][1]].time;
                } else {
                    resultUint[4] = 0;
                }
                resultBytes32[5] = order.payerRepoBusinessNo;
                resultBytes32[6] = order.payeeRepoBusinessNo;
                resultBytes32[7] = order.payerRepoCertNo;
                resultBytes32[8] = order.payeeRepoCertNo;
                resultUint[5] = order.orderState.payerRepoBusiState;
                resultUint[6] = order.orderState.payeeRepoBusiState;
                return (0, resultAddress, resultBytes32, resultUint, order.payingMethod, order.orderState.txState);
            }
            function queryAllOrderOverViewInfoList(address

            acctContractAddress, uint
            role
        )
            returns(uint, bytes32[]
            partList1, address[]
            partList2, uint[]
            partList3, PayingMethod[]
            methodList, uint[]
            stateList
        )
            {
                bytes32[]
                memory
                orderList1;
                if (role == 0) {
                    orderList1 = allPayerOrderMap[msg.sender];
                } else if (role == 1) {
                    orderList1 = allPayeeOrderMap[msg.sender];
                }
                uint
                length = orderList1.length;
                partList1 = new bytes32[](length * 4);
                partList2 = new address[](length * 4);
                partList3 = new uint[](length * 5);
                methodList = new PayingMethod[](length);
                stateList = new uint[](length * 4);
                for (uint k = 0;
                k < orderList1.length;
                k++
            )
                {
                    partList1[k * 4] = orderDetailMap[orderList1[k]].orderNo;
                    partList1[k * 4 + 1] = orderDetailMap[orderList1[k]].productName;
                    partList1[k * 4 + 2] = orderDetailMap[orderList1[k]].payerBank;
                    partList1[k * 4 + 3] = orderDetailMap[orderList1[k]].payerAccount;
                    partList2[k * 4] = orderDetailMap[orderList1[k]].payerAddress;
                    partList2[k * 4 + 1] = orderDetailMap[orderList1[k]].payeeAddress;
                    partList2[k * 4 + 2] = orderDetailMap[orderList1[k]].payerRepoAddress;
                    partList2[k * 4 + 3] = orderDetailMap[orderList1[k]].payeeRepoAddress;
                    partList3[k * 5] = orderDetailMap[orderList1[k]].productQuantity;
                    partList3[k * 5 + 1] = orderDetailMap[orderList1[k]].productUnitPrice;
                    partList3[k * 5 + 2] = orderDetailMap[orderList1[k]].productTotalPrice;
                    partList3[k * 5 + 3] = orderDetailMap[orderList1[k]].orderGenerateTime;
                    if (txSerialNoList[orderList1[k]].length == 2) {
                        partList3[k * 5 + 4] = txRecordDetailMap[txSerialNoList[orderList1[k]][1]].time;
                    } else {
                        partList3[k * 5 + 4] = 0;
                    }
                    methodList[k] = orderDetailMap[orderList1[k]].payingMethod;
                    stateList[k * 4] = orderDetailMap[orderList1[k]].orderState.txState;
                    if (role == 0) {
                        stateList[k * 4 + 1] = orderDetailMap[orderList1[k]].orderState.payerRepoBusiState;
                    } else {
                        stateList[k * 4 + 1] = orderDetailMap[orderList1[k]].orderState.payeeRepoBusiState;
                    }
                    stateList[k * 4 + 2] = orderDetailMap[orderList1[k]].orderState.wayBillState;
                    stateList[k * 4 + 3] = orderDetailMap[orderList1[k]].orderState.receState;
                }
                return (0, partList1, partList2, partList3, methodList, stateList);
            }
            function queryAllOrderListForPayer()

            returns(uint, bytes32[]
            resultList
        )
            {
                return (0, allPayerOrderMap[msg.sender]);
            }
            function queryAllOrderListForPayee()

            returns(uint, bytes32[]
            resultList
        )
            {
                return (0, allPayeeOrderMap[msg.sender]);
            }
            function confirmOrder(address

            acctContractAddress, bytes32
            orderNo, address
            payeeRepoAddress, bytes32
            payeeRepoCertNo, bytes32
            txSerialNo, uint
            orderConfirmTime
        )
            returns(uint)
            {
                Order
                order = orderDetailMap[orderNo];
                if (!orderExists(orderNo)) {
                    return 2001;
                }
                if (order.payeeAddress != msg.sender) {
                    return 2007;
                }
                if (order.orderState.txState == 2) {
                    return 2006;
                }
                updateOrderState(orderNo, "txState", 2);
                orderDetailMap[orderNo].payeeRepoAddress = payeeRepoAddress;
                orderDetailMap[orderNo].payeeRepoCertNo = payeeRepoCertNo;
                if (orderDetailMap[orderNo].orderState.payerRepoBusiState == 2) {
                    updateOrderState(orderNo, "receState", 2);
                }
                txRecordDetailMap[txSerialNo].orderNo = orderNo;
                txRecordDetailMap[txSerialNo].txSerialNo = txSerialNo;
                txRecordDetailMap[txSerialNo].time = orderConfirmTime;
                txRecordDetailMap[txSerialNo].txState = 2;
                txSerialNoList[orderNo].push(txSerialNo);
                return 0;
            }
            function queryProductNameByOrderNo(bytes32

            orderNo
        )
            returns(bytes32)
            {
                return orderDetailMap[orderNo].productName;
            }
            function queryProductQuantityByOrderNo(bytes32

            orderNo
        )
            returns(uint)
            {
                return orderDetailMap[orderNo].productQuantity;
            }
            function updateOrderState(bytes32

            orderNo, bytes32
            stateType, uint
            newState
        )
            returns(uint)
            {
                Order
                order = orderDetailMap[orderNo];
                if (stateType == "txState") {
                    order.orderState.txState = newState;
                    return 0;
                }
                if (stateType == "payerRepoBusiState") {
                    order.orderState.payerRepoBusiState = newState;
                    return 0;
                }
                if (stateType == "payeeRepoBusiState") {
                    order.orderState.payeeRepoBusiState = newState;
                    return 0;
                }
                if (stateType == "wayBillState") {
                    order.orderState.wayBillState = newState;
                    if (newState == RECEIVED) {
                        if (order.orderState.payerRepoBusiState == INCOMED) {
                            order.orderState.txState = COMPLETED;
                        }
                    }
                    return 0;
                }
                if (stateType == "receState") {
                    order.orderState.receState = newState;
                    return 0;
                }
            }
        }