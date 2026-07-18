package com.daemondroid.app.ui.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daemondroid.app.data.model.PrivilegeLevel
import com.daemondroid.app.ui.theme.*

// ─────────────────────────────────────────────────────────────────────────────
// Privilege Wizard — shown on first launch
// Explains each privilege level and lets the user pick their setup
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun PrivilegeWizardScreen(
    onComplete: () -> Unit,
) {
    var selectedLevel by remember { mutableStateOf<PrivilegeLevel?>(null) }
    var step by remember { mutableIntStateOf(0) } // 0=intro, 1=pick, 2=confirm

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
    ) {
        // Ambient glow background
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-80).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(DaemonPurple.copy(alpha = 0.25f), Color.Transparent),
                    ),
                    shape = CircleShape,
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(60.dp))

            // ── Icon ─────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        brush = Brush.linearGradient(listOf(DaemonPurple, DaemonPurpleLight)),
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.FlashOn,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp),
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "Welcome to DaemonDroid",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                ),
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Choose your access level to unlock features.\nYou can change this anytime in Settings.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                ),
            )

            Spacer(Modifier.height(32.dp))

            // ── Privilege Level Selector ──────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                PrivilegeLevelCard(
                    level = PrivilegeLevel.ROOT,
                    icon = Icons.Filled.AdminPanelSettings,
                    accentColor = RedDanger,
                    selected = selectedLevel == PrivilegeLevel.ROOT,
                    onSelect = { selectedLevel = PrivilegeLevel.ROOT },
                )
                PrivilegeLevelCard(
                    level = PrivilegeLevel.SHIZUKU,
                    icon = Icons.Filled.Terminal,
                    accentColor = DaemonPurpleLight,
                    selected = selectedLevel == PrivilegeLevel.SHIZUKU,
                    onSelect = { selectedLevel = PrivilegeLevel.SHIZUKU },
                )
                PrivilegeLevelCard(
                    level = PrivilegeLevel.USB_HOST,
                    icon = Icons.Filled.Usb,
                    accentColor = CyanAccent,
                    selected = selectedLevel == PrivilegeLevel.USB_HOST,
                    onSelect = { selectedLevel = PrivilegeLevel.USB_HOST },
                )
                PrivilegeLevelCard(
                    level = PrivilegeLevel.NONE,
                    icon = Icons.Filled.DoNotDisturb,
                    accentColor = TextTertiary,
                    selected = selectedLevel == PrivilegeLevel.NONE,
                    onSelect = { selectedLevel = PrivilegeLevel.NONE },
                )
            }

            Spacer(Modifier.weight(1f))

            // ── Continue Button ────────────────────────────────────────────────
            Button(
                onClick = onComplete,
                enabled = selectedLevel != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DaemonPurple,
                    disabledContainerColor = DarkSurfaceVariant,
                ),
            ) {
                Text(
                    if (selectedLevel == null) "Select an option above" else "Continue →",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PrivilegeLevelCard(
    level: PrivilegeLevel,
    icon: ImageVector,
    accentColor: Color,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    val borderColor by animateColorAsState(
        targetValue = if (selected) accentColor else DarkOutline,
        animationSpec = tween(200),
        label = "border_color",
    )
    val bgColor by animateColorAsState(
        targetValue = if (selected) accentColor.copy(alpha = 0.08f) else DarkSurfaceVariant,
        animationSpec = tween(200),
        label = "bg_color",
    )

    Surface(
        onClick = onSelect,
        shape = RoundedCornerShape(14.dp),
        color = bgColor,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(14.dp)),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(accentColor.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(22.dp))
            }

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    level.displayName,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
                Text(
                    level.description,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                )
            }

            if (selected) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = "Selected",
                    tint = accentColor,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}
