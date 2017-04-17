
contract AccountContract {

// enum RoleCode {COMPANY, LOGISTICS, REPOSITORY, FINANCIAL}
//RCOMPANY融资企业, LOGISTICS物流公司,REPOSITORY仓储公司,FINANCIAL金融机构
    uint ROLE_COMPANY = 0;
    uint ROLE_LOGISTICS = 1;
    uint ROLE_REPOSITORY = 2;
    uint ROLE_FINANCIAL = 3;

// enum AccountStatus {VALID, INVALID,FROZEN}
//账户状态，有效、无效、冻结
    uint STATUS_VALID = 0;
    uint STATUS_INVALID = 1;
    uint STATUS_FROZEN = 2;

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
    }


// 用户地址 => 结构体Account
    mapping (address => Account) accountMap;
// 账号acctId => 用户地址
    mapping (bytes32 => address) acctIdToAddress;


    function newAccount(bytes32 _accountName, bytes32 _enterpriseName, uint _roleCode, uint _accountStatus, bytes32 _certType, bytes32 _certNo, bytes32[] _acctId, bytes32 _svcrClass, bytes32 _acctSvcr, bytes32 _acctSvcrName) returns (uint code){
        if(accountMap[msg.sender].accountName != ""){
            return 5002; //账户已存在
        }
        accountMap[msg.sender] = Account(msg.sender, _accountName, _enterpriseName, _roleCode, _accountStatus, _certType, _certNo, _acctId, _svcrClass, _acctSvcr, _acctSvcrName);
        for (uint i = 0; i < _acctId.length; i++){
            acctIdToAddress[_acctId[i]] = msg.sender;
        }
        return 0; //成功
    }


    function getAccount() returns (uint code, bytes32 _accountName, bytes32 _enterpriseName, uint _roleCode, uint _accountStatus, bytes32 _certType, bytes32 _certNo, bytes32[] _acctId, bytes32 _class, bytes32 _acctSvcr, bytes32 _acctSvcrName){
        return getAccountByAddress(msg.sender);
    }

    function getAccountByAddress(address addr) returns (uint code, bytes32 _accountName, bytes32 _enterpriseName, uint _roleCode, uint _accountStatus, bytes32 _certType, bytes32 _certNo, bytes32[] _acctId, bytes32 _class, bytes32 _acctSvcr, bytes32 _acctSvcrName){
        if(accountMap[addr].accountName == ""){
            return (2, _accountName, _enterpriseName, _roleCode, _accountStatus, _certType, _certNo, _acctId, _class, _acctSvcr, _acctSvcrName); //账户不存在，该用户可能未注册或已失效
        }
        Account account = accountMap[msg.sender];
        return (0, account.accountName, account.enterpriseName, account.roleCode, account.accountStatus, account.certType, account.certNo, account.acctId, account.svcrClass, account.acctSvcr, account.acctSvcrName); //成功
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

}


