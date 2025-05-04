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
import java.util.List;
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

        // For registration, no authentication needed
        if (path.equals("/register")) {
            // First check if the register.jsp exists in both possible locations
            String registerJspPath = "/WEB-INF/views/register.jsp";
            if (getServletContext().getResourceAsStream(registerJspPath) == null) {
                registerJspPath = "/WEB-INF/register.jsp";
            }

            request.getRequestDispatcher(registerJspPath).forward(request, response);
            return;
        }

        // Check authentication for other paths
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String userRole = (String) session.getAttribute("userRole");

        switch (path) {
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

            case "/profile":
                request.setAttribute("user", currentUser);
                // Try both possible profile JSP locations
                String profileJspPath = "/WEB-INF/views/profile.jsp";
                if (getServletContext().getResourceAsStream(profileJspPath) == null) {
                    profileJspPath = "/WEB-INF/profile.jsp";
                }
                request.getRequestDispatcher(profileJspPath).forward(request, response);
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

        // Check if user with this email already exists
        List<User> existingUsers = userService.findAll();
        for (User user : existingUsers) {
            if (user.getEmail().equals(email)) {
                request.setAttribute("error", "Un compte avec cet email existe déjà.");

                // Create a client object to populate form fields
                Client client = new Client(nom, email, "");
                client.setTelephone(telephone);
                client.setAdresse(adresse);
                request.setAttribute("user", client);

                // Determine the correct JSP path
                String registerJspPath = "/WEB-INF/views/register.jsp";
                if (getServletContext().getResourceAsStream(registerJspPath) == null) {
                    registerJspPath = "/WEB-INF/register.jsp";
                }
                request.getRequestDispatcher(registerJspPath).forward(request, response);
                return;
            }
        }

        // Create new client
        Client client = new Client(nom, email, password);
        client.setTelephone(telephone);
        client.setAdresse(adresse);

        System.out.println("Attempting to register: " + nom + " with email: " + email);

        if (userService.create(client)) {
            System.out.println("Registration successful for: " + email);
            request.setAttribute("success", "Inscription réussie. Veuillez vous connecter.");

            // Find the correct login.jsp path
            String loginJspPath = "/WEB-INF/views/login.jsp";
            if (getServletContext().getResourceAsStream(loginJspPath) == null) {
                loginJspPath = "/WEB-INF/login.jsp";
            }
            request.getRequestDispatcher(loginJspPath).forward(request, response);
        } else {
            System.out.println("Registration failed for: " + email);
            request.setAttribute("error", "Erreur lors de l'inscription. Veuillez réessayer.");
            request.setAttribute("user", client);

            // Find the correct register.jsp path
            String registerJspPath = "/WEB-INF/views/register.jsp";
            if (getServletContext().getResourceAsStream(registerJspPath) == null) {
                registerJspPath = "/WEB-INF/register.jsp";
            }
            request.getRequestDispatcher(registerJspPath).forward(request, response);
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
            client.setTelephone(telephone);
            client.setAdresse(adresse);
        }

        if (userService.update(currentUser)) {
            session.setAttribute("user", currentUser);
            session.setAttribute("userName", currentUser.getNom());
            request.setAttribute("success", "Profil mis à jour avec succès");
        } else {
            request.setAttribute("error", "Erreur lors de la mise à jour du profil");
        }

        request.setAttribute("user", currentUser);

        // Find the correct profile.jsp path
        String profileJspPath = "/WEB-INF/views/profile.jsp";
        if (getServletContext().getResourceAsStream(profileJspPath) == null) {
            profileJspPath = "/WEB-INF/profile.jsp";
        }
        request.getRequestDispatcher(profileJspPath).forward(request, response);
    }
}
