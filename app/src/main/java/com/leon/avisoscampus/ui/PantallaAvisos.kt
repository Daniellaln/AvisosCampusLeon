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
 Scaffold(topBar={ TopAppBar(title={ Text("AvisosCampus") },colors=TopAppBarDefaults.topAppBarColors(containerColor=MaterialTheme.colorScheme.primary,titleContentColor=Color.White)) }) { margen ->
  Column(Modifier.fillMaxSize().padding(margen).verticalScroll(rememberScrollState()).padding(16.dp),verticalArrangement=Arrangement.spacedBy(14.dp)) {
   Tarjeta("Permiso de notificaciones") {
    Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(8.dp)) {
     Text("Estado:")
     val color=when(permiso) { PermisoUi.Concedido->Color(0xFF24683B);PermisoUi.Denegado->MaterialTheme.colorScheme.error;else->Color(0xFF5D636B) }
     Text(when(permiso) { PermisoUi.Concedido->"Permitido";PermisoUi.Denegado->"Denegado";else->"Sin pedir" },color=color,modifier=Modifier.semantics { liveRegion=LiveRegionMode.Polite })
    }
    Button(onClick={  },enabled=false) { Text("Permitir notificaciones") }
   }
   Tarjeta("Avisos") { Button(onClick={  },enabled=false) { Text("Mostrar aviso") } }
   Tarjeta("Recordatorio") {
    FlowRow(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
     Button(onClick={  },enabled=false) { Text("Recordar en 10 s") }
     OutlinedButton(onClick={  },enabled=false) { Text("Cancelar") }
    }
   }
   Tarjeta("Receptor de anuncios") {
    Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
     Text("Escuchar el modo avión",modifier=Modifier.weight(1f))
     Switch(checked=false,onCheckedChange={  },enabled=false,modifier=Modifier.semantics { contentDescription="Escuchar el modo avión" })
    }
    Text("Activa o desactiva el modo avión del celular",style=MaterialTheme.typography.bodySmall)
   }
   Tarjeta("Historial") { Text("Sin avisos todavía") }
  }
 }
}
