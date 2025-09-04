package com.janeirohurley.worldexplorer.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.janeirohurley.worldexplorer.data.Country
import com.janeirohurley.worldexplorer.ui.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailFragment(
    navController: NavController,
    country: Country,
    viewModel: HomeViewModel
) {
    val currentCountry by viewModel.getCountryByName(country.name)
        .collectAsState(initial = country)

    val comments by viewModel.getCommentsForCountry(country.name)
        .collectAsState(initial = emptyList())

    var newComment by remember { mutableStateOf("") }
    var showComments by remember { mutableStateOf(false) }

    currentCountry?.let { country ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(country.name) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                        }
                    }
                )
            },
            floatingActionButton = {
                Row {
                    FloatingActionButton(
                        onClick = { viewModel.toggleFavorite(country) },
                        containerColor = if (country.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        contentColor = if (country.isFavorite) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    ) {
                        Icon(
                            imageVector = if (country.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite"
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // FAB avec badge
                    BadgedBox(
                        badge = {
                            if (comments.isNotEmpty()) {
                                Badge {
                                    Text(comments.size.toString())
                                }
                            }
                        }
                    ) {
                        FloatingActionButton(
                            onClick = { showComments = !showComments },
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ) {
                            Icon(Icons.Filled.Comment, contentDescription = "Comments")
                        }
                    }
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {

                    // 🌍 Drapeau
                    Surface(
                        shape = RoundedCornerShape(0.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        AsyncImage(
                            model = country.flagUrl,
                            contentDescription = "Drapeau de ${country.name}",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Nom officiel
                    Text(
                        text = country.officialName,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Details
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        InfoRowWithIcon(label = "Capitale", value = country.capital, icon = Icons.Filled.LocationCity)
                        InfoRowWithIcon(label = "Région", value = country.region, icon = Icons.Filled.Public)
                        InfoRowWithIcon(label = "Population", value = country.population.toString(), icon = Icons.Filled.People)
                        InfoRowWithIcon(label = "Monnaie", value = country.currency, icon = Icons.Filled.AttachMoney)
                        InfoRowWithIcon(label = "Langue", value = country.language, icon = Icons.Filled.Language)
                    }


                    Spacer(modifier = Modifier.height(24.dp))

                    // 💬 Section commentaires
                    if (showComments) {
                        Text("Commentaires", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Liste
                        comments.forEach { comment ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Text(
                                    text = comment.text,
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Input + bouton Post
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = newComment,
                                onValueChange = { newComment = it },
                                label = { Text("Écrire un commentaire...") },
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (newComment.isNotBlank()) {
                                        viewModel.addCommentHybrid(country.name, newComment)
                                        newComment = ""
                                    }
                                }
                            ) {
                                Text("Post")
                            }

                        }

                        Spacer(modifier = Modifier.height(70.dp))
                    }
                }
            }
        }
    }
}
