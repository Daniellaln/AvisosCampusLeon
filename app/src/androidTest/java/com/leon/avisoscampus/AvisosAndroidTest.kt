package com.leon.avisoscampus

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.SystemClock
import android.service.notification.StatusBarNotification
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.leon.avisoscampus.data.AvisosRepositoryMemoria
import com.leon.avisoscampus.model.Aviso
import com.leon.avisoscampus.model.Origen
import com.leon.avisoscampus.notificacion.Notificador
import com.leon.avisoscampus.receiver.CargadorReceiver
import com.leon.avisoscampus.receiver.ModoAvionReceiver
import com.leon.avisoscampus.ui.HistorialAvisos
import com.leon.avisoscampus.ui.PantallaAvisos
import com.leon.avisoscampus.ui.theme.AvisosCampusTheme
import com.leon.avisoscampus.viewmodel.AvisosViewModel
import com.leon.avisoscampus.viewmodel.AvisosViewModelFactory
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Sustituye el archivo de instrumentación de fase 1 al cerrar el nuevo diseño.
 * Las pruebas usan la aplicación y los controles reales de Compose.
 * No se usa reloj virtual: la duración exacta de los diez segundos se cubre en
 * JVM y la recepción minimizada, rotación y toque real se capturan aparte.
 */
@RunWith(AndroidJUnit4::class)
class AvisosPantallaAndroidTest {
    @get:Rule val compose = createComposeRule()
    private val repositorio = AvisosRepositoryMemoria()
    private lateinit var almacen: ViewModelStore
    private lateinit var vm: AvisosViewModel

