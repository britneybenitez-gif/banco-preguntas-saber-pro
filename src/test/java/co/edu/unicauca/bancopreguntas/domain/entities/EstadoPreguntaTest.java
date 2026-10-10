package co.edu.unicauca.bancopreguntas.domain.entities;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class EstadoPreguntaTest {

    @Test
    void cadaEstadoTieneEtiquetaYColor() {
        for (EstadoPregunta estado : EstadoPregunta.values()) {
            assertNotNull(estado.getLabel());
            assertFalse(estado.getLabel().isBlank());
            assertNotNull(estado.getColor());
        }
    }

    @Test
    void borradorYPendienteTienenEtiquetasEsperadas() {
        assertEquals("En borrador", EstadoPregunta.BORRADOR.getLabel());
        assertEquals("Pendiente de revisión", EstadoPregunta.PENDIENTE_REVISION.getLabel());
    }

    @Test
    void textoOscuroSobreFondoClaro() {
        // Amarillo (pendiente) es un fondo claro
        assertEquals(new Color(33, 37, 41), EstadoPregunta.PENDIENTE_REVISION.getTextColor());
    }

    @Test
    void textoBlancoSobreFondoOscuro() {
        // Gris oscuro (borrador) es un fondo oscuro
        assertEquals(Color.WHITE, EstadoPregunta.BORRADOR.getTextColor());
    }

    @Test
    void toStringDevuelveLaEtiqueta() {
        assertEquals("En borrador", EstadoPregunta.BORRADOR.toString());
    }
}