/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;
import dao.AdminDao;
import dao.ClientDao;
import dao.UserDao;
import entities.Admin;
import entities.Client;
import entities.User;
import java.util.List;
/**
 *
 * @author PC
 */
public class UserService implements IService<User> {
    
    private final UserDao userDao;
    private final AdminDao adminDao;
    private final ClientDao clientDao;
    
    public UserService() {
        this.userDao = new UserDao();
        this.adminDao = new AdminDao();
        this.clientDao = new ClientDao();
    }
    
    @Override
    public boolean create(User o) {
        if (o instanceof Admin) {
            return adminDao.create((Admin) o);
        } else if (o instanceof Client) {
            return clientDao.create((Client) o);
        } else {
            return userDao.create(o);
        }
    }
    
    @Override
    public boolean delete(User o) {
        if (o instanceof Admin) {
            return adminDao.delete((Admin) o);
        } else if (o instanceof Client) {
            return clientDao.delete((Client) o);
        } else {
            return userDao.delete(o);
        }
    }
    
    @Override
    public boolean update(User o) {
        if (o instanceof Admin) {
            return adminDao.update((Admin) o);
        } else if (o instanceof Client) {
            return clientDao.update((Client) o);
        } else {
            return userDao.update(o);
        }
    }
    
    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }
    
    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }
    
    public List<Admin> findAllAdmins() {
        return adminDao.findAll();
    }
    
    public List<Client> findAllClients() {
        return clientDao.findAll();
    }
    
    public User authenticate(String email, String password) {
        List<User> users = findAll();
        System.out.println("Attempting to authenticate user: " + email);
        System.out.println("Total users to check: " + users.size());
        
        for (User user : users) {
            System.out.println("Checking user: " + user.getEmail());
            if (user.getEmail().equals(email) && user.getMotDePasse().equals(password)) {
                System.out.println("Authentication successful for: " + user.getEmail());
                return user;
            }
        }
        System.out.println("No matching user found for email: " + email);
        return null;
    }
}