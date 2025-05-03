package controller;

import entities.Admin;
import entities.Client;
import services.UserService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "InitUserController", urlPatterns = {"/init-users"})
public class InitUserController extends HttpServlet {
    private final UserService userService;
    
    public InitUserController() {
        this.userService = new UserService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if there are already users in the database
        if (userService.findAll().isEmpty()) {
            try {
                // Create an admin user
                Admin admin = new Admin();
                admin.setEmail("admin@example.com");
                admin.setNom("Admin User");
                admin.setMotDePasse("admin123"); // Using the correct method from User entity
                
                userService.create(admin);
                
                // Create a client user
                Client client = new Client();
                client.setEmail("client@example.com");
                client.setNom("Client User");
                client.setMotDePasse("client123"); // Using the correct method from User entity
                
                userService.create(client);
                
                response.getWriter().write("Users initialized successfully! You can now login with:<br><br>" +
                        "Admin: admin@example.com / admin123<br>" +
                        "Client: client@example.com / client123");
            } catch (Exception e) {
                response.getWriter().write("Error initializing users: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            response.getWriter().write("Users already exist in the database. Total users: " + 
                    userService.findAll().size());
        }
    }
}