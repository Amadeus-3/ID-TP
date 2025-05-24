package net.omar.presentation;

import net.omar.dao.IDao;
import net.omar.metier.IMetier;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.Scanner;

public class PresDynamique {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(new File("config.txt"));
        
        String daoClassName = scanner.nextLine();
        Class cDao = Class.forName(daoClassName);
        IDao dao = (IDao) cDao.newInstance();
        
        String metierClassName = scanner.nextLine();
        Class cMetier = Class.forName(metierClassName);
        IMetier metier = (IMetier) cMetier.newInstance();
        
        Method method = cMetier.getMethod("setDao", IDao.class);
        method.invoke(metier, dao);
        
        System.out.println("Résultat = " + metier.calcul());
        scanner.close();
    }
}