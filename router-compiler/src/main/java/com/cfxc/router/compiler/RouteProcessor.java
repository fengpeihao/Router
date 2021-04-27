package com.cfxc.router.compiler;

import com.cfxc.router.annotation.Route;
import com.cfxc.router.annotation.Constants;
import com.cfxc.router.annotation.model.RouteMeta;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.cfxc.router.annotation.Constants.ANNOTATION_TYPE_ROUTE;
import static com.cfxc.router.annotation.Constants.FRAGMENT;
import static com.cfxc.router.annotation.Constants.IROUTE_ROOT;
import static com.cfxc.router.annotation.Constants.KEY_MODULE_GRAPH_NAME;
import static com.cfxc.router.annotation.Constants.KEY_MODULE_NAME;
import static com.cfxc.router.annotation.Constants.NO_MODULE_NAME_TIPS;
import static com.cfxc.router.annotation.Constants.PROVIDER;
import static com.cfxc.router.annotation.utils.Utils.isEmpty;
import static com.cfxc.router.annotation.utils.Utils.isNotEmpty;

@AutoService(Processor.class)
@SupportedAnnotationTypes(ANNOTATION_TYPE_ROUTE)
public class RouteProcessor extends AbstractProcessor {

    private Map<String, RouteMeta> routeMetaMap = new TreeMap<>();
    private Elements elementUtils;
    private Types types;
    private Filer filer;
    private String moduleName;
    private String graphName;

    private Logger logger;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
        types = processingEnvironment.getTypeUtils();
        logger = new Logger(processingEnvironment.getMessager());

        // Attempt to get user configuration [moduleName] [graphName](the text behind graph id of the navigation)
        Map<String, String> options = processingEnv.getOptions();
        if (isNotEmpty(options)) {
            moduleName = options.get(KEY_MODULE_NAME);
            graphName = options.get(KEY_MODULE_GRAPH_NAME);
        }

        if (isNotEmpty(moduleName)) {
            moduleName = moduleName.replaceAll("[^0-9a-zA-Z_]+", "");

            logger.info("The user has configuration the module name, it was [" + moduleName + "]");
        } else {
            logger.error(NO_MODULE_NAME_TIPS);
            throw new RuntimeException("Router::Compiler >>> No module name, for more information, look at gradle log.");
        }
        if (isEmpty(graphName)) {
            logger.error(NO_MODULE_NAME_TIPS);
            throw new RuntimeException("Router::Compiler >>> No graph name, for more information, look at gradle log.");
        }

        logger.info(">>> RouteProcessor init. <<<");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set != null && set.size() > 0) {
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Route.class);
            try {
                logger.info(">>> Found routes, start... <<<");
                this.parseRoutes(elements);
            } catch (Exception e) {
                logger.error(e);
            }
            return true;
        }
        return false;
    }

    /**
     * @param elements
     */
    private void parseRoutes(Set<? extends Element> elements) {
        if (elements != null && elements.size() > 0) {
            logger.info(">>> Found routes, size is " + elements.size() + " <<<");
            routeMetaMap.clear();

            TypeMirror typeFragment = elementUtils.getTypeElement(FRAGMENT).asType();
            TypeMirror typeProvider = elementUtils.getTypeElement(PROVIDER).asType();

            for (Element element : elements) {
                TypeMirror tm = element.asType();
                logger.info("Route class:" + tm.toString());
                Route route = element.getAnnotation(Route.class);
                RouteMeta routeMeta;
                if (types.isSubtype(tm, typeFragment)) {
                    logger.info(">>> Found fragment route: " + tm.toString() + " <<<");
                    routeMeta = new RouteMeta(RouteMeta.RouteType.FRAGMENT, route, graphName, element);
                } else if (types.isSubtype(tm, typeProvider)) {
                    routeMeta = new RouteMeta(RouteMeta.RouteType.PROVIDER, route, graphName, element);
                } else {
                    throw new RuntimeException("Just support Fragment Route: " + element);
                }
                routeMetaMap.put(routeMeta.getDestinationText(), routeMeta);
            }

            TypeElement iRouteRoot = elementUtils.getTypeElement(IROUTE_ROOT);
            generatedRoutFile(iRouteRoot);
        }
    }

    /**
     * @param iRouteRoot
     */
    private void generatedRoutFile(TypeElement iRouteRoot) {
        //create parameter -> Map<String,RouteMeta>
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouteMeta.class));
        //set parameter name -> Map<String,RouteMeta> routes
        ParameterSpec parameter = ParameterSpec.builder(parameterizedTypeName, "routes").build();

        //create function -> public void loadInfo(Map<String,RouteMeta> routes)
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(Constants.METHOD_LOAD_INTO)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(parameter);
        //function body
        for (Map.Entry<String, RouteMeta> entry : routeMetaMap.entrySet()) {
            methodBuilder.addStatement("routes.put($S,$T.build($T.$L,$S,$S,$T.class))",
                    entry.getKey(),
                    ClassName.get(RouteMeta.class),
                    ClassName.get(RouteMeta.RouteType.class),
                    entry.getValue().getType(),
                    entry.getValue().getDestinationText(),
                    graphName,
                    ClassName.get(((TypeElement) entry.getValue().getElement())));
        }

        //create class
        String className = Constants.NAME_OF_ROOT + moduleName;
        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addSuperinterface(ClassName.get(iRouteRoot))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodBuilder.build())
                .build();
        try {
            JavaFile.builder(Constants.PACKAGE_OF_GENERATE_FILE, typeSpec).build().writeTo(filer);
            logger.info("Generated Route：" + Constants.PACKAGE_OF_GENERATE_FILE + "." + className);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getSupportedOptions() {
        return new HashSet<String>() {{
            this.add(KEY_MODULE_NAME);
        }};
    }
}