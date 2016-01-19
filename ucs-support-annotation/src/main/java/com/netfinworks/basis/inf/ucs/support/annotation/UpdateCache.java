/*
 * Copyright 2013 weibopay.com, Inc. All rights reserved.
 */
package com.netfinworks.basis.inf.ucs.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author knico
 * @since Jul 30, 2013
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UpdateCache {
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