    @Before fun prepararModeloPropio() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            almacen = ViewModelStore()
            vm = ViewModelProvider(almacen, AvisosViewModelFactory(repositorio))[
                AvisosViewModel::class.java
            ]
        }
    }

    @After fun cancelarTrabajoYRetirarSoloAvisosDeEstaApp() {
        compose.runOnIdle { almacen.clear() }
        requireNotNull(ApplicationProvider.getApplicationContext<Context>()
            .getSystemService(NotificationManager::class.java)).cancelAll()
    }

    private fun mostrarPantalla() {
        compose.setContent { AvisosCampusTheme { PantallaAvisos(vm) } }
        compose.waitForIdle()
    }

    @Test fun inicioMuestraUnaTareaYLosAjustesQuedanEnSuDestino() {
        mostrarPantalla()
        compose.onNodeWithText("Mostrar aviso").performScrollTo().assertIsDisplayed().assertIsEnabled()
        compose.onNodeWithContentDescription("Ir a Avisos").assertIsSelected()
        compose.onNodeWithContentDescription("Ir a Historial").assertIsDisplayed()
        compose.onNodeWithContentDescription("Ir a Ajustes").performClick()
        if (Notificador.tienePermiso(ApplicationProvider.getApplicationContext())) {
            compose.onNodeWithText("Permitir notificaciones").assertDoesNotExist()
            compose.onNodeWithText("Abrir ajustes").assertExists()
        } else {
            compose.onNodeWithText("Permitir notificaciones").assertExists()
        }
        compose.onNodeWithContentDescription("Escuchar el modo avión").assertExists()
        compose.onNodeWithContentDescription("Escuchar el cargador").assertExists()
        compose.onNodeWithContentDescription("Ir a Ajustes").assertIsSelected()
        Espresso.pressBack()
        compose.onNodeWithContentDescription("Ir a Avisos").assertIsSelected()
        compose.onNodeWithText("Mostrar aviso").performScrollTo().assertIsDisplayed()
    }

    @Test fun dosAvisosSeConservanAlAbrirElHistorial() {
        mostrarPantalla()
        repeat(2) { indice ->
            compose.onNodeWithText("Mostrar aviso").performScrollTo().performClick()
            compose.waitUntil(3_000) { repositorio.avisos.value.size == indice + 1 }
        }
        compose.onNodeWithContentDescription("Ir a Historial").performClick()
        compose.onAllNodesWithText("Tarea lista").assertCountEquals(2)
        compose.onAllNodesWithText("Tu entrega fue registrada").assertCountEquals(2)
        assertEquals(listOf(2, 1), repositorio.avisos.value.map { it.id })
        assertTrue(repositorio.avisos.value.all { it.origen == Origen.BOTON })
        compose.onNodeWithContentDescription("Ir a Avisos").performClick()
        assertEquals(2, repositorio.avisos.value.size)
    }

    @Test fun elegirDiezSegundosIniciaLaCuentaYCancelarNoGeneraUnAviso() {
        mostrarPantalla()
        compose.onNodeWithText("En 10 s").performClick()
        compose.onNodeWithText("Recordar en 10 s").performScrollTo().performClick()
        compose.waitUntil(3_000) { vm.faltan.value != null }
        compose.onNodeWithText("Faltan ", substring = true).assertExists()
        assertTrue(vm.faltan.value?.let { it in 1..10 } == true)
        compose.onNodeWithText("Cancelar").performScrollTo().assertIsEnabled().performClick()
        compose.waitUntil(3_000) { vm.faltan.value == null }
        compose.onNodeWithText("Recordar en 10 s").assertIsEnabled()
        assertTrue(repositorio.avisos.value.isEmpty())
    }

    @Test fun escuchasDeAvionYCargadorSonIndependientesYNoSePierdenAlNavegar() {
        mostrarPantalla()
        compose.onNodeWithContentDescription("Ir a Ajustes").performClick()
        val avion = compose.onNodeWithContentDescription("Escuchar el modo avión")
        val cargador = compose.onNodeWithContentDescription("Escuchar el cargador")
        avion.performScrollTo().assertIsOff().performClick().assertIsOn()
        cargador.performScrollTo().assertIsOff().performClick().assertIsOn()
        compose.runOnIdle {
            assertTrue(vm.escuchandoAvion.value)
            assertTrue(vm.escuchandoCargador.value)
        }
        avion.performScrollTo().performClick().assertIsOff()
        compose.runOnIdle {
            assertFalse(vm.escuchandoAvion.value)
            assertTrue(vm.escuchandoCargador.value)
        }
        compose.onNodeWithContentDescription("Ir a Historial").performClick()
        compose.onNodeWithContentDescription("Ir a Ajustes").performClick()
        cargador.performScrollTo().assertIsOn().performClick().assertIsOff()
        assertTrue(repositorio.avisos.value.isEmpty())
    }

    @Test fun filtroPropioIncluyeCargadorYTodosRestauraElConjunto() {
        val avisos = listOf(
            Aviso(4, "Entrega registrada", "Contenido del botón", Origen.BOTON, "10:00:04"),
            Aviso(3, "Pendiente de mañana", "Contenido del reloj", Origen.RECORDATORIO, "10:00:03"),
            Aviso(2, "Conectividad cambiada", "Contenido del avión", Origen.MODO_AVION, "10:00:02"),
            Aviso(1, "Energía conectada", "Contenido del cable", Origen.CARGADOR, "10:00:01")
        )
        compose.setContent {
            AvisosCampusTheme {
                var filtro by remember { mutableStateOf<Origen?>(null) }
                HistorialAvisos(avisos, filtro) { filtro = it }
            }
        }
        avisos.forEach { elegido ->
            compose.onNode(hasText(elegido.origen.texto) and hasClickAction())
                .performScrollTo().performClick().assertIsSelected()
            compose.onNodeWithText(elegido.titulo).assertExists()
            avisos.filter { it.id != elegido.id }.forEach { otro ->
                compose.onNodeWithText(otro.titulo).assertDoesNotExist()
            }
        }
        compose.onNode(hasText("Todos") and hasClickAction())
            .performScrollTo().performClick().assertIsSelected()
        avisos.forEach { elegido ->
            compose.onNodeWithTag("lista_historial")
                .performScrollToNode(hasText(elegido.titulo))
            compose.onNodeWithText(elegido.titulo).assertIsDisplayed()
        }
    }

    @Test fun filtroSinCoincidenciasNoPresentaAvisosDeOtroOrigen() {
        compose.setContent {
            AvisosCampusTheme {
                HistorialAvisos(
                    listOf(Aviso(1, "Solo botón", "Guardado", Origen.BOTON, "12:00:00")),
                    filtro = Origen.RECORDATORIO
                )
            }
        }
        compose.onNodeWithText("Sin avisos de este origen").assertIsDisplayed()
        compose.onNodeWithText("Sin avisos todavía").assertDoesNotExist()
        compose.onNodeWithText("Solo botón").assertDoesNotExist()
    }

    @Test fun limiteDeVeinteSeAplicaDespuesDelFiltroEnLaListaDesplazable() {
        val avisos = (0 until 50).map { indice ->
            Aviso(indice + 1, "Registro $indice", "Texto $indice",
                if (indice < 25) Origen.BOTON else Origen.RECORDATORIO, "12:00:00")
        }
        compose.setContent {
            AvisosCampusTheme { HistorialAvisos(avisos, filtro = Origen.RECORDATORIO) }
        }
        compose.onNodeWithTag("lista_historial").performScrollToNode(hasText("Registro 25"))
        compose.onNodeWithText("Registro 25").assertIsDisplayed()
        compose.onNodeWithTag("lista_historial").performScrollToNode(hasText("Registro 44"))
        compose.onNodeWithText("Registro 44").assertIsDisplayed()
        // El buscador recorre la lista hasta el final. Si la fila 21 estuviera
        // dentro de ella, encontraría Registro 45 y esta aserción fallaría.
        assertThrows(AssertionError::class.java) {
            compose.onNodeWithTag("lista_historial")
                .performScrollToNode(hasText("Registro 45"))
        }
        compose.onNodeWithText("Registro 0").assertDoesNotExist()
    }
}

