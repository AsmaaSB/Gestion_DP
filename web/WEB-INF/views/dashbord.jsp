<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Tableau de bord - Gestion des dépenses</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <style>
            .budget-card {
                border-radius: 10px;
                box-shadow: 0 4px 6px rgba(0,0,0,0.1);
                transition: transform 0.3s ease;
            }
            .budget-card:hover {
                transform: translateY(-5px);
            }
            .stat-card {
                min-height: 150px;
            }
            .chart-container {
                position: relative;
                height: 200px;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/includes/header.jsp" />

        <div class="container mt-4">
            <c:if test="${not empty sessionScope.success}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    ${sessionScope.success}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="success" scope="session" />
            </c:if>

            <c:if test="${not empty sessionScope.error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${sessionScope.error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="error" scope="session" />
            </c:if>

            <div class="d-flex justify-content-between align-items-center mb-4">
                <h1>Tableau de bord</h1>
                <div>
                    <h5>
                        <span class="badge bg-primary">
                            <fmt:formatDate value="${now}" pattern="MMMM yyyy" />
                        </span>
                    </h5>
                </div>
            </div>

            <div class="row mb-4">
                <div class="col-md-6 mb-3">
                    <div class="card budget-card h-100">
                        <div class="card-header bg-primary text-white">
                            <h5 class="card-title mb-0">Budget mensuel</h5>
                        </div>
                        <div class="card-body">
                            <div class="row mb-3">
                                <div class="col-md-6 text-center mb-3">
                                    <h6>Budget défini</h6>
                                    <h3 class="text-success">
                                        <fmt:formatNumber value="${budgetMensuel.plafond}" type="currency" currencySymbol="MAD" />
                                    </h3>
                                </div>
                                <div class="col-md-6 text-center mb-3">
                                    <h6>Dépensé ce mois</h6>
                                    <h3 class="text-danger">
                                        <fmt:formatNumber value="${totalMois}" type="currency" currencySymbol="€" />
                                    </h3>
                                </div>
                            </div>

                            <div class="progress mb-3" style="height: 25px;">
                                <div class="progress-bar ${pourcentage > 100 ? 'bg-danger' : (pourcentage > 80 ? 'bg-warning' : 'bg-success')}" 
                                     role="progressbar" 
                                     style="width: ${pourcentage > 100 ? 100 : pourcentage}%" 
                                     aria-valuenow="${pourcentage}" aria-valuemin="0" aria-valuemax="100">
                                    <fmt:formatNumber value="${pourcentage}" type="number" maxFractionDigits="1" />%
                                </div>
                            </div>

                            <c:choose>
                                <c:when test="${pourcentage > 100}">
                                    <div class="alert alert-danger">
                                        <i class="fas fa-exclamation-triangle"></i> 
                                        Vous avez dépassé votre budget de 
                                        <strong><fmt:formatNumber value="${totalMois - budgetMensuel.plafond}" type="currency" currencySymbol="MAD" /></strong>
                                    </div>
                                </c:when>
                                <c:when test="${pourcentage > 80}">
                                    <div class="alert alert-warning">
                                        <i class="fas fa-exclamation-circle"></i>
                                        Attention: Vous approchez de votre limite budgétaire
                                    </div>
                                </c:when>
                                <c:when test="${budgetMensuel.plafond == 0}">
                                    <div class="alert alert-info">
                                        <i class="fas fa-info-circle"></i>
                                        Vous n'avez pas encore défini de budget pour ce mois
                                        <a href="${pageContext.request.contextPath}/client/budget/edit/${currentMonth}/${currentYear}" class="btn btn-sm btn-primary ms-2">
                                            Définir un budget
                                        </a>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-success">
                                        <i class="fas fa-check-circle"></i>
                                        Vous gérez bien votre budget ce mois-ci!
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <div class="col-md-6 mb-3">
                    <div class="card budget-card h-100">
                        <div class="card-header bg-info text-white">
                            <h5 class="card-title mb-0">Répartition par catégorie</h5>
                        </div>
                        <div class="card-body">
                            <div class="chart-container">
                                <canvas id="depensesChart"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row mb-4">
                <div class="col-md-8 mb-3">
                    <div class="card budget-card">
                        <div class="card-header bg-secondary text-white d-flex justify-content-between align-items-center">
                            <h5 class="card-title mb-0">Dernières dépenses</h5>
                            <a href="${pageContext.request.contextPath}/client/depenses" class="btn btn-sm btn-light">
                                Voir tout
                            </a>
                        </div>
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover mb-0">
                                    <thead>
                                        <tr>
                                            <th>Date</th>
                                            <th>Description</th>
                                            <th>Catégorie</th>
                                            <th class="text-end">Montant</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${empty dernieresDepenses}">
                                                <tr>
                                                    <td colspan="4" class="text-center">Aucune dépense enregistrée</td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach items="${dernieresDepenses}" var="depense">
                                                    <tr>
                                                        <td><fmt:formatDate value="${depense.date}" pattern="dd/MM/yyyy" /></td>
                                                        <td>${depense.description}</td>
                                                        <td>${depense.categorie.nom}</td>
                                                        <td class="text-end">
                                                            <span class="fw-bold">
                                                                <fmt:formatNumber value="${depense.montant}" type="currency" currencySymbol="MAD" />
                                                            </span>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-4 mb-3">
                    <div class="card budget-card">
                        <div class="card-header bg-success text-white">
                            <h5 class="card-title mb-0">Top catégories</h5>
                        </div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${empty topCategories}">
                                    <p class="text-center my-4">Aucune donnée disponible</p>
                                </c:when>
                                <c:otherwise>
                                    <ul class="list-group">
                                        <c:forEach items="${topCategories}" var="cat">
                                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                                ${cat.categorie}
                                                <span class="badge bg-primary rounded-pill">
                                                    <fmt:formatNumber value="${cat.montant}" type="currency" currencySymbol="MAD" />
                                                </span>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row mb-4">
                <div class="col-12">
                    <div class="d-flex justify-content-around flex-wrap">
                        <a href="${pageContext.request.contextPath}/depenses/ajouter" class="btn btn-lg btn-success m-2">
                            <i class="fas fa-plus-circle"></i> Ajouter une dépense
                        </a>

                        <a href="${pageContext.request.contextPath}/client/depenses" class="btn btn-lg btn-primary m-2">
                            <i class="fas fa-list"></i> Liste des dépenses
                        </a>
                        <a href="${pageContext.request.contextPath}/client/budgets" class="btn btn-lg btn-info m-2 text-white">
                            <i class="fas fa-calendar-alt"></i> Vue mensuelle
                        </a>
                        <a href="${pageContext.request.contextPath}/client/statistiques" class="btn btn-lg btn-warning m-2">
                            <i class="fas fa-chart-pie"></i> Statistiques détaillées
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="/includes/footer.jsp" />

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                // Pie chart for expense categories
                const ctx = document.getElementById('depensesChart').getContext('2d');
                        // Get labels and data from the server
                        const labels = [
            <c:forEach items="${pieChartData.labels}" var="label" varStatus="status">
                        "${label}"${!status.last ? ',' : ''}
            </c:forEach>
                        ];
                        const data = [
            <c:forEach items="${pieChartData.data}" var="value" varStatus="status">
                ${value}${!status.last ? ',' : ''}
            </c:forEach>
                        ];
                        // Generate random colors
                        const backgroundColors = labels.map(() = > {
                        const r = Math.floor(Math.random() * 255);
                                const g = Math.floor(Math.random() * 255);
                                const b = Math.floor(Math.random() * 255);
                                return `rgba(${r}, ${g}, ${b}, 0.7)`;
                    });

            new Chart(ctx, {
            type: 'doughnut',
                    data: {
                    labels: labels,
                            datasets: [{
                            data: data,
                                    backgroundColor: backgroundColors,
                                    borderWidth: 1
                            }]
                    },
                    options: {
                    responsive: true,
                            maintainAspectRatio: false,
                            plugins: {
                            legend: {
                            position: 'right',
                                    labels: {
                                    boxWidth: 15
                                    }
                            }
                            }
                    }
            });
            });
        </script>
    </body>
</html>