
package co.edu.unicauca.bancopreguntas.domain.entities;

public class PreguntaBuilder {

    private final Pregunta pregunta;

    public PreguntaBuilder() {
        this.pregunta = new Pregunta();
    }

    public PreguntaBuilder conContexto(String contexto) {
        pregunta.setContexto(contexto);
        return this;
    }

    public PreguntaBuilder conPreguntaDirecta(String enunciado) {
        pregunta.setPreguntaDirecta(enunciado);
        return this;
    }

    public PreguntaBuilder conDistractor1(String distractor) {
        pregunta.setDistractor1(distractor);
        return this;
    }

    public PreguntaBuilder conDistractor2(String distractor) {
        pregunta.setDistractor2(distractor);
        return this;
    }

    public PreguntaBuilder conDistractor3(String distractor) {
        pregunta.setDistractor3(distractor);
        return this;
    }

    public PreguntaBuilder conDistractor4(String distractor) {
        pregunta.setDistractor4(distractor);
        return this;
    }

    public PreguntaBuilder conRespuestaCorrecta(String respuesta) {
        pregunta.setRespuestaCorrecta(respuesta);
        return this;
    }

    public PreguntaBuilder conJustificacion(String justificacion) {
        pregunta.setJustificacion(justificacion);
        return this;
    }

    public PreguntaBuilder conBibliografia(String bibliografia) {
        pregunta.setBibliografia(bibliografia);
        return this;
    }

    public PreguntaBuilder conCompetencia(String competencia) {
        pregunta.setCompetencia(competencia);
        return this;
    }

    public PreguntaBuilder conTema(String tema) {
        pregunta.setTema(tema);
        return this;
    }

    public PreguntaBuilder conSubtema(String subtema) {
        pregunta.setSubtema(subtema);
        return this;
    }

    public PreguntaBuilder conNivelDificultad(String dificultad) {
        pregunta.setNivelDificultad(dificultad);
        return this;
    }

    public PreguntaBuilder conAutorId(int autorId) {
        pregunta.setAutorId(autorId);
        return this;
    }

    public Pregunta build() {
        pregunta.setEstado(EstadoPregunta.BORRADOR);
        return pregunta;
    }
}
