package services;

import dao.DepenseDao;
import entities.Depense;
import entities.User;
import entities.CategorieDepense;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepenseService implements IService<Depense> {
    private final DepenseDao dao;
    
    public DepenseService() {
        dao = new DepenseDao();
    }
    
    @Override
    public boolean create(Depense o) {
        return dao.create(o);
    }
    
    @Override
    public boolean delete(Depense o) {
        return dao.delete(o);
    }
    
    @Override
    public boolean update(Depense o) {
        return dao.update(o);
    }
    
    @Override
    public List<Depense> findAll() {
        return dao.findAll();
    }
    
    @Override
    public Depense findById(int id) {
        return dao.findById(id);
    }
    
    // ---- Méthodes spécifiques ----
    
    public List<Depense> findByUser(User user) {
        return dao.findByUser(user);
    }
    
    public List<Depense> findByCategory(CategorieDepense categorie) {
        return dao.findByCategory(categorie);
    }
    
    public List<Depense> findByMonth(User user, int month, int year) {
        List<Depense> userDepenses = findByUser(user);
        List<Depense> depensesMensuelles = new ArrayList<>();
        
        Calendar cal = Calendar.getInstance();
        
        for (Depense depense : userDepenses) {
            cal.setTime(depense.getDate());
            int depenseMois = cal.get(Calendar.MONTH) + 1; // +1 car janvier = 0
            int depenseAnnee = cal.get(Calendar.YEAR);
            
            if (depenseMois == month && depenseAnnee == year) {
                depensesMensuelles.add(depense);
            }
        }
        
        return depensesMensuelles;
    }
    
    public double getTotalDepenses(List<Depense> depenses) {
        return depenses.stream()
                       .mapToDouble(Depense::getMontant)
                       .sum();
    }
    
    public Map<String, Double> getDepensesByCategory(List<Depense> depenses) {
        Map<String, Double> depensesByCategory = new HashMap<>();
        
        for (Depense depense : depenses) {
            String categorieName = depense.getCategorie().getNom();
            double montant = depense.getMontant();
            
            depensesByCategory.merge(categorieName, montant, Double::sum);
        }
        
        return depensesByCategory;
    }
}
