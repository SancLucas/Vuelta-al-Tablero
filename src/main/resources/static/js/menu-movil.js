/* Menú desplegable para pantallas chicas.
   Mejora progresiva: si este script no carga, el menú queda siempre visible. */
(function () {
    var boton = document.querySelector('.menu-boton');
    var menu = document.getElementById('menu-principal');
    if (!boton || !menu) {
        return;
    }
    document.documentElement.classList.add('js');

    var texto = boton.querySelector('.menu-boton-texto');

    function mostrar(abrir) {
        menu.classList.toggle('abierto', abrir);
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
