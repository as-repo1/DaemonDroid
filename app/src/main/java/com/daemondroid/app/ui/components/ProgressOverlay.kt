package com.daemondroid.app.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daemondroid.app.ui.theme.*

// ─────────────────────────────────────────────────────────────────────────────
// Progress Overlay — shown during active flash/install operations
// Toggle between simple ring view and verbose terminal panel
// ─────────────────────────────────────────────────────────────────────────────

data class FlashProgressState(
    val phase: String = "Preparing...",
    val percentComplete: Float = 0f,        // 0..1
    val speedMbps: Float = 0f,
    val etaSeconds: Long = 0,
    val bytesWritten: Long = 0,
    val totalBytes: Long = 0,
    val isRunning: Boolean = false,
    val isComplete: Boolean = false,
    val hasError: Boolean = false,
    val errorMessage: String? = null,
    val logLines: List<LogLine> = emptyList(),
)

data class LogLine(
    val text: String,
    val type: LogLineType = LogLineType.STDOUT,
    val timestamp: Long = System.currentTimeMillis(),
)

enum class LogLineType { COMMAND, STDOUT, STDERR, INFO, SUCCESS, ERROR }

@Composable
fun ProgressOverlay(
    state: FlashProgressState,
    onAbort: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var terminalExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(DarkSurfaceContainerHigh)
            .border(1.dp, DarkOutline, RoundedCornerShape(20.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // ── Header Row ────────────────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    state.phase,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
                if (state.isRunning) {
                    Text(
                        "${state.speedMbps.format(1)} MB/s · ETA ${state.etaSeconds.formatEta()}",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                    )
                }
            }

            if (state.isRunning) {
                FilledTonalButton(
                    onClick = onAbort,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = RedDanger.copy(alpha = 0.15f),
                        contentColor = RedDanger,
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                ) {
                    Icon(Icons.Filled.Stop, null, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Abort", fontSize = 12.sp)
                }
            }
        }

        // ── Progress Ring + Stats ─────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Circular progress indicator
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    progress = { state.percentComplete },
                    modifier = Modifier.fillMaxSize(),
                    color = when {
                        state.hasError -> RedDanger
                        state.isComplete -> GreenSuccess
                        else -> DaemonPurpleLight
                    },
                    trackColor = DarkSurface,
                    strokeWidth = 6.dp,
                    strokeCap = StrokeCap.Round,
                )
                Text(
                    "${(state.percentComplete * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }

            // Stats
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                StatRow("Written", state.bytesWritten.formatBytes())
                StatRow("Total", state.totalBytes.formatBytes())
                if (state.isComplete) {
                    StatRow("Status", "✓ Complete", valueColor = GreenSuccess)
                } else if (state.hasError) {
                    StatRow("Error", state.errorMessage ?: "Unknown", valueColor = RedDanger)
                }
            }
        }

        // ── Linear progress bar ───────────────────────────────────────────────
        LinearProgressIndicator(
            progress = { state.percentComplete },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp)),
            color = if (state.hasError) RedDanger else DaemonPurpleLight,
            trackColor = DarkSurface,
        )

        // ── Terminal Panel Toggle ─────────────────────────────────────────────
        TextButton(
            onClick = { terminalExpanded = !terminalExpanded },
            contentPadding = PaddingValues(0.dp),
        ) {
            Icon(
                if (terminalExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                null,
                tint = TextSecondary,
                modifier = Modifier.size(16.dp),
            )
            Spacer(Modifier.width(4.dp))
            Text(
                if (terminalExpanded) "Hide Log" else "Show Log",
                style = MaterialTheme.typography.labelMedium.copy(color = TextSecondary),
            )
        }

        // ── Collapsible Terminal Panel ─────────────────────────────────────────
        AnimatedVisibility(
            visible = terminalExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            TerminalPanel(lines = state.logLines)
        }
    }
}

@Composable
private fun StatRow(
    label: String,
    value: String,
    valueColor: Color = TextPrimary,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "$label:",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
        )
        Text(
            value,
            style = MaterialTheme.typography.bodySmall.copy(
                color = valueColor,
                fontWeight = FontWeight.Medium,
            ),
        )
    }
}

@Composable
fun TerminalPanel(
    lines: List<LogLine>,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    LaunchedEffect(lines.size) {
        if (lines.isNotEmpty()) listState.animateScrollToItem(lines.size - 1)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(TerminalBackground)
            .border(1.dp, Color(0xFF1A1A30), RoundedCornerShape(10.dp))
            .padding(10.dp),
    ) {
        LazyColumn(state = listState, verticalArrangement = Arrangement.spacedBy(1.dp)) {
            items(lines) { line ->
                Text(
                    text = line.text,
                    style = TerminalTextStyle.copy(
                        color = when (line.type) {
                            LogLineType.COMMAND -> TerminalCommand
                            LogLineType.STDERR -> TerminalStderr
                            LogLineType.SUCCESS -> TerminalSuccess
                            LogLineType.ERROR -> RedDanger
                            LogLineType.INFO -> TerminalTimestamp
                            LogLineType.STDOUT -> TerminalStdout
                        },
                    ),
                )
            }
        }
    }
}

// ── Extensions ────────────────────────────────────────────────────────────────
private fun Float.format(decimals: Int) = "%.${decimals}f".format(this)

private fun Long.formatEta(): String = when {
    this < 60 -> "${this}s"
    this < 3600 -> "${this / 60}m ${this % 60}s"
    else -> "${this / 3600}h ${(this % 3600) / 60}m"
}

private fun Long.formatBytes(): String = when {
    this < 1024 -> "${this} B"
    this < 1024 * 1024 -> "${"%.1f".format(this / 1024.0)} KB"
    this < 1024 * 1024 * 1024 -> "${"%.1f".format(this / (1024.0 * 1024))} MB"
    else -> "${"%.2f".format(this / (1024.0 * 1024 * 1024))} GB"
}
