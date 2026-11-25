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
 }
 fun avisarAhora() { viewModelScope.launch {
  val aviso=repo.registrar("Tarea lista", "Tu entrega fue registrada", Origen.BOTON)
  _eventos.emit(aviso)
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
    _eventos.emit(aviso)
   } finally { _faltan.value=null }
  }
 }
 fun cancelarRecordatorio() { trabajo?.cancel() }
}
