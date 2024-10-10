package com.shoux_kream.admin.dto.request;

import com.shoux_kream.admin.entity.UserLog;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class UserLogUpdateRequest {

    private Long userLogId;
    private String clientIp;
    private String requestUrl;
    private String requestMethod;
    private int responseStatus;

    public UserLog toUpdateEntity(UserLog entity) {
        Optional.ofNullable(this.getClientIp()).ifPresent(entity::setClientIp);
        Optional.ofNullable(this.getRequestUrl()).ifPresent(entity::setRequestUrl);
        Optional.ofNullable(this.getRequestMethod()).ifPresent(entity::setRequestMethod);
        Optional.of(this.getResponseStatus()).ifPresent(entity::setResponseStatus);
        return entity;
    }
}
