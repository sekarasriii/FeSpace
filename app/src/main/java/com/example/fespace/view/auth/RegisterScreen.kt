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
import com.example.fespace.viewmodel.AuthViewModel
import com.example.fespace.ui.components.PrimaryButton
import com.example.fespace.ui.theme.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var whatsappNumber by remember { mutableStateOf("") }
    val selectedRole = "client" // Hardcoded - only clients can register
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    
    // Validation error states
    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var whatsappError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val isButtonEnabled = viewModel.isRegisterValid(name, email, password, whatsappNumber) && !isLoading
    
    // Success Dialog
    if (showSuccessDialog) {
        com.example.fespace.ui.components.SuccessDialog(
            title = "Registrasi Berhasil!",
            message = "Akun Anda telah berhasil dibuat. Silakan login untuk melanjutkan.",
            onDismiss = { 
                showSuccessDialog = false
                // Navigate to login screen
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
                }
            }
        )
    }
    
    // Error Dialog
    if (showErrorDialog) {
        com.example.fespace.ui.components.ErrorDialog(
            title = "Registrasi Gagal",
            message = errorMessage,
            onDismiss = { showErrorDialog = false }
        )
    }

    Scaffold(
        containerColor = DarkCharcoal,
        topBar = {
            TopAppBar(
                title = { Text("Daftar Akun Baru", fontWeight = FontWeight.Bold) },
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Text(
                text = "Bergabung dengan FeSpace",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AccentGold
            )
            Text(
                text = "Daftar sekarang dan mulai wujudkan hunian impian Anda",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Form Fields with Validation
            OutlinedTextField(
                value = name,
                onValueChange = { 
                    name = it
                    nameError = com.example.fespace.utils.InputValidator.validateName(it).second
                },
                label = { Text("Nama Lengkap") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = AccentGold)
                },
                supportingText = {
                    if (nameError.isNotEmpty()) {
                        Text(nameError, color = MaterialTheme.colorScheme.error)
                    } else {
                        Text("Harus diawali huruf, hanya boleh huruf", color = TextSecondary.copy(alpha = 0.7f))
                    }
                },
                isError = nameError.isNotEmpty() && name.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
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

            OutlinedTextField(
                value = email,
                onValueChange = { 
                    email = it
                    emailError = com.example.fespace.utils.InputValidator.validateEmail(it).second
                },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = AccentGold)
                },
                supportingText = {
                    if (emailError.isNotEmpty()) {
                        Text(emailError, color = MaterialTheme.colorScheme.error)
                    } else {
                        Text("Harus @gmail.com", color = TextSecondary.copy(alpha = 0.7f))
                    }
                },
                isError = emailError.isNotEmpty() && email.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
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

            OutlinedTextField(
                value = whatsappNumber,
                onValueChange = { input ->
                    // Only allow digits and limit to 12 characters
                    val digitsOnly = input.filter { it.isDigit() }
                    if (digitsOnly.length <= 12) {
                        whatsappNumber = digitsOnly
                        // Validate with formatted number (+62 prefix)
                        val formattedNumber = if (digitsOnly.isNotEmpty()) {
                            com.example.fespace.utils.InputValidator.formatWhatsAppNumber(digitsOnly)
                        } else {
                            ""
                        }
                        whatsappError = com.example.fespace.utils.InputValidator.validateWhatsApp(formattedNumber).second
                    }
                },
                label = { Text("Nomor WhatsApp") },
                leadingIcon = {
                    Icon(Icons.Default.Phone, contentDescription = null, tint = AccentGold)
                },
                prefix = { Text("+62 ", color = TextPrimary) },
                placeholder = { Text("81366359496", color = TextSecondary.copy(alpha = 0.5f)) },
                supportingText = {
                    if (whatsappError.isNotEmpty()) {
                        Text(whatsappError, color = MaterialTheme.colorScheme.error)
                    } else {
                        Text("10-12 digit, contoh: 81366359496", color = TextSecondary.copy(alpha = 0.7f))
                    }
                },
                isError = whatsappError.isNotEmpty() && whatsappNumber.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                ),
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

            OutlinedTextField(
                value = password,
                onValueChange = { 
                    password = it
                    passwordError = com.example.fespace.utils.InputValidator.validatePassword(it).second
                },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = AccentGold)
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
                supportingText = {
                    if (passwordError.isNotEmpty()) {
                        Text(passwordError, color = MaterialTheme.colorScheme.error)
                    } else {
                        Text("Min 8 karakter: huruf besar, kecil, angka, simbol", color = TextSecondary.copy(alpha = 0.7f))
                    }
                },
                isError = passwordError.isNotEmpty() && password.isNotEmpty(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
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

            // Info Card - Only Client Registration Allowed
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = DarkSurface
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = AccentGold
                    )
                    Column {
                        Text(
                            "Pendaftaran Client",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            "Anda akan terdaftar sebagai client. Admin tidak dapat mendaftar melalui aplikasi.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Register Button with error handling
            PrimaryButton(
                text = "Daftar Sekarang",
                onClick = {
                    isLoading = true
                    // Format WhatsApp number with +62 prefix before saving
                    val formattedWhatsApp = com.example.fespace.utils.InputValidator.formatWhatsAppNumber(whatsappNumber)
                    
                    viewModel.register(
                        name = name,
                        email = email,
                        pass = password,
                        role = selectedRole,
                        whatsapp = formattedWhatsApp,
                        onSuccess = {
                            isLoading = false
                            showSuccessDialog = true
                        },
                        onError = { error ->
                            isLoading = false
                            errorMessage = error
                            showErrorDialog = true
                        }
                    )
                },
                enabled = isButtonEnabled,
                loading = isLoading
            )

            // Login Link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Sudah punya akun?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                TextButton(onClick = { navController.navigate("login") }) {
                    Text("Masuk di sini", fontWeight = FontWeight.Bold, color = AccentGold)
                }
            }
        }
    }
}
