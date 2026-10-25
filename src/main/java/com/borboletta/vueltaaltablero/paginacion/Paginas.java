package com.borboletta.vueltaaltablero.paginacion;

import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Reglas comunes de todas las listas paginadas.
 * <ul>
 *   <li>Del cliente (la URL) solo se aceptan el número de página y el tamaño, y el tamaño tiene tope.</li>
 *   <li>El ORDEN lo decide siempre el sistema (en el nombre del método del repositorio): si se aceptara
 *       "?sort=" de la URL, cualquiera podría ordenar por un campo que no corresponde, o romper la consulta
 *       con un nombre inexistente.</li>
 *   <li>Si piden una página que no existe (por ejemplo la 99), se entrega la última en vez de una lista vacía.</li>
 * </ul>
 */
public final class Paginas{

    public static final int TAMANIO_MAXIMO = 48;
    private static final int TAMANIO_PREDETERMINADO = 12;

    private Paginas(){ }

    /** Lo que se toma de lo que pidió el cliente: página y tamaño (con tope), sin orden. */
    public static Pageable pedida(Pageable pedido){
        Pageable segura = PageRequest.of(0, TAMANIO_PREDETERMINADO);
        if (pedido != null && pedido.isPaged()){
            segura = PageRequest.of(pedido.getPageNumber(), Math.min(pedido.getPageSize(), TAMANIO_MAXIMO));
        }
        return segura;
    }

    /** Ejecuta la consulta con la paginación ya saneada y corrige las páginas fuera de rango. */
    public static <T> Page<T> consultar(Pageable pedido, Function<Pageable, Page<T>> consulta){
        Pageable segura = pedida(pedido);
        Page<T> pagina = consulta.apply(segura);
        boolean fueraDeRango = pagina.getTotalPages() > 0 && segura.getPageNumber() >= pagina.getTotalPages();
        return fueraDeRango ? consulta.apply(segura.withPage(pagina.getTotalPages() - 1)) : pagina;
    }
}

