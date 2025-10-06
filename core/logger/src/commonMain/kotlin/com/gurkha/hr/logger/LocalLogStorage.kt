package com.gurkha.hr.logger

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path
import okio.SYSTEM

class LocalLogStorage(
    private val fileSystem: FileSystem = FileSystem.SYSTEM,
    private val filePath: Path = getLogFilePath(),
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    private val json = Json { prettyPrint = false; ignoreUnknownKeys = true }

    // Mutex ensures thread safety
    private val mutex = Mutex()

    // In-memory buffer
    private val buffer = mutableListOf<LogEntry>()

    // Control how often logs are flushed
    private val flushIntervalMs = 5_000L
    private val maxBufferSize = 20

    private var flushJob: Job? = null

    init {
        // Start periodic flush job
        startPeriodicFlush()
    }

    suspend fun appendLog(entry: LogEntry) {
        mutex.withLock {
            buffer.add(entry)

            if (buffer.size >= maxBufferSize) {
                flushToDisk()
            }
        }
    }

    private fun startPeriodicFlush() {
        flushJob = coroutineScope.launch {
            while (isActive) {
                delay(flushIntervalMs)
                mutex.withLock {
                    if (buffer.isNotEmpty()) {
                        flushToDisk()
                    }
                }
            }
        }
    }

    private suspend fun flushToDisk() {
        val logsToWrite: List<LogEntry> = buffer.toList()
        buffer.clear()

        val existing = readAll().toMutableList().apply {
            addAll(logsToWrite)
            if (size > 500) {
                val start = size - 500
                val trimmed = subList(start, size).toList()
                clear()
                addAll(trimmed)
            }
        }

        fileSystem.write(filePath, mustCreate = false) {
            writeUtf8(json.encodeToString(ListSerializer(LogEntry.serializer()), existing))
        }
    }

    suspend fun readAll(): List<LogEntry> {
        return if (fileSystem.exists(filePath)) {
            val text = fileSystem.read(filePath) { readUtf8() }
            runCatching {
                json.decodeFromString(ListSerializer(LogEntry.serializer()), text)
            }.getOrElse { emptyList() }
        } else emptyList()
    }

    suspend fun clear() {
        mutex.withLock {
            buffer.clear()
            if (fileSystem.exists(filePath)) fileSystem.delete(filePath)
        }
    }

    suspend fun flushNow() {
        mutex.withLock {
            flushToDisk()
        }
    }

    fun stop() {
        flushJob?.cancel()
        coroutineScope.launch { flushNow() }
    }
}