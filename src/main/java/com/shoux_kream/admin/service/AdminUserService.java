package com.shoux_kream.admin.service;

import com.shoux_kream.admin.dto.response.AdminUserResponse;
import com.shoux_kream.admin.repository.AdminUserRepository;
import com.shoux_kream.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final AdminUserRepository adminUserRepository;
    //전체 회원 조회
    public ResponseEntity<List<AdminUserResponse>> getAllUser() {
        List<User> userEntity = adminUserRepository.findAll();
        List<AdminUserResponse> userResponse  = userEntity.stream().map(AdminUserResponse::new).collect(Collectors.toList());
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
