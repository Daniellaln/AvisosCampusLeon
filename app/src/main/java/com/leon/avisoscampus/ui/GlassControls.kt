package com.leon.avisoscampus.ui

import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.leon.avisoscampus.R
import kotlinx.coroutines.launch

val LocalMotionEnabled = staticCompositionLocalOf { true }

/** Respeta «Quitar animaciones», incluso si cambia mientras la app está abierta. */
@Composable
fun MotionSettings(content: @Composable () -> Unit) {
    val resolver = LocalContext.current.contentResolver
    fun permiteMovimiento() = runCatching {
        Settings.Global.getFloat(resolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) > 0f
    }.getOrDefault(true)
    var enabled by remember(resolver) { mutableStateOf(permiteMovimiento()) }
    DisposableEffect(resolver) {
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) { enabled = permiteMovimiento() }
        }
        resolver.registerContentObserver(
            Settings.Global.getUriFor(Settings.Global.ANIMATOR_DURATION_SCALE), false, observer
        )
        onDispose { resolver.unregisterContentObserver(observer) }
    }
    CompositionLocalProvider(LocalMotionEnabled provides enabled, content = content)
}

@Composable
fun AvisosGlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Int? = null,
    primary: Boolean = true,
    enabled: Boolean = true
) {
    val colors = MaterialTheme.colorScheme
    val interactions = remember { MutableInteractionSource() }
    val pressed by interactions.collectIsPressedAsState()
    val motion = LocalMotionEnabled.current
    val haptics = LocalHapticFeedback.current
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) .975f else 1f,
        animationSpec = if (motion) spring(dampingRatio = .65f, stiffness = 650f) else snap(),
        label = "Presión del botón"
    )
    val shape = RoundedCornerShape(24.dp)
    val base = if (primary) colors.primary else colors.surfaceContainerHigh
    val ink = if (primary) colors.onPrimary else colors.onSurface
    val fill = if (!enabled) listOf(colors.onSurface.copy(alpha = .10f), colors.onSurface.copy(alpha = .10f))
        else if (primary) listOf(base, base.copy(alpha = .94f), base)
        else listOf(colors.surface.copy(alpha = .90f), base.copy(alpha = .72f))
    Button(
        onClick = {
            haptics.performHapticFeedback(HapticFeedbackType.VirtualKey)
            onClick()
        },
        modifier = modifier
            .heightIn(min = 56.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .shadow(if (enabled) 5.dp else 0.dp, shape)
            .clip(shape)
            .background(Brush.verticalGradient(fill))
            .border(1.dp, Brush.verticalGradient(listOf(Color.White.copy(alpha = .70f), Color.White.copy(alpha = .12f))), shape),
        enabled = enabled,
        shape = shape,
        interactionSource = interactions,
        elevation = null,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent, contentColor = ink,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = colors.onSurface.copy(alpha = .40f)
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 15.dp)
    ) {
        icon?.let {
            Icon(painterResource(it), contentDescription = null, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(10.dp))
        }
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun IlustracionAviso(
    modoRecordatorio: Boolean,
    faltan: Int?,
    pulso: Int,
    modifier: Modifier = Modifier
) {
    val motion = LocalMotionEnabled.current
    val swing = remember { Animatable(0f) }
    val wave = remember { Animatable(1f) }
    val primary = MaterialTheme.colorScheme.primary
    val progress by animateFloatAsState(
        ((faltan ?: 0) / 10f).coerceIn(0f, 1f),
        animationSpec = if (motion) tween(800, easing = LinearEasing) else snap(),
        label = "Tiempo restante"
    )
    LaunchedEffect(pulso, motion) {
        if (!motion || pulso == 0) {
            swing.snapTo(0f)
            wave.snapTo(1f)
        } else {
            launch {
                wave.snapTo(0f)
                wave.animateTo(1f, tween(760, easing = LinearOutSlowInEasing))
            }
            swing.animateTo(-9f, tween(75))
            swing.animateTo(7f, tween(120))
            swing.animateTo(-4f, tween(110))
            swing.animateTo(0f, spring(dampingRatio = .6f, stiffness = 500f))
        }
    }
    Box(modifier, contentAlignment = Alignment.Center) {
        if (motion && wave.value < 1f) Canvas(Modifier.matchParentSize()) {
            val sizeBase = size.minDimension
            repeat(2) { index ->
                val t = ((wave.value - index * .17f) / (1f - index * .17f)).coerceIn(0f, 1f)
                drawCircle(
                    primary.copy(alpha = (1f - t) * .22f),
                    radius = sizeBase * (.26f + t * .22f),
                    style = Stroke(width = 1.5.dp.toPx())
                )
            }
        }
        if (modoRecordatorio && faltan != null) CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxHeight(.9f).aspectRatio(1f),
            color = primary.copy(alpha = .65f),
            trackColor = primary.copy(alpha = .08f),
            strokeWidth = 2.dp
        )
        AnimatedContent(
            targetState = modoRecordatorio,
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
            transitionSpec = {
                if (motion) (fadeIn(tween(200)) + slideInVertically(tween(200)) { it / 12 }) togetherWith
                    (fadeOut(tween(120)) + slideOutVertically(tween(120)) { -it / 12 })
                else fadeIn(snap()) togetherWith fadeOut(snap())
            },
            label = "Ilustración de la tarea"
        ) { recordatorio ->
            Image(
                painterResource(if (recordatorio) R.drawable.aviso_reloj else R.drawable.aviso_campana),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp, vertical = 12.dp)
                    .graphicsLayer {
                        rotationZ = if (!recordatorio) swing.value else 0f
                        transformOrigin = TransformOrigin(.5f, .15f)
                    }
            )
        }
    }
}
