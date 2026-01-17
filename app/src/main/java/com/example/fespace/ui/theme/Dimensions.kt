package com.example.fespace.ui.theme

import androidx.compose.ui.unit.dp

// Consistent spacing scale across the app
object Spacing {
    val ExtraSmall = 4.dp
    val Small = 8.dp
    val Medium = 16.dp
    val Large = 24.dp
    val ExtraLarge = 32.dp
    val ExtraExtraLarge = 48.dp
}

// Card & Component Radius - Organic Rounded (Elegant Homey)
object Radius {
    val Small = 12.dp      // Buttons, chips
    val Medium = 16.dp     // Standard cards
    val Large = 20.dp      // Hero cards, images
    val ExtraLarge = 24.dp // Modals, dialogs
}

// Elevation - Subtle with warm glow
object Elevation {
    val None = 0.dp
    val Low = 2.dp         // Subtle cards
    val Card = 4.dp        // Standard cards
    val Modal = 8.dp       // Floating elements
    val High = 12.dp       // Emphasized elements
}
