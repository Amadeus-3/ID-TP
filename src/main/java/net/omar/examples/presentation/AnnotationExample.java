package net.omar.examples.presentation;

import net.omar.examples.service.UserService;
import net.omar.framework.ioc.core.AnnotationConfigApplicationContext;
import net.omar.framework.ioc.core.ApplicationContext;

public class AnnotationExample {
    public static void main(String[] args) {
        System.out.println("=== Framework IoC - Example avec Annotations ===\n");
        
        // Create application context with package scanning
        ApplicationContext context = new AnnotationConfigApplicationContext("net.omar.examples");
        
        // Get bean by name
        UserService userService = (UserService) context.getBean("userService");
        System.out.println("\n1. Test avec getBean par nom:");
        System.out.println(userService.getUserInfo(1));
        
        // Get bean by type
        UserService userService2 = context.getBean(UserService.class);
        System.out.println("\n2. Test avec getBean par type:");
        userService2.createUser("John Doe");
        
        // List all beans
        System.out.println("\n3. Liste des beans dans le contexte:");
        String[] beanNames = context.getBeanDefinitionNames();
        for (String name : beanNames) {
            System.out.println("- " + name);
        }
        
        // Verify singleton behavior
        System.out.println("\n4. Vérification du comportement singleton:");
        UserService service1 = context.getBean("userService", UserService.class);
        UserService service2 = context.getBean("userService", UserService.class);
        System.out.println("Les deux instances sont identiques: " + (service1 == service2));
    }
}