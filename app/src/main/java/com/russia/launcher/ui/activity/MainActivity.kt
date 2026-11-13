package com.russia.launcher.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.russia.game.R
import com.russia.game.core.Samp
import com.russia.launcher.config.Config
import com.russia.launcher.logging.LauncherLog
import com.russia.launcher.storage.NativeStorage
import org.apache.commons.lang3.StringUtils
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LauncherLog.initialize()
        LauncherLog.info("MainActivity", "onCreate")
        setTheme(R.style.AppBaseTheme)
        setContentView(R.layout.activity_simple_main)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        findViewById<View>(R.id.playButton).setOnClickListener { startGame() }
    }

    private fun startGame() {
        LauncherLog.info("MainActivity", "start button pressed")
        if (!ensureGameStorageAccess()) {
            LauncherLog.warn("MainActivity", "game launch paused for storage permission")
            return
        }

        val gameDataDirectory = Config.getGameDataDirectory()
        LauncherLog.info("MainActivity", "checking game data at ${gameDataDirectory.absolutePath}")
        val missingFiles = findMissingGameData(gameDataDirectory)
        if (missingFiles.isNotEmpty()) {
            LauncherLog.error("MainActivity", "required game data missing: ${missingFiles.joinToString()}")
            AlertDialog.Builder(this)
                .setTitle(R.string.launcher_missing_data_title)
                .setMessage(
                    getString(
                        R.string.launcher_missing_data_message,
                        missingFiles.joinToString("\n") { "- $it" },
                        gameDataDirectory.absolutePath
                    )
                )
                .setPositiveButton(R.string.launcher_ok, null)
                .show()
            return
        }

        LauncherLog.info("MainActivity", "required game data check passed")
        setDefaultServer()

        LauncherLog.info("MainActivity", "saving launcher settings")
        NativeStorage.addClientProperty("password", StringUtils.EMPTY, this)
        val intent = Intent(this, Samp::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        LauncherLog.info("MainActivity", "starting Samp activity")
        startActivity(intent)
        finish()
    }

    private fun findMissingGameData(gameDataDirectory: File): List<String> {
        val missingFiles = mutableListOf<String>()

        REQUIRED_DIRECTORIES
            .filterNot { File(gameDataDirectory, it).isDirectory }
            .forEach { missingFiles.add(it) }

        REQUIRED_FILES
            .filterNot { File(gameDataDirectory, it).isFile }
            .forEach { missingFiles.add(it) }

        for (database in REQUIRED_TEXTURE_DATABASES) {
            val directory = File(gameDataDirectory, "texdb/$database")
            val hasDatabaseData = directory.listFiles()?.any { file ->
                file.isFile &&
                    file.name.startsWith("$database.", ignoreCase = true) &&
                    file.extension.equals("dat", ignoreCase = true)
            } == true

            if (!hasDatabaseData) {
                missingFiles.add("texdb/$database/$database.*.dat")
            }
        }

        return missingFiles
    }

    private fun setDefaultServer() {
        LauncherLog.info("MainActivity", "setting server ${Config.SERVER_IP}:${Config.SERVER_PORT}")
        NativeStorage.addClientProperty("ip", Config.SERVER_IP, this)
        NativeStorage.addClientProperty("port", Config.SERVER_PORT.toString(), this)
        NativeStorage.addClientProperty("server", "0", this)
    }

    private fun ensureGameStorageAccess(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                LauncherLog.info("MainActivity", "all-files storage access granted")
                return true
            }

            LauncherLog.warn("MainActivity", "all-files storage access is not granted")
            Toast.makeText(
                this,
                getString(R.string.launcher_storage_permission_message),
                Toast.LENGTH_LONG
            ).show()
            startActivity(
                Intent(
                    Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                    Uri.parse("package:$packageName")
                )
            )
            return false
        }

        val readGranted = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val writeGranted = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q ||
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

        if (!readGranted || !writeGranted) {
            LauncherLog.warn(
                "MainActivity",
                "legacy storage permission missing: read=$readGranted write=$writeGranted"
            )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                STORAGE_PERMISSION_REQUEST
            )
            return false
        }
        LauncherLog.info("MainActivity", "legacy storage permissions granted")
        return true
    }

    companion object {
        private const val STORAGE_PERMISSION_REQUEST = 229
        private val REQUIRED_DIRECTORIES = listOf("data", "models", "SAMP")
        private val REQUIRED_FILES = listOf(
            "texdb/gta3.img",
            "texdb/gta3/gta3.txt",
            "texdb/gta3/txd.txt",
            "texdb/txd/txd.txt",
            "TEXT/AMERICAN.GXT"
        )
        private val REQUIRED_TEXTURE_DATABASES = listOf(
            "mobile",
            "txd",
            "gta3",
            "gta_int",
            "cutscene",
            "player",
            "menu"
        )
    }
}