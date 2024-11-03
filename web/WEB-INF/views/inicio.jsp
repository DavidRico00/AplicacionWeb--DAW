<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Inicio</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">

        <link href="/PortalVentas/css/HeaderFooter.css" rel="stylesheet" type="text/css"/>
    </head>

    <body>

        <jsp:include page="comun/header.jsp" />

        <div>
            <c:if test="${empty requestScope.products}">
                <p clase="" onclick="marcar(this)"> No hay ningun producto subido!! </p> 
            </c:if>
        </div>

        <div>
            <c:if test="${!empty requestScope.products}">
                <c:forEach var="product" items="${requestScope.products}">    
                    <div class="">
                        <table>
                            <td><img src="/PortalVentas/img/productos/${product.id}.jpg" alt="img-${product.id}"></td>
                            <td>${product.name}</td>
                        </table>
                    </div>
                </c:forEach>
            </c:if>
        </div>

        <jsp:include page="comun/footer.jsp" />
    </body>
</html>
