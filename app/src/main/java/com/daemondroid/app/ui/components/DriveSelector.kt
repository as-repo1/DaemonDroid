package com.daemondroid.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.daemondroid.app.data.model.BlockDeviceInfo
import com.daemondroid.app.data.model.DeviceType
import com.daemondroid.app.ui.theme.*

// ─────────────────────────────────────────────────────────────────────────────
// DriveSelector — reusable drive picker used across all flash flows
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun DriveSelector(
    devices: List<BlockDeviceInfo>,
    selectedDevice: BlockDeviceInfo?,
    onSelectDevice: (BlockDeviceInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(Icons.Filled.Usb, null, tint = CyanAccent, modifier = Modifier.size(18.dp))
            Text(
                "Target Drive",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                ),
            )
        }

        if (devices.isEmpty()) {
            EmptyDriveState()
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 300.dp),
            ) {
                items(devices, key = { it.node }) { device ->
                    DriveRow(
                        device = device,
                        isSelected = device.node == selectedDevice?.node,
                        onSelect = {
                            if (device.isInternalSd) {
                                // Internal SD warning handled by caller via WarningDialog
                            }
                            onSelectDevice(device)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun DriveRow(
    device: BlockDeviceInfo,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) CyanAccent else DarkOutline,
        animationSpec = tween(180),
        label = "drive_border",
    )
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) CyanAccent.copy(alpha = 0.07f) else DarkSurfaceVariant,
        animationSpec = tween(180),
        label = "drive_bg",
    )

    Surface(
        onClick = onSelect,
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(12.dp)),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Device type icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = when (device.deviceType) {
                            DeviceType.INTERNAL_SD -> AmberWarn.copy(alpha = 0.15f)
                            DeviceType.USB_OTG -> CyanAccent.copy(alpha = 0.12f)
                            else -> DaemonPurpleLight.copy(alpha = 0.12f)
                        },
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = when (device.deviceType) {
                        DeviceType.USB_OTG -> Icons.Filled.Usb
                        DeviceType.INTERNAL_SD, DeviceType.EXTERNAL_SD -> Icons.Filled.SdCard
                        else -> Icons.Filled.Storage
                    },
                    contentDescription = null,
                    tint = when (device.deviceType) {
                        DeviceType.INTERNAL_SD -> AmberWarn
                        DeviceType.USB_OTG -> CyanAccent
                        else -> DaemonPurpleLight
                    },
                    modifier = Modifier.size(20.dp),
                )
            }

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        device.label.ifEmpty { device.usbProductName ?: "Unknown Drive" },
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.Medium,
                        ),
                    )
                    if (device.isInternalSd) {
                        WarningChip("INTERNAL")
                    }
                    if (device.isMounted) {
                        WarningChip("MOUNTED", color = AmberWarn)
                    }
                }
                Text(
                    "${device.node} · ${device.sizeHuman} · ${device.partitions.size} partition(s)",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                )
            }

            if (isSelected) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = "Selected",
                    tint = CyanAccent,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

@Composable
private fun WarningChip(label: String, color: Color = RedDanger) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.15f),
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = color,
                fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
        )
    }
}

@Composable
private fun EmptyDriveState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceVariant)
            .border(1.dp, DarkOutline, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = DaemonPurpleLight,
                strokeWidth = 2.dp,
            )
            Text(
                "Scanning for drives...",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
            )
        }
    }
}
