---
name: "AvisosCampus"
description: "Luz y vidrio para avisos locales de práctica en Android."
colors:
  azul-luminoso: "#244DDE"
  sobre-azul: "#FFFFFF"
  azul-suave: "#E0E7FF"
  tinta-azul: "#142D91"
  coral-legible: "#A34031"
  coral-suave: "#FFDAD3"
  tinta-coral: "#622318"
  lila: "#615294"
  lila-suave: "#E7DEFF"
  campo-frio: "#F6F8FF"
  tinta: "#14204C"
  superficie-clara: "#FDFDFF"
  texto-secundario: "#505B7A"
  contenedor-bajo: "#F2F5FF"
  contenedor-alto: "#E6ECFB"
  borde-suave: "#D4DBED"
  error: "#AE2E38"
  azul-noche: "#BAC7FF"
  azul-contenedor-noche: "#233D9D"
  coral-noche: "#FFB4A6"
  lila-noche: "#CAC3FF"
  campo-noche: "#0D1530"
  tinta-noche: "#E7ECFF"
  superficie-noche: "#111A36"
  texto-secundario-noche: "#C0CAE6"
  contenedor-bajo-noche: "#151F3C"
  contenedor-alto-noche: "#24304F"
  borde-noche: "#414D6B"
  error-noche: "#FFB2B8"
typography:
  display-large: { fontFamily: "Outfit", fontSize: "42sp", fontWeight: 600, lineHeight: "48sp", letterSpacing: "-0.4sp" }
  display-medium: { fontFamily: "Outfit", fontSize: "36sp", fontWeight: 600, lineHeight: "42sp", letterSpacing: "-0.4sp" }
  display-small: { fontFamily: "Outfit", fontSize: "32sp", fontWeight: 600, lineHeight: "38sp", letterSpacing: "-0.4sp" }
  headline-large: { fontFamily: "Outfit", fontSize: "28sp", fontWeight: 600, lineHeight: "34sp", letterSpacing: "-0.4sp" }
  headline-medium: { fontFamily: "Outfit", fontSize: "26sp", fontWeight: 600, lineHeight: "32sp", letterSpacing: "-0.4sp" }
  headline-small: { fontFamily: "Outfit", fontSize: "24sp", fontWeight: 600, lineHeight: "30sp", letterSpacing: "-0.4sp" }
  title-large: { fontFamily: "Outfit", fontSize: "22sp", fontWeight: 600, lineHeight: "28sp", letterSpacing: "0sp" }
  title-medium: { fontFamily: "Outfit", fontSize: "18sp", fontWeight: 600, lineHeight: "24sp", letterSpacing: "0sp" }
  title-small: { fontFamily: "Outfit", fontSize: "16sp", fontWeight: 500, lineHeight: "22sp", letterSpacing: "0sp" }
  body-large: { fontFamily: "Outfit", fontSize: "16sp", fontWeight: 400, lineHeight: "24sp", letterSpacing: "0sp" }
  body-medium: { fontFamily: "Outfit", fontSize: "14sp", fontWeight: 400, lineHeight: "21sp", letterSpacing: "0sp" }
  body-small: { fontFamily: "Outfit", fontSize: "12sp", fontWeight: 400, lineHeight: "18sp", letterSpacing: "0sp" }
  label-large: { fontFamily: "Outfit", fontSize: "14sp", fontWeight: 600, lineHeight: "20sp", letterSpacing: "0sp" }
  label-medium: { fontFamily: "Outfit", fontSize: "14sp", fontWeight: 500, lineHeight: "20sp", letterSpacing: "0sp" }
  label-small: { fontFamily: "Outfit", fontSize: "12sp", fontWeight: 500, lineHeight: "18sp", letterSpacing: "0sp" }
rounded:
  control-vidrio: "24dp"
  aviso-en-curso: "14dp"
spacing:
  detalle: "4dp"
  texto: "5dp"
  compacto: "8dp"
  icono: "10dp"
  grupo: "12dp"
  fila: "14dp"
  seccion: "16dp"
  inicio: "18dp"
  margen: "24dp"
  fondo-historial: "28dp"
components:
  button-primary:
    backgroundColor: "{colors.azul-luminoso}"
    textColor: "{colors.sobre-azul}"
    typography: "{typography.label-large}"
    rounded: "{rounded.control-vidrio}"
    padding: "15dp 24dp"
    height: "min 56dp"
  button-secondary:
    backgroundColor: "{colors.contenedor-alto}"
    textColor: "{colors.tinta}"
    typography: "{typography.label-large}"
    rounded: "{rounded.control-vidrio}"
    padding: "15dp 24dp"
    height: "min 56dp"
  history-origin:
    backgroundColor: "{colors.azul-suave}"
    textColor: "{colors.tinta-azul}"
    size: "44dp"
  history-reminder:
    backgroundColor: "{colors.coral-suave}"
    textColor: "{colors.tinta-coral}"
    size: "44dp"
  filter:
    typography: "{typography.label-large}"
    height: "min 48dp"
  navigation:
    backgroundColor: "{colors.contenedor-bajo}"
    textColor: "{colors.tinta}"
