package com.kiranstore.manager.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

// ─────────────────────────────────────────────
// ROUTE CONSTANTS
// ─────────────────────────────────────────────
object Routes {
    // Auth
    const val LOGIN        = "login"
    const val SIGNUP       = "signup"
    const val SHOP_SETUP   = "shop_setup"

    // Main
    const val HOME         = "home"
    const val UDHAAR       = "udhaar"
    const val UDHAAR_DETAIL = "udhaar_detail/{customerId}"
    const val RENTAL       = "rental"
    const val BUY_LIST     = "buy_list"
    const val TASKS        = "tasks"
    const val CUSTOMERS    = "customers"
    const val SETTINGS     = "settings"

    // Helpers
    fun udhaarDetail(customerId: Long) = "udhaar_detail/$customerId"
}

// ─────────────────────────────────────────────
// BOTTOM NAV ITEMS
// ─────────────────────────────────────────────
sealed class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : BottomNavItem(
        route = Routes.HOME,
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    object Udhaar : BottomNavItem(
        route = Routes.UDHAAR,
        label = "Udhaar",
        selectedIcon = Icons.Filled.AccountBalanceWallet,
        unselectedIcon = Icons.Outlined.AccountBalanceWallet
    )

    object Rent : BottomNavItem(
        route = Routes.RENTAL,
        label = "Rent",
        selectedIcon = Icons.Filled.Build,
        unselectedIcon = Icons.Outlined.Build
    )

    object Customers : BottomNavItem(
        route = Routes.CUSTOMERS,
        label = "Customers",
        selectedIcon = Icons.Filled.People,
        unselectedIcon = Icons.Outlined.People
    )
}
