# Dónde está el código

Raíz: app/src/main/java/com/leon/avisoscampus/. Líneas comprobadas de la versión final de main; para otro commit abre Git Log y su diff o la copia acumulativa correspondiente.

| Pregunta o sinónimos | Archivo y línea |
|---|---|
| Canal / categoría de notificación | [notificacion/Notificador.kt:15](../app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt#L15) |
| Permiso / POST_NOTIFICATIONS | [notificacion/Notificador.kt:21](../app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt#L21) |
| Mostrar / NotificationCompat / notify | [notificacion/Notificador.kt:23](../app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt#L23) |
| Abrir desde aviso / PendingIntent | [notificacion/Notificador.kt:30](../app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt#L30) |
| Historial / Repository / memoria | [data/AvisosRepository.kt:10](../app/src/main/java/com/leon/avisoscampus/data/AvisosRepository.kt#L10) |
| Aviso inmediato / botón | [viewmodel/AvisosViewModel.kt:26](../app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt#L26) |
| Corrutina / cuenta / delay / Job | [viewmodel/AvisosViewModel.kt:33](../app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt#L33) |
| Cancelar / trabajo | [viewmodel/AvisosViewModel.kt:44](../app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt#L44) |
| Estado / permiso respondido | [viewmodel/AvisosViewModel.kt:16](../app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt#L16) |
| SharedFlow / evento / recreación | [viewmodel/AvisosViewModel.kt:22](../app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt#L22) |
| Receptor / BroadcastReceiver | [receiver/ModoAvionReceiver.kt:6](../app/src/main/java/com/leon/avisoscampus/receiver/ModoAvionReceiver.kt#L6) |
| Registrar y quitar / DisposableEffect | [ui/PantallaAvisos.kt:58](../app/src/main/java/com/leon/avisoscampus/ui/PantallaAvisos.kt#L58) |
| Registrar modo avión / boolean | [viewmodel/AvisosViewModel.kt:48](../app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt#L48) |
| Denegado / aviso de error | [viewmodel/AvisosViewModel.kt:55](../app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt#L55) |
| Abrir Ajustes / Settings | [ui/PantallaAvisos.kt:80](../app/src/main/java/com/leon/avisoscampus/ui/PantallaAvisos.kt#L80) |
| Función propia / filtro | [viewmodel/AvisosViewModel.kt:58](../app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt#L58) |
| Vista / historial / take(20) | [ui/HistorialAvisos.kt:15](../app/src/main/java/com/leon/avisoscampus/ui/HistorialAvisos.kt#L15) |
| Application / inicio | [AvisosApp.kt:7](../app/src/main/java/com/leon/avisoscampus/AvisosApp.kt#L7) |
| Inyección / Factory / MainActivity | [MainActivity.kt:13](../app/src/main/java/com/leon/avisoscampus/MainActivity.kt#L13) |

Dependencias/librerías: [app/build.gradle.kts](../app/build.gradle.kts). Permiso y Application: [AndroidManifest.xml](../app/src/main/AndroidManifest.xml). Icono/foto del aviso: res/drawable/ic_notificacion.xml; no hay fotografías ni recursos remotos en fase 1.

Para explicar los commits: base → canal → permiso → notificar → abrir app → cuenta/cancelación → receptor → historial → denegado → filtro propio → cierre probado.