---

# Design System: AvisosCampus

## Overview

**Creative North Star: "Luz y vidrio"**

Luz y vidrio reúne azul luminoso, coral contenido y un campo blanco frío. Las imágenes ópticas dan identidad a una tarea sencilla; los textos y controles quedan fuera de sus reflejos para mantener la lectura.

Es una aplicación Android nativa y clara. La forma expresiva se concentra en la ilustración central y en respuestas breves a acciones reales. El modo oscuro conserva esa familia con superficies azul noche. La composición de cada pantalla pertenece a su contrato de superficie; este documento fija el lenguaje reutilizable.

Dirección confirmada por Daniella: azul, coral y vidrio claro. Se documenta la implementación de fase 2, después del lote único de correcciones de la revisión final. Es una extracción de código Android, no una propuesta de reemplazo.

**Key Characteristics:**

- Vidrio ilustrado y controles Material 3 legibles.
- Tipografía Outfit local con jerarquía en sp.
- Una acción principal visible y detalle bajo demanda.
- Movimiento ligado al estado y desactivable por el sistema.

## Colors

La paleta combina un azul decidido con coral cálido en detalles y superficies frías para leer.

### Primary

**Azul luminoso** conduce la acción principal, selección, enlaces e iconos activos. **Azul suave** sostiene iconos y estados sin competir con el botón. **Azul noche** cumple el papel principal en oscuro.

### Secondary

**Coral legible** y **coral suave** identifican el recordatorio y su origen. El coral más luminoso de las ilustraciones pertenece a los archivos de imagen, no a un color inventado para el texto.

### Tertiary

**Lila** y su contenedor acompañan el esquema Material; su versión nocturna conserva el carácter óptico de la familia.

### Neutral

**Campo frío**, **tinta**, **superficie clara**, los contenedores y **texto secundario** estructuran fondo, lectura y separación. Sus pares nocturnos son explícitos en el tema. **Error** comunica permiso denegado o un aviso bloqueado; no depende solo del color, siempre lleva texto.

**The Legibility Rule.** La tipografía y las acciones se dibujan con colores semánticos de MaterialTheme; los reflejos pertenecen a la imagen o al tratamiento del botón.

Los valores del frontmatter son normativos y conservan unidades Android. La fuente completa de esquemas claro y oscuro está en [Theme.kt](app/src/main/java/com/leon/avisoscampus/ui/theme/Theme.kt); no se añade color dinámico del fondo de pantalla.

## Typography

**Display Font / Body Font / Label Font:** Outfit variable local, con pesos explícitos normal, medio, seminegrita y negrita. Se distribuye en res/font y con licencia SIL OFL.

La geometría redondeada de Outfit acompaña el vidrio sin restar claridad. Los títulos usan seminegrita; el texto de apoyo mantiene peso normal. Cada tamaño e interlineado exacto está en el frontmatter y corresponde a Theme.kt.

### Hierarchy

- **Display:** escala preparada por el tema; la pantalla actual no la usa para agrandar la marca.
- **Headline:** titular de tarea e historial; headline-medium también muestra los segundos restantes.
- **Title:** marca y contenido del aviso; title-large abre los grupos de Ajustes, title-medium nombra registros y title-small resume filas.
- **Body:** descripción, información de permiso, contenido del historial y aclaración de ejemplo.
- **Label:** acciones, filtros, origen y hora; la etiqueta pequeña mantiene el papel secundario.

**The Native Type Rule.** Los tamaños se expresan en sp y los textos se componen en Android; nunca se hornean en una ilustración.

## Layout

La pantalla usa un margen horizontal de 24 dp y contenido centrado con ancho máximo de 760 dp. En compacto, la navegación Material tiene tres destinos: Avisos, Historial y Ajustes. Desde 700 dp de ancho se transforma en rail. En el bloque de tarea, desde 560 dp disponibles la ilustración y la acción se distribuyen en dos columnas; por debajo, se apilan.

Inicio y Ajustes tienen desplazamiento vertical propio; Historial es una sola LazyColumn con filtros horizontales. El ritmo del inicio separa grupos con 18 dp; Ajustes usa 16 dp. Las filas del historial conservan icono, texto, origen y hora alineados; el límite se aplica después del filtro.

