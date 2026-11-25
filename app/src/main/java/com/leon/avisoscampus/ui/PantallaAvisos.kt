@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.leon.avisoscampus.ui

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.leon.avisoscampus.R
import com.leon.avisoscampus.model.*
import com.leon.avisoscampus.notificacion.Notificador
import com.leon.avisoscampus.receiver.CargadorReceiver
import com.leon.avisoscampus.receiver.ModoAvionReceiver
import com.leon.avisoscampus.viewmodel.AvisosViewModel
import kotlinx.coroutines.launch

@Composable
fun PantallaAvisos(vm: AvisosViewModel) = MotionSettings {
    val contexto = LocalContext.current
    val ciclo = LocalLifecycleOwner.current
    val permiso by vm.permiso.collectAsState()
    val avisos by vm.avisos.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val faltan by vm.faltan.collectAsState()
    val escuchando by vm.escuchandoAvion.collectAsState()
    val escuchandoCargador by vm.escuchandoCargador.collectAsState()
    val filtro by vm.origenFiltro.collectAsState()
    var seccion by rememberSaveable { mutableIntStateOf(0) }
    var recordatorio by rememberSaveable { mutableStateOf(false) }
    var pulso by remember { mutableIntStateOf(0) }
    val snackbars = remember { SnackbarHostState() }
    val alcance = rememberCoroutineScope()
    val c = MaterialTheme.colorScheme
    val motion = LocalMotionEnabled.current
    val lanzador = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        vm.permisoRespondido(it)
    }
    fun pedirPermiso() {
        if (Build.VERSION.SDK_INT >= 33) lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
        else vm.permisoRespondido(true)
    }
    fun abrirAjustes(canal: String? = null) {
        val intent = when {
            Build.VERSION.SDK_INT >= 26 && canal != null ->
                Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, contexto.packageName)
                    .putExtra(Settings.EXTRA_CHANNEL_ID, canal)
            Build.VERSION.SDK_INT >= 26 -> Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, contexto.packageName)
            else -> Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + contexto.packageName))
        }
        contexto.startActivity(intent)
    }
    BackHandler(seccion != 0) { seccion = 0 }
    DisposableEffect(ciclo, contexto) {
        val observador = LifecycleEventObserver { _, evento ->
            if (evento == Lifecycle.Event.ON_RESUME) {
                if (Notificador.tienePermiso(contexto)) vm.permisoRespondido(true)
                else if (vm.permiso.value != PermisoUi.SinPedir) vm.permisoRespondido(false)
            }
        }
        ciclo.lifecycle.addObserver(observador)
        onDispose { ciclo.lifecycle.removeObserver(observador) }
    }
    LaunchedEffect(Unit) { if (Notificador.tienePermiso(contexto)) vm.permisoRespondido(true) }
    // Se mantiene fuera de las pestañas: navegar no interrumpe la entrega.
    LaunchedEffect(vm, contexto) {
        vm.eventos.collect { aviso ->
            val mostrado = Notificador.mostrar(contexto, aviso)
            pulso++
            if (!mostrado) vm.noSePudoMostrar()
            alcance.launch {
                snackbars.currentSnackbarData?.dismiss()
                snackbars.showSnackbar(if (mostrado) "Aviso mostrado · " + aviso.titulo
                    else "Guardado en Historial. Revisa el permiso o el canal en Ajustes.",
                    withDismissAction = true, duration = SnackbarDuration.Short)
            }
        }
    }
    DisposableEffect(vm, escuchando, contexto) {
        if (escuchando) {
            val receptor = ModoAvionReceiver(vm::alCambiarModoAvion)
            ContextCompat.registerReceiver(contexto, receptor, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED),
                ContextCompat.RECEIVER_NOT_EXPORTED)
            onDispose { contexto.unregisterReceiver(receptor) }
        } else onDispose { }
    }
    DisposableEffect(vm, escuchandoCargador, contexto) {
        if (escuchandoCargador) {
            val receptor = CargadorReceiver(vm::alCambiarCargador)
            val acciones = IntentFilter().apply {
                addAction(Intent.ACTION_POWER_CONNECTED); addAction(Intent.ACTION_POWER_DISCONNECTED)
            }
            ContextCompat.registerReceiver(contexto, receptor, acciones, ContextCompat.RECEIVER_NOT_EXPORTED)
            onDispose { contexto.unregisterReceiver(receptor) }
        } else onDispose { }
    }
    BoxWithConstraints(Modifier.fillMaxSize().background(c.background)) {
        val expandido = maxWidth >= 700.dp
        Row(Modifier.fillMaxSize()) {
            if (expandido) NavigationRail(containerColor = c.surfaceContainerLow,
                modifier = Modifier.fillMaxHeight()) {
                Spacer(Modifier.weight(1f))
                DestinosRail(seccion, { seccion = it })
                Spacer(Modifier.weight(1f))
            }
            Scaffold(
                modifier = Modifier.weight(1f).semantics { testTagsAsResourceId = true },
                containerColor = c.background, contentColor = c.onBackground,
                topBar = {
                    TopAppBar(title = {
                        if (seccion == 0) Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(painterResource(R.drawable.ic_logo), null, Modifier.size(31.dp), tint = c.primary)
                            Text("AvisosCampus", style = MaterialTheme.typography.titleLarge)
                        } else Text(if (seccion == 1) "Historial" else "Ajustes",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.semantics { heading() })
                    }, navigationIcon = {
                        if (seccion != 0) IconButton(onClick = { seccion = 0 }) {
                            Icon(painterResource(R.drawable.ic_volver), "Volver a Avisos", Modifier.size(22.dp))
                        }
                    }, actions = {
                        if (seccion == 0) AssistChip(onClick = { seccion = 2 },
                            label = { Text(etiquetaPermiso(permiso)) },
                            leadingIcon = { Icon(painterResource(if (permiso == PermisoUi.Concedido) R.drawable.ic_check else R.drawable.ic_ajustes),
                                null, Modifier.size(16.dp)) },
                            modifier = Modifier.padding(end = 12.dp).heightIn(min = 48.dp)
                                .semantics { contentDescription = "Permiso: " + etiquetaPermiso(permiso) })
                    }, colors = TopAppBarDefaults.topAppBarColors(containerColor = c.background,
                        titleContentColor = c.onBackground, actionIconContentColor = c.primary))
                }, bottomBar = {
                    if (!expandido) NavigationBar(containerColor = c.surfaceContainerLow,
                        contentColor = c.onSurface, tonalElevation = 2.dp) {
                        DestinosBarra(seccion, { seccion = it })
                    }
                }, snackbarHost = { SnackbarHost(snackbars) }
            ) { margen ->
                Box(Modifier.fillMaxSize().padding(margen), contentAlignment = Alignment.TopCenter) {
                    AnimatedContent(targetState = seccion,
                        modifier = Modifier.widthIn(max = 760.dp).fillMaxSize(), label = "sección de avisos",
                        transitionSpec = {
                            if (motion) (fadeIn(tween(180)) + slideInHorizontally(tween(220)) {
                                if (targetState > initialState) it / 12 else -it / 12
                            }) togetherWith fadeOut(tween(100))
                            else EnterTransition.None togetherWith ExitTransition.None
                        }) { actual ->
                        when (actual) {
                            1 -> HistorialAvisos(avisos, filtro, vm::filtrarHistorial)
                            2 -> AjustesAvisos(permiso, mensaje, escuchando, escuchandoCargador,
                                ::pedirPermiso, ::abrirAjustes, vm::cambiarEscucha, vm::cambiarEscuchaCargador)
                            else -> InicioAvisos(recordatorio, { recordatorio = it }, faltan, pulso,
                                permiso, mensaje, avisos.firstOrNull(), vm::avisarAhora, { vm.recordarEn() },
                                {
                                    vm.cancelarRecordatorio()
                                    alcance.launch {
                                        snackbars.currentSnackbarData?.dismiss()
                                        snackbars.showSnackbar("Recordatorio cancelado", duration = SnackbarDuration.Short)
                                    }
                                }, ::pedirPermiso, { seccion = 2 }, { seccion = 1 })
                        }
                    }
                }
            }
        }
    }
}

