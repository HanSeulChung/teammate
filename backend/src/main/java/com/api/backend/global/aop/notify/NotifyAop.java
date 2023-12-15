package com.api.backend.global.aop.notify;

import com.api.backend.notification.service.SendNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@EnableAsync
@RequiredArgsConstructor
public class NotifyAop {

  private final SendNotificationService sendNotificationService;

  @Pointcut("@annotation(com.api.backend.global.aop.notify.SendNotify)")
  public void pointCut() {
  }

  @Async
  @AfterReturning(pointcut = "pointCut()", returning = "result")
  public void checkNotify(JoinPoint joinPoint, Object result) {
    sendNotificationService.convertToDtoAndSendOrThrowsNotFoundClass(
        ((ResponseEntity) result).getBody()
    );
  }
}
