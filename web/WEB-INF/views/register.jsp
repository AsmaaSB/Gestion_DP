<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <title>Inscription - Gestion des Dépenses</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">

        <style>
            body {
                font-family: 'Poppins', sans-serif;
                height: 100vh;
                margin: 0;
                display: flex;
                align-items: center;
                justify-content: center;
                background: url('${pageContext.request.contextPath}/img/background3.jpg') no-repeat center center fixed;
                background-size: cover;
            }

            .register-card {
                background: rgba(255, 255, 255, 0.05);
                backdrop-filter: blur(15px);
                border-radius: 20px;
                box-shadow: 0 8px 32px rgba(0, 0, 0, 0.25);
                padding: 40px;
                width: 100%;
                max-width: 500px;
                color: white;
                animation: fadeIn 1s ease;
            }

            .form-control {
                background: transparent;
                border: 1px solid #666;
                border-radius: 10px;
                color: white;
            }

            .form-control::placeholder {
                color: #444; /* gris foncé */
            }

            .form-control:focus {
                border-color: #999;
                font-weight: 500;
                box-shadow: 0 0 10px rgba(255, 255, 255, 0.2);
                background: transparent;
                color: white;
            }

            .btn-register {
                background: linear-gradient(135deg, #4f4f4f, #1f1f1f);
                color: white;
                border: none;
                border-radius: 30px;
                padding: 10px 0;
                font-weight: 600;
                transition: 0.3s;
            }

            .btn-register:hover {
                box-shadow: 0 0 12px rgba(255, 255, 255, 0.3);
                transform: scale(1.03);
            }

            .form-group {
                position: relative;
            }

            .form-group i {
                position: absolute;
                top: 12px;
                left: 12px;
                color: #aaa;
            }

            .form-group input,
            .form-group textarea {
                padding-left: 36px;
            }

            .register-header {
                text-align: center;
                margin-bottom: 30px;
            }

            .register-header h2 {
                font-weight: 600;
                color: #000;
            }

            .text-link {
                color: #666;
                transition: color 0.3s ease;
            }

            .text-link:hover {
                color: #999;
                text-decoration: underline;
            }

            @keyframes fadeIn {
                from {opacity: 0; transform: translateY(20px);}
                to {opacity: 1; transform: translateY(0);}
            }
        </style>
    </head>
    <body>

        <div class="register-card">
            <div class="register-header">
                <h2><i class="bi bi-person-plus"></i> Inscription</h2>
            </div>

            <% if (request.getAttribute("error") != null) {%>
            <div class="alert alert-danger">
                <%= request.getAttribute("error")%>
            </div>
            <% }%>

            <form action="${pageContext.request.contextPath}/register" method="post">
                <div class="form-group mb-3">
                    <i class="bi bi-person-fill"></i>
                    <input type="text" class="form-control" id="nom" name="nom" placeholder="Nom complet" required
                           value="${user != null ? user.nom : ''}">
                </div>
                <div class="form-group mb-3">
                    <i class="bi bi-envelope-fill"></i>
                    <input type="email" class="form-control" id="email" name="email" placeholder="Adresse e-mail" required
                           value="${user != null ? user.email : ''}">
                </div>
                <div class="form-group mb-3">
                    <i class="bi bi-lock-fill"></i>
                    <input type="password" class="form-control" id="password" name="password" placeholder="Mot de passe" required>
                </div>
                <div class="form-group mb-3">
                    <i class="bi bi-phone-fill"></i>
                    <input type="text" class="form-control" id="telephone" name="telephone" placeholder="Téléphone">
                </div>
                <div class="form-group mb-4">
                    <i class="bi bi-geo-alt-fill"></i>
                    <textarea class="form-control" id="adresse" name="adresse" placeholder="Adresse" rows="2"></textarea>
                </div>

                <button type="submit" class="btn btn-register w-100">S'inscrire</button>
            </form>

            <div class="text-center mt-4">
                <p>Déjà inscrit ?
                    <a href="${pageContext.request.contextPath}/login" class="text-link">Se connecter</a>
                </p>
            </div>
        </div>

    </body>
</html>
