package io.github.hanjoongcho.easypassword.models

import android.support.annotation.ColorRes
import android.support.annotation.StyleRes
import io.github.hanjoongcho.easypassword.R

/**
 * Created by Administrator on 2017-11-17.
 */

enum class Theme(
        @ColorRes val primaryColor: Int,
        @ColorRes val primaryDarkColor: Int,
        @ColorRes val windowBackgroundColor: Int,
        @ColorRes val textPrimaryColor: Int,
        @ColorRes val accentColor: Int,
        @StyleRes val styleId: Int) {
    topeka(R.color.colorPrimary, R.color.colorPrimaryDark,
            R.color.theme_blue_background, R.color.theme_blue_text,
            R.color.colorAccent, R.style.Topeka),
    blue(R.color.theme_blue_primary, R.color.theme_blue_primary_dark,
            R.color.theme_blue_background, R.color.theme_blue_text,
            R.color.theme_blue_accent, R.style.Topeka_Blue),
    green(R.color.theme_green_primary, R.color.theme_green_primary_dark,
            R.color.theme_green_background, R.color.theme_green_text,
            R.color.theme_green_accent, R.style.Topeka_Green),
    purple(R.color.theme_purple_primary, R.color.theme_purple_primary_dark,
            R.color.theme_purple_background, R.color.theme_purple_text,
            R.color.theme_purple_accent, R.style.Topeka_Purple),
    red(R.color.theme_red_primary, R.color.theme_red_primary_dark,
            R.color.theme_red_background, R.color.theme_red_text,
            R.color.theme_red_accent, R.style.Topeka_Red),
    yellow(R.color.theme_yellow_primary, R.color.theme_yellow_primary_dark,
            R.color.theme_yellow_background, R.color.theme_yellow_text,
            R.color.theme_yellow_accent, R.style.Topeka_Yellow)
}