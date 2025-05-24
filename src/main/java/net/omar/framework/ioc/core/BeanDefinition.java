package net.omar.framework.ioc.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class BeanDefinition {
    private String beanName;
    private Class<?> beanClass;
    private String scope = "singleton";
    private List<PropertyValue> propertyValues = new ArrayList<>();
    private List<ConstructorArgValue> constructorArgValues = new ArrayList<>();
    private Constructor<?> autowiredConstructor;
    private Map<Field, String> autowiredFields = new HashMap<>();
    private Map<Method, String> autowiredSetters = new HashMap<>();
    
    public BeanDefinition(String beanName, Class<?> beanClass) {
        this.beanName = beanName;
        this.beanClass = beanClass;
    }
    
    public String getBeanName() {
        return beanName;
    }
    
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
    
    public Class<?> getBeanClass() {
        return beanClass;
    }
    
    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }
    
    public String getScope() {
        return scope;
    }
    
    public void setScope(String scope) {
        this.scope = scope;
    }
    
    public boolean isSingleton() {
        return "singleton".equals(scope);
    }
    
    public List<PropertyValue> getPropertyValues() {
        return propertyValues;
    }
    
    public void setPropertyValues(List<PropertyValue> propertyValues) {
        this.propertyValues = propertyValues;
    }
    
    public List<ConstructorArgValue> getConstructorArgValues() {
        return constructorArgValues;
    }
    
    public void setConstructorArgValues(List<ConstructorArgValue> constructorArgValues) {
        this.constructorArgValues = constructorArgValues;
    }
    
    public Constructor<?> getAutowiredConstructor() {
        return autowiredConstructor;
    }
    
    public void setAutowiredConstructor(Constructor<?> autowiredConstructor) {
        this.autowiredConstructor = autowiredConstructor;
    }
    
    public Map<Field, String> getAutowiredFields() {
        return autowiredFields;
    }
    
    public void setAutowiredFields(Map<Field, String> autowiredFields) {
        this.autowiredFields = autowiredFields;
    }
    
    public Map<Method, String> getAutowiredSetters() {
        return autowiredSetters;
    }
    
    public void setAutowiredSetters(Map<Method, String> autowiredSetters) {
        this.autowiredSetters = autowiredSetters;
    }
    
    public static class PropertyValue {
        private String name;
        private Object value;
        private String ref;
        
        public PropertyValue(String name, Object value, String ref) {
            this.name = name;
            this.value = value;
            this.ref = ref;
        }
        
        public String getName() {
            return name;
        }
        
        public Object getValue() {
            return value;
        }
        
        public String getRef() {
            return ref;
        }
    }
    
    public static class ConstructorArgValue {
        private Integer index;
        private String type;
        private Object value;
        private String ref;
        
        public ConstructorArgValue(Integer index, String type, Object value, String ref) {
            this.index = index;
            this.type = type;
            this.value = value;
            this.ref = ref;
        }
        
        public Integer getIndex() {
            return index;
        }
        
        public String getType() {
            return type;
        }
        
        public Object getValue() {
            return value;
        }
        
        public String getRef() {
            return ref;
        }
    }
}