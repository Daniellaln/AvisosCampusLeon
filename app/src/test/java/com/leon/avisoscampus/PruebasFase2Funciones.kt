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
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/** Incorporar a src/test después de aplicar funciones_03.patch. */
@OptIn(ExperimentalCoroutinesApi::class)
class PruebasFase2Funciones {
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
    fun cargadorEmpiezaSinEscucharEIgnoraAmbosCambios() = runTest(dispatcher) {
        val vm = AvisosViewModel(AvisosRepositoryMemoria())
        val eventos = recibirEventosDe(vm)

        assertFalse(vm.escuchandoCargador.value)
        vm.alCambiarCargador(true)
        vm.alCambiarCargador(false)
        runCurrent()

        assertTrue(vm.avisos.value.isEmpty())
        assertTrue(eventos.isEmpty())
    }

    @Test
    fun losDosInterruptoresCambianSuEstadoDeFormaIndependiente() = runTest(dispatcher) {
        val vm = AvisosViewModel(AvisosRepositoryMemoria())

        vm.cambiarEscuchaCargador(true)
        assertTrue(vm.escuchandoCargador.value)
        assertFalse(vm.escuchandoAvion.value)
        vm.cambiarEscucha(true)
        vm.cambiarEscuchaCargador(false)
        assertFalse(vm.escuchandoCargador.value)
        assertTrue(vm.escuchandoAvion.value)
        vm.cambiarEscuchaCargador(true)
        vm.cambiarEscucha(false)
        assertTrue(vm.escuchandoCargador.value)
        assertFalse(vm.escuchandoAvion.value)
        assertTrue(vm.avisos.value.isEmpty())
    }

    @Test
    fun conectarRegistraYEmiteElMismoAvisoAunqueNoHayaPermiso() = runTest(dispatcher) {
        val repo = AvisosRepositoryMemoria()
        val vm = AvisosViewModel(repo)
        val eventos = recibirEventosDe(vm)
        vm.permisoRespondido(false)
        vm.cambiarEscuchaCargador(true)

        vm.alCambiarCargador(true)
        runCurrent()

        val aviso = eventos.single()
        assertEquals("Cargador", aviso.titulo)
        assertEquals("Conectado", aviso.texto)
        assertEquals(Origen.CARGADOR, aviso.origen)
        assertEquals("Cargador", aviso.origen.texto)
        assertEquals(listOf(aviso), repo.avisos.value)
        assertEquals(repo.avisos.value, vm.avisos.value)
        assertEquals(PermisoUi.Denegado, vm.permiso.value)
    }

    @Test
    fun desconectarRegistraYEmiteSuTextoConOrigenCargador() = runTest(dispatcher) {
        val repo = AvisosRepositoryMemoria()
        val vm = AvisosViewModel(repo)
        val eventos = recibirEventosDe(vm)
        vm.cambiarEscuchaCargador(true)

        vm.alCambiarCargador(false)
        runCurrent()

        val aviso = eventos.single()
        assertEquals("Cargador", aviso.titulo)
        assertEquals("Desconectado", aviso.texto)
        assertEquals(Origen.CARGADOR, aviso.origen)
        assertEquals(listOf(aviso), repo.avisos.value)
    }

    @Test
    fun apagarLaEscuchaBloqueaNuevosAvisosYReactivarlaConservaHistorialYFiltro() = runTest(dispatcher) {
        val vm = AvisosViewModel(AvisosRepositoryMemoria())
        val eventos = recibirEventosDe(vm)
        vm.filtrarHistorial(Origen.CARGADOR)
        vm.cambiarEscuchaCargador(true)
        vm.alCambiarCargador(true)
        runCurrent()
        val primero = vm.avisos.value.single()

        vm.cambiarEscuchaCargador(false)
        vm.alCambiarCargador(false)
        vm.alCambiarCargador(true)
        runCurrent()
        assertEquals(listOf(primero), vm.avisos.value)
        assertEquals(listOf(primero), eventos)
        assertEquals(Origen.CARGADOR, vm.origenFiltro.value)

        vm.cambiarEscuchaCargador(true)
        vm.alCambiarCargador(false)
        runCurrent()
        assertEquals(listOf("Conectado", "Desconectado"), eventos.map { it.texto })
        assertEquals(listOf(1, 2), eventos.map { it.id })
        assertEquals(eventos.reversed(), vm.avisos.value)
        assertEquals(Origen.CARGADOR, vm.origenFiltro.value)
    }

    @Test
    fun viewModelMantieneSusFirmasYCamposLibresDeTiposDelSistemaAndroid() {
        val clase = AvisosViewModel::class.java
        val tipos = buildList<Class<*>> {
            clase.declaredFields.forEach { add(it.type) }
            clase.declaredConstructors.forEach { addAll(it.parameterTypes.toList()) }
            clase.declaredMethods.forEach {
                add(it.returnType)
                addAll(it.parameterTypes.toList())
            }
        }
        val tiposAndroid = tipos.map { it.name }.filter { it.startsWith("android.") }
        assertTrue("El ViewModel no debe recibir ni retener tipos Android: $tiposAndroid",
            tiposAndroid.isEmpty())
        assertEquals("androidx.lifecycle.ViewModel", clase.superclass.name)
    }

    private fun TestScope.recibirEventosDe(vm: AvisosViewModel): MutableList<Aviso> {
        val recibidos = mutableListOf<Aviso>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            vm.eventos.collect { recibidos.add(it) }
        }
        return recibidos
    }
}
