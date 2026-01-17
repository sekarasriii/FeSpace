package com.example.fespace.view.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.example.fespace.data.local.entity.PortfolioEntity
import com.example.fespace.data.local.entity.ServiceEntity
import com.example.fespace.ui.theme.*
import com.example.fespace.utils.RupiahFormatter
import java.io.File

@Composable
fun ServiceGridItem(
    service: ServiceEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Layanan?", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = { Text("Apakah Anda yakin ingin menghapus layanan \"${service.nameServices}\"?", color = TextSecondary) },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed, contentColor = Cream),
                    shape = RoundedCornerShape(Radius.Small)
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal", color = TextSecondary)
                }
            },
            containerColor = DarkSurface,
            shape = RoundedCornerShape(Radius.Large)
        )
    }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp)) {
                AsyncImage(
                    model = if (service.imagePath != null) File(service.imagePath) else null,
                    contentDescription = service.nameServices,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                Row(modifier = Modifier.align(Alignment.TopEnd).padding(4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Surface(modifier = Modifier.size(28.dp), shape = CircleShape, color = DarkCharcoal.copy(alpha = 0.7f), onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Cream, modifier = Modifier.padding(6.dp))
                    }
                    Surface(modifier = Modifier.size(28.dp), shape = CircleShape, color = DarkCharcoal.copy(alpha = 0.7f), onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Cream, modifier = Modifier.padding(6.dp))
                    }
                }
            }
            
            Column(modifier = Modifier.padding(12.dp)) {
                Text(service.nameServices, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                Text(RupiahFormatter.formatToRupiah(service.priceStart.toDouble()), fontSize = 12.sp, color = AccentGold, fontWeight = FontWeight.Bold)
                Text(service.category, fontSize = 10.sp, color = TextSecondary)
            }
        }
    }
}

@Composable
fun PortfolioGridItem(
    portfolio: PortfolioEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Portfolio?", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = { Text("Apakah Anda yakin ingin menghapus portfolio \"${portfolio.title}\"?", color = TextSecondary) },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed, contentColor = Cream),
                    shape = RoundedCornerShape(Radius.Small)
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal", color = TextSecondary)
                }
            },
            containerColor = DarkSurface,
            shape = RoundedCornerShape(Radius.Large)
        )
    }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp)) {
                AsyncImage(
                    model = if (portfolio.imagePath != null) File(portfolio.imagePath) else null,
                    contentDescription = portfolio.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                Row(modifier = Modifier.align(Alignment.TopEnd).padding(4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Surface(modifier = Modifier.size(28.dp), shape = CircleShape, color = DarkCharcoal.copy(alpha = 0.7f), onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Cream, modifier = Modifier.padding(6.dp))
                    }
                    Surface(modifier = Modifier.size(28.dp), shape = CircleShape, color = DarkCharcoal.copy(alpha = 0.7f), onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Cream, modifier = Modifier.padding(6.dp))
                    }
                }
            }
            
            Column(modifier = Modifier.padding(12.dp)) {
                Text(portfolio.title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                Text("${portfolio.category} • ${portfolio.year}", fontSize = 10.sp, color = TextSecondary)
            }
        }
    }
}
