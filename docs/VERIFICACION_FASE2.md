# Verificación de fase 2

La compilación y las pruebas se repitieron después del lote final que corrigió la acción de permiso en Ajustes y la identificación del contenido de práctica.

| Comprobación | Resultado |
|---|---|
| Pruebas JVM | 23 aprobadas |
| Pruebas Android | 14 aprobadas |
| Total | **37 aprobadas**, cero fallos, errores u omitidas |
| Compilación debug | BUILD SUCCESSFUL |
| Lint | 12 advertencias, cero errores |
| Entorno | Windows, Pixel 8 Pro emulado, Android API 37 |

Los XML, lint, resumen y registro finales se conservan en [pruebas_fase2](pruebas_fase2/). El [registro de compilación final](pruebas_fase2/fase2_verificacion_final.log) informa BUILD SUCCESSFUL in 38s. Los XML JVM suman 2 pruebas de Repository, 15 de ViewModel y 6 de funciones de fase 2; el XML Android declara 14.

## Alcance de las pruebas

Las pruebas cubren registro en memoria, ids, filtro por origen, cuenta y cancelación, eventos, permiso, receptores y mejoras de fase 2. Comprueban los canales Avisos y Urgentes, su importancia, selección por origen, contenido expandido largo y PendingIntent inmutable. La invocación de un receptor en una prueba no se presenta como un cambio real del sistema.

El recorrido final complementa esas pruebas con la bandeja de Android, permiso denegado y concedido, tocar y retirar una notificación, cuenta durante rotación y minimizado, escucha real de modo avión y cargador, interruptores desactivados y filtro. Su resultado se registra por comprobación en [recorrido_fase2_resultado.json](pruebas_fase2/recorrido_fase2_resultado.json); las vistas están en [la galería](GALERIA_FASE2.md). El JSON final registra trece comprobaciones satisfactorias, incluidos dos ids distintos y las escuchas reales con sus interruptores. El detalle permite distinguir estos resultados de las pruebas automatizadas.

La captura se completó en dos tramos; la reanudación verificó el interruptor del cargador y su condición inicial. Los resultados se escribieron tras cada aserción, conservando ambos registros: [tramo inicial](pruebas_fase2/recorrido_fase2_final.log) y [cierre](pruebas_fase2/recorrido_fase2_cierre.log).

## Revisión visual acotada

El [veredicto final acotado](VEREDICTO_FASE2.md) revisa los dos ajustes señalados: SinPedir solicita permiso y Concedido/Denegado abren Ajustes con un solo botón; inicio y notificación identifican el ejemplo del laboratorio. La revisión de capturas no equivale a medir fluidez ni valida dispositivos físicos. También se documentan modo oscuro, texto a 1.3, horizontal y escala cero de animación.

El diseño y su implementación están descritos en [DESIGN.md](../DESIGN.md) y [GUIA_CODIGO_FASE2.md](GUIA_CODIGO_FASE2.md). La procedencia de los tres rasters y la licencia local de Outfit están en [diseno](../diseno/RECURSOS.md).

## Evidencia histórica y límites

Las 29 pruebas de fase 1, sus doce capturas y su recorrido permanecen separados en [VERIFICACION.md](VERIFICACION.md), [pruebas](pruebas/) y [capturas](capturas/). No se atribuyen las pruebas finales de fase 2 a los commits anteriores.

Historial e ids viven en el proceso. El recordatorio usa viewModelScope; no garantiza entrega tras muerte del proceso, cierre forzado, reinicio o suspensión prolongada. El historial registra avisos generados y puede incluir avisos bloqueados por Android. No se afirma validación en teléfono físico ni Mac.



