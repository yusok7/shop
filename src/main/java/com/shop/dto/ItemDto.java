package com.shop.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {

    private Long id;
    private String itemNm;
    private Integer price;
    private String itemDetail;
    private String sellStatCd;

    @Builder
    public ItemDto(String itemNm, Integer price, String itemDetail) {
        this.itemNm = itemNm;
        this.price = price;
        this.itemDetail = itemDetail;
    }
}
