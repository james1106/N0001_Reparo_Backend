
contract WayBillContract {

// enum WAYBILL_{ REQUESTING, REJECTED, SENDING, RECEIVED }
//运单状态（发货待响应、发货被拒绝，已发货，已送达）
    uint WAYBILL_UNDEFINED = 0;
    uint WAYBILL_REQUESTING = 1;
    uint WAYBILL_REJECTED = 2;
    uint WAYBILL_SENDING = 3;
    uint WAYBILL_RECEIVED = 4;

//RCOMPANY融资企业, LOGISTICS物流公司,STORAGE仓储公司,BANK金融机构
// enum RoleCode {COMPANY, LOGISTICS, REPOSITORY, FINANCIAL}
    uint ROLE_COMPANY;
    uint ROLE_LOGISTICS;
    uint ROLE_REPOSITORY;
    uint ROLE_FINANCIAL;


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
        bytes32[] logisticsInfo;

    //TODO ：运单上的每一个address都要判断用户是否存在？（发货者、收货者、物流、发货仓储、收货仓储）

    //权限控制
        if(isAccountExist(msg.sender) == false){ //用户不存在
            return CODE_INVALID_USER;
        }
        if(checkRoleCode(msg.sender, ROLE_COMPANY) == false || msg.sender != addrs[1]){ //用户无权限
            return CODE_PERMISSION_DENIED;
        }
        if(statusTransIdToWayBillDetail[strs[4]].productName != ""){ //运单已经存在
            return CODE_WAY_BILL_ALREADY_EXIST;
        }
    //TODO 查应收款状态是否为承兑已签收（订单状态是否为已确认已经先判断），否则无权限

    //生成未确认运单
        statusTransIdToWayBillDetail[strs[4]] = WayBill(strs[0], strs[4], wayBillNo, addrs[0], addrs[1], addrs[2], strs[1], integers[2], integers[1], integers[0], addrs[4], strs[2], addrs[3], strs[3], logisticsInfo, WAYBILL_REQUESTING);
    //
        orderNoToStatusTransIdList[strs[0]][0] =  strs[4];
    //
        addressToOrderNoList[addrs[1]][addressToOrderNoList[addrs[1]].length] = strs[0];
        addressToOrderNoList[addrs[2]][addressToOrderNoList[addrs[2]].length] = strs[0];
        addressToOrderNoList[addrs[0]][addressToOrderNoList[addrs[0]].length] = strs[0];

        return CODE_SUCCESS; //成功
    }

//生成已确认运单
    function generateWayBill(bytes32 orderNo, bytes32 statusTransId, bytes32 wayBillNo, uint sendTime, address accountContractAddr) returns (uint code){
        accountContract = AccountContract (accountContractAddr);

        bytes32[] statusTransIdList = orderNoToStatusTransIdList[orderNo];
        WayBill oldWaybill = statusTransIdToWayBillDetail[statusTransIdList[statusTransIdList.length - 1]];

    //权限控制
        if(isAccountExist(msg.sender) == false){ //用户不存在
            return CODE_INVALID_USER;
        }
        if(checkRoleCode(msg.sender, ROLE_LOGISTICS) == false || msg.sender != oldWaybill.logisticsAddress || oldWaybill.waybillStatus != WAYBILL_REQUESTING){ //用户无权限
            return CODE_PERMISSION_DENIED;
        }

    //生成已确认运单
        statusTransIdToWayBillDetail[statusTransId] = WayBill(orderNo, statusTransId, wayBillNo, oldWaybill.logisticsAddress, oldWaybill.senderAddress, oldWaybill.receiverAddress, oldWaybill.productName, oldWaybill.productQuantity, oldWaybill.productValue, sendTime, oldWaybill.senderRepoAddress, oldWaybill.senderRepoCertNo, oldWaybill.receiverRepoAddress, oldWaybill.receiverRepoBusinessNo, oldWaybill.logisticsInfo, WAYBILL_REQUESTING);
    //
        orderNoToStatusTransIdList[orderNo][1] =  statusTransId;
    //
        return CODE_SUCCESS;
    }

