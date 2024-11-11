document.addEventListener("DOMContentLoaded", function() {
    const btnModificar = document.getElementById("btnModificar");
    const btnGuardar = document.getElementById("btnGuardar");
    const campos = ["nombre", "email", "telefono"];

    // Función para activar el modo de edición
    function habilitarEdicion() {
        // Habilitar los campos de entrada para edición
        campos.forEach(id => {
            const campo = document.getElementById(id);
            if (campo) {
                campo.readOnly = false;
            }
        });

        // Habilitar el botón "Guardar"
        btnGuardar.disabled = false;
        btnGuardar.classList.remove("btn-outline-primary"); // Quitar clase de desactivado si tiene
        btnGuardar.classList.add("btn-primary"); // Cambiar color a activo
    }

    // Añadir el evento de clic al botón "Modificar" para activar el modo de edición
    if (btnModificar) {
        btnModificar.addEventListener("click", habilitarEdicion);
    }
});

