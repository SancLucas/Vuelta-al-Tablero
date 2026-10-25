# Propuesta de Proyecto: Vuelta al Tablero

**Grupo:** BORBOLETTA

**Integrantes:** Lucas Sanchez Fugaretta, Laura Elena Morea

**Versión:** 2 (ajustada según las correcciones recibidas sobre la propuesta inicial)

---

## 1. Resumen

**Vuelta al Tablero** es una plataforma web enfocada en la economía circular, diseñada para la gestión y el alquiler de juegos de mesa.

La plataforma centraliza un catálogo administrado por el sistema y, en simultáneo, permite que los usuarios registren su propia colección: pueden cargar el ejemplar de un juego que tienen o ponerlo en alquiler. Este modelo busca fomentar la rotación de títulos, facilitar el acceso y el conocimiento de juegos de mesa, y promover la discusión entre los usuarios a través de reseñas y calificaciones.

Esta versión de la propuesta **acota el alcance** a un núcleo concreto y bien definido (sección 2), y precisa las funciones de cada rol, las entidades con CRUD, la autenticación y el modelo de datos.

## 2. Alcance

### 2.1. Dentro del alcance

El proyecto se concentra en seis ejes:

1. **Catálogo de juegos** con su ficha técnica.
2. **Ejemplares**: las copias físicas de cada juego, con su estado y propietario.
3. **Usuarios**: registro, autenticación, roles y administración de cuentas.
4. **Disponibilidad** de cada ejemplar.
5. **Alquileres** entre usuarios y con la administración.
6. **Calificaciones** del juego y del ejemplar.

### 2.2. Fuera del alcance (segunda etapa)

Se deja explícitamente afuera, para no desviar el proyecto de su núcleo:

* Pagos reales, pasarelas de pago y transferencias de dinero.
* Mensajería entre usuarios.
* Logística de entrega y envíos.
* Funcionalidades sociales adicionales (seguimiento entre usuarios, foros, notificaciones).

Cómo se resuelve sin ellas:

* El **precio** es una operación interna del sistema: se calcula y se registra, pero no se cobra.
* La **entrega y la devolución** se coordinan fuera de la plataforma. El sistema solo le muestra a quien alquila el teléfono del propietario mientras el alquiler está abierto, y el propietario registra en el sistema cuándo recibió el ejemplar de vuelta.

## 3. Modelo de negocio

El sistema opera bajo un esquema de monetización modelado internamente:

* **Alquiler de inventario propio:** ingresos por el préstamo de los ejemplares provistos por la administración. Son ejemplares cuyo propietario es una cuenta con rol de administrador, y se gestionan igual que los de cualquier usuario.

El **precio total** de un alquiler se fija en el momento de pedirlo (precio por día del ejemplar multiplicado por la cantidad de días) y no cambia aunque el propietario modifique después su precio.

## 4. Roles y funciones

El sistema distingue tres perfiles. El rol de cada cuenta (usuario final o administrador) se guarda en la base de datos y determina qué rutas puede usar.

| Función | Visitante (sin sesión) | Usuario final | Administrador |
|---|:---:|:---:|:---:|
| Ver el inicio, el catálogo (con filtro por categoría y paginación) y la ficha de cada juego | Sí | Sí | Sí |
| Ver los ejemplares disponibles de un juego y el detalle de cada ejemplar | Sí | Sí | Sí |
| Ver las reseñas y calificaciones | Sí | Sí | Sí |
| Registrarse e iniciar sesión | Sí | n/a | n/a |
| Ver y editar su perfil (datos personales) y darse de baja | No | Sí | Sí |
| Publicar, editar y quitar **sus** ejemplares (estado, disponibilidad y precio por día) | No | Sí | Sí |
| Alquilar el ejemplar de otra persona | No | Sí | Sí |
| Ver sus alquileres y los alquileres de sus ejemplares | No | Sí | Sí |
| Registrar la devolución o cancelar un alquiler (como propietario) | No | Sí | Sí |
| Reseñar un juego | No | Sí | Sí |
| Calificar un ejemplar que alquiló y ya devolvió | No | Sí | Sí |
| Alta, edición y baja de juegos y de sus fichas técnicas | No | No | Sí |
| Listar usuarios, bloquearlos y desbloquearlos | No | No | Sí |

