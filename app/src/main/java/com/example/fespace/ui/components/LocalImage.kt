package com.example.fespace.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fespace.ui.theme.*
import com.example.fespace.utils.FileUtils
import com.example.fespace.utils.ImageUtils

/**
 * Local Image Display Component
 * Loads and displays image from file path
 * Shows PDF icon for PDF files
 * Shows placeholder if image not found
 */
@Composable
fun LocalImage(
    imagePath: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    showPlaceholder: Boolean = true,
    clickable: Boolean = true // Enable clicking to open file
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    
    // Create click modifier if clickable and path exists
    val clickModifier = if (clickable && imagePath != null) {
        modifier.clickable { 
            FileUtils.openFile(context, imagePath)
        }
    } else {
        modifier
    }
    // Check if file is PDF
    if (FileUtils.isPdf(imagePath)) {
        // Show PDF indicator
        Box(
            modifier = clickModifier
                .clip(RoundedCornerShape(Radius.Medium))
                .background(Color(0xFFFFF3E0))
                .border(
                    width = 1.dp,
                    color = Color(0xFFFF9800),
                    shape = RoundedCornerShape(Radius.Medium)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.Small)
            ) {
                Icon(
                    Icons.Default.PictureAsPdf,
                    contentDescription = "PDF Document",
                    modifier = Modifier.size(48.dp),
                    tint = Color(0xFFD32F2F)
                )
                Text(
                    "PDF Document",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFFD32F2F),
                    textAlign = TextAlign.Center
                )
                imagePath?.substringAfterLast('/')?.let { fileName ->
                    Text(
                        fileName,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }
        }
    } else {
        // Try to load as image
        val imageBitmap = ImageUtils.loadImageFromPath(imagePath)
        
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap,
                contentDescription = contentDescription,
                modifier = clickModifier
                    .clip(RoundedCornerShape(Radius.Medium)),
                contentScale = contentScale
            )
        } else if (showPlaceholder) {
            // Placeholder when no image
            Box(
                modifier = clickModifier
                    .clip(RoundedCornerShape(Radius.Medium))
                    .background(Gray100)
                    .border(
                        width = 1.dp,
                        color = Gray300,
                        shape = RoundedCornerShape(Radius.Medium)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall)
                ) {
                    Icon(
                        Icons.Default.BrokenImage,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Gray400
                    )
                    Text(
                        "No Image",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextTertiary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
