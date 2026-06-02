package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.data.ThemePalette

// Defining color schemes for each palette
private val MistyRoseLight = lightColorScheme(
    primary = MistyRosePrimaryLight,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    surface = MistyRoseSurfaceLight,
    onSurface = MistyRoseOnSurfaceLight,
    background = MistyRoseSurfaceLight,
    onBackground = MistyRoseOnSurfaceLight,
    primaryContainer = MistyRosePrimaryLight.copy(alpha = 0.15f),
    onPrimaryContainer = MistyRosePrimaryLight,
    surfaceVariant = MistyRoseSurfaceLight,
    onSurfaceVariant = MistyRosePrimaryLight
)
private val MistyRoseDark = darkColorScheme(
    primary = MistyRosePrimaryDark,
    onPrimary = MistyRoseSurfaceDark,
    surface = MistyRoseSurfaceDark,
    onSurface = MistyRoseOnSurfaceDark,
    background = MistyRoseSurfaceDark,
    onBackground = MistyRoseOnSurfaceDark,
    primaryContainer = MistyRosePrimaryDark.copy(alpha = 0.15f),
    onPrimaryContainer = MistyRosePrimaryDark,
    surfaceVariant = MistyRoseSurfaceDark,
    onSurfaceVariant = MistyRosePrimaryDark
)

private val SmokyBlueLight = lightColorScheme(
    primary = SmokyBluePrimaryLight,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    surface = SmokyBlueSurfaceLight,
    onSurface = SmokyBlueOnSurfaceLight,
    background = SmokyBlueSurfaceLight,
    onBackground = SmokyBlueOnSurfaceLight,
    primaryContainer = SmokyBluePrimaryLight.copy(alpha = 0.15f),
    onPrimaryContainer = SmokyBluePrimaryLight,
    surfaceVariant = SmokyBlueSurfaceLight,
    onSurfaceVariant = SmokyBluePrimaryLight
)
private val SmokyBlueDark = darkColorScheme(
    primary = SmokyBluePrimaryDark,
    onPrimary = SmokyBlueSurfaceDark,
    surface = SmokyBlueSurfaceDark,
    onSurface = SmokyBlueOnSurfaceDark,
    background = SmokyBlueSurfaceDark,
    onBackground = SmokyBlueOnSurfaceDark,
    primaryContainer = SmokyBluePrimaryDark.copy(alpha = 0.15f),
    onPrimaryContainer = SmokyBluePrimaryDark,
    surfaceVariant = SmokyBlueSurfaceDark,
    onSurfaceVariant = SmokyBluePrimaryDark
)

private val SandalwoodLight = lightColorScheme(
    primary = SandalwoodPrimaryLight,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    surface = SandalwoodSurfaceLight,
    onSurface = SandalwoodOnSurfaceLight,
    background = SandalwoodSurfaceLight,
    onBackground = SandalwoodOnSurfaceLight,
    primaryContainer = SandalwoodPrimaryLight.copy(alpha = 0.15f),
    onPrimaryContainer = SandalwoodPrimaryLight,
    surfaceVariant = SandalwoodSurfaceLight,
    onSurfaceVariant = SandalwoodPrimaryLight
)
private val SandalwoodDark = darkColorScheme(
    primary = SandalwoodPrimaryDark,
    onPrimary = SandalwoodSurfaceDark,
    surface = SandalwoodSurfaceDark,
    onSurface = SandalwoodOnSurfaceDark,
    background = SandalwoodSurfaceDark,
    onBackground = SandalwoodOnSurfaceDark,
    primaryContainer = SandalwoodPrimaryDark.copy(alpha = 0.15f),
    onPrimaryContainer = SandalwoodPrimaryDark,
    surfaceVariant = SandalwoodSurfaceDark,
    onSurfaceVariant = SandalwoodPrimaryDark
)

