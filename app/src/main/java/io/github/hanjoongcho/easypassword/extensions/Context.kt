package io.github.hanjoongcho.easypassword.extensions

import android.content.Context
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.helper.*

/**
 * Created by CHO HANJOONG on 2017-12-24.
 */

fun Context.getTextSize() =
        when (config.fontSize) {
            FONT_SIZE_SMALL -> resources.getDimension(R.dimen.smaller_text_size)
            FONT_SIZE_LARGE -> resources.getDimension(R.dimen.big_text_size)
            FONT_SIZE_EXTRA_LARGE -> resources.getDimension(R.dimen.extra_big_text_size)
            else -> resources.getDimension(R.dimen.bigger_text_size)
        }

val Context.config: Config get() = Config.newInstance(applicationContext)