//获取所有用户相关运单的订单号列表
    function listWayBillOrderNo(address accountContractAddr) returns (uint code, bytes32[] orderNoList){
        accountContract = AccountContract (accountContractAddr);
    //权限控制
        if(isAccountExist(msg.sender) == false){ //用户不存在
            return (CODE_INVALID_USER, orderNoList);
        }
        if(checkRoleCode(msg.sender, ROLE_LOGISTICS) == false && checkRoleCode(msg.sender, ROLE_COMPANY) == false){ //用户无权限
            return (CODE_PERMISSION_DENIED, orderNoList);
        }

        return (CODE_SUCCESS,addressToOrderNoList[msg.sender]);
    }

//根据订单号获取运单详情
    function getWayBill(bytes32 orderNo, address accountContractAddr) returns (uint code, uint[] ints, bytes32[] strs, address[] addrs, bytes32[] logisticsInfo, uint waybillStatus) {
        accountContract = AccountContract (accountContractAddr);
    //权限控制
        if(isAccountExist(msg.sender) == false){ //用户不存在
            return (CODE_INVALID_USER,ints, strs, addrs, logisticsInfo, waybillStatus);
        }
        if(checkRoleCode(msg.sender, ROLE_LOGISTICS) == false && checkRoleCode(msg.sender, ROLE_COMPANY) == false){ //用户无权限
            return (CODE_PERMISSION_DENIED,ints, strs, addrs, logisticsInfo, waybillStatus);
        }

    //获取运单最新信息
        bytes32[] statusTransIdList = orderNoToStatusTransIdList[orderNo];
        WayBill waybill = statusTransIdToWayBillDetail[statusTransIdList[statusTransIdList.length - 1]]; //取最新状态的运单信息

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
        uint requestTime;
        uint receiveTime;
        uint sendTime;
        uint rejectTime;
        if(waybill.waybillStatus == WAYBILL_REQUESTING){
            requestTime = statusTransIdToWayBillDetail[statusTransIdList[0]].operateTime;
        }else if(waybill.waybillStatus == WAYBILL_SENDING){
            requestTime = statusTransIdToWayBillDetail[statusTransIdList[0]].operateTime;
            sendTime = statusTransIdToWayBillDetail[statusTransIdList[1]].operateTime;
        }else if(waybill.waybillStatus == WAYBILL_RECEIVED){
            requestTime = statusTransIdToWayBillDetail[statusTransIdList[0]].operateTime;
            sendTime = statusTransIdToWayBillDetail[statusTransIdList[1]].operateTime;
            receiveTime = statusTransIdToWayBillDetail[statusTransIdList[2]].operateTime;
        }else{ //waybill.waybillStatus == WAYBILL_REJECTED
            requestTime = statusTransIdToWayBillDetail[statusTransIdList[0]].operateTime;
            rejectTime = statusTransIdToWayBillDetail[statusTransIdList[1]].operateTime;
        }

        waybillStatus = waybill.waybillStatus;
        logisticsInfo = waybill.logisticsInfo;

        ints[0] = waybill.productQuantity;
        ints[1] = waybill.productValue;
        ints[2] = requestTime;
        ints[3] = receiveTime;
        ints[4] = sendTime;
        ints[5] = rejectTime;

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

        return (CODE_SUCCESS, ints, strs, addrs, logisticsInfo, waybillStatus);

    }


