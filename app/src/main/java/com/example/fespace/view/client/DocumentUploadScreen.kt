package com.example.fespace.view.client

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fespace.data.local.entity.OrderDocumentEntity
import com.example.fespace.viewmodel.ClientViewModel
import com.example.fespace.ui.theme.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentUploadScreen(
    orderId: Int,
    clientId: Int,
    clientViewModel: ClientViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var documents by remember { mutableStateOf<List<OrderDocumentEntity>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var selectedDocType by remember { mutableStateOf("location_photo") }
    var description by remember { mutableStateOf("") }
    var showUploadDialog by remember { mutableStateOf(false) }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf("") }

    // File picker launcher
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedFileUri = it
            selectedFileName = getFileName(context, it)
            showUploadDialog = true
        }
    }

    // Load documents
    LaunchedEffect(orderId) {
        // TODO: Load documents from database
        // documents = clientViewModel.getOrderDocuments(orderId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dokumen Pesanan") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkCharcoal,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = Terracotta
                )
            )
        },
        containerColor = DarkCharcoal,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { filePickerLauncher.launch("*/*") },
                icon = { Icon(Icons.Default.Add, "Upload") },
                text = { Text("Upload Dokumen") },
                containerColor = Terracotta,
                contentColor = Cream
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Document type info card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DarkSurface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Jenis Dokumen",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AccentGold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Foto Lokasi\n• Dokumen Client\n• Referensi Desain\n• Dokumen Lainnya",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            // Documents list
            if (documents.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Belum ada dokumen",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap tombol + untuk upload dokumen",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(documents) { doc ->
                        DocumentItem(
                            document = doc,
                            onDelete = {
                                scope.launch {
                                    // TODO: Delete document
                                    Toast.makeText(context, "Dokumen dihapus", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                }
            }
        }

        // Upload Dialog
        if (showUploadDialog && selectedFileUri != null) {
            AlertDialog(
                onDismissRequest = { showUploadDialog = false },
                title = { Text("Upload Dokumen", color = TextPrimary) },
                containerColor = DarkSurface,
                text = {
                    Column {
                        Text("File: $selectedFileName")
                        Spacer(modifier = Modifier.height(16.dp))

                        // Document type selector
                        Text("Jenis Dokumen:", style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        val docTypes = listOf(
                            "location_photo" to "Foto Lokasi",
                            "client_document" to "Dokumen Client",
                            "reference" to "Referensi Desain",
                            "other" to "Lainnya"
                        )

                        docTypes.forEach { (value, label) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = selectedDocType == value,
                                    onClick = { selectedDocType = value }
                                )
                                Text(label)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Deskripsi (opsional)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = Terracotta,
                                unfocusedBorderColor = Gray700,
                                cursorColor = Terracotta,
                                focusedLabelColor = Terracotta,
                                unfocusedLabelColor = TextSecondary
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            scope.launch {
                                isLoading = true
                                try {
                                    // Copy file to internal storage
                                    val fileName = "${System.currentTimeMillis()}_$selectedFileName"
                                    val file = java.io.File(context.filesDir, fileName)

                                    context.contentResolver.openInputStream(selectedFileUri!!)?.use { input ->
                                        java.io.FileOutputStream(file).use { output ->
                                            input.copyTo(output)
                                        }
                                    }

                                    // TODO: Save to database via ViewModel
                                    clientViewModel.uploadDocument(
                                        orderId = orderId,
                                        uploadedBy = clientId,
                                        filePath = file.absolutePath,
                                        fileName = selectedFileName,
                                        docType = selectedDocType,
                                        description = description.ifEmpty { null }
                                    )

                                    Toast.makeText(context, "Dokumen berhasil diupload", Toast.LENGTH_SHORT).show()
                                    showUploadDialog = false
                                    description = ""
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Gagal upload: ${e.message}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Terracotta,
                            contentColor = Cream
                        ),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = Cream
                            )
                        } else {
                            Text("Upload")
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showUploadDialog = false }) {
                        Text("Batal", color = TextSecondary)
                    }
                }
            )
        }
    }
}

@Composable
fun DocumentItem(
    document: OrderDocumentEntity,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (document.docType) {
                    "location_photo" -> Icons.Default.Photo
                    "client_document" -> Icons.Default.Description
                    "reference" -> Icons.Default.Image
                    else -> Icons.Default.AttachFile
                },
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = document.fileName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Text(
                    text = getDocTypeLabel(document.docType),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                document.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextTertiary
                    )
                }
            }

            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Hapus",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Dokumen?", color = TextPrimary) },
            text = { Text("Dokumen yang dihapus tidak dapat dikembalikan.", color = TextSecondary) },
            containerColor = DarkSurface,
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentRed,
                        contentColor = Cream
                    )
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal", color = TextSecondary)
                }
            }
        )
    }
}

fun getDocTypeLabel(docType: String): String {
    return when (docType) {
        "location_photo" -> "Foto Lokasi"
        "client_document" -> "Dokumen Client"
        "reference" -> "Referensi Desain"
        else -> "Lainnya"
    }
}

fun getFileName(context: android.content.Context, uri: Uri): String {
    var result = "unknown"
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (index >= 0) {
                    result = it.getString(index)
                }
            }
        }
    }
    if (result == "unknown") {
        result = uri.path ?: "unknown"
        val cut = result.lastIndexOf('/')
        if (cut != -1) {
            result = result.substring(cut + 1)
        }
    }
    return result
}
