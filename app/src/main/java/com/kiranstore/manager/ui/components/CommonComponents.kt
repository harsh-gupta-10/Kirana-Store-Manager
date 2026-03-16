package com.kiranstore.manager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiranstore.manager.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

// ── Avatar with initials ──────────────────────────────────────────────────────
private val avatarColors = listOf(
    AvatarBg1 to OrangePrimary,
    AvatarBg2 to Color(0xFF1976D2),
    AvatarBg3 to GreenSuccess,
    AvatarBg4 to Color(0xFF7B1FA2),
    AvatarBg5 to TextSecondary
)

fun initialsOf(name: String): String {
    val parts = name.trim().split(" ")
    return when {
        parts.size >= 2 -> "${parts[0].first()}${parts[1].first()}".uppercase()
        parts.isNotEmpty() -> parts[0].take(2).uppercase()
        else -> "?"
    }
}

@Composable
fun AvatarCircle(name: String, size: Int = 44) {
    val idx = (name.hashCode() and 0x7FFFFFFF) % avatarColors.size
    val (bg, fg) = avatarColors[idx]
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initialsOf(name),
            color = fg,
            fontSize = (size * 0.35).sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ── Summary Card ─────────────────────────────────────────────────────────────
@Composable
fun SummaryCard(
    title: String,
    value: String,
    subtitle: String = "",
    valueColor: Color = TextPrimary,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 11.sp, color = TextSecondary,
                fontWeight = FontWeight.Medium, letterSpacing = 0.5.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = valueColor)
            if (subtitle.isNotBlank()) {
                Text(subtitle, fontSize = 11.sp, color = TextSecondary)
            }
        }
    }
}

// ── Amount Badge ─────────────────────────────────────────────────────────────
@Composable
fun AmountBadge(amount: Double, positive: Boolean = false) {
    val color = if (positive) GreenSuccess else RedDanger
    Text(
        text = "${if (positive) "+ " else "- "}₹${formatAmount(amount)}",
        color = color,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp
    )
}

// ── Status Badge ─────────────────────────────────────────────────────────────
@Composable
fun StatusBadge(status: String) {
    val (bg, fg) = when (status.uppercase()) {
        "ACTIVE" -> OrangeLight to OrangePrimary
        "RETURNED" -> GreenLight to GreenSuccess
        "LATE" -> RedLight to RedDanger
        else -> Color(0xFFF3F4F6) to TextSecondary
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(status, fontSize = 11.sp, color = fg, fontWeight = FontWeight.SemiBold)
    }
}

// ── Section Header ────────────────────────────────────────────────────────────
@Composable
fun SectionHeader(title: String, action: String = "", onAction: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
        if (action.isNotBlank()) {
            Text(
                text = action,
                color = OrangePrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onAction() }
            )
        }
    }
}

// ── Customer Picker Dialog ───────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerSearchField(
    selectedName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = selectedName,
        onValueChange = {},
        readOnly = true,
        label = { Text("Customer") },
        placeholder = { Text("Select customer...") },
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        enabled = false,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = TextPrimary,
            disabledBorderColor = DividerColor,
            disabledLabelColor = TextSecondary
        )
    )
}

// ── Helpers ───────────────────────────────────────────────────────────────────
fun formatAmount(amount: Double): String {
    return if (amount == amount.toLong().toDouble())
        "%,d".format(amount.toLong())
    else
        "%,.2f".format(amount)
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun formatDateTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy • hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun formatTimeAgo(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    return when {
        diff < 60_000 -> "Just now"
        diff < 3_600_000 -> "${diff / 60_000} min ago"
        diff < 86_400_000 -> "${diff / 3_600_000} hours ago"
        diff < 172_800_000 -> "Yesterday"
        else -> formatDate(timestamp)
    }
}
