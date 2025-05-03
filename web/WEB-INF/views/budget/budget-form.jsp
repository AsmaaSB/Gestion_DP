<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Formulaire Budget</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/includes/header.jsp" />

<div class="container mt-5">
    <h2 class="mb-4">
        <c:choose>
            <c:when test="${not empty budget}">Modifier le budget</c:when>
            <c:otherwise>Ajouter un budget</c:otherwise>
        </c:choose>
    </h2>

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
        <div class="mb-3">
            <label for="mois" class="form-label">Mois (1-12)</label>
            <input type="number" id="mois" name="mois" min="1" max="12" class="form-control"
                   value="<c:out value='${budget.mois != 0 ? budget.mois : mois}'/>" required>
        </div>

        <div class="mb-3">
            <label for="annee" class="form-label">Année</label>
            <input type="number" id="annee" name="annee" class="form-control"
                   value="<c:out value='${budget.annee != 0 ? budget.annee : annee}'/>" required>
        </div>

        <div class="mb-3">
            <label for="plafond" class="form-label">Plafond (MAD)</label>
            <input type="number" step="0.01" id="plafond" name="plafond" class="form-control"
                   value="<c:out value='${budget.plafond != 0 ? budget.plafond : plafond}'/>" required>
        </div>

        <button type="submit" class="btn btn-primary">
            <c:choose>
                <c:when test="${not empty budget}">Mettre à jour</c:when>
                <c:otherwise>Ajouter</c:otherwise>
            </c:choose>
        </button>

        <a href="${pageContext.request.contextPath}/client/budgets" class="btn btn-secondary">Annuler</a>
    </form>
</div>

<jsp:include page="/includes/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
