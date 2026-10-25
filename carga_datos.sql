USE `vuelta_al_tablero_db`;
SET NAMES utf8mb4;

-- -----------------------------------------------------
-- Datos para la tabla `usuario`
-- -----------------------------------------------------
INSERT INTO `usuario` (`nro`, `nombre`, `apellido`, `dni`, `correo`, `contrasenia`, `telefono`, `fecha_registro`, `fecha_baja`, `fecha_bloqueo`, `rol_admin`) VALUES
(1, 'Lucas','Sanchez', '45698745', 'administracion@vueltaltablero.com', '$2a$10$U.A6CCMWWPLU0IR.1wjBN.xbgy4isXXTHmwx243EzLiyoWdn7Iu/q', '+5491148756253', '2025-10-24', NULL, NULL, 1),
(2, 'Lucas', 'Martinez', '40111222', 'lucas.martinez@email.com', '$2a$10$a9BsqZZxhJCzr5CVzNOBNeZS7FH2D0gs4JZXSEV.l.ywisxDCHvW6', '+5491133445566', '2026-01-12', NULL, NULL, 0),
(3, 'Camila', 'Rodriguez', '37444555', 'camila.rodriguez@email.com', '$2a$10$RZd6HQFqYUyPwiIw1tWjmOW8NnPq5xJipL..ZMZoyvJ9T29Q.kZm.', '+5491144556677', '2026-02-01', NULL, NULL, 0),
(4, 'Joaquín', 'Pérez', '39888999', 'joaquin.perez@gmail.com', '$2a$10$S6WiiJh201c3qf8Z8vtejuISsHlK5bYGtYX5FeTNGvGR6N1OYiDXq', '+5491155667788', '2026-02-05', NULL, NULL, 0),
(5, 'Martina', 'López', '41222333', 'martina.lopez@hotmail.com', '$2a$10$jyYfX/kltCmglhjSSUsXv.uuY2dtR38kmiZNIIcx6Bqr.1WZIPGqy', '+5491166778899', '2026-02-15', NULL, NULL, 0),
(6, 'Tomás', 'Fernandez', '36555666', 'tomas.fernandez@email.com', '$2a$10$cOVjDTANTV2MsKywDf/diuAtNMp0APDpcK/gPioRr7pD/.KjU266u', '+5491177889900', '2026-03-02', NULL, NULL, 0),
(7, 'Valentina', 'Díaz', '42333444', 'valentina.diaz@gmail.com', '$2a$10$81Kz9QShbJjJFWSHGMawLuZNK9D3srAo7pwbmWF7ijAhzQ4MlhE4a', '+5491188990011', '2026-03-10', NULL, NULL, 0),
(8, 'Mateo', 'Sanchez', '35999000', 'mateo.sanchez@gmail.com', '$2a$10$FxkN80.oPyJS3hkSDmYDG.WqZApTbGNb8c/QFX03L4ba5gOSyaFk.', '+5491199001122', '2026-03-20', NULL, NULL, 0),
(9, 'Julieta', 'Romero', '38777888', 'julieta.romero@email.com', '$2a$10$Xy28LbkIhbu7UagmjHz4cOINS5j1VYtyFg6wMrRYx.ufos7J9nE5W', '+5491100112233', '2026-04-01', NULL, NULL, 0),
(10, 'Nicolás', 'Alvarez', '39444333', 'nicolas.alvarez@email.com', '$2a$10$rlFMUVyKfH56xwMNDwsUr.J0aNL/2r6DNCJrd8XQXuiKP4Dcsu7Ge', '+5491111223344', '2026-04-05', NULL, NULL, 0),
(11, 'Sofía', 'Gómez', '38123456', 'sofia.gomez@email.com', '$2a$10$Qu/ITSaKAvRFZqGaCe5AVuj8NKgn1gaatwvVzjkb12mOacWUCOC8a', '+5491122334455', '2026-01-10', NULL, NULL, 1);

