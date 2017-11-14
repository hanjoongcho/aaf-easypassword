package io.github.hanjoongcho.easypassword.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import io.github.hanjoongcho.easypassword.R

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
                startActivity(Intent(this, PatternLockActivity::class.java))
                finish()
            }
            else -> {
            }
        }
        return false
    }

}
