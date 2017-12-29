package io.github.hanjoongcho.easypassword.activities

import android.content.Intent
import android.util.Log
import io.github.hanjoongcho.commons.activities.BaseSimpleActivity
import io.github.hanjoongcho.commons.helpers.TransitionHelper
import io.github.hanjoongcho.utils.CommonUtils
import io.github.hanjoongcho.easypassword.extensions.initTextSize

/**
 * Created by Administrator on 2017-12-29.
 */

open class SimpleActivity : BaseSimpleActivity() {
    override fun onPause() {
        super.onPause()
        CommonUtils.saveLongPreference(this@SimpleActivity, SETTING_PAUSE_MILLIS, System.currentTimeMillis())
    }

    override fun onResume() {
        super.onResume()
        val pauseMillis = CommonUtils.loadLongPreference(this@SimpleActivity, SETTING_PAUSE_MILLIS, 0L)
        Log.i(IntroActivity.TAG, "pauseMillis: ${System.currentTimeMillis() - pauseMillis}")
        if (pauseMillis != 0L) {
            if (System.currentTimeMillis() - pauseMillis > 1000) {
                val intent = Intent(this@SimpleActivity, PatternLockActivity::class.java).apply {
                    putExtra(PatternLockActivity.MODE, PatternLockActivity.UNLOCK_RESUME)
//                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                TransitionHelper.startSettingActivityWithTransition(this@SimpleActivity, intent)
            }
        }
        initTextSize(findViewById(android.R.id.content), this@SimpleActivity);
    }

    companion object {
        const val SETTING_PAUSE_MILLIS = "setting_pause_millis"
    }
}