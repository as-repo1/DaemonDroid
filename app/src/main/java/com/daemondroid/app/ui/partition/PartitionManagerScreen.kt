package com.daemondroid.app.ui.partition

import com.daemondroid.app.R
import androidx.compose.ui.res.stringResource
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daemondroid.app.data.model.PartitionInfo
import com.daemondroid.app.ui.components.DriveSelector
import com.daemondroid.app.ui.components.WarningDialog
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.daemondroid.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartitionManagerScreen(
    onBack: () -> Unit,
    onViewLog: () -> Unit,
) {
    var showWipeDialog by remember { mutableStateOf(false) }
    var showCreateDialog by remember { mutableStateOf(false) }

    // Placeholder partition data for UI preview
    val samplePartitions = listOf(
        PartitionInfo(1, "/dev/block/sda1", 2048, 1050623, 512L * 1024 * 1024, "512 MB", "FAT32", "EFI"),
        PartitionInfo(2, "/dev/block/sda2", 1050624, 62521343, 30L * 1024 * 1024 * 1024, "30 GB", "exFAT", "Ventoy"),
    )

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.feature_partition), color = TextPrimary, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = TextSecondary) }
                },
                actions = {
                    IconButton(onClick = onViewLog) { Icon(Icons.Filled.Terminal, "Log", tint = TextSecondary) }
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Drive selector
            DriveSelector(devices = emptyList(), selectedDevice = null, onSelectDevice = {})

            // Partition visual map
            PartitionVisualBar(partitions = samplePartitions)

            // Partition list
            samplePartitions.forEach { partition ->
                PartitionRow(partition = partition)
            }

            // Action buttons
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilledTonalButton(
                    onClick = { showWipeDialog = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = RedDanger.copy(alpha = 0.12f),
                        contentColor = RedDanger,
                    ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Icon(Icons.Filled.DeleteForever, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(stringResource(R.string.partition_wipe_all))
                }
                Button(
                    onClick = { showCreateDialog = true },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CardPartitionAccent.copy(alpha = 0.8f), contentColor = DarkBackground),
                ) {
                    Icon(Icons.Filled.Add, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("New Partition")
                }
            }
        }
    }

    if (showWipeDialog) {
        WarningDialog(
            title = "🗑️ Wipe Entire Drive?",
            message = "This will destroy all partitions and data on the selected drive. " +
                    "A new empty GPT partition table will be created. This CANNOT be undone.",
            confirmLabel = "Wipe Drive",
            onConfirm = { showWipeDialog = false },
            onDismiss = { showWipeDialog = false },
            isDangerous = true,
        )
    }

    if (showCreateDialog) {
        CreatePartitionDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { _, _, _ -> showCreateDialog = false },
        )
    }
}

@Composable
private fun PartitionVisualBar(partitions: List<PartitionInfo>) {
    val total = partitions.sumOf { it.sizeBytes }.coerceAtLeast(1)
    val colors = listOf(DaemonPurpleLight, CyanAccent, CardWindowsAccent, CardPartitionAccent, GreenSuccess)

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("Partition Map", style = MaterialTheme.typography.labelLarge.copy(color = TextSecondary))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
                .clip(RoundedCornerShape(8.dp)),
        ) {
            partitions.forEachIndexed { index, partition ->
                val fraction = partition.sizeBytes.toFloat() / total
                Box(
                    modifier = Modifier
                        .weight(fraction)
                        .fillMaxHeight()
                        .background(colors[index % colors.size]),
                    contentAlignment = Alignment.Center,
                ) {
                    if (fraction > 0.1f) {
                        Text(
                            "P${partition.number}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 10.sp,
                            ),
                        )
                    }
                }
            }
        }
        // Legend
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            partitions.forEachIndexed { index, partition ->
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(modifier = Modifier.size(8.dp).background(colors[index % colors.size], RoundedCornerShape(2.dp)))
                    Text(
                        partition.label ?: "P${partition.number}",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary),
                    )
                }
            }
        }
    }
}

@Composable
private fun PartitionRow(partition: PartitionInfo) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DarkSurfaceVariant,
        modifier = Modifier.fillMaxWidth().border(1.dp, DarkOutline, RoundedCornerShape(12.dp)),
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Filled.Layers, null, tint = CardPartitionAccent, modifier = Modifier.size(20.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    "${partition.label ?: "Partition ${partition.number}"} · ${partition.type}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary, fontWeight = FontWeight.Medium),
                )
                Text(
                    "${partition.node} · ${partition.sizeHuman}",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                )
            }
            if (partition.isMounted) {
                Surface(shape = RoundedCornerShape(4.dp), color = AmberWarn.copy(alpha = 0.15f)) {
                    Text("MOUNTED", style = MaterialTheme.typography.labelSmall.copy(color = AmberWarn, fontWeight = FontWeight.Bold), modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}

@Composable
private fun CreatePartitionDialog(
    onDismiss: () -> Unit,
    onCreate: (type: String, label: String, sizeMb: Int) -> Unit,
) {
    var selectedType by remember { mutableStateOf("FAT32") }
    var label by remember { mutableStateOf("") }
    val types = listOf("FAT32", "exFAT", "ext4")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurfaceContainerHigh,
        shape = RoundedCornerShape(20.dp),
        title = { Text("New Partition", style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Filesystem type:", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    types.forEach { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text(type) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DaemonPurple.copy(alpha = 0.2f),
                                selectedLabelColor = DaemonPurpleLight,
                            ),
                        )
                    }
                }
                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    label = { Text("Label (optional)", color = TextSecondary) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DaemonPurpleLight,
                        unfocusedBorderColor = DarkOutline,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                    ),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onCreate(selectedType, label, 0) },
                colors = ButtonDefaults.buttonColors(containerColor = DaemonPurple),
                shape = RoundedCornerShape(10.dp),
            ) { Text("Create") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
        },
    )
}