-- -----------------------------------------------------
-- Datos para la tabla `ficha_tecnica`
-- (id, max_jugadores, min_jugadores, min_edad, descripcion, anio_publicacion)
-- -----------------------------------------------------
INSERT INTO `ficha_tecnica` (`id`, `max_jugadores`, `min_jugadores`, `min_edad`, `descripcion`, `anio_publicacion`) VALUES
(1, 4, 3, 10, 'Colonizá una isla comerciando recursos y construyendo caminos, pueblos y ciudades.', 1995),
(2, 5, 2, 7, 'Armá el paisaje medieval colocando losetas y ubicá tus seguidores para sumar puntos.', 2000),
(3, 8, 2, 8, 'Comprá propiedades, cobrá alquileres y llevá a la quiebra a tus rivales.', 1935),
(4, 6, 2, 10, 'Clásico argentino de estrategia: conquistá territorios y cumplí tu objetivo secreto.', 1976),
(5, 4, 2, 8, 'Cooperá con tu equipo para curar cuatro enfermedades antes de que se desate una pandemia.', 2008),
(6, 5, 2, 8, 'Conectá ciudades con rutas de trenes y completá tus billetes de destino.', 2004),
(7, 8, 3, 8, 'Juego de imaginación: dá pistas con cartas ilustradas para que adivinen cuál es la tuya.', 2008),
(8, 5, 2, 7, 'Juego de cartas de supervivencia: evitá sacar el gatito explosivo.', 2015),
(9, 4, 2, 8, 'Formá palabras sobre el tablero combinando fichas con letras para sumar puntos.', 1948),
(10, 5, 1, 12, 'Liderá una corporación y terraformá Marte elevando temperatura, oxígeno y océanos.', 2016),
(11, 8, 2, 14, 'Dos equipos, dos jefes de espías: descubrí a tus agentes con pistas de una sola palabra.', 2015),
(12, 8, 1, 6, 'Apilá bloques de madera y retiralos de a uno sin que la torre se caiga.', 1983);

-- -----------------------------------------------------
-- Datos para la tabla `juego`
-- (id, titulo, editorial, categoria, id_ficha_tecnica, fecha_baja)
-- -----------------------------------------------------
INSERT INTO `juego` (`id`, `titulo`, `editorial`, `categoria`, `id_ficha_tecnica`, `fecha_baja`) VALUES
(1, 'Catan', 'Devir', 'Modernos', 1, NULL),
(2, 'Carcassonne', 'Devir', 'Familiares', 2, NULL),
(3, 'Monopoly', 'Hasbro', 'Clásicos', 3, NULL),
(4, 'TEG', 'Yetem', 'Clásicos', 4, NULL),
(5, 'Pandemic', 'Z-Man Games', 'Cooperativos', 5, NULL),
(6, 'Ticket to Ride', 'Days of Wonder', 'Familiares', 6, NULL),
(7, 'Dixit', 'Libellud', 'Party', 7, NULL),
(8, 'Exploding Kittens', 'Exploding Kittens', 'Cartas', 8, NULL),
(9, 'Scrabble', 'Mattel', 'Clásicos', 9, NULL),
(10, 'Terraforming Mars', 'FryxGames', 'Estrategia', 10, NULL),
(11, 'Codenames', 'Czech Games Edition', 'Party', 11, NULL),
(12, 'Jenga', 'Hasbro', 'Otros', 12, NULL);

