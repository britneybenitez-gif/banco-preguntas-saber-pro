# banco-preguntas-saber-pro
Aplicación Java Desktop para la gestión de un Banco de Preguntas Saber Pro. Proyecto de Ingeniería de Software II - Universidad del Cauca.

## Arquitectura
Monolito en 3 capas (`presentation` → `domain` ← `dataaccess`) con MVC en la capa de presentación.

| Historia de usuario | Vista | Controlador | Servicio (dominio) |
|---|---|---|---|
| HU-01 Crear pregunta | `CrearPreguntaPanel` | `PreguntaController` | `PreguntaService.crearPregunta` |
| HU-02 Enviar a revisión (estados con color) | `BotonEnviarARevision`, `EstadoCellRenderer` | `EnviarARevisionController` | `PreguntaService.enviarARevision` |
| HU-03 Listar mis preguntas (paginación y filtros) | `MisPreguntasPanel`, `PaginadorPanel` | `ConsultaPreguntaController` | `ConsultaPreguntaService` |
| HU-04 Asignar revisores + email | `AsignarRevisoresPanel` | `AsignacionRevisorController` | `AsignacionRevisorService` |

Patrones GoF: Builder (`PreguntaBuilder`), Template Method (`TransicionEstadoPreguntaTemplate` y sus transiciones), Strategy/puerto (`NotificadorEmail`).
Los repositorios y el notificador son interfaces del dominio; `dataaccess` trae implementaciones en memoria / simuladas.

## Ejecución
`App` pide el usuario con el que ingresar (autor, administrador). Carga preguntas de ejemplo con `DatosDemo` (quitar la llamada en `App` para empezar vacío).

## 📄 Documentación y Video de Sustentación

* **Video de Sustentación (YouTube):** [Ver video aquí](https://youtu.be/m5YG1WERcLg)
* ** Documento PDF:** [Ver documento aquí](docs/Sprint.pdf)
