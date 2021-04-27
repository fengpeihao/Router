package com.cfxc.router.annotation;

public class Constants {

    // log
    public static final String PREFIX_OF_LOGGER = "::Compiler ";
    public static final String NO_MODULE_NAME_TIPS = "These no module name, at 'build.gradle', like :\n" +
            "android {\n" +
            "    defaultConfig {\n" +
            "        ...\n" +
            "        javaCompileOptions {\n" +
            "            annotationProcessorOptions {\n" +
            "                arguments = [ROUTER_MODULE_NAME: project.getName()]\n" +
            "                arguments = [ROUTER_MODULE_GRAPH_NAME: \"nav_graph\"]\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}\n";


    // custom interface
    private static final String FACADE_PACKAGE = "com.cfxc.router.core";
    private static final String TEMPLATE_PACKAGE = ".template";
    public static final String IROUTE_ROOT = FACADE_PACKAGE + TEMPLATE_PACKAGE + ".IRouteRoot";
    public static final String IROUTE_INTERCEPTOR = FACADE_PACKAGE + TEMPLATE_PACKAGE + ".IInterceptor";
    public static final String IROUTE_INTERCEPTOR_ROOT = FACADE_PACKAGE + TEMPLATE_PACKAGE + ".IInterceptorRoot";

    public static final String FRAGMENT = "androidx.fragment.app.Fragment";
    public static final String PROVIDER = FACADE_PACKAGE+TEMPLATE_PACKAGE+".IProvider";

    // Options of processor
    public static final String KEY_MODULE_NAME = "ROUTER_MODULE_NAME";
    public static final String KEY_MODULE_GRAPH_NAME = "ROUTER_MODULE_GRAPH_NAME";

    // Annotation type
    public static final String ANNOTATION_TYPE_ROUTE = "com.cfxc.router.annotation.Route";
    public static final String ANNOTATION_TYPE_INTERCEPTOR = "com.cfxc.router.annotation.Interceptor";

    public static final String SEPARATOR = "_";
    public static final String PROJECT = "Router";
    public static final String SUFFIX_ROOT = "Root";
    public static final String SUFFIX_INTERCEPTOR = "Interceptor";
    public static final String METHOD_LOAD_INTO = "loadInto";
    public static final String MAIN_MODULE_ROUTER_GRAPH_NAME = "nav_graph";
    public static final String PACKAGE_OF_GENERATE_FILE = "com.cfxc.router.routes";
    public static final String NAME_OF_ROOT = PROJECT + SEPARATOR + SUFFIX_ROOT + SEPARATOR;
    public static final String NAME_OF_INTERCEPTOR = PROJECT + SEPARATOR + SUFFIX_INTERCEPTOR + SEPARATOR;

    public static final String KEY_DESTINATION_ID = "key_destination_id";
    public static final String KEY_CONTINUE_DESTINATION = "key_continue_destination";
}
