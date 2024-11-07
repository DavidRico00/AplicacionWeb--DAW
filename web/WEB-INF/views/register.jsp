<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <meta charset="UTF-8">
        <title>Registro</title>
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
                            <h3 class="card-title text-center mb-4">Crear una cuenta</h3>
                            <form action="/PortalVentas/register/save" method="POST">
                                <div class="mb-3">
                                    <label for="name" class="form-label">Nombre</label>
                                    <input type="text" id="name" name="name" class="form-control" placeholder="Tu nombre completo" required>
                                </div>
                                <div class="mb-3">
                                    <label for="email" class="form-label">Correo Electrónico</label>
                                    <input type="email" id="email" name="email" class="form-control" placeholder="usuario@ejemplo.com" required>
                                </div>
                                <div class="mb-3">
                                    <label for="password" class="form-label">Contraseña</label>
                                    <input type="password" id="password" name="password" class="form-control" placeholder="********" required>
                                </div>
                                <div class="d-grid">
                                    <button type="submit" class="btn btn-primary btn-block">Registrar</button>
                                </div>
                            </form>
                            <div class="text-center mt-3">
                                <p class="small">¿Ya tienes una cuenta? <a href="/PortalVentas/login">Iniciar sesión</a></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-OERcA2EmW1Dl6lTkhmPY6Rt1gWZLksB6NMRPAlClVvI1q8mZ4Bgv9KdpzV+qo2e2" crossorigin="anonymous"></script>
    </body>
</html>