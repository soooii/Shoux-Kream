package com.shoux_kream.common.aop;


import com.shoux_kream.admin.entity.UserLog;
import com.shoux_kream.admin.repository.UserLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class UserLoggingAspect {

    private final UserLogRepository userLogRepository;

    @Pointcut("execution(* com.shoux_kream..controller..*(..)) && !@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void userLoggerPointCut() {}

    @Around("userLoggerPointCut()")
    public Object methodAdminLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // request 정보 가져온다.
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 유저 정보 가져온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        String url = request.getRequestURI();
        String httpMethod = request.getMethod();
        String ip = request.getRemoteAddr();

        UserLog adminRequest = UserLog.builder()
                        .userName(username)
                        .requestUrl(url)
                        .requestMethod(httpMethod)
                        .clientIp(ip)
                        .requestTime(LocalDateTime.now())
                        .build();

        // 1. 요청값을 저장한다.
        UserLog savedLog = userLogRepository.save(adminRequest);

        try {
            Object result = proceedingJoinPoint.proceed();

            // ResponseEntity가 아닌 경우에 대비해
            int statusCode = HttpStatus.OK.value(); // 기본 상태 코드를 200으로 설정

            if (result instanceof ResponseEntity) {
                ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
                statusCode = responseEntity.getStatusCodeValue();
            }

            savedLog.setResponseStatus(statusCode);
            savedLog.setResponseTime(LocalDateTime.now());

            // 2. 응답(성공)값을 업데이트 한다.
            userLogRepository.save(savedLog);
            return result;
        } catch (Exception e) {
            savedLog.setResponseStatus(exceptionToStatus(e).value());
            savedLog.setResponseTime(LocalDateTime.now());

            // 3. 응답(실패)값을 업데이트 한다.
            userLogRepository.save(savedLog);
            throw e;
        }
    }

    private HttpStatus exceptionToStatus(Exception e) {
        return HttpStatus.INTERNAL_SERVER_ERROR;  // TODO 현재는 일단 500 세팅 -> 나중에 403, 400 등등 처리필요하면 수정 해야함
    }
}
