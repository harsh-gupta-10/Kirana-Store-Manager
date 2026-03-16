package com.kiranstore.manager.ui.screens.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiranstore.manager.data.database.entities.Debt
import com.kiranstore.manager.data.remote.ai.VoiceAction
import com.kiranstore.manager.ui.components.*
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.viewmodel.AssistantViewModel
import com.kiranstore.manager.viewmodel.CustomerViewModel
import com.kiranstore.manager.viewmodel.HomeViewModel
import com.kiranstore.manager.viewmodel.VoiceUiState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddUdhaar: () -> Unit,
    onAddRental: () -> Unit,
    onAddCustomer: () -> Unit,
    onAddPayment: () -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    customerViewModel: CustomerViewModel = hiltViewModel(),
    assistantViewModel: AssistantViewModel = hiltViewModel()
) {
    val totalUdhaar by viewModel.totalUdhaarAmount.collectAsState(initial = 0.0)
    val customers by viewModel.totalCustomers.collectAsState(initial = emptyList())
    val activeRentals by viewModel.activeRentalCount.collectAsState(initial = 0)
    val recentDebts by viewModel.recentDebts.collectAsState(initial = emptyList())
    val recoveredToday by viewModel.recoveredToday.collectAsState(initial = 0.0)
    val context = LocalContext.current
    val voiceState by assistantViewModel.state.collectAsState()

    var speechError by remember { mutableStateOf<String?>(null) }
    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasAudioPermission = granted
            if (granted) {
                speechError = null
            } else {
                speechError = "Microphone permission is needed for voice commands."
            }
        }
    )

    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    val speechIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
    }

    val startListening: () -> Unit = {
        if (!hasAudioPermission) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        } else {
            speechError = null
            assistantViewModel.setListening(true)
            speechRecognizer.startListening(speechIntent)
        }
    }

    DisposableEffect(speechRecognizer) {
        val listener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {
                assistantViewModel.setListening(false)
            }

            override fun onError(error: Int) {
                assistantViewModel.setListening(false)
                speechError = "Listening error ($error). Try again."
            }

            override fun onResults(results: Bundle?) {
                assistantViewModel.setListening(false)
                val text = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                    ?.trim()
                    .orEmpty()
                if (text.isNotBlank()) {
                    assistantViewModel.submitTranscript(text)
                } else {
                    speechError = "Didn't catch that. Please try again."
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
        speechRecognizer.setRecognitionListener(listener)
        onDispose {
            speechRecognizer.destroy()
        }
    }

    Scaffold(
        containerColor = BackgroundGrey,
        topBar = {
            Surface(shadowElevation = 2.dp, color = CardWhite) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Store avatar
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(OrangeLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Store, contentDescription = null,
                            tint = OrangePrimary, modifier = Modifier.size(22.dp))
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Kiran General Store", fontWeight = FontWeight.Bold,
                            fontSize = 16.sp, color = TextPrimary)
                        Text("नमस्ते / Welcome", fontSize = 12.sp, color = TextSecondary)
                    }
                    IconButton(onClick = startListening) {
                        Icon(
                            imageVector = Icons.Filled.Mic,
                            contentDescription = "Voice commands",
                            tint = if (voiceState.isListening) OrangePrimary else TextSecondary
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Notifications",
                            tint = TextSecondary)
                    }
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings",
                            tint = TextSecondary)
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Overview Section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Overview Today", fontWeight = FontWeight.Bold,
                        fontSize = 18.sp, color = TextPrimary)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(GreenLight)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("LIVE", fontSize = 11.sp, color = GreenSuccess,
                            fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Summary Cards grid
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        UdhaarSummaryCard(
                            totalUdhaar = totalUdhaar,
                            modifier = Modifier.weight(1f)
                        )
                        ActiveRentalCard(
                            count = activeRentals,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SummaryCard(
                            title = "Tasks Today\nआज के काम",
                            value = "5",
                            subtitle = "",
                            modifier = Modifier.weight(1f)
                        )
                        SummaryCard(
                            title = "Buy List\nखरीदने का सामान",
                            value = "12 items",
                            subtitle = "",
                            valueColor = AmberWarning,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Voice Assistant
            item {
                VoiceAssistantCard(
                    voiceState = voiceState,
                    speechError = speechError,
                    onRetry = startListening,
                    onAction = { action ->
                        when (action) {
                            VoiceAction.ADD_CUSTOMER -> onAddCustomer()
                            VoiceAction.ADD_UDHAAR -> onAddUdhaar()
                            VoiceAction.ADD_PAYMENT -> onAddPayment()
                            VoiceAction.ADD_RENTAL -> onAddRental()
                            VoiceAction.SHOW_SUMMARY -> onOpenSettings()
                            VoiceAction.UNKNOWN -> {}
                        }
                        assistantViewModel.clearResult()
                    }
                )
            }

            // Quick Actions
            item {
                Column(modifier = Modifier.padding(top = 20.dp)) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Quick Actions", fontWeight = FontWeight.Bold,
                            fontSize = 16.sp, color = TextPrimary)
                        Spacer(Modifier.width(8.dp))
                        Text("जल्दी काम करें", fontSize = 12.sp, color = TextSecondary)
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickActionCard(
                            icon = Icons.Filled.PersonAdd,
                            label = "Add Udhaar",
                            sublabel = "नया उधार जोड़ें",
                            isPrimary = true,
                            modifier = Modifier.weight(1f),
                            onClick = onAddUdhaar
                        )
                        QuickActionCard(
                            icon = Icons.Filled.Key,
                            label = "Rent Machine",
                            sublabel = "मशीन किराए पर दें",
                            isPrimary = false,
                            modifier = Modifier.weight(1f),
                            onClick = onAddRental
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickActionCard(
                            icon = Icons.Filled.AddTask,
                            label = "Add Task",
                            sublabel = "नया काम जोड़ें",
                            isPrimary = false,
                            modifier = Modifier.weight(1f),
                            onClick = {}
                        )
                        QuickActionCard(
                            icon = Icons.Filled.ShoppingCart,
                            label = "Add Buy Item",
                            sublabel = "खरीद सूची में जोड़ें",
                            isPrimary = false,
                            modifier = Modifier.weight(1f),
                            onClick = {}
                        )
                    }
                }
            }

            // Recent Udhaar
            item {
                Column(modifier = Modifier.padding(top = 20.dp)) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Recent Udhaar", fontWeight = FontWeight.Bold,
                            fontSize = 16.sp, color = TextPrimary)
                        Spacer(Modifier.width(8.dp))
                        Text("हाल का उधार", fontSize = 12.sp, color = TextSecondary)
                    }
                }
            }

            if (recentDebts.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No recent transactions", color = TextSecondary, fontSize = 14.sp)
                    }
                }
            } else {
                items(recentDebts.size) { idx ->
                    val debt = recentDebts[idx]
                    RecentDebtRow(debt = debt, customers = customers.associate { it.id to it.name })
                }
            }
        }
    }
}

