package com.example.fespace.view.common

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun ErrorView(message: String) {
    Text(text = message, color = MaterialTheme.colorScheme.error)
}