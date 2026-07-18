package com.daemondroid.app.data.model

/**
 * Represents the privilege level detected on the device.
 * Used by PrivilegeManager and surfaced in the first-launch wizard.
 */
enum class PrivilegeLevel(
    val displayName: String,
    val description: String,
    val capabilities: Set<Capability>,
) {
    ROOT(
        displayName = "Root Access",
        description = "Full block-level access via su. All features unlocked.",
        capabilities = Capability.entries.toSet()
    ),
    SHIZUKU(
        displayName = "Shizuku",
        description = "Elevated shell via Shizuku. Block access to OTG and SD drives.",
        capabilities = setOf(
            Capability.OTG_FLASH,
            Capability.SD_FLASH,
            Capability.PARTITION_MANAGE,
            Capability.VENTOY_INSTALL,
            Capability.WINDOWS_FLASH,
        )
    ),
    USB_HOST(
        displayName = "USB Host (No Root)",
        description = "Direct OTG USB access via Android USB Host API. Limited to connected drives.",
        capabilities = setOf(
            Capability.OTG_FLASH,
        )
    ),
    NONE(
        displayName = "No Elevated Access",
        description = "Cannot access block devices. Please connect via USB Host or install Shizuku.",
        capabilities = emptySet()
    );
}

enum class Capability {
    OTG_FLASH,          // Flash to OTG USB drives
    SD_FLASH,           // Flash to internal/external SD cards
    PARTITION_MANAGE,   // Create, delete, format partitions
    VENTOY_INSTALL,     // Install Ventoy (needs parted + shell)
    WINDOWS_FLASH,      // Windows ISO extraction (needs wimlib + shell)
    BINARY_EXECUTE,     // Execute downloaded ARM64 binaries
}
