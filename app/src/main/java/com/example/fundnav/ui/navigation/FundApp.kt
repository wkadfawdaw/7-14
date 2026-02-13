package com.example.fundnav.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fundnav.repository.FundRepository
import com.example.fundnav.ui.screen.FundDetailScreen
import com.example.fundnav.ui.screen.FundSearchScreen
import com.example.fundnav.ui.screen.WatchListScreen
import com.example.fundnav.ui.viewmodel.FundViewModel

sealed class Destinations(val route: String, val title: String) {
    data object Search : Destinations("search", "搜基金")
    data object Detail : Destinations("detail", "持仓&净值")
    data object Watch : Destinations("watch", "自选&提醒")
}

@Composable
fun FundApp(repository: FundRepository) {
    val viewModel: FundViewModel = viewModel(factory = remember {
        FundViewModel.Factory(repository)
    })
    val navController = rememberNavController()
    val tabs = listOf(Destinations.Search, Destinations.Detail, Destinations.Watch)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                tabs.forEach { destination ->
                    NavigationBarItem(
                        icon = {
                            val icon = when (destination) {
                                is Destinations.Search -> Icons.Default.Home
                                is Destinations.Detail -> Icons.Default.ShowChart
                                is Destinations.Watch -> Icons.Default.List
                            }
                            Icon(imageVector = icon, contentDescription = destination.title)
                        },
                        label = { Text(destination.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destinations.Search.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destinations.Search.route) {
                FundSearchScreen(viewModel = viewModel)
            }
            composable(Destinations.Detail.route) {
                FundDetailScreen(viewModel = viewModel)
            }
            composable(Destinations.Watch.route) {
                WatchListScreen(viewModel = viewModel)
            }
        }
    }
}
