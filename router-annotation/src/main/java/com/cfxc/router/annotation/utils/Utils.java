package com.cfxc.router.annotation.utils;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/2/21
 */
public class Utils {

    public static boolean isNotEmpty(CharSequence cs) {
        return cs != null && cs.length() > 0;
    }

    public static boolean isNotEmpty(final Collection<?> coll) {
        return coll != null && coll.size() > 0;
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return map != null && map.size() > 0;
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isEmpty(final Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isEmpty(final TreeMap<?, ?> treeMap) {
        return treeMap == null || treeMap.isEmpty();
    }
}
