package net.omar.examples.service;

import net.omar.examples.dao.UserDao;
import net.omar.framework.ioc.annotations.*;

@Component("userService")
public class UserServiceImpl implements UserService {
    
    // Field injection
    @Autowired
    private UserDao userDao;
    
    // For XML configuration
    public UserServiceImpl() {
    }
    
    // Constructor injection
    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
        System.out.println("UserService created with constructor injection");
    }
    
    @Override
    public String getUserInfo(int id) {
        return "Service: " + userDao.findUserById(id);
    }
    
    @Override
    public void createUser(String username) {
        System.out.println("Service creating user: " + username);
        userDao.saveUser(username);
    }
    
    // Setter injection
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
        System.out.println("UserDao injected via setter");
    }
}