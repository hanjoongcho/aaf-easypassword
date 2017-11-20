package io.github.hanjoongcho.easypassword.activities

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.helper.TransitionHelper
import io.github.hanjoongcho.utils.CommonUtils
import kotlinx.android.synthetic.main.activity_intro.*

/**
 * Created by CHO HANJOONG on 2016-12-31.
 */

class IntroActivity : AppCompatActivity(), Handler.Callback {

    var initCount = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

    }

    override fun onResume() {
        super.onResume()
        if (initCount++ < 1) {
            Handler(this).sendEmptyMessageDelayed(START_MAIN_ACTIVITY, 1500)
        } else {
            finish()
        }
    }

    override fun handleMessage(message: Message): Boolean {
        when (message.what) {
            START_MAIN_ACTIVITY -> {
                val savedPattern =  CommonUtils.loadStringPreference(this@IntroActivity, PatternLockActivity.SAVED_PATTERN, PatternLockActivity.SAVED_PATTERN_DEFAULT)
                when (savedPattern) {
                    PatternLockActivity.SAVED_PATTERN_DEFAULT -> startActivityWithTransition(PatternLockActivity.SETTING_LOCK)
                    else -> startActivityWithTransition(PatternLockActivity.UNLOCK)
                }
            }
            else -> {
            }
        }
        return false
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startActivityWithTransition(mode: Int) {

        val animationBundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this@IntroActivity,
                *TransitionHelper.createSafeTransitionParticipants(this@IntroActivity,
                        false,
                        Pair(app_icon, getString(R.string.transition_app_icon))))
                .toBundle()

        // Start the activity with the participants, animating from one to the other.
        val intent = Intent(this, PatternLockActivity::class.java)
        intent.putExtra(PatternLockActivity.MODE, mode)
        ActivityCompat.startActivity(this@IntroActivity, intent, animationBundle)
//        finish()
    }

    companion object {

        const val START_MAIN_ACTIVITY = 0
    }
}
