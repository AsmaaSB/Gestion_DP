package controller;

import entities.CategorieDepense;
import entities.Depense;
import entities.User;
import services.CategorieService;
import services.DepenseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(name = "DepenseController", urlPatterns = {
    "/client/depenses",
    "/depenses/ajouter",
    "/client/depense/edit/*",
    "/client/depense/delete/*"
})
public class DepenseController extends HttpServlet {

    private final DepenseService depenseService = new DepenseService();
    private final CategorieService categorieService = new CategorieService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        HttpSession session = request.getSession(false);

        if (!isAuthenticatedClient(request, session, response)) return;

        User currentUser = (User) session.getAttribute("user");

        switch (path) {
            case "/client/depenses":
                List<Depense> depenses = depenseService.findByUser(currentUser);
                request.setAttribute("depenses", depenses);
                request.getRequestDispatcher("/WEB-INF/views/depenses/liste.jsp").forward(request, response);
                break;

            case "/depenses/ajouter":
                List<CategorieDepense> categories = categorieService.findAll();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/WEB-INF/views/depenses/ajouter.jsp").forward(request, response);
                break;

            default:
                if (path.startsWith("/client/depense/edit/")) {
                    handleEditForm(request, response, currentUser, path);
                } else if (path.startsWith("/client/depense/delete/")) {
                    handleDelete(request, response, currentUser, path);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        HttpSession session = request.getSession(false);

        if (!isAuthenticatedClient(request, session, response)) return;

        User currentUser = (User) session.getAttribute("user");

        if ("/depenses/ajouter".equals(path) || path.startsWith("/client/depense/edit/")) {
            handleFormSubmission(request, response, currentUser, path);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
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

    private void handleEditForm(HttpServletRequest request, HttpServletResponse response, User currentUser, String path)
            throws ServletException, IOException {
        try {
            int depenseId = Integer.parseInt(path.substring("/client/depense/edit/".length()));
            Depense depense = depenseService.findById(depenseId);

            if (depense == null || depense.getUser().getId() != currentUser.getId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            List<CategorieDepense> categories = categorieService.findAll();
            request.setAttribute("categories", categories);
            request.setAttribute("depense", depense);
            request.getRequestDispatcher("/WEB-INF/views/depenses/ajouter.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response, User currentUser, String path)
            throws IOException {
        try {
            int depenseId = Integer.parseInt(path.substring("/client/depense/delete/".length()));
            Depense depense = depenseService.findById(depenseId);

            if (depense == null || depense.getUser().getId() != currentUser.getId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            boolean success = depenseService.delete(depense);
            if (success) {
                request.getSession().setAttribute("success", "Dépense supprimée avec succès");
            } else {
                request.getSession().setAttribute("error", "Erreur lors de la suppression de la dépense");
            }
            response.sendRedirect(request.getContextPath() + "/client/depenses");

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleFormSubmission(HttpServletRequest request, HttpServletResponse response, User currentUser, String path)
            throws ServletException, IOException {

        String description = request.getParameter("description");
        String montantStr = request.getParameter("montant");
        String dateStr = request.getParameter("date");
        String categorieIdStr = request.getParameter("categorie");

        if (description == null || montantStr == null || dateStr == null || categorieIdStr == null
                || description.trim().isEmpty() || montantStr.trim().isEmpty() || dateStr.trim().isEmpty() || categorieIdStr.trim().isEmpty()) {

            sendFormError(request, response, "Tous les champs sont obligatoires");
            return;
        }

        try {
            double montant = Double.parseDouble(montantStr);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            int categorieId = Integer.parseInt(categorieIdStr);

            CategorieDepense categorie = categorieService.findById(categorieId);
            if (categorie == null) throw new Exception("Catégorie non trouvée");

            Depense depense;
            boolean isNewDepense = true;

            if (path.startsWith("/client/depense/edit/")) {
                int depenseId = Integer.parseInt(path.substring("/client/depense/edit/".length()));
                depense = depenseService.findById(depenseId);

                if (depense == null || depense.getUser().getId() != currentUser.getId()) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                isNewDepense = false;
            } else {
                depense = new Depense();
                depense.setUser(currentUser);
            }

            depense.setDescription(description);
            depense.setMontant(montant);
            depense.setDate(date);
            depense.setCategorie(categorie);

            boolean success = isNewDepense ? depenseService.create(depense) : depenseService.update(depense);
            request.getSession().setAttribute("success", isNewDepense ? "Dépense ajoutée avec succès" : "Dépense mise à jour avec succès");

            response.sendRedirect(request.getContextPath() + "/client/depenses");

        } catch (NumberFormatException | ParseException e) {
            sendFormError(request, response, "Format de nombre ou de date invalide");
        } catch (Exception e) {
            sendFormError(request, response, "Erreur : " + e.getMessage());
        }
    }

    private void sendFormError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        List<CategorieDepense> categories = categorieService.findAll();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/views/depenses/ajouter.jsp").forward(request, response);
    }
}
