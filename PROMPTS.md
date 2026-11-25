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

## 3. cargador

```text
Compórtate como un experto en BroadcastReceiver, corrutinas y MVVM. Necesito que AvisosCampus escuche conectar y desconectar el cargador con un interruptor independiente. Añade Origen.CARGADOR con texto Cargador, CargadorReceiver con ACTION_POWER_CONNECTED y ACTION_POWER_DISCONNECTED y un callback Boolean corto. AvisosViewModel expone escuchandoCargador, cambiarEscuchaCargador y alCambiarCargador; empieza apagado, ignora cambios cuando está apagado y cuando escucha registra Cargador / Conectado o Desconectado y emite el mismo aviso. La pantalla registra con RECEIVER_NOT_EXPORTED y siempre retira el receptor en onDispose, sin perderlo al navegar entre destinos. Conserva el modo avión, recordatorio, historial y filtro propio de fase1. Ningún Context llega al ViewModel o Repository. Prueba ambos estados y las dos escuchas independientes.
```

| Prompt | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Bloque 3 | Pendiente de ejecutar por la estudiante | Agregué la escucha del cargador con su propio interruptor y conservé el filtro por origen; implementación comprobada con Codex | Conservar el contrato de la guía y la decisión de diseño de Daniella |

## 4. recursos

```text
Compórtate como un experto en recursos visuales de Android. Ayúdame a integrar los recursos versionados de diseno/RECURSOS.md: ilustraciones aviso_campana.png, aviso_reloj.png y aviso_historial_vacio.png en drawable-nodpi con transparencia real, fuente local y licencia en res/font y diseno/licencias, logo y SVG propios en diseno/iconos con sus VectorDrawable de drawable. Mantén el icono pequeño de notificación separado del icono de aplicación. Consulta diseno/IMAGENES.json para los prompts completos y procedencia; no uses una imagen de toda la pantalla como interfaz ni dependas de archivos del Escritorio. Las ilustraciones se integran en la escena activa, no son stickers arbitrarios.
```

| Prompt | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Bloque 4 | Pendiente de ejecutar por la estudiante | Creé las ilustraciones de vidrio, el logo, los iconos SVG y la tipografía local del diseño; implementación comprobada con Codex | Conservar el contrato de la guía y la decisión de diseño de Daniella |

## 5. diseno

```text
Compórtate como un experto en diseño Android, Jetpack Compose, Material 3 y animaciones. Necesito replicar el diseño final Luz y vidrio de AvisosCampus siguiendo DESIGN.md, docs/capturas_fase2, los SVG y las tres ilustraciones de drawable-nodpi. Mantén azul luminoso, coral contenido, blanco frío y la fuente local. Sustituye las cinco tarjetas apiladas por tres destinos nativos: Avisos, Historial y Ajustes. Avisos muestra el estado de permiso, un selector Ahora / En 10 s, campana o reloj de vidrio y una sola acción principal. La cuenta usa los segundos reales del ViewModel, permite cancelar y sigue reconocible al navegar. Historial muestra los veinte más recientes, con origen, hora y el filtro propio; el vacío usa su ilustración. Ajustes agrupa permiso, acceso a Ajustes Android y escuchas independientes de avión/cargador. La recogida de eventos y los receptores viven fuera del contenido de cada pestaña y se retiran al salir de la composición. Da respuesta al tacto, ondas al enviar, animación de dígitos/progreso y cambios de sección y entradas del historial. Respeta movimiento desactivado, 48dp, texto130%, oscuro, horizontal, teclado e insets. Los controles usan capas claras y reflejos; no prometas refracción del fondo que el código no hace. Mantén toda la lógica MVVM y pruebas de la base, sin inventar entrega después de matar el proceso ni historial persistente. Usa los colores exactos de Theme.kt: fondo claro #F6F8FF, tinta #14204C, azul #244DDE; coral #FA806F se reserva a detalles e ilustraciones, con variantes de contraste para texto y controles. La fuente Outfit local tiene pesos 400, 500, 600 y 700. Pantalla con margen horizontal24dp y separaciones12/18dp; escena vertical228dp, dos columnas desde560dp de contenido y rail nativo desde700dp de ventana. AvisosGlassButton mide al menos56dp, radio24dp, borde blanco y gradiente suave sobre color Material, presión0.975 con spring650 y amortiguación0.65. La campana gira -9/7/-4/0 grados y emite dos ondas durante760ms solo tras un evento; el progreso usa faltan/10 y tween800ms. Los cambios de sección combinan fade180 y desplazamiento220ms; desactiva esas transiciones con ANIMATOR_DURATION_SCALE=0. Conserva etiquetas visibles y semántica Android para navegación y switches. Las ilustraciones son recursos transparentes; no llevan texto incrustado.
```

| Prompt | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Bloque 5 | Pendiente de ejecutar por la estudiante | Reorganicé Avisos, Historial y Ajustes con controles de vidrio, una tarea visible y animaciones de los procesos; implementación comprobada con Codex | Conservar el contrato de la guía y la decisión de diseño de Daniella |

## 6. cierre

```text
Compórtate como un experto en pruebas y documentación Android. Ayúdame a verificar la fase2 de AvisosCampus: ambos canales y prioridad correcta, BigTextStyle con texto largo, receptores de avión/cargador activos y apagados, filtro por origen, permisos denegados, recordatorio, cancelación, rotación y PendingIntent. Usa pruebas de tiempo virtual y emulador real; conserva main de fase1. Captura la versión final clara, oscura, fuente130%, horizontal y sin animaciones. Actualiza README con comparación de solo dos fases, galería, rutas y líneas de código, PROMPTS.md y procedencia de recursos. Documenta resultados y limitaciones reales. No atribuyas una sesión de Codex a Gemini ni asegures resultados de pruebas que no ejecutaste. Aplica también las dos correcciones de la revisión: en Ajustes muestra Permitir notificaciones solamente en SinPedir; en Concedido o Denegado muestra un solo Abrir ajustes. En inicio identifica el contenido con Aviso de ejemplo del laboratorio., conservando Tarea lista y Tu entrega fue registrada que exige la guía. En la notificación añade setSubText con Ejemplo del laboratorio para evitar afirmar una entrega real. El historial indica que son ejemplos de la sesión. Adapta las pruebas a esas acciones según estado y comprueba EXTRA_SUB_TEXT además de BIG_TEXT. Recaptura todos los estados del mismo teléfono después de corregir; no uses las imágenes anteriores como si fueran de esta versión.
```

| Prompt | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Bloque 6 | Pendiente de ejecutar por la estudiante | Ajusté el permiso según su estado, identifiqué los avisos de ejemplo y documenté las pruebas, capturas y prompts finales; implementación comprobada con Codex | Conservar el contrato de la guía y la decisión de diseño de Daniella |

