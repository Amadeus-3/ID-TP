package net.omar.framework.ioc.core;

import net.omar.framework.ioc.annotations.*;
import net.omar.framework.ioc.exceptions.*;
import java.lang.reflect.*;
import java.util.*;

public abstract class AbstractApplicationContext implements ApplicationContext {
    
    protected final Map<String, BeanDefinition> beanDefinitions = new HashMap<>();
    protected final Map<String, Object> singletonBeans = new HashMap<>();
    protected final Set<String> currentlyInCreation = new HashSet<>();
    
    @Override
    public Object getBean(String name) {
        return doGetBean(name, null);
    }
    
    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return (T) doGetBean(name, requiredType);
    }
    
    @Override
    public <T> T getBean(Class<T> requiredType) {
        List<String> beanNames = new ArrayList<>();
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitions.entrySet()) {
            if (requiredType.isAssignableFrom(entry.getValue().getBeanClass())) {
                beanNames.add(entry.getKey());
            }
        }
        
        if (beanNames.isEmpty()) {
            throw new NoSuchBeanException("No bean of type " + requiredType.getName() + " found");
        }
        
        if (beanNames.size() > 1) {
            throw new NoSuchBeanException("Multiple beans of type " + requiredType.getName() + " found: " + beanNames);
        }
        
        return getBean(beanNames.get(0), requiredType);
    }
    
    @Override
    public boolean containsBean(String name) {
        return beanDefinitions.containsKey(name);
    }
    
    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitions.keySet().toArray(new String[0]);
    }
    
    protected Object doGetBean(String name, Class<?> requiredType) {
        BeanDefinition beanDefinition = beanDefinitions.get(name);
        if (beanDefinition == null) {
            throw new NoSuchBeanException("No bean named '" + name + "' found");
        }
        
        if (requiredType != null && !requiredType.isAssignableFrom(beanDefinition.getBeanClass())) {
            throw new BeanCreationException("Bean named '" + name + "' is of type " + 
                beanDefinition.getBeanClass().getName() + " but was required as type " + requiredType.getName());
        }
        
        if (beanDefinition.isSingleton()) {
            if (singletonBeans.containsKey(name)) {
                return singletonBeans.get(name);
            }
            
            if (currentlyInCreation.contains(name)) {
                throw new CircularDependencyException("Circular dependency detected for bean '" + name + "'");
            }
            
            currentlyInCreation.add(name);
            try {
                Object bean = createBean(name, beanDefinition);
                singletonBeans.put(name, bean);
                return bean;
            } finally {
                currentlyInCreation.remove(name);
            }
        } else {
            return createBean(name, beanDefinition);
        }
    }
    
    protected Object createBean(String beanName, BeanDefinition beanDefinition) {
        try {
            Object bean = instantiateBean(beanName, beanDefinition);
            populateBean(beanName, beanDefinition, bean);
            return bean;
        } catch (Exception e) {
            throw new BeanCreationException("Failed to create bean '" + beanName + "'", e);
        }
    }
    
    protected Object instantiateBean(String beanName, BeanDefinition beanDefinition) throws Exception {
        Class<?> beanClass = beanDefinition.getBeanClass();
        
        // Constructor injection
        if (beanDefinition.getAutowiredConstructor() != null) {
            return instantiateUsingAutowiredConstructor(beanDefinition);
        }
        
        // XML constructor args
        if (!beanDefinition.getConstructorArgValues().isEmpty()) {
            return instantiateUsingConstructorArgs(beanDefinition);
        }
        
        // Default constructor
        return beanClass.getDeclaredConstructor().newInstance();
    }
    
    protected Object instantiateUsingAutowiredConstructor(BeanDefinition beanDefinition) throws Exception {
        Constructor<?> constructor = beanDefinition.getAutowiredConstructor();
        Parameter[] parameters = constructor.getParameters();
        Object[] args = new Object[parameters.length];
        
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            Qualifier qualifier = param.getAnnotation(Qualifier.class);
            
            if (qualifier != null) {
                args[i] = getBean(qualifier.value());
            } else {
                args[i] = getBean(param.getType());
            }
        }
        
        return constructor.newInstance(args);
    }
    
    protected Object instantiateUsingConstructorArgs(BeanDefinition beanDefinition) throws Exception {
        List<BeanDefinition.ConstructorArgValue> constructorArgs = beanDefinition.getConstructorArgValues();
        Class<?>[] paramTypes = new Class[constructorArgs.size()];
        Object[] args = new Object[constructorArgs.size()];
        
        for (int i = 0; i < constructorArgs.size(); i++) {
            BeanDefinition.ConstructorArgValue arg = constructorArgs.get(i);
            int index = arg.getIndex() != null ? arg.getIndex() : i;
            
            if (arg.getRef() != null) {
                args[index] = getBean(arg.getRef());
                paramTypes[index] = args[index].getClass();
            } else {
                args[index] = convertValue(arg.getValue(), arg.getType());
                paramTypes[index] = args[index].getClass();
            }
        }
        
        Constructor<?> constructor = beanDefinition.getBeanClass().getConstructor(paramTypes);
        return constructor.newInstance(args);
    }
    
    protected void populateBean(String beanName, BeanDefinition beanDefinition, Object bean) throws Exception {
        // Field injection
        for (Map.Entry<Field, String> entry : beanDefinition.getAutowiredFields().entrySet()) {
            Field field = entry.getKey();
            String qualifierValue = entry.getValue();
            field.setAccessible(true);
            
            Object value;
            if (qualifierValue != null) {
                value = getBean(qualifierValue);
            } else {
                value = getBean(field.getType());
            }
            
            field.set(bean, value);
        }
        
        // Setter injection
        for (Map.Entry<Method, String> entry : beanDefinition.getAutowiredSetters().entrySet()) {
            Method setter = entry.getKey();
            String qualifierValue = entry.getValue();
            Class<?> paramType = setter.getParameterTypes()[0];
            
            Object value;
            if (qualifierValue != null) {
                value = getBean(qualifierValue);
            } else {
                value = getBean(paramType);
            }
            
            setter.invoke(bean, value);
        }
        
        // XML property injection
        for (BeanDefinition.PropertyValue propertyValue : beanDefinition.getPropertyValues()) {
            String propertyName = propertyValue.getName();
            String setterName = "set" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
            
            Method setter = findSetter(bean.getClass(), setterName);
            if (setter == null) {
                throw new BeanCreationException("No setter found for property '" + propertyName + "' in bean '" + beanName + "'");
            }
            
            Object value;
            if (propertyValue.getRef() != null) {
                value = getBean(propertyValue.getRef());
            } else {
                value = convertValue(propertyValue.getValue(), setter.getParameterTypes()[0]);
            }
            
            setter.invoke(bean, value);
        }
    }
    
    protected Method findSetter(Class<?> clazz, String setterName) {
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(setterName) && method.getParameterCount() == 1) {
                return method;
            }
        }
        return null;
    }
    
    protected Object convertValue(Object value, String type) throws ClassNotFoundException {
        if (type == null) {
            return value;
        }
        
        return convertValue(value, Class.forName(type));
    }
    
    protected Object convertValue(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }
        
        if (targetType.isInstance(value)) {
            return value;
        }
        
        String stringValue = value.toString();
        
        if (targetType == String.class) {
            return stringValue;
        } else if (targetType == int.class || targetType == Integer.class) {
            return Integer.parseInt(stringValue);
        } else if (targetType == long.class || targetType == Long.class) {
            return Long.parseLong(stringValue);
        } else if (targetType == double.class || targetType == Double.class) {
            return Double.parseDouble(stringValue);
        } else if (targetType == float.class || targetType == Float.class) {
            return Float.parseFloat(stringValue);
        } else if (targetType == boolean.class || targetType == Boolean.class) {
            return Boolean.parseBoolean(stringValue);
        }
        
        throw new BeanCreationException("Cannot convert value '" + value + "' to type " + targetType.getName());
    }
    
    protected abstract void loadBeanDefinitions();
}