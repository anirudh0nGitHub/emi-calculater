package com.anirudh.de.config;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author Anirudh
 */
@Aspect
@Component
public class LoggingAndPerformance {

  private static final Logger log = LogManager.getLogger(LoggingAndPerformance.class);

  /*
   Performance loging eg.-  Logging of execution time for various methods.
   */
  @Around("execution(* com.anirudh.de..*(..)))")
  public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

    String className = methodSignature.getDeclaringType().getSimpleName();
    String methodName = methodSignature.getName();

    log.info("Entering" + className + "." + methodName + " :: " + " with Arguments " + methodName);
    final StopWatch stopWatch = new StopWatch();

    //Execution time Measurement for the method
    stopWatch.start();
    Object result = proceedingJoinPoint.proceed();
    stopWatch.stop();

    //Log the measured time
    log.info("Execution time of " + className + "." + methodName + " :: " + stopWatch.getTime(
      TimeUnit.MILLISECONDS) + " ms");

    return result;
  }

  /**
   * Pointcut for the relevant spring boot packages
   */
  @Pointcut("within(@org.springframework.stereotype.Repository *)" +
    " || within(@org.springframework.stereotype.Service *)" +
    " || within(@org.springframework.web.bind.annotation.RestController *)")
  public void springBeanPointcut() {
    // Method serves for the sake of pointcut declaration.
  }

  /**
   * Pointcut for the beans in the project
   */
  @Pointcut("within(com.anirudh.de.BaufinanzierungApplication..*)" +
    " || within(com.anirudh.de.service..*)" +
    " || within(com.anirudh.de.web.rest.*)")
  public void applicationPackagePointcut() {
    // Method is empty as this is just a Pointcut, the implementations are in the advices.
  }

  /**
   * Advice that logs methods throwing exceptions.
   *
   * @param joinPoint join point for advice
   * @param e         exception
   */
  @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
  public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
    log.error("Exception in {}.{}() with cause = {}",
      joinPoint.getSignature().getDeclaringTypeName(),
      joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
  }

  /**
   * Advice that logs when a method is entered and exited.
   *
   * @param joinPoint join point for advice
   * @return result
   * @throws Throwable throws IllegalArgumentException
   */
  @Around("applicationPackagePointcut() && springBeanPointcut()")
  public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    log
      .info("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
    try {
      Object result = joinPoint.proceed();
      log.info("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName(), result);
      return result;
    } catch (IllegalArgumentException e) {
      log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
        joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
      throw e;
    }
  }
}
