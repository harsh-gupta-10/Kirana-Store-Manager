package com.kiranstore.manager.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.kiranstore.manager.ui.screens.buylist.BuyListScreen
import com.kiranstore.manager.ui.screens.customers.*
import com.kiranstore.manager.ui.screens.home.HomeScreen
import com.kiranstore.manager.ui.screens.rentals.*
import com.kiranstore.manager.ui.screens.settings.SettingsScreen
import com.kiranstore.manager.ui.screens.tasks.TasksScreen
import com.kiranstore.manager.ui.screens.udhar.*
import com.kiranstore.manager.ui.theme.*

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    // Determine if bottom bar should be shown
    val bottomBarRoutes = setOf(
        Screen.Home.route,
        Screen.Udhaar.route,
        Screen.Rentals.route,
        Screen.Customers.route
    )
    val showBottomBar = bottomBarRoutes.any { currentRoute?.startsWith(it) == true }

    Scaffold(
        containerColor = BackgroundGrey,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = CardWhite,
                    tonalElevation = 8.dp
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(item.icon, contentDescription = item.label)
                            },
                            label = {
                                Text(item.label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = OrangePrimary,
                                selectedTextColor = OrangePrimary,
                                indicatorColor = OrangeLight,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route
            ) {
                // ── Home ──────────────────────────────────────────────────
                composable(Screen.Home.route) {
                    HomeScreen(
                        onAddUdhaar = { navController.navigate(Screen.AddUdhaar.createRoute()) },
                        onAddRental = { navController.navigate(Screen.AddRental.route) },
                        onNavigateToTasks = { navController.navigate(Screen.Tasks.route) },
                        onNavigateToBuyList = { navController.navigate(Screen.BuyList.route) },
                        onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                    )
                }

                // ── Customers ─────────────────────────────────────────────
                composable(Screen.Customers.route) {
                    CustomersScreen(
                        onCustomerClick = { id ->
                            navController.navigate(Screen.CustomerDetails.createRoute(id))
                        },
                        onAddCustomer = { navController.navigate(Screen.AddCustomer.route) },
                        onAddUdhaarForCustomer = { id ->
                            navController.navigate(Screen.AddUdhaar.createRoute(id))
                        }
                    )
                }

                composable(Screen.AddCustomer.route) {
                    AddCustomerScreen(
                        onSaved = { navController.popBackStack() },
                        onCancel = { navController.popBackStack() }
                    )
                }

                composable(
                    route = Screen.CustomerDetails.route,
                    arguments = listOf(navArgument("customerId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val customerId = backStackEntry.arguments?.getLong("customerId") ?: return@composable
                    CustomerDetailsScreen(
                        customerId = customerId,
                        onBack = { navController.popBackStack() },
                        onAddUdhaar = {
                            navController.navigate(Screen.AddUdhaar.createRoute(customerId))
                        },
                        onAddPayment = {
                            navController.navigate(Screen.AddPayment.createRoute(customerId))
                        },
                        onAddRental = { navController.navigate(Screen.AddRental.route) }
                    )
                }

                // ── Udhaar ────────────────────────────────────────────────
                composable(Screen.Udhaar.route) {
                    UdhaarScreen(
                        onAddUdhaar = { navController.navigate(Screen.AddUdhaar.createRoute()) }
                    )
                }

                composable(
                    route = Screen.AddUdhaar.route,
                    arguments = listOf(navArgument("customerId") {
                        type = NavType.LongType
                        defaultValue = -1L
                    })
                ) { backStackEntry ->
                    val customerId = backStackEntry.arguments?.getLong("customerId") ?: -1L
                    AddUdhaarScreen(
                        preselectedCustomerId = customerId,
                        onSaved = { navController.popBackStack() },
                        onCancel = { navController.popBackStack() }
                    )
                }

                composable(
                    route = Screen.AddPayment.route,
                    arguments = listOf(navArgument("customerId") {
                        type = NavType.LongType
                        defaultValue = -1L
                    })
                ) { backStackEntry ->
                    val customerId = backStackEntry.arguments?.getLong("customerId") ?: -1L
                    AddPaymentScreen(
                        preselectedCustomerId = customerId,
                        onSaved = { navController.popBackStack() },
                        onCancel = { navController.popBackStack() }
                    )
                }

                // ── Rentals ───────────────────────────────────────────────
                composable(Screen.Rentals.route) {
                    RentalsScreen(
                        onAddRental = { navController.navigate(Screen.AddRental.route) },
                        onReturnRental = { rentalId ->
                            navController.navigate(Screen.ReturnRental.createRoute(rentalId))
                        }
                    )
                }

                composable(Screen.AddRental.route) {
                    AddRentalScreen(
                        onSaved = { navController.popBackStack() },
                        onCancel = { navController.popBackStack() }
                    )
                }

                composable(
                    route = Screen.ReturnRental.route,
                    arguments = listOf(navArgument("rentalId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val rentalId = backStackEntry.arguments?.getLong("rentalId") ?: return@composable
                    ReturnRentalScreen(
                        rentalId = rentalId,
                        onCompleted = { navController.popBackStack() },
                        onCancel = { navController.popBackStack() }
                    )
                }

                // ── Tasks / Buy List / Settings ─────────────────────────
                composable(Screen.Tasks.route) { TasksScreen() }
                composable(Screen.BuyList.route) { BuyListScreen() }
                composable(Screen.Settings.route) { SettingsScreen() }
            }
        }
    }
}
