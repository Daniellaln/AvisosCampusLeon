# Código de cada commit de fase 2

`mejora-ia` parte de los once commits de `main` y añade seis avances: diecisiete en total al cerrar esta fase. Los cinco SHA publicados se tomaron de `fase2_publicados.json`; sus líneas se comprobaron en las copias acumulativas de entrega. El sexto se consulta por la rama para evitar escribir su propio SHA dentro del commit.

En Android Studio: Git → Log → commit → diff. Para explicar el proceso, sigue canales → texto expandido → cargador → recursos → diseño → revisión y pruebas. La [guía de main](GUIA_COMMITS_MAIN.md) conserva el recorrido de fase 1.

## 1. Añadí el canal Urgentes para los recordatorios y conservé Avisos para las otras notificaciones

`ba9130dfb89517c9ae416575f30503a5e00b9aca`

El canal `Avisos` conserva importancia normal. `Urgentes` se crea con `IMPORTANCE_HIGH` y se elige cuando el origen es `RECORDATORIO`; la configuración de Android puede modificar su presentación.

[Crear Urgentes: Notificador.kt:23](https://github.com/Daniellaln/AvisosCampusLeon/blob/ba9130dfb89517c9ae416575f30503a5e00b9aca/app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt#L23) · [Elegir canal: Notificador.kt:30](https://github.com/Daniellaln/AvisosCampusLeon/blob/ba9130dfb89517c9ae416575f30503a5e00b9aca/app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt#L30)

## 2. Mostré el contenido completo de las notificaciones al expandirlas con BigTextStyle

`5766af2188972883ec6062772e30b17c71414a4d`

`NotificationCompat.BigTextStyle` permite leer el texto largo al expandir el aviso en la bandeja. Conserva título, texto breve y apertura de la app.

[Texto expandido: Notificador.kt:35](https://github.com/Daniellaln/AvisosCampusLeon/blob/5766af2188972883ec6062772e30b17c71414a4d/app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt#L35)

## 3. Agregué la escucha del cargador con su propio interruptor y conservé el filtro por origen

`83a67ad85e60e4cf4e36a1c93ebece0e45117092`

El receptor informa conexión o desconexión mediante un callback breve. Su escucha es independiente de modo avión. Se añade `CARGADOR` y se conserva la función propia de fase 1: filtrar por origen antes de tomar los veinte avisos, sin borrar el historial.

[Receptor: CargadorReceiver.kt:5](https://github.com/Daniellaln/AvisosCampusLeon/blob/83a67ad85e60e4cf4e36a1c93ebece0e45117092/app/src/main/java/com/leon/avisoscampus/receiver/CargadorReceiver.kt#L5) · [Registrar cambio: AvisosViewModel.kt:58](https://github.com/Daniellaln/AvisosCampusLeon/blob/83a67ad85e60e4cf4e36a1c93ebece0e45117092/app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt#L58) · [Filtro y límite: HistorialAvisos.kt:15](https://github.com/Daniellaln/AvisosCampusLeon/blob/83a67ad85e60e4cf4e36a1c93ebece0e45117092/app/src/main/java/com/leon/avisoscampus/ui/HistorialAvisos.kt#L15)

## 4. Creé las ilustraciones de vidrio, el logo, los iconos SVG y la tipografía local del diseño

`befb0be7bf18f58bc9f98ef4b04e56604c1be453`

Se entregan tres PNG transparentes en `drawable-nodpi`, sus prompts y procedencia; catorce SVG editables y su adaptación a VectorDrawable; Outfit local con licencia SIL OFL. La app utiliza los archivos locales durante el uso.

[Ilustraciones y procedencia: RECURSOS.md:3](https://github.com/Daniellaln/AvisosCampusLeon/blob/befb0be7bf18f58bc9f98ef4b04e56604c1be453/diseno/RECURSOS.md#L3) · [Logo e iconos: RECURSOS.md:5](https://github.com/Daniellaln/AvisosCampusLeon/blob/befb0be7bf18f58bc9f98ef4b04e56604c1be453/diseno/RECURSOS.md#L5) · [Outfit y licencia: RECURSOS.md:7](https://github.com/Daniellaln/AvisosCampusLeon/blob/befb0be7bf18f58bc9f98ef4b04e56604c1be453/diseno/RECURSOS.md#L7) · [Metadata de imágenes](https://github.com/Daniellaln/AvisosCampusLeon/blob/befb0be7bf18f58bc9f98ef4b04e56604c1be453/diseno/IMAGENES.json)

## 5. Reorganicé Avisos, Historial y Ajustes con controles de vidrio, una tarea visible y animaciones de los procesos

`235cfd2a5fc11ee3b5a8ec23252c8adf9a5b7ba3`

La dirección «Luz y vidrio» reúne azul, coral y superficies claras. Tres destinos separan la acción principal, la consulta y los ajustes. La campana responde al envío, el progreso sigue la cuenta real y las inserciones del historial se animan. La escala cero del sistema desactiva el movimiento. Los controles son Material 3 nativos con reflejos y gradientes; no hay refracción dinámica del fondo ni API de Apple.

[Organización: PantallaAvisos.kt:45](https://github.com/Daniellaln/AvisosCampusLeon/blob/235cfd2a5fc11ee3b5a8ec23252c8adf9a5b7ba3/app/src/main/java/com/leon/avisoscampus/ui/PantallaAvisos.kt#L45) · [Escala del sistema: GlassControls.kt:52](https://github.com/Daniellaln/AvisosCampusLeon/blob/235cfd2a5fc11ee3b5a8ec23252c8adf9a5b7ba3/app/src/main/java/com/leon/avisoscampus/ui/GlassControls.kt#L52) · [Botón: GlassControls.kt:71](https://github.com/Daniellaln/AvisosCampusLeon/blob/235cfd2a5fc11ee3b5a8ec23252c8adf9a5b7ba3/app/src/main/java/com/leon/avisoscampus/ui/GlassControls.kt#L71) · [Ilustración reactiva: GlassControls.kt:127](https://github.com/Daniellaln/AvisosCampusLeon/blob/235cfd2a5fc11ee3b5a8ec23252c8adf9a5b7ba3/app/src/main/java/com/leon/avisoscampus/ui/GlassControls.kt#L127) · [Inserciones: HistorialAvisos.kt:80](https://github.com/Daniellaln/AvisosCampusLeon/blob/235cfd2a5fc11ee3b5a8ec23252c8adf9a5b7ba3/app/src/main/java/com/leon/avisoscampus/ui/HistorialAvisos.kt#L80)

## 6. Cierre: revisión, pruebas y documentación final

[Versión de cierre en mejora-ia](https://github.com/Daniellaln/AvisosCampusLeon/tree/mejora-ia) · [Rutas y líneas finales](GUIA_CODIGO_FASE2.md) · [Verificación final](VERIFICACION_FASE2.md) · [Galería final](GALERIA_FASE2.md)

La revisión final se resolvió en un único lote: Ajustes pide permiso solo en `SinPedir`; cuando ya está concedido o denegado ofrece una sola acción para abrir configuración. Inicio y notificación identifican el contenido como ejemplo del laboratorio y conservan «Tarea lista / Tu entrega fue registrada» como texto de práctica. Este cierre reúne pruebas, capturas y documentación del diseño; los resultados corresponden a la evidencia final de verificación enlazada, no se atribuyen a los commits anteriores.

El historial continúa en memoria y el recordatorio usa `viewModelScope`. El cierre no promete entrega tras muerte del proceso ni persistencia después de reiniciar. Codex asistió el trabajo; los prompts para Gemini se preparan como documentación y no prueban una ejecución de Gemini.

