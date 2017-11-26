package io.github.hanjoongcho.easypassword.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.github.hanjoongcho.easypassword.helper.EasyPasswordHelper
import io.github.hanjoongcho.utils.CommonUtils

/**
 * Created by CHO HANJOONG on 2017-11-25.
 */

open class CommonActivity : AppCompatActivity() {

    override fun onPause() {
        super.onPause()
        CommonUtils.saveLongPreference(this@CommonActivity, SETTING_PAUSE_MILLIS, System.currentTimeMillis())
    }

    override fun onResume() {
        super.onResume()
        val pauseMillis = CommonUtils.loadLongPreference(this@CommonActivity, SETTING_PAUSE_MILLIS, 0L)
        Log.i(IntroActivity.TAG, "pauseMillis: ${System.currentTimeMillis() - pauseMillis}")
        if (pauseMillis != 0L) {
            if (System.currentTimeMillis() - pauseMillis > 1000) {
                val intent = Intent(this@CommonActivity, IntroActivity::class.java).apply {
                    putExtra(PatternLockActivity.MODE, PatternLockActivity.UNLOCK)
                    putExtra(IntroActivity.FORWARD_ACTIVITY, true)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                EasyPasswordHelper.startSettingActivityWithTransition(this@CommonActivity, intent)
            }
        }
    }

    companion object {

        const val SETTING_PAUSE_MILLIS = "setting_pause_millis"
    }
}