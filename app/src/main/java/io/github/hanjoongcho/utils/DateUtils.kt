package io.github.hanjoongcho.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Administrator on 2017-11-21.
 */

class DateUtils {

    companion object {

        fun getFullPatternDate(timeMillis: Long): String {
            val date = Date(timeMillis)
            val dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.FULL, Locale.getDefault())
            return dateFormat.format(date)
        }
    }
}