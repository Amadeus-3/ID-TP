package net.omar.examples.dao;

import net.omar.framework.ioc.annotations.Component;

@Component("userDao")
public class UserDaoImpl implements UserDao {
    
    @Override
    public String findUserById(int id) {
        System.out.println("Finding user with id: " + id);
        return "User" + id;
    }
    
    @Override
    public void saveUser(String username) {
        System.out.println("Saving user: " + username);
    }
}