private val PaleMossLight = lightColorScheme(
    primary = PaleMossPrimaryLight,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    surface = PaleMossSurfaceLight,
    onSurface = PaleMossOnSurfaceLight,
    background = PaleMossSurfaceLight,
    onBackground = PaleMossOnSurfaceLight,
    primaryContainer = PaleMossPrimaryLight.copy(alpha = 0.15f),
    onPrimaryContainer = PaleMossPrimaryLight,
    surfaceVariant = PaleMossSurfaceLight,
    onSurfaceVariant = PaleMossPrimaryLight
)
private val PaleMossDark = darkColorScheme(
    primary = PaleMossPrimaryDark,
    onPrimary = PaleMossSurfaceDark,
    surface = PaleMossSurfaceDark,
    onSurface = PaleMossOnSurfaceDark,
    background = PaleMossSurfaceDark,
    onBackground = PaleMossOnSurfaceDark,
    primaryContainer = PaleMossPrimaryDark.copy(alpha = 0.15f),
    onPrimaryContainer = PaleMossPrimaryDark,
    surfaceVariant = PaleMossSurfaceDark,
    onSurfaceVariant = PaleMossPrimaryDark
)

private val RhododendronLight = lightColorScheme(
    primary = RhododendronPrimaryLight,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    surface = RhododendronSurfaceLight,
    onSurface = RhododendronOnSurfaceLight,
    background = RhododendronSurfaceLight,
    onBackground = RhododendronOnSurfaceLight,
    primaryContainer = RhododendronPrimaryLight.copy(alpha = 0.15f),
    onPrimaryContainer = RhododendronPrimaryLight,
    surfaceVariant = RhododendronSurfaceLight,
    onSurfaceVariant = RhododendronPrimaryLight
)
private val RhododendronDark = darkColorScheme(
    primary = RhododendronPrimaryDark,
    onPrimary = RhododendronSurfaceDark,
    surface = RhododendronSurfaceDark,
    onSurface = RhododendronOnSurfaceDark,
    background = RhododendronSurfaceDark,
    onBackground = RhododendronOnSurfaceDark,
    primaryContainer = RhododendronPrimaryDark.copy(alpha = 0.15f),
    onPrimaryContainer = RhododendronPrimaryDark,
    surfaceVariant = RhododendronSurfaceDark,
    onSurfaceVariant = RhododendronPrimaryDark
)

private val VioletHazeLight = lightColorScheme(
    primary = VioletHazePrimaryLight,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    surface = VioletHazeSurfaceLight,
    onSurface = VioletHazeOnSurfaceLight,
    background = VioletHazeSurfaceLight,
    onBackground = VioletHazeOnSurfaceLight,
    primaryContainer = VioletHazePrimaryLight.copy(alpha = 0.15f),
    onPrimaryContainer = VioletHazePrimaryLight,
    surfaceVariant = VioletHazeSurfaceLight,
    onSurfaceVariant = VioletHazePrimaryLight
)
private val VioletHazeDark = darkColorScheme(
    primary = VioletHazePrimaryDark,
    onPrimary = VioletHazeSurfaceDark,
    surface = VioletHazeSurfaceDark,
    onSurface = VioletHazeOnSurfaceDark,
    background = VioletHazeSurfaceDark,
    onBackground = VioletHazeOnSurfaceDark,
    primaryContainer = VioletHazePrimaryDark.copy(alpha = 0.15f),
    onPrimaryContainer = VioletHazePrimaryDark,
    surfaceVariant = VioletHazeSurfaceDark,
    onSurfaceVariant = VioletHazePrimaryDark
)

private val DefaultDarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val DefaultLightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun MyApplicationTheme(
    selectedPalette: ThemePalette = ThemePalette.SANDALWOOD,
    isDarkMode: Boolean = false,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colorScheme = when (selectedPalette) {
        ThemePalette.DYNAMIC -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (isDarkMode) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            } else {
                if (isDarkMode) VioletHazeDark else VioletHazeLight
            }
        }
        ThemePalette.MISTY_ROSE -> if (isDarkMode) MistyRoseDark else MistyRoseLight
        ThemePalette.SMOKY_BLUE -> if (isDarkMode) SmokyBlueDark else SmokyBlueLight
        ThemePalette.SANDALWOOD -> if (isDarkMode) SandalwoodDark else SandalwoodLight
        ThemePalette.PALE_MOSS -> if (isDarkMode) PaleMossDark else PaleMossLight
        ThemePalette.RHODODENDRON -> if (isDarkMode) RhododendronDark else RhododendronLight
        ThemePalette.VIOLET_HAZE -> if (isDarkMode) VioletHazeDark else VioletHazeLight
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
