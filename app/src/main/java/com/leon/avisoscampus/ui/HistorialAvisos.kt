@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
package com.leon.avisoscampus.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.leon.avisoscampus.R
import com.leon.avisoscampus.model.Aviso
import com.leon.avisoscampus.model.Origen

internal fun iconoOrigen(origen: Origen): Int = when (origen) {
    Origen.BOTON -> R.drawable.ic_campana
    Origen.RECORDATORIO -> R.drawable.ic_reloj
    Origen.MODO_AVION -> R.drawable.ic_avion
    Origen.CARGADOR -> R.drawable.ic_cargador
}

@Composable
fun HistorialAvisos(
    avisos: List<Aviso>,
    filtro: Origen? = null,
    onFiltro: (Origen?) -> Unit = {}
) {
    val c = MaterialTheme.colorScheme
    val visibles = avisos.filter { filtro == null || it.origen == filtro }.take(20)
    val motion = LocalMotionEnabled.current
    LazyColumn(
        modifier = Modifier.fillMaxSize().testTag("lista_historial"),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        item(key = "titulo") {
            Text("Tus avisos", style = MaterialTheme.typography.headlineMedium,
                color = c.onBackground, modifier = Modifier.semantics { heading() })
            Text("Generados en esta sesión", style = MaterialTheme.typography.bodyMedium,
                color = c.onSurfaceVariant, modifier = Modifier.padding(top = 5.dp, bottom = 12.dp))
        }
        item(key = "filtros") {
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = filtro == null, onClick = { onFiltro(null) },
                    label = { Text("Todos") }, modifier = Modifier.heightIn(min = 48.dp))
                Origen.entries.forEach { origen ->
                    FilterChip(selected = filtro == origen, onClick = { onFiltro(origen) },
                        label = { Text(origen.texto) },
                        leadingIcon = { Icon(painterResource(iconoOrigen(origen)), null, Modifier.size(18.dp)) },
                        modifier = Modifier.heightIn(min = 48.dp))
                }
            }
        }
        if (visibles.isEmpty()) item(key = "vacio") {
            Column(Modifier.fillMaxWidth().padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Image(painterResource(R.drawable.aviso_historial_vacio), null, Modifier.size(224.dp, 184.dp))
                Text(if (avisos.isEmpty()) "Sin avisos todavía" else "Sin avisos de este origen",
                    style = MaterialTheme.typography.titleLarge, color = c.onSurface)
                Text(if (avisos.isEmpty()) "El primero aparecerá cuando generes un aviso."
                    else "Puedes volver a Todos para ver el resto.",
                    style = MaterialTheme.typography.bodyMedium, color = c.onSurfaceVariant)
                if (filtro != null) TextButton(onClick = { onFiltro(null) },
                    modifier = Modifier.heightIn(min = 48.dp)) { Text("Ver todos") }
            }
        }
        items(visibles, key = { it.id }) { aviso ->
            Row(Modifier.fillMaxWidth().then(if (motion) Modifier.animateItem() else Modifier)
                .padding(vertical = 16.dp).testTag("aviso_${aviso.id}"),
                horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.Top) {
                val recordatorio = aviso.origen == Origen.RECORDATORIO
                Box(Modifier.size(44.dp).background(
                    if (recordatorio) c.secondaryContainer else c.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center) {
                    Icon(painterResource(iconoOrigen(aviso.origen)), null, Modifier.size(22.dp),
                        tint = if (recordatorio) c.onSecondaryContainer else c.onPrimaryContainer)
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(aviso.origen.texto, style = MaterialTheme.typography.labelMedium,
                            color = c.primary, modifier = Modifier.weight(1f))
                        Text(aviso.hora, style = MaterialTheme.typography.labelSmall, color = c.onSurfaceVariant)
                    }
                    Text(aviso.titulo, style = MaterialTheme.typography.titleMedium, color = c.onSurface)
                    Text(aviso.texto, style = MaterialTheme.typography.bodyMedium, color = c.onSurfaceVariant)
                }
            }
            HorizontalDivider(color = c.outlineVariant.copy(alpha = .5f))
        }
        if (visibles.isNotEmpty()) item(key = "limite") {
            Text("Hasta 20 avisos recientes por filtro", style = MaterialTheme.typography.bodySmall,
                color = c.onSurfaceVariant, modifier = Modifier.padding(top = 18.dp))
        }
    }
}
