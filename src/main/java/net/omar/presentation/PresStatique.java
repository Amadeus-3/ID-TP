package net.omar.presentation;

import net.omar.dao.DaoImpl;
import net.omar.metier.MetierImpl;

public class PresStatique {
    public static void main(String[] args) {
        DaoImpl dao = new DaoImpl();
        MetierImpl metier = new MetierImpl();
        metier.setDao(dao);
        System.out.println("Résultat = " + metier.calcul());
    }
}