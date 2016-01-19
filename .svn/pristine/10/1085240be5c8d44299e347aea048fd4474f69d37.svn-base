/**
 * 
 */
package com.netfinworks.basis.inf.ucs.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存结果
 * @author bigknife
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheResult {
	/**
	 * 缓存命名空间
	 * @return
	 */
	public abstract String namespace();
	/**
	 * 缓存过期时间,默认采用系统配置的时间
	 * @return
	 */
	public abstract int expireSecond() default -1;
	/**
	 * 指定的缓存key，如果方法签名中指定了KeyProvider标签，则优先使用KeyProvider，忽略assignedKey.
	 * @return
	 */
	public abstract String assignedKey() default "";
}