private fun etiquetaPermiso(permiso: PermisoUi) = when (permiso) {
    PermisoUi.SinPedir -> "Sin pedir"
    PermisoUi.Concedido -> "Permitido"
    PermisoUi.Denegado -> "Denegado"
}

@Composable
private fun RowScope.DestinosBarra(actual: Int, onSeleccionar: (Int) -> Unit) {
    // Esta extensión se utiliza únicamente dentro de NavigationBar.
    val nombres = listOf("Avisos", "Historial", "Ajustes")
    val iconos = listOf(R.drawable.ic_campana, R.drawable.ic_historial, R.drawable.ic_ajustes)
    nombres.forEachIndexed { i, nombre ->
        NavigationBarItem(selected = actual == i, onClick = { onSeleccionar(i) },
            icon = { Icon(painterResource(iconos[i]), null, Modifier.size(24.dp)) },
            label = { Text(nombre) }, modifier = Modifier.semantics { contentDescription = "Ir a $nombre" })
    }
}

@Composable
private fun ColumnScope.DestinosRail(actual: Int, onSeleccionar: (Int) -> Unit) {
    val nombres = listOf("Avisos", "Historial", "Ajustes")
    val iconos = listOf(R.drawable.ic_campana, R.drawable.ic_historial, R.drawable.ic_ajustes)
    nombres.forEachIndexed { i, nombre ->
        NavigationRailItem(selected = actual == i, onClick = { onSeleccionar(i) },
            icon = { Icon(painterResource(iconos[i]), null, Modifier.size(24.dp)) },
            label = { Text(nombre) }, modifier = Modifier.semantics { contentDescription = "Ir a $nombre" })
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun InicioAvisos(
    recordatorio: Boolean, elegirModo: (Boolean) -> Unit, faltan: Int?, pulso: Int,
    permiso: PermisoUi, mensaje: String?, ultimo: Aviso?, mostrar: () -> Unit,
    recordar: () -> Unit, cancelar: () -> Unit, permitir: () -> Unit,
    ajustes: () -> Unit, historial: () -> Unit
) {
    val c = MaterialTheme.colorScheme
    val motion = LocalMotionEnabled.current
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 12.dp)
        .testTag("inicio_avisos"), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Text(if (recordatorio) "Tu recordatorio" else "Envía un aviso",
            style = MaterialTheme.typography.headlineMedium, color = c.onBackground,
            modifier = Modifier.semantics { heading() })
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            SegmentedButton(selected = !recordatorio, onClick = { elegirModo(false) },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                modifier = Modifier.heightIn(min = 48.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    Icon(painterResource(R.drawable.ic_campana), null, Modifier.size(18.dp)); Text("Ahora")
                }
            }
            SegmentedButton(selected = recordatorio, onClick = { elegirModo(true) },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                modifier = Modifier.heightIn(min = 48.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    Icon(painterResource(R.drawable.ic_reloj), null, Modifier.size(18.dp)); Text("En 10 s")
                }
            }
        }
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val amplio = maxWidth >= 560.dp
            @Composable fun Imagen() {
                IlustracionAviso(recordatorio, faltan, pulso, Modifier.fillMaxWidth().height(if (amplio) 210.dp else 228.dp))
            }
            @Composable fun Accion() {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(if (recordatorio) "Revisa tu laboratorio pendiente" else "Tarea lista",
                        style = MaterialTheme.typography.titleLarge, color = c.onSurface)
                    if (!recordatorio) Text("Tu entrega fue registrada", style = MaterialTheme.typography.bodyMedium, color = c.onSurfaceVariant)
                    if (recordatorio) {
                        AnimatedContent(targetState = faltan, label = "segundos restantes",
                            transitionSpec = {
                                if (motion) (fadeIn(tween(130)) + slideInVertically(tween(180)) { it / 3 }) togetherWith fadeOut(tween(90))
                                else EnterTransition.None togetherWith ExitTransition.None
                            }) { segundos ->
                            Text(if (segundos != null) "Faltan $segundos s" else "Listo para programar",
                                style = if (segundos != null) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.bodyMedium,
                                color = if (segundos != null) c.primary else c.onSurfaceVariant,
                                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
                        }
                        AvisosGlassButton("Recordar en 10 s", recordar, Modifier.fillMaxWidth(),
                            icon = R.drawable.ic_reloj, enabled = faltan == null)
                        if (faltan != null) OutlinedButton(onClick = cancelar, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                            Icon(painterResource(R.drawable.ic_cancelar), null, Modifier.size(19.dp))
                            Spacer(Modifier.width(8.dp)); Text("Cancelar")
                        }
                    } else {
                        AvisosGlassButton("Mostrar aviso", mostrar, Modifier.fillMaxWidth(), icon = R.drawable.ic_enviar)
                    }
                }
            }
            if (amplio) Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Box(Modifier.weight(1f)) { Imagen() }
                Box(Modifier.weight(1f)) { Accion() }
            } else Column(verticalArrangement = Arrangement.spacedBy(12.dp)) { Imagen(); Accion() }
        }
        if (!recordatorio && faltan != null) {
            Row(Modifier.fillMaxWidth().background(c.primaryContainer, RoundedCornerShape(14.dp)).padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(R.drawable.ic_reloj), null, Modifier.size(22.dp), tint = c.onPrimaryContainer)
                Text("Faltan $faltan s", style = MaterialTheme.typography.titleSmall,
                    color = c.onPrimaryContainer, modifier = Modifier.weight(1f))
                TextButton(onClick = { elegirModo(true) }, modifier = Modifier.heightIn(min = 48.dp)) { Text("Ver cuenta") }
            }
        }
        if (permiso != PermisoUi.Concedido || mensaje != null) {
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(if (permiso != PermisoUi.Concedido) "Activa las notificaciones para ver el aviso."
                    else mensaje ?: "", style = MaterialTheme.typography.bodyMedium,
                    color = if (permiso == PermisoUi.Denegado || mensaje != null) c.error else c.onSurfaceVariant,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
                TextButton(onClick = if (permiso == PermisoUi.Concedido || permiso == PermisoUi.Denegado) ajustes else permitir,
                    modifier = Modifier.heightIn(min = 48.dp), contentPadding = PaddingValues(horizontal = 0.dp)) {
                    Text(if (permiso == PermisoUi.SinPedir) "Permitir notificaciones" else "Abrir ajustes")
                }
            }
        }
        if (ultimo != null) {
            HorizontalDivider(color = c.outlineVariant.copy(alpha = .6f))
            TextButton(onClick = historial, modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp),
                contentPadding = PaddingValues(0.dp)) {
                Icon(painterResource(iconoOrigen(ultimo.origen)), null, Modifier.size(22.dp))
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(ultimo.titulo, style = MaterialTheme.typography.titleSmall, color = c.onSurface)
                    Text("Último aviso · " + ultimo.hora, style = MaterialTheme.typography.bodySmall, color = c.onSurfaceVariant)
                }
                Text("Ver historial", style = MaterialTheme.typography.labelMedium)
            }
        }
        Spacer(Modifier.height(4.dp))
    }
}

