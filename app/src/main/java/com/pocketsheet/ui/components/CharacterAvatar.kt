package com.pocketsheet.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pocketsheet.ui.theme.DndCrimson
import com.pocketsheet.ui.theme.DndGold
import com.pocketsheet.ui.theme.DndLeather
import com.pocketsheet.ui.theme.ParchmentBorder
import com.pocketsheet.ui.theme.ParchmentHeader

@Composable
fun CharacterAvatar(
    imageUri: String?,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    shapeRadius: Dp = 12.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(4.dp, RoundedCornerShape(shapeRadius))
            .clip(RoundedCornerShape(shapeRadius))
            .border(2.dp, DndGold, RoundedCornerShape(shapeRadius)),
        contentAlignment = Alignment.Center
    ) {
        if (!imageUri.isNullOrBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUri)
                    .crossfade(true)
                    .build(),
                contentDescription = "Ritrattista dell'eroe",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Fantasy Spellbook / Grimoire placeholder in authentic D&D parchment and leather hues
            Canvas(modifier = Modifier.fillMaxSize()) {
                val brush = Brush.radialGradient(
                    colors = listOf(
                        ParchmentHeader,
                        Color(0xFFE2D4BC),
                        DndLeather
                    ),
                    center = Offset(this.size.width / 2f, this.size.height / 2f),
                    radius = this.size.width * 0.8f
                )
                drawRoundRect(
                    brush = brush,
                    size = this.size,
                    cornerRadius = CornerRadius(shapeRadius.toPx(), shapeRadius.toPx())
                )
                drawRoundRect(
                    color = DndGold.copy(alpha = 0.6f),
                    size = this.size,
                    cornerRadius = CornerRadius(shapeRadius.toPx(), shapeRadius.toPx()),
                    style = Stroke(width = 1.5.dp.toPx())
                )
            }
            Icon(
                imageVector = Icons.Default.AutoStories,
                contentDescription = null,
                tint = DndCrimson,
                modifier = Modifier.size(size * 0.52f)
            )
        }
    }
}
