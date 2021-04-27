package com.cfxc.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Route {

    /**
     * the text behind destination id of the fragment
     * E.g: the destinationId is R.id.homeFragment, then the destinationText is 'homeFragment'
     */
    String destinationText() default "";
}
