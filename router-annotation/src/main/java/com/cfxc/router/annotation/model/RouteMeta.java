package com.cfxc.router.annotation.model;

import com.cfxc.router.annotation.Route;

import javax.lang.model.element.Element;

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/2/21
 */
public class RouteMeta {

    public enum RouteType {
        FRAGMENT, PROVIDER
    }

    private RouteType type;
    private Element element;
    private String destinationText;      // the text behind destination id of the fragment
    private String graphText;            // the text behind graph id of the navigation
    private Class<?> destination;        // the Class that used the annotation

    public RouteMeta() {
    }

    public static RouteMeta build(RouteType type, String destinationText, String graphText, Class<?> destination) {
        return new RouteMeta(type, destinationText, graphText, destination);
    }

    public RouteMeta(RouteType type, String destinationText, String graphText, Class<?> destination) {
        this.type = type;
        this.graphText = graphText;
        this.destinationText = destinationText;
        this.destination = destination;
    }

    public RouteMeta(RouteType type, Route route, String graphText, Element element) {
        this.type = type;
        this.graphText = graphText;
        this.destinationText = route.destinationText();
        this.element = element;
    }

    public String getDestinationText() {
        return destinationText;
    }

    public void setDestinationText(String destinationText) {
        this.destinationText = destinationText;
    }

    public String getGraphText() {
        return graphText;
    }

    public void setGraphText(String graphText) {
        this.graphText = graphText;
    }

    public RouteType getType() {
        return type;
    }

    public void setType(RouteType type) {
        this.type = type;
    }

    public Class<?> getDestination() {
        return destination;
    }

    public void setDestination(Class<?> destination) {
        this.destination = destination;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }
}
