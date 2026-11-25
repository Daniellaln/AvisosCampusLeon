# Código de cada commit en main

En Android Studio abre Git → Log, selecciona el commit y abre su diff. Estos enlaces de GitHub apuntan al código de ese avance, no a las líneas cambiadas de la versión final. Cada carpeta de entrega contiene además su proyecto completo.

## 1. Preparé la base de AvisosCampus con las tarjetas de la guía y la estructura MVVM

[app/build.gradle.kts:3](https://github.com/Daniellaln/AvisosCampusLeon/blob/fcd00268045eff964824cfc45dc6576fbdb88d99/app/build.gradle.kts#L3)

## 2. Creé el canal Avisos al iniciar la aplicación y lo registré en el Manifest

[app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt:15](https://github.com/Daniellaln/AvisosCampusLeon/blob/8d5a1d1802fc15037e09f4e2b7fd310f1f780a4a/app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt#L15)

## 3. Añadí la solicitud de POST_NOTIFICATIONS y los estados Sin pedir, Permitido y Denegado

[app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt:16](https://github.com/Daniellaln/AvisosCampusLeon/blob/7619bd5f7ac5a5a31713635f4e4643ebf0b10d1a/app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt#L16)

## 4. Conecté Mostrar aviso con el historial y las notificaciones locales con identificadores distintos

[app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt:23](https://github.com/Daniellaln/AvisosCampusLeon/blob/df548fb79aca92850990804fb4abcb879b7b16ec/app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt#L23)

## 5. Hice que tocar una notificación abra la app usando un PendingIntent inmutable

[app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt:30](https://github.com/Daniellaln/AvisosCampusLeon/blob/ef432325af87a65b4da887d299e2088b52df88af/app/src/main/java/com/leon/avisoscampus/notificacion/Notificador.kt#L30)

## 6. Programé el recordatorio de diez segundos con cuenta regresiva, cancelación y protección contra duplicados

[app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt:26](https://github.com/Daniellaln/AvisosCampusLeon/blob/f2965ec66786d496a2a4de3ce21b442ff00ca726/app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt#L26)

## 7. Añadí la escucha del modo avión con un receptor que se registra y se retira al salir

[app/src/main/java/com/leon/avisoscampus/receiver/ModoAvionReceiver.kt:6](https://github.com/Daniellaln/AvisosCampusLeon/blob/60136005c9269f380f735cd2fe69881d5245db2e/app/src/main/java/com/leon/avisoscampus/receiver/ModoAvionReceiver.kt#L6)

## 8. Mostré los veinte avisos más recientes con su origen, texto y hora en el historial

[app/src/main/java/com/leon/avisoscampus/ui/HistorialAvisos.kt:9](https://github.com/Daniellaln/AvisosCampusLeon/blob/a0740da4b3cc5e8b235e2bc9616ba4aad159dacf/app/src/main/java/com/leon/avisoscampus/ui/HistorialAvisos.kt#L9)

## 9. Conservé el historial al denegar notificaciones y añadí el aviso de error y el acceso a Ajustes

[app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt:49](https://github.com/Daniellaln/AvisosCampusLeon/blob/b4b5cf8aca9869864d2979552a478d680d2654c9/app/src/main/java/com/leon/avisoscampus/viewmodel/AvisosViewModel.kt#L49)

## 10. Añadí como función propia el filtro del historial por origen sin borrar los avisos

[app/src/main/java/com/leon/avisoscampus/ui/HistorialAvisos.kt:15](https://github.com/Daniellaln/AvisosCampusLeon/blob/8d4ebd82e8b8558a410b9b92edcfe452ca80570f/app/src/main/java/com/leon/avisoscampus/ui/HistorialAvisos.kt#L15)

## 11. Pruebas y documentación final

El último avance comprueba la aceptación, añade la espera de recolector para eventos durante la recreación, la alternativa de Ajustes para Android 7 y documenta el resultado. Ver [guía final de rutas y líneas](GUIA_CODIGO.md), [pruebas](VERIFICACION.md) y README. Se localiza en Git Log por el mensaje del cierre; no se escribe un SHA circular dentro del propio commit.
