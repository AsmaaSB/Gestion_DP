/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author PC
 */
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "clients")
@DiscriminatorValue("CLIENT")
public class Client extends User {
    
    public Client() {
        super();
    }
    
    public Client(String nom, String email, String motDePasse) {
        super(nom, email, motDePasse);
    }
}
