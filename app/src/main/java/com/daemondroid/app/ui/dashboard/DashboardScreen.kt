package com.daemondroid.app.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daemondroid.app.ui.theme.*

// ─────────────────────────────────────────────────────────────────────────────
// Dashboard Screen — the app's main hub
// ─────────────────────────────────────────────────────────────────────────────

data class FeatureCardData(
    val title: String,
    val subtitle: String,
    val description: String,
    val icon: ImageVector,
    val gradientStart: Color,
    val gradientEnd: Color,
    val accentColor: Color,
    val badge: String? = null,
    val requiresRoot: Boolean = false,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToFlash: () -> Unit,
    onNavigateToVentoy: () -> Unit,
    onNavigateToWindows: () -> Unit,
    onNavigateToPartition: () -> Unit,
    onNavigateToLog: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToWizard: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            DashboardTopBar(
                onLogClick = onNavigateToLog,
                onSettingsClick = onNavigateToSettings,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // ── Header ───────────────────────────────────────────────────────
            DashboardHeader()

            // ── Privilege Status Banner ───────────────────────────────────────
            PrivilegeBanner(onConfigureClick = onNavigateToWizard)

            // ── Feature Cards ─────────────────────────────────────────────────
            FeatureCardGrid(
                onFlash = onNavigateToFlash,
                onVentoy = onNavigateToVentoy,
                onWindows = onNavigateToWindows,
                onPartition = onNavigateToPartition,
            )

            // ── Recent Activity ───────────────────────────────────────────────
            RecentActivitySection(onViewAll = onNavigateToLog)

            Spacer(Modifier.height(80.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardTopBar(
    onLogClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // Daemon glyph
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(DaemonPurpleLight, DaemonPurple)
                            ),
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.FlashOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp),
                    )
                }
                Text(
                    text = "DaemonDroid",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                    ),
                )
            }
        },
        actions = {
            IconButton(onClick = onLogClick) {
                Icon(
                    Icons.Outlined.Terminal,
                    contentDescription = "Operation Log",
                    tint = TextSecondary,
                )
            }
            IconButton(onClick = onSettingsClick) {
                Icon(
                    Icons.Outlined.Settings,
                    contentDescription = "Settings",
                    tint = TextSecondary,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DarkBackground,
            scrolledContainerColor = DarkSurface,
        ),
    )
}

@Composable
private fun DashboardHeader() {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { -20 }),
    ) {
        Column {
            Text(
                text = "Universal Bootable",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Normal,
                ),
            )
            Text(
                text = "USB/SD Maker",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                ),
            )
        }
    }
}

