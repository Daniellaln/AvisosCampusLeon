package com.leon.avisoscampus

import com.leon.avisoscampus.data.AvisosRepositoryMemoria
import com.leon.avisoscampus.model.Aviso
import com.leon.avisoscampus.model.Origen
import com.leon.avisoscampus.model.PermisoUi
import com.leon.avisoscampus.viewmodel.AvisosViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/** Pruebas JVM: no necesitan Context, un dispositivo ni esperas reales. */
@OptIn(ExperimentalCoroutinesApi::class)
class AvisosViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun instalarMainDePrueba() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun restaurarMain() {
        Dispatchers.resetMain()
    }

    @Test
    fun estadoInicialSinAvisosPermisoRecordatorioEscuchaNiFiltro() = runTest(dispatcher) {
        val vm = AvisosViewModel(AvisosRepositoryMemoria())

        assertTrue(vm.avisos.value.isEmpty())
        assertEquals(PermisoUi.SinPedir, vm.permiso.value)
        assertNull(vm.mensaje.value)
        assertNull(vm.faltan.value)
        assertFalse(vm.escuchandoAvion.value)
        assertNull(vm.origenFiltro.value)
    }

    @Test
    fun botonRegistraYEmiteElMismoAvisoConLosDatosDelLaboratorio() = runTest(dispatcher) {
        val repo = AvisosRepositoryMemoria()
        val vm = AvisosViewModel(repo)
        val eventos = recibirEventosDe(vm)
        vm.permisoRespondido(true)

        vm.avisarAhora()
        runCurrent()

        assertEquals(1, eventos.size)
        val aviso = eventos.single()
        assertEquals("Tarea lista", aviso.titulo)
        assertEquals("Tu entrega fue registrada", aviso.texto)
        assertEquals(Origen.BOTON, aviso.origen)
        assertEquals(listOf(aviso), repo.avisos.value)
        assertEquals(repo.avisos.value, vm.avisos.value)
    }

    @Test
    fun permisoDenegadoConservaElAvisoYPermiteComunicarElFalloDeLaPantalla() = runTest(dispatcher) {
        val repo = AvisosRepositoryMemoria()
        val vm = AvisosViewModel(repo)
        val eventos = recibirEventosDe(vm)
        vm.permisoRespondido(false)

        vm.avisarAhora()
        runCurrent()
        vm.noSePudoMostrar()

        assertEquals(PermisoUi.Denegado, vm.permiso.value)
        assertEquals(1, eventos.size)
        assertEquals(eventos, repo.avisos.value)
        assertEquals(
            "Sin permiso: el aviso se guardó en el historial, pero no se mostró",
            vm.mensaje.value,
        )
    }

    @Test
    fun concederElPermisoBorraElMensajeDeErrorAnterior() = runTest(dispatcher) {
        val vm = AvisosViewModel(AvisosRepositoryMemoria())
        vm.permisoRespondido(false)
        vm.noSePudoMostrar()
        assertTrue(vm.mensaje.value != null)

        vm.permisoRespondido(true)

        assertEquals(PermisoUi.Concedido, vm.permiso.value)
        assertNull(vm.mensaje.value)
    }

    @Test
    fun recordatorioDeDiezSegundosNoLlegaAntesYEmiteUnaSolaVezAlVencer() = runTest(dispatcher) {
        val repo = AvisosRepositoryMemoria()
        val vm = AvisosViewModel(repo)
        val eventos = recibirEventosDe(vm)

        vm.recordarEn()
        runCurrent()
        advanceTimeBy(9_999)
        runCurrent()

        assertEquals(1, vm.faltan.value)
        assertTrue(eventos.isEmpty())
        assertTrue(repo.avisos.value.isEmpty())

        advanceTimeBy(1)
        runCurrent()

        val aviso = eventos.single()
        assertEquals("Recordatorio", aviso.titulo)
        assertEquals("Revisa tu laboratorio pendiente", aviso.texto)
        assertEquals(Origen.RECORDATORIO, aviso.origen)
        assertEquals(listOf(aviso), repo.avisos.value)
        assertNull(vm.faltan.value)

        advanceTimeBy(30_000)
        runCurrent()
        assertEquals(1, eventos.size)
        assertEquals(1, repo.avisos.value.size)
    }

    @Test
    fun cuentaRegresivaExponeDiezHastaUnoSinMostrarCero() = runTest(dispatcher) {
        val vm = AvisosViewModel(AvisosRepositoryMemoria())
        val eventos = recibirEventosDe(vm)

        vm.recordarEn()
        runCurrent()

        for (esperado in 10 downTo 1) {
            assertEquals(esperado, vm.faltan.value)
            assertTrue(eventos.isEmpty())
            advanceTimeBy(1_000)
            runCurrent()
        }

        assertNull(vm.faltan.value)
        assertEquals(1, eventos.size)
    }

    @Test
    fun cancelarALaMitadLimpiaLaCuentaSinGuardarNiEmitirAviso() = runTest(dispatcher) {
        val repo = AvisosRepositoryMemoria()
        val vm = AvisosViewModel(repo)
        val eventos = recibirEventosDe(vm)
        vm.recordarEn()
        runCurrent()
        advanceTimeBy(5_000)
        runCurrent()
        assertEquals(5, vm.faltan.value)

        vm.cancelarRecordatorio()
        runCurrent()
        assertNull(vm.faltan.value)
        advanceTimeBy(30_000)
        runCurrent()

        assertTrue(eventos.isEmpty())
        assertTrue(repo.avisos.value.isEmpty())
    }

    @Test
    fun segundoRecordatorioActivoSeIgnoraSinAdelantarNiReiniciarElPrimero() = runTest(dispatcher) {
        val vm = AvisosViewModel(AvisosRepositoryMemoria())
        val eventos = recibirEventosDe(vm)
        vm.recordarEn()
        runCurrent()
        advanceTimeBy(4_000)
        runCurrent()
        assertEquals(6, vm.faltan.value)

        vm.recordarEn(segundos = 1)
        runCurrent()
        assertEquals(6, vm.faltan.value)
        advanceTimeBy(5_999)
        runCurrent()
        assertTrue(eventos.isEmpty())

        advanceTimeBy(1)
        runCurrent()
        assertEquals(1, eventos.size)
        assertNull(vm.faltan.value)

        advanceTimeBy(10_000)
        runCurrent()
        assertEquals(1, eventos.size)
    }

    @Test
    fun sePuedeProgramarOtroRecordatorioDespuesDeTerminar() = runTest(dispatcher) {
        val repo = AvisosRepositoryMemoria()
        val vm = AvisosViewModel(repo)
        val eventos = recibirEventosDe(vm)
        vm.recordarEn(segundos = 2)
        runCurrent()
        advanceTimeBy(2_000)
        runCurrent()
        assertEquals(1, eventos.size)
        assertNull(vm.faltan.value)

        vm.recordarEn(segundos = 1)
        runCurrent()
        assertEquals(1, vm.faltan.value)
        advanceTimeBy(1_000)
        runCurrent()

        assertEquals(listOf(1, 2), eventos.map { it.id })
        assertEquals(listOf(2, 1), repo.avisos.value.map { it.id })
        assertNull(vm.faltan.value)
    }

    @Test
    fun sePuedeProgramarOtroRecordatorioDespuesDeCancelar() = runTest(dispatcher) {
        val vm = AvisosViewModel(AvisosRepositoryMemoria())
        val eventos = recibirEventosDe(vm)
        vm.recordarEn()
        runCurrent()
        advanceTimeBy(1_000)
        runCurrent()
        vm.cancelarRecordatorio()
        runCurrent()

        vm.recordarEn(segundos = 2)
        runCurrent()
        assertEquals(2, vm.faltan.value)
        advanceTimeBy(2_000)
        runCurrent()

        assertEquals(1, eventos.size)
        assertEquals(1, eventos.single().id)
        assertNull(vm.faltan.value)
        advanceTimeBy(20_000)
        runCurrent()
        assertEquals(1, eventos.size)
    }

    @Test
    fun modoAvionSinEscuchaIgnoraTantoActivacionComoDesactivacion() = runTest(dispatcher) {
        val vm = AvisosViewModel(AvisosRepositoryMemoria())
        val eventos = recibirEventosDe(vm)

        vm.alCambiarModoAvion(true)
        vm.alCambiarModoAvion(false)
        runCurrent()

        assertFalse(vm.escuchandoAvion.value)
        assertTrue(eventos.isEmpty())
        assertTrue(vm.avisos.value.isEmpty())
    }

    @Test
    fun modoAvionConEscuchaRegistraYEmiteActivadoYDesactivado() = runTest(dispatcher) {
        val repo = AvisosRepositoryMemoria()
        val vm = AvisosViewModel(repo)
        val eventos = recibirEventosDe(vm)
        vm.cambiarEscucha(true)
        assertTrue(vm.escuchandoAvion.value)

        vm.alCambiarModoAvion(true)
        runCurrent()
        vm.alCambiarModoAvion(false)
        runCurrent()

        assertEquals(listOf("Activado", "Desactivado"), eventos.map { it.texto })
        assertEquals(listOf("Modo avión", "Modo avión"), eventos.map { it.titulo })
        assertEquals(listOf(Origen.MODO_AVION, Origen.MODO_AVION), eventos.map { it.origen })
        assertEquals(listOf(1, 2), eventos.map { it.id })
        assertEquals(eventos.reversed(), repo.avisos.value)
    }

    @Test
    fun apagarLaEscuchaImpideNuevosAvisosSinBorrarElHistorial() = runTest(dispatcher) {
        val vm = AvisosViewModel(AvisosRepositoryMemoria())
        val eventos = recibirEventosDe(vm)
        vm.cambiarEscucha(true)
        vm.alCambiarModoAvion(true)
        runCurrent()
        val historialAnterior = vm.avisos.value

        vm.cambiarEscucha(false)
        vm.alCambiarModoAvion(false)
        runCurrent()

        assertFalse(vm.escuchandoAvion.value)
        assertEquals(1, eventos.size)
        assertEquals(historialAnterior, vm.avisos.value)
    }

    @Test
    fun seleccionarOrigenYVolverATodosConservaElHistorialCompleto() = runTest(dispatcher) {
        val repo = AvisosRepositoryMemoria()
        repo.registrar("Aviso de prueba", "Contenido ficticio", Origen.BOTON)
        repo.registrar("Recordatorio de prueba", "Contenido ficticio", Origen.RECORDATORIO)
        val vm = AvisosViewModel(repo)
        val historialCompleto = repo.avisos.value

        for (origen in Origen.entries) {
            vm.filtrarHistorial(origen)
            assertEquals(origen, vm.origenFiltro.value)
            assertEquals(historialCompleto, repo.avisos.value)
        }
        vm.filtrarHistorial(null)

        assertNull(vm.origenFiltro.value)
        assertEquals(historialCompleto, repo.avisos.value)
        assertEquals(historialCompleto, vm.avisos.value)
    }

    @Test
    fun avisoEsperaUnRecolectorYNoSeRepiteAlReconectarlo() = runTest(dispatcher) {
        val vm = AvisosViewModel(AvisosRepositoryMemoria())
        vm.avisarAhora()
        runCurrent()
        assertEquals(1, vm.avisos.value.size)
        val recibidos = mutableListOf<Aviso>()
        val primero = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            vm.eventos.collect { recibidos.add(it) }
        }
        runCurrent()
        assertEquals(1, recibidos.size)
        primero.cancel()
        runCurrent()
        val segundos = recibirEventosDe(vm)
        runCurrent()
        assertTrue(segundos.isEmpty())
    }

    /** El recolector queda activo antes de emitir y runTest lo cancela al acabar. */
    private fun TestScope.recibirEventosDe(vm: AvisosViewModel): MutableList<Aviso> {
        val recibidos = mutableListOf<Aviso>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            vm.eventos.collect { recibidos.add(it) }
        }
        return recibidos
    }
}

