# Revisión de Fase 1 — AvisosCampusLeon

Fecha: 25 de noviembre de 2026.

Alcance: lectura completa de `guia_completa.txt`, del generador `generar.py` (etapas 0 a 9), de los archivos Kotlin generados en etapa 9 y de las pruebas JVM presentes. Incluye una segunda lectura del ajuste de entrega de eventos aplicado por el ejecutor en `AvisosViewModel.kt`. No ejecuté Gradle, pruebas ni emulador, ni modifiqué el proyecto. La documentación y las capturas del cierre siguen a cargo del proceso de publicación; no se consideran faltantes por estar en preparación. El plan de cierre indicado contempla once commits, incluido el de documentación.

## Resultado

No encontré un incumplimiento bloqueante confirmado en los contratos de código de Fase 1. El flujo normal de diez segundos, la cancelación a mitad, la generación con permiso denegado y el filtro propio son coherentes con la guía. Las comprobaciones de comportamiento en Android necesitan la evidencia del ejecutor del emulador; esta revisión no las da por aprobadas.

| Contrato | Evidencia leída | Resultado estático |
| --- | --- | --- |
| MVVM + Repository | `AvisosViewModel` recibe la interfaz `AvisosRepository`; no importa ni recibe `Context`, `NotificationManager` o `BroadcastReceiver`. | Cumple. |
| Eventos de notificación | El ViewModel registra primero en el repositorio y emite el mismo aviso por `SharedFlow`; `PantallaAvisos` llama a `Notificador`. El helper `emitir` espera un suscriptor. | Cumple el esquema entregado y cubre la ausencia inicial de recolector. Véase el alcance de la mitigación abajo. |
| Canal | `CANAL = "avisos"`, nombre `Avisos`, importancia predeterminada y guardia API 26; creado desde `AvisosApp.onCreate`; Application declarada en Manifest. | Cumple. |
| Permiso | Manifest declara `POST_NOTIFICATIONS`; se pide mediante `RequestPermission` en API 33+; API anterior concede directamente. | Cumple. |
| Inicio y regreso de Ajustes | La consulta inicial comunica un permiso ya concedido. El observador de `ON_RESUME` vuelve a comprobarlo, actualiza una denegación ya conocida y se retira en `onDispose`. Al conceder, `permisoRespondido(true)` limpia el mensaje. | Cumple; conserva `Sin pedir` cuando la app aún no ha recibido una respuesta. |
| Notificación | Ícono, título, texto, prioridad, autocancelado e identificador del aviso. `mostrar` devuelve `false` sin permiso, con notificaciones/canal bloqueados o ante `SecurityException` de `notify`. | Cumple y trata los bloqueos del sistema. |
| Abrir desde notificación | `PendingIntent.getActivity` apunta a `MainActivity`, usa `FLAG_IMMUTABLE` y se conecta al builder. | Cumple. |
| Recordatorio | Job en `viewModelScope`, guardia de trabajo activo, cuenta 10 a 1 con `delay(1000)`, emisión al terminar y limpieza de `faltan` en `finally`. | Cumple. |
| Cancelación | `cancelarRecordatorio` cancela el Job; la cancelación durante un `delay` evita alcanzar el registro y la emisión. La pantalla impide iniciar otro mientras `faltan` siga activo. | No identifiqué una carrera que rompa el flujo normal de los botones. |
| Minimizar | El recolector está en `LaunchedEffect`, sin una condición `STARTED`/`RESUMED`. La composición de la Activity normalmente permanece al minimizar. El Job tampoco depende de la visibilidad. | El código permite recibir el aviso al minimizar; debe verificarse en Android. |
| Receptor | Comprueba `ACTION_AIRPLANE_MODE_CHANGED`, extrae `state` y llama a una función breve. Registro dinámico con `RECEIVER_NOT_EXPORTED`; retirada con el mismo Context/receptor en `onDispose`. | Cumple. La guardia del ViewModel también ignora cambios cuando el switch está apagado. |
| Historial | Repositorio antepone avisos; `HistorialAvisos` desconoce el ViewModel, muestra origen, título, texto y hora, y aplica `take(20)`. | Cumple. |
| Filtro propio | Estado de origen en ViewModel; chips Todos y cada origen; `avisos.filter { ... }.take(20)`. | Muestra hasta veinte avisos recientes del origen elegido, conserva toda la lista y no filtra las notificaciones del sistema. |
| Permiso denegado | El aviso ya queda registrado antes de intentar mostrarlo; la pantalla comunica el fallo, enseña el mensaje requerido y abre los Ajustes de notificaciones de la propia app. | Cumple. |

## Entrega de eventos durante la recreación

La versión inicial del generador crea `MutableSharedFlow<Aviso>()` sin replay y `PantallaAvisos.kt:52` instala el único recolector en la composición. Emitir directamente sin suscriptores perdía el evento aunque el aviso ya estuviera registrado en el historial. El propio ejemplo de la guía emplea este SharedFlow sin replay.

La secuencia concreta que exponía el límite era: iniciar un recordatorio; retirar el recolector de eventos; dejar vencer el Job; volver a instalar el recolector. Durante una recreación de Activity puede existir una ventana entre la destrucción de la composición anterior y el arranque de la siguiente. No reproduje esa coincidencia en dispositivo y no la presento como una prueba fallida.

El ejecutor añadió `emitir(aviso)` en `AvisosViewModel.kt:22–24`: espera `_eventos.subscriptionCount.first { it > 0 }` antes de `emit`. Los tres orígenes usan ahora el helper. Esto cubre el evento que vence cuando todavía no hay recolector, mantiene la decisión en el ViewModel y no añade replay ni dependencias de Android. La nueva prueba `avisoEsperaUnRecolectorYNoSeRepiteAlReconectarlo` cubre la conexión posterior y la ausencia de repetición. No ejecuté esa prueba. Leí también `fortalecer_eventos.py`, que aplica este avance de cierre después de las etapas del generador; el generador conserva deliberadamente las etapas originales y no se volverá a ejecutar sobre este cierre.

El ajuste es una mitigación de la ventana sin suscriptores, no una confirmación atómica de recepción: si un recolector desaparece mientras `emit` está suspendido, todavía puede terminar sin que la nueva composición reciba ese evento. No observé ese caso en Android. Conviene que la validación de rotación incluya una rotación muy cerca del vencimiento, además de comprobar que la cuenta continúa. No considero esta posibilidad un bloqueo del contrato de Fase 1 ni propongo introducir persistencia o Fase 2 para cubrirla.

## Límites intencionales y comprobaciones pendientes

- El recordatorio en corrutina y el historial en memoria no sobreviven a que Android termine el proceso o a forzar el cierre. Es el alcance de Fase 1, no un defecto que requiera introducir persistencia, alarmas o Fase 2.
- El registro del historial representa avisos generados, incluidos los que no se pudieron mostrar; no representa un recibo de entrega del sistema. La guía pide conservarlos al denegar el permiso.
- Canal, permiso real, notificaciones diferentes, apertura desde notificación, minimizado, cambios del modo avión, rotación y apariencia de las cinco capturas requieren comprobación en el emulador. No hay afirmaciones de ejecución en este informe.
- Las pruebas JVM presentes cubren estado inicial, registro/emisión, denegación y limpieza del mensaje, temporización, cuenta regresiva, cancelación, bloqueo de recordatorios duplicados, reprogramación, escucha del modo avión, filtro, espera del recolector sin replay y orden/IDs del repositorio. Su ejecución y resultados pertenecen al informe del ejecutor.

No se proponen mejoras de Fase 2 como condición para cerrar esta fase.
