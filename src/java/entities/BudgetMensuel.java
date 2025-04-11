/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "budgets_mensuels")
public class BudgetMensuel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int mois;
    private int annee;
    private double plafond;
    
    @ManyToOne
    private User user;

    public BudgetMensuel() {
    }

    public BudgetMensuel(int mois, int annee, double plafond, User user) {
        this.mois = mois;
        this.annee = annee;
        this.plafond = plafond;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMois() {
        return mois;
    }

    public void setMois(int mois) {
        this.mois = mois;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public double getPlafond() {
        return plafond;
    }

    public void setPlafond(double plafond) {
        this.plafond = plafond;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}