package com.example.fespace.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fespace.ui.theme.*
import com.example.fespace.utils.ImageUtils

/**
 * Image Picker Button Component
 * Allows user to select image from gallery
 * Shows preview of selected image
 */
@Composable
fun ImagePickerButton(
    currentImagePath: String? = null,
    onImageSelected: (String?) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Pilih Gambar"
) {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var imagePath by remember { mutableStateOf(currentImagePath) }
    
    // Image picker launcher - using GetContent for broader file access
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            // Save to internal storage
            val savedPath = ImageUtils.saveImageToInternalStorage(context, it)
            imagePath = savedPath
            onImageSelected(savedPath)
        }
    }
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = TextSecondary,
            fontWeight = FontWeight.Medium
        )
        
        // Image preview or picker button
        if (imagePath != null) {
            // Show image preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                // Image
                ImageUtils.loadImageFromPath(imagePath)?.let { bitmap ->
                    Image(
                        bitmap = bitmap,
                        contentDescription = "Selected image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(Radius.Medium))
                            .border(
                                width = 1.dp,
                                color = Gray300,
                                shape = RoundedCornerShape(Radius.Medium)
                            ),
                        contentScale = ContentScale.Crop
                    )
                }
                
                // Remove button
                IconButton(
                    onClick = {
                        imagePath = null
                        selectedImageUri = null
                        onImageSelected(null)
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(Spacing.Small)
                        .background(
                            color = AccentRed.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(Radius.Small)
                        )
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Remove image",
                        tint = White
                    )
                }
            }
        } else {
            // Picker button
            OutlinedButton(
                onClick = {
                    launcher.launch("image/*") // Accept any image type
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(Radius.Medium),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = BrownWarm
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 1.5.dp,
                    brush = androidx.compose.ui.graphics.SolidColor(Gray300)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Spacing.Small)
                ) {
                    Icon(
                        Icons.Default.AddPhotoAlternate,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = BrownWarm
                    )
                    Text(
                        "Pilih Gambar",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}
