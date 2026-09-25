# Propuesta de Proyecto: Vuelta al Tablero

**Grupo:** BORBOLETTA

**Integrantes:** Lucas Sanchez Fugaretta, Laura Elena Morea

---

## 1. Resumen

**Vuelta al Tablero** es una idea enfocada en la economía circular, diseñada para la gestión y alquiler de juegos de mesa.
La plataforma centraliza la oferta de un catálogo administrado por el sistema y, en simultáneo, habilita un mercado colaborativo en donde los usuarios pueden poner en alquiler su propia colección (o simplemente "anotarla"). Este modelo buscaría fomentar la rotación de títulos, facilitando el acceso y conocimiento de juegos de mesa, además de fomentar la discusión entre los usuarios.

## 2. Modelo de Negocio

El sistema opera bajo un esquema de monetización mixto:

* **Alquiler de inventario propio:** Ingresos directos generados por el préstamo de los ejemplares provistos exclusivamente por la administración.
* **Sistema de comisiones:** Retención de un porcentaje preestablecido como comisión por cada transacción de alquiler concretada entre los usuarios que ofrecen sus juegos y quienes los alquilan.

## 3. Funcionalidades Principales

### 3.1. Gestión de Catálogo y Fichas de Juegos

Para facilitar la búsqueda y selección, cada título disponible en el sistema cuenta con una ficha descriptiva exhaustiva que incluye:

* **Descripción y Objetivo:** Sinopsis de la temática y la meta principal para ganar la partida.
* **Especificaciones:** Cantidad de jugadores admitidos y tipo/categoría de juego (estrategia, *party game*, cartas, cooperativo, etc.).
* **Reglamento:** Detalle de las reglas principales para facilitar el aprendizaje.

### 3.2. Mercado Colaborativo y Control de Ejemplares

* **Publicación de Usuarios (P2P):** Los usuarios registrados están habilitados para listar sus propios juegos físicos dentro de la plataforma, pudiendo ofrecerlos, o no, a terceros.
* **Trazabilidad por Ejemplar:** El sistema distingue entre el "Juego" (el concepto general) y el "Ejemplar" (la copia física). Un mismo juego puede tener múltiples ejemplares vinculados a distintos propietarios (administrador o usuarios), cada uno con su propia disponibilidad.

### 3.3. Sistema de Calificaciones Bidireccional

Para garantizar la confianza en el intercambio y el cuidado del material, se implementa una doble capa de evaluación post-alquiler:

* **Reseña del Título:** Valoración enfocada en la experiencia lúdica, las mecánicas y la diversión del juego en general.
* **Calificación del Ejemplar:** Evaluación específica sobre la copia física que se alquiló, calificando el estado de conservación, la limpieza y la integridad de los componentes de esa caja en particular.
