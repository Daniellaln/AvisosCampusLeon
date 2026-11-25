# AvisosCampus

<!-- impeccable:product-schema 1 -->

## Platform

android

## Users

Daniella Leon para desarrollar y sustentar el laboratorio 15. App de práctica con avisos ficticios, sin usuarios autenticados.

## Product Purpose

Entender notificaciones locales, permisos, recordatorios y anuncios del celular. El recorrido debe ser fácil de entender sin enseñar todos los controles al mismo tiempo.

## Operating Context

Kotlin, Jetpack Compose, Material 3, MVVM + Repository. Paquete com.leon.avisoscampus. Guía GLAB-S15-JLEONS-2026-1.docx. main conserva fase1; mejora-ia desarrolla fase2. No se recibió base descargable; se reconstruyó desde el documento.

## Capabilities and Constraints

Permiso POST_NOTIFICATIONS, canal Avisos, avisos inmediatos y PendingIntent inmutable, cuenta de10s cancelable, receptor del modo avión y últimos20avisos. Función propia desde fase1: filtro del historial por origen. Daniella aprobó para fase2 texto expandido, canal urgente de recordatorios y receptor del cargador. No hay Firebase, internet ni persistencia remota. El historial vive en el proceso y el recordatorio en viewModelScope. No inventar entrega después de matar el proceso.

## Brand Commitments

Daniella confirmó azul luminoso, coral, vidrio claro e imágenes. Pide un resultado lleno de vida, organizado y bonito que no abrume. Nombre AvisosCampus y marca propia; no hay logotipo institucional autorizado. El diseño de fase1 es evidencia de funcionamiento, no autoridad sobre la fase2.

## Evidence on Hand

Fase1 publicada con11commits,29pruebas y12capturas reales en docs/capturas. main final86a28c1. AndroidAPI37emulado enWindows. No se afirma teléfono físico oMac. Ninguna cuenta oclave externa necesaria.

## Product Principles

Una tarea principal visible. Datos y permisos honestos. Función propia desde la base y conservada al rediseñar. La imagen ayuda a reconocer la acción; no reemplaza sus controles. El detalle y los estados tienen el mismo cuidado que el inicio.

## Accessibility & Inclusion

Áreas táctiles48dp, texto ensp y ampliación1.3, contrasteAA, modooscuro, insets yAtrásAndroid. Desactivar movimiento cuando el sistema lo pida.
