package test;

import dao.AdminDao;
import dao.BudgetMensuelDao;
import dao.CategorieDepenseDao;
import dao.ClientDao;
import dao.DepenseDao;
import entities.Admin;
import entities.BudgetMensuel;
import entities.CategorieDepense;
import entities.Client;
import entities.Depense;
import java.util.Date;
import util.HibernateUtil;

/**
 *
 * @author user
 */
public class Test {

    public static void main(String[] args) {

        HibernateUtil.getSessionFactory();

        AdminDao adminDao = new AdminDao();
        ClientDao clientDao = new ClientDao();
        DepenseDao depenseDao = new DepenseDao();
        CategorieDepenseDao categorieDao = new CategorieDepenseDao();
        BudgetMensuelDao budgetDao = new BudgetMensuelDao();


        Admin admin = new Admin("SuperAdmin", "admin@example.com", "adminPass");
        adminDao.create(admin);
        System.out.println("Admin créé: " + admin.getNom());


        Client client1 = new Client("Asmaa", "asmaa@example.com", "password123");
        Client client2 = new Client("Karim", "karim@example.com", "password456");
        clientDao.create(client1);
        clientDao.create(client2);
        System.out.println("Clients créés: " + client1.getNom() + ", " + client2.getNom());


        CategorieDepense categorie1 = new CategorieDepense("Alimentation");
        CategorieDepense categorie2 = new CategorieDepense("Transport");
        CategorieDepense categorie3 = new CategorieDepense("Loisirs");
        categorieDao.create(categorie1);
        categorieDao.create(categorie2);
        categorieDao.create(categorie3);
        System.out.println("Catégories créées: " + categorie1.getNom() + ", " + categorie2.getNom() + ", " + categorie3.getNom());


        BudgetMensuel budget1 = new BudgetMensuel(4, 2025, 1000.0, client1);
        BudgetMensuel budget2 = new BudgetMensuel(4, 2025, 1500.0, client2);
        budgetDao.create(budget1);
        budgetDao.create(budget2);
        System.out.println("Budgets créés pour: " + client1.getNom() + " (" + budget1.getPlafond() + "€), "
                + client2.getNom() + " (" + budget2.getPlafond() + "€)");


        Depense depense1 = new Depense();
        depense1.setMontant(85.50);
        depense1.setDescription("Courses hebdomadaires");
        depense1.setDate(new Date());
        depense1.setUser(client1);
        depense1.setCategorie(categorie1);

        Depense depense2 = new Depense();
        depense2.setMontant(35.00);
        depense2.setDescription("Essence");
        depense2.setDate(new Date());
        depense2.setUser(client1);
        depense2.setCategorie(categorie2);

        Depense depense3 = new Depense();
        depense3.setMontant(120.00);
        depense3.setDescription("Restaurant avec amis");
        depense3.setDate(new Date());
        depense3.setUser(client2);
        depense3.setCategorie(categorie1);

        Depense depense4 = new Depense();
        depense4.setMontant(45.00);
        depense4.setDescription("Cinéma");
        depense4.setDate(new Date());
        depense4.setUser(client2);
        depense4.setCategorie(categorie3);

        depenseDao.create(depense1);
        depenseDao.create(depense2);
        depenseDao.create(depense3);
        depenseDao.create(depense4);


        System.out.println("\nToutes les dépenses :");
        for (Depense depense : depenseDao.findAll()) {
            System.out.println("Description : " + depense.getDescription()
                    + ", Montant : " + depense.getMontant()
                    + ", Utilisateur : " + depense.getUser().getNom()
                    + ", Catégorie : " + depense.getCategorie().getNom()
                    + ", Date : " + depense.getDate());
        }


        System.out.println("\nDépenses de l'utilisateur Asmaa :");
        for (Depense depense : depenseDao.findAll()) {
            if (depense.getUser().getId() == client1.getId()) {
                System.out.println("Description : " + depense.getDescription()
                        + ", Montant : " + depense.getMontant()
                        + ", Catégorie : " + depense.getCategorie().getNom()
                        + ", Date : " + depense.getDate());
            }
        }


        System.out.println("\nDépenses dans la catégorie Alimentation :");
        for (Depense depense : depenseDao.findAll()) {
            if (depense.getCategorie().getId() == categorie1.getId()) {
                System.out.println("Description : " + depense.getDescription()
                        + ", Montant : " + depense.getMontant()
                        + ", Utilisateur : " + depense.getUser().getNom()
                        + ", Date : " + depense.getDate());
            }
        }


        System.out.println("\nTotal des dépenses par utilisateur :");

        double totalClient1 = 0;
        for (Depense depense : depenseDao.findAll()) {
            if (depense.getUser().getId() == client1.getId()) {
                totalClient1 += depense.getMontant();
            }
        }
        System.out.println(client1.getNom() + " : " + totalClient1 + " €");

        double totalClient2 = 0;
        for (Depense depense : depenseDao.findAll()) {
            if (depense.getUser().getId() == client2.getId()) {
                totalClient2 += depense.getMontant();
            }
        }
        System.out.println(client2.getNom() + " : " + totalClient2 + " €");


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


        System.out.println("\nStatut des budgets mensuels :");
        checkBudgetStatus(client1, budget1, totalClient1);
        checkBudgetStatus(client2, budget2, totalClient2);

        HibernateUtil.getSessionFactory().close();
    }

    private static void checkBudgetStatus(Client client, BudgetMensuel budget, double totalDepenses) {
        double remainingBudget = budget.getPlafond() - totalDepenses;
        System.out.println(client.getNom() + " - Budget: " + budget.getPlafond()
                + " €, Dépensé: " + totalDepenses + " €, Reste: " + remainingBudget + " €");

        if (remainingBudget < 0) {
            System.out.println("  ALERTE: Budget dépassé de " + Math.abs(remainingBudget) + " €!");
        } else if (remainingBudget < (budget.getPlafond() * 0.1)) {
            System.out.println("  ATTENTION: Moins de 10% du budget restant!");
        }
    }
}
