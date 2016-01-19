/**
 * 
 */
package com.netfinworks.basis.inf.ucs.client;

/**
 * @author bigknife
 *
 */
public enum CacheEventType {
	SET_OK,
	SET_FAIL,
	SET_EXCEPTION,

    ADD_OK,
    ADD_FAIL,
    ADD_EXCEPTION,
	
	GET_OK,
	GET_EXCEPTION,
	
	DEL_OK,
	DEL_FAIL,
	DEL_EXCEPTION,
	
	REPLACE_OK,
	REPLACE_FAIL,
	REPLACE_EXCEPTION,
	
	TOUCH_OK,
	TOUCH_FAIL,
	TOUCH_EXCEPTION,
	
	GAT_OK,
	GAT_EXCEPTION,
	
	FLUSH_OK,
	FLUSH_EXCEPTION
}
