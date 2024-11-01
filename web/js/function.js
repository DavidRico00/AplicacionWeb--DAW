const perfilDropdown = document.getElementById("perfil");
const perfilMenu = document.getElementById("perfilMenu");

perfilDropdown.addEventListener("click", function(event) {
    event.preventDefault();
    perfilMenu.style.display = perfilMenu.style.display === "none" ? "block" : "none";
});

document.addEventListener("click", function(event) {
    if (!perfilDropdown.contains(event.target) && !perfilMenu.contains(event.target)) {
        perfilMenu.style.display = "none";
    }
});
