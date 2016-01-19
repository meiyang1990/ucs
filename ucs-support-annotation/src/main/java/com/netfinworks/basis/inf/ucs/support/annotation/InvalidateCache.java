/**
 * 
 */
package com.netfinworks.basis.inf.ucs.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 清空缓存
 * @author bigknife
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InvalidateCache{
	/**
	 * 缓存命名空间
	 * @return
	 */
	public abstract String namespace();
	
	/**
	 * 指定的缓存key，如果方法签名中指定了KeyProvider标签，则优先使用KeyProvider，忽略assignedKey.
	 * @return
	 */
	public abstract String assignedKey() default "";
	/**
	 * <ul>
	 * 清空模式：
	 * <li><code>InvalidateCache.MODE_SINGE</code> 将多个keyGenerator生成的key合并为一个key清除; 如果没有keyGenerator则清除assignedKey</li>
	 * <li><code>InvalidateCache.MODE_MULTI</code> 将多个keyGenerator生成的key当成一个独立的key清除; 如果没有keyGenerator则清除assignedKey</li>
	 * </ul>
	 * @return
	 */
	public abstract InvalidateCacheMode mode() default InvalidateCacheMode.MODE_SINGE;
	
	/**
	 * <ul>
	 * 切面执行顺序
	 * <li><code>InvalidateExecuteOrder.BEFORE</code>在原方法之前执行，默认</li>
	 * <li><code>InvalidateExecuteOrder.AFTER</code>在原方法之后执行</li>
	 * </ul>
	 * @see InvalidateExecuteOrder
	 * @return
	 */
	public abstract InvalidateExecuteOrder executeOrder() default InvalidateExecuteOrder.BEFORE;
}
