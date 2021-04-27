package com.cfxc.router.compiler;

import com.cfxc.router.annotation.Constants;
import com.cfxc.router.annotation.Interceptor;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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

import static com.cfxc.router.annotation.Constants.ANNOTATION_TYPE_INTERCEPTOR;
import static com.cfxc.router.annotation.utils.Utils.isEmpty;
import static com.cfxc.router.annotation.utils.Utils.isNotEmpty;

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/20/21
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes(ANNOTATION_TYPE_INTERCEPTOR)
public class InterceptorProcessor extends AbstractProcessor {

    private Map<Integer, Element> interceptors = new HashMap<>();
    private Elements elementUtils;
    private Types types;
    private Filer filer;
    private TypeMirror iInterceptor;
    private Logger logger;
    private String moduleName;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
        types = processingEnvironment.getTypeUtils();
        iInterceptor = elementUtils.getTypeElement(Constants.IROUTE_INTERCEPTOR).asType();
        logger = new Logger(processingEnvironment.getMessager());
        Map<String, String> options = processingEnv.getOptions();
        if (isNotEmpty(options)) {
            moduleName = options.get(Constants.KEY_MODULE_NAME);
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!isEmpty(set)) {
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Interceptor.class);
            try {
                parseInterceptor(elements);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            return true;
        }
        return false;
    }

    private void parseInterceptor(Set<? extends Element> elements) throws IOException {
        if (isNotEmpty(elements)) {
            for (Element element : elements) {
                if (verify(element)) {
                    Interceptor interceptor = element.getAnnotation(Interceptor.class);
                    Element lastInterceptor = interceptors.get(interceptor.priority());
                    if (null != lastInterceptor) { // Added, throw exceptions
                        throw new IllegalArgumentException(
                                String.format(Locale.getDefault(), "More than one interceptors use same priority [%d], They are [%s] and [%s].",
                                        interceptor.priority(),
                                        lastInterceptor.getSimpleName(),
                                        element.getSimpleName())
                        );
                    }
                    interceptors.put(interceptor.priority(), element);
                } else {
                    logger.error("A interceptor verify failed, its " + element.asType());
                }
            }

            TypeElement iInterceptor = elementUtils.getTypeElement(Constants.IROUTE_INTERCEPTOR);
            TypeElement iInterceptorRoot = elementUtils.getTypeElement(Constants.IROUTE_INTERCEPTOR_ROOT);
            // Map<String, Class<? extends IInterceptor></>>
            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(
                    ClassName.get(Map.class),
                    ClassName.get(Integer.class),
                    ParameterizedTypeName.get(
                            ClassName.get(Class.class),
                            WildcardTypeName.subtypeOf(ClassName.get(iInterceptor))
                    )
            );
            // Map<String, Class<? extends IInterceptor>> interceptors
            ParameterSpec parameterSpec = ParameterSpec.builder(parameterizedTypeName, "interceptors").build();
            // public void loadInto(Map<String, Class<? extends IInterceptor>> interceptors){}
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(Constants.METHOD_LOAD_INTO)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(parameterSpec);
            if (null != interceptors && !interceptors.isEmpty()) {
                for (Map.Entry<Integer, Element> entry : interceptors.entrySet()) {
                    methodBuilder.addStatement("interceptors.put(" + entry.getKey() + ", $T.class)",
                            ClassName.get((TypeElement) entry.getValue()));
                }
            }
            JavaFile.builder(Constants.PACKAGE_OF_GENERATE_FILE,
                    TypeSpec.classBuilder(Constants.NAME_OF_INTERCEPTOR + moduleName)
                            .addModifiers(Modifier.PUBLIC)
                            .addMethod(methodBuilder.build())
                            .addSuperinterface(ClassName.get(iInterceptorRoot))
                            .build()
            ).build().writeTo(filer);
        }
    }

    /**
     * Verify the interceptor meta
     */
    private boolean verify(Element element) {
        Interceptor interceptor = element.getAnnotation(Interceptor.class);
        // It must be implement the interface IInterceptor and marked with annotation Interceptor.
        return interceptor != null && ((TypeElement) element).getInterfaces().contains(iInterceptor);
    }
}
