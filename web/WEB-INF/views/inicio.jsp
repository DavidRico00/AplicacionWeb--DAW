<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Inicio</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">

        <link href="css/HeaderFooter.css" rel="stylesheet" type="text/css"/>
    </head>

    <body>
        <jsp:include page="comun/header.jsp" />

        <div class="text-center mt-4 mb-3">
            <c:if test="${empty requestScope.products}">
                <p class="text-muted">No hay ningún producto subido.</p>
            </c:if>
        </div>

        <div class="mb-4 mt-4">
            <div class="container mt-1" >
                <c:if test="${!empty requestScope.products}">
                    <div class="row row-cols-1 row-cols-md-5 g-4">
                        <c:forEach var="product" items="${requestScope.products}">   
                            <div class="col"> 
                                <a href="/PortalVentas/producto?id=${product.id}" style="text-decoration: none; color: inherit;">
                                    <div class="card h-100 shadow-sm">
                                        <img src="${product.rutaImg}" class="card-img-top" alt="img-${product.nombre}">
                                        <div class="card-body">
                                            <h5 class="card-title">${product.nombre}</h5>
                                        </div>
                                    </div>
                                </a>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
            </div>
        </div>

        <jsp:include page="comun/footer.jsp" />
    </body>
</html>