class AvisosRepositoryMemoriaTest {
    @Test
    fun registrarAsignaIdsConsecutivosConservaDatosYOrdenaLoMasRecientePrimero() {
        val repo = AvisosRepositoryMemoria()

        val primero = repo.registrar("Primero", "Texto uno", Origen.BOTON)
        val segundo = repo.registrar("Segundo", "Texto dos", Origen.RECORDATORIO)
        val tercero = repo.registrar("Tercero", "Texto tres", Origen.MODO_AVION)

        assertEquals(listOf(1, 2, 3), listOf(primero.id, segundo.id, tercero.id))
        assertEquals(listOf(tercero, segundo, primero), repo.avisos.value)
        assertEquals("Primero", primero.titulo)
        assertEquals("Texto uno", primero.texto)
        assertEquals(Origen.BOTON, primero.origen)
        val formatoHora = Regex("(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d")
        assertTrue(repo.avisos.value.all { formatoHora.matches(it.hora) })
    }

    @Test
    fun unaNuevaInstanciaNoRecuperaAvisosNiIdsDeLaAnterior() {
        val anterior = AvisosRepositoryMemoria()
        anterior.registrar("Anterior", "Contenido ficticio", Origen.BOTON)
        anterior.registrar("Otro", "Contenido ficticio", Origen.BOTON)

        val nueva = AvisosRepositoryMemoria()
        assertTrue(nueva.avisos.value.isEmpty())
        val primeroNuevo = nueva.registrar("Nueva sesión", "Contenido ficticio", Origen.BOTON)

        assertEquals(1, primeroNuevo.id)
        assertEquals(listOf(primeroNuevo), nueva.avisos.value)
        assertEquals(2, anterior.avisos.value.size)
    }
}
