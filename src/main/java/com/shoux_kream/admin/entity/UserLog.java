package com.shoux_kream.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_log_id")
    private Long userLogId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "request_url")
    private String requestUrl;
    @Column(name = "request_method")
    private String requestMethod;
    @Column(name = "client_ip")
    private String clientIp;
    @Column(name = "response_status")
    private int responseStatus;
    @Column(name = "response_time")
    private LocalDateTime responseTime;
    @Column(name = "request_time")
    private LocalDateTime requestTime;

    // 추가 dto
    public UserLog(String requestUrl, String requestMethod, String clientIp, int responseStatus) {
        this.requestUrl = requestUrl;
        this.requestMethod = requestMethod;
        this.clientIp = clientIp;
        this.responseStatus = responseStatus;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public void setResponseTime(LocalDateTime responseTime) {
        this.responseTime = responseTime;
    }

}
