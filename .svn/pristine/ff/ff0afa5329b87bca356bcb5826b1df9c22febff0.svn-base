/**
 * 
 */
package com.netfinworks.basis.inf.ucs.local;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

import com.netfinworks.basis.inf.ucs.local.support.CachedItem;

/**
 * @author bigknife
 *
 */
public abstract class AbstractDiscardPolicy<T> implements DiscardingPolicy<T> {
	protected static final Random R = new Random();
	
	public String discard(Map<String, CachedItem<T>> map) {
		//随机获取一定数量的item
		CachedItem<T> [] items = getCandidates(map.values());
		if(items != null && items.length > 0){
			CachedItem<T> item = null;
			for(CachedItem<T> _item : items){
				if(item == null){
					item = _item;
					continue;
				}
				item = compare(item,_item);
			}
			
			return item.getKey();
		}
		return null;
	}
	/**
	 * 比较两个缓存数据， 返回要丢弃的数据
	 * @param item1
	 * @param item2
	 * @return 
	 */
	abstract protected CachedItem<T> compare(CachedItem<T> item1, CachedItem<T> item2);
	
	
	@SuppressWarnings("unchecked")
	protected CachedItem<T>[] getCandidates(Collection<CachedItem<T>> elements) {
		if (elements == null || elements.isEmpty()) {
			return null;
		}

		int amount = elements.size();

		int candidateAmount = 0;
		if (amount < 10000) {
			candidateAmount = Math.min(amount, 10);
		} else {
			candidateAmount = 100;
		}

		Iterator<CachedItem<T>> iterator = elements.iterator();
		CachedItem<T>[] candidates = new CachedItem[candidateAmount];
		int interval = amount / candidateAmount;
		int forward = 0;
		int random = 0;
		int oldRandom = 0;

		for (int i = 0; i < candidateAmount; i++) {
			oldRandom = random;
			random = R.nextInt(interval);
			forward = i == 0 ? random : interval - oldRandom + random;

			for (int j = 0; j < forward - 1; j++)
				try {
					iterator.next();
				} catch (NoSuchElementException e) {
				}
			try {
				candidates[i] = iterator.next();
			} catch (NoSuchElementException e) {
			}
		}
		return candidates;
	}

}