@Composable
private fun AjustesAvisos(
    permiso: PermisoUi, mensaje: String?, avion: Boolean, cargador: Boolean,
    permitir: () -> Unit, abrirAjustes: (String?) -> Unit,
    cambiarAvion: (Boolean) -> Unit, cambiarCargador: (Boolean) -> Unit
) {
    val c = MaterialTheme.colorScheme
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 12.dp)
        .testTag("ajustes_avisos"), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Permiso de notificaciones", style = MaterialTheme.typography.titleLarge,
            color = c.onSurface, modifier = Modifier.semantics { heading() })
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(painterResource(if (permiso == PermisoUi.Concedido) R.drawable.ic_check else R.drawable.ic_campana),
                null, Modifier.size(23.dp), tint = if (permiso == PermisoUi.Denegado) c.error else c.primary)
            Text(etiquetaPermiso(permiso), style = MaterialTheme.typography.titleMedium,
                color = if (permiso == PermisoUi.Denegado) c.error else c.primary,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
        }
        AvisosGlassButton("Permitir notificaciones", permitir, Modifier.fillMaxWidth(), icon = R.drawable.ic_campana)
        OutlinedButton(onClick = { abrirAjustes(null) }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text("Abrir ajustes")
        }
        if (mensaje != null) Text(mensaje, style = MaterialTheme.typography.bodyMedium, color = c.error,
            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
        HorizontalDivider(Modifier.padding(vertical = 4.dp), color = c.outlineVariant)
        Text("Escuchar al celular", style = MaterialTheme.typography.titleLarge,
            color = c.onSurface, modifier = Modifier.semantics { heading() })
        AjusteEscucha("Escuchar el modo avión", "Al activar o desactivar el modo avión", R.drawable.ic_avion, avion, cambiarAvion)
        AjusteEscucha("Escuchar el cargador", "Al conectar o desconectar el cargador", R.drawable.ic_cargador, cargador, cambiarCargador)
        HorizontalDivider(Modifier.padding(vertical = 4.dp), color = c.outlineVariant)
        Text("Canales", style = MaterialTheme.typography.titleLarge,
            color = c.onSurface, modifier = Modifier.semantics { heading() })
        CanalAvisos("Avisos", "Botón, modo avión y cargador", R.drawable.ic_campana) { abrirAjustes(Notificador.CANAL) }
        CanalAvisos("Urgentes", "Recordatorios de 10 segundos", R.drawable.ic_reloj) { abrirAjustes(Notificador.CANAL_URGENTE) }
        Text("El historial se conserva durante esta sesión.", style = MaterialTheme.typography.bodySmall,
            color = c.onSurfaceVariant, modifier = Modifier.padding(top = 6.dp, bottom = 18.dp))
    }
}

