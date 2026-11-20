package com.pocketsheet.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DndGold,
    onPrimary = Color.Black,
    secondary = DndCrimsonLight,
    onSecondary = Color.White,
    background = DungeonBg,
    surface = DungeonSurface,
    onBackground = DungeonTextPrimary,
    onSurface = DungeonTextPrimary,
    outline = DungeonBorder
)

private val LightColorScheme = lightColorScheme(
    primary = DndCrimson,
    onPrimary = Color.White,
    secondary = DndGold,
    onSecondary = InkTextPrimary,
    background = ParchmentBg,
    surface = ParchmentCard,
    onBackground = InkTextPrimary,
    onSurface = InkTextPrimary,
    outline = ParchmentBorder
)

@Composable
fun PocketSheetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
