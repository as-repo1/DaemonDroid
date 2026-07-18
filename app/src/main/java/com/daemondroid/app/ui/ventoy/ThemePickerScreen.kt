package com.daemondroid.app.ui.ventoy

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.daemondroid.app.data.model.VentoyTheme
import com.daemondroid.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemePickerScreen(onBack: () -> Unit) {
    var selectedTheme by remember { mutableStateOf(VentoyTheme.VIMIX) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { Text("GRUB Themes", color = TextPrimary, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, "Back", tint = TextSecondary) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground),
            )
        },
        bottomBar = {
            Surface(color = DarkSurface) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = ButtonDefaults.outlinedButtonBorder,
                    ) { Text("Cancel", color = TextSecondary) }
                    Button(
                        onClick = onBack,
                        modifier = Modifier.weight(2f).height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CardVentoyAccent.copy(alpha = 0.85f), contentColor = DarkBackground),
                    ) {
                        Icon(Icons.Filled.Check, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Apply ${selectedTheme.displayName} Theme", fontWeight = FontWeight.Bold)
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
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                "Select a GRUB theme to inject into the Ventoy EFI partition during installation.",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
            )

            // ── Theme Cards ────────────────────────────────────────────────────
            VentoyTheme.entries.filter { it != VentoyTheme.NONE }.forEach { theme ->
                ThemeCard(
                    theme = theme,
                    isSelected = selectedTheme == theme,
                    onSelect = { selectedTheme = theme },
                )
            }

            // No theme option
            Surface(
                onClick = { selectedTheme = VentoyTheme.NONE },
                shape = RoundedCornerShape(16.dp),
                color = if (selectedTheme == VentoyTheme.NONE) DarkSurfaceVariant else DarkSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (selectedTheme == VentoyTheme.NONE) DarkOutline else DarkOutlineVariant,
                        RoundedCornerShape(16.dp),
                    ),
            ) {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Icon(Icons.Filled.HideImage, null, tint = TextTertiary, modifier = Modifier.size(24.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("No Theme", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary))
                        Text("Use Ventoy's default GRUB menu", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    }
                    if (selectedTheme == VentoyTheme.NONE) {
                        Icon(Icons.Filled.CheckCircle, null, tint = CardVentoyAccent)
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeCard(
    theme: VentoyTheme,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    val borderColor by animateColorAsState(
        if (isSelected) CardVentoyAccent else DarkOutline,
        tween(180), label = "theme_border",
    )

    val themeInfo = mapOf(
        VentoyTheme.VIMIX to Triple(
            "Clean, Material Design inspired",
            "Smooth gradients, card-based layout, excellent readability",
            listOf(DaemonPurple, DaemonPurpleLight, CyanAccent),
        ),
        VentoyTheme.SLEEK to Triple(
            "Minimalist and ultra-modern",
            "Monochrome palette, sharp typography, distraction-free",
            listOf(Color(0xFF1A1A1A), Color(0xFF333333), Color(0xFF555555)),
        ),
        VentoyTheme.TELA to Triple(
            "Round icons, intuitive navigation",
            "Colorful icon set, rounded corners, great for touchscreens",
            listOf(Color(0xFF0D47A1), Color(0xFF1565C0), Color(0xFF42A5F5)),
        ),
    )

    val (tagline, description, palette) = themeInfo[theme]!!

    Surface(
        onClick = onSelect,
        shape = RoundedCornerShape(16.dp),
        color = DarkSurfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp)),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column {
                    Text(theme.displayName, style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                    Text(tagline, style = MaterialTheme.typography.bodySmall.copy(color = CardVentoyAccent))
                }
                if (isSelected) {
                    Icon(Icons.Filled.CheckCircle, null, tint = CardVentoyAccent)
                }
            }

            // Color palette preview
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                palette.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(color, shape = RoundedCornerShape(6.dp)),
                    )
                }
            }

            // Mock GRUB preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        brush = Brush.linearGradient(palette.map { it.copy(alpha = 0.6f) }),
                    )
                    .border(1.dp, borderColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "[ ${theme.displayName} GRUB Preview ]",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.7f),
                    ),
                )
            }

            Text(description, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
        }
    }
}
