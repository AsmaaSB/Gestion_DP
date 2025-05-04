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
@WebServlet(name = "LoginController", urlPatterns = {"/login", "/logout"})
public class LoginController extends HttpServlet {

    private final UserService userService;

    public LoginController() {
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if (path.equals("/logout")) {
            HttpSession session = request.getSession();
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");

            // Forward to the shared dashboard JSP for both Admin and Client
            request.getRequestDispatcher("/WEB-INF/views/dashbord.jsp").forward(request, response);
            return;
        }

        // Find the correct login.jsp path
        String loginJspPath = "/WEB-INF/views/login.jsp";
        if (getServletContext().getResourceAsStream(loginJspPath) == null) {
            loginJspPath = "/WEB-INF/login.jsp";
        }
        request.getRequestDispatcher(loginJspPath).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        System.out.println("Login attempt with email: " + email); // Debug info

        // Check if we have any users in the database
        System.out.println("Total users in database: " + userService.findAll().size()); // Debug info

        User user = userService.authenticate(email, password);

        if (user != null) {
            System.out.println("Authentication successful for user: " + user.getNom()); // Debug info

            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getNom());

            if (user instanceof Admin) {
                session.setAttribute("userRole", "admin");
            } else if (user instanceof Client) {
                session.setAttribute("userRole", "client");
            } else {
                session.setAttribute("userRole", "user");
            }

            // Forward to the shared dashboard view
            request.getRequestDispatcher("/WEB-INF/views/dashbord.jsp").forward(request, response);

        } else {
            System.out.println("Authentication failed for email: " + email); // Debug info
            request.setAttribute("error", "Email ou mot de passe incorrect");

            // Find the correct login.jsp path
            String loginJspPath = "/WEB-INF/views/login.jsp";
            if (getServletContext().getResourceAsStream(loginJspPath) == null) {
                loginJspPath = "/WEB-INF/login.jsp";
            }
            request.getRequestDispatcher(loginJspPath).forward(request, response);
        }
    }

}