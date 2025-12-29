package com.example.fespace.view.admin

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fespace.data.local.entity.ServiceEntity
import com.example.fespace.viewmodel.AdminViewModel
import kotlinx.coroutines.launch
import com.example.fespace.data.local.entity.PortfolioEntity
import androidx.compose.material3.MenuAnchorType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    adminViewModel: AdminViewModel
) {
    // State Data dari ViewModel (StateFlow)
    val services by adminViewModel.services.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // State UI
    var selectedMenu by remember { mutableStateOf("Dashboard") }
    var showServiceDialog by remember { mutableStateOf(false) }
    var selectedService by remember { mutableStateOf<ServiceEntity?>(null) }

    //portofolio
    var showPortfolioDialog by remember { mutableStateOf(false) }
    var selectedPortfolio by remember { mutableStateOf<PortfolioEntity?>(null) }
    val portfolios by adminViewModel.portfolios.collectAsStateWithLifecycle()

    // Dialog Input/Edit Service
    if (showServiceDialog) {
        ServiceFormDialog(
            service = selectedService,
            onDismiss = {
                showServiceDialog = false
                selectedService = null
            },
            onConfirm = { service ->
                if (selectedService == null) {
                    // Tambah Baru
                    adminViewModel.addService(
                        name = service.nameServices,
                        desc = service.description,
                        price = service.priceStart,
                        duration = service.durationEstimate,
                        features = service.features ?: "",
                        adminId = 1 // Contoh ID Admin
                    )
                    Toast.makeText(context, "Layanan Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                } else {
                    // Update
                    adminViewModel.updateService(service)
                    Toast.makeText(context, "Layanan Berhasil Diperbarui", Toast.LENGTH_SHORT).show()
                }
                showServiceDialog = false
                selectedService = null
            }
        )
    }

    // Dialog Input/Edit Portfolio
    if (showPortfolioDialog) {
        PortfolioFormDialog(
            portfolio = selectedPortfolio,
            onDismiss = {
                showPortfolioDialog = false
                selectedPortfolio = null
            },
            onConfirm = { title, desc, category, year ->
                if (selectedPortfolio == null) {
                    // Tambah Baru
                    adminViewModel.addPortfolio(
                        title = title,
                        desc = desc,
                        category = category,
                        year = year,
                        adminId = 1 // Contoh ID Admin
                    )
                    Toast.makeText(context, "Portfolio Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                } else {
                    // Update - Buat objek entity baru dari data input
                    val updatedPortfolio = selectedPortfolio!!.copy(
                        title = title,
                        description = desc,
                        category = category,
                        year = year,
                        updateAt = System.currentTimeMillis()
                    )
                    adminViewModel.updatePortfolio(updatedPortfolio)
                    Toast.makeText(context, "Portfolio Berhasil Diperbarui", Toast.LENGTH_SHORT).show()
                }
                showPortfolioDialog = false
                selectedPortfolio = null
            }
        )
    }

    // State Konfirmasi Hapus
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<Any?>(null) } // Bisa ServiceEntity atau PortfolioEntity

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Konfirmasi Hapus") },
            text = { Text("Apakah Anda yakin ingin menghapus data ini? Tindakan ini tidak dapat dibatalkan.") },
            confirmButton = {
                Button(
                    onClick = {
                        when (val item = itemToDelete) {
                            is ServiceEntity -> {
                                adminViewModel.deleteService(item)
                                Toast.makeText(context, "Layanan berhasil dihapus", Toast.LENGTH_SHORT).show()
                            }
                            is PortfolioEntity -> {
                                adminViewModel.deletePortfolio(item)
                                Toast.makeText(context, "Portfolio berhasil dihapus", Toast.LENGTH_SHORT).show()
                            }
                        }
                        showDeleteConfirm = false
                        itemToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Hapus") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Batal") }
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text("FeSpace Admin", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                HorizontalDivider()

                NavigationDrawerItem(
                    label = { Text("Dashboard") },
                    selected = selectedMenu == "Dashboard",
                    onClick = { selectedMenu = "Dashboard"; scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Default.Home, null) }
                )
                NavigationDrawerItem(
                    label = { Text("Kelola Layanan") },
                    selected = selectedMenu == "Services",
                    onClick = { selectedMenu = "Services"; scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Default.Settings, null) }
                )
                NavigationDrawerItem(
                    label = { Text("Portfolio") },
                    selected = selectedMenu == "Portfolio",
                    onClick = { selectedMenu = "Portfolio"; scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Default.Image, null) }
                )
                NavigationDrawerItem(
                    label = { Text("Pesanan Masuk") },
                    selected = selectedMenu == "Orders",
                    onClick = { selectedMenu = "Orders"; scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Default.ShoppingCart, null) }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(selectedMenu) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, null)
                        }
                    }
                )
            },
            floatingActionButton = {
                if (selectedMenu == "Services" || selectedMenu == "Portfolio") {
                    FloatingActionButton(onClick = {
                        if(selectedMenu == "Services") { selectedService = null; showServiceDialog = true }
                        else { selectedPortfolio = null; showPortfolioDialog = true }
                    }) { Icon(Icons.Default.Add, null) }
                }
            }
        ) { padding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)) {

                when (selectedMenu) {
                    "Dashboard" -> AdminOverviewContent(
                        serviceCount = services.size,
                        portfolioCount = portfolios.size
                    )
                    "Services" -> ServiceManagementList(
                        services = services,
                        onEdit = {
                            selectedService = it
                            showServiceDialog = true
                        },
                        onDelete = {
                            itemToDelete = it         // 1. Simpan item yang mau dihapus
                            showDeleteConfirm = true  // 2. Munculkan dialog konfirmasi
                        }
                    )
                    "Portfolio" -> PortfolioManagementList(
                        portfolios = portfolios,
                        onEdit = {
                            selectedPortfolio = it
                            showPortfolioDialog = true
                        },
                        onDelete = {
                            itemToDelete = it // Set item yang mau dihapus
                            showDeleteConfirm = true
                        }
                    )
                    "Orders" -> AdminManagementList("Pesanan Terbaru")
                }
            }
        }
    }
}

