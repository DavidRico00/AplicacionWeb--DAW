document.addEventListener("DOMContentLoaded", function () {
    const btnModificar = document.getElementById("btnModificar");
    const btnGuardar = document.getElementById("btnGuardar");
    const campos = ["nombre", "email"];
    
    function habilitarEdicion() {    
        campos.forEach(id => {
            const campo = document.getElementById(id);
            if (campo) {
                campo.readOnly = false;
            }
        });
        
        btnGuardar.disabled = false;
        btnGuardar.classList.remove("btn-outline-primary");
        btnGuardar.classList.add("btn-primary");
    }
    
    if (btnModificar) {
        btnModificar.addEventListener("click", habilitarEdicion);
    }
});

function confirmarEliminacion() {
    return confirm('¿Estás seguro de que deseas eliminar este producto?');
}