-- -----------------------------------------------------
-- Datos para la tabla `ejemplar`
-- (id, estado, disponible_alquiler, precio_por_dia, id_juego, nro_usuario, fecha_baja)
-- Si disponible_alquiler = 0, precio_por_dia debe ser NULL (chk_ejemplar_precio).
-- -----------------------------------------------------
INSERT INTO `ejemplar` (`id`, `estado`, `disponible_alquiler`, `precio_por_dia`, `id_juego`, `nro_usuario`, `fecha_baja`) VALUES
(1, 'Nuevo', 1, 1500.00, 1, 2, NULL),
(2, 'Bueno', 1, 1200.00, 1, 5, NULL),
(3, 'Excelente', 1, 1000.00, 2, 3, NULL),
(4, 'Bueno', 0, NULL, 2, 6, NULL),
(5, 'Excelente', 1, 800.00, 3, 4, NULL),
(6, 'Dañado', 0, NULL, 3, 7, NULL),
(7, 'Bueno', 1, 900.00, 4, 8, NULL),
(8, 'Nuevo', 1, 1800.00, 5, 2, NULL),
(9, 'Excelente', 1, 1600.00, 5, 9, NULL),
(10, 'Bueno', 1, 1400.00, 6, 10, NULL),
(11, 'Nuevo', 0, NULL, 6, 3, NULL),
(12, 'Excelente', 1, 700.00, 7, 5, NULL),
(13, 'Bueno', 1, 500.00, 8, 6, NULL),
(14, 'Nuevo', 1, 600.00, 8, 4, NULL),
(15, 'Incompleto', 0, NULL, 9, 7, NULL),
(16, 'Bueno', 1, 750.00, 9, 9, NULL),
(17, 'Excelente', 1, 2000.00, 10, 8, NULL),
(18, 'Nuevo', 1, 2200.00, 10, 10, NULL),
(19, 'Excelente', 1, 650.00, 11, 2, NULL),
(20, 'Bueno', 1, 600.00, 11, 3, NULL),
(21, 'Bueno', 1, 450.00, 12, 4, NULL),
(22, 'Excelente', 0, NULL, 12, 5, NULL);

-- -----------------------------------------------------
-- Datos para la tabla `alquila`
-- (id, nro_usuario [quien alquila], id_ejemplar, precio [total], fecha_entrega,
--  fecha_devolucion_prevista, fecha_devolucion)
-- 1-15: devueltos | 16-19: en curso (el 19 está vencido) | 20-21: aún no comenzaron
-- -----------------------------------------------------
INSERT INTO `alquila` (`id`, `nro_usuario`, `id_ejemplar`, `precio`, `fecha_entrega`, `fecha_devolucion_prevista`, `fecha_devolucion`) VALUES
(1, 3, 1, 4500.00, '2026-03-10', '2026-03-13', '2026-03-13'),
(2, 4, 3, 2000.00, '2026-03-15', '2026-03-17', '2026-03-17'),
(3, 5, 8, 7200.00, '2026-04-02', '2026-04-06', '2026-04-07'),
(4, 6, 12, 1400.00, '2026-04-10', '2026-04-12', '2026-04-12'),
(5, 7, 10, 7000.00, '2026-05-01', '2026-05-06', '2026-05-05'),
(6, 8, 5, 800.00, '2026-05-20', '2026-05-21', '2026-05-21'),
(7, 2, 9, 4800.00, '2026-06-05', '2026-06-08', '2026-06-08'),
(8, 9, 14, 4200.00, '2026-06-20', '2026-06-27', '2026-06-27'),
(9, 10, 16, 1500.00, '2026-07-04', '2026-07-06', '2026-07-06'),
(10, 3, 17, 6000.00, '2026-07-18', '2026-07-21', '2026-07-21'),
(11, 6, 19, 1300.00, '2026-08-08', '2026-08-10', '2026-08-10'),
(12, 5, 21, 1800.00, '2026-08-22', '2026-08-26', '2026-08-26'),
(13, 4, 2, 3600.00, '2026-09-05', '2026-09-08', '2026-09-08'),
(14, 7, 7, 1800.00, '2026-09-12', '2026-09-14', '2026-09-14'),
(15, 2, 18, 6600.00, '2026-09-19', '2026-09-22', '2026-09-22'),
(16, 8, 1, 6000.00, '2026-10-22', '2026-10-26', NULL),
(17, 9, 13, 1500.00, '2026-10-23', '2026-10-26', NULL),
(18, 10, 20, 3000.00, '2026-10-20', '2026-10-25', NULL),
(19, 4, 10, 4200.00, '2026-10-18', '2026-10-21', NULL),
(20, 6, 8, 3600.00, '2026-10-28', '2026-10-30', NULL),
(21, 3, 12, 2100.00, '2026-11-02', '2026-11-05', NULL);

