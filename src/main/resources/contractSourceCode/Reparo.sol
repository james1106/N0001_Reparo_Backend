pragma solidity ^0.4.0;


/**
* 国金和趣链的合作项目——物流保险子项目 智能合约代码
*
* $description 依据业务需求，实现物流企业、国金平台、保险服务商三方对投保申请单和投保反馈单的创建、更新、查询功能
* $version v1
*
* $modifyLog rita.mao(2017-03-20 18:55)    创建：定义变量和规范
* $modifyLog liangyue(2017-03-21 21:34)    增加：投保申请单的创建和更新状态函数
* $modifyLog martin(2017-03-21 21:34)      增加：根据流水号查询投保申请单和国金平台查询未收到的投保申请单
* $modifyLog martin(2017-03-23 22:03)      增加：投保反馈单的创建和更新状态函数
* $modifyLog liangyue(2017-03-23 22:03)    增加：根据流水号查询投保反馈单和国金平台查询未收到的投保反馈单
*
* Copyright(C)2016 Hyperchain Technologies Co., Ltd. All rights reserved.
*/
contract Alohomora {
/*************************************************************************
                                变量定义
**************************************************************************/
// 返回结果"code"
    enum Code {
    // 0 成功
    SUCCESS,
    // 1 权限拒绝
    PERMISSION_DENIED,
    // 2 无效的用户（公钥已被无效或者公钥未注册）
    INVALID_USER,
    // 3 角色代码参数非法（不是BM、OJ、IFC中之一）
    INVALID_PARAM_ROLE,
    // 4 状态参数非法（不是1、2、4、8之一）
    INVALID_PARAM_STATUS,
    // 5 投保申请单已经存在（重复新建时将报此错）
    BILL_ALREADY_EXISTED,
    // 6 投保申请单不存在（查询、更新状态时不存在将报此错）
    BILL_DO_NOT_EXISTE,
    // 7 投保申请单状态更新非法（没有按状态机变更的流程更新，比如直接从1更新为8）
    BILL_STATUS_UPDATE_ILLEGAL,
    // 8 投保申请单状态重复更新（重复更新时报此错）
    BILL_STATUS_WAS_ALREADY_UPDATED,
    // 9 投保反馈单已经存在（重复新建时将报此错）
    REPLY_ALREADY_EXISTED,
    // 10 投保反馈单不存在（查询、更新状态时不存在将报此错）
    REPLY_DO_NOT_EXISTE,
    // 11 投保反馈单状态更新非法（没有按状态机变更的流程更新，比如直接从1更新为8）
    REPLY_STATUS_UPDATE_ILLEGAL,
    // 12 投保反馈单状态重复更新（重复更新时报此错）
    REPLY_STATUS_WAS_ALREADY_UPDATED,
    // 13 投保反馈单不能在投保申请单收到（状态=8）前创建
    REPLY_CANNOT_BE_CREATED_UNTIL_BILL_HAS_BEEN_RECEIVED,
    // 14 轮询投保申请单或投保反馈单的状态不符合流程
    POLLING_BILL_REPLY_WITH_ILLEGAL_STATUS
    }

// 账户状态，判断账户是否有效
    enum State {
    INVALID, // 没有设置值时默认为0，即无效
    VALID,
    FROZEN}

// 角色
    bytes5 constant BM = "BM";   // 物流企业
    bytes5 constant OJ = "OJ";   // 保险服务商
    bytes5 constant IFC = "IFC";     // 国金平台

// 用户描述结构体
    struct User {
    bytes5 role;    // 角色，BM,OJ,IFC
    bytes16 logisticsExchangeCode;     // 物流交换码
    State state;     // 账号状态
    }

// 投保申请描述结构体
    struct InsuranceBill {
    bytes32 sequenceCode;    // 流水作业号
    int status;     // 状态机，状态机变化在此状态位体现
    bytes16 bmCode;    // 物流交换代码
    bytes16 ojCode;    // 保险服务商物流交换代码
    bytes detail;
    }

// 投保反馈描述结构体
    struct Reply {
    bytes32 sequenceCode;     // 流水作业号，与投保反馈对应
    int status;     // 状态机
    int8 insuranceStatusCode;  // 反馈状态，成功(1)、失败(2)、退回(3)
    bytes detail;
    }

// 管理员地址
    address chairperson;

// 用户<公钥地址，用户信息>
    mapping (address => User) users;

// 用户<物流交换代码,公钥地址>
    mapping (bytes16 => address) usersAddress;

// 投保申请单详情<流水作业号，投保申请详情>
    mapping (bytes32 => InsuranceBill) allBillMap;

// 状态机分类 <状态机，List<流水作业号>>
    mapping (int => bytes32[]) billStatusMap;

// 投保反馈单详情<流水作业号，投保反馈单详情>
    mapping (bytes32 => Reply) allReplyMap;

// 投保反馈分类信息<状态机,List<流水作业号>>
    mapping (int => bytes32[]) replyStatusMap;

// 用户涉及的保险服务列表 <物流交换代码，List<流水作业号>>
    mapping (bytes16 => bytes32[]) userBillMap;

// 所有交易流水号的数组
    bytes32[] sequenceCodeArray;

// 根据特定需要，该数组用于返回某类交易流水号数组
    bytes32[] sequenceCodeList;

/*************************************************************************
                                构造函数
**************************************************************************/
    function Alohomora() {
        chairperson = msg.sender;
        users[chairperson] = User(IFC, "chairperson", State.VALID);
        usersAddress["chairperson"] = chairperson;
    }

/*************************************************************************
                                工具函数
**************************************************************************/
// 是否是有效用户
    function isValidUser() internal returns (bool) {
    // 无效用户(包括未注册的用户)
        if (users[msg.sender].state != State.VALID) {
            return false;
        }
        else {
            return true;
        }
    }

// 是否是合约所有者
    function isChairperson() internal returns (bool) {
        if (msg.sender != chairperson) {
            return false;
        }
        else {
            return true;
        }
    }

// 是否是所需的用户角色
    function isUserRole(bytes5 role) internal returns (bool){
        if (users[msg.sender].role == role) {
            return true;
        }
        else {
            return false;
        }
    }

// 判断投保申请单是否已经被创建
    function isBillAlreadyCreated(bytes32 sequenceCode) internal returns (bool) {
        if (allBillMap[sequenceCode].status != 0) {
            return true;
        }
        else {
            return false;
        }
    }

// 判断投保申请单是否已经被创建
    function isReplyAlreadyCreated(bytes32 sequenceCode) internal returns (bool) {
        if (allReplyMap[sequenceCode].status != 0) {
            return true;
        }
        else {
            return false;
        }
    }

// 更新投保单状态
    function updateBillStatusDatabase(bytes32 sequenceCode, int oldStatus, int newStatus) internal {
        allBillMap[sequenceCode].status = newStatus;
        billStatusMap[newStatus].push(sequenceCode);
        bytes32[] billSequenceCodeList = billStatusMap[oldStatus];
        uint listLength = billSequenceCodeList.length;
        for (uint i = 0; i < listLength; i++) {
            if (billSequenceCodeList[i] == sequenceCode) {
                billSequenceCodeList[i] = billSequenceCodeList[listLength - 1];
                billSequenceCodeList.length = listLength - 1;
            }
        }
    }

// 更新投保反馈单状态
    function updateReplyStatusDatabase(bytes32 sequenceCode, int oldStatus, int newStatus) internal {
        allReplyMap[sequenceCode].status = newStatus;
        replyStatusMap[newStatus].push(sequenceCode);
        bytes32[] replySequenceCodeList = replyStatusMap[oldStatus];
        uint listLength = replySequenceCodeList.length;
        for (uint i = 0; i < listLength; i++) {
            if (replySequenceCodeList[i] == sequenceCode) {
                replySequenceCodeList[i] = replySequenceCodeList[listLength - 1];
                replySequenceCodeList.length = listLength - 1;
            }
        }
    }

/*************************************************************************
                                接口函数
**************************************************************************/
/**
* 创建用户
*
* $param pubKey 公钥
* $param role 用户角色
* $param logisticsExchangeCode 物流交换码
*
* $return code 结果代码
*/
    function createUser(address pubKey, bytes5 role, bytes16 logisticsExchangeCode) returns (Code code) {
    // 调用者需已注册且有效
        if (!isValidUser()) {
            return Code.INVALID_USER;
        }
        // 权限：调用者仅合约创建者
        else if (!isChairperson()) {
            return Code.PERMISSION_DENIED;
        }
        // 若用户ID（物流交换码）已经注册过，则无效上次的公钥
        else if (usersAddress[logisticsExchangeCode] != 0x0) {
            users[usersAddress[logisticsExchangeCode]].state = State.INVALID;
        }
    // 存入数据：用户ID(物流交换码) -> 公钥 -> 用户信息
        users[pubKey] = User(role, logisticsExchangeCode, State.VALID);
        usersAddress[logisticsExchangeCode] = pubKey;
        return Code.SUCCESS;
    }

/**
* 查询合约调用者当前用户信息
*
* $return code 结果代码
* $return logisticsExchangeCode 物流交换码
* $return role 用户角色
* $return state 用户状态
*/
    function queryUser() returns (Code code, bytes16 logisticsExchangeCode, bytes5 role, State state) {
    // 调用者需已注册且有效
        if (!isValidUser()) {
            return (Code.INVALID_USER, "", "", State.INVALID);
        }
        else {
            User user = users[msg.sender];
            return (code, user.logisticsExchangeCode, user.role, user.state);
        }
    }

/**
* 创建投保申请单
*
* $param sequenceCode 流水号
* $param bmCode BM物流交换码
* $param ojCode OJ物流交换码
* $param detail 投保申请单详情JSON
*
* $return code 结果代码
*/
    function createBill(bytes32 sequenceCode, bytes16 bmCode, bytes16 ojCode, bytes detail) returns (Code code) {
    // 调用者需已注册且有效
        if (!isValidUser()) {
            return Code.INVALID_USER;
        }
    // 调用者需是物流企业
        if (!isUserRole(BM)) {
            return Code.PERMISSION_DENIED;
        }
    // 投保申请单需未存在
        if (isBillAlreadyCreated(sequenceCode)) {
            return Code.BILL_ALREADY_EXISTED;
        }
    // 存入数据
    // 流水号 -> 投保申请单
        allBillMap[sequenceCode] = InsuranceBill(sequenceCode, 1, bmCode, ojCode, detail);
    // 状态 -> 流水号[]
        billStatusMap[1].push(sequenceCode);
    // 物流企业用户ID -> 流水号[]
        userBillMap[bmCode].push(sequenceCode);
    // 保险服务商用户ID -> 流水号[]
        userBillMap[ojCode].push(sequenceCode);
    // 所有流水号数组
        sequenceCodeArray.push(sequenceCode);
        return Code.SUCCESS;
    }

/**
* 根据流水号轮询投保申请单
*
* $param sequenceCode
*
* $return code 结果代码
* $return status 投保申请单状态
* $return billDetail 投保申请单详情JSON
*/
    function pollingBillBySequenceCode(bytes32 sequenceCode) returns (Code code, bytes billDetail) {
    // 调用者需已注册且有效
        if (!isValidUser()) {
            return (Code.INVALID_USER, "");
        }
        // 投保申请单需存在
        else if (!isBillAlreadyCreated(sequenceCode)) {
            return (Code.BILL_DO_NOT_EXISTE, "");
        }
        InsuranceBill bill = allBillMap[sequenceCode];
    // 如果调用者是OJ
        if (isUserRole(OJ)) {
        // 投保申请单对应的保险服务商需是调用者
            if (users[msg.sender].logisticsExchangeCode != bill.ojCode) {
                return (Code.PERMISSION_DENIED, "");
            // 投保申请单的状态需是4（保险服务商未收到）
            }
            else if (bill.status != 4) {
                return (Code.POLLING_BILL_REPLY_WITH_ILLEGAL_STATUS, "");
            }
            else {
                return (Code.SUCCESS, bill.detail);
            }
        // 如果调用者是IFC
        }
        else if (isUserRole(IFC)) {
        // 投保申请单的状态需是1（国金平台未收到）
            if (bill.status != 1) {
                return (Code.POLLING_BILL_REPLY_WITH_ILLEGAL_STATUS, "");
            }
            else {
                return (Code.SUCCESS, bill.detail);
            }
        }
        // 如果调用者是BM则权限拒绝
        else {
            return (Code.PERMISSION_DENIED, "");
        }
    }

/**
* 国金平台查询未收到的投保申请单
*
* $return code 结果代码
* $return sequenceCodeList 所有未收到的投保申请单流水号列表
*/
    function pollingBillsForIFCByStatus1() returns (Code code, bytes32[]) {
    // 调用者需已注册且有效
        if (!isValidUser()) {
            return (Code.INVALID_USER, billStatusMap[0]);
        }
        // 调用者需是国金平台
        else if (!isUserRole(IFC)) {
            return (Code.PERMISSION_DENIED, billStatusMap[0]);
        }
        // 国金平台可以获得所有状态是1的投保申请单流水号列表
        else {
            return (Code.SUCCESS, billStatusMap[1]);
        }
    }

/**
* 保险服务商查询未收到的投保申请单
*
* $return code 结果代码
* $return sequenceCodeList 所有未收到的投保申请单流水号列表
*/
    function pollingBillsForOJByStatus4() returns (Code code, bytes32[]) {
    // 调用者需已注册且有效
        if (!isValidUser()) {
            return (Code.INVALID_USER, billStatusMap[0]);
        }
        // 调用者需是OJ
        else if (!isUserRole(OJ)) {
            return (Code.PERMISSION_DENIED, billStatusMap[0]);
        }
        else {
        // 全局通用的数组使用前需清空
            sequenceCodeList.length = 0;
        // 筛选出所有状态4且属于调用者OJ的流水号
            for (uint i = 0; i < billStatusMap[4].length; i++) {
                if (allBillMap[billStatusMap[4][i]].ojCode == users[msg.sender].logisticsExchangeCode) {
                    sequenceCodeList.push(billStatusMap[4][i]);
                }
            }
            return (Code.SUCCESS, sequenceCodeList);
        }
    }


/**
* 更新投保申请单状态
*
* $param sequenceCode 流水号
* $param status 投保申请单状态
*
* $return code 结果代码
*/
    function updateBill(bytes32 sequenceCode, int status) returns (Code code){
    // 调用者需已注册且有效
        if (!isValidUser()) {
            return Code.INVALID_USER;
        }
    // 投保申请单需存在
        if (!isBillAlreadyCreated(sequenceCode)) {
            return Code.BILL_DO_NOT_EXISTE;
        }
        int billStatus = allBillMap[sequenceCode].status;
    //如果投保单新状态与原状态相同，返回保单已更新
        if (billStatus == status) {
            return Code.BILL_STATUS_WAS_ALREADY_UPDATED;
        }
    //如果保单原状态为1（保单已创建成功）
        if (billStatus == 1) {
            if (status == 2 || status == - 1) {
                if (users[msg.sender].role == IFC) {
                    updateBillStatusDatabase(sequenceCode, billStatus, status);
                    return Code.SUCCESS;
                }
                else {
                    return Code.PERMISSION_DENIED;
                }
            }
            else {
                return Code.BILL_STATUS_UPDATE_ILLEGAL;
            }
        }
        if (billStatus == 2) {
            if (status == 4) {
                if (users[msg.sender].role == IFC) {
                    updateBillStatusDatabase(sequenceCode, billStatus, status);
                    return Code.SUCCESS;
                }
                else {
                    return Code.PERMISSION_DENIED;
                }
            }
            else {
                return Code.BILL_STATUS_UPDATE_ILLEGAL;
            }
        }
        if (billStatus == 4) {
            if (status == 8 || status == - 2) {
                if (users[msg.sender].role == OJ) {
                    updateBillStatusDatabase(sequenceCode, billStatus, status);
                    return Code.SUCCESS;
                }
                else {
                    return Code.PERMISSION_DENIED;
                }
            }
            else {
                return Code.BILL_STATUS_UPDATE_ILLEGAL;
            }
        }
        if (billStatus == - 1 || billStatus == - 2) {
            return Code.BILL_STATUS_UPDATE_ILLEGAL;
        }
        else {
            return Code.BILL_DO_NOT_EXISTE;
        }
    }

/**
* 国金平台轮询所有未从保险服务商收到的投保反馈单流水号（只查询状态为为1）
*
* $return code 结果代码
* $return sequenceCodeList 所有未收到的投保申请单流水号列表
*/
    function pollingRepliesForIFCByStatus1() returns (Code code, bytes32[]) {
        if (!isValidUser()) {
            return (Code.INVALID_USER, replyStatusMap[0]);
        }
        if (!isUserRole(IFC)) {
            return (Code.PERMISSION_DENIED, replyStatusMap[0]);
        }
        else {
            return (Code.SUCCESS, replyStatusMap[1]);
        }
    }

/**
* 物流公司轮询所有未从国金平台收到的投保申请单回执流水号（只查询状态为为4的回执单）
*
* $return code 结果代码
* $return sequenceCodeList 所有未收到的投保申请单流水号列表
*/
    function pollingRepliesForBMByStatus4() returns (Code code, bytes32[]) {
        if (!isValidUser()) {
            return (Code.INVALID_USER, replyStatusMap[0]);
        }
        if (!isUserRole(BM)) {
            return (Code.PERMISSION_DENIED, replyStatusMap[0]);
        }
        else {
            sequenceCodeList.length = 0;
            for (uint i = 0; i < replyStatusMap[4].length; i++) {
                if (allBillMap[replyStatusMap[4][i]].bmCode == users[msg.sender].logisticsExchangeCode) {
                    sequenceCodeList.push(replyStatusMap[4][i]);
                }
            }
            return (Code.SUCCESS, sequenceCodeList);
        }
    }

/**
* 根据流水号查询投保反馈单
*
* $param sequenceCode
*
* $return code 结果代码
* $return status 投保反馈单状态
* $return billDetail 投保反馈单详情JSON
*/
    function pollingReplyBySequenceCode(bytes32 sequenceCode) returns (Code code, bytes replyDetail) {
    // 调用者需已注册且有效
        if (!isValidUser()) {
            return (Code.INVALID_USER, "");
        }
    // 投保反馈单需存在
        if (!isReplyAlreadyCreated(sequenceCode)) {
            return (Code.REPLY_DO_NOT_EXISTE, "");
        }
        Reply reply = allReplyMap[sequenceCode];
    // 如果调用者是BM
        if (isUserRole(BM)) {
        // 投保反馈单对应的物流企业需是调用者
            if (users[msg.sender].logisticsExchangeCode != allBillMap[sequenceCode].bmCode) {
                return (Code.PERMISSION_DENIED, "");
            // 投保申请单的状态需是4（物流企业未收到）
            }
            else if (reply.status != 4) {
                return (Code.POLLING_BILL_REPLY_WITH_ILLEGAL_STATUS, "");
            }
            else {
                return (Code.SUCCESS, reply.detail);
            }
        // 如果调用者是IFC
        }
        else if (isUserRole(IFC)) {
        // 投保申请单的状态需是1（国金平台未收到）
            if (reply.status != 1) {
                return (Code.POLLING_BILL_REPLY_WITH_ILLEGAL_STATUS, "");
            }
            else {
                return (Code.SUCCESS, reply.detail);
            }
        }
        // 如果调用者是OJ则权限拒绝
        else {
            return (Code.PERMISSION_DENIED, "");
        }
    }

/**
* 创建投保反馈单
*
* $param sequenceCode 流水号
* $param insuranceStatusCode 投保反馈单的二级状态
* $param detail 投保反馈单详情JSON
*
* $return code 结果代码
*/
    function createReply(bytes32 sequenceCode, int8 insuranceStatusCode, bytes detail) returns (Code code) {
    // 调用者需已注册且有效
        if (!isValidUser()) {
            return Code.INVALID_USER;
        }
    // 调用者需是保险服务商
        if (!isUserRole(OJ)) {
            return Code.PERMISSION_DENIED;
        }
    // 投保申请单需已存在
        if (!isBillAlreadyCreated(sequenceCode)) {
            return Code.BILL_DO_NOT_EXISTE;
        }
    // 投保申请单需已被保险服务商接收（状态是8）
        if (allBillMap[sequenceCode].status != 8) {
            return Code.REPLY_CANNOT_BE_CREATED_UNTIL_BILL_HAS_BEEN_RECEIVED;
        }
    // 投保反馈单需未被创建
        if (isReplyAlreadyCreated(sequenceCode)) {
            return Code.REPLY_ALREADY_EXISTED;
        }
    // 存入数据
    // 流水号 -> 投保反馈单
        allReplyMap[sequenceCode] = Reply(sequenceCode, 1, insuranceStatusCode, detail);
    // 状态 -> 流水号[]
        replyStatusMap[1].push(sequenceCode);
        return Code.SUCCESS;
    }


/**
* 更新投保反馈单状态
*
* $param sequenceCode 流水号
* $param status 投保反馈单状态
*
* $return code 结果代码
*/
    function updateReply(bytes32 sequenceCode, int status) returns (Code code) {
    // 调用者需已注册且有效
        if (!isValidUser()) {
            return Code.INVALID_USER;
        }
    // 投保申请单需存在
        if (!isReplyAlreadyCreated(sequenceCode)) {
            return Code.REPLY_DO_NOT_EXISTE;
        }
        int replyStatus = allReplyMap[sequenceCode].status;
    // 如果新状态与原状态相同，返回投保申请单已经更新过
        if (replyStatus == status) {
            return Code.REPLY_STATUS_WAS_ALREADY_UPDATED;
        }
    // 如果投保反馈单状态为1（保险服务商新建投保反馈单成功）
        if (replyStatus == 1) {
            if (status == 2 || status == - 1) {
                if (users[msg.sender].role == IFC) {
                    updateReplyStatusDatabase(sequenceCode, replyStatus, status);
                    return Code.SUCCESS;
                }
                else {
                    return Code.PERMISSION_DENIED;
                }
            }
            else {
                return Code.REPLY_STATUS_UPDATE_ILLEGAL;
            }
        }
    // 如果投保反馈单状态为2（国金平台接收投保反馈单成功）
        if (replyStatus == 2) {
            if (status == 4) {
                if (users[msg.sender].role == IFC) {
                    updateReplyStatusDatabase(sequenceCode, replyStatus, status);
                    return Code.SUCCESS;
                }
                else {
                    return Code.PERMISSION_DENIED;
                }
            }
            else {
                return Code.REPLY_STATUS_UPDATE_ILLEGAL;
            }
        }
    // 如果投保反馈单状态为4（国金平台转发投保反馈单成功）
        if (replyStatus == 4) {
            if (status == 8 || status == - 2) {
                if (users[msg.sender].role == BM) {
                    updateReplyStatusDatabase(sequenceCode, replyStatus, status);
                    return Code.SUCCESS;
                }
                else {
                    return Code.PERMISSION_DENIED;
                }
            }
            else {
                return Code.REPLY_STATUS_UPDATE_ILLEGAL;
            }
        }
    // 如果投保反馈单状态为-1或-2（投保反馈单数据不一致）
        if (replyStatus == - 1 || replyStatus == - 2) {
            return Code.REPLY_STATUS_UPDATE_ILLEGAL;
        }
        return Code.REPLY_DO_NOT_EXISTE;
    }

/**
* IFC根据流水号查询保单和反馈单详情
*
* $param sequenceCode 流水号
*
* $return code 结果代码
* $return insuranceBillDetail 投保申请单详情
* $return insuranceBillStatus 投保申请单状态
* $return replyDetail 投保反馈单详情
* $return replyStatus 投保反馈单状态
* $return replySubStatus 投保反馈单二级状态
*/
    function queryBillAndReplyForIFC(bytes32 sequenceCode) returns (Code code, bytes insuranceBillDetail, int insuranceBillStatus, bytes replyDetail, int replyStatus, int8 replySubStatus){
    // 公钥未注册
        if (!isValidUser()) {
            return (Code.INVALID_USER, insuranceBillDetail, insuranceBillStatus, replyDetail, replyStatus, replySubStatus);
        }
    // 权限不是IFC
        if (!isUserRole(IFC)) {
            return (Code.PERMISSION_DENIED, insuranceBillDetail, insuranceBillStatus, replyDetail, replyStatus, replySubStatus);
        }
    // 保单不存在
        if (!isBillAlreadyCreated(sequenceCode)) {
            return (Code.BILL_DO_NOT_EXISTE, insuranceBillDetail, insuranceBillStatus, replyDetail, replyStatus, replySubStatus);
        }
    // 如果投保申请单状态是已收到，则返回投保申请单和投保反馈单的状态和详情
        if (allBillMap[sequenceCode].status == 8) {
            insuranceBillStatus = allBillMap[sequenceCode].status;
            insuranceBillDetail = allBillMap[sequenceCode].detail;
            replyStatus = allReplyMap[sequenceCode].status;
            replyDetail = allReplyMap[sequenceCode].detail;
            replySubStatus = allReplyMap[sequenceCode].insuranceStatusCode;
            code = Code.SUCCESS;
            return (code, insuranceBillDetail, insuranceBillStatus, replyDetail, replyStatus, replySubStatus);
        }
        //否则仅返回投保申请单的状态和详情
        else {
            insuranceBillStatus = allBillMap[sequenceCode].status;
            insuranceBillDetail = allBillMap[sequenceCode].detail;
            code = Code.SUCCESS;
            return (code, insuranceBillDetail, insuranceBillStatus, "", 0, 0);
        }
    }

/**
* BM根据流水号查询保单和反馈单详情
*
* $param sequenceCode 流水号
*
* $return code 结果代码
* $return insuranceBillDetail 投保申请单详情
* $return insuranceBillStatus 投保申请单状态
* $return replyDetail 投保反馈单详情
* $return replyStatus 投保反馈单状态
* $return replySubStatus 投保反馈单二级状态
*/
    function queryBillAndReplyForBM(bytes32 sequenceCode) returns (Code code, bytes insuranceBillDetail, int insuranceBillStatus, bytes replyDetail, int replyStatus, int8 replySubStatus){
    //公钥未注册
        if (!isValidUser()) {
            return (Code.INVALID_USER, "", 0, "", 0, 0);
        }
    //权限不是BM
        if (!isUserRole(BM)) {
            return (Code.PERMISSION_DENIED, "", 0, "", 0, 0);
        }
    // 保单不存在
        if (!isBillAlreadyCreated(sequenceCode)) {
            return (Code.BILL_DO_NOT_EXISTE, "", 0, "", 0, 0);
        }
    //查询详情
    //保单status为8：保单和反馈单信息
        if (allBillMap[sequenceCode].status == 8) {
            insuranceBillStatus = allBillMap[sequenceCode].status;
            insuranceBillDetail = allBillMap[sequenceCode].detail;
        //反馈单status为1、2、-1：反馈单仅返回状态
            if (allReplyMap[sequenceCode].status == 1 || allReplyMap[sequenceCode].status == 2 || allReplyMap[sequenceCode].status == - 1) {
                replyStatus = allReplyMap[sequenceCode].status;
            }
            //反馈单status为4、8、-2：反馈单返回详情、状态、二级状态
            else if (allReplyMap[sequenceCode].status == 4 || allReplyMap[sequenceCode].status == 8 || allReplyMap[sequenceCode].status == - 2) {
                replyStatus = allReplyMap[sequenceCode].status;
                replyDetail = allReplyMap[sequenceCode].detail;
                replySubStatus = allReplyMap[sequenceCode].insuranceStatusCode;
            }
            code = Code.SUCCESS;
            return (code, insuranceBillDetail, insuranceBillStatus, replyDetail, replyStatus, replySubStatus);
        }
        //保单status为1、2、4、-1、-2：仅保单信息
        else {
            insuranceBillStatus = allBillMap[sequenceCode].status;
            insuranceBillDetail = allBillMap[sequenceCode].detail;
            code = Code.SUCCESS;
            return (code, insuranceBillDetail, insuranceBillStatus, replyDetail, replyStatus, replySubStatus);
        }
    }
/**
* OJ根据流水号查询保单和反馈单详情
*
* $param sequenceCode 流水号
*
* $return code 结果代码
* $return insuranceBillDetail 投保申请单详情
* $return insuranceBillStatus 投保申请单状态
* $return replyDetail 投保反馈单详情
* $return replyStatus 投保反馈单状态
* $return replySubStatus 投保反馈单二级状态
*/
    function queryBillAndReplyForOJ(bytes32 sequenceCode) returns (Code code, bytes insuranceBillDetail, int insuranceBillStatus, bytes replyDetail, int replyStatus, int8 replySubStatus){
    //公钥未注册
        if (!isValidUser()) {
            return (Code.INVALID_USER, insuranceBillDetail, 0, replyDetail, 0, replySubStatus);
        }
    //权限不是OJ
        if (!isUserRole(OJ)) {
            return (Code.PERMISSION_DENIED, insuranceBillDetail, 0, replyDetail, 0, replySubStatus);
        }
    // 保单不存在
        if (!isBillAlreadyCreated(sequenceCode)) {
            return (Code.BILL_DO_NOT_EXISTE, "", 0, "", 0, 0);
        }
    //查询详情
    //保单status为1、2、-1：仅保单状态
        if (allBillMap[sequenceCode].status == 1 || allBillMap[sequenceCode].status == 2 || allBillMap[sequenceCode].status == - 1) {
            insuranceBillStatus = allBillMap[sequenceCode].status;
            code = Code.SUCCESS;
            return (code, insuranceBillDetail, insuranceBillStatus, replyDetail, replyStatus, replySubStatus);
        }
    //保单status为4、-2：保单状态、详情
        if (allBillMap[sequenceCode].status == 4 || allBillMap[sequenceCode].status == - 2) {
            insuranceBillStatus = allBillMap[sequenceCode].status;
            insuranceBillDetail = allBillMap[sequenceCode].detail;
            code = Code.SUCCESS;
            return (code, insuranceBillDetail, insuranceBillStatus, replyDetail, replyStatus, replySubStatus);
        }
        //保单status为8：保单和反馈单信息
        else {
            insuranceBillStatus = allBillMap[sequenceCode].status;
            insuranceBillDetail = allBillMap[sequenceCode].detail;
            replyStatus = allReplyMap[sequenceCode].status;
            replyDetail = allReplyMap[sequenceCode].detail;
            replySubStatus = allReplyMap[sequenceCode].insuranceStatusCode;
            code = Code.SUCCESS;
            return (code, insuranceBillDetail, insuranceBillStatus, replyDetail, replyStatus, replySubStatus);
        }
    }

/**
* 国金平台根据物流交换码查询所有该用户相关流水号列表
*
* $param logisticsExchangeCode 物流交换码
*
* $return code 结果代码
* $return insuranceBillDetail 投保申请单详情
* $return insuranceBillStatus 投保申请单状态
* $return replyDetail 投保反馈单详情
* $return replyStatus 投保反馈单状态
* $return replySubStatus 投保反馈单二级状态
*/
    function queryBillsForIFCByLogisticsExchangeCode(bytes16 logisticsExchangeCode) returns (Code code, bytes32[]){
        sequenceCodeList.length = 0;
    // 公钥未注册
        if (!isValidUser()) {
            return (Code.INVALID_USER, sequenceCodeList);
        }
    // 权限IFC
        if (!isUserRole(IFC)) {
            return (Code.PERMISSION_DENIED, sequenceCodeList);
        }
    // 物流交换码是IFC，返回所有流水号
        if (logisticsExchangeCode == "chairperson") {
            return (Code.SUCCESS, sequenceCodeArray);
        }
        return (Code.SUCCESS, userBillMap[users[msg.sender].logisticsExchangeCode]);
    }
/**
* BM或OJ查询所有自己相关的流水号列表
*
* $param logisticsExchangeCode 物流交换码
*
* $return code 结果代码
* $return insuranceBillDetail 投保申请单详情
* $return insuranceBillStatus 投保申请单状态
* $return replyDetail 投保反馈单详情
* $return replyStatus 投保反馈单状态
* $return replySubStatus 投保反馈单二级状态
*/
    function queryBillsForBMAndOj() returns (Code code, bytes32[]){
        sequenceCodeList.length = 0;
    // 公钥未注册
        if (!isValidUser()) {
            return (Code.INVALID_USER, sequenceCodeList);
        }
    // 权限不是BM或Oj
        if (!(isUserRole(BM) || isUserRole(OJ))) {
            return (Code.PERMISSION_DENIED, sequenceCodeList);
        }
        return (Code.SUCCESS, userBillMap[users[msg.sender].logisticsExchangeCode]);
    }
}
