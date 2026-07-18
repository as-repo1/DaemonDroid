package com.daemondroid.app.ui.flash

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.daemondroid.app.data.model.BlockDeviceInfo
import com.daemondroid.app.ui.components.DriveSelector
import com.daemondroid.app.ui.components.FlashProgressState
import com.daemondroid.app.ui.components.ProgressOverlay
import com.daemondroid.app.ui.components.WarningDialog
import com.daemondroid.app.ui.theme.*

// ─────────────────────────────────────────────────────────────────────────────
// Standard Flash Screen — Linux/Raspberry Pi raw image write
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardFlashScreen(
    onBack: () -> Unit,
    onViewLog: () -> Unit,
) {
    var selectedImageName by remember { mutableStateOf<String?>(null) }
    var selectedDevice by remember { mutableStateOf<BlockDeviceInfo?>(null) }
    var showInternalSdWarning by remember { mutableStateOf(false) }
    var pendingInternalSdDevice by remember { mutableStateOf<BlockDeviceInfo?>(null) }
    var isFlashing by remember { mutableStateOf(false) }
    val progressState by remember { mutableStateOf(FlashProgressState(phase = "Ready")) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { Text("Standard Flash", color = TextPrimary, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "Back", tint = TextSecondary)
                    }
                },
                actions = {
                    IconButton(onClick = onViewLog) {
                        Icon(Icons.Filled.Terminal, "Log", tint = TextSecondary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground),
            )
        },
        bottomBar = {
            FlashActionBar(
                canFlash = selectedImageName != null && selectedDevice != null && !isFlashing,
                isFlashing = isFlashing,
                onFlash = { isFlashing = true },
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
            // ── Step 1: Pick Image ─────────────────────────────────────────────
            StepCard(stepNumber = 1, title = "Select Source Image") {
                ImagePickerButton(
                    selectedName = selectedImageName,
                    onPickImage = { selectedImageName = "ubuntu-24.04-arm64.img" /* placeholder */ },
                )
            }

            // ── Step 2: Select Drive ───────────────────────────────────────────
            StepCard(stepNumber = 2, title = "Select Target Drive") {
                DriveSelector(
                    devices = emptyList(), // populated by ViewModel in Phase 2
                    selectedDevice = selectedDevice,
                    onSelectDevice = { device ->
                        if (device.isInternalSd) {
                            pendingInternalSdDevice = device
                            showInternalSdWarning = true
                        } else {
                            selectedDevice = device
                        }
                    },
                )
            }

            // ── Step 3: Options ────────────────────────────────────────────────
            StepCard(stepNumber = 3, title = "Flash Options") {
                FlashOptionsPanel()
            }

            // ── Progress (when flashing) ───────────────────────────────────────
            if (isFlashing) {
                ProgressOverlay(
                    state = progressState,
                    onAbort = { isFlashing = false },
                )
            }
        }
    }

    // Internal SD warning dialog
    if (showInternalSdWarning) {
        WarningDialog(
            title = "⚠️ Internal SD Card Selected",
            message = "You've selected what appears to be an internal SD card slot. " +
                    "Writing to this device will ERASE ALL DATA on that card. " +
                    "This operation requires root or Shizuku access and CANNOT be undone.\n\n" +
                    "Are you absolutely sure you want to continue?",
            confirmLabel = "Yes, I understand — Continue",
            onConfirm = {
                selectedDevice = pendingInternalSdDevice
                pendingInternalSdDevice = null
                showInternalSdWarning = false
            },
            onDismiss = {
                pendingInternalSdDevice = null
                showInternalSdWarning = false
            },
            isDangerous = true,
        )
    }
}

@Composable
fun StepCard(
    stepNumber: Int,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DarkSurfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DarkOutline, RoundedCornerShape(16.dp)),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DaemonPurple.copy(alpha = 0.2f),
                ) {
                    Text(
                        "$stepNumber",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = DaemonPurpleLight,
                            fontWeight = FontWeight.Bold,
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                }
                Text(
                    title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
            }
            HorizontalDivider(color = DarkOutline, thickness = 0.5.dp)
            content()
        }
    }
}

@Composable
private fun ImagePickerButton(
    selectedName: String?,
    onPickImage: () -> Unit,
) {
    Surface(
        onClick = onPickImage,
        shape = RoundedCornerShape(10.dp),
        color = DarkSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DarkOutline, RoundedCornerShape(10.dp)),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                if (selectedName != null) Icons.Filled.CheckCircle else Icons.Filled.FileOpen,
                contentDescription = null,
                tint = if (selectedName != null) GreenSuccess else TextTertiary,
                modifier = Modifier.size(20.dp),
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    selectedName ?: "Tap to browse for .img / .iso file",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (selectedName != null) TextPrimary else TextTertiary,
                    ),
                )
                if (selectedName != null) {
                    Text(
                        "Tap to change",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextTertiary),
                    )
                }
            }
            Icon(Icons.Filled.ChevronRight, null, tint = TextTertiary, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun FlashOptionsPanel() {
    var verifyAfterFlash by remember { mutableStateOf(true) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                "Verify after flash",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary),
            )
            Text(
                "SHA256 checksum verification",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
            )
        }
        Switch(
            checked = verifyAfterFlash,
            onCheckedChange = { verifyAfterFlash = it },
            colors = SwitchDefaults.colors(
                checkedTrackColor = DaemonPurple,
                checkedThumbColor = DaemonPurpleLight,
            ),
        )
    }
}

@Composable
private fun FlashActionBar(
    canFlash: Boolean,
    isFlashing: Boolean,
    onFlash: () -> Unit,
) {
    Surface(color = DarkSurface) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Button(
                onClick = onFlash,
                enabled = canFlash,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CardFlashAccent.copy(alpha = 0.85f),
                    disabledContainerColor = DarkSurfaceVariant,
                    contentColor = DarkBackground,
                ),
            ) {
                Icon(Icons.Filled.FlashOn, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    if (isFlashing) "Flashing..." else "Flash Now",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                )
            }
        }
    }
}
