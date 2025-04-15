/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author PC
 */
import entities.Client;

public class ClientDao extends AbstractDao<Client> {
    
    public ClientDao() {
        super(Client.class);
    }
    
}
