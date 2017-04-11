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