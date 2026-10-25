USE `Vuelta_al_tablero_db`;

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

