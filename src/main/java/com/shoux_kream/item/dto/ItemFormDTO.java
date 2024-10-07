package com.shoux_kream.item.dto;

import com.shoux_kream.item.entity.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDTO {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    // 상품 판매 상태
//    private ItemSellStatus itemSellStatus;

//    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

//    private static ModelMapper modelMapper = new ModelMapper();
//
//    public Item createItem() {
//        return modelMapper.map(this, Item.class);
//    }
//
//    public static ItemFormDTO of(Item item){
//        return modelMapper.map(item,ItemFormDTO.class);
//    }
}