@Composable
private fun AjusteEscucha(titulo: String, detalle: String, icono: Int, activo: Boolean, cambiar: (Boolean) -> Unit) {
    val c = MaterialTheme.colorScheme
    Row(Modifier.fillMaxWidth().heightIn(min = 76.dp)
        .toggleable(value = activo, role = Role.Switch, onValueChange = cambiar)
        .semantics(mergeDescendants = true) { contentDescription = titulo }
        .padding(vertical = 9.dp), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        Box(Modifier.size(42.dp).background(if (activo) c.primaryContainer else c.surfaceContainerHigh, CircleShape),
            contentAlignment = Alignment.Center) {
            Icon(painterResource(icono), null, Modifier.size(21.dp), tint = if (activo) c.primary else c.onSurfaceVariant)
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleSmall, color = c.onSurface)
            Text(detalle, style = MaterialTheme.typography.bodySmall, color = c.onSurfaceVariant)
        }
        Switch(checked = activo, onCheckedChange = null, modifier = Modifier.clearAndSetSemantics { })
    }
}

@Composable
private fun CanalAvisos(titulo: String, detalle: String, icono: Int, abrir: () -> Unit) {
    val c = MaterialTheme.colorScheme
    TextButton(onClick = abrir, modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp),
        contentPadding = PaddingValues(vertical = 8.dp)) {
        Icon(painterResource(icono), null, Modifier.size(22.dp))
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleSmall, color = c.onSurface)
            Text(detalle, style = MaterialTheme.typography.bodySmall, color = c.onSurfaceVariant)
        }
        Icon(painterResource(R.drawable.ic_ajustes), "Configurar canal $titulo", Modifier.size(19.dp), tint = c.onSurfaceVariant)
    }
}
