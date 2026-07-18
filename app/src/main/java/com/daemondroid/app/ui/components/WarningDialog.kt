package com.daemondroid.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.daemondroid.app.ui.theme.*

@Composable
fun WarningDialog(
    title: String,
    message: String,
    confirmLabel: String = "Confirm",
    dismissLabel: String = "Cancel",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isDangerous: Boolean = false,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurfaceContainerHigh,
        shape = RoundedCornerShape(20.dp),
        icon = {
            Icon(
                Icons.Filled.Warning,
                contentDescription = null,
                tint = if (isDangerous) RedDanger else AmberWarn,
                modifier = Modifier.size(32.dp),
            )
        },
        title = {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                ),
            )
        },
        text = {
            Text(
                message,
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDangerous) RedDanger else DaemonPurple,
                ),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(
                    confirmLabel,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissLabel, color = TextSecondary)
            }
        },
    )
}
