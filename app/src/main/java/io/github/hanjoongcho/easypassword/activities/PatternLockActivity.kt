package io.github.hanjoongcho.easypassword.activities

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Window
import android.view.WindowManager

import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.andrognito.patternlockview.utils.ResourceUtils
import com.andrognito.rxpatternlockview.RxPatternLockView
import com.andrognito.rxpatternlockview.events.PatternLockCompoundEvent
import io.github.hanjoongcho.easypassword.R
import kotlinx.android.synthetic.main.activity_pattern_lock.*

import io.reactivex.functions.Consumer

class PatternLockActivity : AppCompatActivity() {

    private val mPatternLockViewListener = object : PatternLockViewListener {
        override fun onStarted() {
            Log.d(javaClass.name, "Pattern drawing started")
        }

        override fun onProgress(progressPattern: List<PatternLockView.Dot>) {
            Log.d(javaClass.name, "Pattern progress: " + PatternLockUtils.patternToString(patterLockView, progressPattern))
        }

        override fun onComplete(pattern: List<PatternLockView.Dot>) {
            Log.d(javaClass.name, "Pattern complete: " + PatternLockUtils.patternToString(patterLockView, pattern))
        }

        override fun onCleared() {
            Log.d(javaClass.name, "Pattern has been cleared")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_pattern_lock)

        patterLockView.dotCount = 3
        patterLockView.dotNormalSize = ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_size).toInt()
        patterLockView.dotSelectedSize = ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_selected_size).toInt()
        patterLockView.pathWidth = ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_path_width).toInt()
        patterLockView.isAspectRatioEnabled = true
        patterLockView.aspectRatio = PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS
        patterLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT)
        patterLockView.dotAnimationDuration = 150
        patterLockView.pathEndAnimationDuration = 100
        patterLockView.correctStateColor = ResourceUtils.getColor(this, R.color.white)
        patterLockView.isInStealthMode = false
        patterLockView.isTactileFeedbackEnabled = true
        patterLockView.isInputEnabled = true
        patterLockView.addPatternLockListener(mPatternLockViewListener)

        RxPatternLockView.patternComplete(patterLockView).subscribe({ patternLockCompleteEvent ->
            Log.d(javaClass.name, "Complete: " + patternLockCompleteEvent.pattern.toString())
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@PatternLockActivity)
            builder.setMessage(patternLockCompleteEvent.pattern.toString())
            builder.setPositiveButton("OK", DialogInterface.OnClickListener({ _, _ ->
                finish()
                AccountSelectionActivity.start(this@PatternLockActivity)
            }))
//            builder.create().show()
            patterLockView.clearPattern()
            AccountSelectionActivity.start(this@PatternLockActivity)
        })
//                .subscribe(object : Consumer<PatternLockCompleteEvent> {
//                    @Throws(Exception::class)
//                    override fun accept(patternLockCompleteEvent: PatternLockCompleteEvent) {
////                        Log.d(javaClass.name, "Complete: " + patternLockCompleteEvent.pattern.toString())
//                        val builder: AlertDialog.Builder = AlertDialog.Builder(applicationContext)
//                        builder.setMessage(patternLockCompleteEvent.pattern.toString())
//                        builder.setPositiveButton("OK", null)
//                        builder.create().show()
//                        patterLockView.clearPattern()
//                    }
//                })

        RxPatternLockView.patternChanges(patterLockView)
                .subscribe(object : Consumer<PatternLockCompoundEvent> {
                    @Throws(Exception::class)
                    override fun accept(event: PatternLockCompoundEvent) {
                        when (event.eventType) {
                            PatternLockCompoundEvent.EventType.PATTERN_STARTED -> Log.d(javaClass.name, "Pattern drawing started")
                            PatternLockCompoundEvent.EventType.PATTERN_PROGRESS -> Log.d(javaClass.name, "Pattern progress: " + PatternLockUtils.patternToString(patterLockView, event.pattern))
                            PatternLockCompoundEvent.EventType.PATTERN_COMPLETE -> Log.d(javaClass.name, "Pattern complete: " + PatternLockUtils.patternToString(patterLockView, event.pattern))
                            PatternLockCompoundEvent.EventType.PATTERN_CLEARED ->  Log.d(javaClass.name, "Pattern has been cleared")
                        }
                    }
                })
    }

}
