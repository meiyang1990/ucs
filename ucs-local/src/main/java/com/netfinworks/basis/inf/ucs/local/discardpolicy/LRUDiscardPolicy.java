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
public class LRUDiscardPolicy<T> extends AbstractDiscardPolicy<T> {

	/* (non-Javadoc)
	 * @see com.netfinworks.basis.inf.ucs.local.AbstractDiscardPolicy#compare(com.netfinworks.basis.inf.ucs.local.support.CachedItem, com.netfinworks.basis.inf.ucs.local.support.CachedItem)
	 */
	@Override
	protected CachedItem<T> compare(CachedItem<T> item1, CachedItem<T> item2) {
		if(item1.getHint().get() == 0){
			return item1;
		}
		if(item2.getHint().get() == 0){
			return item2;
		}
		long life1 = item1.getUpdateTime() - item1.getCreateTime();
		long life2 = item2.getUpdateTime() - item2.getCreateTime();
		return ( (double) life1 / ((double) item1.getHint().get()) - ((double)life2 / (double)item2.getHint().get() )) >=0 ? item1 : item2;
	}

}
