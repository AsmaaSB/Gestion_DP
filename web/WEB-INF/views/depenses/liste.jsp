<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Liste des dépenses</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f5f7fa;
            color: #333;
        }

        .container {
            max-width: 1000px;
        }

        h2 {
            font-weight: 600;
            color: #2c3e50;
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
            padding: 1rem 1.5rem;
            font-size: 1.25rem;
            font-weight: 600;
        }

        .table th {
            background-color: #2c3e50;
            color: white;
        }

        .table td {
            vertical-align: middle;
        }

        .btn {
            border-radius: 50px;
            padding: 0.4rem 1rem;
            font-size: 0.875rem;
        }

        .btn-success {
            background-color: #27ae60;
            border-color: #27ae60;
        }

        .btn-danger {
            background-color: #e74c3c;
            border-color: #e74c3c;
        }

        .btn-primary {
            background-color: #3498db;
            border-color: #3498db;
        }

        .alert {
            border-radius: 10px;
            font-size: 0.95rem;
        }

        .actions a {
            margin-right: 5px;
        }

        .no-data {
            text-align: center;
            padding: 2rem 0;
            color: #888;
        }
    </style>
</head>
<body>

<jsp:include page="/includes/header.jsp" />

<div class="container mt-5">
    <div class="card">
        <div class="card-header">
            Liste des dépenses
        </div>
        <div class="card-body">
            <c:if test="${not empty sessionScope.success}">
                <div class="alert alert-success">${sessionScope.success}</div>
                <c:remove var="success" scope="session" />
            </c:if>

            <c:if test="${not empty sessionScope.error}">
                <div class="alert alert-danger">${sessionScope.error}</div>
                <c:remove var="error" scope="session" />
            </c:if>

            <table class="table table-hover align-middle">
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Description</th>
                        <th>Catégorie</th>
                        <th>Montant</th>
                        <th class="text-center">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${depenses}" var="depense">
                        <tr>
                            <td><fmt:formatDate value="${depense.date}" pattern="dd/MM/yyyy"/></td>
                            <td>${depense.description}</td>
                            <td>${depense.categorie.nom}</td>
                            <td><fmt:formatNumber value="${depense.montant}" type="currency" currencySymbol="MAD" /></td>
                            <td class="text-center actions">
                                <a href="${pageContext.request.contextPath}/client/depense/edit/${depense.id}" class="btn btn-sm btn-primary">
                                    <i class="fas fa-edit"></i> Modifier
                                </a>
                                <a href="${pageContext.request.contextPath}/client/depense/delete/${depense.id}" class="btn btn-sm btn-danger"
                                   onclick="return confirm('Confirmer la suppression ?');">
                                    <i class="fas fa-trash-alt"></i> Supprimer
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty depenses}">
                        <tr>
                            <td colspan="5" class="no-data">Aucune dépense enregistrée.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>

            <div class="text-end mt-4">
                <a href="${pageContext.request.contextPath}/depenses/ajouter" class="btn btn-success btn-lg">
                    <i class="fas fa-plus-circle"></i> Ajouter une dépense
                </a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/js/all.min.js"></script>
</body>
</html>
