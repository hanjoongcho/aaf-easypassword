package io.github.hanjoongcho.easypassword.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.andrognito.patternlockview.utils.ResourceUtils
import com.andrognito.rxpatternlockview.RxPatternLockView
import com.andrognito.rxpatternlockview.events.PatternLockCompoundEvent
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.helper.EasyPasswordHelper
import io.github.hanjoongcho.easypassword.helper.database
import io.github.hanjoongcho.utils.AesUtils
import io.github.hanjoongcho.utils.CommonUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_pattern_lock.*

class PatternLockActivity : AppCompatActivity() {

    private var mMode: Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_pattern_lock)

        mMode = intent.getIntExtra(MODE, -1)
        when (mMode) {
            UNLOCK, UNLOCK_RESUME -> guide_message.text = getString(R.string.pattern_lock_activity_unlock_message)
            SETTING_LOCK -> guide_message.text = getString(R.string.pattern_lock_activity_setting_lock_message)
            VERIFY -> guide_message.text = getString(R.string.pattern_lock_activity_verify_message)
        }

        with(patterLockView) {
            dotCount = 3
            dotNormalSize = ResourceUtils.getDimensionInPx(this@PatternLockActivity, R.dimen.pattern_lock_dot_size).toInt()
            dotSelectedSize = ResourceUtils.getDimensionInPx(this@PatternLockActivity, R.dimen.pattern_lock_dot_selected_size).toInt()
            pathWidth = ResourceUtils.getDimensionInPx(this@PatternLockActivity, R.dimen.pattern_lock_path_width).toInt()
            isAspectRatioEnabled = true
            aspectRatio = PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS
            setViewMode(PatternLockView.PatternViewMode.CORRECT)
            dotAnimationDuration = 150
            pathEndAnimationDuration = 100
            correctStateColor = ResourceUtils.getColor(this@PatternLockActivity, R.color.white)
            isInStealthMode = false
            isTactileFeedbackEnabled = true
            isInputEnabled = true
            addPatternLockListener(mPatternLockViewListener)
        }

        RxPatternLockView.patternComplete(patterLockView).subscribe({ patternLockCompleteEvent ->
            Log.d(javaClass.name, "Complete: " + patternLockCompleteEvent.pattern.toString())
//            val builder: AlertDialog.Builder = AlertDialog.Builder(this@PatternLockActivity)
//            builder.setMessage(patternLockCompleteEvent.pattern.toString())
//            builder.setPositiveButton("OK", DialogInterface.OnClickListener({ _, _ ->
//                finish()
//                SecuritySelectionActivity.start(this@PatternLockActivity)
//            }))
//            builder.create().show()
            val savedPattern = CommonUtils.loadStringPreference(this@PatternLockActivity, PatternLockActivity.SAVED_PATTERN, PatternLockActivity.SAVED_PATTERN_DEFAULT)
            when (mMode) {
                UNLOCK -> {
                    if (savedPattern == patternLockCompleteEvent.pattern.toString()) {
//                        SecuritySelectionActivity.start(this@PatternLockActivity)
                        EasyPasswordHelper.startSettingActivityWithTransition(this@PatternLockActivity, SecuritySelectionActivity::class.java)
                        finish()
                    } else {
                        patterLockView.clearPattern()
                        val unlockBuilder: AlertDialog.Builder = AlertDialog.Builder(this@PatternLockActivity).apply {
                            setMessage(getString(R.string.pattern_lock_activity_verify_reject))
                            setPositiveButton("OK", ({ _, _ ->
                                finish()
                            }))
                            setCancelable(false)
                        }
                        unlockBuilder.create().show()
                    }
                }
                SETTING_LOCK -> {
                    val intent = Intent(this, PatternLockActivity::class.java)
                    intent.putExtra(PatternLockActivity.MODE, PatternLockActivity.VERIFY)
                    intent.putExtra(PatternLockActivity.REQUEST_PATTERN, patternLockCompleteEvent.pattern.toString())
                    EasyPasswordHelper.startSettingActivityWithTransition(this@PatternLockActivity, intent)
                    finish()
                }
                VERIFY -> {
                    if (intent.getStringExtra(PatternLockActivity.REQUEST_PATTERN) == patternLockCompleteEvent.pattern.toString()) {
                        val previousPattern = CommonUtils.loadStringPreference(this@PatternLockActivity, PatternLockActivity.SAVED_PATTERN, PatternLockActivity.SAVED_PATTERN_DEFAULT)
                        CommonUtils.saveStringPreference(this@PatternLockActivity, PatternLockActivity.SAVED_PATTERN, patternLockCompleteEvent.pattern.toString())
                        if (this@PatternLockActivity.database().countSecurity() < 1) {
                            EasyPasswordHelper.startSettingActivityWithTransition(this@PatternLockActivity, SecuritySelectionActivity::class.java)
                            finish()
                        } else {
                            progressBar.visibility = View.VISIBLE
                            guide_message.text = getString(R.string.pattern_lock_progress_message)
                            patterLockView.clearPattern()
                            Thread(Runnable {
                                val listSecurity = this@PatternLockActivity.database().selectSecurityAll()
                                this@PatternLockActivity.database().beginTransaction()
                                listSecurity.map {

                                    val previousCipherPassword = it.password
                                    val previousPlainPassword = AesUtils.decryptPassword(this@PatternLockActivity, previousCipherPassword, previousPattern)
                                    val currentCipherPassword = AesUtils.encryptPassword(this@PatternLockActivity, previousPlainPassword)
                                    it.password = currentCipherPassword

                                    it.creditCard?.let { cc ->
                                        val previousCipherSerial = cc.serial
                                        val previousCipherExpireDate = cc.expireDate
                                        val previousCipherCardValidationCode = cc.cardValidationCode

                                        val previousPlainSerial = AesUtils.decryptPassword(this@PatternLockActivity, previousCipherSerial, previousPattern)
                                        val previousPlainExpireDate = AesUtils.decryptPassword(this@PatternLockActivity, previousCipherExpireDate, previousPattern)
                                        val previousPlainCardValidationCode = AesUtils.decryptPassword(this@PatternLockActivity, previousCipherCardValidationCode, previousPattern)

                                        val currentCipherSerial = AesUtils.encryptPassword(this@PatternLockActivity, previousPlainSerial)
                                        val currentCipherExpireDate = AesUtils.encryptPassword(this@PatternLockActivity, previousPlainExpireDate)
                                        val currentCipherCardValidationCode = AesUtils.encryptPassword(this@PatternLockActivity, previousPlainCardValidationCode)

                                        cc.serial = currentCipherSerial
                                        cc.expireDate = currentCipherExpireDate
                                        cc.cardValidationCode = currentCipherCardValidationCode
                                    }
                                }
                                this@PatternLockActivity.database().commitTransaction()
                                Handler(Looper.getMainLooper()).post({
                                    progressBar.visibility = View.INVISIBLE
                                    finish()
                                    overridePendingTransition(0, 0)
                                })
                            }).start()
                        }
                    } else {
                        val builder: AlertDialog.Builder = AlertDialog.Builder(this@PatternLockActivity)
                        builder.setMessage(getString(R.string.pattern_lock_activity_verify_reject))
                        builder.setPositiveButton("OK", ({ _, _ ->
                            intent.putExtra(PatternLockActivity.MODE, PatternLockActivity.SETTING_LOCK)
                            EasyPasswordHelper.startSettingActivityWithTransition(this@PatternLockActivity, intent)
                            finish()
                        }))
                        builder.create().show()
                    }
                }
                UNLOCK_RESUME -> {
                    if (savedPattern == patternLockCompleteEvent.pattern.toString()) {
                        finish()
                        overridePendingTransition(0, 0)
                    } else {
                        patterLockView.clearPattern()
                        val unlockBuilder: AlertDialog.Builder = AlertDialog.Builder(this@PatternLockActivity).apply {
                            setMessage(getString(R.string.pattern_lock_activity_verify_reject))
                            setPositiveButton("OK", ({ _, _ ->
                                ActivityCompat.finishAffinity(this@PatternLockActivity)
                            }))
                            setCancelable(false)
                        }
                        unlockBuilder.create().show()
                    }
                }
            }
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

    override fun onPause() {
        super.onPause()
        CommonUtils.saveLongPreference(this@PatternLockActivity, CommonActivity.SETTING_PAUSE_MILLIS, System.currentTimeMillis())
    }

    override fun onBackPressed() {
        ActivityCompat.finishAffinity(this@PatternLockActivity)
    }

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

    companion object {

        const val TAG = "PatternLockActivity"
        const val MODE = "mMode"
        const val REQUEST_PATTERN = "request_pattern"
        const val SAVED_PATTERN = "saved_pattern"
        const val SAVED_PATTERN_DEFAULT = "NA"
        const val UNLOCK = 1
        const val SETTING_LOCK = 2
        const val VERIFY = 3
        const val UNLOCK_RESUME = 4
    }
}
