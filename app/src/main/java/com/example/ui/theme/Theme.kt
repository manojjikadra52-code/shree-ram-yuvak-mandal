package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = SaffronDarkPrimary,
    onPrimary = SaffronDarkOnPrimary,
    primaryContainer = SaffronDeepOrange,
    onPrimaryContainer = SaffronLightOrange,
    secondary = SaffronMediumOrange,
    onSecondary = Color.White,
    secondaryContainer = SaffronDarkSurface,
    onSecondaryContainer = SaffronDarkText,
    tertiary = SaffronGoldAccent,
    background = SaffronDarkBackground,
    onBackground = SaffronDarkText,
    surface = SaffronDarkSurface,
    onSurface = SaffronDarkText,
    surfaceVariant = SaffronDarkSurface,
    onSurfaceVariant = SaffronDarkText,
    outline = SaffronDarkBorder
  )

private val LightColorScheme =
  lightColorScheme(
    primary = SaffronDeepOrange,
    onPrimary = Color.White,
    primaryContainer = SaffronLightOrange,
    onPrimaryContainer = SaffronSecondaryText,
    secondary = SaffronMediumOrange,
    onSecondary = Color.White,
    secondaryContainer = SaffronLightOrange,
    onSecondaryContainer = SaffronSecondaryText,
    tertiary = SaffronGoldAccent,
    background = SaffronBackgroundWarm,
    onBackground = SaffronDarkSlate,
    surface = Color.White,
    onSurface = SaffronDarkSlate,
    surfaceVariant = SaffronLightOrange,
    onSurfaceVariant = SaffronSecondaryText,
    outline = SaffronBorderColor
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // For a branded app like Shree Ram Yuvak Mandal, we lock standard dynamic colors
  // to false so that our hand-crafted, beautiful Saffron brand themes are always utilized.
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
