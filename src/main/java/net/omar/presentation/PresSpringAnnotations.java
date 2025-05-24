package net.omar.presentation;

import net.omar.metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"net.omar.dao", "net.omar.metier"})
public class PresSpringAnnotations {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(PresSpringAnnotations.class);
        IMetier metier = context.getBean(IMetier.class);
        System.out.println("Résultat = " + metier.calcul());
    }
}