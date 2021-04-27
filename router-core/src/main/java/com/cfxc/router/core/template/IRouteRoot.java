package com.cfxc.router.core.template;

import com.cfxc.router.annotation.model.RouteMeta;

import java.util.Map;

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/6/21
 */
public interface IRouteRoot {

    void loadInto(Map<String, RouteMeta> routes);
}
