/* Encabezado: sombra al desplazarse y menú desplegable para pantallas chicas.
   Mejora progresiva: si este script no carga, el menú queda siempre visible. */
(function () {
    var encabezado = document.querySelector('.encabezado');

    // Sombra suave cuando la página ya no está arriba de todo
    if (encabezado) {
        var marcarDesplazamiento = function () {
            encabezado.classList.toggle('desplazado', window.scrollY > 4);
        };
        window.addEventListener('scroll', marcarDesplazamiento, { passive: true });
        marcarDesplazamiento();
    }

    var boton = document.querySelector('.menu-boton');
    var menu = document.getElementById('menu-principal');
    if (!encabezado || !boton || !menu) {
        return;
    }
    document.documentElement.classList.add('js');

    var texto = boton.querySelector('.menu-boton-texto');

    function mostrar(abrir) {
        menu.classList.toggle('abierto', abrir);
        encabezado.classList.toggle('menu-abierto', abrir);
        boton.setAttribute('aria-expanded', String(abrir));
        texto.textContent = abrir ? 'Cerrar' : 'Menú';
    }

    boton.addEventListener('click', function () {
        mostrar(boton.getAttribute('aria-expanded') !== 'true');
    });

    document.addEventListener('keydown', function (evento) {
        if (evento.key === 'Escape' && boton.getAttribute('aria-expanded') === 'true') {
            mostrar(false);
            boton.focus();
        }
    });
})();
