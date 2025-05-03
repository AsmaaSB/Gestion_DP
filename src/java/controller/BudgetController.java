package controller;

import entities.BudgetMensuel;
import entities.User;
import services.BudgetService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "BudgetController", urlPatterns = {
    "/client/budgets",
    "/client/budget/mensuel",
    "/client/budget/add",
    "/client/budget/edit/*",
    "/client/budget/delete/*"
})
public class BudgetController extends HttpServlet {

    private final BudgetService budgetService;

    public BudgetController() {
        this.budgetService = new BudgetService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String context = request.getContextPath();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(context + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String userRole = (String) session.getAttribute("userRole");

        if (!"client".equals(userRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (uri.equals(context + "/client/budgets") || uri.equals(context + "/client/budget/mensuel")) {
            List<BudgetMensuel> budgets = budgetService.findByUser(currentUser);
            request.setAttribute("budgets", budgets);
            request.getRequestDispatcher("/WEB-INF/views/budget/mensuel.jsp").forward(request, response);
        } else if (uri.equals(context + "/client/budget/add")) {
            request.getRequestDispatcher("/WEB-INF/views/budget/budget-form.jsp").forward(request, response);
        } else if (uri.startsWith(context + "/client/budget/edit/")) {
            try {
                String idStr = uri.substring((context + "/client/budget/edit/").length());
                int budgetId = Integer.parseInt(idStr);
                BudgetMensuel budget = budgetService.findById(budgetId);

                if (budget == null || budget.getUser().getId() != currentUser.getId()) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }

                request.setAttribute("budget", budget);
                request.getRequestDispatcher("/WEB-INF/views/budget/budget-form.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else if (uri.startsWith(context + "/client/budget/delete/")) {
            try {
                String idStr = uri.substring((context + "/client/budget/delete/").length());
                int budgetId = Integer.parseInt(idStr);
                BudgetMensuel budget = budgetService.findById(budgetId);

                if (budget == null || budget.getUser().getId() != currentUser.getId()) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }

                if (budgetService.delete(budget)) {
                    session.setAttribute("success", "Budget supprimé avec succès");
                } else {
                    session.setAttribute("error", "Erreur lors de la suppression du budget");
                }

                response.sendRedirect(context + "/client/budgets");
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String context = request.getContextPath();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(context + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String userRole = (String) session.getAttribute("userRole");

        if (!"client".equals(userRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (uri.equals(context + "/client/budget/add") || uri.startsWith(context + "/client/budget/edit/")) {
            String moisStr = request.getParameter("mois");
            String anneeStr = request.getParameter("annee");
            String plafondStr = request.getParameter("plafond");

            if (moisStr == null || anneeStr == null || plafondStr == null ||
                moisStr.trim().isEmpty() || anneeStr.trim().isEmpty() || plafondStr.trim().isEmpty()) {

                request.setAttribute("error", "Tous les champs sont obligatoires");
                request.setAttribute("mois", moisStr);
                request.setAttribute("annee", anneeStr);
                request.setAttribute("plafond", plafondStr);
                request.getRequestDispatcher("/WEB-INF/views/budget/budget-form.jsp").forward(request, response);
                return;
            }

            try {
                int mois = Integer.parseInt(moisStr);
                int annee = Integer.parseInt(anneeStr);
                double plafond = Double.parseDouble(plafondStr);

                if (mois < 1 || mois > 12) throw new Exception("Mois invalide");
                if (annee < 2000 || annee > 2100) throw new Exception("Année invalide");
                if (plafond <= 0) throw new Exception("Plafond doit être > 0");

                BudgetMensuel existingBudget = budgetService.findByMonthAndYear(currentUser, mois, annee);
                BudgetMensuel budget;
                boolean isNew = uri.equals(context + "/client/budget/add");

                if (!isNew) {
                    String idStr = uri.substring((context + "/client/budget/edit/").length());
                    int budgetId = Integer.parseInt(idStr);
                    budget = budgetService.findById(budgetId);
                    if (budget == null || budget.getUser().getId() != currentUser.getId()) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                } else {
                    if (existingBudget != null) {
                        request.setAttribute("error", "Un budget pour cette période existe déjà");
                        request.getRequestDispatcher("/WEB-INF/views/budget/budget-form.jsp").forward(request, response);
                        return;
                    }
                    budget = new BudgetMensuel();
                    budget.setUser(currentUser);
                }

                budget.setMois(mois);
                budget.setAnnee(annee);
                budget.setPlafond(plafond);

                boolean success = isNew ? budgetService.create(budget) : budgetService.update(budget);
                session.setAttribute("success", isNew ? "Budget ajouté avec succès" : "Budget mis à jour avec succès");

                if (!success) {
                    session.setAttribute("error", "Erreur lors de l'enregistrement");
                }

                response.sendRedirect(context + "/client/budgets");

            } catch (NumberFormatException e) {
                request.setAttribute("error", "Valeur numérique invalide");
                request.getRequestDispatcher("/WEB-INF/views/budget/budget-form.jsp").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("error", "Erreur : " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/views/budget/budget-form.jsp").forward(request, response);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
