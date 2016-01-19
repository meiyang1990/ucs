/**
 * 
 */
package com.netfinworks.basis.inf.ucs.support.annotation.advice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import com.netfinworks.basis.inf.ucs.client.CacheRespone;
import com.netfinworks.basis.inf.ucs.support.annotation.CacheAnnotationData;
import com.netfinworks.basis.inf.ucs.support.annotation.CacheResult;

/**
 * Cache Result Advice
 * 
 * @author bigknife
 * 
 */
@Aspect
@Order(1000)
public class CacheResultAdvice<T> extends CacheAdviceBase<T> {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(com.netfinworks.basis.inf.ucs.support.annotation.CacheResult)")
    public void cacheResult() {
    }

    @SuppressWarnings("unchecked")
    @Around("cacheResult()")
    public Object aroundCacheResult(ProceedingJoinPoint pjp) throws Throwable {
        // 从pjp中初始化CacheAnnotationData
        long startTime = System.currentTimeMillis();

        T ret = null;
        CacheRespone<T> resp = null;
        String key = null;
        CacheAnnotationData cad = null;
        try {
            Method currentMethod = getCurrentMethod(pjp);
            Annotation annotation = getMethodAnnotation(currentMethod, CacheResult.class);
            cad = buildCacheAnnotationData(pjp, currentMethod, annotation);
            key = cad.buildKey();
            resp = getCache().get(key);
        } catch (Throwable t) {
            log.warn("缓存切面内部错误，无法从缓存获取数据！", t);
        }
        if (resp == null || (!resp.isSuccess()) || resp.get() == null) {
            log.debug("cache not hinted: key = {}", key);
            ret = (T) pjp.proceed();
            if (ret != null) {
                try {
                    int exp = cad.getExpireSecond();
                    exp = exp == -1 ? CacheDefaultConfig.getInstance().getDefaultExpireSecond()
                        : exp;
                    getCache().set(key, ret, exp);
                } catch (Throwable t) {
                    log.warn("缓存切面内部错误，无法缓存数据！", t);
                }
            }
        }else{
            ret = resp.get();
        }
        if (getLogger().isDebugEnabled()) {
            long endTime = System.currentTimeMillis();
            log.debug("cache result invoke consume : {} ms, key:{}", new Object[] {
                    endTime - startTime, key });
        }
        return ret;
    }
}
