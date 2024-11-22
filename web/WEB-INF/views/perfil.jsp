<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Perfil de Usuario</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">

        <link href="css/HeaderFooter.css" rel="stylesheet" type="text/css"/>
        <script src="js/function.js"></script>
    </head>

    <body>
        <jsp:include page="comun/header.jsp"/>

        <c:if test="${!empty requestScope.msg}">
            <div class="alert alert-success text-center" role="alert">
                ${requestScope.msg}
            </div>
        </c:if>

        <div class="container mt-4 mb-4">
            <h2>Perfil de Usuario</h2>

            <form action="/PortalVentas/perfil/save" method="POST" id="perfilForm">

                <div class="mb-3">
                    <label for="nombre" class="form-label">Nombre</label>
                    <input type="text" class="form-control" id="nombre" name="nombre" value="${user.name}" readonly>
                </div>

                <div class="mb-3">
                    <label for="email" class="form-label">Correo Electrónico</label>
                    <input type="email" class="form-control" id="email" name="email" value="${user.email}" readonly>
                </div>

                <button type="button" class="btn btn-secondary" id="btnModificar">Modificar</button>
                <button type="submit" class="btn btn-primary ms-3" id="btnGuardar" disabled>Guardar</button>
            </form>
        </div>

        <jsp:include page="comun/footer.jsp" />
    </body>
</html>