contract WayBillContract {

// enum WAYBILL_{ REQUESTING, REJECTED, SENDING, RECEIVED }
//运单状态（发货待响应、发货被拒绝，已发货，已送达）
    uint WAYBILL_UNDEFINED = 0;
    uint WAYBILL_WAITING = 1;
    uint WAYBILL_REQUESTING = 2;
    uint WAYBILL_REJECTED = 3;
    uint WAYBILL_SENDING = 4;
    uint WAYBILL_RECEIVED = 5;

//RCOMPANY融资企业, LOGISTICS物流公司,STORAGE仓储公司,BANK金融机构
// enum RoleCode {COMPANY, LOGISTICS, REPOSITORY, FINANCIAL}
    uint ROLE_COMPANY = 0;
    uint ROLE_LOGISTICS = 1;
    uint ROLE_REPOSITORY = 2;
    uint ROLE_FINANCIAL = 3;

    AccountContract accountContract;
    WayBillContract wayBillContract;
    ReceivableContract receivableContract;
    OrderContract orderContract;
    RepositoryContract repositoryContract;

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

    uint CODE_SUCCESS = 0; //成功
    uint CODE_PERMISSION_DENIED = 1; //用户无权限
    uint CODE_INVALID_USER = 2; //用户不存在
    uint CODE_WAY_BILL_ALREADY_EXIST = 3000; //运单已经存在
    uint CODE_WAY_BILL_NO_DATA = 3001; //该用户暂无数据

//生成待发货运单（初始化状态为待发货、待发货时间）。内部调用：供应收账款模块调用（当应收款状态为承兑已签收时调用）
    function initWayBillStatus(bytes32 orderNo, uint waitTime, address senderAddress, address receiverAddress) returns (uint code){
    //拼接statusTransId
        string memory s1 = bytes32ToString(orderNo);
        string memory s2 = bytes32ToString(bytes32(WAYBILL_WAITING));
        string memory conStr = concatString(s1, s2);
        bytes32 statusTransId = stringToBytes32(conStr);
    //其他参数
        address logisticsAddress;
        address receiverRepoAddress;
        address senderRepoAddress;
        bytes32[] memory logisticsInfo;

    //生成未确认运单
        statusTransIdToWayBillDetail[statusTransId] = WayBill(orderNo, statusTransId, "", logisticsAddress, senderAddress, receiverAddress, "", 0, 0, waitTime, senderRepoAddress, "", receiverRepoAddress, "", logisticsInfo, WAYBILL_WAITING);
    //
        orderNoToStatusTransIdList[orderNo].push(statusTransId);
    //
        addressToOrderNoList[senderAddress].push(orderNo);
        addressToOrderNoList[receiverAddress].push(orderNo);

        return (CODE_SUCCESS);
    }

//生成待确认运单
    function generateUnConfirmedWayBill(uint[] integers, address[] addrs, bytes32[] strs, address accountContractAddr, address receivableContractAddress) returns (uint code){
        accountContract = AccountContract (accountContractAddr);
        receivableContract = ReceivableContract (receivableContractAddress);
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
        bytes32 wayBillNo;
        bytes32[] memory logisticsInfo;

    //TODO ：运单上的每一个address都要判断用户是否存在？（发货者、收货者、物流、发货仓储、收货仓储）

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
        statusTransIdToWayBillDetail[strs[4]] = WayBill(strs[0], strs[4], wayBillNo, addrs[0], addrs[1], addrs[2], strs[1], integers[2], integers[1], integers[0], addrs[4], strs[2], addrs[3], strs[3], logisticsInfo, WAYBILL_REQUESTING);
    //
        orderNoToStatusTransIdList[strs[0]].push(strs[4]);
    //
        addressToOrderNoList[addrs[0]].push(strs[0]);

        return CODE_SUCCESS; //成功
    }

//生成已确认运单
    function generateWayBill(bytes32 orderNo, bytes32 statusTransId, bytes32 wayBillNo, uint sendTime, address accountContractAddr) returns (uint code){
        accountContract = AccountContract (accountContractAddr);

        bytes32[] memory statusTransIdList = orderNoToStatusTransIdList[orderNo];
        WayBill memory oldWaybill = statusTransIdToWayBillDetail[statusTransIdList[statusTransIdList.length - 1]];

    //权限控制
        if(accountContract.isAccountExist(msg.sender) == false){ //用户不存在
            return CODE_INVALID_USER;
        }
    // TODO 权限控制
        if(accountContract.checkRoleCode(msg.sender, ROLE_LOGISTICS) == false || msg.sender != oldWaybill.logisticsAddress || oldWaybill.waybillStatus != WAYBILL_REQUESTING){ //用户无权限
            return CODE_PERMISSION_DENIED;
        }

    //生成已确认运单
        statusTransIdToWayBillDetail[statusTransId] = WayBill(orderNo, statusTransId, wayBillNo, oldWaybill.logisticsAddress, oldWaybill.senderAddress, oldWaybill.receiverAddress, oldWaybill.productName, oldWaybill.productQuantity, oldWaybill.productValue, sendTime, oldWaybill.senderRepoAddress, oldWaybill.senderRepoCertNo, oldWaybill.receiverRepoAddress, oldWaybill.receiverRepoBusinessNo, oldWaybill.logisticsInfo, WAYBILL_SENDING);
    //
        orderNoToStatusTransIdList[orderNo].push(statusTransId);
    //
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
        if(accountContract.checkRoleCode(msg.sender, ROLE_LOGISTICS) == false && accountContract.checkRoleCode(msg.sender, ROLE_COMPANY) == false){ //用户无权限
            return (CODE_PERMISSION_DENIED,ints, strs, addrs, logisticsInfo);
        }

    //获取运单最新信息
        bytes32[] memory statusTransIdList = orderNoToStatusTransIdList[orderNo];
        if (statusTransIdList.length == 0) {
            return (CODE_WAY_BILL_NO_DATA,ints, strs, addrs, logisticsInfo);
        }
        WayBill memory waybill = statusTransIdToWayBillDetail[statusTransIdList[statusTransIdList.length - 1]]; //取最新状态的运单信息

    //bytes32 orderNo = waybill.orderNo;
    // bytes32 wayBillNo = waybill.wayBillNo;
    // bytes32 productName = waybill.productName;
    // bytes32 senderRepoCertNo = waybill.senderRepoCertNo;
    // bytes32 receiverRepoBusinessNo = waybill.receiverRepoBusinessNo;
    // address logisticsAddress = waybill.logisticsAddress;
    // address senderAddress = waybill.senderAddress;
    // address receiverAddress = waybill.receiverAddress;
    // address senderRepoAddress = waybill.senderRepoAddress;
    // address receiverRepoAddress = waybill.receiverRepoAddress;
    // uint productQuantity = waybill.productQuantity;
    // uint productValue = waybill.productValue;
    // uint waitTime;
    // uint requestTime;
    // uint receiveTime;
    // uint sendTime;
    // uint rejectTime;
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
            requestTime = statusTransIdToWayBillDetail[statusTransIdList[1]].operateTime;
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

// for other contract
    function getWayBillOverview(bytes32 orderNo, address accountContractAddr)
    returns(bytes32 wayBillNo, uint requestTime, address logisticsAddress, uint waybillStatus, uint operateTime){
    /*运单号, 下单时间, 物流公司, 物流当前状态, 更新时间*/
        accountContract = AccountContract (accountContractAddr);

        if (statusTransIdList.length == 0) {
            return (wayBillNo,requestTime, logisticsAddress, waybillStatus, operateTime);
        }
    //获取运单最新信息
        bytes32[] memory statusTransIdList = orderNoToStatusTransIdList[orderNo];
        WayBill memory waybill = statusTransIdToWayBillDetail[statusTransIdList[statusTransIdList.length - 1]]; //取最新状态的运单信息
        requestTime = statusTransIdToWayBillDetail[statusTransIdList[0]].operateTime;
        return (waybill.wayBillNo, requestTime, waybill.logisticsAddress, waybill.waybillStatus, waybill.operateTime);
    }


//更新运单状态为已送达
    function updateWayBillStatusToReceived(bytes32 orderNo, bytes32 statusTransId, uint operateTime, address accountContractAddr, address repoContractAddr, address orderContractAddr) returns (uint code){
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

    //获取运单最新信息
        bytes32[] memory statusTransIdList = orderNoToStatusTransIdList[orderNo];
        if (statusTransIdList.length == 0) {
            return CODE_WAY_BILL_NO_DATA;
        }
        WayBill oldWaybill = statusTransIdToWayBillDetail[statusTransIdList[statusTransIdList.length - 1]]; //取最新状态的运单信息

        if(oldWaybill.waybillStatus != WAYBILL_SENDING){//用户无权限（状态流转）
            return CODE_PERMISSION_DENIED;
        }


        statusTransIdToWayBillDetail[statusTransId] = WayBill(orderNo, statusTransId, oldWaybill.wayBillNo, oldWaybill.logisticsAddress, oldWaybill.senderAddress, oldWaybill.receiverAddress, oldWaybill.productName, oldWaybill.productQuantity, oldWaybill.productValue, operateTime, oldWaybill.senderRepoAddress, oldWaybill.senderRepoCertNo, oldWaybill.receiverRepoAddress, oldWaybill.receiverRepoBusinessNo, oldWaybill.logisticsInfo, WAYBILL_RECEIVED);
    //
        orderNoToStatusTransIdList[orderNo].push(statusTransId);
    //
    //TODO 调用订单合约更新订单状态为已完成
    //orderContract.updateOrderState(orderNo, "wayBillState", WAYBILL_RECEIVED);
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

        if(oldWaybill.waybillStatus != WAYBILL_REQUESTING){//用户无权限（状态流转）
            return CODE_PERMISSION_DENIED;
        }

    //TODO 判断仓储状态是否为已入库，如果是，则更新订单状态为已完成

        statusTransIdToWayBillDetail[statusTransId] = WayBill(orderNo, statusTransId, oldWaybill.wayBillNo, oldWaybill.logisticsAddress, oldWaybill.senderAddress, oldWaybill.receiverAddress, oldWaybill.productName, oldWaybill.productQuantity, oldWaybill.productValue, operateTime, oldWaybill.senderRepoAddress, oldWaybill.senderRepoCertNo, oldWaybill.receiverRepoAddress, oldWaybill.receiverRepoBusinessNo, oldWaybill.logisticsInfo, WAYBILL_REJECTED);
    //
        orderNoToStatusTransIdList[orderNo].push(statusTransId);
    //
        return (CODE_SUCCESS);
    }


    function test(bytes32[] strs) returns (uint code){
        return 0;
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
 */
if(judgeRepetitiveReceivableNo(receivableNo)){//判断该应收款编号是否已经存在
return (1030);
}

allReceivableNos.push(receivableNo);
giveReceivableInfo(receivableNo, serialNo, orderNo, signer, accptr, pyer, pyee, isseAmt, dueDt, rate, contractAndInvoiceNo, time);

//newReceivableRecord(serialNo, receivableNo, signer, accptr, ResponseType.NULL, time, "signOutApply", isseAmt);

accountReceivableRecords[signer].push(serialNo);
holdingReceivablesMap[signer].push(receivableNo);
orderNoToReceivableNoMap[orderNo] = receivableNo;
pyerToReceivableMap[pyer].push(receivableNo);
pyeeToReceivableMap[pyee].push(receivableNo);
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
receivable.status = 21;
receivable.contractNo = contractAndInvoiceNo[0];
receivable.invoiceNo = contractAndInvoiceNo[1];
newReceivableRecord(serialNo, receivableNo, signer, accptr, ResponseType.NULL, time, "signOutApply", isseAmt, receivable.status);
}

//签发回复
function signOutReply(bytes32 receivableNo, bytes32 replyerAcctId, ResponseType response, bytes32 serialNo, uint time, address accountAddress, address wayBillContractAddress) returns (uint){
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
/*        if(replyerAcctId != receivable.accptr){
 return (1);
 }*/
receivable.lastStatus = receivable.status;
if(response == ResponseType.NO){
receivable.status = 3;
}else{
receivable.status = 26;
address pyeeAddress = callAccountContractGetAddressByAcctId(receivable.pyee, accountAddress);
address pyerAddress = callAccountContractGetAddressByAcctId(receivable.pyer, accountAddress);
WayBillContract wayBillCon = WayBillContract(wayBillContractAddress);
wayBillCon.initWayBillStatus(receivable.orderNo, time, pyeeAddress, pyerAddress);
}
receivable.signInDt = time;

//pyerToReceivableMap[receivable.pyer].push(receivableNo);
//pyeeToReceivableMap[receivable.pyee].push(receivableNo);
accptrToReceivableMap[receivable.accptr].push(receivableNo);
signerToReceivableMap[receivable.signer].push(receivableNo);
accountReceivableRecords[replyerAcctId].push(serialNo);
newReceivableRecord(serialNo, receivableNo, receivable.signer, replyerAcctId, response, time, "signOutReply", receivable.isseAmt, receivable.status);
receivableTransferHistoryMap[receivableNo].push(serialNo);
return (0);
}

function callAccountContractGetAddressByAcctId(bytes32 acctId, address accountAddress) returns (address){
AccountContract accountCon = AccountContract(accountAddress);
return accountCon.getAddressByAcctId(acctId);
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
if(receivable.status == 1){
return (1038);
}
receivable.lastStatus = receivable.status;
receivable.status = 41;
receivable.secondOwner = replyerAcctId;
newReceivableRecord(serialNo, receivableNo, applicantAcctId, replyerAcctId, ResponseType.NULL, time, "discountApply", discountApplyAmount, receivable.status);
accountReceivableRecords[applicantAcctId].push(serialNo);
receivableTransferHistoryMap[receivableNo].push(serialNo);
return(0);
}

//贴现回复
function discountReply(bytes32 receivableNo, bytes32 replyerAcctId, ResponseType responseType, bytes32 serialNo, uint time, bytes32 newReceivableNo, uint discountInHandAmount) returns(uint) {
if(receivableNo == "" || replyerAcctId == "" || serialNo == ""){
return (3);
}
if(responseType != ResponseType.NO && responseType != ResponseType.YES){
return (1020);
}
if(judgeRepetitiveSerialNo(serialNo)){
return (1032);
}
if(judgeRepetitiveReceivableNo(newReceivableNo)){//判断新应收款编号是否已经存在
return (1030);
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
subDiscount(receivableNo, serialNo, responseType, time, newReceivableNo);
holdingReceivablesMap[replyerAcctId].push(newReceivableNo);
accountReceivableRecords[replyerAcctId].push(serialNo);
newReceivableRecord(serialNo, receivableNo, receivable.firstOwner, replyerAcctId, responseType, time, "discountResponse", discountInHandAmount, receivable.status);
receivableTransferHistoryMap[receivableNo].push(serialNo);
return (0);
}

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
function cash(bytes32 receivableNo, uint cashedAmount, uint time, bytes32 serialNo, ResponseType responseType)returns(uint){
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
/*
 if(receivable.status != "020006" && receivable.status != "070006"){
 return(1006);
 }
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
return (0);
}

//带有应收款流水信息的应收款更具体详情
function getReceivableAllInfoWithSerial(bytes32 receivableNo, bytes32 acctId) returns (uint, bytes32[], uint[], DiscountedStatus discounted, bytes note){
Account account = accountMap[msg.sender];
Receivable receivable = receivableDetailMap[receivableNo];
bytes32[] memory historySerialNos = receivableTransferHistoryMap[receivableNo];

uint[] memory uintInfo = new uint[](historySerialNos.length * 2 + 8);
bytes32[] memory bytesInfo1 = new bytes32[](11);
//uint[] memory uintSerials = new uint[](historySerialNos.length * 2 + 8);
/*
 if(judgeAccount(msg.sender)){
 return (2,
 bytesInfo1,
 //bytesInfo2,
 uintInfo,
 uintSerials,
 discounted,
 note
 );
 }
 */
if(receivableNo == ""){
return (3,
bytesInfo1,
uintInfo,

discounted,
note
);
}

if(receivable.receivableNo == 0x0) {
return(1005,
bytesInfo1,
uintInfo,

discounted,
note
);
}

/*
 if(receivable.signer != acctId && receivable.accptr != acctId && receivable.pyer != acctId && receivable.pyee != acctId) {
 return(1,
 bytesInfo1,
 uintInfo,
 uintSerials,
 discounted,
 note
 );
 }
 */

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
uintInfo,
receivable.discounted,
receivable.note
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

//获取买卖方列表
function receivableSimpleDetailList(uint roleCode, bytes32 acctId, address orderAddress, address accountAddress) returns (uint, bytes32[], uint[]) {
bytes32[] memory receivableNos;
if(roleCode == 0){//买家(付款人)
receivableNos = pyerToReceivableMap[acctId];
}else if(roleCode == 1){//卖家
receivableNos = pyeeToReceivableMap[acctId];
}
uint receivableNosLength = receivableNos.length;
bytes32[] memory list1 = new bytes32[](receivableNosLength * 3);//receivableNo，productName,收款人企业名enterpriseName
uint[] memory list2 = new uint[](receivableNosLength * 4);//productQuantity,isseAmt, dueDt,status

for(uint i = 0; i < receivableNosLength; i++){
list1[i*3] = receivableNos[i];
list1[i*3+1] = callOrderContractGetProductName(orderAddress, receivableNos[i]);
if(roleCode == 0){
list1[i*3+2] = callAccountContractGetPyeeEnterpriseName(accountAddress, receivableNos[i]);
}else if(roleCode ==1){
list1[i*3+2] = callAccountContractGetPyerEnterpriseName(accountAddress, receivableNos[i]);
}
list2[i*4] = callOrderContractGetProductQuantity(orderAddress, receivableNos[i]);
list2[i*4+1] = receivableDetailMap[receivableNos[i]].isseAmt;
list2[i*4+2] = receivableDetailMap[receivableNos[i]].dueDt;
list2[i*4+3] = receivableDetailMap[receivableNos[i]].status;
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

contract OrderContract{
address owner;
//==========运单状态=======
uint UNDEFINED = 0;           //未定义
uint REQUESTING = 1;          //发货待响应
uint REJECTED = 2;            //发货被拒绝
uint SENDING = 3;             //已发货
uint RECEIVED = 4;            //已送达
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
address acctContractAddress,
address payeeAddress,           //卖方地址
address payerRepoAddress,       //买方仓储公司地址
uint productUnitPrice,          //货品单价
uint productQuantity,           //货品数量
uint productTotalPrice,         //货品总价
bytes32[] bytes32Params,        //数组里有6个参数，orderNo，productName，repoBusinessNo
//payerBank, payerBankClss, payerAccount
PayingMethod payingMethod,      //付款方式
uint orderGenerateTime) returns(uint){  //生成订单时间

//此处从公用合约处获取调用者的角色和账户状态以做权限控制
//如果用户不存在，返回"账户不存在，该用户可能未注册或已失效"
/*accountContract = AccountContract(acctContractAddress);
 if(!accountContract.isAccountExist(msg.sender)){
 return 2;
 }*/

//如果用户不是融资企业，返回"权限拒绝"
/*if(accountContract.queryRoleCode(msg.sender) != 0){
 return 1;
 }*/
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
updateOrderState(orderNo, "txState", 1);      //更新买方的所有订单和卖方所有的订单
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
receivableContract = ReceivableContract(receAddress);
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
param1[0] = "";
param1[1] = "";
param1[2] = "";
param1[3] = "";

param2[0] = 1;
param2[1] = 1;
param2[2] = 1;
param2[3] = 1;
param2[4] = 1;

//此处为运单信息
param1[4] = wayBillNo;//运单号
param2[5] = requestTime; //下单时间
param2[6] = waybillStatus;  //当前状态
param2[7] = operateTime;  //更新时间
param3[0] = logisticsAddress;  //物流公司地址

/*param1[4] = "wayBillNo";//运单号
param2[5] = 99; //下单时间
param2[6] = 99;  //当前状态
param2[7] = 99;  //更新时间
param3[0] = msg.sender;  //物流公司地址*/

return(param1, param2, param3);
}

/*******************************根据订单编号获取订单详情***********************************/

function queryOrderDetail(address receAddress, address wbillContractAddress, address accountContractAddr, bytes32 orderNo) returns(uint, address[] resultAddress, bytes32[] resultBytes32, uint[] resultUint, PayingMethod resultMethod, uint txState){
//如果用户不存在，返回"账户不存在，该用户可能未注册或已失效"
/*accountContract = AccountContract(acctContractAddress);
 if(!accountContract.isAccountExist(msg.sender)){
 return (2, resultAccount, resultBytes32, resultUint, resultMethod, txState);
 }*/

//如果用户不是融资企业，返回"权限拒绝"
/*if(accountContract.queryRoleCode(msg.sender) != 0){
 return (1, resultAccount, resultBytes32, resultUint, resultMethod, txState);
 }*/
Order order = orderDetailMap[orderNo];

//如果订单不存在，返回"订单不存在"
if(!orderExists(orderNo)){
return (2001, resultAddress, resultBytes32, resultUint, resultMethod, txState);
}

//如果订单与合约调用者无关，"权限拒绝"
if (order.payerAddress != msg.sender && order.payeeAddress != msg.sender) {
return (2005, resultAddress, resultBytes32, resultUint, resultMethod, txState);
}


bytes32[] memory param1 = new bytes32[](5);
uint[] memory param2 = new uint[](8);
address[] memory param3 = new address[](1);

(param1, param2, param3) = searchReceAndWayBillInfo(receAddress, wbillContractAddress, accountContractAddr, orderNo);
resultUint = new uint[](10);
resultBytes32 = new bytes32[](10);
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
if(txSerialNoList[orderNo].length == 2){
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
resultUint[8] = param2[6];//当前状态
resultUint[9] = param2[7];//更新时间
resultAddress[4] = param3[0];//物流公司地址

//应收账款信息uint
/*resultUint[5] = paramPart2[0];
 resultUint[6] = paramPart2[1];
 resultUint[7] = paramPart2[2];
 resultUint[8] = paramPart2[3];
 resultUint[9] = paramPart2[4];*/

return (0, resultAddress, resultBytes32, resultUint, order.payingMethod, order.orderState.txState);
}

function queryAllOrderOverViewInfoList(address acctContractAddress,uint role)returns(
uint,
bytes32[] partList1,//5值 orderNo, productName，payerRepo，payerBank，payerBankAccount
address[] partList2, //2值 payerAddress, payeeAddress
uint[] partList3,//4值productQuantity,productUnitPrice,productTotalPrice,orderGenerateTime,orderConfirmTime
PayingMethod[] methodList,
uint[] stateList){
//如果用户不存在，返回"账户不存在，该用户可能未注册或已失效"
/*accountContract = AccountContract(acctContractAddress);
 if(!accountContract.isAccountExist(msg.sender)){
 return (2, partList1, partList2, partList3, methodList, stateList);
 }*/

//如果用户不是融资企业，返回"权限拒绝"
/*if(accountContract.queryRoleCode(msg.sender) != 0){
 return (1, partList1, partList2, partList3, methodList, stateList);
 }*/


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

function confirmOrder(address acctContractAddress, bytes32 orderNo, address payeeRepoAddress, bytes32 payeeRepoCertNo, bytes32 txSerialNo, uint orderConfirmTime) returns(uint){
//如果用户不存在，返回"账户不存在，该用户可能未注册或已失效"
/*accountContract = AccountContract(acctContractAddress);
 if(!accountContract.isAccountExist(msg.sender)){
 return 2;
 }*/

//如果用户不是融资企业，返回"权限拒绝"
/*if(accountContract.queryRoleCode(msg.sender) != 0){
 return 1;
 }*/
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
updateOrderState(orderNo, "txState", 2);//修改订单详情map中的订单状态
orderDetailMap[orderNo].payeeRepoAddress = payeeRepoAddress;
orderDetailMap[orderNo].payeeRepoCertNo = payeeRepoCertNo;

//确认订单后，检查仓储状态，如果为"待入库",则修改应收账款状态为"待签发"
if(orderDetailMap[orderNo].orderState.payerRepoBusiState == 2){
updateOrderState(orderNo, "receState", 2);
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

/***********************根据订单编号更新订单的某状态******************************/
function updateOrderState(bytes32 orderNo, bytes32 stateType, uint newState)returns(uint){
Order order = orderDetailMap[orderNo];
if(stateType == "txState"){
order.orderState.txState = newState;
return 0;
}
if(stateType == "payerRepoBusiState"){
order.orderState.payerRepoBusiState = newState;
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

contract RepositoryContract{

}


