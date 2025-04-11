package test;

import dao.CategorieDepenseDao;
import dao.DepenseDao;
import dao.UserDao;
import entities.CategorieDepense;
import entities.Depense;
import entities.User;
import java.util.Date;
import util.HibernateUtil;

/**
 *
 * @author user
 */
public class Test {
    public static void main(String[] args) {
        // Initialize Hibernate session
        HibernateUtil.getSessionFactory();
        
        // Initialize DAOs
        UserDao userDao = new UserDao();
        DepenseDao depenseDao = new DepenseDao();
        CategorieDepenseDao categorieDao = new CategorieDepenseDao();

        // Create users
        User user1 = new User("Asmaa", "asmaa@example.com", "password123");
        User user2 = new User("Karim", "karim@example.com", "password456");
        userDao.create(user1);
        userDao.create(user2);
        
        // Create expense categories
        CategorieDepense categorie1 = new CategorieDepense("Alimentation");
        CategorieDepense categorie2 = new CategorieDepense("Transport");
        CategorieDepense categorie3 = new CategorieDepense("Loisirs");
        categorieDao.create(categorie1);
        categorieDao.create(categorie2);
        categorieDao.create(categorie3);

        // Create expenses
        Depense depense1 = new Depense();
        depense1.setMontant(85.50);
        depense1.setDescription("Courses hebdomadaires");
        depense1.setDate(new Date());
        depense1.setUser(user1);
        depense1.setCategorie(categorie1);
        
        Depense depense2 = new Depense();
        depense2.setMontant(35.00);
        depense2.setDescription("Essence");
        depense2.setDate(new Date());
        depense2.setUser(user1);
        depense2.setCategorie(categorie2);
        
        Depense depense3 = new Depense();
        depense3.setMontant(120.00);
        depense3.setDescription("Restaurant avec amis");
        depense3.setDate(new Date());
        depense3.setUser(user2);
        depense3.setCategorie(categorie1);
        
        Depense depense4 = new Depense();
        depense4.setMontant(45.00);
        depense4.setDescription("Cinéma");
        depense4.setDate(new Date());
        depense4.setUser(user2);
        depense4.setCategorie(categorie3);

        depenseDao.create(depense1);
        depenseDao.create(depense2);
        depenseDao.create(depense3);
        depenseDao.create(depense4);

        // Display all expenses
        System.out.println("\nToutes les dépenses :");
        for (Depense depense : depenseDao.findAll()) {
            System.out.println("Description : " + depense.getDescription() + 
                             ", Montant : " + depense.getMontant() + 
                             ", Utilisateur : " + depense.getUser().getNom() + 
                             ", Catégorie : " + depense.getCategorie().getNom() + 
                             ", Date : " + depense.getDate());
        }

        // Display expenses for a specific user
        System.out.println("\nDépenses de l'utilisateur Asmaa :");
        for (Depense depense : depenseDao.findAll()) {
            if (depense.getUser().getId() == user1.getId()) {
                System.out.println("Description : " + depense.getDescription() + 
                                 ", Montant : " + depense.getMontant() + 
                                 ", Catégorie : " + depense.getCategorie().getNom() + 
                                 ", Date : " + depense.getDate());
            }
        }

        // Display expenses for a specific category
        System.out.println("\nDépenses dans la catégorie Alimentation :");
        for (Depense depense : depenseDao.findAll()) {
            if (depense.getCategorie().getId() == categorie1.getId()) {
                System.out.println("Description : " + depense.getDescription() + 
                                 ", Montant : " + depense.getMontant() + 
                                 ", Utilisateur : " + depense.getUser().getNom() + 
                                 ", Date : " + depense.getDate());
            }
        }

        // Calculate total expenses by user
        System.out.println("\nTotal des dépenses par utilisateur :");
        
        double totalUser1 = 0;
        for (Depense depense : depenseDao.findAll()) {
            if (depense.getUser().getId() == user1.getId()) {
                totalUser1 += depense.getMontant();
            }
        }
        System.out.println("Asmaa : " + totalUser1 + " €");
        
        double totalUser2 = 0;
        for (Depense depense : depenseDao.findAll()) {
            if (depense.getUser().getId() == user2.getId()) {
                totalUser2 += depense.getMontant();
            }
        }
        System.out.println("Karim : " + totalUser2 + " €");
        
        // Calculate total expenses by category
        System.out.println("\nTotal des dépenses par catégorie :");
        
        double totalCategorie1 = 0;
        for (Depense depense : depenseDao.findAll()) {
            if (depense.getCategorie().getId() == categorie1.getId()) {
                totalCategorie1 += depense.getMontant();
            }
        }
        System.out.println("Alimentation : " + totalCategorie1 + " €");
        
        double totalCategorie2 = 0;
        for (Depense depense : depenseDao.findAll()) {
            if (depense.getCategorie().getId() == categorie2.getId()) {
                totalCategorie2 += depense.getMontant();
            }
        }
        System.out.println("Transport : " + totalCategorie2 + " €");
        
        double totalCategorie3 = 0;
        for (Depense depense : depenseDao.findAll()) {
            if (depense.getCategorie().getId() == categorie3.getId()) {
                totalCategorie3 += depense.getMontant();
            }
        }
        System.out.println("Loisirs : " + totalCategorie3 + " €");
        
        // Close the session factory
        HibernateUtil.getSessionFactory().close();
    }
}