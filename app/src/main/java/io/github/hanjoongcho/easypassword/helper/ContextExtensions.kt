package io.github.hanjoongcho.easypassword.helper

import android.content.Context
import io.github.hanjoongcho.easypassword.persistence.DatabaseHelper

/**
 * Created by Administrator on 2017-11-17.
 * This code based 'Simple-Commons' package
 * You can see original 'Simple-Commons' from below link.
 * https://github.com/SimpleMobileTools/Simple-Commons
 */

fun Context.database() = DatabaseHelper.getInstance(this)
