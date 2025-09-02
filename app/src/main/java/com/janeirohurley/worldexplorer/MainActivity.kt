package com.janeirohurley.worldexplorer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.janeirohurley.worldexplorer.data.Country
import com.janeirohurley.worldexplorer.ui.detail.DetailFragment
import com.janeirohurley.worldexplorer.ui.favorites.FavoritesFragment
import com.janeirohurley.worldexplorer.ui.home.HomeFragment
import com.janeirohurley.worldexplorer.ui.home.HomeViewModel
import com.janeirohurley.worldexplorer.ui.theme.WorldExplorerTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorldExplorerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    val isBottomBarVisible by derivedStateOf {
                        currentDestination?.hierarchy?.any {
                            it.route == "home" || it.route == "favorites"
                        } == true
                    }

                    Scaffold(
                        bottomBar = {
                            if (isBottomBarVisible) {
                                NavigationBar(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
                                    contentColor = MaterialTheme.colorScheme.onSurface,
                                    tonalElevation = 4.dp
                                ) {
                                    val viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                                    val countries by viewModel.countries.collectAsState()

                                    // Nombre total de pays
                                    val totalCountries = countries.size

                                    // Nombre de favoris
                                    val favoriteCount = countries.count { it.isFavorite }

                                    // Home item avec badge dynamique
                                    NavigationBarItem(
                                        selected = currentDestination?.hierarchy?.any { it.route == "home" } == true,
                                        onClick = {
                                            navController.navigate("home") {
                                                popUpTo(navController.graph.findStartDestination().id)
                                                launchSingleTop = true
                                            }
                                        },
                                        icon = {
                                            BadgedBox(
                                                badge = {
                                                    if (totalCountries > 0) {
                                                        Badge { Text(totalCountries.toString()) }
                                                    }
                                                }
                                            ) {
                                                Icon(Icons.Default.Home, contentDescription = stringResource(R.string.home))
                                            }
                                        },
                                        label = {
                                            Text(stringResource(R.string.home), fontWeight = FontWeight.Medium, fontSize = 12.sp)
                                        }
                                    )

                                    // Favorites item avec badge dynamique
                                    NavigationBarItem(
                                        selected = currentDestination?.hierarchy?.any { it.route == "favorites" } == true,
                                        onClick = {
                                            navController.navigate("favorites") {
                                                popUpTo(navController.graph.findStartDestination().id)
                                                launchSingleTop = true
                                            }
                                        },
                                        icon = {
                                            BadgedBox(
                                                badge = {
                                                    if (favoriteCount > 0) {
                                                        Badge { Text(favoriteCount.toString()) }
                                                    }
                                                }
                                            ) {
                                                Icon(Icons.Default.Favorite, contentDescription = stringResource(R.string.favorites))
                                            }
                                        },
                                        label = {
                                            Text(stringResource(R.string.favorites), fontWeight = FontWeight.Medium, fontSize = 12.sp)
                                        }
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("home") { HomeFragment(navController) }
                            composable("favorites") { FavoritesFragment(navController) }

                            composable("detail/{countryName}") { backStackEntry ->
                                val viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                                val countries by viewModel.countries.collectAsState()
                                val isLoading by viewModel.isLoading.collectAsState()
                                val countryName = backStackEntry.arguments?.getString("countryName")
                                val country = countries.find { it.name == countryName }

                                when {
                                    isLoading -> {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                    country != null -> {
                                        DetailFragment(navController, country,viewModel)
                                    }
                                    else -> {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("Pays non trouvé")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
