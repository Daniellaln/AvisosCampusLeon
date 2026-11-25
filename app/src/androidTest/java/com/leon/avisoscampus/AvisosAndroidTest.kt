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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.leon.avisoscampus.data.AvisosRepositoryMemoria
import com.leon.avisoscampus.model.Aviso
import com.leon.avisoscampus.model.Origen
import com.leon.avisoscampus.notificacion.Notificador
import com.leon.avisoscampus.receiver.ModoAvionReceiver
import com.leon.avisoscampus.ui.HistorialAvisos
import com.leon.avisoscampus.ui.PantallaAvisos
import com.leon.avisoscampus.ui.theme.AvisosCampusTheme
import com.leon.avisoscampus.viewmodel.AvisosViewModel
import com.leon.avisoscampus.viewmodel.AvisosViewModelFactory
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Incorporar después de generar la etapa 9:
 * app/src/androidTest/java/com/leon/avisoscampus/AvisosAndroidTest.kt
 *
 * Usa producción real, Material 3 y el host de ui-test-manifest. No sustituye
 * Dispatchers.Main ni espera diez segundos reales: la precisión del reloj está
 * cubierta por las pruebas JVM, y aquí se verifica el control desde la pantalla.
 */
@RunWith(AndroidJUnit4::class)
class AvisosPantallaAndroidTest {
    @get:Rule
    val compose = createComposeRule()

    private val repositorio = AvisosRepositoryMemoria()
    private lateinit var almacen: ViewModelStore
    private lateinit var vm: AvisosViewModel

    @Before
    fun prepararModeloPropio() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            almacen = ViewModelStore()
            vm = ViewModelProvider(almacen, AvisosViewModelFactory(repositorio))[
                AvisosViewModel::class.java
            ]
        }
    }

    @After
    fun cancelarTrabajoYNotificacionesDeEstaApp() {
        // El almacén se limpia también si falla una aserción durante la cuenta.
        compose.runOnIdle { almacen.clear() }
        requireNotNull(ApplicationProvider.getApplicationContext<Context>()
            .getSystemService(NotificationManager::class.java)).cancelAll()
    }

    private fun mostrarPantalla() {
        compose.setContent { AvisosCampusTheme { PantallaAvisos(vm) } }
        compose.waitForIdle()
    }

    @Test
    fun cincoTarjetasEnOrdenYSePuedeLlegarAlFinalYVolver() {
        mostrarPantalla()

        val titulos = compose.onAllNodes(
            SemanticsMatcher.keyIsDefined(SemanticsProperties.Heading)
        ).fetchSemanticsNodes().map { nodo ->
            nodo.config[SemanticsProperties.Text].joinToString("") { it.text }
        }
        assertEquals(
            listOf("Permiso de notificaciones", "Avisos", "Recordatorio",
                "Receptor de anuncios", "Historial"),
            titulos
        )
        compose.onNodeWithText("Permitir notificaciones").assertIsEnabled()
        compose.onNodeWithText("Historial").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Sin avisos todavía").assertExists()
        compose.onNodeWithText("Permiso de notificaciones")
            .performScrollTo().assertIsDisplayed()
    }

    @Test
    fun dosPulsacionesGuardanDosAvisosYLosMuestranEnElHistorial() {
        mostrarPantalla()
        repeat(2) { indice ->
            compose.onNodeWithText("Mostrar aviso").performScrollTo().performClick()
            compose.waitUntil(3_000) { repositorio.avisos.value.size == indice + 1 }
        }

        compose.onNodeWithText("Historial").performScrollTo().assertIsDisplayed()
        compose.onAllNodesWithText("Tarea lista").assertCountEquals(2)
        compose.onAllNodesWithText("Tu entrega fue registrada").assertCountEquals(2)
        assertEquals(listOf(2, 1), repositorio.avisos.value.map { it.id })
        assertTrue(repositorio.avisos.value.all { it.origen == Origen.BOTON })
    }

    @Test
    fun recordatorioActivaLaCuentaYCancelarRestauraLosControles() {
        mostrarPantalla()
        compose.onNodeWithText("Cancelar").performScrollTo().assertIsNotEnabled()
        compose.onNodeWithText("Recordar en 10 s").performClick()
        compose.waitUntil(3_000) { vm.faltan.value != null }

        compose.onNodeWithText("Recordar en 10 s").assertIsNotEnabled()
        compose.onNodeWithText("Faltan ", substring = true).assertExists()
        assertTrue(vm.faltan.value?.let { it in 1..10 } == true)
        compose.onNodeWithText("Cancelar").assertIsEnabled().performClick()
        compose.waitUntil(3_000) { vm.faltan.value == null }

        compose.onNodeWithText("Faltan ", substring = true).assertDoesNotExist()
        compose.onNodeWithText("Recordar en 10 s").assertIsEnabled()
        compose.onNodeWithText("Cancelar").assertIsNotEnabled()
        assertTrue(repositorio.avisos.value.isEmpty())
    }

    @Test
    fun interruptorActivaYDesactivaLaEscuchaEnElModelo() {
        mostrarPantalla()
        val interruptor = compose.onNodeWithContentDescription("Escuchar el modo avión")
        interruptor.performScrollTo().assertIsOff().performClick()
        interruptor.assertIsOn()
        compose.runOnIdle { assertTrue(vm.escuchandoAvion.value) }
        interruptor.performClick().assertIsOff()
        compose.runOnIdle { assertFalse(vm.escuchandoAvion.value) }
    }

    @Test
    fun filtrosMuestranSoloSuOrigenYTodosRestauraLasFilas() {
        val avisos = listOf(
            Aviso(3, "Entrega registrada", "Contenido del botón", Origen.BOTON, "10:00:03"),
            Aviso(2, "Pendiente de mañana", "Contenido del reloj", Origen.RECORDATORIO, "10:00:02"),
            Aviso(1, "Conectividad cambiada", "Contenido del receptor", Origen.MODO_AVION, "10:00:01")
        )
        compose.setContent {
            AvisosCampusTheme {
                var filtro by remember { mutableStateOf<Origen?>(null) }
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    HistorialAvisos(avisos, filtro) { filtro = it }
                }
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
        avisos.forEach { compose.onNodeWithText(it.titulo).assertExists() }
    }

    @Test
    fun filtroSinCoincidenciasExplicaPorQueNoHayFilas() {
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

    @Test
    fun limiteDeVeinteSeAplicaDespuesDelFiltro() {
        val avisos = (0 until 50).map { indice ->
            Aviso(indice + 1, "Registro $indice", "Texto $indice",
                if (indice < 25) Origen.BOTON else Origen.RECORDATORIO, "12:00:00")
        }
        compose.setContent {
            AvisosCampusTheme {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    HistorialAvisos(avisos, filtro = Origen.RECORDATORIO)
                }
            }
        }
        compose.onAllNodes(hasText("Registro ", substring = true)).assertCountEquals(20)
        compose.onNodeWithText("Registro 25").assertExists()
        compose.onNodeWithText("Registro 44").assertExists()
        compose.onNodeWithText("Registro 45").assertDoesNotExist()
        compose.onNodeWithText("Registro 0").assertDoesNotExist()
    }
}

