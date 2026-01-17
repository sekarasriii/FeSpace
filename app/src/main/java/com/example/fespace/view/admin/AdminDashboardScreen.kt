package com.example.fespace.view.admin

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.fespace.data.local.entity.OrderEntity
import com.example.fespace.data.local.entity.ServiceEntity
import com.example.fespace.data.local.entity.PortfolioEntity
import com.example.fespace.viewmodel.AdminViewModel
import com.example.fespace.view.navigation.Screen
import com.example.fespace.ui.theme.*
import com.example.fespace.utils.ValidationUtils
import com.example.fespace.utils.ImageUploadHelper
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import coil.compose.AsyncImage
import com.example.fespace.utils.RupiahFormatter
import com.example.fespace.view.components.StatusPieChart
import com.example.fespace.view.admin.ServiceGridItem
import com.example.fespace.view.admin.PortfolioGridItem
import com.example.fespace.view.admin.ServiceFormDialog
import com.example.fespace.view.admin.PortfolioFormDialog
import com.example.fespace.view.admin.AdminProfileDialog
import com.example.fespace.view.admin.PremiumMetricCard
import com.example.fespace.view.admin.ElegantActivityItem
import com.example.fespace.view.admin.MenuOption
import com.example.fespace.view.admin.BottomNavItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    adminViewModel: AdminViewModel,
    navController: NavHostController,
    initialSelectedMenu: String = "Dashboard"
) {
    val orders by adminViewModel.orders.collectAsStateWithLifecycle()
    val services by adminViewModel.services.collectAsStateWithLifecycle()
    val portfolios by adminViewModel.portfolios.collectAsStateWithLifecycle()
    val clientCount by adminViewModel.clientCount.collectAsStateWithLifecycle()
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showServiceDialog by remember { mutableStateOf(false) }
    var showPortfolioDialog by remember { mutableStateOf(false) }
    var selectedService by remember { mutableStateOf<ServiceEntity?>(null) }
    var selectedPortfolio by remember { mutableStateOf<PortfolioEntity?>(null) }
    
    // Tab-specific filtered data
    val filteredOrders = remember(orders, searchQuery, selectedTab) {
        if (selectedTab == 0 && searchQuery.isNotBlank()) {
            orders.filter { order ->
                order.idOrders.toString().contains(searchQuery, ignoreCase = true) ||
                order.status.contains(searchQuery, ignoreCase = true)
            }
        } else {
            orders
        }
    }
    
    val filteredServices = remember(services, searchQuery, selectedTab) {
        if (selectedTab == 1 && searchQuery.isNotBlank()) {
            services.filter { service ->
                service.nameServices.contains(searchQuery, ignoreCase = true) ||
                service.category.contains(searchQuery, ignoreCase = true)
            }
        } else {
            services
        }
    }
    
    val filteredPortfolios = remember(portfolios, searchQuery, selectedTab) {
        if (selectedTab == 2 && searchQuery.isNotBlank()) {
            portfolios.filter { portfolio ->
                portfolio.title.contains(searchQuery, ignoreCase = true) ||
                portfolio.category.contains(searchQuery, ignoreCase = true)
            }
        } else {
            portfolios
        }
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    "Konfirmasi Logout",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Text(
                    "Apakah Anda yakin ingin keluar dari dashboard admin?",
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        navController.navigate("welcome") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Terracotta,
                        contentColor = Cream
                    )
                ) {
                    Text("Ya, Logout")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLogoutDialog = false },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Terracotta
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Terracotta.copy(alpha = 0.5f))
                ) {
                    Text("Batal")
                }
            },
            containerColor = DarkSurface,
            shape = RoundedCornerShape(20.dp)
        )
    }

    Scaffold(
        containerColor = DarkCharcoal,
        bottomBar = {
            BottomAppBar(
                containerColor = DarkSurface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomNavItem(
                        icon = Icons.Default.Home,
                        label = "Dashboard",
                        selected = selectedTab == 0,
                        onClick = { 
                            selectedTab = 0
                            searchQuery = ""
                        }
                    )
                    BottomNavItem(
                        icon = Icons.Default.Build,
                        label = "Layanan",
                        selected = selectedTab == 1,
                        onClick = { 
                            selectedTab = 1
                            searchQuery = ""
                        }
                    )
                    BottomNavItem(
                        icon = Icons.Default.PhotoLibrary,
                        label = "Portfolio",
                        selected = selectedTab == 2,
                        onClick = { 
                            selectedTab = 2
                            searchQuery = ""
                        }
                    )
                    BottomNavItem(
                        icon = Icons.Default.Menu,
                        label = "Menu",
                        selected = selectedTab == 3,
                        onClick = { 
                            selectedTab = 3
                            searchQuery = ""
                        }
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Glassmorphic Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                DarkCharcoal,
                                DarkCharcoal.copy(alpha = 0.95f)
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(Terracotta, TerracottaDark)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Cream,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            
                            Spacer(Modifier.width(16.dp))
                            
                            Column {
                                Text(
                                    "Hi Rahayu!",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    "Ready to create today?",
                                    fontSize = 14.sp,
                                    color = TextTertiary
                                )
                            }
                        }
                    }
                }
            }
            
            // Main Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(top = 100.dp, bottom = 100.dp)
            ) {
                
                // Content based on tab
                when (selectedTab) {
                    0 -> {
                        // Dashboard
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                PremiumMetricCard(
                                    modifier = Modifier.weight(1f),
                                    title = "Pesanan Baru",
                                    value = orders.count { it.status.equals("pending", ignoreCase = true) }.toString(),
                                    subtitle = "MENUNGGU",
                                    gradient = Brush.linearGradient(
                                        colors = listOf(Terracotta, TerracottaDark)
                                    ),
                                    icon = Icons.Default.ShoppingCart,
                                    onClick = { }
                                )
                                
                                PremiumMetricCard(
                                    modifier = Modifier.weight(1f),
                                    title = "Total Layanan",
                                    value = com.example.fespace.utils.RupiahFormatter.formatToRupiah(
                                        services.sumOf { it.priceStart.toDouble() }
                                    ),
                                    subtitle = "${services.size} Aktif",
                                    gradient = Brush.linearGradient(
                                        colors = listOf(Gold, Copper)
                                    ),
                                    icon = Icons.Default.Settings,
                                    onClick = { selectedTab = 1 }
                                )
                            }
                        }
                        
                        item { Spacer(Modifier.height(32.dp)) }
                        
                        item {
                            Text(
                                "Status Proyek",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                        
                        item { Spacer(Modifier.height(16.dp)) }
                        
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                colors = CardDefaults.cardColors(containerColor = DarkCharcoal.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(24.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    StatusPieChart(orders = orders)
                                    
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.padding(start = 16.dp)
                                    ) {
                                        val statusLabelMap = mapOf(
                                            "PENDING" to "Baru",
                                            "APPROVED" to "Disetujui",
                                            "INDESIGN" to "Desain",
                                            "COMPLETED" to "Selesai"
                                        )
                                        val statusColorMap = mapOf(
                                            "PENDING" to StatusPending,
                                            "APPROVED" to StatusApproved,
                                            "INDESIGN" to StatusInDesign,
                                            "COMPLETED" to StatusApproved
                                        )
                                        
                                        statusLabelMap.forEach { (status, label) ->
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(10.dp)
                                                        .clip(CircleShape)
                                                        .background(statusColorMap[status] ?: Color.Gray)
                                                )
                                                Spacer(Modifier.width(8.dp))
                                                Text(
                                                    text = label,
                                                    color = TextPrimary,
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        item { Spacer(Modifier.height(32.dp)) }
                        
                        item {
                            Text(
                                "Aktivitas Terbaru",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                        
                        item { Spacer(Modifier.height(16.dp)) }
                        
                        items(filteredOrders.take(10)) { order ->
                            ElegantActivityItem(
                                order = order,
                                onClick = {
                                    navController.navigate(Screen.AdminOrderDetail(order.idOrders).route)
                                },
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                            )
                        }
                    }
                    
                    1 -> {
                        // Services Grid
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Layanan (${filteredServices.size})",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                
                                FloatingActionButton(
                                    onClick = {
                                        selectedService = null
                                        showServiceDialog = true
                                    },
                                    containerColor = Terracotta,
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Tambah", tint = Cream)
                                }
                            }
                        }
                        
                        item { Spacer(Modifier.height(16.dp)) }
                        
                        item {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 2000.dp)
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                userScrollEnabled = false
                            ) {
                                items(filteredServices) { service ->
                                    ServiceGridItem(
                                        service = service,
                                        onEdit = {
                                            selectedService = service
                                            showServiceDialog = true
                                        },
                                        onDelete = {
                                            adminViewModel.deleteService(service)
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    2 -> {
                        // Portfolio Grid
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Portfolio (${filteredPortfolios.size})",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                
                                FloatingActionButton(
                                    onClick = {
                                        selectedPortfolio = null
                                        showPortfolioDialog = true
                                    },
                                    containerColor = Terracotta,
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Tambah", tint = Cream)
                                }
                            }
                        }
                        
                        item { Spacer(Modifier.height(16.dp)) }
                        
                        item {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 2000.dp)
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                userScrollEnabled = false
                            ) {
                                items(filteredPortfolios) { portfolio ->
                                    PortfolioGridItem(
                                        portfolio = portfolio,
                                        onEdit = {
                                            selectedPortfolio = portfolio
                                            showPortfolioDialog = true
                                        },
                                        onDelete = {
                                            adminViewModel.deletePortfolio(portfolio)
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    3 -> {
                        // Menu
                        item {
                            Text(
                                "Menu",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                        
                        item { Spacer(Modifier.height(16.dp)) }
                        
                        item {
                            MenuOption(
                                icon = Icons.Default.Person,
                                title = "Klien",
                                subtitle = "$clientCount Total Klien",
                                onClick = { navController.navigate(Screen.AdminClients.route) },
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                            )
                        }
                        
                        item {
                            var showProfileEditDialog by remember { mutableStateOf(false) }
                            
                            MenuOption(
                                icon = Icons.Default.AccountCircle,
                                title = "Profil Admin",
                                subtitle = "rahayu@gmail.com",
                                onClick = { showProfileEditDialog = true },
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                            )
                            
                            if (showProfileEditDialog) {
                                AdminProfileDialog(
                                    adminViewModel = adminViewModel,
                                    onDismiss = { showProfileEditDialog = false }
                                )
                            }
                        }
                        
                        item {
                            MenuOption(
                                icon = Icons.Default.ExitToApp,
                                title = "Logout",
                                subtitle = "Keluar dari akun",
                                onClick = { showLogoutDialog = true },
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Service Dialog
    if (showServiceDialog) {
        ServiceFormDialog(
            service = selectedService,
            onDismiss = { showServiceDialog = false },
            onSave = { name, category, desc, price, duration, features, imagePath ->
                if (selectedService == null) {
                    adminViewModel.addService(name, category, desc, price, duration, features, imagePath, 1)
                } else {
                    adminViewModel.updateService(
                        selectedService!!.copy(
                            nameServices = name,
                            category = category,
                            description = desc,
                            priceStart = price,
                            durationEstimate = duration,
                            features = features,
                            imagePath = imagePath
                        )
                    )
                }
                showServiceDialog = false
            }
        )
    }
    
    // Portfolio Dialog
    if (showPortfolioDialog) {
        PortfolioFormDialog(
            portfolio = selectedPortfolio,
            onDismiss = { showPortfolioDialog = false },
            onSave = { title, desc, category, year, imagePath ->
                if (selectedPortfolio == null) {
                    adminViewModel.addPortfolio(title, desc, category, year, imagePath, 1)
                } else {
                    adminViewModel.updatePortfolio(
                        selectedPortfolio!!.copy(
                            title = title,
                            description = desc,
                            category = category,
                            year = year,
                            imagePath = imagePath
                        )
                    )
                }
                showPortfolioDialog = false
            }
        )
    }
}
