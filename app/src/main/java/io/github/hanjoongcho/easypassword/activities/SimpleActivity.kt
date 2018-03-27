package io.github.hanjoongcho.easypassword.activities

import android.view.ViewGroup
import io.github.hanjoongcho.commons.activities.BaseSimpleActivity
import io.github.hanjoongcho.commons.extensions.updateAppViews
import io.github.hanjoongcho.commons.extensions.updateTextColors
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.extensions.initTextSize
import io.github.hanjoongcho.easypassword.extensions.pausePatternLock
import io.github.hanjoongcho.easypassword.extensions.resumePatternLock
import io.github.hanjoongcho.easypassword.helper.APP_BACKGROUND_ALPHA

/**
 * Created by Administrator on 2017-12-29.
 */

open class SimpleActivity : BaseSimpleActivity() {
    override fun onPause() {
        super.onPause()
        pausePatternLock()
    }

    override fun onResume() {
        isBackgroundColorFromPrimaryColor = true
        super.onResume()
        resumePatternLock()
        initTextSize(findViewById(android.R.id.content), this@SimpleActivity);
        getMainViewGroup()?.let {
            updateTextColors(it)
            updateAppViews(it)    
        }
    }

    override fun getMainViewGroup(): ViewGroup? = findViewById(R.id.main_holder)
    override fun getBackgroundAlpha(): Int = APP_BACKGROUND_ALPHA
}