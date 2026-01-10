package com.example.fespace.view.client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fespace.viewmodel.ClientViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderId: Int,
    clientViewModel: ClientViewModel,
    onBack: () -> Unit
) {
    // State untuk mengambil data order dari database berdasarkan ID
    val order by produceState<com.example.fespace.data.local.entity.OrderEntity?>(initialValue = null) {
        value = clientViewModel.getOrderById(orderId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Pesanan #$orderId", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        if (order == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // --- SECTION STATUS ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF3F51B5), modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Status Saat Ini: ${order?.status?.uppercase() ?: "PENDING"}",
                                color = Color(0xFF3F51B5),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Text(
                            text = "Arsitek: Admin FeSpace",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 26.dp, top = 4.dp)
                        )

                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = { /* Integrasi WhatsApp */ },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.padding(start = 26.dp)
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Hubungi Arsitek", fontSize = 12.sp)
                        }
                    }
                }

                // --- SECTION DETAIL PROYEK ---
                Text("Detail Lokasi & Anggaran", fontWeight = FontWeight.Bold)
                OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text("Alamat:", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text(order?.locationAddress ?: "-", fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(8.dp))
                        Text("Estimasi Budget:", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text("Rp ${order?.budget ?: 0}", fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                    }
                }

                // --- SECTION DOKUMEN DARI ARSITEK ---
                Text("Dokumen dari Arsitek", fontWeight = FontWeight.Bold)

                // Kondisi jika dokumen belum ada (Biasanya file_arsitek di DB masih null)
                val documentUrl = null // Simulasi dari DB (Ganti dengan order?.fileArsitek jika ada)

                if (documentUrl == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Belum ada dokumen yang diunggah oleh Arsitek.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                } else {
                    // Jika ada dokumen (Ditampilkan hanya jika status sudah APPROVED/DESIGN PROGRESS)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
                    ) {
                        Row(
                            Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Draft_Desain_Final.pdf", modifier = Modifier.weight(1f))
                            IconButton(onClick = {}) { Icon(Icons.Default.Download, null) }
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                // --- SECTION ACTION CLIENT ---
                Text("Upload Dokumen Tambahan (Opsional)", fontWeight = FontWeight.Bold)
                Button(
                    onClick = { /* Pilih File */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.UploadFile, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Pilih File")
                }
            }
        }
    }
}
