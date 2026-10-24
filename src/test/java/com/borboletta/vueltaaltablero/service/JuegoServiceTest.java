package com.borboletta.vueltaaltablero.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.borboletta.vueltaaltablero.dto.DatosDeJuego;
import com.borboletta.vueltaaltablero.dto.DetalleDeJuego;
import com.borboletta.vueltaaltablero.entity.CategoriaJuego;
import com.borboletta.vueltaaltablero.entity.FichaTecnica;
import com.borboletta.vueltaaltablero.entity.Juego;
import com.borboletta.vueltaaltablero.exception.JuegoNoEncontradoException;
import com.borboletta.vueltaaltablero.exception.JuegoYaExisteException;
import com.borboletta.vueltaaltablero.repository.JuegoRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JuegoServiceTest{

    @Mock private JuegoRepository juegoRepository;
    @InjectMocks private JuegoService servicio;

    private final DatosDeJuego datos = new DatosDeJuego(
            "Catan", CategoriaJuego.ESTRATEGIA, "Devir", 3, 4, 10, "Comerciá y construí", 1995);

    private Juego catan(){
        return Juego.crear("Catan", CategoriaJuego.ESTRATEGIA, "Devir",
                FichaTecnica.crear(3, 4, 10, "Comerciá y construí", 1995));
    }

    @Test
    void creaElJuegoJuntoConSuFicha(){
        when(juegoRepository.findByTituloIgnoreCaseAndFechaBajaIsNotNull("Catan")).thenReturn(Optional.empty());
        when(juegoRepository.existsByTituloIgnoreCaseAndFechaBajaIsNull("Catan")).thenReturn(false);

        servicio.crear(datos);

        ArgumentCaptor<Juego> captor = ArgumentCaptor.forClass(Juego.class);
        verify(juegoRepository).save(captor.capture());
        assertThat(captor.getValue().getTitulo()).isEqualTo("Catan");
        assertThat(captor.getValue().getCategoria()).isEqualTo(CategoriaJuego.ESTRATEGIA);
        assertThat(captor.getValue().getFichaTecnica().getMaxJugadores()).isEqualTo(4);
    }

    @Test
    void rechazaUnTituloQueYaEstaEnElCatalogo(){
        when(juegoRepository.findByTituloIgnoreCaseAndFechaBajaIsNotNull("Catan")).thenReturn(Optional.empty());
        when(juegoRepository.existsByTituloIgnoreCaseAndFechaBajaIsNull("Catan")).thenReturn(true);

        assertThatThrownBy(() -> servicio.crear(datos)).isInstanceOf(JuegoYaExisteException.class);
        verify(juegoRepository, never()).save(any());
    }

    @Test
    void agregarUnJuegoDadoDeBajaLoReactivaEnLugarDeDuplicarlo(){
        Juego dadoDeBaja = catan();
        dadoDeBaja.darDeBaja();
        when(juegoRepository.findByTituloIgnoreCaseAndFechaBajaIsNotNull("Catan"))
                .thenReturn(Optional.of(dadoDeBaja));

        servicio.crear(new DatosDeJuego("Catan", CategoriaJuego.FAMILIARES, "Otra editorial",
                2, 6, 8, "Nueva descripción", 2020));

        assertThat(dadoDeBaja.getFechaBaja()).isNull();
        assertThat(dadoDeBaja.getCategoria()).isEqualTo(CategoriaJuego.FAMILIARES);
        assertThat(dadoDeBaja.getFichaTecnica().getMaxJugadores()).isEqualTo(6);
        verify(juegoRepository, never()).save(any());     // la misma fila: no se insertó otra
    }

    @Test
    void actualizaElJuegoYTambienSuFicha(){
        Juego juego = catan();
        when(juegoRepository.findByIdAndFechaBajaIsNull(1L)).thenReturn(Optional.of(juego));
        when(juegoRepository.existsByTituloIgnoreCaseAndFechaBajaIsNullAndIdNot("Catan Nuevo", 1L))
                .thenReturn(false);
        when(juegoRepository.existsByTituloIgnoreCaseAndFechaBajaIsNotNullAndIdNot("Catan Nuevo", 1L))
                .thenReturn(false);

        servicio.actualizar(1L, new DatosDeJuego(
                "Catan Nuevo", CategoriaJuego.CLASICOS, "Devir", 2, 6, 12, "Otra descripción", 2020));

        assertThat(juego.getTitulo()).isEqualTo("Catan Nuevo");
        assertThat(juego.getCategoria()).isEqualTo(CategoriaJuego.CLASICOS);
        assertThat(juego.getFichaTecnica().getMaxJugadores()).isEqualTo(6);
    }

    @Test
    void alEditarNoPermiteElTituloDeOtroJuegoDelCatalogo(){
        Juego juego = catan();
        when(juegoRepository.findByIdAndFechaBajaIsNull(1L)).thenReturn(Optional.of(juego));
        when(juegoRepository.existsByTituloIgnoreCaseAndFechaBajaIsNullAndIdNot("Carcassonne", 1L))
                .thenReturn(true);

        assertThatThrownBy(() -> servicio.actualizar(1L, new DatosDeJuego(
                "Carcassonne", CategoriaJuego.ESTRATEGIA, "Devir", 2, 5, 8, "Descripción", 2000)))
                .isInstanceOf(JuegoYaExisteException.class);
        assertThat(juego.getTitulo()).isEqualTo("Catan");   // no se modificó nada
    }

    @Test
    void alEditarNoPermiteElTituloDeUnJuegoDadoDeBaja(){
        Juego juego = catan();
        when(juegoRepository.findByIdAndFechaBajaIsNull(1L)).thenReturn(Optional.of(juego));
        when(juegoRepository.existsByTituloIgnoreCaseAndFechaBajaIsNullAndIdNot("Carcassonne", 1L))
                .thenReturn(false);
        when(juegoRepository.existsByTituloIgnoreCaseAndFechaBajaIsNotNullAndIdNot("Carcassonne", 1L))
                .thenReturn(true);

        assertThatThrownBy(() -> servicio.actualizar(1L, new DatosDeJuego(
                "Carcassonne", CategoriaJuego.ESTRATEGIA, "Devir", 2, 5, 8, "Descripción", 2000)))
                .isInstanceOf(JuegoYaExisteException.class)
                .hasMessageContaining("dado de baja");
    }

    @Test
    void elCatalogoSinFiltroTraeTodosLosJuegos(){
        when(juegoRepository.findAllByFechaBajaIsNullOrderByTituloAsc()).thenReturn(List.of(catan()));

        List<DetalleDeJuego> juegos = servicio.catalogo();

        assertThat(juegos).hasSize(1);
        assertThat(juegos.get(0).categoria()).isEqualTo(CategoriaJuego.ESTRATEGIA);
    }

    @Test
    void elCatalogoConCategoriaSoloTraeLosDeEsaCategoria(){
        when(juegoRepository.findAllByCategoriaAndFechaBajaIsNullOrderByTituloAsc(CategoriaJuego.ESTRATEGIA))
                .thenReturn(List.of(catan()));

        assertThat(servicio.catalogo(CategoriaJuego.ESTRATEGIA)).hasSize(1);
        verify(juegoRepository, never()).findAllByFechaBajaIsNullOrderByTituloAsc();
    }

    @Test
    void daDeBajaSinBorrarElRegistro(){
        Juego juego = catan();
        when(juegoRepository.findByIdAndFechaBajaIsNull(1L)).thenReturn(Optional.of(juego));

        servicio.darDeBaja(1L);

        assertThat(juego.getFechaBaja()).isNotNull();
        verify(juegoRepository, never()).delete(any());
    }

    @Test
    void obtenerFallaSiElJuegoNoExisteOEstaDadoDeBaja(){
        when(juegoRepository.findByIdAndFechaBajaIsNull(9L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> servicio.obtener(9L)).isInstanceOf(JuegoNoEncontradoException.class);
    }
}
