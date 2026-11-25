# Laboratorio 15 AvisosCampus

Daniella Leon. Paquete `com.leon.avisoscampus`. Repositorio público: https://github.com/Daniellaln/AvisosCampusLeon.

Solo dos fases: `main` conserva la fase 1 funcional; `mejora-ia` se creará al iniciar la fase 2. Ahora se implementa únicamente fase 1.

## Alcance

Se reconstruye la base desde los modelos, Repository, esqueletos y contratos del Word. No se recibió un proyecto base descargable. Kotlin, Compose, Material 3, MVVM + Repository. Ninguna clave, Firebase, internet, servicio permanente ni alarma exacta en fase 1.

Una pantalla desplazable con las cinco tarjetas en el orden de la guía: permiso, aviso inmediato, recordatorio, receptor e historial. Android dibuja el diálogo de permiso, Ajustes y panel de notificaciones.

Los pasos son: base; canal Avisos; permiso; notificar; PendingIntent; recordatorio de diez segundos; receptor del modo avión; historial de veinte; permiso denegado; filtro propio del historial; documentación y capturas. Cada paso debe compilar antes de su commit y tiene copia acumulativa completa.

## Función propia

Filtrar el historial por origen (Todos, Botón, Recordatorio y Modo avión) sin borrar datos. Es adicional a la guía, entra en fase 1 y se conservará en fase 2. Se filtra antes de tomar los veinte más recientes.

## Reglas y límites

El ViewModel usa el Repository y emite SharedFlow; no conoce Context, NotificationManager ni BroadcastReceiver. La UI recoge eventos y llama al Notificador. El receptor solo entrega el Boolean, se registra con RECEIVER_NOT_EXPORTED y se retira en onDispose.

La cuenta usa viewModelScope y Job, continúa al rotar o minimizar mientras el proceso vive. No garantiza ejecución tras forzar cierre, matar el proceso, reiniciar ni larga suspensión. El historial es memoria de la aplicación y no sobrevive a su proceso; persistencia corresponde a una mejora de fase 2.

La guía titula fase 1 sin IA y propone mensajes con prefijos. Por petición expresa de Daniella se trabaja con asistencia de Codex y mensajes naturales detallados; no se afirmará ausencia de IA. Se respetan los contratos y los pasos independientes.

## Verificación y entrega

Compilación en cada paso; JVM con tiempo virtual para cuenta/cancelación/duplicados/SharedFlow/Repository/filtro; pruebas Android de UI, canal, notificaciones y receptor. Recorrido real en emulador API 37 para aceptar/denegar permiso, dos avisos, tocar una notificación, minimizar, cancelar, rotar, avión activo/inactivo, filtros y Ajustes.

README con árbol, diagrama MVVM, respuestas Para pensar y al menos cinco capturas reales. Copia completa con .git y carpetas acumulativas en DANIELLA/LAB15_AvisosCampus. No se incluye local.properties ni build/cachés. Fase 2 pendiente de autorización.