Aclaraciones:

* **Usuario final:** cualquier persona registrada. Puede ser propietaria (publica ejemplares) y, a la vez, inquilina (alquila los de otros). No puede alquilar su propio ejemplar.
* **Administrador:** hereda todas las funciones del usuario final y suma la gestión del catálogo y de las cuentas. No puede ser bloqueado. Las cuentas administradoras se cargan directamente en la base de datos: no existe registro público de administradores.
* **Pedido de juegos nuevos:** al final del catálogo hay un enlace al correo de la administración para pedir un título que no está.

## 5. Funcionalidades principales

### 5.1. Gestión de catálogo y fichas de juegos

Cada título cuenta con una ficha descriptiva:

* **Datos del juego:** título (único), categoría y editorial.
* **Categorías:** Clásicos, Modernos, Estrategia, Cooperativos, Familiares, Cartas, Party y Otros. El catálogo se puede recorrer completo ("Todos los juegos") o filtrado por categoría.
* **Ficha técnica:** cantidad mínima y máxima de jugadores, edad mínima, año de publicación y descripción (sinopsis, temática y objetivo, hasta 200 caracteres).
* **Listados paginados:** el catálogo, los ejemplares de cada juego y el listado de usuarios del administrador se muestran por páginas, para que el sistema siga siendo ágil cuando haya miles de registros.
* **Baja lógica con reactivación:** un juego dado de baja no se borra. Si el administrador vuelve a agregar un título que estaba dado de baja, se reactiva el mismo registro, sin duplicarlo.

### 5.2. Ejemplares y control de disponibilidad

* **Juego y Ejemplar:** el sistema distingue entre el "Juego" (el concepto general) y el "Ejemplar" (la copia física). Un mismo juego puede tener múltiples ejemplares con distintos propietarios (administrador o usuarios), cada uno con su estado y su disponibilidad.
* **Estado de conservación:** Nuevo, Excelente, Bueno, Dañado o Incompleto.
* **Anotar u ofrecer:** un usuario puede registrar un ejemplar sin ofrecerlo o ponerlo en alquiler. Para ofrecerlo debe indicar un precio por día mayor a cero.
* **Disponibilidad efectiva:** un ejemplar aparece como disponible en la página de su juego solo si su propietario lo ofrece, no tiene un alquiler abierto, y ni él ni su dueño están dados de baja o bloqueados.

### 5.3. Alquileres

El alquiler es una operación interna entre dos usuarios, con estas reglas:

* Dura entre **1 y 7 días**.
* La fecha de entrega va desde hoy hasta **7 días de anticipación**.
* Nadie puede alquilar su propio ejemplar.
* Un ejemplar tiene **un solo alquiler abierto** a la vez.
* Solo el **propietario** registra la devolución (no antes de la fecha de entrega) y puede cancelar un alquiler que todavía no empezó. Un alquiler cancelado se elimina porque nunca llegó a ocurrir.
* Se conserva el historial de alquileres devueltos.

### 5.4. Sistema de calificaciones

Para dar confianza al intercambio y cuidar el material, se evalúa en dos capas:

* **Reseña del título:** valoración de la experiencia lúdica, las mecánicas y la diversión del juego. Puede escribirla cualquier usuario registrado, aunque no lo haya alquilado.
* **Calificación del ejemplar:** evaluación del estado de conservación, la limpieza y la integridad de los componentes de esa copia en particular. Solo puede hacerla quien **alquiló ese ejemplar y ya lo devolvió**.

Ambas llevan una puntuación de 1 a 5 y un comentario opcional de hasta 200 caracteres. Cada persona tiene como máximo una reseña por juego y una calificación por ejemplar: si vuelve a enviarla, se corrige la anterior.

## 6. Entidades, CRUD y validaciones

### 6.1. Entidades con CRUD completo

Se implementa CRUD completo, con validaciones, en **tres entidades**:

