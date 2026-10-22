/* Formulario de alquiler: calcula el total mientras se eligen las fechas y acota la fecha de
   devolución. Es solo una ayuda: el servidor valida todo otra vez. Sin JavaScript el formulario
   funciona igual, solo que sin el resumen. */
(function () {
    var formulario = document.querySelector('form[data-precio-dia]');
    if (!formulario) {
        return;
    }

    var precioPorDia = parseFloat(formulario.dataset.precioDia);
    var diasMinimos = parseInt(formulario.dataset.diasMinimos, 10);
    var diasMaximos = parseInt(formulario.dataset.diasMaximos, 10);
    var entrega = formulario.querySelector('input[name="fechaEntrega"]');
    var devolucion = formulario.querySelector('input[name="fechaDevolucion"]');
    var resumen = formulario.querySelector('.resumen-alquiler');
    var MS_POR_DIA = 24 * 60 * 60 * 1000;

    /* "2026-10-23" -> Date a medianoche LOCAL (new Date("2026-10-23") corre un día según la zona). */
    function aFecha(texto) {
        var partes = texto.split('-');
        return new Date(parseInt(partes[0], 10), parseInt(partes[1], 10) - 1,
                        parseInt(partes[2], 10));
    }

    function aTexto(fecha) {
        var mes = String(fecha.getMonth() + 1).padStart(2, '0');
        var dia = String(fecha.getDate()).padStart(2, '0');
        return fecha.getFullYear() + '-' + mes + '-' + dia;
    }

    function sumarDias(fecha, dias) {
        return new Date(fecha.getFullYear(), fecha.getMonth(), fecha.getDate() + dias);
    }

    function formatear(importe) {
        return importe.toLocaleString('es-AR', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        });
    }

    function actualizar() {
        var texto = '';
        if (entrega.value) {
            var desde = aFecha(entrega.value);
            devolucion.min = aTexto(sumarDias(desde, diasMinimos));
            devolucion.max = aTexto(sumarDias(desde, diasMaximos));
            if (devolucion.value) {
                var dias = Math.round((aFecha(devolucion.value) - desde) / MS_POR_DIA);
                if (dias >= diasMinimos && dias <= diasMaximos) {
                    texto = dias + (dias === 1 ? ' día' : ' días') + ' × $ '
                          + formatear(precioPorDia) + ' = Total: $ ' + formatear(dias * precioPorDia);
                }
            }
        }
        resumen.textContent = texto;
    }

    entrega.addEventListener('change', actualizar);
    devolucion.addEventListener('change', actualizar);
    actualizar();
})();