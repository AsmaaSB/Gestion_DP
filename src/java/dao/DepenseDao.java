/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entities.CategorieDepense;
import entities.Depense;
import entities.User;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

public class DepenseDao extends AbstractDao<Depense> {
    
    public DepenseDao() {
        super(Depense.class);
    }
    
}
