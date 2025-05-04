package controller;

import entities.User;
import services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "ProfileController", urlPatterns = {"/client/profile"})
public class ProfileController extends HttpServlet {

    private final UserService userService = new UserService();

    /* ----------- AFFICHAGE ----------- */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // on remet l’utilisateur dans la requête pour JSP
        request.setAttribute("user", session.getAttribute("user"));
        request.getRequestDispatcher("/WEB-INF/views/client/profile.jsp")
               .forward(request, response);
    }

    /* ----------- MISE À JOUR ----------- */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        /* 1. Récupération des champs */
        String nom   = request.getParameter("nom");
        String email = request.getParameter("email");

        /* 2. Validation basique */
        if (nom == null || nom.trim().isEmpty() ||
            email == null || email.trim().isEmpty()) {

            request.setAttribute("errorMessage", "Tous les champs sont obligatoires.");
            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/views/client/profile.jsp")
                   .forward(request, response);
            return;
        }

        /* 3. Mise à jour + persistance */
        user.setNom(nom.trim());
        user.setEmail(email.trim());

        boolean ok = userService.update(user);   // ton DAO JPA ou Hibernate

        if (ok) {
            session.setAttribute("user", user);  // refléter la session
            request.setAttribute("successMessage", "Profil mis à jour avec succès.");
        } else {
            request.setAttribute("errorMessage", "Une erreur est survenue, réessayez.");
        }

        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/client/profile.jsp")
               .forward(request, response);
    }
}
