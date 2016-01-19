/**
 * Copyright 2014 netfinworks.com, Inc. All rights reserved.
 */
package com.netfinworks.basis.inf.ucs.support.annotation.advice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

import com.netfinworks.basis.inf.ucs.client.CacheRespone;
import com.netfinworks.basis.inf.ucs.support.annotation.CacheAnnotationData;
import com.netfinworks.basis.inf.ucs.support.annotation.CacheDecrease;

/**
 * <p>注释</p>
 * @author huipeng
 * @version $Id: CacheDecreaseAdvice.java, v 0.1 Jan 8, 2014 6:31:05 PM knico Exp $
 */
@Aspect
@Order(1000)
public class CacheDecreaseAdvice<T> extends CacheAdviceBase<T> {
    @Pointcut("@annotation(com.netfinworks.basis.inf.ucs.support.annotation.CacheDecrease)")
    public void cacheDecrease() {
    }

    @Around("cacheDecrease()")
    public Object aroundDecreaseCache(ProceedingJoinPoint pjp) throws Throwable {
        // 从pjp中初始化CacheAnnotationData
        long startTime = System.currentTimeMillis();

        String key = null;
        CacheAnnotationData cad = null;
        Object[] args = pjp.getArgs();
        Object ret = null;
        Long result = -1L;
        try {
            Method currentMethod = getCurrentMethod(pjp);
            Annotation annotation = getMethodAnnotation(currentMethod, CacheDecrease.class);
            cad = buildCacheAnnotationData(pjp, currentMethod, annotation);
            key = cad.buildKey();
            long delta = (Long) args[cad.getDeltaValueParamIndex()];
            CacheRespone<Long> resp = getCache().decr(key, delta);
            if (resp != null && resp.isSuccess()) {
                result = resp.get();
            } else {
                getLogger().error("cache cant decrease: key = {}", key);
            }
        } catch (Throwable t) {
            getLogger().warn("缓存切面内部错误，无法在缓存计数。", t);
        }
        if (cad.getDeltaResultParamIndex() >= 0) {
            args[cad.getDeltaResultParamIndex()] = result;
        }
        ret = pjp.proceed(args);
        if (getLogger().isDebugEnabled()) {
            long endTime = System.currentTimeMillis();
            getLogger().debug("decrease cache invoke consume : {} ms, key:{}",
                new Object[] { endTime - startTime, key });
        }
        return ret;
    }

}
