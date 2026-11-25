## verdict

1. resolved — Acción de permiso según estado. 02b_ajustes_sin_pedir.png muestra «Sin pedir» y una sola acción «Permitir notificaciones»; 04b_ajustes_denegado.png muestra «Denegado» y solo «Abrir ajustes»; 11_ajustes.png muestra «Permitido» y solo «Abrir ajustes». 16_oscuro_ajustes.png y 18_ajustes_130.png mantienen la corrección sin recorte ni duplicación. PantallaAvisos.kt:352–355 confirma que únicamente SinPedir solicita el permiso y los otros estados abren la configuración.

2. resolved — Identificación del contenido de práctica. 01_inicio.png, 04_permiso_denegado.png, 06_aviso_enviado.png, 08_cuenta_regresiva.png, 14_inicio_final.png, 15_oscuro.png, 17_texto_130.png, 19_horizontal.png y 20_sin_animaciones.png muestran «Aviso de ejemplo del laboratorio.» junto al contenido, sin reemplazar los textos requeridos ni desplazar la acción fuera de la primera vista. 02_historial_vacio.png, 12_historial.png y 13_filtro_cargador.png muestran «Ejemplos generados en esta sesión». 07_panel_notificaciones.png muestra la etiqueta «Ejemplo del laboratorio» en los avisos inmediatos. La captura actualizada 10_recordatorio_urgente.png muestra el recordatorio expandido con esa misma etiqueta y «Revisa tu laboratorio pendiente». La fuente coincide: PantallaAvisos.kt:266, HistorialAvisos.kt:49 y Notificador.kt:35; este último aplica setSubText al builder común.

## remaining

clear. No quedan abiertos los dos hallazgos puntuados y no se observan regresiones materiales introducidas por este lote en las vistas revisadas. Las mismas capturas obligatorias de la primera revisión y las vistas adicionales de los estados de permiso se reabrieron con view_image: son capturas válidas de 1344×2992, salvo 19_horizontal.png de 2992×1344, coherentes con su estado y sin regiones inválidas.

Este ship cubre únicamente los dos arreglos puntuados; no constituye una nueva aprobación integral de la superficie ni amplía la evidencia del emulador a hardware físico. No se realizaron cambios de código, Git, ejecución de la app ni una nueva búsqueda de mejoras.

disposition: ship
