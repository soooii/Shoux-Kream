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
    @Column(name = "admin_log_id")
    private Long adminLogId;
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

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public void setResponseTime(LocalDateTime responseTime) {
        this.responseTime = responseTime;
    }

}
