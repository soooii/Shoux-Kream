package com.shoux_kream.item.dto.response;

import java.util.List;

public record ItemsGetResponse(
        int size,
        List<ItemResponse> itemList
) {
}