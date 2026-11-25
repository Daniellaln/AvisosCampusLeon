package com.leon.avisoscampus.ui
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.leon.avisoscampus.model.*
@Composable fun HistorialAvisos(avisos: List<Aviso>,filtro: Origen?=null,onFiltro:(Origen?)->Unit={}) {
 Tarjeta("Historial") {
  Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)) {
   FilterChip(selected=filtro==null,onClick={ onFiltro(null) },label={ Text("Todos") })
   Origen.entries.forEach { origen -> FilterChip(selected=filtro==origen,onClick={ onFiltro(origen) },label={ Text(origen.texto) }) }
  }
  val visibles=avisos.filter { filtro==null || it.origen==filtro }.take(20)
  if(visibles.isEmpty()) Text(if(avisos.isEmpty()) "Sin avisos todavía" else "Sin avisos de este origen")
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