/**
 * Pruebas reales de NotificationManager en API33+. El permiso se concede solo
 * a esta app; no se modifica la importancia de los canales del usuario.
 */
@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 33)
class AvisosNotificacionesAndroidTest {
    @get:Rule val permiso: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.POST_NOTIFICATIONS
    )
    private val contexto: Context = ApplicationProvider.getApplicationContext()
    private val gestor = requireNotNull(contexto.getSystemService(NotificationManager::class.java))

    @Before fun prepararCanalesYLimpiarSoloEstePaquete() {
        gestor.cancelAll()
        Notificador.crearCanal(contexto)
    }
    @After fun retirarLasNotificacionesDePrueba() { gestor.cancelAll() }

    private fun esperarNotificaciones(ids: Set<Int>): List<StatusBarNotification> {
        val limite = SystemClock.uptimeMillis() + 5_000
        var actuales: List<StatusBarNotification>
        do {
            actuales = gestor.activeNotifications.filter {
                it.packageName == contexto.packageName && it.id in ids
            }
            if (actuales.map { it.id }.toSet() == ids) return actuales
            SystemClock.sleep(25)
        } while (SystemClock.uptimeMillis() < limite)
        assertEquals("No se publicaron todos los IDs esperados", ids,
            actuales.map { it.id }.toSet())
        return actuales
    }

    @Test fun crearCanalesDosVecesConservaAvisosDefaultYUrgentesHigh() {
        Notificador.crearCanal(contexto)
        val avisos = requireNotNull(gestor.getNotificationChannel(Notificador.CANAL))
        val urgentes = requireNotNull(gestor.getNotificationChannel(Notificador.CANAL_URGENTE))
        assertEquals("avisos", avisos.id)
        assertEquals("Avisos", avisos.name.toString())
        assertEquals(NotificationManager.IMPORTANCE_DEFAULT, avisos.importance)
        assertEquals("urgentes", urgentes.id)
        assertEquals("Urgentes", urgentes.name.toString())
        assertEquals(NotificationManager.IMPORTANCE_HIGH, urgentes.importance)
        assertEquals(1, gestor.notificationChannels.count { it.id == avisos.id })
        assertEquals(1, gestor.notificationChannels.count { it.id == urgentes.id })
    }

    @Test fun permisoConcedidoCoincideConElPermisoRealDeAndroid() {
        assertEquals(PackageManager.PERMISSION_GRANTED,
            ContextCompat.checkSelfPermission(contexto, Manifest.permission.POST_NOTIFICATIONS))
        assertTrue(Notificador.tienePermiso(contexto))
    }

    @Test fun tresIdsUsanElCanalCorrectoYConservanContenidoIconoEIntentInmutable() {
        val avisos = listOf(
            Aviso(15_001, "Primera prueba", "Texto uno", Origen.BOTON, "09:00:01"),
            Aviso(15_002, "Segunda prueba", "Texto dos", Origen.RECORDATORIO, "09:00:02"),
            Aviso(15_003, "Tercera prueba", "Texto tres", Origen.CARGADOR, "09:00:03")
        )
        avisos.forEach { assertTrue(Notificador.mostrar(contexto, it)) }
        val publicadas = esperarNotificaciones(avisos.map { it.id }.toSet())
        avisos.forEach { aviso ->
            val notificacion = publicadas.single { it.id == aviso.id }.notification
            val urgente = aviso.origen == Origen.RECORDATORIO
            assertEquals(aviso.titulo, notificacion.extras.getString(Notification.EXTRA_TITLE))
            assertEquals(aviso.texto, notificacion.extras.getString(Notification.EXTRA_TEXT))
            assertEquals("Ejemplo del laboratorio", notificacion.extras.getCharSequence(Notification.EXTRA_SUB_TEXT)?.toString())
            assertEquals(if (urgente) Notificador.CANAL_URGENTE else Notificador.CANAL,
                notificacion.channelId)
            assertEquals(if (urgente) Notification.PRIORITY_HIGH else Notification.PRIORITY_DEFAULT,
                notificacion.priority)
            assertEquals(R.drawable.ic_notificacion, requireNotNull(notificacion.smallIcon).resId)
            assertTrue(notificacion.flags and Notification.FLAG_AUTO_CANCEL != 0)
            assertNotNull(notificacion.contentIntent)
            assertTrue(notificacion.contentIntent.isActivity)
            assertTrue(notificacion.contentIntent.isImmutable)
            assertEquals(contexto.packageName, notificacion.contentIntent.creatorPackage)
        }
    }

    @Test fun textoExpandidoConservaTodoElMensajeLargoYSusSaltosDeLinea() {
        val largo = (1..12).joinToString("\n") {
            "Línea $it: contenido ficticio de laboratorio que debe conservarse completo."
        }
        val aviso = Aviso(15_004, "Texto expandido", largo, Origen.BOTON, "09:00:04")
        assertTrue(Notificador.mostrar(contexto, aviso))
        val notificacion = esperarNotificaciones(setOf(aviso.id)).single().notification
        assertEquals(largo, notificacion.extras.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString())
        assertEquals(largo, notificacion.extras.getCharSequence(Notification.EXTRA_TEXT)?.toString())
        assertTrue(largo.length > 600)
    }

    @Test fun pendingIntentDeLaNotificacionAbreMainActivity() {
        val instrumentacion = InstrumentationRegistry.getInstrumentation()
        val host = Intent(contexto, ComponentActivity::class.java).addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        ActivityScenario.launch<ComponentActivity>(host).use {
            val monitor = instrumentacion.addMonitor(MainActivity::class.java.name, null, false)
            try {
                val aviso = Aviso(15_005, "Abrir campus", "Prueba del destino", Origen.BOTON, "09:00:05")
                assertTrue(Notificador.mostrar(contexto, aviso))
                esperarNotificaciones(setOf(aviso.id)).single().notification.contentIntent.send()
                val actividad = instrumentacion.waitForMonitorWithTimeout(monitor, 5_000)
                assertNotNull("El PendingIntent debe abrir MainActivity", actividad)
                if (actividad != null) {
                    try { assertEquals(MainActivity::class.java, actividad.javaClass) }
                    finally { instrumentacion.runOnMainSync { actividad.finish() } }
                }
            } finally { instrumentacion.removeMonitor(monitor) }
        }
        // send() acredita el destino. AutoCancel exige tocar la bandeja real;
        // ese recorrido se verifica aparte en aceptación y capturas.
    }
}

