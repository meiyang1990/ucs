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
import com.netfinworks.basis.inf.ucs.support.annotation.CacheIncrease;

/**
 * <p>注释</p>
 * @author huipeng
 * @version $Id: CacheIncreaseAdvice.java, v 0.1 Jan 8, 2014 6:30:44 PM knico Exp $
 */
@Aspect
@Order(1000)
public class CacheIncreaseAdvice<T> extends CacheAdviceBase<T> {
    @Pointcut("@annotation(com.netfinworks.basis.inf.ucs.support.annotation.CacheIncrease)")
    public void cacheIncrease() {
    }

    @Around("cacheIncrease()")
    public Object aroundIncreaseCache(ProceedingJoinPoint pjp) throws Throwable {
        // 从pjp中初始化CacheAnnotationData
        long startTime = System.currentTimeMillis();

        String key = null;
        CacheAnnotationData cad = null;
        Object[] args = pjp.getArgs();
        Object ret = null;
        Long result = -1L;
        try {
            Method currentMethod = getCurrentMethod(pjp);
            Annotation annotation = getMethodAnnotation(currentMethod, CacheIncrease.class);
            cad = buildCacheAnnotationData(pjp, currentMethod, annotation);
            key = cad.buildKey();
            long delta = (Long) args[cad.getDeltaValueParamIndex()];
            CacheRespone<Long> resp = getCache().incr(key, delta);
            if (resp != null && resp.isSuccess()) {
                result = resp.get();
            } else {
                getLogger().error("cache cant increase: key = {}", key);
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
            getLogger().debug("increase cache invoke consume : {} ms, key:{}",
                new Object[] { endTime - startTime, key });
        }
        return ret;
    }

}