package test;

import dao.UserDao;
import dao.DepenseDao;
import dao.CategorieDepenseDao;
import entities.CategorieDepense;
import entities.Depense;
import entities.User;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author user
 */
public class Test {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        DepenseDao expenseDao = new DepenseDao();
        CategorieDepenseDao categoryDao = new CategorieDepenseDao();
        
        // Create a user
        User user = new User("Asmaa", "asmaasb@example.com", "password1");
        userDao.create(user);
        
        // Create a category
        CategorieDepense categorie = new CategorieDepense("Groceries");
        categoryDao.create(categorie);
        
        // Create an expense
        Depense expense = new Depense();
        expense.setMontant(75.50);
        expense.setDescription("Weekly grocery shopping");
        expense.setDate(new Date());
        expense.setUser(user);
        expense.setCategorie(categorie);
        expenseDao.create(expense);
        
        System.out.println("Expense recorded successfully!");
        
        // Use HQL queries to retrieve data
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        // Find all expenses for a specific category
        String categoryName = "Groceries";
        String hql1 = "SELECT e FROM Expense e WHERE e.category.name = :categoryName";
        Query query1 = session.createQuery(hql1);
        query1.setParameter("categoryName", categoryName);
        List<Depense> expenses = query1.list();
        
        System.out.println("\nExpenses in category " + categoryName + ":");
        for (Depense e : expenses) {
            System.out.println("- " + e.getDescription() + " ($" + e.getMontant() + ")");
        }
        
        // Find all categories used by a specific user
        String username = "john_doe";
        String hql2 = "SELECT DISTINCT e.category FROM Expense e WHERE e.user.username = :username";
        Query query2 = session.createQuery(hql2);
        query2.setParameter("username", username);
        List<Categorie> categories = query2.list();
        
        System.out.println("\nCategories used by user " + username + ":");
        for (Categorie c : categories) {
            System.out.println("- " + c.getNom() + ": " + c.getDescription());
        }
        
        // Calculate total expenses for a user
        String hql3 = "SELECT SUM(e.amount) FROM Expense e WHERE e.user.username = :username";
        Query query3 = session.createQuery(hql3);
        query3.setParameter("username", username);
        Double totalExpenses = (Double) query3.uniqueResult();
        
        System.out.println("\nTotal expenses for " + username + ": $" + totalExpenses);
        
        session.close();
        HibernateUtil.getSessionFactory().close();
    }

    private static class Categorie {

        public Categorie() {
        }

        private String getNom() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        private String getDescription() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}