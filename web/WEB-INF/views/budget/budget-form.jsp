<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Formulaire Budget</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f5f7fa;
            color: #333;
        }

        .container {
            max-width: 700px;
        }

        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.05);
            background-color: white;
        }

        .card-header {
            background-color: #2c3e50;
            color: white;
            border-radius: 15px 15px 0 0;
            padding: 1.25rem 1.5rem;
            font-size: 1.3rem;
            font-weight: 600;
        }

        .card-body {
            padding: 2rem;
        }

        .form-label {
            font-weight: 500;
            color: #2c3e50;
        }

        .form-control {
            border-radius: 10px;
            font-size: 0.95rem;
        }

        .btn {
            border-radius: 50px;
            padding: 0.6rem 1.8rem;
            font-weight: 500;
            font-size: 0.95rem;
            text-transform: uppercase;
        }

        .btn-primary {
            background-color: #3498db;
            border: none;
        }

        .btn-secondary {
            background-color: #95a5a6;
            border: none;
        }

        .alert {
            border-radius: 10px;
            font-size: 0.9rem;
        }

        .form-title {
            margin-bottom: 2rem;
        }
    </style>
</head>
<body>

<jsp:include page="/includes/header.jsp" />

<div class="container mt-5">
    <div class="card">
        <div class="card-header">
            <c:choose>
                <c:when test="${not empty budget}">Modifier le budget</c:when>
                <c:otherwise>Ajouter un budget</c:otherwise>
            </c:choose>
        </div>
        <div class="card-body">
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <!-- Définir dynamiquement l'action du formulaire -->
            <c:choose>
                <c:when test="${not empty budget}">
                    <c:set var="formAction" value="${pageContext.request.contextPath}/client/budget/edit/${budget.id}" />
                </c:when>
                <c:otherwise>
                    <c:set var="formAction" value="${pageContext.request.contextPath}/client/budget/add" />
                </c:otherwise>
            </c:choose>

            <form method="post" action="${formAction}">
                <div class="mb-4">
                    <label for="mois" class="form-label">Mois (1-12)</label>
                    <input type="number" id="mois" name="mois" min="1" max="12" class="form-control"
                           value="<c:out value='${budget.mois != 0 ? budget.mois : mois}'/>" required>
                </div>

                <div class="mb-4">
                    <label for="annee" class="form-label">Année</label>
                    <input type="number" id="annee" name="annee" class="form-control"
                           value="<c:out value='${budget.annee != 0 ? budget.annee : annee}'/>" required>
                </div>

                <div class="mb-4">
                    <label for="plafond" class="form-label">Plafond (MAD)</label>
                    <input type="number" step="0.01" id="plafond" name="plafond" class="form-control"
                           value="<c:out value='${budget.plafond != 0 ? budget.plafond : plafond}'/>" required>
                </div>

                <div class="d-flex justify-content-between">
                    <a href="${pageContext.request.contextPath}/client/budgets" class="btn btn-secondary">
                        Annuler
                    </a>
                    <button type="submit" class="btn btn-primary">
                        <c:choose>
                            <c:when test="${not empty budget}">Mettre à jour</c:when>
                            <c:otherwise>Ajouter</c:otherwise>
                        </c:choose>
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
