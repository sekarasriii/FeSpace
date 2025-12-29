package com.example.fespace.view.client

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun OrderStatusScreen() {
    Scaffold { padding ->
        Text(
            text = "Order Status Screen",
            modifier = androidx.compose.ui.Modifier.padding(padding)
        )
    }
}