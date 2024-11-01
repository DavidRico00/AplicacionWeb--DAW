<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
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
        
    </body>
</html>