@Composable
private fun UdhaarSummaryCard(totalUdhaar: Double, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.AccountBalanceWallet, contentDescription = null,
                    tint = OrangePrimary, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Total Udhaar", fontSize = 11.sp, color = TextSecondary)
            }
            Spacer(Modifier.height(4.dp))
            Text("₹${formatAmount(totalUdhaar)}", fontSize = 20.sp,
                fontWeight = FontWeight.Bold, color = RedDanger)
            Text("कुल उधार", fontSize = 11.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun ActiveRentalCard(count: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Construction, contentDescription = null,
                    tint = OrangePrimary, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Active Rentals", fontSize = 11.sp, color = TextSecondary)
            }
            Spacer(Modifier.height(4.dp))
            Text("$count", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("किराए की मशीन", fontSize = 11.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun QuickActionCard(
    icon: ImageVector,
    label: String,
    sublabel: String,
    isPrimary: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPrimary) OrangePrimary else CardWhite
        ),
        elevation = CardDefaults.cardElevation(if (isPrimary) 4.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isPrimary) Color.White else OrangePrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                label,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (isPrimary) Color.White else TextPrimary
            )
            Text(
                sublabel,
                fontSize = 11.sp,
                color = if (isPrimary) Color.White.copy(alpha = 0.8f) else TextSecondary
            )
        }
    }
}

@Composable
private fun VoiceAssistantCard(
    voiceState: VoiceUiState,
    speechError: String?,
    onRetry: () -> Unit,
    onAction: (VoiceAction) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = if (voiceState.isListening) OrangeLight else BackgroundGrey,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Mic,
                            contentDescription = null,
                            tint = if (voiceState.isListening) OrangePrimary else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("Voice Assistant", fontWeight = FontWeight.Bold, color = TextPrimary)
                    val subtitle = when {
                        voiceState.isListening -> "Listening..."
                        voiceState.isProcessing -> "Understanding with Gemini..."
                        voiceState.result != null -> "Action ready"
                        else -> "Tap mic to speak a command"
                    }
                    Text(subtitle, color = TextSecondary, fontSize = 12.sp)
                }
                Spacer(Modifier.weight(1f))
                OutlinedButton(onClick = onRetry) {
                    Text(if (voiceState.isListening) "Listening" else "Start")
                }
            }

            voiceState.transcript.takeIf { it.isNotBlank() }?.let {
                Text("Heard: \"$it\"", color = TextPrimary, fontSize = 13.sp)
            }
            speechError?.let { Text(it, color = RedDanger, fontSize = 12.sp) }
            voiceState.error?.let { Text(it, color = RedDanger, fontSize = 12.sp) }

            voiceState.result?.let { result ->
                val actionLabel = when (result.action) {
                    VoiceAction.ADD_CUSTOMER -> "Add Customer"
                    VoiceAction.ADD_UDHAAR -> "Add Udhaar"
                    VoiceAction.ADD_PAYMENT -> "Add Payment"
                    VoiceAction.ADD_RENTAL -> "Add Rental"
                    VoiceAction.SHOW_SUMMARY -> "Open Cloud"
                    VoiceAction.UNKNOWN -> "No action"
                }
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("AI Suggestion: $actionLabel", fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    result.notes?.let { Text("Notes: $it", color = TextSecondary, fontSize = 12.sp) }
                    Button(
                        onClick = { onAction(result.action) },
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                    ) {
                        Text("Go")
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentDebtRow(debt: Debt, customers: Map<Long, String>) {
    val name = customers[debt.customerId] ?: "Unknown"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarCircle(name = name, size = 38)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimary)
                Text(formatTimeAgo(debt.date), fontSize = 12.sp, color = TextSecondary)
            }
            Text(
                "- ₹${formatAmount(debt.amount)}",
                color = RedDanger,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}
