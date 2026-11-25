@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.leon.avisoscampus.ui
import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import com.leon.avisoscampus.model.*
import com.leon.avisoscampus.notificacion.Notificador
import com.leon.avisoscampus.viewmodel.AvisosViewModel
import com.leon.avisoscampus.receiver.ModoAvionReceiver

@Composable fun Tarjeta(titulo: String, contenido: @Composable ColumnScope.()->Unit) {
 Card(Modifier.fillMaxWidth(),shape=RoundedCornerShape(12.dp),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface),border=BorderStroke(1.dp,Color(0xFFDFE1E7))) {
  Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)) {
   Text(titulo,style=MaterialTheme.typography.titleMedium,color=MaterialTheme.colorScheme.primary,modifier=Modifier.semantics { heading() })
   contenido()
  }
 }
}
@Composable fun PantallaAvisos(vm: AvisosViewModel) {
 val contexto=LocalContext.current
 val permiso by vm.permiso.collectAsState()
 val avisos by vm.avisos.collectAsState()
 val mensaje by vm.mensaje.collectAsState()
 val lanzador=rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { vm.permisoRespondido(it) }
 val ciclo=LocalLifecycleOwner.current
 DisposableEffect(ciclo,contexto) {
  val observador=LifecycleEventObserver { _,evento -> if(evento==Lifecycle.Event.ON_RESUME) {
   if(Notificador.tienePermiso(contexto)) vm.permisoRespondido(true)
   else if(vm.permiso.value != PermisoUi.SinPedir) vm.permisoRespondido(false)
  } }
  ciclo.lifecycle.addObserver(observador)
  onDispose { ciclo.lifecycle.removeObserver(observador) }
 }
 LaunchedEffect(Unit) { if(Notificador.tienePermiso(contexto)) vm.permisoRespondido(true) }
 LaunchedEffect(vm,contexto) { vm.eventos.collect { aviso ->
  val mostrado=Notificador.mostrar(contexto,aviso)
  if(!mostrado) vm.noSePudoMostrar()
 } }
 val faltan by vm.faltan.collectAsState()
 val escuchando by vm.escuchandoAvion.collectAsState()
 DisposableEffect(escuchando,contexto) {
  if(escuchando) {
   val receptor=ModoAvionReceiver(vm::alCambiarModoAvion)
   ContextCompat.registerReceiver(contexto,receptor,IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED),ContextCompat.RECEIVER_NOT_EXPORTED)
   onDispose { contexto.unregisterReceiver(receptor) }
  } else { onDispose { } }
 }
 Scaffold(topBar={ TopAppBar(title={ Text("AvisosCampus") },colors=TopAppBarDefaults.topAppBarColors(containerColor=MaterialTheme.colorScheme.primary,titleContentColor=Color.White)) }) { margen ->
  Column(Modifier.fillMaxSize().padding(margen).verticalScroll(rememberScrollState()).padding(16.dp),verticalArrangement=Arrangement.spacedBy(14.dp)) {
   Tarjeta("Permiso de notificaciones") {
    Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(8.dp)) {
     Text("Estado:")
     val color=when(permiso) { PermisoUi.Concedido->Color(0xFF24683B);PermisoUi.Denegado->MaterialTheme.colorScheme.error;else->Color(0xFF5D636B) }
     Text(when(permiso) { PermisoUi.Concedido->"Permitido";PermisoUi.Denegado->"Denegado";else->"Sin pedir" },color=color,modifier=Modifier.semantics { liveRegion=LiveRegionMode.Polite })
    }
    Button(onClick={ if(Build.VERSION.SDK_INT>=33) lanzador.launch(Manifest.permission.POST_NOTIFICATIONS) else vm.permisoRespondido(true) },enabled=true) { Text("Permitir notificaciones") }
   }
   if(mensaje!=null) {
    Surface(color=MaterialTheme.colorScheme.errorContainer,shape=RoundedCornerShape(8.dp),modifier=Modifier.fillMaxWidth()) {
     Text(mensaje!!,color=MaterialTheme.colorScheme.onErrorContainer,modifier=Modifier.padding(14.dp).semantics { liveRegion=LiveRegionMode.Polite })
    }
    OutlinedButton(onClick={ contexto.startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE,contexto.packageName)) }) { Text("Abrir ajustes") }
   }
   Tarjeta("Avisos") { Button(onClick={ vm.avisarAhora() },enabled=true) { Text("Mostrar aviso") } }
   Tarjeta("Recordatorio") {
    FlowRow(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
     Button(onClick={ vm.recordarEn() },enabled=faltan==null) { Text("Recordar en 10 s") }
     OutlinedButton(onClick={ vm.cancelarRecordatorio() },enabled=faltan!=null) { Text("Cancelar") }
    }
    faltan?.let { Text("Faltan $it s",style=MaterialTheme.typography.titleMedium,modifier=Modifier.semantics { liveRegion=LiveRegionMode.Polite }) }
   }
   Tarjeta("Receptor de anuncios") {
    Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
     Text("Escuchar el modo avión",modifier=Modifier.weight(1f))
     Switch(checked=escuchando,onCheckedChange={ vm.cambiarEscucha(it) },enabled=true,modifier=Modifier.semantics { contentDescription="Escuchar el modo avión" })
    }
    Text("Activa o desactiva el modo avión del celular",style=MaterialTheme.typography.bodySmall)
   }
   HistorialAvisos(avisos)
  }
 }
}
