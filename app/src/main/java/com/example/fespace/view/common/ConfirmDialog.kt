package com.example.fespace.view.common

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Ya") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Tidak") }
        },
        title = { Text(title) },
        text = { Text(message) }
    )
}
