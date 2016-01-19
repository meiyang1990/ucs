/**
 * 
 */
package com.netfinworks.basis.inf.ucs.local.support;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author bigknife
 *
 */
public class CachedItem<T> {
	private String key;
	private T item;
	private long createTime;
	private long updateTime;
	private AtomicLong hint = new AtomicLong(0);
	private int expireSecond;
	
	public CachedItem(){
		this.createTime = System.nanoTime();
		this.updateTime = System.nanoTime();
		
	}
	public CachedItem(String key, T t, int expiredSecond){
		this.createTime = System.nanoTime();
		this.updateTime = System.nanoTime();
		this.expireSecond = expiredSecond;
		this.item = t;
		this.key = key;
	}
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public boolean isExpired(){
		long t1 = System.nanoTime() - updateTime;
		long t2 = expireSecond * 1000000000L;
		return t1 > t2;
	}
	
	
	public AtomicLong getHint() {
		return hint;
	}
	public void setHint(AtomicLong hint) {
		this.hint = hint;
	}
	public T getItem() {
		return item;
	}
	public void setItem(T item) {
		this.item = item;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public int getExpireSecond() {
		return expireSecond;
	}
	public void setExpireSecond(int expireSecond) {
		this.expireSecond = expireSecond;
	}
}
