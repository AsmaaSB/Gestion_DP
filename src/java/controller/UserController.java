/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entities.Admin;
import entities.Client;
import entities.User;
import services.UserService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author PC
 */
@WebServlet(name = "UserController", urlPatterns = {
    "/register",
    "/admin/users",
    "/admin/clients",
    "/profile"
})
public class UserController extends HttpServlet {

    private final UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        HttpSession session = request.getSession(false);

        // Check authentication
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String userRole = (String) session.getAttribute("userRole");

        switch (path) {
            case "/admin/dashboard":
                if (!"admin".equals(userRole)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                request.getRequestDispatcher("/WEB-INF/admin/dashboard.jsp").forward(request, response);
                break;

            case "/client/dashboard":
                if (!"client".equals(userRole)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                request.getRequestDispatcher("/WEB-INF/client/dashboard.jsp").forward(request, response);
                break;

            case "/admin/users":
                if (!"admin".equals(userRole)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                request.setAttribute("users", userService.findAll());
                request.getRequestDispatcher("/WEB-INF/admin/users.jsp").forward(request, response);
                break;

            case "/admin/clients":
                if (!"admin".equals(userRole)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                request.setAttribute("clients", userService.findAllClients());
                request.getRequestDispatcher("/WEB-INF/admin/clients.jsp").forward(request, response);
                break;

            case "/register":
                request.getRequestDispatcher("/WEB-INF/register.jsp").forward(request, response);
                break;

            case "/profile":
                request.setAttribute("user", currentUser);
                request.getRequestDispatcher("/WEB-INF/profile.jsp").forward(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        switch (path) {
            case "/register":
                registerUser(request, response);
                break;

            case "/profile":
                updateProfile(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nom = request.getParameter("nom");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String telephone = request.getParameter("telephone");
        String adresse = request.getParameter("adresse");

        Client client = new Client(nom, email, password);

        if (userService.create(client)) {
            request.setAttribute("success", "Inscription réussie. Veuillez vous connecter.");
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Erreur lors de l'inscription. Veuillez réessayer.");
            request.setAttribute("user", client);
            request.getRequestDispatcher("/WEB-INF/register.jsp").forward(request, response);
        }
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        int userId = currentUser.getId();

        String nom = request.getParameter("nom");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        currentUser.setNom(nom);
        currentUser.setEmail(email);

        if (password != null && !password.trim().isEmpty()) {
            currentUser.setMotDePasse(password);
        }

        if (currentUser instanceof Client) {
            Client client = (Client) currentUser;
            String telephone = request.getParameter("telephone");
            String adresse = request.getParameter("adresse");
        }

        if (userService.update(currentUser)) {
            session.setAttribute("user", currentUser);
            session.setAttribute("userName", currentUser.getNom());
            request.setAttribute("success", "Profil mis à jour avec succès");
        } else {
            request.setAttribute("error", "Erreur lors de la mise à jour du profil");
        }

        request.setAttribute("user", currentUser);
        request.getRequestDispatcher("/WEB-INF/profile.jsp").forward(request, response);
    }
}
