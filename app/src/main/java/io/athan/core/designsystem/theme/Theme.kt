package io.athan.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BottleGreenPrimaryDark,
    onPrimary = OnBottleGreenPrimaryDark,
    primaryContainer = BottleGreenContainerDark,
    onPrimaryContainer = OnBottleGreenContainerDark,
    secondary = SageSecondaryDark,
    secondaryContainer = SageContainerLight,
    onSecondaryContainer = OnSageContainerLight,
    tertiary = WarmAmberTertiaryDark,
    background = DarkBackground,
    surface = DarkSurface,
    onSurface = Color(0xFFE1E3DF),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFC1C9C3)
)

private val LightColorScheme = lightColorScheme(
    primary = BottleGreenPrimaryLight,
    onPrimary = OnBottleGreenPrimaryLight,
    primaryContainer = BottleGreenContainerLight,
    onPrimaryContainer = OnBottleGreenContainerLight,
    secondary = SageSecondaryLight,
    secondaryContainer = SageContainerDark,
    onSecondaryContainer = OnSageContainerDark,
    tertiary = WarmAmberTertiary,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = Color(0xFFDEE5DF),
    onSurfaceVariant = Color(0xFF424944)
)

@Composable
fun AthanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AthanTypography,
        shapes = AthanShapes,
        content = content
    )
}