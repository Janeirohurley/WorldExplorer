package com.janeirohurley.worldexplorer.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize

@Composable
fun SettingsFragment() {
    var isDarkMode by remember { mutableStateOf(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Paramètres", style = MaterialTheme.typography.headlineMedium)
        Switch(
            checked = isDarkMode,
            onCheckedChange = {
                isDarkMode = it
                AppCompatDelegate.setDefaultNightMode(
                    if (it) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        )
        Text(text = "Mode sombre")
    }
}