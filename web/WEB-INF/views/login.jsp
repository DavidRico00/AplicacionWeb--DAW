<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <meta charset="UTF-8">
        <title>Login</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    </head>

    <body class="d-flex align-items-center justify-content-center bg-light" style="height: 100vh;">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-6 col-lg-4">

                    <c:if test="${!empty requestScope.msg}">
                        <div class="alert alert-warning text-center" role="alert">
                            ${requestScope.msg}
                        </div>
                    </c:if>

                    <div class="card shadow-sm">
                        <div class="card-body p-4">
                            <h3 class="card-title text-center mb-4">Iniciar Sesión</h3>
                            <form action="/PortalVentas/login/check" method="post">
                                <div class="mb-3">
                                    <label for="email" class="form-label">Correo Electrónico</label>
                                    <input type="email" id="email" name="email" class="form-control" placeholder="usuario@ejemplo.com" required>
                                </div>
                                <div class="mb-3">
                                    <label for="password" class="form-label">Contraseña</label>
                                    <input type="password" id="password" name="password" class="form-control" placeholder="********" required>
                                </div>
                                <div class="d-grid">
                                    <button class="btn btn-primary btn-block" type="submit">Iniciar Sesión</button>
                                </div>
                            </form>
                            <div class="text-center mt-3">
                                <p class="small">¿No tienes cuenta? <a href="/PortalVentas/register">Crea una</a></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-OERcA2EmW1Dl6lTkhmPY6Rt1gWZLksB6NMRPAlClVvI1q8mZ4Bgv9KdpzV+qo2e2" crossorigin="anonymous"></script>
    </body>
</html>



<!--
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <meta charset="UTF-8">
        <title>Login</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    </head>

    <body>
        <c:if test="${!empty requestScope.msg}">
            <div class="" role="alert">
                ${requestScope.msg}
            </div>
        </c:if>

        <form action="/PortalVentas/login/check" method="post">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required>
            <label for="password">Contraseña:</label>
            <input type="password" id="password" name="password" required>

            <button class="" type="submit">Login</button>
        </form>

        <p>No tienes cuenta. <a href="/PortalVentas/register">Crea una</a><p>

    </body>

</html>
-->