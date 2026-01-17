package com.example.fespace.view.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fespace.data.local.entity.OrderEntity
import com.example.fespace.ui.theme.*

@Composable
fun StatusPieChart(orders: List<OrderEntity>, modifier: Modifier = Modifier) {
    val statusColors = mapOf(
        "PENDING" to StatusPending,
        "APPROVED" to StatusApproved,
        "SURVEY" to AccentBlue,
        "INDESIGN" to StatusInDesign,
        "REVISION" to AccentOrange,
        "FINAL" to Terracotta,
        "COMPLETED" to StatusApproved,
        "REJECTED" to StatusCancelled
    )

    val counts = orders.groupingBy { it.status.uppercase() }.eachCount()
    val total = orders.size.toFloat()
    
    if (total == 0f) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text("Tidak ada data", color = TextPrimary.copy(alpha = 0.5f))
        }
        return
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(150.dp)) {
            var startAngle = -90f
            counts.forEach { (status, count) ->
                val sweepAngle = (count / total) * 360f
                drawArc(
                    color = statusColors[status] ?: Color.LightGray,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 30.dp.toPx())
                )
                startAngle += sweepAngle
            }
        }
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${orders.size}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Proyek",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}
