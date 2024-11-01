<!DOCTYPE html>

<html>

<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>

<body>
    <form action="/PortalVentas/login/check" method="post">
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required>
        <label for="password">Contraseña:</label>
        <input type="password" id="password" name="password" required>

        <button type="submit">Login</button>
    </form>
    
    <p>No tienes cuenta. <a href="/PortalVentas/register">Crea una</a><p>
   
</body>

</html>