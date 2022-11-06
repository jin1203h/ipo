package com.ipo.ipo.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IpoDTO {
    private Long ipoId;
    private Long memberId;
    private Long ipoStockId;
    private String ipoStockName;
    private String subscribeStartDate;
    private String subscribeEndDate;
    private String goPublicCategory;
    private Long parValue;
    private Long publicOfferingQuantity;
    private String Sector;
    private Long competition;
    private Long allocationQuantity;
    private Long totalSubscribeCount;
    private Long totalSubscribeQuantity;
}