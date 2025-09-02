package com.janeirohurley.worldexplorer.ui.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.janeirohurley.worldexplorer.data.Country

@Composable
fun CountryItem(country: Country, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp) // Margin for card spacing
            .clickable { onClick() }
            .animateContentSize(), // Smooth animation for dynamic content
        shape = RoundedCornerShape(12.dp), // Rounded corners for modern look
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), // Very light, non-white background
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Consistent spacing
        ) {
            // Row for flag and country name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Flag Image
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(country.flagUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Drapeau de ${country.name}",
                    modifier = Modifier
                        .size(40.dp) // Smaller size to align with text
                        .clip(CircleShape) // Circular flag for modern aesthetic
                        .background(MaterialTheme.colorScheme.background)
                        .padding(2.dp) // Padding to avoid flag touching edges
                )

                // Country Name
                Text(
                    text = country.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold, // Bold for emphasis
                        fontSize = 18.sp // Prominent size
                    ),
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.primary // Vibrant color
                )
            }

            // Details Column
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Region
                Text(
                    text = country.region,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    ),
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Subtle color
                )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Population
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Population",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary // Consistent color
                    )
                    Text(
                        text = "${country.population}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Capital
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationCity,
                        contentDescription = "Capitale",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = country.capital,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            }
        }
    }
}