<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <title>Mon profil</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f1f2f7;
                font-family: 'Poppins', sans-serif;
                color: #333;
            }

            .profile-card {
                background: white;
                border-radius: 15px;
                padding: 2rem;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
                margin-top: 3rem;
                animation: fadeIn 0.6s ease-out;
            }

            .profile-title {
                font-weight: 600;
                font-size: 1.8rem;
                color: #2c3e50;
                margin-bottom: 1.5rem;
                display: flex;
                align-items: center;
            }

            .profile-title i {
                font-size: 1.6rem;
                margin-right: 0.6rem;
                color: #3498db;
            }

            .form-label {
                color: #555;
                font-weight: 500;
            }

            .form-control {
                border-radius: 8px;
                padding: 0.75rem;
                border: 1px solid #ccc;
                transition: all 0.3s ease;
            }

            .form-control:focus {
                border-color: #3498db;
                box-shadow: 0 0 0 0.2rem rgba(52, 152, 219, 0.2);
            }

            .btn-save {
                background: linear-gradient(135deg, #3498db, #2980b9);
                color: white;
                border: none;
                border-radius: 30px;
                padding: 0.6rem 2rem;
                font-weight: 600;
                transition: 0.3s;
            }


            .btn-save:hover {
                box-shadow: 0 0 12px rgba(0, 0, 0, 0.2);
                transform: scale(1.03);
            }

            .alert {
                border-radius: 8px;
                font-size: 0.95rem;
            }

            @keyframes fadeIn {
                from { opacity: 0; transform: translateY(20px); }
                to { opacity: 1; transform: translateY(0); }
            }
        </style>
    </head>
    <body>
        <jsp:include page="/includes/header.jsp" />

        <div class="container">
            <div class="profile-card mx-auto" style="max-width: 600px;">
                <div class="profile-title">
                    <i class="fas fa-user-circle"></i> Mon profil
                </div>

                <!-- messages flash -->
                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success">
                        <i class="fas fa-check-circle me-2"></i>${successMessage}
                    </div>
                </c:if>
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger">
                        <i class="fas fa-exclamation-triangle me-2"></i>${errorMessage}
                    </div>
                </c:if>

                <form method="post" action="${pageContext.request.contextPath}/client/profile" class="needs-validation" novalidate>
                    <div class="mb-4">
                        <label class="form-label" for="nom">Nom complet</label>
                        <input  type="text" name="nom" id="nom" required
                                value="${user.nom}" class="form-control">
                    </div>

                    <div class="mb-4">
                        <label class="form-label" for="email">Adresse e-mail</label>
                        <input  type="email" name="email" id="email" required
                                value="${user.email}" class="form-control">
                    </div>

                    <div class="d-flex justify-content-end">
                        <button class="btn btn-save">
                            <i class="fas fa-save me-2"></i>Enregistrer
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <jsp:include page="/includes/footer.jsp" />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
