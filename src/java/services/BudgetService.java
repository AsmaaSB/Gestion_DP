package services;

import dao.BudgetMensuelDao;
import entities.BudgetMensuel;
import entities.User;
import java.util.ArrayList;
import java.util.List;

public class BudgetService implements IService<BudgetMensuel> {
    private BudgetMensuelDao dao;
    
    public BudgetService() {
        dao = new BudgetMensuelDao();
    }
    
    @Override
    public boolean create(BudgetMensuel o) {
        return dao.create(o);
    }
    
    @Override
    public boolean delete(BudgetMensuel o) {
        return dao.delete(o);
    }
    
    @Override
    public boolean update(BudgetMensuel o) {
        return dao.update(o);
    }
    
    @Override
    public List<BudgetMensuel> findAll() {
        return dao.findAll();
    }
    
    @Override
    public BudgetMensuel findById(int id) {
        return dao.findById(id);
    }
    
    // Méthodes spécifiques
    public BudgetMensuel findByMonthAndYear(User user, int mois, int annee) {
        List<BudgetMensuel> allBudgets = dao.findAll();
        
        for (BudgetMensuel budget : allBudgets) {
            if (budget.getUser().getId() == user.getId() && budget.getMois() == mois && budget.getAnnee() == annee) {
                return budget;
            }
        }
        
        return null;
    }
    
    public List<BudgetMensuel> findByUser(User user) {
        List<BudgetMensuel> allBudgets = dao.findAll();
        List<BudgetMensuel> userBudgets = new ArrayList<>();
        
        for (BudgetMensuel budget : allBudgets) {
            if (budget.getUser().getId() == user.getId()) {
                userBudgets.add(budget);
            }
        }
        
        return userBudgets;
    }
    
    public double calculateBudgetRestant(BudgetMensuel budget, double totalDepenses) {
        return budget.getPlafond() - totalDepenses;
    }
    
    public double getPourcentageUtilisation(BudgetMensuel budget, double totalDepenses) {
        if (budget.getPlafond() == 0) {
            return 0;
        }
        return (totalDepenses / budget.getPlafond()) * 100;
    }
}