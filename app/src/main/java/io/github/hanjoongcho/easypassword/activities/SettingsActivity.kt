package io.github.hanjoongcho.easypassword.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.android.gms.drive.GoogleDriveDownloader
import com.google.android.gms.drive.GoogleDriveUploader
import com.simplemobiletools.commons.dialogs.RadioGroupDialog
import com.simplemobiletools.commons.models.RadioItem
import kotlinx.android.synthetic.main.activity_setting.*
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.helper.*
import io.github.hanjoongcho.easypassword.extensions.config


/**
 * Created by CHO HANJOONG on 2017-11-21.
 */

class SettingsActivity : CommonActivity() {
    lateinit var res: Resources
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        res = resources
        
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            title = getString(R.string.settings)
            setDisplayHomeAsUpEnabled(true)
        }

        initPreference()
        bindEvent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this@SettingsActivity.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        Log.i(IntroActivity.TAG, "SettingsActivity")
        super.onResume()

        setupFontSize()
    }

    private fun setupFontSize() {
        settings_font_size.text = getFontSizeText()
        settings_font_size_holder.setOnClickListener {
            val items = arrayListOf(
                    RadioItem(FONT_SIZE_SMALL, res.getString(R.string.small)),
                    RadioItem(FONT_SIZE_MEDIUM, res.getString(R.string.medium)),
                    RadioItem(FONT_SIZE_LARGE, res.getString(R.string.large)),
                    RadioItem(FONT_SIZE_EXTRA_LARGE, res.getString(R.string.extra_large)))

            RadioGroupDialog(this@SettingsActivity, items, config.fontSize) {
                config.fontSize = it as Int
                settings_font_size.text = getFontSizeText()
//                updateWidget()
            }
        }
    }

    private fun getFontSizeText() = getString(when (config.fontSize) {
        FONT_SIZE_SMALL -> R.string.small
        FONT_SIZE_MEDIUM -> R.string.medium
        FONT_SIZE_LARGE -> R.string.large
        else -> R.string.extra_large
    })
    
    private fun bindEvent() {
        pref1.setOnClickListener {
            val intent = Intent(this, PatternLockActivity::class.java)
            intent.putExtra(PatternLockActivity.MODE, PatternLockActivity.SETTING_LOCK)
            EasyPasswordHelper.startSettingActivityWithTransition(this@SettingsActivity, intent)
        }

        pref2.setOnClickListener {
            EasyPasswordHelper.startSettingActivityWithTransition(this@SettingsActivity, GoogleDriveUploader::class.java)
        }

        pref3.setOnClickListener {
            EasyPasswordHelper.startSettingActivityWithTransition(this@SettingsActivity, GoogleDriveDownloader::class.java)
        }

        pref8.setOnClickListener {
            val uri = Uri.parse("market://details?id=io.github.hanjoongcho.easypassword")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                EasyPasswordHelper.startSettingActivityWithTransition(this@SettingsActivity, goToMarket)
            } catch (e: ActivityNotFoundException) {
                val browser = Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=io.github.hanjoongcho.easypassword"))
                EasyPasswordHelper.startSettingActivityWithTransition(this@SettingsActivity, browser)
            }
        }

        pref9.setOnClickListener {
            EasyPasswordHelper.startSettingActivityWithTransition(this@SettingsActivity, WebViewActivity.getStartIntent(this@SettingsActivity, getString(R.string.setting_license_url)))
        }
    }

    private fun initPreference() {
        prefSummary8.text = "Easy Password v ${getPackageVersion()}"
    }

    private fun getPackageVersion(): String? {

        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return packageInfo?.versionName
    }
}