<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Vue Mensuelle du Budget</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f5f7fa;
            color: #333;
        }

        .container {
            max-width: 960px;
        }

        h2 {
            font-weight: 600;
            margin-bottom: 2rem;
        }

        .table {
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.05);
        }

        .table th {
            background-color: #2c3e50;
            color: white;
        }

        .table td, .table th {
            vertical-align: middle;
        }

        .btn {
            border-radius: 50px;
            font-weight: 500;
            font-size: 0.9rem;
            padding: 0.4rem 1rem;
        }

        .btn-success {
            background-color: #27ae60;
            border: none;
        }

        .btn-warning {
            background-color: #f39c12;
            border: none;
        }

        .btn-danger {
            background-color: #e74c3c;
            border: none;
        }

        .alert {
            border-radius: 10px;
            font-size: 0.9rem;
        }
    </style>
</head>
<body>

<jsp:include page="/includes/header.jsp" />

<div class="container mt-5">
    <h2>Budgets Mensuels</h2>

    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success">${sessionScope.success}</div>
        <c:remove var="success" scope="session" />
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger">${sessionScope.error}</div>
        <c:remove var="error" scope="session" />
    </c:if>

    <a href="${pageContext.request.contextPath}/client/budget/add" class="btn btn-success mb-3">
        <i class="fas fa-plus me-1"></i> Ajouter un budget
    </a>

    <div class="table-responsive">
        <table class="table table-bordered align-middle text-center">
            <thead>
                <tr>
                    <th>Mois</th>
                    <th>Année</th>
                    <th>Plafond</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="budget" items="${budgets}">
                    <tr>
                        <td><c:out value="${budget.mois}" /></td>
                        <td><c:out value="${budget.annee}" /></td>
                        <td><fmt:formatNumber value="${budget.plafond}" type="currency" currencySymbol="MAD" /></td>
                        <td>
                            <a class="btn btn-sm btn-warning me-1" href="${pageContext.request.contextPath}/client/budget/edit/${budget.id}">
                                <i class="fas fa-edit me-1"></i> Modifier
                            </a>
                            <a class="btn btn-sm btn-danger" href="${pageContext.request.contextPath}/client/budget/delete/${budget.id}"
                               onclick="return confirm('Confirmer la suppression ?')">
                                <i class="fas fa-trash-alt me-1"></i> Supprimer
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty budgets}">
                    <tr>
                        <td colspan="4">Aucun budget enregistré.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
