<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ajouter une dépense | Gestion des finances</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        /* ... (styles identiques, sans modification) ... */
        /* Tu peux garder ici le même contenu de style que dans ton code précédent */
    </style>
</head>
<body>
<jsp:include page="/includes/header.jsp" />

<!-- Bloc supprimé : .page-header contenant "Gestion des dépenses" -->

<div class="content-wrapper">
    <div class="container mt-5">
        <div class="row justify-content-center animate-fade-in-up">
            <div class="col-lg-8 col-md-10">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h3 class="mb-0"><i class="fas fa-plus-circle me-2"></i>Ajouter une dépense</h3>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">
                                <i class="fas fa-exclamation-circle"></i>
                                <div>${error}</div>
                            </div>
                        </c:if>
                        
                        <form method="post" action="${pageContext.request.contextPath}/depenses/ajouter" class="needs-validation" novalidate>
                            <!-- Champs du formulaire inchangés -->
                            <div class="mb-4">
                                <div class="form-floating">
                                    <input type="text" class="form-control" id="description" name="description" 
                                           placeholder="Description" value="${depense.description}" required>
                                    <label for="description">Description</label>
                                </div>
                                <div class="invalid-feedback">Veuillez saisir une description.</div>
                            </div>

                            <div class="mb-4">
                                <div class="form-floating icon-group">
                                    <input type="number" step="0.01" class="form-control" id="montant" 
                                           name="montant" placeholder="0.00" value="${depense.montant}" required>
                                    <label for="montant">Montant</label>
                                    <span class="input-icon"></span>
                                </div>
                                <div class="invalid-feedback">Veuillez saisir un montant valide.</div>
                            </div>

                            <div class="mb-4">
                                <div class="form-floating icon-group">
                                    <input type="date" class="form-control" id="date" name="date"
                                           value="<fmt:formatDate value='${depense.date}' pattern='yyyy-MM-dd'/>" 
                                           required>
                                    <label for="date">Date</label>
                                </div>
                                <div class="invalid-feedback">Veuillez sélectionner une date.</div>
                            </div>

                            <div class="mb-4">
                                <div class="form-floating">
                                    <select class="form-select" id="categorie" name="categorie" required>
                                        <option value="" disabled selected>Sélectionner une catégorie</option>
                                        <c:forEach items="${categories}" var="cat">
                                            <option value="${cat.id}"
                                                    <c:if test="${depense.categorie != null && depense.categorie.id == cat.id}">selected</c:if>>
                                                ${cat.nom}
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <label for="categorie">Catégorie</label>
                                </div>
                                <div class="invalid-feedback">Veuillez sélectionner une catégorie.</div>
                            </div>

                            <div class="mt-4 d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/client/depenses" class="btn btn-secondary btn-lg">
                                    <i class="fas fa-arrow-left me-2"></i>Annuler
                                </a>
                                <button type="submit" class="btn btn-success btn-lg btn-action">
                                    <i class="fas fa-save me-2"></i>Enregistrer
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

                <div class="mt-4 text-center">
                    <a href="${pageContext.request.contextPath}/client/depenses" class="text-decoration-none">
                        <i class="fas fa-list me-1"></i> Retour à la liste des dépenses
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Form validation
    (function () {
        'use strict'
        const forms = document.querySelectorAll('.needs-validation')
        Array.from(forms).forEach(form => {
            form.addEventListener('submit', event => {
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                }
                form.classList.add('was-validated')
            }, false)
        })
        const dateField = document.getElementById('date');
        if (!dateField.value) {
            const today = new Date();
            const yyyy = today.getFullYear();
            let mm = today.getMonth() + 1;
            let dd = today.getDate();
            if (mm < 10) mm = '0' + mm;
            if (dd < 10) dd = '0' + dd;
            dateField.value = `${yyyy}-${mm}-${dd}`;
        }
    })()
</script>
</body>
</html>
