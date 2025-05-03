package services;

import dao.CategorieDepenseDao;
import entities.CategorieDepense;
import java.util.List;

public class CategorieService implements IService<CategorieDepense> {
    private CategorieDepenseDao dao;
    
    public CategorieService() {
        dao = new CategorieDepenseDao();
    }
    
    @Override
    public boolean create(CategorieDepense o) {
        return dao.create(o);
    }
    
    @Override
    public boolean delete(CategorieDepense o) {
        return dao.delete(o);
    }
    
    @Override
    public boolean update(CategorieDepense o) {
        return dao.update(o);
    }
    
    @Override
    public List<CategorieDepense> findAll() {
        return dao.findAll();
    }
    
    @Override
    public CategorieDepense findById(int id) {
        return dao.findById(id);
    }
    
    // Méthode pour récupérer une catégorie par son nom
    public CategorieDepense findByName(String nom) {
        List<CategorieDepense> categories = dao.findAll();
        
        for (CategorieDepense categorie : categories) {
            if (categorie.getNom().equalsIgnoreCase(nom)) {
                return categorie;
            }
        }
        
        return null;
    }
}