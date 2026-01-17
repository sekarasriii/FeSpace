package com.example.fespace.view.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fespace.data.local.entity.OrderEntity
import com.example.fespace.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PremiumMetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    gradient: Brush,
    icon: ImageVector,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .height(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Cream.copy(alpha = 0.25f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = Cream, modifier = Modifier.size(24.dp))
                }
                
                Column {
                    Text(title, fontSize = 13.sp, color = Cream.copy(alpha = 0.85f), fontWeight = FontWeight.Medium)
                    Spacer(Modifier.height(4.dp))
                    Text(value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Cream)
                    Spacer(Modifier.height(4.dp))
                    Text(subtitle, fontSize = 12.sp, color = Cream.copy(alpha = 0.75f))
                }
            }
        }
    }
}

@Composable
fun ElegantActivityItem(
    order: OrderEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val timeAgo = getTimeAgo(order.createAt)
    val statusUpper = order.status.uppercase()
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            when {
                                statusUpper.contains("PENDING") -> StatusPending.copy(alpha = 0.15f)
                                statusUpper.contains("APPROVED") -> StatusApproved.copy(alpha = 0.15f)
                                statusUpper.contains("INDESIGN") || statusUpper.contains("DESIGN") -> StatusInDesign.copy(alpha = 0.15f)
                                else -> Ivory.copy(alpha = 0.15f)
                            },
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        when {
                            statusUpper.contains("PENDING") -> Icons.Default.ShoppingCart
                            statusUpper.contains("APPROVED") -> Icons.Default.CheckCircle
                            statusUpper.contains("INDESIGN") || statusUpper.contains("DESIGN") -> Icons.Default.Create
                            else -> Icons.Default.Info
                        },
                        contentDescription = null,
                        tint = when {
                            statusUpper.contains("PENDING") -> StatusPending
                            statusUpper.contains("APPROVED") -> StatusApproved
                            statusUpper.contains("INDESIGN") || statusUpper.contains("DESIGN") -> StatusInDesign
                            else -> Ivory
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Column {
                    Text("Order #${order.idOrders}", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Text(timeAgo, fontSize = 13.sp, color = TextSecondary)
                }
            }
            
            com.example.fespace.ui.components.StatusBadge(status = order.status)
        }
    }
}

@Composable
fun MenuOption(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Terracotta.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Terracotta, modifier = Modifier.size(24.dp))
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Text(subtitle, fontSize = 13.sp, color = TextSecondary)
            }
            
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = TextSecondary)
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick).padding(8.dp)
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = if (selected) Terracotta else TextSecondary,
            modifier = Modifier.size(24.dp)
        )
        if (selected) {
            Spacer(Modifier.height(4.dp))
            Box(modifier = Modifier.size(6.dp).background(Terracotta, CircleShape))
        }
    }
}

fun getTimeAgo(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    return when {
        diff < 60000 -> "Baru saja"
        diff < 3600000 -> "${diff / 60000} menit lalu"
        diff < 86400000 -> "${diff / 3600000} jam lalu"
        diff < 172800000 -> "1 hari lalu"
        diff < 604800000 -> "${diff / 86400000} hari lalu"
        else -> {
            val dateFormat = SimpleDateFormat("d MMM yyyy", Locale("id", "ID"))
            dateFormat.format(Date(timestamp))
        }
    }
}
