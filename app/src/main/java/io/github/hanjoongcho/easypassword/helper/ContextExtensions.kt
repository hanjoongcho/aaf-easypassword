package io.github.hanjoongcho.easypassword.helper

import android.content.Context
import io.github.hanjoongcho.easypassword.persistence.DatabaseHelper

/**
 * Created by Administrator on 2017-11-17.
 */

fun Context.database() = DatabaseHelper.getInstance(this)
