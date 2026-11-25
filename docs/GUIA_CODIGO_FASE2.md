# Dónde está el código de fase 2

Líneas verificadas sobre las fuentes finales de `mejora-ia`. La [guía de fase 1](GUIA_CODIGO.md) conserva sus referencias históricas; estas corresponden al rediseño y las tres mejoras aprobadas.

| Pregunta para la exposición | Archivo y línea |
|---|---|
| ¿Dónde se crean Avisos y Urgentes? | [Notificador.kt:17](../app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt#L17) |
| ¿Cuál es el canal urgente con IMPORTANCE_HIGH? | [Notificador.kt:23](../app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt#L23) |
| ¿Cómo se asigna Urgentes solo al recordatorio? | [Notificador.kt:30](../app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt#L30) |
| ¿Dónde se comprueba POST_NOTIFICATIONS y el bloqueo de canal? | [Notificador.kt:26](../app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt#L26) y [Notificador.kt:32](../app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt#L32) |
| ¿Dónde se identifica el aviso como ejemplo y se expande el texto largo? | [Notificador.kt:35](../app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt#L35) y [Notificador.kt:36](../app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt#L36) |
| ¿Cómo abre la app al tocar el aviso? | [Notificador.kt:38](../app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt#L38) |
| ¿Dónde se pide permiso y se abren Ajustes? | [PantallaAvisos.kt:65](../app/src/main/java/com/leon/avisoscampus/ui/PantallaAvisos.kt#L65) y [PantallaAvisos.kt:69](../app/src/main/java/com/leon/avisoscampus/ui/PantallaAvisos.kt#L69) |
| ¿Cómo cambia el botón según Sin pedir, Permitido o Denegado? | [PantallaAvisos.kt:352](../app/src/main/java/com/leon/avisoscampus/ui/PantallaAvisos.kt#L352) |
| ¿Dónde se actualiza el permiso al volver de Android? | [PantallaAvisos.kt:82](../app/src/main/java/com/leon/avisoscampus/ui/PantallaAvisos.kt#L82) |
| ¿Quién recibe el evento y muestra la notificación? | [PantallaAvisos.kt:94](../app/src/main/java/com/leon/avisoscampus/ui/PantallaAvisos.kt#L94) |
| ¿Dónde se registran y retiran los dos receptores? | [PantallaAvisos.kt:107](../app/src/main/java/com/leon/avisoscampus/ui/PantallaAvisos.kt#L107) y [PantallaAvisos.kt:115](../app/src/main/java/com/leon/avisoscampus/ui/PantallaAvisos.kt#L115) |
| ¿Dónde se lee el modo avión? | [ModoAvionReceiver.kt:6](../app/src/main/java/com/leon/avisoscampus/receiver/ModoAvionReceiver.kt#L6) |
| ¿Dónde se detecta conectar o desconectar cargador? | [CargadorReceiver.kt:6](../app/src/main/java/com/leon/avisoscampus/receiver/CargadorReceiver.kt#L6) |
| ¿Cómo tiene el cargador escucha independiente? | [AvisosViewModel.kt:55](../app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt#L55) y [PantallaAvisos.kt:363](../app/src/main/java/com/leon/avisoscampus/ui/PantallaAvisos.kt#L363) |
| ¿Cómo se registra el aviso inmediato? | [AvisosViewModel.kt:26](../app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt#L26) |
| ¿Dónde están la cuenta, Job y cancelación? | [AvisosViewModel.kt:30](../app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt#L30), [AvisosViewModel.kt:33](../app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt#L33) y [AvisosViewModel.kt:44](../app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt#L44) |
| ¿Cómo se evita repetir el evento al rotar? | [AvisosViewModel.kt:22](../app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt#L22) |
| ¿Dónde se guarda el historial en memoria? | [AvisosRepository.kt:10](../app/src/main/java/com/leon/avisoscampus/data/AvisosRepository.kt#L10) |
| ¿Dónde se conserva el filtro propio de fase 1? | [AvisosViewModel.kt:68](../app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt#L68) |
| ¿Se toman veinte antes o después de filtrar? | [HistorialAvisos.kt:39](../app/src/main/java/com/leon/avisoscampus/ui/HistorialAvisos.kt#L39) |
| ¿Cómo se añade Cargador a los orígenes? | [Modelos.kt:2](../app/src/main/java/com/leon/avisoscampus/model/Modelos.kt#L2) |
| ¿Dónde están la lista, los filtros y el vacío? | [HistorialAvisos.kt:41](../app/src/main/java/com/leon/avisoscampus/ui/HistorialAvisos.kt#L41), [HistorialAvisos.kt:53](../app/src/main/java/com/leon/avisoscampus/ui/HistorialAvisos.kt#L53) y [HistorialAvisos.kt:65](../app/src/main/java/com/leon/avisoscampus/ui/HistorialAvisos.kt#L65) |
| ¿Dónde cambia la barra inferior por rail? | [PantallaAvisos.kt:126](../app/src/main/java/com/leon/avisoscampus/ui/PantallaAvisos.kt#L126) |
| ¿Cómo cambia el inicio a dos columnas? | [PantallaAvisos.kt:257](../app/src/main/java/com/leon/avisoscampus/ui/PantallaAvisos.kt#L257) |
| ¿Dónde se desactiva el movimiento con la escala del sistema? | [GlassControls.kt:52](../app/src/main/java/com/leon/avisoscampus/ui/GlassControls.kt#L52) |
| ¿Dónde están el botón de vidrio y su respuesta al tacto? | [GlassControls.kt:71](../app/src/main/java/com/leon/avisoscampus/ui/GlassControls.kt#L71) |
| ¿Dónde se animan campana, ondas y progreso? | [GlassControls.kt:127](../app/src/main/java/com/leon/avisoscampus/ui/GlassControls.kt#L127) |
| ¿Dónde están Outfit y los modos claro/oscuro? | [Theme.kt:20](../app/src/main/java/com/leon/avisoscampus/ui/theme/Theme.kt#L20), [Theme.kt:27](../app/src/main/java/com/leon/avisoscampus/ui/theme/Theme.kt#L27) y [Theme.kt:48](../app/src/main/java/com/leon/avisoscampus/ui/theme/Theme.kt#L48) |

El ViewModel decide y registra; la pantalla recoge el `SharedFlow` y llama al Notificador. ViewModel y Repository no reciben ni guardan `Context`. Los receptores reciben el parámetro que exige Android, leen el cambio y llaman a una función breve. La pantalla los registra con `RECEIVER_NOT_EXPORTED` y los retira en `onDispose`. El recolector de eventos y ambos receptores están fuera del contenido de las pestañas, por lo que pasar a Historial o Ajustes no los interrumpe.

`Avisos` usa importancia normal para botón, modo avión y cargador. `Urgentes` usa `IMPORTANCE_HIGH` para recordatorios; Android y la configuración del usuario determinan su presentación. `BigTextStyle` permite leer el texto completo al expandir. Ajustes solicita permiso solo en `SinPedir`; en `Concedido` o `Denegado` muestra una única acción para abrir la configuración. Inicio y notificación identifican el contenido como ejemplo del laboratorio.

La función propia de fase 1 sigue siendo el filtro por origen: primero filtra y después aplica `take(20)`, sin borrar la lista del Repository. Fase 2 añade `CARGADOR` y su interruptor independiente. La lista vive en memoria del proceso y el recordatorio en `viewModelScope`; no garantizan conservación ni entrega tras muerte del proceso, cierre forzado o reinicio.

En ancho de al menos 700 dp aparece el rail; desde 560 dp de contenido el inicio organiza imagen y acción en dos columnas. El contenido se limita a 760 dp. Las animaciones responden a estados reales y respetan escala cero. Las ilustraciones de vidrio son recursos locales: no se implementa refracción dinámica ni una API visual de Apple.

