package net.omar.examples.presentation;

import net.omar.examples.dao.MockDataSource;
import net.omar.examples.service.UserService;
import net.omar.framework.ioc.core.ApplicationContext;
import net.omar.framework.ioc.core.XmlApplicationContext;

public class XmlExample {
    public static void main(String[] args) {
        System.out.println("=== Framework IoC - Example avec XML ===\n");
        
        // Create application context from XML
        ApplicationContext context = new XmlApplicationContext("classpath:framework-context.xml");
        
        // Test setter injection
        System.out.println("1. Test injection par setter:");
        UserService userServiceSetter = (UserService) context.getBean("userServiceSetter");
        System.out.println(userServiceSetter.getUserInfo(100));
        
        // Test constructor injection
        System.out.println("\n2. Test injection par constructeur:");
        UserService userServiceConstructor = (UserService) context.getBean("userServiceConstructor");
        userServiceConstructor.createUser("Jane Doe");
        
        // Test property injection with simple values
        System.out.println("\n3. Test injection de propriétés simples:");
        MockDataSource dataSource = (MockDataSource) context.getBean("dataSource");
        System.out.println("DataSource config: " + dataSource);
        
        // List all beans
        System.out.println("\n4. Liste des beans dans le contexte XML:");
        String[] beanNames = context.getBeanDefinitionNames();
        for (String name : beanNames) {
            System.out.println("- " + name);
        }
    }
}