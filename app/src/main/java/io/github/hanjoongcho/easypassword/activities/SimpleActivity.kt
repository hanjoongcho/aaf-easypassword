package io.github.hanjoongcho.easypassword.activities

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import com.simplemobiletools.commons.extensions.updateTextColors
import io.github.hanjoongcho.commons.activities.BaseSimpleActivity
import io.github.hanjoongcho.commons.extensions.baseConfig
import io.github.hanjoongcho.commons.helpers.TransitionHelper
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.extensions.initTextSize

/**
 * Created by Administrator on 2017-12-29.
 */

open class SimpleActivity : BaseSimpleActivity() {
    override fun onPause() {
        super.onPause()
        baseConfig.aafPatternLockPauseMillis = System.currentTimeMillis()
    }

    override fun onResume() {
        super.onResume()
        val pauseMillis = baseConfig.aafPatternLockPauseMillis
        Log.i(IntroActivity.TAG, "pauseMillis: ${System.currentTimeMillis() - pauseMillis}")
        if (pauseMillis != 0L) {
            if (System.currentTimeMillis() - pauseMillis > 1000) {
                val intent = Intent(this@SimpleActivity, PatternLockActivity::class.java).apply {
                    putExtra(PatternLockActivity.MODE, PatternLockActivity.UNLOCK_RESUME)
//                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                TransitionHelper.startActivityWithTransition(this@SimpleActivity, intent)
            }
        }
        initTextSize(findViewById(android.R.id.content), this@SimpleActivity);
        updateTextColors(findViewById(android.R.id.content))
    }

    override fun getMainViewGroup(): ViewGroup? = findViewById(R.id.main_holder)
}