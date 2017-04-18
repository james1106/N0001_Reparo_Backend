

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

contract ReceivableContract{


}
contract RepositoryContract{

}
contract OrderContract{

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
/*senderAddress, receiverAddress, senderRepoAddress, receiverRepoAddress*/
/*orderNo, senderRepoCertNo, receiverRepoBusinessNo, productName*/
/*waitTime, productQuantity, productValue*/
    function initWayBillStatus( address[] addrs, bytes32[] strs, uint[] integers ) returns (uint code){
    //拼接statusTransId
        string memory s1 = bytes32ToString(strs[0]);
        string memory s2 = bytes32ToString(bytes32(WAYBILL_WAITING));
        string memory conStr = concatString(s1, s2);
        bytes32 statusTransId = stringToBytes32(conStr);
    //其他参数
        address logisticsAddress;
        bytes32[] memory logisticsInfo;

    //生成初始化运单
        statusTransIdToWayBillDetail[statusTransId] = WayBill(strs[0], statusTransId, "", logisticsAddress, addrs[0], addrs[1], strs[3], integers[1], integers[2], integers[0], addrs[2], strs[2], addrs[3], strs[2], logisticsInfo, WAYBILL_WAITING);
    //
        orderNoToStatusTransIdList[strs[0]].push(statusTransId);
    //
        addressToOrderNoList[addrs[0]].push(strs[0]);
        addressToOrderNoList[addrs[1]].push(strs[0]);

        return CODE_SUCCESS;
    }

//生成待确认运单
/*requestTime, productValue, productQuantity, */
/*logisticsAddress, senderAddress, receiverAddress, receiverRepoAddress, senderRepoAddress*/
/*orderNo, productName, senderRepoCertNo, receiverRepoBusinessNo, statusTransId, waybillNo*/
    function generateUnConfirmedWayBill(uint[] integers,  address[] addrs, bytes32[] strs, address accountContractAddr, address receivableContractAddress) returns (uint code){
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
    // bytes32 waybillNo = strs[5];
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
        statusTransIdToWayBillDetail[strs[4]] = WayBill(strs[0], strs[4], strs[5], addrs[0], addrs[1], addrs[2], strs[1], integers[2], integers[1], integers[0], addrs[4], strs[2], addrs[3], strs[3], logisticsInfo, WAYBILL_REQUESTING);
    //
        orderNoToStatusTransIdList[strs[0]].push(strs[4]);
    //
        addressToOrderNoList[addrs[0]].push(strs[0]);

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
    // TODO 权限控制
        if(accountContract.checkRoleCode(msg.sender, ROLE_LOGISTICS) == false || msg.sender != oldWaybill.logisticsAddress || oldWaybill.waybillStatus != WAYBILL_REQUESTING){ //用户无权限
            return CODE_PERMISSION_DENIED;
        }

    //生成已确认运单
        statusTransIdToWayBillDetail[statusTransId] = WayBill(orderNo, statusTransId, oldWaybill.wayBillNo, oldWaybill.logisticsAddress, oldWaybill.senderAddress, oldWaybill.receiverAddress, oldWaybill.productName, oldWaybill.productQuantity, oldWaybill.productValue, sendTime, oldWaybill.senderRepoAddress, oldWaybill.senderRepoCertNo, oldWaybill.receiverRepoAddress, oldWaybill.receiverRepoBusinessNo, oldWaybill.logisticsInfo, WAYBILL_SENDING);
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
    //if(accountContract.checkRoleCode(msg.sender, ROLE_LOGISTICS) == false && accountContract.checkRoleCode(msg.sender, ROLE_COMPANY) == false){ //用户无权限
    //    return (CODE_PERMISSION_DENIED,ints, strs, addrs, logisticsInfo);
    //}

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

