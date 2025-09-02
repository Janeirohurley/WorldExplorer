package com.janeirohurley.worldexplorer.ui.home

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeFragment(
    navController: NavController,
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val countries by viewModel.countries.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedContinent by remember { mutableStateOf<String?>(null) }

    val continents = listOf("Africa", "Americas", "Asia", "Europe", "Oceania")
    val filteredCountries = countries.filter { country ->
        country.name.contains(searchQuery, ignoreCase = true) &&
                (selectedContinent == null || country.region == selectedContinent)
    }

    val isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("World Explorer") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { viewModel.refreshCountries() }, // Ajoute cette fonction dans ton ViewModel
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(12.dp)
                ) {
                    // Search
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Rechercher un pays") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    // Filter Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = selectedContinent == null,
                                onClick = { selectedContinent = null },
                                label = { Text("Tous") }
                            )
                        }
                        items(continents) { continent ->
                            FilterChip(
                                selected = selectedContinent == continent,
                                onClick = { selectedContinent = continent },
                                label = { Text(continent) }
                            )
                        }
                    }

                    // Contenu
                    when {
                        isLoading -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                        error != null -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(error ?: "Erreur inconnue")
                            }
                        }
                        else -> {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                items(filteredCountries) { country ->
                                    CountryItem(country = country) {
                                        navController.navigate("detail/${Uri.encode(country.name)}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
