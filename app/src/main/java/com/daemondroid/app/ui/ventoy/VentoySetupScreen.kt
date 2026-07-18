package com.daemondroid.app.ui.ventoy

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.daemondroid.app.data.model.VentoyTheme
import com.daemondroid.app.ui.components.DriveSelector
import com.daemondroid.app.ui.components.FlashProgressState
import com.daemondroid.app.ui.components.ProgressOverlay
import com.daemondroid.app.ui.components.WarningDialog
import com.daemondroid.app.ui.flash.StepCard
import com.daemondroid.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentoySetupScreen(
    onBack: () -> Unit,
    onNavigateToThemePicker: () -> Unit,
    onViewLog: () -> Unit,
) {
    var selectedTheme by remember { mutableStateOf(VentoyTheme.VIMIX) }
    var secureBoot by remember { mutableStateOf(true) }
    var showWipeWarning by remember { mutableStateOf(false) }
    var isInstalling by remember { mutableStateOf(false) }
    val progressState by remember { mutableStateOf(FlashProgressState(phase = "Ready")) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { Text("Ventoy Maker", color = TextPrimary, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, "Back", tint = TextSecondary) }
                },
                actions = {
                    IconButton(onClick = onViewLog) { Icon(Icons.Filled.Terminal, "Log", tint = TextSecondary) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground),
            )
        },
        bottomBar = {
            Surface(color = DarkSurface) {
                Box(Modifier.fillMaxWidth().padding(16.dp)) {
                    Button(
                        onClick = { showWipeWarning = true },
                        enabled = !isInstalling,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CardVentoyAccent.copy(alpha = 0.85f),
                            contentColor = DarkBackground,
                        ),
                    ) {
                        Icon(Icons.Filled.LayersClear, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Install Ventoy", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Info banner
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CardVentoyAccent.copy(alpha = 0.08f),
                modifier = Modifier.fillMaxWidth().border(1.dp, CardVentoyAccent.copy(alpha = 0.25f), RoundedCornerShape(12.dp)),
            ) {
                Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Info, null, tint = CardVentoyAccent, modifier = Modifier.size(18.dp))
                    Text(
                        "Ventoy will create a multi-boot USB. Drop any ISO file onto the Ventoy partition — no reflashing needed.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                    )
                }
            }

            StepCard(1, "Select Target Drive") {
                DriveSelector(devices = emptyList(), selectedDevice = null, onSelectDevice = {})
            }

            StepCard(2, "GRUB Theme") {
                ThemeQuickSelect(
                    selectedTheme = selectedTheme,
                    onSelect = { selectedTheme = it },
                    onBrowseThemes = onNavigateToThemePicker,
                )
            }

            StepCard(3, "Options") {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Secure Boot Support", style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary))
                        Text("Includes MOK enrollment files", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    }
                    Switch(checked = secureBoot, onCheckedChange = { secureBoot = it },
                        colors = SwitchDefaults.colors(checkedTrackColor = DaemonPurple, checkedThumbColor = DaemonPurpleLight))
                }
            }

            if (isInstalling) {
                ProgressOverlay(state = progressState, onAbort = { isInstalling = false })
            }
        }
    }

    if (showWipeWarning) {
        WarningDialog(
            title = "⚠️ Drive Will Be Wiped",
            message = "Installing Ventoy will COMPLETELY ERASE the selected drive and repartition it. " +
                    "All existing data will be lost permanently.\n\nThis will use the official Ventoy2Disk.sh script. Continue?",
            confirmLabel = "Wipe & Install Ventoy",
            onConfirm = { showWipeWarning = false; isInstalling = true },
            onDismiss = { showWipeWarning = false },
            isDangerous = true,
        )
    }
}

@Composable
private fun ThemeQuickSelect(
    selectedTheme: VentoyTheme,
    onSelect: (VentoyTheme) -> Unit,
    onBrowseThemes: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(VentoyTheme.VIMIX, VentoyTheme.SLEEK, VentoyTheme.TELA).forEach { theme ->
                val isSelected = selectedTheme == theme
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelect(theme) },
                    label = { Text(theme.displayName) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CardVentoyAccent.copy(alpha = 0.2f),
                        selectedLabelColor = CardVentoyAccent,
                    ),
                )
            }
        }
        TextButton(onClick = onBrowseThemes, contentPadding = PaddingValues(0.dp)) {
            Icon(Icons.Filled.Palette, null, tint = CardVentoyAccent, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(4.dp))
            Text("Browse & Preview Themes", color = CardVentoyAccent)
        }
    }
}
