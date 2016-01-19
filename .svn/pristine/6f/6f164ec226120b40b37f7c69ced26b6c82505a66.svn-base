/**
 * 
 */
package com.netfinworks.basis.inf.ucs.client;

/**
 * 缓存监听器接口
 * @author bigknife
 *
 */
public interface CacheListener<T> {
	public static class Event<T>{
		
		private CacheEventType type;
		private String key;
		private T value;
		private Exception exception;
		
		public Event(CacheEventType type, String key, T value, Exception exception){
			this.key = key;
			this.value = value;
			this.type = type;
			this.exception = exception;
		}
		public Event(CacheEventType type, String key, T value){
			this(type,key,value,null);
		}
		public Event(CacheEventType type, String key, Exception exception){
			this(type,key,null,exception);
		}
		public Event(CacheEventType type, String key){
			this(type,key,null,null);
		}
		public Event(CacheEventType type){
			this(type,null,null,null);
		}
		public Event(CacheEventType type,Exception exception){
			this(type,null,null,exception);
		}
		
		
	
		/**
		 * @return the type
		 */
		public CacheEventType getType() {
			return type;
		}
		/**
		 * @return the exception
		 */
		public Exception getException() {
			return exception;
		}

		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}
		
		/**
		 * @return the value
		 */
		public T getValue() {
			return value;
		}
		
		
	}
	void onErroEvent(Event<T> event) throws Exception;
}
