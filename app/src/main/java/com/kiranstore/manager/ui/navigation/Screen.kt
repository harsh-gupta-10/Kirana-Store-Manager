package com.kiranstore.manager.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Customers : Screen("customers")
    object AddCustomer : Screen("add_customer")
    object CustomerDetails : Screen("customer_details/{customerId}") {
        fun createRoute(customerId: Long) = "customer_details/$customerId"
    }
    object Udhaar : Screen("udhaar")
    object AddUdhaar : Screen("add_udhaar?customerId={customerId}") {
        fun createRoute(customerId: Long? = null) =
            if (customerId != null) "add_udhaar?customerId=$customerId" else "add_udhaar?customerId=-1"
    }
    object AddPayment : Screen("add_payment?customerId={customerId}") {
        fun createRoute(customerId: Long? = null) =
            if (customerId != null) "add_payment?customerId=$customerId" else "add_payment?customerId=-1"
    }
    object Rentals : Screen("rentals")
    object AddRental : Screen("add_rental")
    object ReturnRental : Screen("return_rental/{rentalId}") {
        fun createRoute(rentalId: Long) = "return_rental/$rentalId"
    }
    object Tasks : Screen("tasks")
    object BuyList : Screen("buy_list")
    object Settings : Screen("settings")
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Filled.Home, Screen.Home.route),
    BottomNavItem("Udhaar", Icons.Filled.AccountBalanceWallet, Screen.Udhaar.route),
    BottomNavItem("Rent", Icons.Filled.Construction, Screen.Rentals.route),
    BottomNavItem("Customers", Icons.Filled.People, Screen.Customers.route)
)