//更新运单状态为已送达
    function updateWAYBILL_oReceived(bytes32 orderNo, bytes32 statusTransId, uint operateTime, address accountContractAddr, address repoContractAddr, address orderContractAddr) returns (uint code){
        accountContract = AccountContract (accountContractAddr);
        repositoryContract = RepositoryContract (repoContractAddr);
        orderContract = OrderContract (orderContractAddr);

    //权限控制
        if(isAccountExist(msg.sender) == false){ //用户不存在
            return CODE_INVALID_USER;
        }
        if(checkRoleCode(msg.sender, ROLE_LOGISTICS) == false){ //用户无权限
            return CODE_PERMISSION_DENIED;
        }

    //获取运单最新信息
        bytes32[] statusTransIdList = orderNoToStatusTransIdList[orderNo];
        WayBill oldWaybill = statusTransIdToWayBillDetail[statusTransIdList[statusTransIdList.length - 1]]; //取最新状态的运单信息

        if(oldWaybill.waybillStatus != WAYBILL_SENDING){//用户无权限（状态流转）
            return CODE_PERMISSION_DENIED;
        }

        statusTransIdToWayBillDetail[statusTransId] = WayBill(orderNo, statusTransId, oldWaybill.wayBillNo, oldWaybill.logisticsAddress, oldWaybill.senderAddress, oldWaybill.receiverAddress, oldWaybill.productName, oldWaybill.productQuantity, oldWaybill.productValue, operateTime, oldWaybill.senderRepoAddress, oldWaybill.senderRepoCertNo, oldWaybill.receiverRepoAddress, oldWaybill.receiverRepoBusinessNo, oldWaybill.logisticsInfo, WAYBILL_RECEIVED);
    //
        orderNoToStatusTransIdList[orderNo][statusTransIdList.length] =  statusTransId;
    //
        return (CODE_SUCCESS);
    }

//更新运单状态为申请发货被拒绝
    function updateWAYBILL_oRejected(bytes32 orderNo, bytes32 statusTransId, uint operateTime, address accountContractAddr) returns (uint code){
        accountContract = AccountContract (accountContractAddr);

    //权限控制
        if(isAccountExist(msg.sender) == false){ //用户不存在
            return CODE_INVALID_USER;
        }
        if(checkRoleCode(msg.sender, ROLE_LOGISTICS) == false){ //用户无权限
            return CODE_PERMISSION_DENIED;
        }

    //获取运单最新信息
        bytes32[] statusTransIdList = orderNoToStatusTransIdList[orderNo];
        WayBill oldWaybill = statusTransIdToWayBillDetail[statusTransIdList[statusTransIdList.length - 1]]; //取最新状态的运单信息

        if(oldWaybill.waybillStatus != WAYBILL_REQUESTING){//用户无权限（状态流转）
            return CODE_PERMISSION_DENIED;
        }

    //TODO 判断仓储状态是否为已入库，如果是，则更新订单状态为已完成

        statusTransIdToWayBillDetail[statusTransId] = WayBill(orderNo, statusTransId, oldWaybill.wayBillNo, oldWaybill.logisticsAddress, oldWaybill.senderAddress, oldWaybill.receiverAddress, oldWaybill.productName, oldWaybill.productQuantity, oldWaybill.productValue, operateTime, oldWaybill.senderRepoAddress, oldWaybill.senderRepoCertNo, oldWaybill.receiverRepoAddress, oldWaybill.receiverRepoBusinessNo, oldWaybill.logisticsInfo, WAYBILL_REJECTED);
    //
        orderNoToStatusTransIdList[orderNo][statusTransIdList.length] =  statusTransId;
    //
        return (CODE_SUCCESS);
    }


    function isAccountExist(address accountAddress) returns (bool){

        var(code, accountName, enterpriseName, roleCode, accountStatus, certType, certNo, cctId, class, acctSvcr, acctSvcrName) = accountContract.getAccountByAddress(accountAddress);

        if(accountName == ""){
            return false;
        }else{
            return true;
        }
    }

    function checkRoleCode(address accountAddress, uint targetRoleCode)returns (bool){
        var(code, accountName, enterpriseName, roleCode, accountStatus, certType, certNo, cctId, class, acctSvcr, acctSvcrName) = accountContract.getAccountByAddress(accountAddress);
        if(roleCode == targetRoleCode){
            return true;
        }else{
            return false;
        }
    }

//备注：用户权限控制：用户是否存在，用户身份操作权限，业务状态流转权限

}

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

}

contract ReceivableContract{

}
contract OrderContract{

}
contract RepositoryContract{

}



