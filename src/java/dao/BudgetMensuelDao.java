/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entities.BudgetMensuel;
import entities.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class BudgetMensuelDao extends AbstractDao<BudgetMensuel> {
    
    public BudgetMensuelDao() {
        super(BudgetMensuel.class);
    }
    
}
