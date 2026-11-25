package com.leon.avisoscampus
import android.app.Application
import com.leon.avisoscampus.data.AvisosRepositoryMemoria
import com.leon.avisoscampus.notificacion.Notificador
class AvisosApp: Application() {
 val repository = AvisosRepositoryMemoria()
 override fun onCreate() { super.onCreate(); /* Paso 1: crear el canal Avisos. */ }
}