@Composable
fun AdminOverviewContent(serviceCount: Int, portfolioCount: Int) { // Tambahkan parameter portfolioCount
    Column {
        Text("Ringkasan Bisnis", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Tampilkan jumlah Pesanan (sementara masih 0)
            StatCard("Orders", "0", Icons.Default.ShoppingCart, Modifier.weight(1f))

            // Tampilkan jumlah Layanan
            StatCard("Services", serviceCount.toString(), Icons.Default.Settings, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp)) // Jarak antar baris

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Tampilkan jumlah Portfolio (Sekarang sudah dinamis)
            StatCard("Portfolios", portfolioCount.toString(), Icons.Default.Image, Modifier.weight(1f))

            // Tampilkan jumlah User (Placeholder)
            StatCard("Users", "0", Icons.Default.Person, Modifier.weight(1f))
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, modifier: Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, null)
            Text(title, style = MaterialTheme.typography.bodySmall)
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AdminManagementList(title: String) {
    Text(title)
}

@Composable
fun ServiceManagementList(
    services: List<ServiceEntity>,
    onEdit: (ServiceEntity) -> Unit,
    onDelete: (ServiceEntity) -> Unit
) {
    if (services.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Belum ada layanan.")
        }
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(services) { service ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(service.nameServices, fontWeight = FontWeight.Bold)
                            Text("Rp ${service.priceStart}", style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { onEdit(service) }) {
                            Icon(Icons.Default.Edit, null, tint = Color.Blue)
                        }
                        IconButton(onClick = { onDelete(service) }) {
                            Icon(Icons.Default.Delete, null, tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceFormDialog(
    service: ServiceEntity?,
    onDismiss: () -> Unit,
    onConfirm: (ServiceEntity) -> Unit
) {
    var name by remember { mutableStateOf(service?.nameServices ?: "") }
    var desc by remember { mutableStateOf(service?.description ?: "") }
    var price by remember { mutableStateOf(service?.priceStart?.toLong()?.toString() ?: "") }
    var duration by remember { mutableStateOf(service?.durationEstimate ?: "") }
    var features by remember { mutableStateOf(service?.features ?: "") }

    // Logika Validasi
    val isNameValid = isTextInputValid(name)
    val isDescValid = isTextInputValid(desc)
    val isPriceValid = price.isNotEmpty() && price.all { it.isDigit() } && (price.toDoubleOrNull() ?: 0.0) >= 1000

    val isFormValid = isNameValid && isDescValid && isPriceValid && duration.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (service == null) "Tambah Layanan" else "Edit Layanan") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Layanan") },
                    isError = !isNameValid && name.isNotEmpty(),
                    supportingText = {
                        if (!isNameValid && name.isNotEmpty()) {
                            Text("Harus diawali huruf & mengandung huruf", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { if (it.all { char -> char.isDigit() }) price = it },
                    label = { Text("Harga Mulai") },
                    prefix = { Text("Rp ") },
                    isError = !isPriceValid && price.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Estimasi Durasi") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Deskripsi") },
                    isError = !isDescValid && desc.isNotEmpty(),
                    supportingText = {
                        if (!isDescValid && desc.isNotEmpty()) {
                            Text("Harus diawali huruf & mengandung huruf", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        ServiceEntity(
                            idServices = service?.idServices ?: 0,
                            idAdmin = service?.idAdmin ?: 1,
                            nameServices = name,
                            description = desc,
                            priceStart = price.toDoubleOrNull() ?: 0.0,
                            durationEstimate = duration,
                            features = features
                        )
                    )
                },
                enabled = isFormValid
            ) { Text("Simpan") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}


@Composable
fun PortfolioManagementList(
    portfolios: List<PortfolioEntity>,
    onEdit: (PortfolioEntity) -> Unit,
    onDelete: (PortfolioEntity) -> Unit
) {
    if (portfolios.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Belum ada portfolio.")
        }
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(portfolios) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.title, fontWeight = FontWeight.Bold)
                            Text("${item.category} • ${item.year}", style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { onEdit(item) }) {
                            Icon(Icons.Default.Edit, null, tint = Color.Blue)
                        }
                        IconButton(onClick = { onDelete(item) }) {
                            Icon(Icons.Default.Delete, null, tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioFormDialog(
    portfolio: PortfolioEntity?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, Int) -> Unit
) {
    var title by remember { mutableStateOf(portfolio?.title ?: "") }
    var desc by remember { mutableStateOf(portfolio?.description ?: "") }
    var year by remember { mutableStateOf(portfolio?.year?.toString() ?: "2024") }
    var expanded by remember { mutableStateOf(false) }
    val categories = listOf("residential", "commercial", "renovation", "interior")
    var selectedCategory by remember { mutableStateOf(portfolio?.category ?: categories[0]) }

    // Logika Validasi
    val isTitleValid = isTextInputValid(title)
    val isYearValid = year.length == 4 && year.all { it.isDigit() } && (year.toIntOrNull() in 1900..2100)
    val isFormValid = isTitleValid && year.length == 4 // Contoh validasi simpel

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (portfolio == null) "Tambah Portfolio" else "Edit Portfolio") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul Proyek") },
                    isError = !isTitleValid && title.isNotEmpty(),
                    supportingText = {
                        if (!isTitleValid && title.isNotEmpty()) {
                            Text("Harus diawali huruf & mengandung huruf", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = year,
                    onValueChange = { if (it.length <= 4 && it.all { c -> c.isDigit() }) year = it },
                    label = { Text("Tahun Proyek (YYYY)") },
                    isError = !isYearValid && year.isNotEmpty(),
                    supportingText = {
                        if (!isYearValid && year.isNotEmpty()) {
                            Text("Masukkan 4 digit tahun (1900-2100)", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Dropdown Kategori (Sudah diperbaiki ke versi non-deprecated)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Kategori") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true).fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = { selectedCategory = item; expanded = false }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(title, desc, selectedCategory, year.toIntOrNull() ?: 0) },
                enabled = isFormValid
            ) { Text("Simpan") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}

// Fungsi validasi: Harus diawali huruf, dan tidak boleh hanya berisi angka/simbol
fun isTextInputValid(text: String): Boolean {
    if (text.isBlank()) return false
    val trimmed = text.trim()
    val firstChar = trimmed.first()

    // 1. Cek apakah karakter pertama adalah Huruf
    val startsWithLetter = firstChar.isLetter()
    // 2. Cek apakah mengandung setidaknya satu huruf (mencegah isian "A123" yang valid tapi "123" tidak)
    val containsLetter = trimmed.any { it.isLetter() }

    return startsWithLetter && containsLetter
}