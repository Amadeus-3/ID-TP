package net.omar.examples.dao;

public interface UserDao {
    String findUserById(int id);
    void saveUser(String username);
}