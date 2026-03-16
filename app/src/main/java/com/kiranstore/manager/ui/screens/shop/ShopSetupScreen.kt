package com.kiranstore.manager.ui.screens.shop

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.viewmodel.shop.ShopProfileState

@Composable
fun ShopSetupScreen(
    state: ShopProfileState,
    onSave: (shopName: String, ownerName: String, phone: String, address: String) -> Unit,
    onUploadLogo: (Uri) -> Unit,
    onClearError: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var shopName by remember(state.shop) { mutableStateOf(state.shop?.shopName ?: "") }
    var ownerName by remember(state.shop) { mutableStateOf(state.shop?.ownerName ?: "") }
    var phoneNumber by remember(state.shop) { mutableStateOf(state.shop?.phoneNumber ?: "") }
    var address by remember(state.shop) { mutableStateOf(state.shop?.address ?: "") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            onUploadLogo(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Shop Setup",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = OrangePrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Set up your shop profile to get started.",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Logo upload area
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, OrangePrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (state.isUploading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = OrangePrimary,
                    strokeWidth = 2.dp
                )
            } else if (state.logoUrl != null) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Logo uploaded",
                    tint = GreenSuccess,
                    modifier = Modifier.size(48.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Store,
                    contentDescription = "Shop logo placeholder",
                    tint = OrangePrimary,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { imagePickerLauncher.launch("image/*") },
            enabled = !state.isUploading,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = OrangePrimary)
        ) {
            Icon(Icons.Filled.PhotoCamera, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Upload Logo")
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = shopName,
            onValueChange = {
                shopName = it
                onClearError()
            },
            label = { Text("Shop Name") },
            leadingIcon = { Icon(Icons.Filled.Store, contentDescription = "Shop Name") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = OrangePrimary,
                focusedLabelColor = OrangePrimary,
                cursorColor = OrangePrimary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = ownerName,
            onValueChange = {
                ownerName = it
                onClearError()
            },
            label = { Text("Owner Name") },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Owner Name") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = OrangePrimary,
                focusedLabelColor = OrangePrimary,
                cursorColor = OrangePrimary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {
                phoneNumber = it
                onClearError()
            },
            label = { Text("Phone Number") },
            leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = "Phone Number") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = OrangePrimary,
                focusedLabelColor = OrangePrimary,
                cursorColor = OrangePrimary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = address,
            onValueChange = {
                address = it
                onClearError()
            },
            label = { Text("Shop Address") },
            leadingIcon = { Icon(Icons.Filled.LocationOn, contentDescription = "Address") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            minLines = 2,
            maxLines = 3,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = OrangePrimary,
                focusedLabelColor = OrangePrimary,
                cursorColor = OrangePrimary
            )
        )

        if (state.error != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = state.error,
                color = RedDanger,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onSave(shopName.trim(), ownerName.trim(), phoneNumber.trim(), address.trim()) },
            enabled = shopName.isNotBlank() && ownerName.isNotBlank() &&
                    phoneNumber.isNotBlank() && address.isNotBlank() &&
                    !state.isSaving && !state.isUploading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (state.isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = CardWhite,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Save & Continue", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
