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

import com.netfinworks.basis.inf.ucs.support.annotation.CacheAnnotationData;
import com.netfinworks.basis.inf.ucs.support.annotation.InvalidateCache;
import com.netfinworks.basis.inf.ucs.support.annotation.InvalidateCacheMode;
import com.netfinworks.basis.inf.ucs.support.annotation.InvalidateExecuteOrder;

/**
 * Cache Result Advice
 * 
 * @author bigknife
 * 
 */
@Aspect
@Order(1000)
public class InvalidateCacheAdvice<T> extends CacheAdviceBase<T> {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Pointcut("@annotation(com.netfinworks.basis.inf.ucs.support.annotation.InvalidateCache)")
	public void invalidateCache() {
	}

	@Around("invalidateCache()")
	public Object aroundCacheResult(ProceedingJoinPoint pjp) throws Throwable {
		Method currentMethod = getCurrentMethod(pjp);
		Annotation annotation = getMethodAnnotation(currentMethod,
				InvalidateCache.class);
		CacheAnnotationData cad = buildCacheAnnotationData(pjp, currentMethod, annotation);
		
		InvalidateExecuteOrder order = cad.getInvalidateExecuteOrder();
		if(InvalidateExecuteOrder.BEFORE.equals(order)){
			Object t = raw(pjp);
			advice(cad);
			return t;
		}else{
			advice(cad);
			return raw(pjp);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Object raw(ProceedingJoinPoint pjp) throws Throwable{
		T  t = (T) pjp.proceed();
		return t;
	}
	public void advice(CacheAnnotationData cad){
		try {
			
			if(InvalidateCacheMode.MODE_SINGE.equals(cad.getMode())){
				String key = cad.buildKey();
				if(!getCache().delete(key)){
					log.warn("删除缓存失败,key={}",key);
				}
			}else if(InvalidateCacheMode.MODE_MULTI.equals(cad.getMode())){
				String keys [] = cad.buildKeys();
				for(String _key : keys ){
					getCache().delete(_key);
				}
			}else if(InvalidateCacheMode.MODE_FLUSHALL.equals(cad.getMode())){
				getCache().flush();
			}
		} catch (Throwable ex) {
			log.warn("缓存切面内部错误，无法从删除缓存数据！", ex);
		}
	}
}