| Entidad | Alta | Consulta | Modificación | Baja | Quién |
|---|---|---|---|---|---|
| **Usuario** | Registro público | Perfil propio; listado paginado para el administrador | Datos personales (el DNI y el correo no se modifican) | Baja lógica propia; además del bloqueo y desbloqueo por el administrador | Visitante, usuario y administrador |
| **Juego** (con su **Ficha técnica**) | Administrador | Pública: catálogo, filtro, paginación y detalle | Administrador | Baja lógica (administrador) | Administrador |
| **Ejemplar** | Usuario propietario | Pública en el juego y en su detalle; privada en "Mis ejemplares" | Propietario | Baja lógica (propietario) | Usuario propietario |

Entidades complementarias, que completan el dominio:

* **Alquiler:** alta (alquilar), consulta (mis alquileres y los de mis ejemplares), modificación (registrar la devolución) y baja (cancelación previa al inicio).
* **Reseña del juego** y **calificación del ejemplar:** alta o corrección, y consulta.

**Baja lógica:** los usuarios, los juegos y los ejemplares no se borran de la base: se les marca una fecha de baja. Así se conserva el historial de alquileres y calificaciones, y se puede reactivar un registro (por ejemplo, una persona que se dio de baja y vuelve a registrarse con el mismo correo y DNI recupera su cuenta).

### 6.2. Reglas de validación

| Entidad | Reglas |
|---|---|
| **Usuario** | Nombre y apellido obligatorios (hasta 45). DNI de 7 u 8 dígitos, único. Correo con formato válido, hasta 100 caracteres, único sin distinguir mayúsculas. Teléfono obligatorio, hasta 14 caracteres. Contraseña de 8 a 72 caracteres, con confirmación; se guarda cifrada. |
| **Juego** | Título obligatorio (hasta 45) y único. Editorial obligatoria (hasta 25). Categoría obligatoria, dentro de las 8 definidas. |
| **Ficha técnica** | Mínimo de jugadores de al menos 1; máximo no menor que el mínimo. Edad mínima no negativa. Año de publicación entre 1900 y el año actual. Descripción obligatoria (hasta 200). |
| **Ejemplar** | Estado obligatorio (5 valores). Si se ofrece en alquiler, precio por día mayor a cero; si no se ofrece, no tiene precio. Solo el propietario lo modifica o lo quita. |
| **Alquiler** | Duración de 1 a 7 días. Entrega entre hoy y 7 días adelante. Ejemplar ofrecido, a la vista y sin otro alquiler abierto. No se alquila el ejemplar propio. |
| **Reseña y calificación** | Puntuación de 1 a 5. Comentario opcional de hasta 200 caracteres. Una por persona y por juego o ejemplar. La del ejemplar exige haberlo alquilado y devuelto. |

Las validaciones se aplican en **tres capas**, de modo que ninguna se pueda saltear:

1. **Formularios:** validación de los datos de entrada (Bean Validation), con un mensaje junto a cada campo.
2. **Dominio:** las propias entidades controlan sus reglas al crearse o modificarse, y ninguna puede quedar en un estado inválido.
3. **Base de datos:** restricciones `CHECK`, `UNIQUE` y claves foráneas como última barrera.

## 7. Autenticación y sesión

* **Autenticación:** formulario de ingreso propio (correo y contraseña), gestionado con Spring Security. Las contraseñas se almacenan cifradas con BCrypt (con sal) y nunca se muestran. Ante un fallo se muestra un mensaje genérico que no revela si el correo existe.
* **Sesión:** sesión HTTP del servidor, identificada por una cookie. El sistema lleva un registro de sesiones activas por cuenta. Cerrar sesión se hace con una petición POST protegida.
* **Autorización por rol:** el rol (administrador, usuario o visitante) define qué rutas se pueden usar.

| Zona | Acceso |
|---|---|
| Inicio, registro, ingreso, catálogo, ficha de cada juego y detalle de cada ejemplar (solo lectura) | Público |
| Perfil, "Mis ejemplares", "Mis alquileres", alquilar, devolver, reseñar y calificar | Con sesión iniciada |
| Alta, edición y baja de juegos; administración de usuarios | Solo administrador |

