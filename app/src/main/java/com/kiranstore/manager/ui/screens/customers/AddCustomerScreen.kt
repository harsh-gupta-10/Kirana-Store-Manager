package com.kiranstore.manager.ui.screens.customers

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.viewmodel.CloudViewModel
import com.kiranstore.manager.viewmodel.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomerScreen(
    onSaved: (Long) -> Unit,
    onCancel: () -> Unit,
    viewModel: CustomerViewModel = hiltViewModel(),
    cloudViewModel: CloudViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val uploadState by cloudViewModel.upload.collectAsState()
    val context = LocalContext.current
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
            if (uri != null) cloudViewModel.clearUploadState()
        }
    )

    Scaffold(
        containerColor = BackgroundGrey,
        topBar = {
            TopAppBar(
                title = { Text("Add Customer", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardWhite)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it; nameError = false },
                        label = { Text("Customer Name *") },
                        leadingIcon = {
                            Icon(Icons.Filled.Person, contentDescription = null, tint = OrangePrimary)
                        },
                        isError = nameError,
                        supportingText = { if (nameError) Text("Name is required") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it; phoneError = false },
                        label = { Text("Phone Number *") },
                        leadingIcon = {
                            Icon(Icons.Filled.Phone, contentDescription = null, tint = OrangePrimary)
                        },
                        isError = phoneError,
                        supportingText = { if (phoneError) Text("Phone number is required") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes (Optional)") },
                        leadingIcon = {
                            Icon(Icons.Filled.Notes, contentDescription = null, tint = OrangePrimary)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        minLines = 2,
                        maxLines = 4
                    )
                    Divider()
                    Text("Supabase Storage (optional)", fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(
                        "Pick an image to upload as WebP (80%) after saving the customer.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = {
                            pickMedia.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }) {
                            Icon(Icons.Filled.Image, contentDescription = null)
                            Spacer(Modifier.width(6.dp))
                            Text("Choose Image")
                        }
                        selectedImageUri?.let { uri ->
                            Text(
                                text = uri.lastPathSegment ?: "Selected",
                                color = TextSecondary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    when {
                        uploadState.uploading -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = OrangePrimary
                                )
                                Spacer(Modifier.width(6.dp))
                                Text("Uploading to cloud...", fontSize = 12.sp, color = TextSecondary)
                            }
                        }
                        uploadState.url != null -> {
                            Text("Uploaded: ${uploadState.url}", color = GreenSuccess, fontSize = 12.sp)
                        }
                        uploadState.error != null -> {
                            Text(uploadState.error ?: "Upload failed", color = RedDanger, fontSize = 12.sp)
                        }
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel", fontSize = 16.sp)
                }
                    Button(
                        onClick = {
                            nameError = name.isBlank()
                            phoneError = phone.isBlank()
                            if (!nameError && !phoneError) {
                                viewModel.saveCustomer(name, phone, notes) { id ->
                                    selectedImageUri?.let { uri ->
                                        cloudViewModel.uploadCustomerImage(id, uri, context)
                                    }
                                    onSaved(id)
                                }
                            }
                        },
                        modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                ) {
                    Icon(Icons.Filled.Save, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Save Customer", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