/**
 * Las comprobaciones de publicación se ejecutan desde Android 13, donde existe
 * el permiso de notificaciones. GrantPermissionRule concede el permiso solo a
 * este paquete; el caso de denegación se comprueba aparte en aceptación manual.
 */
@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 33)
class AvisosNotificacionesAndroidTest {
    @get:Rule
    val permiso: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.POST_NOTIFICATIONS
    )

    private val contexto: Context = ApplicationProvider.getApplicationContext()
    private val gestor = requireNotNull(contexto.getSystemService(NotificationManager::class.java))

    @Before
    fun prepararCanalYLimpiarSoloEstePaquete() {
        gestor.cancelAll()
        Notificador.crearCanal(contexto)
    }

    @After
    fun retirarLasNotificacionesDePrueba() {
        gestor.cancelAll()
    }

    private fun esperarNotificaciones(ids: Set<Int>): List<StatusBarNotification> {
        val limite = SystemClock.uptimeMillis() + 5_000
        var actuales: List<StatusBarNotification>
        do {
            actuales = gestor.activeNotifications.filter {
                it.packageName == contexto.packageName && it.id in ids
            }
            if (actuales.map { it.id }.toSet() == ids) return actuales
            // Sondeo acotado del estado real del sistema, sin una espera fija larga.
            SystemClock.sleep(25)
        } while (SystemClock.uptimeMillis() < limite)
        assertEquals("No se publicaron todos los IDs esperados", ids,
            actuales.map { it.id }.toSet())
        return actuales
    }

    @Test
    fun crearElCanalDosVecesConservaIdentidadNombreEImportancia() {
        val primero = requireNotNull(gestor.getNotificationChannel(Notificador.CANAL))
        Notificador.crearCanal(contexto)
        val segundo = requireNotNull(gestor.getNotificationChannel(Notificador.CANAL))

        assertEquals("avisos", primero.id)
        assertEquals("Avisos", primero.name.toString())
        assertEquals(NotificationManager.IMPORTANCE_DEFAULT, primero.importance)
        assertEquals(primero.id, segundo.id)
        assertEquals(primero.name.toString(), segundo.name.toString())
        assertEquals(primero.importance, segundo.importance)
        assertEquals(1, gestor.notificationChannels.count { it.id == Notificador.CANAL })
    }

    @Test
    fun permisoConcedidoCoincideConElPermisoRealDeAndroid() {
        assertEquals(PackageManager.PERMISSION_GRANTED,
            ContextCompat.checkSelfPermission(contexto, Manifest.permission.POST_NOTIFICATIONS))
        assertTrue(Notificador.tienePermiso(contexto))
    }

    @Test
    fun dosIdsPermanecenPublicadosConContenidoIconoCanalEIntentInmutable() {
        val avisos = listOf(
            Aviso(15_001, "Primera prueba", "Texto uno", Origen.BOTON, "09:00:01"),
            Aviso(15_002, "Segunda prueba", "Texto dos", Origen.RECORDATORIO, "09:00:02")
        )
        avisos.forEach { assertTrue(Notificador.mostrar(contexto, it)) }
        val publicadas = esperarNotificaciones(avisos.map { it.id }.toSet())

        avisos.forEach { aviso ->
            val notificacion = publicadas.single { it.id == aviso.id }.notification
            assertEquals(aviso.titulo, notificacion.extras.getString(Notification.EXTRA_TITLE))
            assertEquals(aviso.texto, notificacion.extras.getString(Notification.EXTRA_TEXT))
            assertEquals(Notificador.CANAL, notificacion.channelId)
            assertEquals(R.drawable.ic_notificacion, requireNotNull(notificacion.smallIcon).resId)
            assertEquals(Notification.PRIORITY_DEFAULT, notificacion.priority)
            assertTrue(notificacion.flags and Notification.FLAG_AUTO_CANCEL != 0)
            assertNotNull(notificacion.contentIntent)
            assertTrue(notificacion.contentIntent.isActivity)
            assertTrue(notificacion.contentIntent.isImmutable)
            assertEquals(contexto.packageName, notificacion.contentIntent.creatorPackage)
        }
    }

    @Test
    fun pendingIntentDeLaNotificacionAbreMainActivity() {
        val instrumentacion = InstrumentationRegistry.getInstrumentation()
        // Un host visible permite enviar el PendingIntent sin depender de las
        // restricciones de lanzamiento de actividades desde segundo plano.
        // CLEAR_TASK deja únicamente el host en la tarea de ESTA aplicación.
        val intentHost = Intent(contexto, ComponentActivity::class.java).addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        ActivityScenario.launch<ComponentActivity>(intentHost).use {
            val monitor = instrumentacion.addMonitor(MainActivity::class.java.name, null, false)
            try {
                val aviso = Aviso(15_003, "Abrir campus", "Prueba del destino", Origen.BOTON, "09:00:03")
                assertTrue(Notificador.mostrar(contexto, aviso))
                val notificacion = esperarNotificaciones(setOf(aviso.id)).single().notification
                notificacion.contentIntent.send()

                val actividad = instrumentacion.waitForMonitorWithTimeout(monitor, 5_000)
                assertNotNull("El PendingIntent debe abrir MainActivity", actividad)
                if (actividad != null) {
                    try {
                        assertEquals(MainActivity::class.java, actividad.javaClass)
                    } finally {
                        instrumentacion.runOnMainSync { actividad.finish() }
                    }
                }
            } finally {
                instrumentacion.removeMonitor(monitor)
            }
        }
        // send() prueba el destino. La desaparición por autoCancel necesita una
        // pulsación REAL en la bandeja y se comprueba en la aceptación manual.
    }
}

@RunWith(AndroidJUnit4::class)
class ModoAvionReceiverAndroidTest {
    @Test
    fun receptorAceptaSoloLaAccionCorrectaYLeeElEstado() {
        val recibidos = mutableListOf<Boolean>()
        val receptor = ModoAvionReceiver { recibidos.add(it) }
        val contexto: Context = ApplicationProvider.getApplicationContext()

        receptor.onReceive(contexto,
            Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED).putExtra("state", true))
        receptor.onReceive(contexto,
            Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED).putExtra("state", false))
        receptor.onReceive(contexto,
            Intent("com.leon.avisoscampus.ACCION_AJENA").putExtra("state", true))
        receptor.onReceive(contexto, null)

        assertEquals(listOf(true, false), recibidos)
        // Invocación directa del receptor: no simula la emisión de un broadcast
        // protegido ni afirma verificar el registro dinámico del sistema.
    }
}
