package com.cfxc.router.core.template;

import java.util.Map;

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/20/21
 */
public interface IInterceptorRoot {

    void loadInto(Map<Integer, Class<? extends IInterceptor>> map);
}
