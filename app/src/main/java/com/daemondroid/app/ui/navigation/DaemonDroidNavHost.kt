package com.daemondroid.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.daemondroid.app.ui.dashboard.DashboardScreen
import com.daemondroid.app.ui.flash.StandardFlashScreen
import com.daemondroid.app.ui.log.OperationLogScreen
import com.daemondroid.app.ui.onboarding.PrivilegeWizardScreen
import com.daemondroid.app.ui.partition.PartitionManagerScreen
import com.daemondroid.app.ui.settings.SettingsScreen
import com.daemondroid.app.ui.ventoy.ThemePickerScreen
import com.daemondroid.app.ui.ventoy.VentoySetupScreen
import com.daemondroid.app.ui.windows.WindowsFlashScreen

// ─────────────────────────────────────────────────────────────────────────────
// Navigation Destinations
// ─────────────────────────────────────────────────────────────────────────────

object Destinations {
    const val PRIVILEGE_WIZARD = "privilege_wizard"
    const val DASHBOARD        = "dashboard"
    const val STANDARD_FLASH   = "standard_flash"
    const val VENTOY_SETUP     = "ventoy_setup"
    const val THEME_PICKER     = "theme_picker"
    const val WINDOWS_FLASH    = "windows_flash"
    const val PARTITION_MANAGER = "partition_manager"
    const val OPERATION_LOG    = "operation_log"
    const val SETTINGS         = "settings"
}

@Composable
fun DaemonDroidNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Destinations.DASHBOARD,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        // ── Onboarding ───────────────────────────────────────────────────────
        composable(Destinations.PRIVILEGE_WIZARD) {
            PrivilegeWizardScreen(
                onComplete = {
                    navController.navigate(Destinations.DASHBOARD) {
                        popUpTo(Destinations.PRIVILEGE_WIZARD) { inclusive = true }
                    }
                }
            )
        }

        // ── Dashboard ────────────────────────────────────────────────────────
        composable(Destinations.DASHBOARD) {
            DashboardScreen(
                onNavigateToFlash     = { navController.navigate(Destinations.STANDARD_FLASH) },
                onNavigateToVentoy    = { navController.navigate(Destinations.VENTOY_SETUP) },
                onNavigateToWindows   = { navController.navigate(Destinations.WINDOWS_FLASH) },
                onNavigateToPartition = { navController.navigate(Destinations.PARTITION_MANAGER) },
                onNavigateToLog       = { navController.navigate(Destinations.OPERATION_LOG) },
                onNavigateToSettings  = { navController.navigate(Destinations.SETTINGS) },
                onNavigateToWizard    = { navController.navigate(Destinations.PRIVILEGE_WIZARD) },
            )
        }

        // ── Standard Flash ───────────────────────────────────────────────────
        composable(Destinations.STANDARD_FLASH) {
            StandardFlashScreen(
                onBack   = { navController.popBackStack() },
                onViewLog = { navController.navigate(Destinations.OPERATION_LOG) },
            )
        }

        // ── Ventoy Setup ─────────────────────────────────────────────────────
        composable(Destinations.VENTOY_SETUP) {
            VentoySetupScreen(
                onBack                  = { navController.popBackStack() },
                onNavigateToThemePicker = { navController.navigate(Destinations.THEME_PICKER) },
                onViewLog               = { navController.navigate(Destinations.OPERATION_LOG) },
            )
        }

        // ── Theme Picker ─────────────────────────────────────────────────────
        composable(Destinations.THEME_PICKER) {
            ThemePickerScreen(
                onBack = { navController.popBackStack() },
            )
        }

        // ── Windows Flash ────────────────────────────────────────────────────
        composable(Destinations.WINDOWS_FLASH) {
            WindowsFlashScreen(
                onBack    = { navController.popBackStack() },
                onViewLog = { navController.navigate(Destinations.OPERATION_LOG) },
            )
        }

        // ── Partition Manager ────────────────────────────────────────────────
        composable(Destinations.PARTITION_MANAGER) {
            PartitionManagerScreen(
                onBack    = { navController.popBackStack() },
                onViewLog = { navController.navigate(Destinations.OPERATION_LOG) },
            )
        }

        // ── Operation Log ────────────────────────────────────────────────────
        composable(Destinations.OPERATION_LOG) {
            OperationLogScreen(
                onBack = { navController.popBackStack() },
            )
        }

        // ── Settings ─────────────────────────────────────────────────────────
        composable(Destinations.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
            )
        }
    }
}
