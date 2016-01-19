/**
 * 
 */
package com.netfinworks.basis.inf.ucs.local.discardpolicy;

import com.netfinworks.basis.inf.ucs.local.AbstractDiscardPolicy;
import com.netfinworks.basis.inf.ucs.local.support.CachedItem;

/**
 * @author bigknife
 *
 */
public class FIFODiscardPolicy<T> extends AbstractDiscardPolicy<T> {

	/* (non-Javadoc)
	 * @see com.netfinworks.basis.inf.ucs.local.AbstractDiscardPolicy#compare(com.netfinworks.basis.inf.ucs.local.support.CachedItem, com.netfinworks.basis.inf.ucs.local.support.CachedItem)
	 */
	@Override
	protected CachedItem<T> compare(CachedItem<T> item1, CachedItem<T> item2) {
		return (item1.getCreateTime() - item2.getCreateTime()) >= 0 ? item1 : item2;
	}

}
