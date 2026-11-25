package com.leon.avisoscampus.ui
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.leon.avisoscampus.model.*
@Composable fun HistorialAvisos(avisos: List<Aviso>) {
 Tarjeta("Historial") {
  val visibles=avisos.take(20)
  if(visibles.isEmpty()) Text("Sin avisos todavía")
  visibles.forEach { aviso ->
   Column(Modifier.fillMaxWidth().padding(vertical=5.dp),verticalArrangement=Arrangement.spacedBy(4.dp)) {
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween) {
     Text(aviso.origen.texto,color=MaterialTheme.colorScheme.primary,style=MaterialTheme.typography.labelMedium)
     Text(aviso.hora,style=MaterialTheme.typography.labelSmall)
    }
    Text(aviso.titulo,style=MaterialTheme.typography.titleSmall)
    Text(aviso.texto,style=MaterialTheme.typography.bodySmall)
    HorizontalDivider(Modifier.padding(top=8.dp))
   }
  }
 }
}
