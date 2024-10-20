package com.shoux_kream.admin.dto.request;

import com.shoux_kream.user.entity.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
@Getter
public class AdminUserUpdateRequest {
    @NotNull(message = "유저 id가 없습니다.")
    private Long userId;
    @NotNull(message = "권한이 없습니다.")
    private Role userRole;
}