@Composable
private fun PrivilegeBanner(onConfigureClick: () -> Unit) {
    // Placeholder — real privilege detection happens in Phase 2
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF1A1A26),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DarkOutline, RoundedCornerShape(12.dp)),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                Icons.Filled.Shield,
                contentDescription = null,
                tint = AmberWarn,
                modifier = Modifier.size(20.dp),
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "No Drive Detected",
                    style = MaterialTheme.typography.labelLarge.copy(color = TextPrimary),
                )
                Text(
                    "Plug in a USB drive or SD card to begin",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                )
            }
            TextButton(onClick = onConfigureClick) {
                Text("Setup", color = DaemonPurpleLight, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun FeatureCardGrid(
    onFlash: () -> Unit,
    onVentoy: () -> Unit,
    onWindows: () -> Unit,
    onPartition: () -> Unit,
) {
    val cards = listOf(
        FeatureCardData(
            title = "Standard Flash",
            subtitle = "Linux / Raspberry Pi",
            description = "Write raw disk images directly to USB or SD. Supports dd and FileChannel modes.",
            icon = Icons.Filled.Memory,
            gradientStart = CardFlashStart,
            gradientEnd = CardFlashEnd,
            accentColor = CardFlashAccent,
            badge = "FAST",
        ),
        FeatureCardData(
            title = "Ventoy Maker",
            subtitle = "Multi-Boot USB",
            description = "Install Ventoy with custom GRUB themes. Drop ISOs onto the drive — no reflashing needed.",
            icon = Icons.Filled.LayersClear,
            gradientStart = CardVentoyStart,
            gradientEnd = CardVentoyEnd,
            accentColor = CardVentoyAccent,
            badge = "GRUB THEMES",
            requiresRoot = true,
        ),
        FeatureCardData(
            title = "Windows Installer",
            subtitle = "ISO Extraction + WIM Split",
            description = "Extract Windows 11/10 ISOs to FAT32. Auto-splits large install.wim files via wimlib.",
            icon = Icons.Filled.Window,
            gradientStart = CardWindowsStart,
            gradientEnd = CardWindowsEnd,
            accentColor = CardWindowsAccent,
        ),
        FeatureCardData(
            title = "Partition Manager",
            subtitle = "FAT32 · exFAT · ext4",
            description = "View, wipe, and create partitions using parted. Full GPT and MBR support.",
            icon = Icons.Filled.Storage,
            gradientStart = CardPartitionStart,
            gradientEnd = CardPartitionEnd,
            accentColor = CardPartitionAccent,
            requiresRoot = true,
        ),
    )

    val actions = listOf(onFlash, onVentoy, onWindows, onPartition)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Top row: Standard Flash (full-width hero card)
        FeatureCard(
            data = cards[0],
            onClick = actions[0],
            modifier = Modifier.fillMaxWidth(),
            isHero = true,
        )
        // Bottom row: 3 cards in a grid
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FeatureCard(
                data = cards[1],
                onClick = actions[1],
                modifier = Modifier.weight(1f),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                FeatureCard(
                    data = cards[2],
                    onClick = actions[2],
                    modifier = Modifier.fillMaxWidth(),
                )
                FeatureCard(
                    data = cards[3],
                    onClick = actions[3],
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun FeatureCard(
    data: FeatureCardData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isHero: Boolean = false,
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "card_scale",
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(data.gradientStart, data.gradientEnd)
                )
            )
            .border(1.dp, data.accentColor.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    pressed = true
                    onClick()
                },
            )
            .padding(if (isHero) 20.dp else 14.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(if (isHero) 10.dp else 6.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(if (isHero) 40.dp else 32.dp)
                        .background(data.accentColor.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = data.icon,
                        contentDescription = null,
                        tint = data.accentColor,
                        modifier = Modifier.size(if (isHero) 22.dp else 18.dp),
                    )
                }

                data.badge?.let { badge ->
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = data.accentColor.copy(alpha = 0.18f),
                    ) {
                        Text(
                            text = badge,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = data.accentColor,
                                fontWeight = FontWeight.Bold,
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        )
                    }
                }
            }

            Text(
                text = data.title,
                style = if (isHero) {
                    MaterialTheme.typography.titleLarge.copy(color = TextPrimary, fontWeight = FontWeight.Bold)
                } else {
                    MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold)
                },
            )

            Text(
                text = data.subtitle,
                style = MaterialTheme.typography.labelMedium.copy(color = data.accentColor),
            )

            if (isHero) {
                Text(
                    text = data.description,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                )
            }

            if (data.requiresRoot) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        Icons.Filled.Lock,
                        contentDescription = null,
                        tint = AmberWarn.copy(alpha = 0.7f),
                        modifier = Modifier.size(10.dp),
                    )
                    Text(
                        "Needs root/Shizuku",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AmberWarn.copy(alpha = 0.7f),
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentActivitySection(onViewAll: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "Recent Activity",
                style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary),
            )
            TextButton(onClick = onViewAll) {
                Text("View Log", color = DaemonPurpleLight, fontSize = 13.sp)
            }
        }

        // Placeholder for recent log entries
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = DarkSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, DarkOutline, RoundedCornerShape(12.dp)),
        ) {
            Box(
                modifier = Modifier.padding(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        Icons.Outlined.Terminal,
                        contentDescription = null,
                        tint = TextTertiary,
                        modifier = Modifier.size(28.dp),
                    )
                    Text(
                        "No operations yet",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextTertiary),
                    )
                }
            }
        }
    }
}
