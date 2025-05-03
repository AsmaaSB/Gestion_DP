package services;

import entities.Depense;
import entities.CategorieDepense;
import entities.User;
import entities.BudgetMensuel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatistiqueService {
    private DepenseService depenseService;
    private BudgetService budgetService;
    
    public StatistiqueService() {
        depenseService = new DepenseService();
        budgetService = new BudgetService();
    }
    
    // Obtenir la répartition des dépenses par catégorie pour un utilisateur donné sur une période
    public Map<String, Double> getDepensesByCategory(User user, int mois, int annee) {
        List<Depense> depensesMensuelles = depenseService.findByMonth(user, mois, annee);
        return depenseService.getDepensesByCategory(depensesMensuelles);
    }
    
    // Obtenir l'évolution des dépenses sur les derniers mois
    public Map<String, Double> getEvolutionDepenses(User user, int nombreMois) {
        Map<String, Double> evolution = new HashMap<>();
        
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentYear = cal.get(Calendar.YEAR);
        
        for (int i = 0; i < nombreMois; i++) {
            int month = currentMonth - i;
            int year = currentYear;
            
            // Ajustement si on remonte aux mois de l'année précédente
            if (month <= 0) {
                month += 12;
                year--;
            }
            
            List<Depense> depensesMois = depenseService.findByMonth(user, month, year);
            double totalMois = depenseService.getTotalDepenses(depensesMois);
            
            String monthKey = month + "/" + year;
            evolution.put(monthKey, totalMois);
        }
        
        return evolution;
    }
    
    // Comparaison budget vs dépenses réelles
    public Map<String, Object> compareBudgetToActual(User user, int mois, int annee) {
        Map<String, Object> result = new HashMap<>();
        
        BudgetMensuel budget = budgetService.findByMonthAndYear(user, mois, annee);
        List<Depense> depensesMois = depenseService.findByMonth(user, mois, annee);
        double totalDepenses = depenseService.getTotalDepenses(depensesMois);
        
        if (budget != null) {
            double budgetRestant = budgetService.calculateBudgetRestant(budget, totalDepenses);
            double pourcentageUtilisation = budgetService.getPourcentageUtilisation(budget, totalDepenses);
            
            result.put("budget", budget.getPlafond());
            result.put("depenses", totalDepenses);
            result.put("restant", budgetRestant);
            result.put("pourcentage", pourcentageUtilisation);
            result.put("status", budgetRestant >= 0 ? "OK" : "Dépassement");
        } else {
            result.put("budget", 0.0);
            result.put("depenses", totalDepenses);
            result.put("restant", -totalDepenses);
            result.put("pourcentage", 0.0);
            result.put("status", "Pas de budget");
        }
        
        return result;
    }
    
    // Top des catégories de dépenses
    public List<Map<String, Object>> getTopCategories(User user, int mois, int annee, int limit) {
        List<Depense> depensesMois = depenseService.findByMonth(user, mois, annee);
        Map<CategorieDepense, Double> catTotal = new HashMap<>();
        
        for (Depense d : depensesMois) {
            CategorieDepense cat = d.getCategorie();
            double montant = d.getMontant();
            
            if (catTotal.containsKey(cat)) {
                catTotal.put(cat, catTotal.get(cat) + montant);
            } else {
                catTotal.put(cat, montant);
            }
        }
        
        // Convertir en liste pour trier
        List<Map.Entry<CategorieDepense, Double>> sortedEntries = new ArrayList<>(catTotal.entrySet());
        sortedEntries.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())); // Tri décroissant
        
        // Formater le résultat
        List<Map<String, Object>> result = new ArrayList<>();
        int count = 0;
        for (Map.Entry<CategorieDepense, Double> entry : sortedEntries) {
            if (count >= limit) break;
            
            Map<String, Object> categoryInfo = new HashMap<>();
            categoryInfo.put("categorie", entry.getKey().getNom());
            categoryInfo.put("montant", entry.getValue());
            
            result.add(categoryInfo);
            count++;
        }
        
        return result;
    }
    
    // Données pour graphique de répartition des dépenses par catégorie (pour Chart.js)
    public Map<String, Object> getPieChartData(User user, int mois, int annee) {
        Map<String, Object> chartData = new HashMap<>();
        Map<String, Double> depensesByCategory = getDepensesByCategory(user, mois, annee);
        
        List<String> labels = new ArrayList<>(depensesByCategory.keySet());
        List<Double> data = new ArrayList<>();
        
        for (String label : labels) {
            data.add(depensesByCategory.get(label));
        }
        
        chartData.put("labels", labels);
        chartData.put("data", data);
        
        return chartData;
    }
}