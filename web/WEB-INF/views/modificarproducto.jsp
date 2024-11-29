<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Modificar Producto</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

        <link href="/PortalVentas/css/HeaderFooter.css" rel="stylesheet" type="text/css"/>
    </head>

    <body>
        <jsp:include page="comun/header.jsp" />

        <div class="container mt-5 mb-5">
            <h2>Modificar Producto</h2>

            <form action="/PortalVentas/perfil/productos/savemod" method="POST" enctype="multipart/form-data">

                <div class="mb-3">
                    <label for="titulo" class="form-label">Titulo del Producto</label>
                    <input type="text" class="form-control" id="titulo" name="titulo" value="${producto.nombre}" required>
                </div>

                <div class="mb-3">
                    <label for="descripcion" class="form-label">Descripción</label>
                    <textarea class="form-control" id="descripcion" name="descripcion" rows="4" required>${producto.descripcion}</textarea>
                </div>

                <div class="mb-3">
                    <label for="imagen" class="form-label">Imagen del Producto</label>
                    <input type="file" class="form-control" id="imagen" name="imagen" accept="image/*">
                    <div class="mt-2">
                        <p>Imagen actual:</p>
                        <img src="/PortalVentas/${producto.rutaImg}" alt="${producto.nombre}" class="img-fluid" style="max-height: 200px;">
                    </div>
                </div>

                    <input type="hidden" name="idprod" value="${producto.id}">
                    <button type="submit" class="btn btn-primary">Guardar Cambios</button>
                <a href="/PortalVentas/perfil/productos" class="btn btn-secondary ms-2">Cancelar</a>
            </form>
        </div>

        <jsp:include page="comun/footer.jsp" />
    </body>
</html>
