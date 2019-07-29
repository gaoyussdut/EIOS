package top.toptimus.timer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 统计时间aop
 *
 * @author gaoyu
 * @since 2018-12-03
 */
@Aspect
@Component
public class LogTimeInterceptor {

    private static Logger logger = LoggerFactory.getLogger(LogTimeInterceptor.class);

    @Pointcut("@annotation(top.toptimus.timer.LogExecuteTime)")
    public void logTimeMethodPointcut() {

    }

    @Around("logTimeMethodPointcut()")
    public Object interceptor(ProceedingJoinPoint pjp) {
        long startTime = System.currentTimeMillis();

        Object result;
        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        logger.info(pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName() + " spend " + (System.currentTimeMillis() - startTime) + "ms");

        return result;
    }
}