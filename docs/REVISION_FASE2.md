disposition: fix

No se suministró una comp de crítica renderizada. La dirección es code-led: no hay comp aprobada, build state ni diffs exigibles; el JSON de decisión y la barra de calidad textual son la autoridad disponible.

## persistence

pass. PRODUCT.md existe y corresponde a Android nativo, datos ficticios, tres destinos y avisos locales sin Firebase. FORM registra candidata 5/7 y semilla fa82d61e, corroboradas en direccion_fase2_roll.txt. El contrato y la decisión conservan el azul, coral y vidrio claro confirmados. DESIGN.md corresponde al cierre posterior de esta revisión y su ausencia actual no es una falta de persistencia.

Evidencia válida inspeccionada con view_image: 01_inicio.png, 02_historial_vacio.png, 04_permiso_denegado.png, 08_cuenta_regresiva.png, 11_ajustes.png, 12_historial.png, 13_filtro_cargador.png, 14_inicio_final.png, 15_oscuro.png, 16_oscuro_ajustes.png, 17_texto_130.png, 18_ajustes_130.png, 19_horizontal.png y 20_sin_animaciones.png. También se inspeccionaron 07_panel_notificaciones.png, 10_recordatorio_urgente.png y la versión actualizada de 21_canales_permitidos.png con Avisos y Urgentes expandidos. Son vistas completas coherentes con su nombre: 1344×2992 en vertical y 2992×1344 en horizontal, sin regiones negras o vacías indebidas. Proceden del Pixel 8 Pro emulado, API 37, Windows; no prueban hardware físico ni tabletas. No se ejecutó detector: el control de convenciones y piso visual se hizo como Android nativo.

Los tres rasters visibles tienen procedencia y prompts en diseno/IMAGENES.json; RECURSOS.md identifica los originales, los SVG editables y Outfit local con su licencia. Se muestrearon PantallaAvisos.kt, HistorialAvisos.kt, GlassControls.kt y Theme.kt sin ejecutar ni modificar la app.

## fidelity

| Elemento / promesa | Estado | Evidencia y juicio |
|---|---|---|
| TYPE | match | Outfit local mapeada a roles Material, tamaño en sp y jerarquía geométrica legible. La ampliación 1.3 de 17/18 mantiene textos y acciones sin recorte visible. |
| MATERIAL | match | Campana, reloj y bandeja son imágenes dimensionales reales y visibles, con vidrio óptico azul, acento coral y alfa; no sustituciones vectoriales de ilustraciones. |
| Controles del mundo de vidrio | adaptation | El botón azul conserva reflejo leve y borde luminoso, sin simular refracción del fondo. Es un Button Material con contraste legible; lo justifican PRODUCT y FIRST VIEWPORT, que exigen controles claros y nativos en modo operate. No es una promesa de material Apple. |
| GROUND | match | Campo frío medido #F6F8FF en 01/02/04/08/11/12/13/14/17/18/19/20; coincide con la decisión. El esquema oscuro usa #0D1530 y mantiene contraste de texto e imagen, conforme a la accesibilidad exigida. |
| THESIS | match | Un foco central y una acción de aviso; Historial y Ajustes retiran los controles secundarios del inicio. No vuelve la pila de cinco tarjetas. |
| OWN-WORLD | match | Azul luminoso, coral contenido, blancos fríos, texto tinta y familia de esculturas de vidrio coherentes. Las superficies dejan el texto fuera de los reflejos. |
| STORY | match | Ahora / En 10 s, botón, cuenta real con Cancelar, último aviso e historial filtrable están presentes; 13 confirma el filtro Cargador. |
| FIRST VIEWPORT | match | Marca y permiso arriba, selector, campana óptica y acción visibles; el último aviso aparece cuando existe, como corresponde a la verdad del producto. |
| FORM | match | Galería de esculturas ópticas, candidata 5/7 y semilla comprobada; se traduce a Material 3 en vez de copiar estructura web. |
| SIGNATURE | match | GlassControls liga campana/ondas a pulso y progreso a faltan; PantallaAvisos usa cambio de sección con eje horizontal; Historial anima inserciones. MotionSettings observa escala del sistema y usa cortes/snap al llegar a cero. 08 y 20 corroboran los estados estáticos; no se afirma haber medido fluidez con estas capturas. |
| Navegación y tamaños | adaptation | Barra Material con tres destinos en compacto; rail y composición en dos columnas en 19. La adaptación deriva de Android y mantiene la tarea visible. Atrás y controles semánticos están implementados; los controles interactivos usan mínimos de 48 dp. |
| Estado de permiso en Ajustes | contradicted | 11/16/18 muestran «Permitido» junto al botón principal activo «Permitir notificaciones». PantallaAvisos.kt:350 lo dibuja sin condición para los tres estados: se ofrece una acción que ya no corresponde. |
| Veracidad de los avisos de demostración | missing | 01 presenta «Tarea lista / Tu entrega fue registrada» aun antes del primer aviso. El panel de Android repite ese contenido. PRODUCT los declara ficticios, pero ninguna vista inspeccionada informa al usuario de que es un aviso de práctica; la frase parece confirmar una entrega real. |

## ceiling

reached respecto a la barra proporcionada: la familia óptica ocupa el foco sin tapar los controles, la jerarquía y la navegación son Android, la cuenta tiene contenido real y el movimiento está ligado a eventos y se puede desactivar. No falta un recurso visual que justifique producir nuevas imágenes o reconstruir la composición. El límite actual es la claridad de estados y la identificación del contenido de práctica, no la cantidad de efectos.

## material_fixes

1. STORY / estados, PantallaAvisos.kt:350: hacer condicional la acción de permiso en Ajustes. SinPedir ofrece «Permitir notificaciones»; Denegado ofrece abrir la configuración de notificaciones; Concedido conserva «Permitido» como estado y una única acción útil «Gestionar notificaciones». Eliminar el paso redundante que solicita un permiso ya resuelto. Recapturar Ajustes con permiso concedido y denegado, incluyendo la variante 1.3.
2. Truth / datos ficticios: añadir una frase visible y breve que identifique los avisos como ejemplos del laboratorio, cerca de su contenido en el inicio, y la misma identificación en el texto expandido de la notificación de práctica. Conservar los textos exigidos «Tarea lista / Tu entrega fue registrada» como contenido del ejemplo, sin presentarlos como estado real de una entrega. Recapturar inicio y panel expandido; no añadir otro encabezado decorativo ni una tarjeta.

## keep

Conservar las esculturas ópticas actuales, Outfit, el campo frío exacto, azul/coral, una acción principal, navegación Material de tres destinos, filtro por origen desde la base, cuenta cancelable y respeto de escala cero; aplicar los dos arreglos en un solo lote.
