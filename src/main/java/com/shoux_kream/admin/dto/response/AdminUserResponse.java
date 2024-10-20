package com.shoux_kream.admin.dto.response;

import com.shoux_kream.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminUserResponse {
    private Long userId;
    private String userEmail;
    private String userName;
    private String userNickName;
    private String userRole;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public AdminUserResponse(User entity) {
        this.userId = entity.getId();
        this.userEmail = entity.getEmail();
        this.userName = entity.getName();
        this.userNickName = entity.getNickname();
        this.userRole = entity.getRole().getValue().replace("ROLE_", "");
        this.createAt = entity.getCreatedAt();
        this.updateAt = entity.getUpdatedAt();
    }

}
