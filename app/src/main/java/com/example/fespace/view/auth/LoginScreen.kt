package com.example.fespace.view.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fespace.ui.components.PrimaryButton
import com.example.fespace.ui.theme.*
import com.example.fespace.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    onLoginSuccess: (String, Int) -> Unit,
    onRegisterClick: () -> Unit,
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val isButtonEnabled = viewModel.isLoginValid(email, password) && !isLoading
    
    // Error Dialog
    if (showErrorDialog) {
        com.example.fespace.ui.components.ErrorDialog(
            title = "Login Gagal",
            message = "Email atau password yang Anda masukkan salah. Silakan coba lagi.",
            onDismiss = { showErrorDialog = false }
        )
    }

    Scaffold(
        containerColor = DarkCharcoal,
        topBar = {
            TopAppBar(
                title = { Text("Masuk", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkCharcoal,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(Spacing.Large),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            Spacer(modifier = Modifier.height(Spacing.Large))
            
            // Welcome Header with Elegant Theme
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally),
                tint = Terracotta
            )
            
            Spacer(modifier = Modifier.height(Spacing.Large))
            
            Text(
                text = "Selamat Datang Kembali",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            
            Text(
                text = "Masuk untuk melanjutkan proyek Anda",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            
            Spacer(modifier = Modifier.height(Spacing.Large))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                singleLine = true,
                shape = RoundedCornerShape(Radius.Medium),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Terracotta,
                    focusedLabelColor = Terracotta,
                    focusedLeadingIconColor = Terracotta,
                    cursorColor = Terracotta,
                    unfocusedBorderColor = AccentGold.copy(alpha = 0.5f),
                    unfocusedLabelColor = TextSecondary,
                    unfocusedLeadingIconColor = TextSecondary,
                    unfocusedTextColor = TextPrimary,
                    focusedTextColor = TextPrimary
                )
            )

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Sembunyikan password" else "Tampilkan password",
                            tint = AccentGold
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                singleLine = true,
                shape = RoundedCornerShape(Radius.Medium),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Terracotta,
                    focusedLabelColor = Terracotta,
                    focusedLeadingIconColor = Terracotta,
                    cursorColor = Terracotta,
                    unfocusedBorderColor = AccentGold.copy(alpha = 0.5f),
                    unfocusedLabelColor = TextSecondary,
                    unfocusedLeadingIconColor = TextSecondary,
                    unfocusedTextColor = TextPrimary,
                    focusedTextColor = TextPrimary
                )
            )
            
            Spacer(modifier = Modifier.height(Spacing.Small))

            // Login Button using custom component
            PrimaryButton(
                text = "Masuk",
                onClick = {
                    isLoading = true
                    viewModel.login(email, password) { user ->
                        isLoading = false
                        if (user != null) {
                            onLoginSuccess(user.role, user.idUser)
                        } else {
                            showErrorDialog = true
                        }
                    }
                },
                enabled = isButtonEnabled,
                loading = isLoading
            )

            Spacer(modifier = Modifier.height(Spacing.Medium))

            // Register Link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Belum punya akun?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                TextButton(onClick = onRegisterClick) {
                    Text(
                        "Daftar di sini",
                        fontWeight = FontWeight.Bold,
                        color = AccentGold
                    )
                }
            }
        }
    }
}
