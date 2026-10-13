package co.edu.unicauca.bancopreguntas.domain.entities;

import java.text.Normalizer;
import java.util.Locale;

/**
 * Criterios de búsqueda de preguntas. Cualquier criterio nulo o vacío se ignora.
 * <ul>
 *   <li>{@code autorId}: solo preguntas de ese autor.</li>
 *   <li>{@code estado}: estado exacto.</li>
 *   <li>{@code nivelDificultad}: BAJA, MEDIA o ALTA.</li>
 *   <li>{@code competencia}, {@code tema}: contiene el texto (sin tildes ni mayúsculas).</li>
 *   <li>{@code texto}: contiene el texto en contexto, pregunta, competencia, tema o subtema.</li>
 * </ul>
 */
public record FiltroPregunta(
        Integer autorId,
        EstadoPregunta estado,
        String nivelDificultad,
        String competencia,
        String tema,
        String texto) {

    public static FiltroPregunta vacio() {
        return new FiltroPregunta(null, null, null, null, null, null);
    }

    public FiltroPregunta conAutor(Integer nuevoAutorId) {
        return new FiltroPregunta(nuevoAutorId, estado, nivelDificultad, competencia, tema, texto);
    }

    public FiltroPregunta conEstado(EstadoPregunta nuevoEstado) {
        return new FiltroPregunta(autorId, nuevoEstado, nivelDificultad, competencia, tema, texto);
    }

    /** Indica si la pregunta cumple todos los criterios del filtro. */
    public boolean coincide(Pregunta p) {
        if (autorId != null && p.getAutorId() != autorId) {
            return false;
        }
        if (estado != null && p.getEstado() != estado) {
            return false;
        }
        if (hayTexto(nivelDificultad)
                && !normalizar(nivelDificultad).equals(normalizar(p.getNivelDificultad()))) {
            return false;
        }
        if (hayTexto(competencia) && !contiene(p.getCompetencia(), competencia)) {
            return false;
        }
        if (hayTexto(tema) && !contiene(p.getTema(), tema)) {
            return false;
        }
        if (hayTexto(texto)) {
            boolean alguno = contiene(p.getContexto(), texto)
                    || contiene(p.getPreguntaDirecta(), texto)
                    || contiene(p.getCompetencia(), texto)
                    || contiene(p.getTema(), texto)
                    || contiene(p.getSubtema(), texto);
            if (!alguno) {
                return false;
            }
        }
        return true;
    }

    private static boolean hayTexto(String s) {
        return s != null && !s.isBlank();
    }

    private static boolean contiene(String campo, String buscado) {
        return normalizar(campo).contains(normalizar(buscado));
    }

    private static String normalizar(String s) {
        if (s == null) {
            return "";
        }
        String n = Normalizer.normalize(s.trim().toLowerCase(Locale.ROOT), Normalizer.Form.NFD);
        return n.replaceAll("\\p{M}+", "");
    }
}
