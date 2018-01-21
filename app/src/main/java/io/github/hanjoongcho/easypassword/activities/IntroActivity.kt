package io.github.hanjoongcho.easypassword.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.graphics.ColorUtils
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.github.hanjoongcho.commons.helpers.TransitionHelper
import io.github.hanjoongcho.commons.extensions.baseConfig
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.commons.helpers.AAF_PATTERN_LOCK_DEFAULT
import kotlinx.android.synthetic.main.activity_intro.*

/**
 * Created by CHO HANJOONG on 2016-12-31.
 */

class IntroActivity : AppCompatActivity(), Handler.Callback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        setContentView(R.layout.activity_intro)
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume INTRO_MODE: ${intent.getIntExtra(INTRO_MODE, INTRO_MODE_DEFAULT)}")
        when (intent.getIntExtra(INTRO_MODE, INTRO_MODE_DEFAULT)) {
            INTRO_MODE_DEFAULT -> Handler(this).sendEmptyMessageDelayed(START_MAIN_ACTIVITY, 1500)
            INTRO_MODE_RESTART -> {
                val context = this@IntroActivity
                val introActivity = Intent(context, IntroActivity::class.java)
                val mPendingIntentId = 123456
                val mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, introActivity, PendingIntent.FLAG_CANCEL_CURRENT)
                val mgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, mPendingIntent)
                intent.putExtra(INTRO_MODE, INTRO_MODE_DEFAULT)

                ActivityCompat.finishAffinity(this)
                System.runFinalizersOnExit(true)
                System.exit(0)
            }
        }
        main_holder.setBackgroundColor(ColorUtils.setAlphaComponent(baseConfig.primaryColor, 255))
    }

    override fun handleMessage(message: Message): Boolean {
        when (message.what) {
            START_MAIN_ACTIVITY -> {
                val savedPattern =  baseConfig.aafPatternLockSaved
                when (savedPattern) {
                    AAF_PATTERN_LOCK_DEFAULT -> {
                        TransitionHelper.startActivityWithTransition(
                                this@IntroActivity,
                                Intent(this, PatternLockActivity::class.java).apply {
                                    putExtra(PatternLockActivity.MODE, PatternLockActivity.SETTING_LOCK)
                                },
                                ActivityOptionsCompat.makeSceneTransitionAnimation(this@IntroActivity,
                                        *TransitionHelper.createSafeTransitionParticipants(
                                                this@IntroActivity,
                                                false,
                                                Pair(app_icon, getString(R.string.transition_app_icon))
                                        )
                                ).toBundle()
                        )
                    }
                    else -> {
                        TransitionHelper.startActivityWithTransition(
                                this@IntroActivity,
                                Intent(this, PatternLockActivity::class.java).apply {
                                    putExtra(PatternLockActivity.MODE, PatternLockActivity.UNLOCK)
                                },
                                ActivityOptionsCompat.makeSceneTransitionAnimation(this@IntroActivity,
                                        *TransitionHelper.createSafeTransitionParticipants(
                                                this@IntroActivity,
                                                false,
                                                Pair(app_icon, getString(R.string.transition_app_icon))
                                        )
                                ).toBundle()
                        )
                    }
                }
            }
            else -> {
            }
        }
        return false
    }

    override fun onBackPressed() {}
    
    companion object {
        const val TAG = "RESTORE"
        const val START_MAIN_ACTIVITY = 0
        const val INTRO_MODE = "intro_mode"
        const val INTRO_MODE_DEFAULT = 0
        const val INTRO_MODE_RESTART = 1
    }
}
