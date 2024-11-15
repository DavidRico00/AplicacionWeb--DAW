<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<header>
    <div class="container-fluid d-flex align-items-center p-3 bg-light">
        <img src="/PortalVentas/img/logo.png" alt="Logo de PortalVentas" width="50" height="50" class="me-3">
        <h1 class="me-auto">Portal Productos</h1>
    </div>
</header>

<div class="container-fluid d-flex align-items-center p-3 bg-light">
    <nav class="me-auto">
        <ul class="nav">
            <li class="nav-item">
                <a class="nav-link active" href="/PortalVentas/inicio">Inicio</a>
            </li>

            <c:if test="${empty requestScope.id}">
                <li class="nav-item">
                    <a class="nav-link" href="/PortalVentas/login">Iniciar Sesión</a>
                </li>
            </c:if>

            <c:if test="${!empty requestScope.id}">
                <li class="nav-item">
                    <a class="nav-link" href="/PortalVentas/nuevoproducto">Publicar Producto</a>
                </li>

                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="perfilDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        Perfil
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="perfilDropdown" style="padding: 10px;">
                        <li><a class="dropdown-item" href="/PortalVentas/perfil" style="padding: 0px 5px;">Mi perfil</a></li>
                        <li><a class="dropdown-item" href="/PortalVentas/perfil/productos" style="padding: 0px 5px;">Mis productos</a></li>
                        <li><a class="dropdown-item" href="/PortalVentas/login/logout" style="padding: 0px 5px;">Cerrar sesión</a></li>
                    </ul>
                </li>

            </c:if>
        </ul>
    </nav>

    <form class="d-flex" role="search" style="margin-left: auto;" action="/PortalVentas/inicio">
        <input class="form-control me-2" type="search" placeholder="Buscar productos..." name="query" aria-label="Buscar">
        <button class="btn btn-outline-primary" type="submit" href>Buscar</button>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
