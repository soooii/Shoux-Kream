package com.shoux_kream.admin.service;

import com.shoux_kream.admin.dto.request.UserLogCreateRequest;
import com.shoux_kream.admin.dto.request.UserLogUpdateRequest;
import com.shoux_kream.admin.dto.response.UserLogResponse;
import com.shoux_kream.admin.entity.UserLog;
import com.shoux_kream.admin.repository.UserLogRepository;
import com.shoux_kream.exception.ErrorCode;
import com.shoux_kream.exception.KreamException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserLogService {

    private final UserLogRepository userLogRepository;

    public ResponseEntity<List<UserLogResponse>> getUserLogList() {
        List<UserLog> userLogsEntity = userLogRepository.findAll();
        List<UserLogResponse> userLogsResponse  = userLogsEntity.stream().map(UserLogResponse::new).collect(Collectors.toList());
        return new ResponseEntity<>(userLogsResponse, HttpStatus.OK);
    }

    public ResponseEntity<Void> createUserLog(UserLogCreateRequest userLogCreateRequest) {
        UserLog userLog = userLogCreateRequest.toCreateEntity();
        userLogRepository.save(userLog);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Void> updateUserLog(UserLogUpdateRequest userLogUpdateRequest) {
        UserLog userLog = userLogRepository.findById(userLogUpdateRequest.getUserLogId()).orElseThrow(() -> new KreamException(ErrorCode.INVALID_ID));
        userLogUpdateRequest.toUpdateEntity(userLog);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<Void> deleteUserLog(List<Long> userLogIds) {
        //TODO 클라이언트에서 넘어온 userLogIds 중에 테이블에 값이 없어서 삭제가 안되도 200(성공)으로 떨어지기 때문에 해당 에러처리 필요함
        userLogRepository.deleteAllByIds(userLogIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
