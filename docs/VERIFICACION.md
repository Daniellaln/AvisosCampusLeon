# Verificación de fase 1

17 pruebas JVM y 12 Android: **29 aprobadas**, cero fallos, errores u omitidas. Lint: {'Warning': 9}. Evidencia ejecutada sobre Windows y emulador Pixel 8 Pro API 37; no teléfono físico ni Mac.

**JVM:** tiempo virtual, duplicados, cancelación, SharedFlow, permiso, Repository y selección del filtro.

**Android:** tarjetas, cuenta, interruptor, filtrado antes del límite de veinte, canal, ids distintos, contenido de notificación, PendingIntent inmutable y callback del receptor.

## Recorrido real

- canalAvisos: True
- denegadoSinCierreConHistorial: True
- abrirAjustes: True
- permisoConcedidoLimpiaMensaje: True
- dosIdsDistintos: ['3', '2']
- tocarAbreYAutoCancela: True
- cancelarNoNotifica: True
- recordatorioMinimizadoYRotacion: True
- modoAvionOnOff: True
- filtroOrigen: True
- switchApagadoNoNotifica: True

Los doce PNG y el registro del recorrido documentan el panel real, permiso, minimizado y modo avión; las pruebas del receptor por invocación directa no se confunden con los cambios reales del sistema. El resumen XML, logs y lint están en [pruebas](pruebas/). No hay FATAL EXCEPTION en el logcat de aceptación. La cuenta y la memoria dependen de que el proceso siga vivo.
