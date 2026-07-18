package com.daemondroid.app.ui.settings

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.daemondroid.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    var verifyByDefault by remember { mutableStateOf(true) }
    var autoDetectDrives by remember { mutableStateOf(true) }
    var logRetentionDays by remember { mutableStateOf(30) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = TextPrimary, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, "Back", tint = TextSecondary) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground),
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            SettingsSection("Flash Options") {
                ToggleSettingRow(
                    icon = Icons.Filled.Verified,
                    title = "Verify after flash",
                    subtitle = "SHA256 checksum check post-write",
                    checked = verifyByDefault,
                    onCheckedChange = { verifyByDefault = it },
                )
                ToggleSettingRow(
                    icon = Icons.Filled.Usb,
                    title = "Auto-detect drives",
                    subtitle = "Scan for USB/SD devices on open",
                    checked = autoDetectDrives,
                    onCheckedChange = { autoDetectDrives = it },
                )
            }

            SettingsSection("Logs") {
                ActionSettingRow(
                    icon = Icons.Filled.History,
                    title = "Log retention",
                    subtitle = "Keep logs for $logRetentionDays days",
                    trailingLabel = "$logRetentionDays days",
                    onClick = { /* open picker */ },
                )
                ActionSettingRow(
                    icon = Icons.Filled.DeleteSweep,
                    title = "Clear all logs",
                    subtitle = "Delete all recorded operations",
                    onClick = { /* confirm + clear */ },
                    isDangerous = true,
                )
            }

            SettingsSection("Binaries") {
                ActionSettingRow(
                    icon = Icons.Filled.Download,
                    title = "Download required binaries",
                    subtitle = "parted, wimlib-imagex — ARM64",
                    onClick = { /* trigger download */ },
                )
                ActionSettingRow(
                    icon = Icons.Filled.DeleteForever,
                    title = "Remove cached binaries",
                    subtitle = "Free storage — re-downloaded on demand",
                    onClick = { /* clear binary cache */ },
                    isDangerous = true,
                )
            }

            SettingsSection("Access") {
                ActionSettingRow(
                    icon = Icons.Filled.AdminPanelSettings,
                    title = "Reconfigure access level",
                    subtitle = "Change Root / Shizuku / USB Host mode",
                    onClick = { /* navigate to wizard */ },
                )
            }

            SettingsSection("About") {
                ActionSettingRow(
                    icon = Icons.Filled.Info,
                    title = "DaemonDroid",
                    subtitle = "v1.0.0 · Open Source · GPL-3.0",
                    onClick = { },
                )
                ActionSettingRow(
                    icon = Icons.Filled.Code,
                    title = "Source Code",
                    subtitle = "github.com/daemondroid/app",
                    onClick = { },
                )
                ActionSettingRow(
                    icon = Icons.Filled.Gavel,
                    title = "Open Source Licenses",
                    subtitle = "wimlib, parted, Ventoy, and more",
                    onClick = { },
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            title.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                color = DaemonPurpleLight,
                fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier.padding(bottom = 6.dp),
        )
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = DarkSurfaceVariant,
            modifier = Modifier.fillMaxWidth().border(1.dp, DarkOutline, RoundedCornerShape(14.dp)),
        ) {
            Column { content() }
        }
    }
}

@Composable
private fun ToggleSettingRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(icon, null, tint = DaemonPurpleLight, modifier = Modifier.size(20.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary))
            Text(subtitle, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedTrackColor = DaemonPurple, checkedThumbColor = DaemonPurpleLight),
        )
    }
}

@Composable
private fun ActionSettingRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    trailingLabel: String? = null,
    onClick: () -> Unit,
    isDangerous: Boolean = false,
) {
    Surface(onClick = onClick, color = DarkSurfaceVariant) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(icon, null, tint = if (isDangerous) RedDanger else DaemonPurpleLight, modifier = Modifier.size(20.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyMedium.copy(color = if (isDangerous) RedDanger else TextPrimary))
                Text(subtitle, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
            }
            if (trailingLabel != null) {
                Text(trailingLabel, style = MaterialTheme.typography.bodySmall.copy(color = TextTertiary))
            } else {
                Icon(Icons.Filled.ChevronRight, null, tint = TextTertiary, modifier = Modifier.size(16.dp))
            }
        }
    }
}
