<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Inicio</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">

        <link href="/PortalVentas/css/HeaderFooter.css" rel="stylesheet" type="text/css"/>
        <link href="/PortalVentas/css/principal.css" rel="stylesheet" type="text/css"/>
        <script src="/PortalVentas/js/function.js"></script>
    </head>

    <body>
        <jsp:include page="comun/header.jsp" />

        <h2 class="mx-5 mt-2">Mis Productos</h2>

        <div class="text-center mt-4 mb-4">
            <c:if test="${empty requestScope.productos}">
                <p class="text-muted">No tienes ningún producto publicado.</p>
            </c:if>
        </div>


        <c:if test="${not empty productos}">
            <div class="row mt-2 mb-4 mx-2">
                <c:forEach var="producto" items="${productos}">
                    <div class="col-12 mb-4">
                        <div class="card">
                            <div class="row g-0">

                                <div class="col-md-3">
                                    <img src="/PortalVentas/${producto.rutaImg}" class="img-fluid normal-Img" alt="${producto.nombre}">
                                </div>

                                <div class="col-md-9">
                                    <div class="card-body">
                                        <h5 class="card-title">${producto.nombre}</h5>
                                        <p class="card-text">${producto.descripcion}</p>

                                        <div class="d-flex justify-content-end gap-2">

                                            <form action="/PortalVentas/perfil/productos/modificar" method="GET">
                                                <input type="hidden" name="idProd" value="${producto.id}">
                                                <button type="submit" class="btn btn-warning">Modificar</button>
                                            </form>

                                            <form action="/PortalVentas/perfil/productos/delete" method="POST">
                                                <input type="hidden" name="idProd" value="${producto.id}">
                                                <button type="submit" class="btn btn-danger" onclick="return confirmarEliminacion()">Eliminar</button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>


        <jsp:include page="comun/footer.jsp" />
    </body>
</html>
