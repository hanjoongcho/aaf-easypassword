package io.github.hanjoongcho.easypassword.activities

import android.R
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.github.hanjoongcho.easypassword.extensions.getThemeId
import io.github.hanjoongcho.easypassword.extensions.initTextSize
import io.github.hanjoongcho.easypassword.helper.EasyPasswordHelper
import io.github.hanjoongcho.utils.CommonUtils

/**
 * Created by CHO HANJOONG on 2017-11-25.
 * This code based 'Simple-Commons' package
 * You can see original 'Simple-Commons' from below link.
 * https://github.com/SimpleMobileTools/Simple-Commons
 */

open class CommonActivity : AppCompatActivity() {
    var useDynamicTheme = true

    override fun onCreate(savedInstanceState: Bundle?) {
        if (useDynamicTheme) {
            setTheme(getThemeId())
        }

        super.onCreate(savedInstanceState)
    }
    
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
                val intent = Intent(this@CommonActivity, PatternLockActivity::class.java).apply {
                    putExtra(PatternLockActivity.MODE, PatternLockActivity.UNLOCK_RESUME)
//                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                EasyPasswordHelper.startSettingActivityWithTransition(this@CommonActivity, intent)
            }
        }
        initTextSize(findViewById(R.id.content), this@CommonActivity);
    }

    companion object {
        const val SETTING_PAUSE_MILLIS = "setting_pause_millis"
    }
}