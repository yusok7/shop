package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 상품 데이터 정보를 전달하는 DTO
 */

@Getter @Setter
@NoArgsConstructor
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    // 상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    /**
     * 상품의 이미지 아이디를 저장하는 리스트
     * 상품 등록 시에는 아직 상품의 이미지를 저장하지 않았기 때문에 아무 값도 들어가 있지 않고,
     * 수정 시에 이미지 아이디를 담아둘 용도로 사용
     */
    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    @Builder
    public ItemFormDto(String itemNm, Integer price, String itemDetail, Integer stockNumber, ItemSellStatus itemSellStatus) {
        this.itemNm = itemNm;
        this.price = price;
        this.itemDetail = itemDetail;
        this.stockNumber = stockNumber;
        this.itemSellStatus = itemSellStatus;
    }

    public Item toEntity() {
        return Item.builder()
                .itemNm(itemNm)
                .price(price)
                .itemDetail(itemDetail)
                .stockNumber(stockNumber)
                .itemSellStatus(itemSellStatus)
                .build();
    }

    public static ItemFormDto of(Item item) {
        return modelMapper.map(item, ItemFormDto.class);
    }
}
