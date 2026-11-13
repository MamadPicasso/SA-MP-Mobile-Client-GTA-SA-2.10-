package com.russia.launcher.domain.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    NICKNAME_IS_EMPTY("نام کاربری را وارد کنید."),
    EMAIL_IS_EMPTY("ایمیل را وارد کنید."),
    DONATE_SUM_IS_EMPTY("مبلغ را وارد کنید."),
    SERVER_NOT_SELECTED("سرور را انتخاب کنید."),
    NICKNAME_CONTAINS_DIGITS("نام کاربری نباید شامل عدد باشد."),
    DONATE_SUM_CONTAINS_SYMBOLS_OTHER_DIGITS("مبلغ باید فقط شامل عدد باشد."),
    NICKNAME_IS_TOO_LONG("نام کاربری باید کمتر از ۲۰ نویسه باشد."),
    EMAIL_IS_TOO_LONG("ایمیل باید کمتر از ۱۰۰ نویسه باشد."),
    NICKNAME_DOES_NOT_MATCH_PATTERN("قالب نام کاربری باید «نام_نام‌خانوادگی» باشد."),
    EMAIL_DOES_NOT_MATCH_PATTERN("قالب ایمیل معتبر نیست؛ نمونه: example@example.com"),
    PASSWORD_IS_EMPTY("رمز عبور را وارد کنید."),
    PASSWORD_IS_TOO_LONG("رمز عبور باید کمتر از ۱۷ نویسه باشد."),
    PASSWORD_IS_TOO_SHORT("رمز عبور باید بیش از ۵ نویسه باشد."),
    TEST_SERVER_PASSWORD_NOT_INPUT("رمز عبور سرور آزمایشی وارد نشده است."),

    INPUT_NICKNAME_BEFORE_SERVER_CONNECT("پیش از اتصال به سرور، نام کاربری را وارد کنید."),
    CAPTCHA_NOT_PASSED("پیش از ورود، تأیید کنید که ربات نیستید."),
    CAPTCHA_COMPLETING_PROBLEMS("تأیید هویت انجام نشد؛ دوباره تلاش کنید."),

    FIRSTLY_LOAD_GAME("ابتدا بازی را دانلود کنید."),

    FAILED_LOAD_LOADER_SLIDER_DATA("بارگذاری اطلاعات انجام نشد."),
    APK_UPDATE_FILE_NOT_FOUND("خطای نصب: فایل پیدا نشد.");


    private final String text;

}
