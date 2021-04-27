package com.cfxc.router.core.callback;

import com.cfxc.router.core.Postcard;

/**
 * @description Callback after navigation.
 * @author: created by peihao.feng
 * @date: 4/20/21
 */
public interface NavigationCallback {

    /**
     * Callback after lose your way.
     *
     * @param postcard meta
     */
    void onLost(Postcard postcard);

    /**
     * Callback after navigation.
     *
     * @param postcard meta
     */
    void onArrival(Postcard postcard);

    /**
     * Callback on interrupt.
     *
     * @param postcard meta
     */
    void onInterrupt(Postcard postcard);
}
