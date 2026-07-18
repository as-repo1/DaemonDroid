package com.daemondroid.app.ui.log

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daemondroid.app.data.model.LogEntry
import com.daemondroid.app.data.model.OperationPhase
import com.daemondroid.app.data.model.OperationResult
import com.daemondroid.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationLogScreen(onBack: () -> Unit) {
    // Sample log entries for Phase 1 UI preview
    val sampleLogs = remember {
        listOf(
            LogEntry(1, System.currentTimeMillis() - 5000, OperationPhase.SYSTEM, "App started", result = OperationResult.SUCCESS, durationMs = 0),
            LogEntry(2, System.currentTimeMillis() - 4000, OperationPhase.PRIVILEGE, "Detecting privilege level", command = "id", stdout = "uid=0(root)", result = OperationResult.SUCCESS, durationMs = 12),
            LogEntry(3, System.currentTimeMillis() - 3000, OperationPhase.DEVICE, "Enumerating block devices", command = "lsblk -J", result = OperationResult.SUCCESS, durationMs = 45),
            LogEntry(4, System.currentTimeMillis() - 2000, OperationPhase.FLASH, "Writing image to /dev/block/sda", command = "dd if=/sdcard/ubuntu.img of=/dev/block/sda bs=4M status=progress", result = OperationResult.RUNNING, durationMs = 0),
        )
    }

    var expandedId by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { Text("Operation Log", color = TextPrimary, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, "Back", tint = TextSecondary) }
                },
                actions = {
                    IconButton(onClick = { /* export */ }) {
                        Icon(Icons.Filled.Share, "Export", tint = TextSecondary)
                    }
                    IconButton(onClick = { /* clear */ }) {
                        Icon(Icons.Filled.DeleteSweep, "Clear", tint = TextSecondary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground),
            )
        }
    ) { paddingValues ->
        if (sampleLogs.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Filled.Terminal, null, tint = TextTertiary, modifier = Modifier.size(48.dp))
                    Text("No operations recorded yet", style = MaterialTheme.typography.bodyLarge.copy(color = TextTertiary))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
            ) {
                itemsIndexed(sampleLogs, key = { _, item -> item.id }) { _, entry ->
                    LogEntryCard(
                        entry = entry,
                        isExpanded = expandedId == entry.id,
                        onToggle = { expandedId = if (expandedId == entry.id) null else entry.id },
                    )
                }
            }
        }
    }
}

@Composable
private fun LogEntryCard(
    entry: LogEntry,
    isExpanded: Boolean,
    onToggle: () -> Unit,
) {
    val resultColor = when (entry.result) {
        OperationResult.SUCCESS -> GreenSuccess
        OperationResult.FAILURE -> RedDanger
        OperationResult.RUNNING -> CyanAccent
        OperationResult.CANCELLED -> AmberWarn
        OperationResult.WARNING -> AmberWarn
        else -> TextTertiary
    }
    val resultIcon = when (entry.result) {
        OperationResult.SUCCESS -> Icons.Filled.CheckCircle
        OperationResult.FAILURE -> Icons.Filled.Error
        OperationResult.RUNNING -> Icons.Filled.HourglassTop
        OperationResult.CANCELLED -> Icons.Filled.Cancel
        else -> Icons.Filled.Circle
    }
    val phaseColor = when (entry.phase) {
        OperationPhase.FLASH -> CardFlashAccent
        OperationPhase.VENTOY -> CardVentoyAccent
        OperationPhase.WINDOWS -> CardWindowsAccent
        OperationPhase.PARTITION -> CardPartitionAccent
        OperationPhase.PRIVILEGE -> DaemonPurpleLight
        else -> TextTertiary
    }

    Surface(
        onClick = onToggle,
        shape = RoundedCornerShape(12.dp),
        color = DarkSurfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, if (isExpanded) resultColor.copy(alpha = 0.3f) else DarkOutline, RoundedCornerShape(12.dp)),
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Header row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Icon(resultIcon, null, tint = resultColor, modifier = Modifier.size(18.dp))
                Column(Modifier.weight(1f)) {
                    Text(entry.operation, style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary, fontWeight = FontWeight.Medium))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        PhaseChip(entry.phase.name, phaseColor)
                        Text(
                            SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(entry.timestamp)),
                            style = MaterialTheme.typography.labelSmall.copy(color = TextTertiary),
                        )
                        if (entry.durationMs > 0) {
                            Text("${entry.durationMs}ms", style = MaterialTheme.typography.labelSmall.copy(color = TextTertiary))
                        }
                    }
                }
                Icon(
                    if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    null, tint = TextTertiary, modifier = Modifier.size(16.dp),
                )
            }

            // Expanded detail — command + output
            if (isExpanded) {
                HorizontalDivider(color = DarkOutline, thickness = 0.5.dp)
                if (entry.command != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(TerminalBackground, RoundedCornerShape(6.dp))
                            .padding(8.dp),
                    ) {
                        Text(
                            "$ ${entry.command}",
                            style = TerminalTextStyle.copy(color = TerminalCommand),
                        )
                    }
                }
                entry.stdout?.let { stdout ->
                    Text(stdout, style = TerminalTextStyle.copy(color = TerminalStdout), modifier = Modifier.padding(horizontal = 2.dp))
                }
                entry.stderr?.let { stderr ->
                    Text(stderr, style = TerminalTextStyle.copy(color = TerminalStderr), modifier = Modifier.padding(horizontal = 2.dp))
                }
            }
        }
    }
}

@Composable
private fun PhaseChip(label: String, color: Color) {
    Surface(shape = RoundedCornerShape(4.dp), color = color.copy(alpha = 0.12f)) {
        Text(label, style = MaterialTheme.typography.labelSmall.copy(color = color, fontWeight = FontWeight.Medium), modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
    }
}
