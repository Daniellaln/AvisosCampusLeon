package com.leon.avisoscampus.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leon.avisoscampus.data.AvisosRepository
import com.leon.avisoscampus.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
class AvisosViewModel(private val repo: AvisosRepository): ViewModel() {
 val avisos: StateFlow<List<Aviso>> = repo.avisos
 private val _eventos = MutableSharedFlow<Aviso>()
 val eventos: SharedFlow<Aviso> = _eventos.asSharedFlow()
 private val _permiso = MutableStateFlow<PermisoUi>(PermisoUi.SinPedir)
 val permiso: StateFlow<PermisoUi> = _permiso.asStateFlow()
 private val _mensaje = MutableStateFlow<String?>(null)
 val mensaje: StateFlow<String?> = _mensaje.asStateFlow()
 fun permisoRespondido(concedido: Boolean) {
  _permiso.value = if(concedido) PermisoUi.Concedido else PermisoUi.Denegado
  if(concedido) _mensaje.value = null
 }
 // Espera la nueva pantalla si el aviso vence al recrear la composición.
 // No conserva replay: un aviso entregado no reaparece al rotar.
 private suspend fun emitir(aviso: Aviso) {
  _eventos.subscriptionCount.first { it > 0 }
  _eventos.emit(aviso)
 }
 fun avisarAhora() { viewModelScope.launch {
  val aviso=repo.registrar("Tarea lista", "Tu entrega fue registrada", Origen.BOTON)
  emitir(aviso)
 } }
 private var trabajo: Job? = null
 private val _faltan = MutableStateFlow<Int?>(null)
 val faltan: StateFlow<Int?> = _faltan.asStateFlow()
 fun recordarEn(segundos: Int = 10) {
  if(trabajo?.isActive == true) return
  require(segundos > 0)
  trabajo = viewModelScope.launch {
   try {
    for(segundo in segundos downTo 1) { _faltan.value=segundo; delay(1000) }
    val aviso=repo.registrar("Recordatorio", "Revisa tu laboratorio pendiente", Origen.RECORDATORIO)
    emitir(aviso)
   } finally { _faltan.value=null }
  }
 }
 fun cancelarRecordatorio() { trabajo?.cancel() }
 private val _escuchandoAvion=MutableStateFlow(false)
 val escuchandoAvion: StateFlow<Boolean> = _escuchandoAvion.asStateFlow()
 fun cambiarEscucha(valor: Boolean) { _escuchandoAvion.value=valor }
 fun alCambiarModoAvion(activo: Boolean) {
  if(!_escuchandoAvion.value) return
  viewModelScope.launch {
   val aviso=repo.registrar("Modo avión", if(activo) "Activado" else "Desactivado", Origen.MODO_AVION)
   emitir(aviso)
  }
 }
 fun noSePudoMostrar() { _mensaje.value="Sin permiso: el aviso se guardó en el historial, pero no se mostró" }
 private val _origenFiltro=MutableStateFlow<Origen?>(null)
 val origenFiltro: StateFlow<Origen?> = _origenFiltro.asStateFlow()
 fun filtrarHistorial(origen: Origen?) { _origenFiltro.value=origen }
}
