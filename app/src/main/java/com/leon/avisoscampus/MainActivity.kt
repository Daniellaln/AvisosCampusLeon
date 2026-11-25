package com.leon.avisoscampus
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leon.avisoscampus.viewmodel.*
import com.leon.avisoscampus.ui.*
import com.leon.avisoscampus.ui.theme.AvisosCampusTheme
class MainActivity: ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState); enableEdgeToEdge()
  val repo = (application as AvisosApp).repository
  setContent { AvisosCampusTheme { val vm: AvisosViewModel = viewModel(factory=AvisosViewModelFactory(repo)); PantallaAvisos(vm) } }
 }
}
