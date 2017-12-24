package io.github.hanjoongcho.easypassword.extensions

import android.content.Context
import io.github.hanjoongcho.easypassword.helper.Config

/**
 * Created by CHO HANJOONG on 2017-12-24.
 */

val Context.config: Config get() = Config.newInstance(applicationContext)