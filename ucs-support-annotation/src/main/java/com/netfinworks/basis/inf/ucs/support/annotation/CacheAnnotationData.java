/**
 * 
 */
package com.netfinworks.basis.inf.ucs.support.annotation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author bigknife
 * 
 */
public class CacheAnnotationData {
    //指定的key
    private String                 assignedKey;
    //过期时间（秒）
    private int                    expireSecond;
    //缓存命名空间
    private String                 namespace;
    //删除模式
    private InvalidateCacheMode    mode;
    //删除执行顺序
    private InvalidateExecuteOrder invalidateExecuteOrder;
    //更新的时候当前缓存数据的参数位置
    private int                    lastCacheValueParamIndex = -1;
    //更新时无缓存数据的默认值
    private Object                 defaultCacheValue;
    private int                    deltaValueParamIndex     = -1;
    private int                    deltaResultParamIndex    = -1;

    /**
     * @return the invalidateExecuteOrder
     */
    public InvalidateExecuteOrder getInvalidateExecuteOrder() {
        return invalidateExecuteOrder;
    }

    /**
     * @param invalidateExecuteOrder the invalidateExecuteOrder to set
     */
    public void setInvalidateExecuteOrder(InvalidateExecuteOrder invalidateExecuteOrder) {
        this.invalidateExecuteOrder = invalidateExecuteOrder;
    }

    /**
     * @return the mode
     */
    public InvalidateCacheMode getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(InvalidateCacheMode mode) {
        this.mode = mode;
    }

    //key provider 提供的key列表
    private List<String> providedKeys;

    public void addProvidedKey(String key) {
        if (providedKeys == null) {
            providedKeys = new ArrayList<String>();
        }
        providedKeys.add(key);
    }

    public void removeProvidedKey(String key) {
        if (providedKeys != null) {
            providedKeys.remove(key);
        }
    }

    public String buildKey() {
        if (providedKeys != null && !providedKeys.isEmpty()) {
            StringBuffer buf = new StringBuffer();
            buf.append(namespace);
            for (String pk : providedKeys) {
                buf.append(pk);
            }
            return buf.toString();
        }
        return namespace + assignedKey;
    }

    /**
     * 根据传入的Key生产一组Key，由于目前只有invalidateCache使用，所以改为Set
     * @return
     */
    public String[] buildKeys() {
        if (providedKeys != null && !providedKeys.isEmpty()) {
            Set<String> set = new HashSet<String>();

            for (String pk : providedKeys) {
                set.add(namespace + pk);
            }
            return (String[]) set.toArray(new String[set.size()]);
        }
        return new String[] { namespace + assignedKey };
    }

    /**
     * @return the assignedKey
     */
    public String getAssignedKey() {
        return assignedKey;
    }

    /**
     * @param assignedKey the assignedKey to set
     */
    public void setAssignedKey(String assignedKey) {
        this.assignedKey = assignedKey;
    }

    /**
     * @return the expireSecond
     */
    public int getExpireSecond() {
        return expireSecond;
    }

    /**
     * @param expireSecond the expireSecond to set
     */
    public void setExpireSecond(int expireSecond) {
        this.expireSecond = expireSecond;
    }

    /**
     * @return the namespace
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * @param namespace the namespace to set
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * @return the lastCacheValueParamIndex
     */
    public int getLastCacheValueParamIndex() {
        return lastCacheValueParamIndex;
    }

    /**
     * @param i
     */
    public void setLastCacheValueParamIndex(int idx) {
        this.lastCacheValueParamIndex = idx;
    }

    /**
     * @return the defaultCacheValue
     */
    public Object getDefaultCacheValue() {
        return defaultCacheValue;
    }

    /**
     * @param clz
     * @param init the defaultCacheValue to set
     */
    public void initDefaultCacheValue(Class<?> clz) {
        if (clz == Boolean.TYPE) {
            defaultCacheValue = false;
        } else if (clz == Character.TYPE) {
            defaultCacheValue = ' ';
        } else if (clz == Byte.TYPE) {
            defaultCacheValue = (byte) -1;
        } else if (clz == Short.TYPE) {
            defaultCacheValue = (short) -1;
        } else if (clz == Integer.TYPE) {
            defaultCacheValue = (int) -1;
        } else if (clz == Long.TYPE) {
            defaultCacheValue = (long) -1;
        } else if (clz == Float.TYPE) {
            defaultCacheValue = (float) -1;
        } else if (clz == Double.TYPE) {
            defaultCacheValue = (double) -1;
        }
    }

    /**
     * @param i
     */
    public void setDeltaValueParamIndex(int idx) {
        this.deltaValueParamIndex = idx;
    }

    /**
     * @return the deltaValueParamIndex
     */
    public int getDeltaValueParamIndex() {
        return deltaValueParamIndex;
    }

    /**
     * @param i
     */
    public void setDeltaResultParamIndex(int idx) {
        this.deltaResultParamIndex = idx;
    }

    /**
     * @return the deltaResultParamIndex
     */
    public int getDeltaResultParamIndex() {
        return deltaResultParamIndex;
    }
}
