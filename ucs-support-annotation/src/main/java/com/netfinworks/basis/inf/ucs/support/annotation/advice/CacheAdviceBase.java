/**
 * 
 */
package com.netfinworks.basis.inf.ucs.support.annotation.advice;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.Collection;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netfinworks.basis.inf.ucs.client.UCache;
import com.netfinworks.basis.inf.ucs.support.annotation.CacheAnnotationData;
import com.netfinworks.basis.inf.ucs.support.annotation.DeltaResult;
import com.netfinworks.basis.inf.ucs.support.annotation.DeltaValue;
import com.netfinworks.basis.inf.ucs.support.annotation.InvalidateCacheMode;
import com.netfinworks.basis.inf.ucs.support.annotation.InvalidateExecuteOrder;
import com.netfinworks.basis.inf.ucs.support.annotation.KeyGenerator;
import com.netfinworks.basis.inf.ucs.support.annotation.LastCacheValue;

/**
 * @author bigknife
 * 
 */
public class CacheAdviceBase<T> {
    private Logger    log = LoggerFactory.getLogger(getClass());
    private UCache<T> uCache;

    /**
     * @param uCache the uCache to set
     */
    public void setCache(UCache<T> uCache) {
        this.uCache = uCache;
    }

    /**
     * @return the uCache
     */
    public UCache<T> getCache() {
        return uCache;
    }

    protected Logger getLogger() {
        return log;
    }

    //private Map<String, CacheAnnotationData> cacheAnnoDataMap = new HashMap<String, CacheAnnotationData>();
    protected CacheAnnotationData buildCacheAnnotationData(ProceedingJoinPoint pjp,
                                                           Method currentMethod,
                                                           Annotation annotation) throws Throwable {
        /*
         * String cacheAnnoDataKey = currentMethod.toGenericString()+pjp.getTarget().hashCode();
         * CacheAnnotationData cad = cacheAnnoDataMap.get(cacheAnnoDataKey); if(cad != null){ return
         * cad; }
         */

        CacheAnnotationData cad = new CacheAnnotationData();
        //cacheAnnoDataMap.put(cacheAnnoDataKey, cad);

        cad.setAssignedKey((String) quietGetFromAnnotation("assignedKey", annotation));
        Integer expSecond = (Integer) quietGetFromAnnotation("expireSecond", annotation);
        cad.setExpireSecond(expSecond == null ? 0 : expSecond);
        cad.setNamespace((String) quietGetFromAnnotation("namespace", annotation));
        cad.setMode((InvalidateCacheMode) quietGetFromAnnotation("mode", annotation));
        cad.setInvalidateExecuteOrder((InvalidateExecuteOrder) quietGetFromAnnotation(
            "executeOrder", annotation));

        // provided key
        Annotation[][] paramAnnotations = currentMethod.getParameterAnnotations();
        if (paramAnnotations != null && paramAnnotations.length != 0) {
            Object[] args = pjp.getArgs();
            for (int i = 0; i < paramAnnotations.length; i++) {
                Annotation[] annotations = paramAnnotations[i];
                Object arg = args[i];
                for (int j = 0; j < annotations.length; j++) {
                    Annotation paramAnnotation = annotations[j];
                    if (paramAnnotation instanceof KeyGenerator) {
                        // 该参数是key 生成器
                        String[] keyMethod = (String[]) quietGetFromAnnotation("keyMethod",
                            paramAnnotation);
                        // key method must return a string value as the key
                        if (arg instanceof Collection<?>) {
                            // 如果是集合，则调用集合元素的keyMethod方法
                            for (Object item : (Collection<?>) arg) {
                                if (item != null) {
                                    String key = buildKey(item, keyMethod);
                                    cad.addProvidedKey(key);
                                } else if ((Boolean) quietGetFromAnnotation("couldBeNull",
                                    paramAnnotation)) {
                                    cad.addProvidedKey("null");
                                }
                            }
                        } else {
                            // 参数为空的情况需要判断
                            if (arg == null) {
                                if ((Boolean) quietGetFromAnnotation("couldBeNull", paramAnnotation)) {
                                    cad.addProvidedKey("null");
                                } else {
                                    throw new RuntimeException("缓存数据Key不能为空.");
                                }
                            } else {
                                String key = buildKey(arg, keyMethod);
                                cad.addProvidedKey(key);
                            }
                        }
                    } else {
                        Class<?> paramType = currentMethod.getParameterTypes()[i];
                        if (paramAnnotation instanceof LastCacheValue) {
                            // 该参数是当前缓存数据
                            if (currentMethod.getReturnType().equals(paramType)) {
                                cad.setLastCacheValueParamIndex(i);
                                if (currentMethod.getReturnType().isPrimitive()) {
                                    cad.initDefaultCacheValue(currentMethod.getReturnType());
                                }
                            } else {
                                log.warn("LastCacheValue指定的参数类型和返回的数据类型不匹配.", arg);
                            }
                        } else if (paramAnnotation instanceof DeltaValue) {
                            if (paramType.equals(Long.class)||paramType.equals(Long.TYPE)) {
                                cad.setDeltaValueParamIndex(i);
                            } else {
                                throw new InvalidParameterException("增量参数类型必须为Long");
                            }
                        } else if (paramAnnotation instanceof DeltaResult) {
                            if (paramType.equals(Long.class)) {
                                cad.setDeltaResultParamIndex(i);
                            }else if(paramType.equals(Long.TYPE)){
                                cad.setDeltaResultParamIndex(i);
                                cad.initDefaultCacheValue(paramType);
                            } else {
                                throw new InvalidParameterException("增量返回类型必须为Long");
                            }
                        }
                    }
                }
            }
        }

        return cad;
    }

    private String buildKey(Object item, String[] keyMethod) throws IllegalArgumentException,
                                                            SecurityException,
                                                            IllegalAccessException,
                                                            InvocationTargetException,
                                                            NoSuchMethodException {
        StringBuffer buf = new StringBuffer();
        for (String km : keyMethod) {
            Object result = item.getClass().getMethod(km).invoke(item);
            buf.append(result == null ? "null" : result.toString());
        }

        return buf.toString();
    }

    protected Method getCurrentMethod(ProceedingJoinPoint pjp) throws Throwable {
        Signature sig = pjp.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = pjp.getTarget();
        Method currentMethod = target.getClass()
            .getMethod(msig.getName(), msig.getParameterTypes());
        return currentMethod;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Annotation getMethodAnnotation(Method method, Class annotationClass) {
        return method.getAnnotation(annotationClass);
    }

    protected Object quietGetFromAnnotation(String methodName, Annotation annotation) {
        if (annotation == null) {
            return null;
        }
        try {
            return annotation.annotationType().getDeclaredMethod(methodName).invoke(annotation);
        } catch (Exception e) {
            log.debug(e.getMessage());
            // ignore
        }
        return null;
    }

}
