package com.example.fespace.view.admin

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ManageServiceScreen() {
    Scaffold { padding ->
        Text(
            text = "Manage Service",
            modifier = Modifier.padding(padding)
        )
    }
}