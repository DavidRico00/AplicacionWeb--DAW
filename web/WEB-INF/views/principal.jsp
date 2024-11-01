<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Inicio</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
        <link href="../../css/principal.css" rel="stylesheet" type="text/css"/>
    </head>

    <body>

        <nav class="navbar navbar-expand-lg bg-body-tertiary">
            <div class="container-fluid">
                <div class="collapse navbar-collapse" id="navbarSupportedContent">

                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item"> 
                            <a class="nav-link active" aria-current="page" href="#">Inicio</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#">Subir Producto</a>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" id="perfil" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                Perfil
                            </a>
                            <ul class="dropdown-menu" id="perfilMenu">
                                <li><a class="dropdown-item" href="#">Ver perfil</a></li>
                                <li><a class="dropdown-item" href="#">Mis productos</a></li>
                            </ul>
                        </li>
                    </ul>

                    <form class="d-flex" role="search">
                        <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
                        <button class="btn btn-outline-success" type="submit">Buscar</button>
                    </form>
                </div>
            </div>
        </nav>

        <div>
            <c:if test="${empty requestScope.products}">
                <p> No hay ningun producto subido!! </p>
            </c:if>
        </div>

        <div>
            <c:if test="${!empty requestScope.products}">
                <c:forEach var="product" items="${requestScope.products}">    
                    <div class="">
                        <table>
                            <td><img src="${pageContext.servletContext.contextPath}/img/productos/${product.id}.jpg" alt="img-${product.id}"></td>
                            <td>${product.name}</td>
                        </table>
                    </div>
                </c:forEach>
            </c:if>
        </div>

    </body>
</html>
