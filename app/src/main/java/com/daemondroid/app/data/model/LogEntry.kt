package com.daemondroid.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Persistent log entry — records every shell command, result, and timing.
 * Stored in Room database. Exported as JSON or plain text.
 */
@Entity(tableName = "operation_logs")
@Serializable
data class LogEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),   // Epoch millis
    val phase: OperationPhase,
    val operation: String,                              // Human description, e.g. "Writing MBR"
    val command: String? = null,                        // Exact shell command executed
    val stdout: String? = null,
    val stderr: String? = null,
    val exitCode: Int? = null,
    val result: OperationResult = OperationResult.PENDING,
    val durationMs: Long = 0,
    val jobId: String? = null,                          // Links to FlashJob.id
    val notes: String? = null,
)

enum class OperationPhase {
    PRIVILEGE,      // Permission detection and acquisition
    BINARY,         // Binary download and extraction
    DEVICE,         // Device detection and selection
    PARTITION,      // Partition table operations
    FORMAT,         // Filesystem formatting
    VENTOY,         // Ventoy installation steps
    WINDOWS,        // Windows ISO extraction / WIM split
    FLASH,          // Raw block write
    VERIFY,         // Checksum verification
    SYSTEM,         // App lifecycle events
}

enum class OperationResult {
    PENDING,
    RUNNING,
    SUCCESS,
    FAILURE,
    CANCELLED,
    SKIPPED,
    WARNING,
}
