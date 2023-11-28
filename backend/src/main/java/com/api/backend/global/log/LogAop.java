package com.api.backend.global.log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAop {

  private static final Logger log = LoggerFactory.getLogger(LogAop.class);

  @Pointcut(value = "execution(* com.api.backend..*.*(..))")
  private void cut() {
  }

  @Before("cut()")
  public void beforeParameterLog(JoinPoint joinPoint) {
    Method method = getMethod(joinPoint);
    StringBuffer sb = new StringBuffer();

    sb.append("method start : ").append(method.getName())
        .append(" ");
    Object[] args = joinPoint.getArgs();

    if (args.length <= 0) {
      sb.append("[no parameter]");
      log.info(sb.toString());
      return;
    }

    for (Object arg : args) {
      sb.append("[parameter type : ")
          .append(arg.getClass().getSimpleName())
          .append(" value : ").append(arg).append("] ");
    }
    log.info(sb.toString());
  }

  // Poincut에 의해 필터링된 경로로 들어오는 경우 메서드 리턴 후에 적용
  @AfterReturning(value = "cut()", returning = "returnObj")
  public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {
    // 메서드 정보 받아오기
    Method method = getMethod(joinPoint);

    if (Objects.isNull(returnObj)) {
      log.info("method end : {} [return : void]",
          method.getName());
      return;
    }
    log.info("method end : {} [return type : {} / value : {}]",
        method.getName(),
        returnObj.getClass().getSimpleName(),
        returnObj);
  }

  // JoinPoint로 메서드 정보 가져오기
  private Method getMethod(JoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    return signature.getMethod();
  }
}
