<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Nuevo producto</title>

        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">

        <link href="css/HeaderFooter.css" rel="stylesheet" type="text/css"/>
        <script src="js/functions.js"></script>
    </head>

    <body>
        <jsp:include page="comun/header.jsp" />

        <div class="container mt-5 mb-5">
            <h2>Publicar Producto</h2>
            <form action="/PortalVentas/nuevoproducto/save" method="POST" enctype="multipart/form-data">

                <div class="mb-3">
                    <label for="titulo" class="form-label">Título del Producto</label>
                    <input type="text" class="form-control" id="titulo" name="titulo" required>
                </div>

                <div class="mb-3">
                    <label for="descripcion" class="form-label">Descripción</label>
                    <textarea class="form-control" id="descripcion" name="descripcion" rows="4" required></textarea>
                </div>

                <div class="mb-3">
                    <label for="imagen" class="form-label">Subir Imagen</label>
                    <input type="file" class="form-control" id="imagen" name="imagen" required>
                </div>

                <button type="submit" class="btn btn-primary">Publicar</button>
            </form>
        </div>

        <jsp:include page="comun/footer.jsp" />
    </body>
</html>
