package com.example.fespace.view.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fespace.ui.theme.*
import com.example.fespace.viewmodel.AdminViewModel

@Composable
fun AdminProfileDialog(
    adminViewModel: AdminViewModel,
    onDismiss: () -> Unit
) {
    val admin by adminViewModel.adminProfile.collectAsState(initial = null)
    var name by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    
    LaunchedEffect(admin) {
        admin?.let {
            name = it.nameUser
            whatsapp = it.whatsappNumber ?: ""
            email = it.email
        }
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Profil Admin", fontWeight = FontWeight.Bold, color = TextPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama", color = AccentGold) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedLabelColor = AccentGold,
                        unfocusedLabelColor = TextSecondary,
                        focusedBorderColor = Terracotta,
                        unfocusedBorderColor = AccentGold.copy(alpha = 0.5f)
                    )
                )
                
                OutlinedTextField(
                    value = email,
                    onValueChange = { },
                    label = { Text("Email", color = AccentGold) },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary.copy(alpha = 0.5f),
                        unfocusedTextColor = TextPrimary.copy(alpha = 0.5f),
                        focusedLabelColor = AccentGold,
                        unfocusedLabelColor = TextSecondary,
                        focusedBorderColor = Terracotta,
                        unfocusedBorderColor = AccentGold.copy(alpha = 0.5f)
                    )
                )
                
                OutlinedTextField(
                    value = whatsapp,
                    onValueChange = { whatsapp = it },
                    label = { Text("Nomor WhatsApp", color = AccentGold) },
                    placeholder = { Text("+628...", color = TextSecondary.copy(alpha = 0.5f)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedLabelColor = AccentGold,
                        unfocusedLabelColor = TextSecondary,
                        focusedBorderColor = Terracotta,
                        unfocusedBorderColor = AccentGold.copy(alpha = 0.5f)
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    admin?.let { 
                        adminViewModel.updateAdminProfile(it.copy(
                            nameUser = name, 
                            whatsappNumber = whatsapp, 
                            updateAt = System.currentTimeMillis()
                        )) 
                    }
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Terracotta,
                    contentColor = Cream
                ),
                shape = RoundedCornerShape(Radius.Medium)
            ) {
                Text("Simpan Perubahan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal", color = TextSecondary) }
        },
        containerColor = DarkSurface,
        shape = RoundedCornerShape(24.dp)
    )
}
