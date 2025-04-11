/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "depenses")
public class Depense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double montant;
    
    @Temporal(TemporalType.DATE)
    private Date date;
    
    private String description;
    
    @ManyToOne
    private CategorieDepense categorie;
    
    @ManyToOne
    private User user;

    public Depense() {
    }

    public Depense(double montant, Date date, String description, CategorieDepense categorie, User user) {
        this.montant = montant;
        this.date = date;
        this.description = description;
        this.categorie = categorie;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategorieDepense getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieDepense categorie) {
        this.categorie = categorie;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
