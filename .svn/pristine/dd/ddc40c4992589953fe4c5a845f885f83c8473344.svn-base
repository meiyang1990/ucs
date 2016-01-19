/**
 * 
 */
package com.netfinworks.basis.inf.ucs.support.annotation;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * @author bigknife
 *
 */
public class LogbackFilter<E> extends Filter<E> {

	@Override
	public FilterReply decide(E event) {
		ILoggingEvent e = (ILoggingEvent) event;
		String loggerName = e.getLoggerName();
		if("net.rubyeye.xmemcached".equals(loggerName) || "com.google.code.yanf4j".equals(loggerName)){
			return FilterReply.DENY; 
		}
		return FilterReply.ACCEPT;
	}

}
