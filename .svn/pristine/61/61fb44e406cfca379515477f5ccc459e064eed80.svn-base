/**
 * Copyright 2014 netfinworks.com, Inc. All rights reserved.
 */
package com.netfinworks.basis.inf.ucs.support.annotation;

import org.junit.Test;

/**
 * <p>注释</p>
 * @author huipeng
 * @version $Id: CacheDeltaTest.java, v 0.1 Jan 13, 2014 3:04:17 PM knico Exp $
 */
public class CacheDeltaTest extends AOPTest {
    @Test
    public void test_decrease() {
        TestService ts = (TestService) getBean("testService");
        System.out.println(ts.decreaseTop(11, 0));
        System.out.println(ts.decreaseTop(11, 0));
        System.out.println(ts.decreaseTop(11, 0));
        System.out.println(ts.decreaseTop(11, 0));
        System.out.println(ts.decreaseTop(11, 0));
        System.out.println(ts.decreaseTop(11, 0));
        System.out.println(ts.decreaseTop(11, 0));
        System.out.println(ts.decreaseTop(11, 0));
        System.out.println(ts.decreaseTop(11, 0));
        System.out.println(ts.decreaseTop(11, 0));
    }

    @Test
    public void test_setTop() {
        TestService ts = (TestService) getBean("testService");
        ts.setTop(10000);
    }
}