Los insets de Scaffold, Atrás de Android, objetivos táctiles mínimos de 48 dp y ampliación de texto son parte de la interfaz. No hay breakpoint web ni un tamaño fijo de teléfono exigido por el sistema visual.

## Elevation & Depth

La profundidad combina capas tonales Material con tres esculturas raster de vidrio: campana, reloj de arena y bandeja vacía. El botón usa gradiente vertical suave, borde blanco de alfa descendente y sombra nativa de 5 dp cuando está activo; deshabilitado, la sombra desaparece. La barra inferior conserva elevación tonal de 2 dp.

El efecto del botón se construye con Compose, sin desenfoque ni refracción del fondo. Las imágenes ya contienen sus reflejos y volumen. La app no usa API de material de Apple. La ficha de recursos y los prompts están en [diseno/RECURSOS.md](diseno/RECURSOS.md) y [diseno/IMAGENES.json](diseno/IMAGENES.json).

**The Quiet Depth Rule.** La profundidad acompaña una acción reconocible; el texto queda sobre superficies legibles.

## Shapes

El control de vidrio tiene esquinas amplias; el aviso de cuenta en curso usa una curva menor. Los contenedores de origen y escucha son circulares. Filas y separadores del historial conservan una lectura continua, sin tarjetas anidadas. Las formas de los demás controles pertenecen a Material 3; no se documenta un radio fijo que el código no haya establecido.

## Components

### Buttons

AvisosGlassButton es un Button Material con tratamiento luminoso. Tiene altura mínima de 56 dp, relleno horizontal de 24 dp y vertical de 15 dp, icono de 22 dp y separación de 10 dp. La variante primaria toma primary/onPrimary; la secundaria usa superficie/contenedor. El deshabilitado reduce alfa y mantiene texto explícito.

Al pulsar, el control se comprime a 0.975 con resorte de amortiguación 0.65 y rigidez 650; ejecuta respuesta háptica nativa. Con movimiento desactivado, el cambio usa snap. No se define hover de aplicación móvil; foco y semántica los conserva Material.

### Chips and mode selection

Ahora / En 10 s es una selección única Material y cada segmento respeta el mínimo táctil. Los FilterChip de Historial seleccionan Todos, Botón, Recordatorio, Modo avión o Cargador. El filtro no elimina datos y muestra hasta veinte resultados de ese origen.

### Rows and containers

El historial usa filas con icono circular, origen, hora, título y contenido; los recordatorios tienen fondo coral suave. Los separadores se dibujan con outlineVariant a media opacidad. El estado vacío muestra una bandeja de vidrio y un mensaje útil, incluida la opción Ver todos si hay filtro.

### Navigation

Tres destinos con icono y etiqueta. La barra inferior se transforma en rail al disponer de ancho; un destino secundario presenta Atrás para volver a Avisos. El estado del permiso está disponible en la cabecera y en Ajustes.

### Settings and permission

Las escuchas de avión y cargador tienen interruptores independientes y fila completa pulsable con rol Switch. SinPedir muestra Permitir notificaciones; Concedido o Denegado muestran una única acción Abrir ajustes. Canales abre la configuración real de Avisos o Urgentes.

### Signature: illustration and motion

La campana responde al envío con oscilación y ondas. El reloj de arena aparece al elegir recordatorio; el aro de progreso y los segundos derivan del estado real faltan. Cancelar detiene el Job. La navegación usa una transición horizontal breve y el historial anima la inserción por id.

MotionSettings observa ANIMATOR_DURATION_SCALE incluso si cambia con la app abierta. En escala cero, las transiciones se eliminan y los valores saltan al estado final. No hay bucle decorativo permanente.

## Do's and Don'ts

### Do:

- **Do** conservar Outfit local y las funciones tipográficas de Material 3.
- **Do** usar ilustraciones transparentes existentes y conservar texto y controles fuera de los reflejos.
- **Do** vincular movimiento con envío, cuenta, navegación o inserción de avisos.
- **Do** mantener objetivos táctiles de al menos 48 dp y respetar el tamaño de letra del sistema.
- **Do** identificar el contenido de práctica como ejemplo del laboratorio.

### Don't:

- **Don't** presentar este tratamiento como API de Apple o refracción dinámica del fondo.
- **Don't** colocar texto o controles esenciales dentro de imágenes rasterizadas.
- **Don't** convertir el inicio en una pila de tarjetas con todas las funciones.
- **Don't** añadir movimiento continuo o ignorar la escala cero de animaciones.
- **Don't** tratar el historial como confirmación de entrega o almacenamiento persistente.

