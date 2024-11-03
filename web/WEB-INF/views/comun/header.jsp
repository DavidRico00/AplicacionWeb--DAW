<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<header>
    <div class="container-fluid d-flex align-items-center p-3 bg-light">
        <img src="/PortalVentas/img/logo.png" alt="Logo de PortalVentas" width="50" height="50" class="me-3">
        <h1 class="me-auto">Portal Ventas</h1>
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
                <li class="nav-item position-relative">
                    <a class="nav-link">Perfil</a>
                    <ul>
                        <li class="nav-item">
                            <a class="nav-link" href="/PortalVentas/miperfil">Modificar perfil</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/PortalVentas/miperfil/productos">Ver mis productos</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/PortalVentas/login/logout">Cerrar sesión</a>
                        </li>
                    </ul>
                </li>
            </c:if>
        </ul>
    </nav>

    <form class="d-flex" role="search" style="margin-left: auto;">
        <input class="form-control me-2" type="search" placeholder="Buscar productos..." aria-label="Buscar">
        <button class="btn btn-outline-primary" type="submit">Buscar</button>
    </form>
</div>
