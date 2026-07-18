package com.daemondroid.app.ui.windows

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
import com.daemondroid.app.ui.components.DriveSelector
import com.daemondroid.app.ui.components.FlashProgressState
import com.daemondroid.app.ui.components.ProgressOverlay
import com.daemondroid.app.ui.components.WarningDialog
import com.daemondroid.app.ui.flash.StepCard
import com.daemondroid.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WindowsFlashScreen(
    onBack: () -> Unit,
    onViewLog: () -> Unit,
) {
    var selectedIsoName by remember { mutableStateOf<String?>(null) }
    var wimSplitEnabled by remember { mutableStateOf(true) }
    var isFlashing by remember { mutableStateOf(false) }
    var showWarnDialog by remember { mutableStateOf(false) }
    val progressState by remember { mutableStateOf(FlashProgressState(phase = "Ready")) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { Text("Windows Installer", color = TextPrimary, fontWeight = FontWeight.SemiBold) },
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
                        onClick = { showWarnDialog = true },
                        enabled = selectedIsoName != null && !isFlashing,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CardWindowsAccent.copy(alpha = 0.85f),
                            contentColor = DarkBackground,
                        ),
                    ) {
                        Icon(Icons.Filled.Window, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Create Windows USB", fontWeight = FontWeight.Bold)
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
            // UEFI info chip
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = CardWindowsAccent.copy(alpha = 0.1f),
            ) {
                Row(
                    Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Filled.Computer, null, tint = CardWindowsAccent, modifier = Modifier.size(14.dp))
                    Text("UEFI Boot Mode · FAT32 Partition", style = MaterialTheme.typography.labelSmall.copy(color = CardWindowsAccent))
                }
            }

            StepCard(1, "Select Windows ISO") {
                Surface(
                    onClick = { selectedIsoName = "Win11_24H2_x64.iso" },
                    shape = RoundedCornerShape(10.dp),
                    color = DarkSurface,
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(
                            if (selectedIsoName != null) Icons.Filled.CheckCircle else Icons.Filled.FolderOpen,
                            null, tint = if (selectedIsoName != null) GreenSuccess else TextTertiary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            selectedIsoName ?: "Tap to pick Windows ISO...",
                            style = MaterialTheme.typography.bodyMedium.copy(color = if (selectedIsoName != null) TextPrimary else TextTertiary)
                        )
                    }
                }
            }

            StepCard(2, "Select Target Drive") {
                DriveSelector(devices = emptyList(), selectedDevice = null, onSelectDevice = {})
            }

            StepCard(3, "WIM Split Settings") {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Auto-split install.wim", style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary))
                            Text("Required for files > 4 GB on FAT32 (uses wimlib-imagex)", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        }
                        Switch(
                            checked = wimSplitEnabled, onCheckedChange = { wimSplitEnabled = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = DaemonPurple, checkedThumbColor = DaemonPurpleLight),
                        )
                    }
                    if (wimSplitEnabled) {
                        Surface(shape = RoundedCornerShape(8.dp), color = DaemonPurple.copy(alpha = 0.08f)) {
                            Text(
                                "📦 wimlib-imagex will be downloaded on first use (~2.1 MB)",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                                modifier = Modifier.padding(10.dp),
                            )
                        }
                    }
                }
            }

            if (isFlashing) {
                ProgressOverlay(state = progressState, onAbort = { isFlashing = false })
            }
        }
    }

    if (showWarnDialog) {
        WarningDialog(
            title = "Format & Flash Drive?",
            message = "The selected drive will be formatted as FAT32 and all data will be erased. " +
                    "The Windows installer files will then be extracted from the ISO.",
            confirmLabel = "Format & Create",
            onConfirm = { showWarnDialog = false; isFlashing = true },
            onDismiss = { showWarnDialog = false },
            isDangerous = true,
        )
    }
}
