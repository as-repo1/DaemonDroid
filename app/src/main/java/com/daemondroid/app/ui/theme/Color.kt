package com.daemondroid.app.ui.theme

import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────────────────────────────────────────
// DaemonDroid Color System
// Visual personality: Hybrid Dark — Material 3 surface in light,
// moody terminal aesthetic in dark mode.
// ─────────────────────────────────────────────────────────────────────────────

// ── Brand / Primary ───────────────────────────────────────────────────────────
val DaemonPurple = Color(0xFF7C3AED)          // Primary brand — electric violet
val DaemonPurpleLight = Color(0xFF9F67FF)     // Lighter variant
val DaemonPurpleDark = Color(0xFF5B21B6)      // Darker variant
val DaemonPurpleContainer = Color(0xFF1E1033) // Container bg (dark)
val OnDaemonPurpleContainer = Color(0xFFD4BBFF)

// ── Accent / Secondary ────────────────────────────────────────────────────────
val CyanAccent = Color(0xFF00D4FF)            // Neon cyan — terminal feel
val CyanAccentDim = Color(0xFF0097B3)
val AmberWarn = Color(0xFFFFA500)             // Warning / active write
val RedDanger = Color(0xFFEF4444)             // Destructive actions
val GreenSuccess = Color(0xFF22C55E)          // Verified / complete

// ── Feature Card Colors ───────────────────────────────────────────────────────
// Standard Flash (Linux/Pi)
val CardFlashStart = Color(0xFF1A1A2E)
val CardFlashEnd = Color(0xFF16213E)
val CardFlashAccent = Color(0xFF00D4FF)

// Windows Installer
val CardWindowsStart = Color(0xFF1A1A2E)
val CardWindowsEnd = Color(0xFF1A2A1E)
val CardWindowsAccent = Color(0xFF3ECF8E)

// Ventoy Maker
val CardVentoyStart = Color(0xFF1A1A2E)
val CardVentoyEnd = Color(0xFF2A1A2E)
val CardVentoyAccent = Color(0xFF9F67FF)

// Partition Manager
val CardPartitionStart = Color(0xFF1A1A2E)
val CardPartitionEnd = Color(0xFF2A1A1A)
val CardPartitionAccent = Color(0xFFFFA500)

// ── Surface / Background (Dark Mode) ─────────────────────────────────────────
val DarkBackground = Color(0xFF0A0A0F)        // Near-black background
val DarkSurface = Color(0xFF111118)           // Elevated surfaces
val DarkSurfaceVariant = Color(0xFF1A1A26)    // Cards, dialogs
val DarkSurfaceContainer = Color(0xFF16161F)
val DarkSurfaceContainerHigh = Color(0xFF1E1E2E) // Sheet surfaces
val DarkOutline = Color(0xFF2A2A40)
val DarkOutlineVariant = Color(0xFF1E1E30)

// ── Terminal / Log Colors ─────────────────────────────────────────────────────
val TerminalBackground = Color(0xFF060608)
val TerminalText = Color(0xFF00FF41)          // Classic green-on-black
val TerminalStdout = Color(0xFFCCCCCC)
val TerminalStderr = Color(0xFFFF6B6B)
val TerminalCommand = Color(0xFF74C7EC)       // Highlighted commands
val TerminalSuccess = Color(0xFF89DCEB)
val TerminalTimestamp = Color(0xFF6C7086)

// ── Light Mode Surfaces ───────────────────────────────────────────────────────
val LightBackground = Color(0xFFF8F8FC)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFEEEEF8)
val LightOutline = Color(0xFFCACADA)

// ── Text ──────────────────────────────────────────────────────────────────────
val TextPrimary = Color(0xFFF2F2FF)
val TextSecondary = Color(0xFFAAABBE)
val TextTertiary = Color(0xFF6C6D7E)
val TextOnDark = Color(0xFFF2F2FF)
val TextOnLight = Color(0xFF1A1A2E)
