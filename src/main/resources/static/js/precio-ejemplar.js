/* Muestra el campo "Precio por día" solo si el ejemplar se ofrece en alquiler.
   Sin JavaScript el campo se ve siempre y el servidor valida igual. */
(function () {
    var casilla = document.querySelector('input[type="checkbox"][name="disponibleAlquiler"]');
    var campo = document.getElementById('campo-precio');
    if (!casilla || !campo) {
        return;
    }

    function actualizar() {
        campo.hidden = !casilla.checked;
    }

    casilla.addEventListener('change', actualizar);
    actualizar();
})();