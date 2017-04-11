

contract ReparoAccount {

    enum RoleCode {COMPANY, LOGISTICS, STORAGE, BANK} //RCOMPANY融资企业, LOGISTICS物流公司,STORAGE仓储公司,BANK金融机构
    enum AccountStatus {VALID, INVALID,FROZEN} //账户状态，有效、无效、冻结

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


// 用户地址 => 结构体Account
    mapping (address => Account) accountMap;
// 账号acctId => 用户地址
    mapping (bytes32 => address) acctIdToAddress;

    function newAccount(bytes32 _accountName, bytes32 _enterpriseName, RoleCode _roleCode, AccountStatus _accountStatus, bytes32 _certType, bytes32 _certNo, bytes32[] _acctId, bytes32 _svcrClass, bytes32 _acctSvcr, bytes32 _acctSvcrName) returns (uint code){
        if(accountMap[msg.sender].accountName != ""){
            return 5002; //账户已存在
        }
        accountMap[msg.sender] = Account(msg.sender, _accountName, _enterpriseName, _roleCode, _accountStatus, _certType, _certNo, _acctId, _svcrClass, _acctSvcr, _acctSvcrName);
        for (uint i = 0; i < _acctId.length; i++){
            acctIdToAddress[_acctId[i]] = msg.sender;
        }
        return 0; //成功
    }

    function getAccount() returns (uint code, bytes32 _accountName, bytes32 _enterpriseName, RoleCode _roleCode, AccountStatus _accountStatus, bytes32 _certType, bytes32 _certNo, bytes32[] _acctId, bytes32 _class, bytes32 _acctSvcr, bytes32 _acctSvcrName){
        if(accountMap[msg.sender].accountName == ""){
            return (2, _accountName, _enterpriseName, _roleCode, _accountStatus, _certType, _certNo, _acctId, _class, _acctSvcr, _acctSvcrName); //账户不存在，该用户可能未注册或已失效
        }
        Account account = accountMap[msg.sender];
        return (0, account.accountName, account.enterpriseName, account.roleCode, account.accountStatus, account.certType, account.certNo, account.acctId, account.svcrClass, account.acctSvcr, account.acctSvcrName); //成功
    }

}