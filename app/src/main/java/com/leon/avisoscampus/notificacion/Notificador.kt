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
 fun tienePermiso(context: Context): Boolean = Build.VERSION.SDK_INT < 33 || ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
 @SuppressLint("MissingPermission")
 fun mostrar(context: Context, aviso: Aviso): Boolean {
 if (!tienePermiso(context) || !NotificationManagerCompat.from(context).areNotificationsEnabled()) return false
  if (Build.VERSION.SDK_INT >= 26 && context.getSystemService(NotificationManager::class.java).getNotificationChannel(CANAL)?.importance == NotificationManager.IMPORTANCE_NONE) return false
  val builder = NotificationCompat.Builder(context, CANAL)
   .setSmallIcon(R.drawable.ic_notificacion).setContentTitle(aviso.titulo).setContentText(aviso.texto)
   .setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true)
  val intent = Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
  val pendiente = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
  builder.setContentIntent(pendiente)
  return try { NotificationManagerCompat.from(context).notify(aviso.id, builder.build()); true }
  catch (_: SecurityException) { false }
 }
}
