package com.daemondroid.app.data.model

import kotlinx.serialization.Serializable

/**
 * Represents a flash / install job — passed between screens as the user
 * builds up their operation configuration.
 */
@Serializable
data class FlashJob(
    val id: String = java.util.UUID.randomUUID().toString(),
    val mode: FlashMode,
    val sourceUri: String,              // Content URI or path to the source ISO/image
    val sourceName: String,             // Display name
    val sourceSizeBytes: Long,
    val targetDevice: BlockDeviceInfo,
    val options: FlashOptions = FlashOptions(),
    val checksumExpected: String? = null,    // SHA256 or MD5 sidecar value if available
    val checksumType: ChecksumType = ChecksumType.SHA256,
)

@Serializable
data class FlashOptions(
    // Standard flash
    val blockSize: Int = 4 * 1024 * 1024, // 4 MB

    // Ventoy
    val ventoyTheme: VentoyTheme = VentoyTheme.VIMIX,
    val ventoySecureBoot: Boolean = true,
    val ventoyGptScheme: Boolean = true,

    // Windows
    val splitWimSizeMb: Int = 3800,
    val windowsUefiOnly: Boolean = true,

    // Verification
    val verifyAfterFlash: Boolean = true,
)

enum class FlashMode {
    STANDARD_FLASH,   // Raw dd / FileChannel write
    VENTOY,           // Ventoy multi-boot installer
    WINDOWS_ISO,      // Windows ISO extraction + WIM split
    PARTITION_ONLY,   // Partition manager operations only
}

enum class VentoyTheme(val displayName: String, val assetDir: String) {
    VIMIX("Vimix", "themes/vimix"),
    SLEEK("Sleek", "themes/sleek"),
    TELA("Tela", "themes/tela"),
    NONE("No Theme (Default Ventoy)", ""),
}

enum class ChecksumType { MD5, SHA256, SHA512 }
