<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Error</h1>

        <c:if test="${!empty requestScope.msg}">
            <div class="" role="alert">
                ${requestScope.msg}
            </div>
        </c:if>

        <a href="/PortalVentas/inicio"><button type="submit" class="btn btn-warning" >Volver a inicio</button></a>

    </body>
</html>
