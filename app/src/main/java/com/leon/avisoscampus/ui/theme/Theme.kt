package com.leon.avisoscampus.ui.theme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val Colores=lightColorScheme(primary=Color(0xFF216F9B),onPrimary=Color.White,background=Color(0xFFFBF9FE),surface=Color.White,onSurface=Color(0xFF20232A),onBackground=Color(0xFF20232A),error=Color(0xFFAA343B))
@Composable fun AvisosCampusTheme(content:@Composable ()->Unit) { MaterialTheme(colorScheme=Colores,content=content) }
