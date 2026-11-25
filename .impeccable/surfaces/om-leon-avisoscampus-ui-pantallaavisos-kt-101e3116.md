---
version: 1
slug: "om-leon-avisoscampus-ui-pantallaavisos-kt-101e3116"
primary_target: "app/src/main/java/com/leon/avisoscampus/ui/PantallaAvisos.kt"
related_targets: ["app/src/main/java/com/leon/avisoscampus/ui/HistorialAvisos.kt", "app/src/main/java/com/leon/avisoscampus/ui/GlassControls.kt", "app/src/main/java/com/leon/avisoscampus/ui/theme/Theme.kt"]
---

# AvisosCampus fase 2

Modo: operate. Plataforma: Android nativo. Daniella usa la app en una demostración académica con datos ficticios. La implementación final organiza una tarea principal a la vez y navegación Material entre Avisos, Historial y Ajustes.

Dirección confirmada: **Luz y vidrio**, azul luminoso, coral y vidrio claro. Camino de implementación: código Android nativo. No se aprobó una composición renderizada; la decisión y la dirección escrita son la autoridad visual.

## Direction contract

THESIS: El aviso es una acción sencilla que se ve ocurrir; las cinco tarjetas apiladas de la base se sustituyen por tres destinos y un foco activo.

OWN-WORLD: Blanco frío y azul luminoso con coral y lila contenidos, tipografía Outfit legible, ilustraciones ópticas de vidrio y controles nativos con objetivos de al menos 48 dp.

STORY: Elegir Ahora o En 10 s, tocar la acción y ver el envío o la cuenta cancelable. Historial permite revisar y filtrar los avisos generados; Ajustes reúne permiso, canales y las dos escuchas independientes.

FIRST VIEWPORT: Marca compacta y permiso arriba; selector, campana o reloj de vidrio, identificación del aviso como ejemplo del laboratorio y acción principal. Último aviso solo si existe. Navegación inferior o rail según el ancho.

FORM: Galería de esculturas ópticas, candidata 5 de 7 y semilla fa82d61e. Se traducen tipo, paleta, densidad y firma a Material 3; las referencias culturales no son material de la app.

SIGNATURE: Campana y ondas responden al envío; la cuenta mueve el progreso con sus segundos reales; Historial inserta eventos por id y las secciones cambian con eje compartido. La escala cero del sistema elimina animaciones.

FINISH: La entrega conserva el informe de revisión y aplica sus dos ajustes en un único lote: permiso condicional en Ajustes e identificación de contenido de práctica. DESIGN.md y .impeccable/design.json documentan los tokens y componentes reales. Los tres rasters incluyen procedencia y prompts en diseno/IMAGENES.json. La comprobación y las capturas finales se mantienen separadas de fase 1.

## Resultado implementado

Se conserva el filtro propio por origen desde fase 1, con límite de veinte después de filtrar, y se añade Cargador. Las tres mejoras aprobadas son canal Urgentes para recordatorios, texto expandido y escucha independiente del cargador. El recolector de eventos y ambos receptores quedan fuera de los destinos.

Ajustes solicita permiso solo en SinPedir. Concedido y Denegado muestran una única acción Abrir ajustes. Inicio y notificación dicen que el aviso es un ejemplo del laboratorio; Tarea lista y Tu entrega fue registrada siguen siendo el contenido ficticio requerido.

El tratamiento de vidrio utiliza ilustraciones, gradientes y bordes de Compose. No se atribuye a API de Apple ni a refracción dinámica. Historial y recordatorio dependen de la sesión del proceso, sin promesa de persistencia tras su muerte.

## Evidencia y documentación

- [Sistema visual](../../DESIGN.md) y [sidecar](../design.json).
- [Galería de fase 2](../../docs/GALERIA_FASE2.md).
- [Código final y líneas](../../docs/GUIA_CODIGO_FASE2.md).
- [Pruebas de fase 2](../../docs/VERIFICACION_FASE2.md).
- [Recursos y licencias](../../diseno/RECURSOS.md).

