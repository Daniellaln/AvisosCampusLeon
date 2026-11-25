# Recursos visuales de fase2

Ilustraciones originales generadas con image_gen integrada: aviso_campana.png para el aviso inmediato, aviso_reloj.png para recordatorio y aviso_historial_vacio.png para el historial vacío. Están en app/src/main/res/drawable-nodpi con transparencia real; Compose decide el tamaño. No son pantallas pintadas ni datos remotos. Los prompts completos están en IMAGENES.json y prompts/.

Logo propio azul/coral de AvisosCampus e iconos de trazo1.8, vista24, en iconos/ (SVG editable) y res/drawable (VectorDrawable que carga painterResource). ic_launcher_aviso es el icono de aplicación; ic_notificacion se conserva como silueta pequeña monocroma del sistema. No se usa logotipo institucional.

Tipografía local Outfit variable, de Google Fonts: https://github.com/google/fonts/tree/main/ofl/outfit . Se entrega en res/font/outfit.ttf con licencia SIL OFL en licencias/OFL-Outfit.txt. No hay fuentes o imágenes que dependan de internet durante el uso.

Los controles usan Material3, bordes claros, gradientes y presión con resorte. Es una implementación inspirada en vidrio; no usa la API de Apple ni promete refracción del fondo. La animación se detiene con escala0 del sistema; las imágenes no muestran un contador inventado, la cuenta es texto y progreso de Compose a partir del ViewModel.
