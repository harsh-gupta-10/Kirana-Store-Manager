package com.kiranstore.manager.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.kiranstore.manager.ui.theme.*
import com.kiranstore.manager.utils.*

// ─────────────────────────────────────────────────────────
// AVATAR CIRCLE with initials
// ─────────────────────────────────────────────────────────
@Composable
fun AvatarCircle(
    name: String,
    size: Dp = 44.dp,
    fontSize: TextUnit = 16.sp
) {
    val color = Color(name.avatarColor())
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.initials(),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = fontSize,
                color = color
            )
        )
    }
}

// ─────────────────────────────────────────────────────────
// SUMMARY CARD (for dashboard)
// ─────────────────────────────────────────────────────────
@Composable
fun SummaryCard(
    title: String,
    value: String,
    subtitle: String = "",
    badge: String = "",
    badgeColor: Color = OrangePrimary,
    icon: ImageVector? = null,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (icon != null) {
                        Icon(icon, contentDescription = null, tint = OrangePrimary, modifier = Modifier.size(18.dp))
                    }
                    Text(title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (badge.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = badgeColor.copy(alpha = 0.12f)
                    ) {
                        Text(
                            badge,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(color = badgeColor, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = valueColor))
            if (subtitle.isNotBlank()) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// QUICK ACTION BUTTON
// ─────────────────────────────────────────────────────────
@Composable
fun QuickActionButton(
    label: String,
    subLabel: String = "",
    icon: ImageVector,
    isPrimary: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isPrimary) OrangePrimary else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPrimary) 4.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                icon, contentDescription = label,
                tint = if (isPrimary) Color.White else OrangePrimary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                label,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = if (isPrimary) Color.White else MaterialTheme.colorScheme.onSurface
                )
            )
            if (subLabel.isNotBlank()) {
                Text(
                    subLabel,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isPrimary) Color.White.copy(alpha = 0.8f)
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// VOICE FAB
// ─────────────────────────────────────────────────────────
@Composable
fun VoiceFab(
    isListening: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = if (isListening) 1.12f else 1f,
        animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse),
        label = "scale"
    )
    ExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier.then(if (isListening) Modifier.graphicsLayer(scaleX = scale, scaleY = scale) else Modifier),
        containerColor = OrangePrimary,
        contentColor = Color.White,
        shape = RoundedCornerShape(50),
        icon = { Icon(Icons.Filled.Mic, contentDescription = "Voice") },
        text = { Text(if (isListening) "Listening…" else "बोलकर जोड़ें", fontWeight = FontWeight.SemiBold) }
    )
}

// ─────────────────────────────────────────────────────────
// STATUS CHIP
// ─────────────────────────────────────────────────────────
@Composable
fun StatusChip(status: String) {
    val (bgColor, textColor, label) = when (status.uppercase()) {
        "ACTIVE"    -> Triple(Color(0xFFE3F2FD), Color(0xFF1565C0), "Active")
        "RETURNED"  -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), "Returned")
        "LATE"      -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "Late")
        "PENDING"   -> Triple(Color(0xFFFFF8E1), Color(0xFFF57F17), "Pending")
        "PAID"      -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), "Paid")
        "DONE"      -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), "Done")
        "HIGH"      -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "High")
        "MEDIUM"    -> Triple(Color(0xFFFFF8E1), Color(0xFFF57F17), "Medium")
        "LOW"       -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), "Low")
        else        -> Triple(Color(0xFFF5F5F5), Color(0xFF757575), status)
    }
    Surface(shape = RoundedCornerShape(8.dp), color = bgColor) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall.copy(color = textColor, fontWeight = FontWeight.Bold)
        )
    }
}

// ─────────────────────────────────────────────────────────
// SECTION HEADER
// ─────────────────────────────────────────────────────────
@Composable
fun SectionHeader(title: String, actionLabel: String = "", onAction: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        if (actionLabel.isNotBlank()) {
            TextButton(onClick = onAction) {
                Text(actionLabel, color = OrangePrimary, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// KIRAN TOP APP BAR
// ─────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KiranTopBar(
    title: String,
    subtitle: String = "",
    showBack: Boolean = false,
    onBack: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Column {
                Text(title, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                if (subtitle.isNotBlank())
                    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        navigationIcon = {
            if (showBack) IconButton(onClick = onBack) {
                Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = "Back")
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

// ─────────────────────────────────────────────────────────
// EMPTY STATE
// ─────────────────────────────────────────────────────────
@Composable
fun EmptyState(icon: ImageVector, title: String, subtitle: String, actionLabel: String = "", onAction: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = OrangePrimary.copy(alpha = 0.4f), modifier = Modifier.size(80.dp))
        Spacer(Modifier.height(16.dp))
        Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
        Spacer(Modifier.height(6.dp))
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (actionLabel.isNotBlank()) {
            Spacer(Modifier.height(20.dp))
            Button(onClick = onAction, colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)) {
                Text(actionLabel)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// KIRAN TEXT FIELD
// ─────────────────────────────────────────────────────────
@Composable
fun KiranTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    keyboardType: androidx.compose.ui.text.input.KeyboardType = androidx.compose.ui.text.input.KeyboardType.Text,
    isError: Boolean = false,
    errorText: String = ""
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = if (leadingIcon != null) {{ Icon(leadingIcon, contentDescription = null, tint = OrangePrimary) }} else null,
            trailingIcon = trailingIcon,
            singleLine = singleLine,
            isError = isError,
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = OrangePrimary,
                focusedLabelColor = OrangePrimary,
                cursorColor = OrangePrimary
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if (isError && errorText.isNotBlank()) {
            Text(errorText, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 16.dp, top = 4.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────
// PRIMARY BUTTON
// ─────────────────────────────────────────────────────────
@Composable
fun PrimaryButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true, isLoading: Boolean = false) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(52.dp),
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
        shape = RoundedCornerShape(12.dp)
    ) {
        if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
        else Text(label, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
    }
}

// ─────────────────────────────────────────────────────────
// VOICE COMMAND DIALOG
// ─────────────────────────────────────────────────────────
@Composable
fun VoiceCommandDialog(
    isListening: Boolean,
    recognizedText: String,
    onDismiss: () -> Unit
) {
    if (isListening || recognizedText.isNotBlank()) {
        AlertDialog(
            onDismissRequest = onDismiss,
            icon = {
                Icon(Icons.Filled.Mic, contentDescription = null, tint = OrangePrimary, modifier = Modifier.size(32.dp))
            },
            title = { Text(if (isListening) "सुन रहा हूँ…" else "Processing…", fontWeight = FontWeight.Bold) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    if (isListening) {
                        CircularProgressIndicator(color = OrangePrimary)
                        Spacer(Modifier.height(12.dp))
                        Text("बोलिए…", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        Text(recognizedText, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = onDismiss) { Text("Cancel", color = OrangePrimary) }
            }
        )
    }
}
