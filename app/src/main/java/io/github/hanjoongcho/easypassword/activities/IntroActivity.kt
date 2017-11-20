package io.github.hanjoongcho.easypassword.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.utils.CommonUtils

/**
 * Created by CHO HANJOONG on 2016-12-31.
 */

class IntroActivity : Activity(), Handler.Callback {

    companion object {
        private val START_MAIN_ACTIVITY = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        Handler(this).sendEmptyMessageDelayed(START_MAIN_ACTIVITY, 1500)
    }

    override fun handleMessage(message: Message): Boolean {
        when (message.what) {
            START_MAIN_ACTIVITY -> {
                val savedPattern =  CommonUtils.loadStringPreference(this@IntroActivity, PatternLockActivity.SAVED_PATTERN, PatternLockActivity.SAVED_PATTERN_DEFAULT)
                val intent = Intent(this, PatternLockActivity::class.java)
                when (savedPattern) {
                    PatternLockActivity.SAVED_PATTERN_DEFAULT -> intent.putExtra(PatternLockActivity.MODE, PatternLockActivity.SETTING_LOCK)
                    else -> intent.putExtra(PatternLockActivity.MODE, PatternLockActivity.UNLOCK)
                }
                startActivity(intent)
                finish()
            }
            else -> {
            }
        }
        return false
    }

}