@RunWith(AndroidJUnit4::class)
class ReceptoresAndroidTest {
    @Test fun avionAceptaSoloSuAccionYLeeLosDosEstados() {
        val recibidos = mutableListOf<Boolean>()
        val receptor = ModoAvionReceiver { recibidos.add(it) }
        val contexto: Context = ApplicationProvider.getApplicationContext()
        receptor.onReceive(contexto,
            Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED).putExtra("state", true))
        receptor.onReceive(contexto,
            Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED).putExtra("state", false))
        receptor.onReceive(contexto, Intent(Intent.ACTION_POWER_CONNECTED))
        receptor.onReceive(contexto, null)
        assertEquals(listOf(true, false), recibidos)
    }

    @Test fun cargadorAceptaConectarYDesconectarEIgnoraAvionYOtrasAcciones() {
        val recibidos = mutableListOf<Boolean>()
        val receptor = CargadorReceiver { recibidos.add(it) }
        val contexto: Context = ApplicationProvider.getApplicationContext()
        receptor.onReceive(contexto, Intent(Intent.ACTION_POWER_CONNECTED))
        receptor.onReceive(contexto, Intent(Intent.ACTION_POWER_DISCONNECTED))
        receptor.onReceive(contexto,
            Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED).putExtra("state", true))
        receptor.onReceive(contexto, Intent("com.leon.avisoscampus.ACCION_AJENA"))
        receptor.onReceive(contexto, null)
        assertEquals(listOf(true, false), recibidos)
        // Invocación directa: no pretende sustituir la conexión y desconexión
        // reales probadas con el emulador ni demostrar registro/desregistro.
    }
}
