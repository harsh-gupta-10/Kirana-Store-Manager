package com.kiranstore.manager.ui.screens.auth

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kiranstore.manager.ui.components.*
import com.kiranstore.manager.ui.navigation.Routes
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavController, vm: AuthViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Navigate when logged in
    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            if (state.isNewUser) navController.navigate(Routes.SHOP_SETUP) { popUpTo(Routes.LOGIN) { inclusive = true } }
            else navController.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(BackgroundLight),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(48.dp))

            // Logo / Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(OrangeContainer, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Store, contentDescription = null, tint = OrangePrimary, modifier = Modifier.size(44.dp))
            }

            Spacer(Modifier.height(8.dp))
            Text("Kiran Store Manager", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
            Text("Login to your shop", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(16.dp))

            // Email
            KiranTextField(value = email, onValueChange = { email = it }, label = "Email",
                leadingIcon = Icons.Filled.Email,
                keyboardType = KeyboardType.Email)

            // Password
            KiranTextField(
                value = password, onValueChange = { password = it }, label = "Password",
                leadingIcon = Icons.Filled.Lock,
                keyboardType = KeyboardType.Password,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = null)
                    }
                }
            )

            // Error
            if (state.error.isNotBlank()) {
                Text(state.error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(4.dp))
            PrimaryButton(label = "Login", onClick = { vm.login(email, password) }, isLoading = state.isLoading,
                enabled = email.isNotBlank() && password.length >= 6)

            // Divider
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text("  or  ", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            OutlinedButton(
                onClick = { navController.navigate(Routes.SIGNUP) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.5.dp, OrangePrimary)
            ) {
                Text("Create New Account", color = OrangePrimary, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────
// SIGNUP SCREEN
// ─────────────────────────────────────────────────────────
@Composable
fun SignupScreen(navController: NavController, vm: AuthViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    LaunchedEffect(state.isNewUser) {
        if (state.isNewUser) navController.navigate(Routes.SHOP_SETUP) { popUpTo(Routes.SIGNUP) { inclusive = true } }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundLight)
            .padding(horizontal = 28.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(60.dp))
        Icon(Icons.Filled.AddBusiness, contentDescription = null, tint = OrangePrimary, modifier = Modifier.size(56.dp))
        Text("Register Your Shop", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
        Text("Create a new account", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(8.dp))

        KiranTextField(value = email, onValueChange = { email = it }, label = "Email", leadingIcon = Icons.Filled.Email, keyboardType = KeyboardType.Email)
        KiranTextField(value = password, onValueChange = { password = it }, label = "Password (min 6 chars)", leadingIcon = Icons.Filled.Lock, keyboardType = KeyboardType.Password)
        KiranTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = "Confirm Password", leadingIcon = Icons.Filled.Lock, keyboardType = KeyboardType.Password,
            isError = confirmPassword.isNotBlank() && password != confirmPassword, errorText = "Passwords do not match")

        if (state.error.isNotBlank()) Text(state.error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)

        PrimaryButton("Create Account", onClick = { vm.signUp(email, password) }, isLoading = state.isLoading,
            enabled = email.isNotBlank() && password.length >= 6 && password == confirmPassword)

        TextButton(onClick = { navController.popBackStack() }) {
            Text("Already have an account? Login", color = OrangePrimary)
        }
        Spacer(Modifier.height(32.dp))
    }
}
