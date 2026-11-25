# Prompts

Estos prompts se pueden utilizar uno por uno, siguiendo este orden, para replicar los resultados. Parte del cierre de main; todos los recursos están en esta rama. La implementación y revisión se hicieron con asistencia de Codex. Los bloques están preparados para Gemini, sin afirmar una ejecución ni un parecido porcentual no medido.

## 1. canal urgente

```text
Compórtate como un experto en notificaciones Android y MVVM. Ayúdame a añadir a AvisosCampus un canal Urgentes con id urgentes e IMPORTANCE_HIGH, conservando Avisos con id avisos e IMPORTANCE_DEFAULT. crearCanal crea ambos al iniciar AvisosApp. Notificador.mostrar elige Urgentes solo si aviso.origen es RECORDATORIO y usa PRIORITY_HIGH como compatibilidad; los demás conservan Avisos y PRIORITY_DEFAULT. Comprueba el permiso y el bloqueo del canal que realmente se eligió. Mantén PendingIntent inmutable, ids distintos y autoCancel. No pongas Context ni NotificationManager en el ViewModel. Prueba canales y enrutamiento antes de continuar.
```

| Prompt | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Bloque 1 | Pendiente de ejecutar por la estudiante | Añadí el canal Urgentes para los recordatorios y conservé Avisos para las otras notificaciones; implementación comprobada con Codex | Conservar el contrato de la guía y la decisión de diseño de Daniella |

## 2. texto expandido

```text
Compórtate como un experto en Android con NotificationCompat. Ayúdame a mostrar el texto completo de cada aviso al expandirlo mediante BigTextStyle().bigText(aviso.texto) dentro de Notificador.mostrar. Conserva título, setContentText, canales, PendingIntent inmutable, ids y autoCancel. No recortes el texto ni simules una notificación con una vista de Compose. Verifica con un aviso ficticio largo que Notification.EXTRA_BIG_TEXT conserva el texto completo.
```

| Prompt | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Bloque 2 | Pendiente de ejecutar por la estudiante | Mostré el contenido completo de las notificaciones al expandirlas con BigTextStyle; implementación comprobada con Codex | Conservar el contrato de la guía y la decisión de diseño de Daniella |

