package com.leon.avisoscampus.notificacion
import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.*
import androidx.core.content.ContextCompat
import com.leon.avisoscampus.MainActivity
import com.leon.avisoscampus.R
import com.leon.avisoscampus.model.Aviso
object Notificador {
 const val CANAL = "avisos"
 fun crearCanal(context: Context) {
 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
   context.getSystemService(NotificationManager::class.java).createNotificationChannel(
    NotificationChannel(CANAL, "Avisos", NotificationManager.IMPORTANCE_DEFAULT))
  }
 }
 fun tienePermiso(context: Context): Boolean = false
 @SuppressLint("MissingPermission")
 fun mostrar(context: Context, aviso: Aviso): Boolean {
 return false // Paso 3: construir y mostrar.
 }
}
