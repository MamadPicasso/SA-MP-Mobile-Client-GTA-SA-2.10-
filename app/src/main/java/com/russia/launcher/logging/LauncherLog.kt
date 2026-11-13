package com.russia.launcher.logging

import android.os.Build
import android.os.Process
import android.util.Log
import com.russia.launcher.config.Config
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.system.exitProcess

object LauncherLog {
    private const val TAG = "RockstarLauncher"
    private val lock = Any()
    private val timestampFormatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)
        .withZone(ZoneId.systemDefault())
    private val logFile by lazy {
        File(Config.getGameDataDirectory(), "Log/log.txt")
    }

    @Volatile
    private var initialized = false

    @Synchronized
    fun initialize() {
        if (initialized) return
        initialized = true

        val previousHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            error("UncaughtException", "thread=${thread.name}", throwable)
            if (previousHandler != null) {
                previousHandler.uncaughtException(thread, throwable)
            } else {
                Process.killProcess(Process.myPid())
                exitProcess(10)
            }
        }

        info(
            "Process",
            "started pid=${Process.myPid()} sdk=${Build.VERSION.SDK_INT} " +
                "device=${Build.MANUFACTURER} ${Build.MODEL} " +
                "gameData=${Config.getGameDataDirectory().absolutePath}"
        )
    }

    fun info(component: String, message: String) {
        write(Log.INFO, "INFO", component, message, null)
    }

    fun warn(component: String, message: String) {
        write(Log.WARN, "WARN", component, message, null)
    }

    fun error(component: String, message: String, throwable: Throwable? = null) {
        write(Log.ERROR, "ERROR", component, message, throwable)
    }

    private fun write(
        priority: Int,
        level: String,
        component: String,
        message: String,
        throwable: Throwable?
    ) {
        val stackTrace = throwable?.let { "\n${Log.getStackTraceString(it)}" }.orEmpty()
        val line = "${timestampFormatter.format(Instant.now())} $level/$component: $message$stackTrace\n"
        Log.println(priority, TAG, line.trimEnd())

        synchronized(lock) {
            try {
                val parent = logFile.parentFile
                if (parent != null && !parent.isDirectory && !parent.mkdirs()) {
                    throw IOException("Could not create log directory: ${parent.absolutePath}")
                }
                FileOutputStream(logFile, true).use { output ->
                    output.write(line.toByteArray(Charsets.UTF_8))
                }
            } catch (exception: IOException) {
                Log.e(TAG, "Could not append to ${logFile.absolutePath}", exception)
            } catch (exception: SecurityException) {
                Log.e(TAG, "No permission to append to ${logFile.absolutePath}", exception)
            }
        }
    }
}
