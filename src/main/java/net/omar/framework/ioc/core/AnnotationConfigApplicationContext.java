package net.omar.framework.ioc.core;

import net.omar.framework.ioc.annotations.*;
import net.omar.framework.ioc.utils.ClassScanner;
import java.lang.reflect.*;
import java.util.*;

public class AnnotationConfigApplicationContext extends AbstractApplicationContext {
    
    private final Set<String> basePackages = new HashSet<>();
    
    public AnnotationConfigApplicationContext(String... basePackages) {
        this.basePackages.addAll(Arrays.asList(basePackages));
        refresh();
    }
    
    public AnnotationConfigApplicationContext(Class<?>... componentClasses) {
        for (Class<?> componentClass : componentClasses) {
            this.basePackages.add(componentClass.getPackage().getName());
        }
        refresh();
    }
    
    public void refresh() {
        loadBeanDefinitions();
    }
    
    @Override
    protected void loadBeanDefinitions() {
        ClassScanner scanner = new ClassScanner();
        
        for (String basePackage : basePackages) {
            Set<Class<?>> classes = scanner.scanPackage(basePackage);
            
            for (Class<?> clazz : classes) {
                if (clazz.isAnnotationPresent(Component.class)) {
                    registerBeanDefinition(clazz);
                }
            }
        }
    }
    
    private void registerBeanDefinition(Class<?> clazz) {
        Component component = clazz.getAnnotation(Component.class);
        String beanName = component.value();
        
        if (beanName.isEmpty()) {
            beanName = Character.toLowerCase(clazz.getSimpleName().charAt(0)) + clazz.getSimpleName().substring(1);
        }
        
        BeanDefinition beanDefinition = new BeanDefinition(beanName, clazz);
        
        // Scan for @Autowired constructor
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                beanDefinition.setAutowiredConstructor(constructor);
                break;
            }
        }
        
        // Scan for @Autowired fields
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                Qualifier qualifier = field.getAnnotation(Qualifier.class);
                String qualifierValue = qualifier != null ? qualifier.value() : null;
                beanDefinition.getAutowiredFields().put(field, qualifierValue);
            }
        }
        
        // Scan for @Autowired methods (setters)
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Autowired.class) && 
                method.getName().startsWith("set") && 
                method.getParameterCount() == 1) {
                
                Qualifier qualifier = method.getAnnotation(Qualifier.class);
                String qualifierValue = qualifier != null ? qualifier.value() : null;
                beanDefinition.getAutowiredSetters().put(method, qualifierValue);
            }
        }
        
        beanDefinitions.put(beanName, beanDefinition);
    }
}