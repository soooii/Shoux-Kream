package com.shoux_kream.admin.service;

import com.shoux_kream.admin.dto.response.UserLogResponse;
import com.shoux_kream.admin.entity.UserLog;
import com.shoux_kream.admin.repository.UserLogRepositry;
import com.shoux_kream.cart.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserLogService {

    private final UserLogRepositry userLogRepositry;

    public ResponseEntity<List<UserLogResponse>> getUserLogList() {
        List<UserLog> userLogsEntity = userLogRepositry.findAll();
        List<UserLogResponse> userLogsResponse  = userLogsEntity.stream().map(UserLogResponse::new).collect(Collectors.toList());
        return new ResponseEntity<>(userLogsResponse, HttpStatus.OK);
    }
}
