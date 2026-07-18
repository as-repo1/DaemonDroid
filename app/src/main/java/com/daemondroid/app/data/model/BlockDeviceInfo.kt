package com.daemondroid.app.data.model

import kotlinx.serialization.Serializable

/**
 * Represents a physical block device (USB drive, SD card, internal storage).
 */
@Serializable
data class BlockDeviceInfo(
    val node: String,           // e.g. "/dev/block/sda" or "/dev/block/mmcblk1"
    val label: String,          // Human-readable label if available
    val sizeBytes: Long,        // Total size in bytes
    val sizeHuman: String,      // e.g. "32 GB"
    val partitions: List<PartitionInfo> = emptyList(),
    val isRemovable: Boolean = true,
    val isMounted: Boolean = false,
    val isInternalSd: Boolean = false, // True if this is the device's internal SD slot
    val deviceType: DeviceType = DeviceType.USB_OTG,
    val usbVendorId: Int? = null,
    val usbProductId: Int? = null,
    val usbProductName: String? = null,
)

@Serializable
data class PartitionInfo(
    val number: Int,            // Partition number (1, 2, ...)
    val node: String,           // e.g. "/dev/block/sda1"
    val start: Long,            // Start sector
    val end: Long,              // End sector
    val sizeBytes: Long,
    val sizeHuman: String,
    val type: String,           // e.g. "FAT32", "ext4", "exFAT"
    val label: String? = null,
    val isMounted: Boolean = false,
    val mountPoint: String? = null,
)

enum class DeviceType {
    USB_OTG,
    INTERNAL_SD,
    EXTERNAL_SD,
    INTERNAL_STORAGE,
    UNKNOWN
}
