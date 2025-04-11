/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entities.CategorieDepense;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class CategorieDepenseDao extends AbstractDao<CategorieDepense> {
    
    public CategorieDepenseDao() {
        super(CategorieDepense.class);
    }
    
}