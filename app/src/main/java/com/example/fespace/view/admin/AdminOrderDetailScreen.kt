package com.example.fespace.view.admin

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fespace.ui.components.LocalImage
import com.example.fespace.ui.theme.*
import com.example.fespace.utils.FileUtils
import com.example.fespace.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrderDetailScreen(
    orderId: Int,
    adminViewModel: AdminViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { com.example.fespace.utils.SessionManager(context) }
    val currentAdminId = sessionManager.getUserId()
    
    // Reactive state
    val order by adminViewModel.getOrderById(orderId).collectAsStateWithLifecycle(initialValue = null)
    
    // Get client info - properly handle Flow
    val client by produceState<com.example.fespace.data.local.entity.UserEntity?>(initialValue = null, order) {
        order?.idClient?.let { clientId ->
            adminViewModel.getClientById(clientId).collect { userData ->
                value = userData
            }
        }
    }
    
    // File picker for design upload
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            val fileSizeMB = FileUtils.getFileSizeMB(context, it)
            if (fileSizeMB > 10) {
                Toast.makeText(context, "File terlalu besar! Maksimal 10MB", Toast.LENGTH_LONG).show()
                return@rememberLauncherForActivityResult
            }
            
            val savedPath = FileUtils.saveFileToInternalStorage(context, it)
            if (savedPath != null) {
                order?.let { currentOrder ->
                    adminViewModel.updateOrderDesign(currentOrder, savedPath, currentAdminId)
                }
                Toast.makeText(context, "Desain berhasil diupload", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Gagal upload. Pastikan file JPG/PNG/PDF dan < 10MB", Toast.LENGTH_LONG).show()
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Kelola Pesanan",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkCharcoal,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary
                )
            )
        },
        containerColor = DarkCharcoal
    ) { padding ->
        if (order == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Terracotta)
            }
        } else {
            val currentOrder = order!!
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // === ADMIN HERO SECTION ===
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    DarkSurface,
                                    DarkCharcoal
                                )
                            )
                        )
                        .padding(Spacing.Large)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
                    ) {
                        // Order ID + Client Name
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "Order #${orderId}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                client?.let {
                                    Text(
                                        "Client: ${it.nameUser}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = TextSecondary
                                    )
                                }
                            }
                            
                            // Status Badge
                            val statusColor = when (currentOrder.status.lowercase()) {
                                "pending" -> StatusPending
                                "approved" -> StatusApproved
                                "survey" -> StatusSurvey
                                "indesign" -> StatusInDesign
                                "final" -> StatusFinal
                                "completed" -> StatusCompleted
                                "rejected" -> StatusCancelled
                                else -> TextTertiary
                            }
                            
                            Surface(
                                shape = RoundedCornerShape(Radius.Medium),
                                color = statusColor.copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    when (currentOrder.status.lowercase()) {
                                        "pending" -> "Menunggu"
                                        "approved" -> "Disetujui"
                                        "survey" -> "Survey"
                                        "indesign" -> "Desain"
                                        "final" -> "Final"
                                        "completed" -> "Selesai"
                                        "rejected" -> "Ditolak"
                                        else -> currentOrder.status.uppercase()
                                    },
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    color = statusColor,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        // Service Info
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Architecture,
                                contentDescription = null,
                                tint = AccentGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                "Service ID: ${currentOrder.idServices}",
                                style = MaterialTheme.typography.titleLarge,
                                color = AccentGold
                            )
                        }
                    }
                }
                
                // === CONTENT SECTION ===
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.Large),
                    verticalArrangement = Arrangement.spacedBy(Spacing.Large)
                ) {
                    // Quick Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
                    ) {
                        // Contact Client Button
                        Button(
                            onClick = {
                                client?.whatsappNumber?.let { phone ->
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW)
                                        val message = "Halo ${client?.nameUser}, saya Admin FeSpace terkait pesanan #$orderId"
                                        val url = "https://wa.me/$phone?text=${Uri.encode(message)}"
                                        intent.data = Uri.parse(url)
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "WhatsApp tidak terinstall", Toast.LENGTH_SHORT).show()
                                    }
                                } ?: run {
                                    Toast.makeText(context, "Nomor WhatsApp client tidak tersedia", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.weight(1f).height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SageGreen,
                                contentColor = DarkCharcoal
                            ),
                            shape = RoundedCornerShape(Radius.Medium),
                            enabled = client?.whatsappNumber != null
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Hubungi", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        }
                        
                        // Update Status Button
                        var showStatusDialog by remember { mutableStateOf(false) }
                        Button(
                            onClick = { showStatusDialog = true },
                            modifier = Modifier.weight(1f).height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                            shape = RoundedCornerShape(Radius.Medium)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Update Status", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        }
                        
                        // Status Update Dialog
                        if (showStatusDialog) {
                            StatusUpdateDialog(
                                currentStatus = currentOrder.status,
                                onDismiss = { showStatusDialog = false },
                                onConfirm = { newStatus ->
                                    adminViewModel.updateOrderStatus(currentOrder, newStatus)
                                    showStatusDialog = false
                                    Toast.makeText(context, "Status berhasil diupdate", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                    
                    // Client Information Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        shape = RoundedCornerShape(Radius.Large),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(Spacing.Large),
                            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Copper,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    "Informasi Client",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            
                            HorizontalDivider(color = Copper.copy(alpha = 0.2f))
                            
                            client?.let { clientData ->
                                AdminDetailRow(
                                    icon = Icons.Default.Person,
                                    label = "Nama",
                                    value = clientData.nameUser
                                )
                                AdminDetailRow(
                                    icon = Icons.Default.Email,
                                    label = "Email",
                                    value = clientData.email
                                )
                                clientData.whatsappNumber?.let { phone ->
                                    AdminDetailRow(
                                        icon = Icons.Default.Phone,
                                        label = "WhatsApp",
                                        value = phone
                                    )
                                }
                            }
                            
                            AdminDetailRow(
                                icon = Icons.Default.LocationOn,
                                label = "Lokasi Proyek",
                                value = currentOrder.locationAddress
                            )
                            AdminDetailRow(
                                icon = Icons.Default.AttachMoney,
                                label = "Budget",
                                value = com.example.fespace.utils.RupiahFormatter.formatToRupiah(currentOrder.budget),
                                valueColor = AccentGold
                            )
                        }
                    }
                    
                    // Design Upload Section
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        shape = RoundedCornerShape(Radius.Large),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(Spacing.Large),
                            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
                            ) {
                                Icon(
                                    Icons.Default.Upload,
                                    contentDescription = null,
                                    tint = Terracotta,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    "Upload Hasil Desain",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            
                            if (currentOrder.designPath != null) {
                                LocalImage(
                                    imagePath = currentOrder.designPath,
                                    contentDescription = "Design Preview",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(220.dp)
                                        .clip(RoundedCornerShape(Radius.Medium)),
                                    showPlaceholder = true,
                                    clickable = true
                                )
                                
                                // Preview/Share Buttons for uploaded design
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
                                ) {
                                    OutlinedButton(
                                        onClick = { 
                                            com.example.fespace.utils.FileDownloadHelper.openFile(
                                                context, 
                                                currentOrder.designPath
                                            )
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = Terracotta
                                        )
                                    ) {
                                        Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text("Preview")
                                    }
                                    
                                    OutlinedButton(
                                        onClick = { 
                                            com.example.fespace.utils.FileDownloadHelper.shareFile(
                                                context, 
                                                currentOrder.designPath
                                            )
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = Terracotta
                                        )
                                    ) {
                                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text("Share")
                                    }
                                }
                            }
                            
                            Button(
                                onClick = { 
                                    launcher.launch(arrayOf("image/jpeg", "image/png", "application/pdf"))
                                },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                                shape = RoundedCornerShape(Radius.Medium)
                            ) {
                                Icon(Icons.Default.Upload, contentDescription = null)
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    if (currentOrder.designPath != null) "Ganti Desain" else "Upload Desain",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            
                            Text(
                                "Format: JPG, PNG, PDF • Max: 10MB",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary.copy(alpha = 0.7f)
                            )
                        }
                    }
                    
                    // Client Document View
                    if (currentOrder.clientDocumentPath != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            shape = RoundedCornerShape(Radius.Large),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(Spacing.Large),
                                verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
                                ) {
                                    Icon(
                                        Icons.Default.Description,
                                        contentDescription = null,
                                        tint = SageGreen,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        "Dokumen dari Client",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                
                                LocalImage(
                                    imagePath = currentOrder.clientDocumentPath,
                                    contentDescription = "Client Document",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .clip(RoundedCornerShape(Radius.Medium)),
                                    showPlaceholder = true,
                                    clickable = true
                                )
                                
                                // Download/Preview Buttons
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
                                ) {
                                    OutlinedButton(
                                        onClick = { 
                                            com.example.fespace.utils.FileDownloadHelper.openFile(
                                                context, 
                                                currentOrder.clientDocumentPath
                                            )
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = SageGreen
                                        )
                                    ) {
                                        Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text("Preview")
                                    }
                                    
                                    Button(
                                        onClick = { 
                                            com.example.fespace.utils.FileDownloadHelper.shareFile(
                                                context, 
                                                currentOrder.clientDocumentPath
                                            )
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = SageGreen,
                                            contentColor = DarkCharcoal
                                        )
                                    ) {
                                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text("Download")
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(Modifier.height(Spacing.ExtraLarge))
                }
            }
        }
    }
}

@Composable
private fun AdminDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    valueColor: Color = TextPrimary
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Copper,
            modifier = Modifier.size(20.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary.copy(alpha = 0.7f)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                value,
                style = MaterialTheme.typography.bodyLarge,
                color = valueColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatusUpdateDialog(
    currentStatus: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    // Status workflow: pending → approved → survey → indesign → revision → final → completed/rejected
    val statusOptions = listOf(
        "pending",
        "approved", 
        "survey",
        "indesign",
        "revision",
        "final",
        "completed",
        "rejected"
    )
    
    var selectedStatus by remember { mutableStateOf(currentStatus) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                "Update Status Pesanan",
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.Small)
            ) {
                Text(
                    "Status saat ini: ${currentStatus.uppercase()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                
                Spacer(Modifier.height(Spacing.Small))
                
                Text(
                    "Pilih status baru:",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(Modifier.height(Spacing.ExtraSmall))
                
                // Status options
                statusOptions.forEach { status ->
                    val statusThemeColor = when (status.lowercase()) {
                        "pending" -> StatusPending
                        "approved" -> StatusApproved
                        "survey" -> StatusSurvey
                        "indesign" -> StatusInDesign
                        "final" -> StatusFinal
                        "completed" -> StatusCompleted
                        "rejected" -> StatusCancelled
                        else -> TextTertiary
                    }
                    
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(Radius.Small)),
                        color = if (selectedStatus == status) {
                            statusThemeColor.copy(alpha = 0.15f)
                        } else {
                            DarkGrayLight.copy(alpha = 0.3f)
                        },
                        border = if (selectedStatus == status) {
                            androidx.compose.foundation.BorderStroke(1.dp, statusThemeColor.copy(alpha = 0.3f))
                        } else null,
                        onClick = { selectedStatus = status }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Spacing.Medium),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                status.uppercase(),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (selectedStatus == status) FontWeight.Bold else FontWeight.Normal,
                                color = statusThemeColor
                            )
                            
                            if (selectedStatus == status) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = statusThemeColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    
                    Spacer(Modifier.height(Spacing.ExtraSmall))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selectedStatus) },
                colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                enabled = selectedStatus != currentStatus
            ) {
                Text("Update", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = TextSecondary)
            }
        },
        containerColor = DarkSurface,
        shape = RoundedCornerShape(Radius.Large)
    )
}
