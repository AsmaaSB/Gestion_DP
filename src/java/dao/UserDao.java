/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entities.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class UserDao extends AbstractDao<User> {
    
    public UserDao() {
        super(User.class);
    }
    
}