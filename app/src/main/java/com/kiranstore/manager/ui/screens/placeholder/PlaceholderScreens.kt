package com.kiranstore.manager.ui.screens.placeholder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.viewmodel.CloudViewModel

@Composable
fun PlaceholderScreen(icon: ImageVector, title: String) {
    Scaffold(
        containerColor = BackgroundGrey,
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardWhite)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.padding(32.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = OrangeLight,
                            modifier = Modifier.size(80.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = OrangePrimary,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Feature coming in next version",
                        fontSize = 14.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(20.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = OrangeLight
                    ) {
                        Text(
                            "Phase 2",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            color = OrangePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TasksScreen() = PlaceholderScreen(Icons.Filled.ChecklistRtl, "Tasks")

@Composable
fun BuyListScreen() = PlaceholderScreen(Icons.Filled.ShoppingCart, "Buy List")

@Composable
fun SettingsScreen(
    viewModel: CloudViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by viewModel.auth.collectAsState()
    val syncState by viewModel.sync.collectAsState()

    Scaffold(
        containerColor = BackgroundGrey,
        topBar = {
            TopAppBar(
                title = { Text("Cloud & Security", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardWhite)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Supabase Authentication", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = OrangePrimary) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = OrangePrimary) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (authState.error != null) {
                        Text(authState.error ?: "", color = RedDanger, fontSize = 12.sp)
                    } else if (authState.message != null) {
                        Text(authState.message ?: "", color = GreenSuccess, fontSize = 12.sp)
                    }
                    Text("Current session: ${authState.email ?: "Not signed in"}", color = TextSecondary, fontSize = 12.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { viewModel.signIn(email, password) },
                            enabled = !authState.loading,
                            colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                        ) {
                            if (authState.loading) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = CardWhite, strokeWidth = 2.dp)
                                Spacer(Modifier.width(6.dp))
                            }
                            Text("Sign In")
                        }
                        OutlinedButton(
                            onClick = { viewModel.signUp(email, password) },
                            enabled = !authState.loading
                        ) {
                            Text("Sign Up")
                        }
                        OutlinedButton(
                            onClick = { viewModel.signOut() },
                            enabled = !authState.loading
                        ) {
                            Text("Sign Out")
                        }
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Database Sync", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(
                        "Push latest customers, udhaar, payments and rentals to Supabase for backup.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                    if (syncState.error != null) {
                        Text(syncState.error ?: "", color = RedDanger, fontSize = 12.sp)
                    } else if (syncState.message != null) {
                        Text(syncState.message ?: "", color = GreenSuccess, fontSize = 12.sp)
                    }
                    Button(
                        onClick = { viewModel.syncNow() },
                        enabled = !syncState.syncing,
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                    ) {
                        if (syncState.syncing) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = CardWhite, strokeWidth = 2.dp)
                            Spacer(Modifier.width(8.dp))
                        }
                        Text(if (syncState.syncing) "Syncing..." else "Sync Now")
                    }
                }
            }
        }
    }
}
