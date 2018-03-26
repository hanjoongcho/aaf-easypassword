package io.github.hanjoongcho.easypassword.activities

import android.content.Intent
import android.view.ViewGroup
import com.simplemobiletools.commons.helpers.APP_VERSION_NAME
import com.simplemobiletools.commons.views.MyTextView
import io.github.hanjoongcho.commons.activities.BaseAboutActivity
import io.github.hanjoongcho.commons.activities.BaseWebViewActivity
import io.github.hanjoongcho.commons.extensions.updateTextColors
import io.github.hanjoongcho.commons.helpers.TransitionHelper
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.extensions.initTextSize
import io.github.hanjoongcho.easypassword.extensions.pausePatternLock
import io.github.hanjoongcho.easypassword.extensions.resumePatternLock
import io.github.hanjoongcho.easypassword.helper.APP_BACKGROUND_ALPHA
import java.util.*

/**
 * Created by CHO HANJOONG on 2017-11-25.
 * This code based 'Simple-Commons' package
 * You can see original 'Simple-Commons' from below link.
 * https://github.com/SimpleMobileTools/Simple-Commons
 */

class AboutActivity : BaseAboutActivity() {

    override fun onPause() {
        super.onPause()
        pausePatternLock()
    }
    
    override fun onResume() {
        isBackgroundColorFromPrimaryColor = true
        resumePatternLock()
        super.onResume()
//        updateTextColors(about_holder)
//
        setupWebsite()
        setupEmail()
        setupMoreApps()
        setupRateUs()
        setupInvite()
        setupLicense()
//        setupDonate()
//        setupFacebook()
//        setupGPlus()
        setupCopyright()
        initTextSize(findViewById(R.id.main_holder), this@AboutActivity)
    }

    override fun setupLicense() {
        val aboutLicense: MyTextView = findViewById<MyTextView>(io.github.hanjoongcho.commons.R.id.about_license)
        aboutLicense.setOnClickListener {
            val licenseIntent = Intent(this, WebViewActivity::class.java)
            licenseIntent.putExtra(BaseWebViewActivity.OPEN_URL_INFO, getString(R.string.aaf_license_url))
            TransitionHelper.startActivityWithTransition(
                    this@AboutActivity,
                    licenseIntent
            )
        }
        aboutLicense.setTextColor(linkColor)
    }
    
    override fun setupCopyright() {
        val aboutCopyright: MyTextView = findViewById<MyTextView>(io.github.hanjoongcho.commons.R.id.about_copyright)
        val versionName = intent.getStringExtra(APP_VERSION_NAME) ?: ""
        val year = Calendar.getInstance().get(Calendar.YEAR)
        aboutCopyright.text = String.format(getString(R.string.aaf_copyright), versionName, year)
    }

    override fun getMainViewGroup(): ViewGroup? = findViewById(R.id.main_holder)
    override fun getBackgroundAlpha(): Int = APP_BACKGROUND_ALPHA
}