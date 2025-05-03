<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des dépenses</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>

<jsp:include page="/includes/header.jsp" />

<div class="container mt-5">
    <h2 class="mb-4">Liste des dépenses</h2>

    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success">${sessionScope.success}</div>
        <c:remove var="success" scope="session" />
    </c:if>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger">${sessionScope.error}</div>
        <c:remove var="error" scope="session" />
    </c:if>

    <table class="table table-bordered table-striped">
        <thead class="table-dark">
            <tr>
                <th>Date</th>
                <th>Description</th>
                <th>Catégorie</th>
                <th>Montant</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${depenses}" var="depense">
            <tr>
                <td><fmt:formatDate value="${depense.date}" pattern="dd/MM/yyyy"/></td>
                <td>${depense.description}</td>
                <td>${depense.categorie.nom}</td>
                <td><fmt:formatNumber value="${depense.montant}" type="currency" currencySymbol="MAD" /></td>
                <td>
                    <a href="${pageContext.request.contextPath}/client/depense/edit/${depense.id}" class="btn btn-sm btn-primary">Modifier</a>
                    <a href="${pageContext.request.contextPath}/client/depense/delete/${depense.id}" class="btn btn-sm btn-danger"
                       onclick="return confirm('Confirmer la suppression ?');">Supprimer</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty depenses}">
            <tr>
                <td colspan="5" class="text-center">Aucune dépense enregistrée.</td>
            </tr>
        </c:if>
        </tbody>
    </table>

    <a href="${pageContext.request.contextPath}/depenses/ajouter" class="btn btn-success mt-3">
        Ajouter une dépense
    </a>
</div>

<jsp:include page="/includes/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
