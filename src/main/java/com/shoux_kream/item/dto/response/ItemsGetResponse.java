package com.shoux_kream.item.dto.response;

import java.util.List;

public record ItemsGetResponse(
        Integer size,
        List<ItemResponse> itemList
) {
}