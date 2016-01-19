/**
 * 
 */
package com.netfinworks.basis.inf.ucs.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 生成Key
 * 
 * @author bigknife
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface KeyGenerator {
	/**
	 * 生成key的方法名，默认为toString方法
	 * 
	 * @return
	 */
	public abstract String[] keyMethod() default "toString";

	/**
	 * 是否允许Key为null，默认不允许
	 * 
	 * @return
	 */
	public abstract boolean couldBeNull() default false;
}
