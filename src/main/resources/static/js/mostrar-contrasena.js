/* Muestra u oculta la contraseña con el botón del ojo.
   Funciona sobre cualquier ".campo-contrasena" que tenga un input y un ".ver-contrasena". */
document.querySelectorAll('.campo-contrasena').forEach(function (grupo) {
    var campo = grupo.querySelector('input');
    var boton = grupo.querySelector('.ver-contrasena');
    if (!campo || !boton) {
        return;
    }
    boton.addEventListener('click', function () {
        var estabaOculta = campo.type === 'password';
        campo.type = estabaOculta ? 'text' : 'password';
        boton.setAttribute('aria-pressed', String(estabaOculta));
        boton.setAttribute('aria-label', estabaOculta ? 'Ocultar contraseña' : 'Mostrar contraseña');
    });
});
