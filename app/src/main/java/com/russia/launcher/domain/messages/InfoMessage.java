package com.russia.launcher.domain.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InfoMessage {
    CONNECTION_TO_LAST_SERVER("در حال اتصال به آخرین سرور انتخاب‌شده…"),
    CONNECTION_TO_SELECT_SERVER("در حال اتصال به سرور: %s…"),
    SERVER_SELECTED("سرور انتخاب شد. برای شروع بازی دکمهٔ بازی را بزنید."),
    INSTALL_GAME_FIRST("ابتدا بازی را نصب کنید."),
    GAME_FILES_VALID("فایل‌های بازی سالم هستند."),
    SETTINGS_ALREADY_DEFAULT("تنظیمات از قبل پیش‌فرض هستند."),
    DOWNLOAD_SUCCESS_INPUT_YOUR_NICKNAME("بازی نصب شد؛ نام کاربری خود را وارد کنید."),
    DOWNLOAD_SUCCESS_SELECT_SERVER("بازی نصب شد؛ سرور را انتخاب کنید."),
    REINSTALL_GAME_QUESTION("اطلاعات محل داده‌های بازی نمایش داده شود؟"),
    RESET_SETTINGS_QUESTION("تنظیمات بازی بازنشانی شود؟"),
    APPROVE_INSTALL("نصب را تأیید کنید.");

    private final String text;

    public static String createConnectToSelectServerMessage(String serverName) {
        return String.format(
                CONNECTION_TO_SELECT_SERVER.getText(),
                serverName
        );
    }
}
