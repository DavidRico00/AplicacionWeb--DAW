<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Perfil de Usuario</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">

        <link href="css/HeaderFooter.css" rel="stylesheet" type="text/css"/>
        <script src="js/function.js"></script>
    </head>

    <body>
        <jsp:include page="comun/header.jsp"/>

        <div class="container mt-5 mb-5">
            <h2>${producto.nombre}</h2>

            <p class="text-muted">Publicado por: ${producto.owner.name}</p>

            <div class="row my-3">
                <div class="col-md-6">
                    <img src="${producto.rutaImg}" alt="${producto.nombre}" class="img-fluid" style="width: 100%; height: auto;">
                </div>
                <div class="col-md-6">
                    <h4>Descripción</h4>
                    <p class="lead">${producto.descripcion}</p>
                </div>
            </div>

            <div class="mt-5">
                <h4>Comentarios</h4>
                <c:if test="${not empty producto.comentarios}">
                    <ul class="list-group">
                        <c:forEach var="comentario" items="${producto.comentarios}">
                            <li class="list-group-item">
                                <p class="mb-0">${comentario.text}</p>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>
                <c:if test="${empty producto.comentarios}">
                    <p>No hay comentarios aún. ¡Sé el primero en comentar!</p>
                </c:if>
            </div>

            <c:if test="${!empty requestScope.id}">
                <div class="mt-4">
                    <h5>Añadir un comentario</h5>
                    <form action="/PortalVentas/producto/addcomment" method="POST" >
                        <input type="hidden" name="id" value="${producto.id}">
                        <div class="mb-3">
                            <label for="comentario" class="form-label">Comentario</label>
                            <textarea class="form-control" id="comentario" name="comentario" rows="2" required></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">Añadir Comentario</button>
                    </form>
                </div>
            </c:if>
                
        </div>

        <jsp:include page="comun/footer.jsp" />
    </body>
</html>