-- -----------------------------------------------------
-- Datos para la tabla `critica_ejemplar`
-- (id_ejemplar, nro_usuario, comentario, puntuacion)
-- Solo de usuarios que ya alquilaron y devolvieron ese ejemplar.
-- -----------------------------------------------------
INSERT INTO `critica_ejemplar` (`id_ejemplar`, `nro_usuario`, `comentario`, `puntuacion`) VALUES
(1, 3, 'Impecable, con todas las piezas y el dueño muy puntual en la entrega.', 5),
(3, 4, 'Muy buen estado, las losetas casi sin uso.', 4),
(8, 5, 'Nuevo y completo. Excelente trato del dueño.', 5),
(12, 6, 'Las cartas tenían algunas marcas, pero se pudo jugar bien.', 3),
(10, 7, 'Buen estado general, faltaba un vagón de repuesto.', 4),
(5, 8, 'Tablero y fichas en buen estado.', 4),
(9, 2, 'Perfecto para jugar en equipo, volvería a alquilarlo.', 5),
(14, 9, 'Cartas en muy buen estado, lo usamos toda la semana.', 4),
(17, 3, 'Componentes de primera, el ejemplar parecía recién abierto.', 5),
(21, 5, 'Bloques en buen estado, aunque algo gastados.', 4),
(2, 4, 'Faltaba una ficha de ladrón y la caja estaba golpeada.', 3),
(18, 2, 'Ejemplar nuevo, entrega rápida y sin problemas.', 5);

-- -----------------------------------------------------
-- Datos para la tabla `critica_juego`
-- (nro_usuario, id_juego, comentario, puntuacion)
-- Se pueden hacer en cualquier momento (un usuario, una crítica por juego).
-- -----------------------------------------------------
INSERT INTO `critica_juego` (`nro_usuario`, `id_juego`, `comentario`, `puntuacion`) VALUES
(3, 1, 'Un clásico moderno, cada partida es distinta.', 5),
(4, 1, 'Muy entretenido, aunque depende mucho de los dados.', 4),
(5, 1, 'Ideal para jugar en grupo de amigos.', 4),
(2, 2, 'Fácil de aprender y muy rejugable.', 5),
(6, 2, 'Simple y lindo, bueno para jugar en familia.', 4),
(4, 3, 'Entretenido pero las partidas se hacen largas.', 3),
(7, 3, 'Demasiado azar y partidas interminables.', 2),
(8, 4, 'Un orgullo nacional, imperdible entre amigos.', 5),
(9, 4, 'Muy buen juego de estrategia, ideal para tardes largas.', 4),
(2, 5, 'Excelente cooperativo, la tensión se siente hasta el final.', 5),
(5, 5, 'Nos obliga a trabajar en equipo de verdad.', 5),
(10, 6, 'Reglas simples y muy adictivo.', 4),
(3, 6, 'Perfecto para iniciarse en juegos de mesa modernos.', 5),
(6, 7, 'Hermosas ilustraciones y mucha creatividad.', 4),
(9, 7, NULL, 3),
(7, 8, 'Rápido y divertido, ideal para pasar el rato.', 4),
(10, 8, 'Nos reímos muchísimo en cada partida.', 5),
(8, 9, 'Bueno para quienes disfrutan de las palabras.', 3),
(2, 10, 'Profundo y desafiante, requiere varias partidas para dominarlo.', 5),
(4, 10, 'Muy completo, aunque la preparación lleva tiempo.', 4),
(3, 11, 'Genial para grupos grandes, muy divertido.', 5),
(6, 11, 'Divertido, pero requiere buena comunicación en el equipo.', 4),
(5, 12, 'Entretenido, pero no tanto para jugarlo muchas veces seguidas.', 2),
(9, 12, NULL, 3);