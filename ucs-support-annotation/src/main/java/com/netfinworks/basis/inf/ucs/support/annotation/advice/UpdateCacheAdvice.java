/*
 * Copyright 2013 weibopay.com, Inc. All rights reserved.
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
import com.netfinworks.basis.inf.ucs.support.annotation.UpdateCache;

/**
 * @author knico
 * @since Jul 30, 2013
 * 
 */
@Aspect
@Order(1000)
public class UpdateCacheAdvice<T> extends CacheAdviceBase<T> {
    @Pointcut("@annotation(com.netfinworks.basis.inf.ucs.support.annotation.UpdateCache)")
    public void updateCache() {
    }

    @SuppressWarnings("unchecked")
    @Around("updateCache()")
    public Object aroundUpdateCache(ProceedingJoinPoint pjp) throws Throwable {
        // 从pjp中初始化CacheAnnotationData
        long startTime = System.currentTimeMillis();

        T ret = null;
        CacheRespone<T> resp = null;
        String key = null;
        CacheAnnotationData cad = null;
        try {
            Method currentMethod = getCurrentMethod(pjp);
            Annotation annotation = getMethodAnnotation(currentMethod, UpdateCache.class);
            cad = buildCacheAnnotationData(pjp, currentMethod, annotation);
            key = cad.buildKey();
            resp = getCache().get(key);
            if (resp != null && resp.isSuccess()) {
                ret = resp.get();
            }
        } catch (Throwable t) {
            getLogger().warn("缓存切面内部错误，无法从缓存获取数据！", t);
        }
        Object[] args = pjp.getArgs();
        if (ret == null) {
            getLogger().debug("cache not hinted: key = {}", key);
        }
        if (cad.getLastCacheValueParamIndex() < 0) {
            getLogger().debug("no last cache value parameter found!");
        } else {
            args[cad.getLastCacheValueParamIndex()] = (ret == null) ? cad.getDefaultCacheValue()
                : ret;
        }
        ret = (T) pjp.proceed(args);
        if (ret != null) {
            try {
                int exp = cad.getExpireSecond();
                exp = exp == -1 ? CacheDefaultConfig.getInstance().getDefaultExpireSecond() : exp;
                getCache().set(key, ret, exp);
            } catch (Throwable t) {
                getLogger().warn("缓存切面内部错误，无法缓存数据！", t);
            }
        } else {
            try {
                getLogger().info("返回数据为空，删除缓存：{}", key);
                getCache().delete(key);
            } catch (Throwable t) {
                getLogger().warn("缓存切面内部错误，无法删除数据！", t);
            }
        }
        if (getLogger().isDebugEnabled()) {
            long endTime = System.currentTimeMillis();
            getLogger().debug("update cache invoke consume : {} ms, key:{}",
                new Object[] { endTime - startTime, key });
        }
        return ret;
    }

}
