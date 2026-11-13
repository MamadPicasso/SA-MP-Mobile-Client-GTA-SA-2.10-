package com.russia.launcher.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.russia.game.R
import com.russia.game.core.Samp
import com.russia.launcher.config.Config
import com.russia.launcher.service.ActivityService
import com.russia.launcher.service.impl.ActivityServiceImpl
import com.russia.launcher.storage.NativeStorage
import com.russia.launcher.ui.dialogs.ConfirmDialog
import com.russia.launcher.ui.dialogs.EnterNicknameDialog
import java.io.File

class SettingsFragment : Fragment(), View.OnClickListener {
    private var animation: Animation? = null
    private var nicknameField: TextView? = null
    private var activityService: ActivityService? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activityService = ActivityServiceImpl()
        val inflate = inflater.inflate(R.layout.fragment_settings, container, false)
        animation = AnimationUtils.loadAnimation(context, R.anim.button_click)
        nicknameField = inflate.findViewById(R.id.nick_edit)
        nicknameField?.setOnClickListener(this)
        inflate.findViewById<View>(R.id.youtubeButton).setOnClickListener(this)
        inflate.findViewById<View>(R.id.vkButton).setOnClickListener(this)
        inflate.findViewById<View>(R.id.discordButton).setOnClickListener(this)
        inflate.findViewById<View>(R.id.resetSettings).setOnClickListener(this)
        inflate.findViewById<View>(R.id.reinstallGame).setOnClickListener(this)
        inflate.findViewById<View>(R.id.validateCache).visibility = View.GONE
        inflate.findViewById<View>(R.id.telegramButton).setOnClickListener(this)
        initUserData()
        return inflate
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.youtubeButton -> {
                view.startAnimation(animation)
                performYouTubeButtonAction()
            }

            R.id.vkButton -> {
                view.startAnimation(animation)
                performVkButtonAction()
            }

            R.id.discordButton -> {
                view.startAnimation(animation)
                performDiscordButtonAction()
            }

            R.id.telegramButton -> {
                view.startAnimation(animation)
                performTelegramButtonAction()
            }

            R.id.resetSettings -> {
                view.startAnimation(animation)
                performResetSettingsButtonAction()
            }

            R.id.reinstallGame -> {
                view.startAnimation(animation)
                performReinstallGameButtonAction()
            }

            R.id.nick_edit -> performNickEditFieldOnClickAction()
            else -> {}
        }
    }

    private fun performNickEditFieldOnClickAction() {
        EnterNicknameDialog(this)
    }

    private fun performReinstallGameButtonAction() {
        val confirmDialog = ConfirmDialog(activity, "Переустановить игру?")
        confirmDialog.setOnDialogCloseListener { isConfirm: Boolean -> onConfirmReinstallFinished(isConfirm) }
        confirmDialog.createDialog()
    }

    private fun onConfirmReinstallFinished(isConfirm: Boolean) {
        if (isConfirm) {
            activityService?.showInfoMessage(
                "Manage the Google Play game data in /storage/emulated/0/ROCKSTAR.",
                requireActivity()
            )
        }
    }

    private fun performResetSettingsButtonAction() {
        val confirmDialog = ConfirmDialog(activity, "Сбросить настройки игры?")
        confirmDialog.setOnDialogCloseListener { isConfirm: Boolean -> onConfirmResetSettingsFinished(isConfirm) }
        confirmDialog.createDialog()
    }

    private fun onConfirmResetSettingsFinished(isConfirm: Boolean) {
        if (!isConfirm) {
            return
        }
        if (!activityService!!.isGameFileInstall(activity, Config.SETTINGS_FILE_PATH)) {
            activityService!!.showInfoMessage("Сначала установите игру", activity)
            return
        }
        val settingsFile = File(Config.getGameDataDirectory(), Config.SETTINGS_FILE_PATH.substring(1))
        if (settingsFile.exists()) {
            settingsFile.delete()
            activityService!!.showInfoMessage("Вы успешно сбросили настройки!", activity)
        } else {
            activityService!!.showInfoMessage("Настройки по умолчанию уже установлены", activity)
        }
    }

    private fun performDiscordButtonAction() {
        startActivity(Intent(CONTACTS_VIEW_ACTION, Uri.parse(Config.DISCORD_URI)))
    }

    private fun performVkButtonAction() {
        startActivity(Intent(CONTACTS_VIEW_ACTION, Uri.parse(Config.VK_URI)))
    }

    private fun performYouTubeButtonAction() {
        startActivity(Intent(CONTACTS_VIEW_ACTION, Uri.parse(Config.YOU_TUBE_URI)))
    }

    private fun performTelegramButtonAction() {
        startActivity(Intent(CONTACTS_VIEW_ACTION, Uri.parse(Config.TELEGRAM_URI)))
    }

    private fun initUserData() {
        val nickname = NativeStorage.getClientProperty("name", this.activity)
        nicknameField!!.text = nickname
    }

    fun updateNicknameField(nickname: String?) {
        nicknameField!!.text = nickname
    }

    companion object {
        private const val GAME_DIRECTORY_EMPTY_SIZE = 0
        private const val CONTACTS_VIEW_ACTION = "android.intent.action.VIEW"
    }
}