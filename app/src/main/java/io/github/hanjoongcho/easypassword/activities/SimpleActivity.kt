package io.github.hanjoongcho.easypassword.activities

import android.view.ViewGroup
import io.github.hanjoongcho.commons.activities.BaseSimpleActivity
import io.github.hanjoongcho.commons.extensions.updateTextColors
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.extensions.initTextSize
import io.github.hanjoongcho.easypassword.extensions.pausePatternLock
import io.github.hanjoongcho.easypassword.extensions.resumePatternLock

/**
 * Created by Administrator on 2017-12-29.
 */

open class SimpleActivity : BaseSimpleActivity() {
    override fun onPause() {
        super.onPause()
        pausePatternLock()
    }

    override fun onResume() {
        super.onResume()
        resumePatternLock()
        initTextSize(findViewById(android.R.id.content), this@SimpleActivity);
        updateTextColors(findViewById(android.R.id.content))
    }

    override fun getMainViewGroup(): ViewGroup? = findViewById(R.id.main_holder)
}