package controller;

import entities.BudgetMensuel;
import entities.Depense;
import entities.User;
import services.BudgetService;
import services.DepenseService;
import services.StatistiqueService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@WebServlet(name = "DashboardController", urlPatterns = {
    "/client/dashboard"
})
public class DashboardController extends HttpServlet {

    private final DepenseService depenseService = new DepenseService();
    private final BudgetService budgetService = new BudgetService();
    private final StatistiqueService statistiqueService = new StatistiqueService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!isAuthenticatedClient(request, session, response)) {
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        // Get current month and year
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH) + 1; // Calendar months are 0-based
        int currentYear = cal.get(Calendar.YEAR);

        // Get budget for current month
        BudgetMensuel budgetMensuel = budgetService.findByMonthAndYear(currentUser, currentMonth, currentYear);
        
        if (budgetMensuel == null) {
            // Create an empty budget object for display purposes
            budgetMensuel = new BudgetMensuel();
            budgetMensuel.setPlafond(0.0);
            budgetMensuel.setMois(currentMonth);
            budgetMensuel.setAnnee(currentYear);
            budgetMensuel.setUser(currentUser);
        }

        // Get expenses for current month
        List<Depense> depensesMensuelles = depenseService.findByMonth(currentUser, currentMonth, currentYear);
        double totalMois = depenseService.getTotalDepenses(depensesMensuelles);

        // Calculate percentage of budget used
        double pourcentage = 0;
        if (budgetMensuel.getPlafond() > 0) {
            pourcentage = (totalMois / budgetMensuel.getPlafond()) * 100;
        }

        // Get recent expenses (last 5)
        List<Depense> dernieresDepenses = depenseService.findByUser(currentUser);
        dernieresDepenses.sort((d1, d2) -> d2.getDate().compareTo(d1.getDate())); // Sort by date descending
        
        // Limit to 5 most recent expenses
        int maxExpenses = Math.min(5, dernieresDepenses.size());
        dernieresDepenses = dernieresDepenses.subList(0, maxExpenses);

        // Get expense statistics by category
        Map<String, Object> pieChartData = statistiqueService.getPieChartData(currentUser, currentMonth, currentYear);
        List<Map<String, Object>> topCategories = statistiqueService.getTopCategories(currentUser, currentMonth, currentYear, 5);

        // Add data to request
        request.setAttribute("budgetMensuel", budgetMensuel);
        request.setAttribute("totalMois", totalMois);
        request.setAttribute("pourcentage", pourcentage);
        request.setAttribute("dernieresDepenses", dernieresDepenses);
        request.setAttribute("pieChartData", pieChartData);
        request.setAttribute("topCategories", topCategories);
        request.setAttribute("currentMonth", currentMonth);
        request.setAttribute("currentYear", currentYear);

        // Forward to dashboard JSP
        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }

    private boolean isAuthenticatedClient(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        String userRole = (String) session.getAttribute("userRole");
        if (!"client".equals(userRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }
}