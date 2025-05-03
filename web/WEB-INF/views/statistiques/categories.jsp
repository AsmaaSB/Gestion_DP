<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Statistiques - Catégories</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <style>
        canvas {
            max-width: 600px;
            margin: auto;
            display: block;
        }
    </style>
</head>
<body>

<jsp:include page="/includes/header.jsp"/>

<div class="container mt-5">
    <h2>Statistiques - Catégories</h2>

    <h4 class="mt-4">Dépenses par Catégorie</h4>
    <table class="table table-bordered">
        <thead class="table-light">
        <tr>
            <th>Catégorie</th>
            <th>Total dépensé</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${depensesParCategorie}" var="entry">
            <tr>
                <td>${entry.key}</td>
                <td><fmt:formatNumber value="${entry.value}" type="currency" currencySymbol="DH " /></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <h4 class="mt-5">Graphique Circulaire</h4>
    <canvas id="pieChart" height="300"></canvas>

    <h4 class="mt-5">Évolution des Dépenses (6 derniers mois)</h4>
    <canvas id="barChart" height="300"></canvas>
</div>

<jsp:include page="/includes/footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // Données JSON injectées par le contrôleur
    const pieChartData = ${donneesGraphiqueJson};
    const evolutionData = ${evolutionDepensesJson};

    // PIE CHART
    const pieCtx = document.getElementById('pieChart').getContext('2d');
    new Chart(pieCtx, {
        type: 'pie',
        data: {
            labels: pieChartData.labels,
            datasets: [{
                data: pieChartData.data,
                backgroundColor: [
                    'rgba(255, 99, 132, 0.6)',
                    'rgba(54, 162, 235, 0.6)',
                    'rgba(255, 206, 86, 0.6)',
                    'rgba(75, 192, 192, 0.6)',
                    'rgba(153, 102, 255, 0.6)',
                    'rgba(255, 159, 64, 0.6)'
                ]
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { position: 'top' }
            }
        }
    });

    // BAR CHART - Évolution des dépenses
    const barCtx = document.getElementById('barChart').getContext('2d');
    new Chart(barCtx, {
        type: 'bar',
        data: {
            labels: Object.keys(evolutionData),
            datasets: [{
                label: 'Montant dépensé (DH)',
                data: Object.values(evolutionData),
                backgroundColor: 'rgba(54, 162, 235, 0.7)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: false }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    title: { display: true, text: 'Montant (DH)' }
                }
            }
        }
    });
</script>

</body>
</html>
