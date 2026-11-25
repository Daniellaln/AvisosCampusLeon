# AvisosCampus

<!-- impeccable:product-schema 1 -->

## Platform

android

## Users

Daniella Leon para desarrollar y sustentar el laboratorio 15. Es una app de práctica con avisos ficticios, sin usuarios autenticados.

## Product Purpose

Entender notificaciones locales, permisos, recordatorios y anuncios del celular. El recorrido permite reconocer la acción y su resultado sin mostrar todos los controles a la vez.

## Operating Context

Kotlin, Jetpack Compose, Material 3 y arquitectura MVVM + Repository. Paquete com.leon.avisoscampus. Base reconstruida a partir de los contratos, modelos y ejemplos de GLAB-S15-JLEONS-2026-1.docx, porque no se recibió un proyecto descargable.

La entrega tiene dos fases: main conserva la fase 1 funcional de once commits; mejora-ia añade seis avances de fase 2, incluido su cierre de pruebas y documentación. La asistencia de Codex se declara. Los prompts para Gemini son material preparado; no prueban que Gemini se haya ejecutado.

## Capabilities and Constraints

Permiso POST_NOTIFICATIONS con estados Sin pedir, Permitido y Denegado. Canal Avisos para botón, modo avión y cargador; canal Urgentes con IMPORTANCE_HIGH para recordatorios. Notificaciones locales con ids distintos, PendingIntent inmutable, autoCancel y BigTextStyle para texto expandido.

Cuenta de diez segundos cancelable en viewModelScope, escucha de modo avión y escucha de cargador con interruptores independientes. El recolector de eventos y los receptores permanecen fuera del contenido de los destinos, por lo que navegar entre Avisos, Historial y Ajustes no interrumpe esas funciones.

La función propia desde fase 1 es el filtro del historial por origen; se conserva y añade Cargador. Primero se filtra y después se toman los veinte avisos más recientes. El Repository mantiene el historial en memoria del proceso. No hay Firebase, servidor, lectura de JSON ni internet necesario al usar la app. Las descargas iniciales de desarrollo son independientes del uso.

El recordatorio no es una alarma persistente: no garantiza entrega tras muerte del proceso, cierre forzado, reinicio o suspensión prolongada. El historial registra avisos generados, incluso si Android bloquea su presentación; no es un recibo de entrega. Inicio y notificación identifican su contenido como ejemplo del laboratorio, conservando los textos de práctica exigidos.

## Brand Commitments

Daniella confirmó azul luminoso, coral y vidrio claro, con la dirección Luz y vidrio. Pide un resultado vivo, organizado y fácil de usar. Se conserva el nombre AvisosCampus y una marca propia, sin logotipo institucional autorizado.

La fase 2 se implementa con código Android nativo. La fase 1 es evidencia histórica de funcionamiento y no fija su composición visual. Se usan Outfit local, tres ilustraciones transparentes y catorce iconos SVG con sus equivalentes VectorDrawable. El tratamiento óptico no usa API de Apple ni refracción dinámica del fondo.

## Evidence on Hand

Fase 1 publicada en main con once commits, veintinueve pruebas y doce capturas reales conservadas en docs/capturas y docs/pruebas. Su cierre es 86a28c1.

Fase 2 conserva veintitrés capturas y evidencia separada en docs/capturas_fase2 y docs/pruebas_fase2. La compilación, veintitrés pruebas JVM, catorce pruebas Android y lint se repitieron después de las correcciones finales. Las cifras y el recorrido se detallan en docs/VERIFICACION_FASE2.md.

El entorno de prueba es Windows y Pixel 8 Pro emulado con Android API 37. No se afirma validación en un teléfono físico, en Mac ni en un laboratorio físico. No hace falta una cuenta ni una clave externa para usar la app.

## Product Principles

Una tarea principal visible. Permisos y contenido de práctica honestos. Función propia desde la base, conservada al rediseñar. La imagen ayuda a reconocer la acción; los controles y estados siguen siendo Android. Historial y Ajustes reciben el mismo cuidado que el inicio.

## Accessibility & Inclusion

Objetivos táctiles de al menos 48 dp, texto en sp, ampliación de texto a 1.3, colores semánticos legibles y modo oscuro. Respetar insets y Atrás de Android. MotionSettings observa la escala de animación del sistema y elimina el movimiento cuando vale cero. La información esencial nunca depende de una animación.


