/**
 * 
 */
package com.netfinworks.basis.inf.ucs.support.annotation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author bigknife
 *
 */
public class AOPTest {
	private ApplicationContext ac = new ClassPathXmlApplicationContext(new String[]{
			"classpath*:ucs-support-annotatiion.xml",
			"classpath*:context-test.xml"});
	public Object getBean(String name){
		return ac.getBean(name);
	}
}
