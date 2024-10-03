package com.shoux_kream.item.dto.request;

import com.shoux_kream.item.entity.ItemInfo;

public record ItemSaveRequest(Long brandId, String name, ItemInfo itemInfo, int size) {
}
