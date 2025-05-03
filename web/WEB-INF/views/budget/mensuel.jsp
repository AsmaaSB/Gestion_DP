<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Vue Mensuelle du Budget</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/includes/header.jsp" />

<div class="container mt-5">
    <h2 class="mb-4">Budgets Mensuels</h2>

    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success">${sessionScope.success}</div>
        <c:remove var="success" scope="session" />
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger">${sessionScope.error}</div>
        <c:remove var="error" scope="session" />
    </c:if>

    <a href="${pageContext.request.contextPath}/client/budget/add" class="btn btn-success mb-3">
        <i class="fas fa-plus"></i> Ajouter un budget
    </a>

    <table class="table table-bordered">
        <thead class="table-dark">
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
                        <a class="btn btn-sm btn-warning" href="${pageContext.request.contextPath}/client/budget/edit/${budget.id}">
                            Modifier
                        </a>
                        <a class="btn btn-sm btn-danger" href="${pageContext.request.contextPath}/client/budget/delete/${budget.id}" onclick="return confirm('Confirmer la suppression ?')">
                            Supprimer
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="/includes/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
