package com.example.fespace.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fespace.ui.theme.*

/**
 * Status Badge Component
 * Shows order/project status with appropriate color
 */
@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val statusLower = status.lowercase()
    val (backgroundColor, textColor, label) = when {
        statusLower.contains("pending") -> Triple(StatusPending.copy(alpha = 0.15f), StatusPending, "PENDING")
        statusLower.contains("approved") -> Triple(StatusApproved.copy(alpha = 0.15f), StatusApproved, "APPROVED")
        statusLower.contains("indesign") || statusLower.contains("design") -> Triple(StatusInDesign.copy(alpha = 0.15f), StatusInDesign, "IN DESIGN")
        statusLower.contains("completed") || statusLower.contains("done") || statusLower.contains("selesai") -> Triple(StatusApproved.copy(alpha = 0.15f), StatusApproved, "COMPLETED")
        statusLower.contains("cancelled") || statusLower.contains("rejected") || statusLower.contains("ditolak") -> Triple(StatusCancelled.copy(alpha = 0.15f), StatusCancelled, "FAILED")
        statusLower.contains("review") -> Triple(StatusReview.copy(alpha = 0.15f), StatusReview, "REVIEW")
        else -> Triple(Gray700.copy(alpha = 0.15f), TextSecondary, status.uppercase())
    }
    
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Radius.Small),
        color = backgroundColor
    ) {
        Text(
            text = label,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

/**
 * Category Chip Component
 * Shows category tags for services/portfolios
 */
@Composable
fun CategoryChip(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = BeigeLight,
    textColor: Color = TextPrimary
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Radius.Small),
        color = backgroundColor
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

/**
 * Info Badge Component
 * Shows small informational badges (e.g., "New", "Popular")
 */
@Composable
fun InfoBadge(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = AccentGold,
    textColor: Color = White
) {
    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(Radius.Small)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Count Badge Component
 * Shows count in a circular badge (e.g., notification count)
 */
@Composable
fun CountBadge(
    count: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = AccentRed,
    textColor: Color = White
) {
    if (count > 0) {
        Box(
            modifier = modifier
                .size(20.dp)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (count > 99) "99+" else count.toString(),
                color = textColor,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
