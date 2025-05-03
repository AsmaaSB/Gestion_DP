package controller;

import com.google.gson.Gson;
import entities.Depense;
import entities.User;
import entities.BudgetMensuel;
import services.DepenseService;
import services.StatistiqueService;
import services.BudgetService;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "StatistiqueController", urlPatterns = {
    "/client/statistiques",
    "/client/statistiques/data",
    "/client/vue-mensuelle"
})
public class StatistiqueController extends HttpServlet {

    private final StatistiqueService statistiqueService;
    private final DepenseService depenseService;
    private final BudgetService budgetService;
    private final Gson gson;

    public StatistiqueController() {
        this.statistiqueService = new StatistiqueService();
        this.depenseService = new DepenseService();
        this.budgetService = new BudgetService();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String userRole = (String) session.getAttribute("userRole");

        if (!"client".equals(userRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Calendar cal = Calendar.getInstance();
        int mois = cal.get(Calendar.MONTH) + 1;
        int annee = cal.get(Calendar.YEAR);

        try {
            if (request.getParameter("mois") != null) {
                mois = Integer.parseInt(request.getParameter("mois"));
            }
            if (request.getParameter("annee") != null) {
                annee = Integer.parseInt(request.getParameter("annee"));
            }
        } catch (NumberFormatException ignored) {
        }

        if (path.equals("/client/statistiques")) {
            request.setAttribute("mois", mois);
            request.setAttribute("annee", annee);
            request.setAttribute("depensesParCategorie", statistiqueService.getDepensesByCategory(currentUser, mois, annee));
            request.setAttribute("comparaisonBudget", statistiqueService.compareBudgetToActual(currentUser, mois, annee));
            request.setAttribute("topCategories", statistiqueService.getTopCategories(currentUser, mois, annee, 5));
            request.setAttribute("evolutionDepenses", statistiqueService.getEvolutionDepenses(currentUser, 6));

            String donneesGraphiqueJson = gson.toJson(statistiqueService.getPieChartData(currentUser, mois, annee));
            String evolutionDepensesJson = gson.toJson(statistiqueService.getEvolutionDepenses(currentUser, 6));
            request.setAttribute("donneesGraphiqueJson", donneesGraphiqueJson);
            request.setAttribute("evolutionDepensesJson", evolutionDepensesJson);

            request.getRequestDispatcher("/WEB-INF/views/statistiques/categories.jsp").forward(request, response);

        } else if (path.equals("/client/statistiques/data")) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Map<String, Object> data = new HashMap<>();
            data.put("pieChart", statistiqueService.getPieChartData(currentUser, mois, annee));
            data.put("evolutionDepenses", statistiqueService.getEvolutionDepenses(currentUser, 6));
            data.put("budget", statistiqueService.compareBudgetToActual(currentUser, mois, annee));

            response.getWriter().write(gson.toJson(data));

        } else if (path.equals("/client/vue-mensuelle")) {
            request.setAttribute("mois", mois);
            request.setAttribute("annee", annee);

            List<Depense> depenses = depenseService.findByMonth(currentUser, mois, annee);
            request.setAttribute("depenses", depenses);

            BudgetMensuel budget = budgetService.findByMonthAndYear(currentUser, mois, annee);
            request.setAttribute("budget", budget);

            double total = depenses.stream().mapToDouble(Depense::getMontant).sum();
            request.setAttribute("totalDepenses", total);

            request.setAttribute("moisPrecedent", mois == 1 ? 12 : mois - 1);
            request.setAttribute("anneePrecedente", mois == 1 ? annee - 1 : annee);
            request.setAttribute("moisSuivant", mois == 12 ? 1 : mois + 1);
            request.setAttribute("anneeSuivante", mois == 12 ? annee + 1 : annee);

            request.getRequestDispatcher("/WEB-INF/views/statistiques/dashboard.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String userRole = (String) session.getAttribute("userRole");
        if (!"client".equals(userRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/client/statistiques");
    }
}
