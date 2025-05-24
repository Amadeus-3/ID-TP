package net.omar.framework.ioc.core;

import net.omar.framework.ioc.xml.*;
import jakarta.xml.bind.*;
import java.io.File;
import java.io.InputStream;
import java.util.*;

public class XmlApplicationContext extends AbstractApplicationContext {
    
    private final String configLocation;
    
    public XmlApplicationContext(String configLocation) {
        this.configLocation = configLocation;
        refresh();
    }
    
    public void refresh() {
        loadBeanDefinitions();
    }
    
    @Override
    protected void loadBeanDefinitions() {
        try {
            JAXBContext context = JAXBContext.newInstance(Beans.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            
            Beans beans;
            if (configLocation.startsWith("classpath:")) {
                String resourcePath = configLocation.substring("classpath:".length());
                InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
                beans = (Beans) unmarshaller.unmarshal(is);
            } else {
                beans = (Beans) unmarshaller.unmarshal(new File(configLocation));
            }
            
            if (beans.getBeans() != null) {
                for (Bean bean : beans.getBeans()) {
                    registerBeanDefinition(bean);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load XML configuration from " + configLocation, e);
        }
    }
    
    private void registerBeanDefinition(Bean bean) throws ClassNotFoundException {
        String beanName = bean.getId();
        Class<?> beanClass = Class.forName(bean.getClassName());
        
        BeanDefinition beanDefinition = new BeanDefinition(beanName, beanClass);
        beanDefinition.setScope(bean.getScope());
        
        // Process constructor arguments
        if (bean.getConstructorArgs() != null) {
            List<BeanDefinition.ConstructorArgValue> constructorArgValues = new ArrayList<>();
            for (ConstructorArg arg : bean.getConstructorArgs()) {
                constructorArgValues.add(new BeanDefinition.ConstructorArgValue(
                    arg.getIndex(),
                    arg.getType(),
                    arg.getValue(),
                    arg.getRef()
                ));
            }
            beanDefinition.setConstructorArgValues(constructorArgValues);
        }
        
        // Process properties
        if (bean.getProperties() != null) {
            List<BeanDefinition.PropertyValue> propertyValues = new ArrayList<>();
            for (Property property : bean.getProperties()) {
                propertyValues.add(new BeanDefinition.PropertyValue(
                    property.getName(),
                    property.getValue(),
                    property.getRef()
                ));
            }
            beanDefinition.setPropertyValues(propertyValues);
        }
        
        beanDefinitions.put(beanName, beanDefinition);
    }
}