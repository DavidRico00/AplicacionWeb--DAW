<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- header.jsp -->
<header>
    <div class="container-fluid d-flex align-items-center p-3 bg-light">

        <img src="/PortalVentas/img/logo.png" alt="Logo de PortalVentas" width="50" height="50" class="me-3">

        <h1 class="me-auto">Portal Ventas</h1>

        <div>
            <nav>
                <ul class="nav">
                    <li class="nav-item">
                        <a class="nav-link active" href="/PortalVentas/inicio">Inicio</a>
                    </li>

                    <c:if test="${empty requestScope.id}">
                        <li class="nav-item">
                            <a class="nav-link" href="/PortalVentas/login">Iniciar Sesion</a>
                        </li>
                    </c:if>

                    <c:if test="${!empty requestScope.id}">
                        <li class="nav-item">
                            <a class="nav-link" href="publicar.jsp">Publicar Producto</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link">Perfil</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/PortalVentas/login/logout">Cerrar sesion</a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </div>
    </div>
</header>