* **Identidad tomada de la sesión:** quién realiza una operación (publicar, alquilar, calificar) se obtiene siempre de la sesión y nunca de un parámetro de la URL ni de un formulario. Así nadie puede actuar a nombre de otro ni ver ejemplares ajenos cambiando un número en la dirección.
* **Protección contra CSRF** en todos los formularios que modifican datos.
* **Baja y bloqueo:** una cuenta dada de baja o bloqueada no puede ingresar, y se le cierran las sesiones que tuviera abiertas. Quien intenta ingresar con una cuenta bloqueada recibe un aviso específico.
* **Redirección tras el ingreso:** si una persona sin sesión intenta entrar a una zona protegida, al iniciar sesión vuelve a ella.

## 8. Modelo de datos (DER)

### 8.1. Entidades

| Entidad (tabla) | Atributos principales |
|---|---|
| **Usuario** (`usuario`) | nro (PK), nombre, apellido, dni (único), correo (único), contraseña, teléfono, fecha de registro, fecha de baja, fecha de bloqueo, rol de administrador |
| **Juego** (`juego`) | id (PK), título (único), editorial, categoría, id de ficha técnica (FK, única), fecha de baja |
| **Ficha técnica** (`ficha_tecnica`) | id (PK), mínimo y máximo de jugadores, edad mínima, descripción, año de publicación |
| **Ejemplar** (`ejemplar`) | id (PK), estado, disponible para alquiler, precio por día, id del juego (FK), nro del usuario propietario (FK), fecha de baja |
| **Alquila** (`alquila`) | id (PK), nro del usuario (FK), id del ejemplar (FK), precio, fecha de entrega, fecha de devolución prevista, fecha de devolución |
| **Crítica de juego** (`critica_juego`) | nro del usuario y id del juego (PK compuesta, ambas FK), puntuación, comentario |
| **Crítica de ejemplar** (`critica_ejemplar`) | id del ejemplar y nro del usuario (PK compuesta, ambas FK), puntuación, comentario |

### 8.2. Relaciones y cardinalidades

| Relación | Tipo | Cómo se representa |
|---|---|---|
| Juego y Ficha técnica | **1:1** | `juego.id_ficha_tecnica` es clave foránea **única** y obligatoria. Cada juego tiene exactamente una ficha y cada ficha pertenece a un solo juego. |
| Usuario y Ejemplar (propietario) | **1:N** | Un usuario posee muchos ejemplares; cada ejemplar tiene un único propietario (`ejemplar.nro_usuario`). |
| Juego y Ejemplar | **1:N** | Un juego tiene muchos ejemplares; cada ejemplar es copia de un solo juego (`ejemplar.id_juego`). |
| Usuario y Ejemplar (alquiler) | **N:M** | Un usuario alquila muchos ejemplares y un ejemplar es alquilado por muchos usuarios a lo largo del tiempo. Se resuelve con la tabla `alquila`, que tiene atributos propios (precio y fechas) y clave primaria propia, porque la misma persona puede alquilar el mismo ejemplar más de una vez. |
| Usuario y Juego (reseña) | **N:M** | Se resuelve con `critica_juego`, de clave compuesta (usuario, juego): una reseña por persona y por juego. |
| Usuario y Ejemplar (calificación) | **N:M** | Se resuelve con `critica_ejemplar`, de clave compuesta (ejemplar, usuario): una calificación por persona y por ejemplar. |


## 9. Arquitectura y tecnologías

* **Tecnologías:** Java con Spring Boot, Spring Data JPA, Spring Security, base de datos MySQL y vistas HTML y CSS con Thymeleaf. La interfaz es adaptable a distintos tamaños de pantalla.
* **Capas:** controlador, servicio, repositorio y entidad, más objetos de transferencia (DTO) para que las vistas nunca manejen las entidades directamente.
* **Principios de diseño:** responsabilidad única en cada clase, dependencia de abstracciones, y "Tell, Don't Ask": se le indica a cada entidad qué hacer y ella decide si puede, en lugar de consultarle su estado desde afuera.

## 10. Estado de avance

| Elemento | Estado |
|---|---|
| Catálogo con fichas, categorías, filtro y paginación | Implementado |
| Ejemplares, estados, disponibilidad y precio por día | Implementado |
| Registro, ingreso, perfil, baja lógica, bloqueo y roles | Implementado |
| Alquileres (pedido, devolución, cancelación e historial) | Implementado |
| Reseña del juego y calificación del ejemplar | Implementado |
