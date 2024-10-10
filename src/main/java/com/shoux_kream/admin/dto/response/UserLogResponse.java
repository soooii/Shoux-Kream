package com.shoux_kream.admin.dto.response;

import com.shoux_kream.admin.entity.UserLog;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserLogResponse {
    private String userName;
    private String requestUrl;
    private String requestMethod;
    private String clientIp;
    private int responseStatus;
    private LocalDateTime responseTime;
    private LocalDateTime requestTime;

    public UserLogResponse(UserLog entity) {
        this.userName = entity.getUserName();
        this.requestUrl = entity.getRequestUrl();
        this.requestMethod = entity.getRequestMethod();
        this.clientIp = entity.getClientIp();
        this.responseStatus = entity.getResponseStatus();
        this.responseTime = entity.getResponseTime();
        this.requestTime = entity.getRequestTime();
    }

}
