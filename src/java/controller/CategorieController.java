package controller;

import entities.CategorieDepense;
import entities.User;
import services.CategorieService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CategorieController", urlPatterns = {
    "/admin/categories",
    "/admin/categorie/add",
    "/admin/categorie/edit/*",
    "/admin/categorie/delete/*"
})
public class CategorieController extends HttpServlet {

    private final CategorieService categorieService;

    public CategorieController() {
        this.categorieService = new CategorieService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        HttpSession session = request.getSession(false);

        // Vérifier l'authentification
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String userRole = (String) session.getAttribute("userRole");

        // Vérifier si l'utilisateur est un administrateur
        if (!"admin".equals(userRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (path.equals("/admin/categories")) {
            // Liste des catégories
            List<CategorieDepense> categories = categorieService.findAll();
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/WEB-INF/admin/categories.jsp").forward(request, response);
        } else if (path.equals("/admin/categorie/add")) {
            // Formulaire d'ajout de catégorie
            request.getRequestDispatcher("/WEB-INF/admin/categorie-form.jsp").forward(request, response);
        } else if (path.startsWith("/admin/categorie/edit/")) {
            // Formulaire d'édition de catégorie
            try {
                int categorieId = Integer.parseInt(path.substring("/admin/categorie/edit/".length()));
                CategorieDepense categorie = categorieService.findById(categorieId);

                if (categorie == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }

                request.setAttribute("categorie", categorie);
                request.getRequestDispatcher("/WEB-INF/admin/categorie-form.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else if (path.startsWith("/admin/categorie/delete/")) {
            // Supprimer une catégorie
            try {
                int categorieId = Integer.parseInt(path.substring("/admin/categorie/delete/".length()));
                CategorieDepense categorie = categorieService.findById(categorieId);

                if (categorie == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }

                // Vérifier si la catégorie est utilisée
                if (categorie.getDepenses() != null && !categorie.getDepenses().isEmpty()) {
                    request.getSession().setAttribute("error", "Impossible de supprimer cette catégorie car elle est utilisée par des dépenses");
                } else if (categorieService.delete(categorie)) {
                    request.getSession().setAttribute("success", "Catégorie supprimée avec succès");
                } else {
                    request.getSession().setAttribute("error", "Erreur lors de la suppression de la catégorie");
                }
                
                response.sendRedirect(request.getContextPath() + "/admin/categories");
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

        String path = request.getServletPath();
        HttpSession session = request.getSession(false);

        // Vérifier l'authentification
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String userRole = (String) session.getAttribute("userRole");

        // Vérifier si l'utilisateur est un administrateur
        if (!"admin".equals(userRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (path.equals("/admin/categorie/add") || path.startsWith("/admin/categorie/edit/")) {
            // Traitement du formulaire d'ajout/édition
            
            // Récupérer les paramètres
            String nom = request.getParameter("nom");
            
            // Valider les données
            if (nom == null || nom.trim().isEmpty()) {
                request.setAttribute("error", "Le nom de la catégorie est obligatoire");
                
                // Conserver les valeurs saisies
                request.setAttribute("nom", nom);
                
                request.getRequestDispatcher("/WEB-INF/admin/categorie-form.jsp").forward(request, response);
                return;
            }
            
            try {
                // Créer ou mettre à jour la catégorie
                CategorieDepense categorie;
                boolean isNewCategorie = true;
                
                if (path.startsWith("/admin/categorie/edit/")) {
                    int categorieId = Integer.parseInt(path.substring("/admin/categorie/edit/".length()));
                    categorie = categorieService.findById(categorieId);
                    
                    if (categorie == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    
                    isNewCategorie = false;
                } else {
                    categorie = new CategorieDepense();
                }
                
                // Mettre à jour les propriétés
                categorie.setNom(nom);
                
                boolean success;
                if (isNewCategorie) {
                    success = categorieService.create(categorie);
                    request.getSession().setAttribute("success", "Catégorie ajoutée avec succès");
                } else {
                    success = categorieService.update(categorie);
                    request.getSession().setAttribute("success", "Catégorie mise à jour avec succès");
                }
                
                if (!success) {
                    request.getSession().setAttribute("error", "Erreur lors de l'enregistrement de la catégorie");
                }
                
                response.sendRedirect(request.getContextPath() + "/admin/categories");
                
            } catch (Exception e) {
                request.setAttribute("error", "Erreur : " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/admin/categorie-form.jsp").forward(request, response);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}