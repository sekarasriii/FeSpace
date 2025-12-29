package com.example.fespace.view.client

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp

@Composable
fun OrderServiceScreen(
    onOrderSuccess: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {

        Text(text = "Order Service")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // simulasi order berhasil
                onOrderSuccess()
            }
        ) {
            Text("Submit Order")
        }
    }
}
