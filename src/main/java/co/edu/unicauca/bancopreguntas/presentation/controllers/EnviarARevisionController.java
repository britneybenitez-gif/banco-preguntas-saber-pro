package co.edu.unicauca.bancopreguntas.presentation.controllers;

import co.edu.unicauca.bancopreguntas.domain.services.PreguntaService;

/**
 * HU-02: conecta la pantalla con el caso de uso "Enviar a revisión".
 */
public class EnviarARevisionController {

    private final PreguntaService preguntaService;
    private final int autorId;

    public EnviarARevisionController(PreguntaService preguntaService, int autorId) {
        this.preguntaService = preguntaService;
        this.autorId = autorId;
    }

    public void enviarARevision(int preguntaId) {
        preguntaService.enviarARevision(preguntaId, autorId);
    }
}