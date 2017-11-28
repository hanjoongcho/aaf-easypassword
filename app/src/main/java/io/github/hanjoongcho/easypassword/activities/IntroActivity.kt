package io.github.hanjoongcho.easypassword.activities

import android.annotation.TargetApi
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.helper.EasyPasswordHelper
import io.github.hanjoongcho.utils.CommonUtils
import kotlinx.android.synthetic.main.activity_intro.*

/**
 * Created by CHO HANJOONG on 2016-12-31.
 */

class IntroActivity : AppCompatActivity(), Handler.Callback {

    private var mInitCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

    }

    override fun onResume() {
        super.onResume()
        mInitCount++
        Log.i(TAG, "mInitCount: $mInitCount, FORWARD_ACTIVITY: ${intent.getIntExtra(INTRO_MODE, INTRO_MODE_DEFAULT)}")
        if (mInitCount == 1) {
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
                *EasyPasswordHelper.createSafeTransitionParticipants(this@IntroActivity,
                        false,
                        Pair(app_icon, getString(R.string.transition_app_icon))))
                .toBundle()

        // Start the activity with the participants, animating from one to the other.
        val intent = Intent(this, PatternLockActivity::class.java)
        intent.putExtra(PatternLockActivity.MODE, mode)
        ActivityCompat.startActivity(this@IntroActivity, intent, animationBundle)
    }

    companion object {

        const val TAG = "RESTORE"
        const val START_MAIN_ACTIVITY = 0
        const val INTRO_MODE = "intro_mode"
        const val INTRO_MODE_DEFAULT = 0
        const val INTRO_MODE_RESTART = 1
    }
}
