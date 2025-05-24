package net.omar.framework.ioc.core;

public interface ApplicationContext {
    Object getBean(String name);
    <T> T getBean(String name, Class<T> requiredType);
    <T> T getBean(Class<T> requiredType);
    boolean containsBean(String name);
    String[] getBeanDefinitionNames();
}