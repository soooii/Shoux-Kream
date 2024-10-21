package com.shoux_kream.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ItemDto {

//    @NotNull(message = "브랜드 ID는 필수 입력 값입니다.")
//    private Long brandId;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String title;

//    @NotNull(message = "카테고리 ID는 필수 입력 값입니다.")
//    private Long categoryId;

    @NotBlank(message = "제조사는 필수 입력 값입니다.")
    private String manufacturer;

    @NotBlank(message = "짧은 설명은 필수 입력 값입니다.")
    private String shortDescription;

    private String detailDescription;

    @NotBlank(message = "이미지 키는 필수 입력 값입니다.")
    private String imageKey;

    @Positive(message = "재고 수량은 양수여야 합니다.")
    private Integer inventory;

    @Positive(message = "가격은 양수여야 합니다.")
    private Integer price;

    private List<String> keyWords;
}
