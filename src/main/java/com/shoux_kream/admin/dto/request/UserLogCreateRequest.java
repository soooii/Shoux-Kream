package com.shoux_kream.admin.dto.request;

import com.shoux_kream.admin.entity.UserLog;
import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class UserLogCreateRequest {

    private String clientIp;
    private String requestUrl;
    private String requestMethod;
    private int responseStatus;

    public UserLog toCreateEntity() {
        return new UserLog(requestUrl, requestMethod, clientIp, responseStatus);
    }
}
