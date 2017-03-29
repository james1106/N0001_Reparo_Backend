package com.hyperchain.test;

/**
 * Created by martin on 2017/3/20.
 */
public class TestData {
    /**
     * 国金平台私钥，每次跑新的合约需要更新
     */
    public static final String IFCPRIVATEKEY = "{\"address\":\"05e603bcfb89488949472a29d483f24f73b36ee1\",\"encrypted\":\"b8d419d1855dab99f686b9382d19d5755845f9b69351eddf296cc25f81a13b54c6f431ea0e0e22ac\",\"version\":\"1.0\",\"algo\":\"0x02\"}";

    /**
     * 物流企业私钥，每次跑新的合约，重新注册账号后更新私钥
     */
    public static final String BM001PrivateKey = "{\"address\":\"5a787980b8719bf7e28f61d0eef28d69a91b1b17\",\"encrypted\":\"81cdd0c1e307ea2704c719c3e5c2af56166d7aadde3b9454bc1f358d2b667a96c6f431ea0e0e22ac\",\"version\":\"1.0\",\"algo\":\"0x02\"}";

    // 物流企业交换码（密码）
    public static final String BM001LogisticExchangeCode = "M2017032800001";

    /**
     * 保险服务商私钥，每次跑新的合约，重新注册账号后更新私钥
     */
    public static final String OJ001PrivateKey = "{\"address\":\"f3df42a3e56fc8999175e2b5f2125b364eb7e8c3\",\"encrypted\":\"c7cf514f95c508ab45aaac2262fa92c2748103cd0007e582dff4e265e6c715a5c6f431ea0e0e22ac\",\"version\":\"1.0\",\"algo\":\"0x02\"}";
    // 保险服务商交换码
    public static final String OJ001LogisticExchangeCode = "J2017032800001";


    // 投保申请单JSON详情
    public static final String SequenceCode001Bill = "{\"SequenceCode\":\"12345678\",\"InsuranceBillCode\":\"12345678\",\"PartyInformation\":[{\"PartyFunctionCode\":\"BM\",\"LogisticsExchangeCode\":\"M2017032800001\",\"PartyName\":\"物流公共信息平台\",\"PersonalIdentityDocument\":\"\"},{\"PartyFunctionCode\":\"BN\",\"LogisticsExchangeCode\":\"N2017032800001\",\"PartyName\":\"保险受益人\",\"PersonalIdentityDocument\":\"\"},{\"PartyFunctionCode\":\"OJ\",\"LogisticsExchangeCode\":\"J2017032800001\",\"PartyName\":\"服务提供商\",\"PersonalIdentityDocument\":\"\"},{\"PartyFunctionCode\":\"BR\",\"LogisticsExchangeCode\":\"R2017032800001\",\"PartyName\":\"保险人\",\"PersonalIdentityDocument\":\"\"},{\"PartyFunctionCode\":\"SP\",\"LogisticsExchangeCode\":\"P2017032800001\",\"PartyName\":\"软件服务商\",\"PersonalIdentityDocument\":\"\"}],\"ChargeInformation\":[{\"InsureDateTime\":\"20161214110901\",\"InsuranceCoverageName\":\"\",\"InsuranceCoverageCode\":\"101\",\"InsuranceCoverageTypeCode\":\"\",\"InsurableValue\":\"\",\"SumInsuredMonetaryAmount\":\"1111.111\",\"ChargeTypeCode\":\"\",\"Rate\":\"\",\"MonetaryAmount\":\"1111\",\"PriceCurrencyCode\":\"\"}],\"InvoiceInformation\":[{\"CodeListQualifier\":\"\",\"PartyName\":\"1\",\"TaxpayerIdentifyNumber\":\"\"},{\"CodeListQualifier\":\"\",\"PartyName\":\"2\",\"TaxpayerIdentifyNumber\":\"\"}],\"TransportInformation\":[{\"OriginalDocumentNumber\":\"\",\"VehicleClassification\":\"\",\"VehicleClassificationCode\":\"\",\"VehicleNumber\":\"\",\"TrailerVehiclePlateNumber\":\"\",\"ModeOfTransport\":\"\",\"TransportModeCode\":\"\",\"Remark\":\"\",\"PlaceOrLocationInformation\":[{\"PlaceLocationQualifier\":\"\",\"PlaceOrLocation\":\"\",\"CountrySubdivisionCode\":\"\",\"PostalIdentificationCode\":\"\"}],\"GoodsInformation\":[{\"ShippingNoteNumber\":\"\",\"DescriptionOfGoods\":\"\",\"CargoTypeClassificationCode\":\"\",\"PackageType\":\"\",\"PackageTypeCode\":\"\",\"PackageQuantity\":\"\",\"ItemGrossWeight\":{\"GoodsItemGrossWeight\":\"\",\"MeasurementUnitCode\":\"\"},\"ItemVolume\":{\"Cube\":\"\",\"MeasurementUnitCode\":\"\"}}]}]}";
    // 投保申请单流水号
    public static final String insuranceBillSequenceCode = "12345678";

    // 投保反馈单JSON详情
    public static final String SequenceCode001Reply = "{\"OriginalDocumentNumber\":[\"11111111\",\"22222222\"],\"SequenceCode\":\"12345678\",\"InsuranceBillCode\":\"\",\"InsuranceStatusCode\":\"3101\",\"ErrorDescription\":\"\",\"StartTime\":\"\",\"EndTime\":\"\",\"PartyInformation\":[{\"PartyName\":\"LOGINK\",\"PartyIdentifier\":\"\",\"PartyFunctionCode\":\"OJ\",\"PersonalIdentityDocument\":\"\"}],\"ChargeInformation\":[{\"InsureDateTime\":\"\",\"InsuranceCoverageName\":\"\",\"InsuranceCoverageCode\":\"\",\"InsuranceCoverageTypeCode\":\"\",\"SumInsuredMonetaryAmount\":\"\",\"ChargeTypeCode\":\"\",\"Rate\":\"\",\"MonetaryAmount\":\"\",\"PriceCurrencyCode\":\"\"}],\"LimitOfRecovery\":[{\"MonetaryAmount\":\"\",\"PriceCurrencyCode\":\"\",\"FreeText\":\"\"}],\"FranchiseClause\":[{\"MonetaryAmount\":\"\",\"PriceCurrencyCode\":\"\",\"Franchise\":\"\",\"FreeText\":\"\"}],\"NetworkAccessAddress\":\"\",\"Remark\":\"\"}";
    // 投保申请单流水号
    public static final String replySequenceCode = "12345678";
}
