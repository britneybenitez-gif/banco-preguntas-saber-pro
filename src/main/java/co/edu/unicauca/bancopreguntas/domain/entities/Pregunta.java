
package co.edu.unicauca.bancopreguntas.domain.entities;

public class Pregunta {

    private int id;
    private String contexto;
    private String preguntaDirecta;

    private String distractor1;
    private String distractor2;
    private String distractor3;
    private String distractor4;

    private String respuestaCorrecta;
    private String justificacion;
    private String bibliografia;
    private String competencia;
    private String tema;
    private String subtema;
    private String nivelDificultad;

    private int autorId;
    private EstadoPregunta estado;

    public Pregunta() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContexto() {
        return contexto;
    }

    public void setContexto(String contexto) {
        this.contexto = contexto;
    }

    public String getPreguntaDirecta() {
        return preguntaDirecta;
    }

    public void setPreguntaDirecta(String preguntaDirecta) {
        this.preguntaDirecta = preguntaDirecta;
    }

    public String getDistractor1() {
        return distractor1;
    }

    public void setDistractor1(String distractor1) {
        this.distractor1 = distractor1;
    }

    public String getDistractor2() {
        return distractor2;
    }

    public void setDistractor2(String distractor2) {
        this.distractor2 = distractor2;
    }

    public String getDistractor3() {
        return distractor3;
    }

    public void setDistractor3(String distractor3) {
        this.distractor3 = distractor3;
    }

    public String getDistractor4() {
        return distractor4;
    }

    public void setDistractor4(String distractor4) {
        this.distractor4 = distractor4;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public void setRespuestaCorrecta(String respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public String getBibliografia() {
        return bibliografia;
    }

    public void setBibliografia(String bibliografia) {
        this.bibliografia = bibliografia;
    }

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getSubtema() {
        return subtema;
    }

    public void setSubtema(String subtema) {
        this.subtema = subtema;
    }

    public String getNivelDificultad() {
        return nivelDificultad;
    }

    public void setNivelDificultad(String nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
    }

     public int getAutorId() {
        return autorId;
    }

    public void setAutorId(int autorId) {
        this.autorId = autorId;
    }

    public EstadoPregunta getEstado() {
        return estado;
    }

    public void setEstado(EstadoPregunta estado) {
        this.estado = estado;
    }
}