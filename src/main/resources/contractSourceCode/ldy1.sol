
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


