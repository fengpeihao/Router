package com.cfxc.router.core.utils;

import java.util.TreeMap;

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/2/21
 */
public class UniqueKeyTreeMap<K, V> extends TreeMap<K, V> {
    private String tipText;

    public UniqueKeyTreeMap(String exceptionText) {
        super();
        tipText = exceptionText;
    }

    @Override
    public V put(K key, V value) {
        if (containsKey(key)) {
            throw new RuntimeException(String.format(tipText, key));
        } else {
            return super.put(key, value);
        }
    }
}
