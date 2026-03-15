package com.kiranstore.manager.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.kiranstore.manager.ui.screens.auth.*
import com.kiranstore.manager.ui.screens.buylist.BuyListScreen
import com.kiranstore.manager.ui.screens.customers.CustomersScreen
import com.kiranstore.manager.ui.screens.home.HomeScreen
import com.kiranstore.manager.ui.screens.rental.RentalScreen
import com.kiranstore.manager.ui.screens.settings.SettingsScreen
import com.kiranstore.manager.ui.screens.setup.ShopSetupScreen
import com.kiranstore.manager.ui.screens.tasks.TasksScreen
import com.kiranstore.manager.ui.screens.udhaar.UdhaarDetailScreen
import com.kiranstore.manager.ui.screens.udhaar.UdhaarScreen
import com.kiranstore.manager.ui.theme.OrangePrimary

private val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Udhaar,
    BottomNavItem.Rent,
    BottomNavItem.Customers
)

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String
) {
    val currentBack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBack?.destination?.route

    val showBottomBar = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute == item.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        this.saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = OrangePrimary,
                                selectedTextColor = OrangePrimary,
                                indicatorColor = OrangePrimary.copy(alpha = 0.12f)
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.LOGIN)      { LoginScreen(navController) }
            composable(Routes.SIGNUP)     { SignupScreen(navController) }
            composable(Routes.SHOP_SETUP) { ShopSetupScreen(navController) }
            composable(Routes.HOME)       { HomeScreen(navController) }
            composable(Routes.UDHAAR)     { UdhaarScreen(navController) }
            composable(
                route = Routes.UDHAAR_DETAIL,
                arguments = listOf(navArgument("customerId") { type = NavType.LongType })
            ) { back ->
                val customerId = back.arguments?.getLong("customerId") ?: return@composable
                UdhaarDetailScreen(navController, customerId)
            }
            composable(Routes.RENTAL)     { RentalScreen(navController) }
            composable(Routes.CUSTOMERS)  { CustomersScreen(navController) }
            composable(Routes.BUY_LIST)   { BuyListScreen(navController) }
            composable(Routes.TASKS)      { TasksScreen(navController) }
            composable(Routes.SETTINGS)   { SettingsScreen(navController) }
        }
    }
}
