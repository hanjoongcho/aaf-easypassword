package io.github.hanjoongcho.easypassword.activities

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.android.gms.drive.GoogleDriveDownloader
import com.google.android.gms.drive.GoogleDriveUploader
import com.simplemobiletools.commons.dialogs.RadioGroupDialog
import com.simplemobiletools.commons.extensions.baseConfig
import com.simplemobiletools.commons.extensions.isBlackAndWhiteTheme
import com.simplemobiletools.commons.helpers.APP_NAME
import com.simplemobiletools.commons.helpers.APP_VERSION_NAME
import com.simplemobiletools.commons.models.RadioItem
import io.github.hanjoongcho.commons.helpers.TransitionHelper
import io.github.hanjoongcho.easypassword.BuildConfig
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.extensions.config
import io.github.hanjoongcho.easypassword.extensions.initTextSize
import io.github.hanjoongcho.easypassword.helper.*
import kotlinx.android.synthetic.main.activity_settings.*


/**
 * Created by CHO HANJOONG on 2017-11-21.
 */

class SettingsActivity : SimpleActivity() {
    lateinit var res: Resources
    private var linkColor = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        res = resources
        linkColor = if (isBlackAndWhiteTheme()) Color.WHITE else baseConfig.primaryColor
        
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            title = getString(R.string.settings)
            setDisplayHomeAsUpEnabled(true)
        }
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
        
        // handle option click
        setupFontSize()
        setupPatternLock()
        setupGoogleDrive()
        setupAbout()
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
                initTextSize(findViewById(android.R.id.content), this@SettingsActivity);
            }
        }
        settings_font_size_label.setTextColor(linkColor)
    }

    private fun setupPatternLock() {
        pattern_lock_holder.setOnClickListener {
            val intent = Intent(this, PatternLockActivity::class.java)
            intent.putExtra(PatternLockActivity.MODE, PatternLockActivity.SETTING_LOCK)
            TransitionHelper.startActivityWithTransition(this@SettingsActivity, intent)
        }
        pattern_lock_label.setTextColor(linkColor)
    }
    
    private fun setupGoogleDrive() {
        google_drive_backup_holder.setOnClickListener {
            TransitionHelper.startActivityWithTransition(this@SettingsActivity, GoogleDriveUploader::class.java)
        }
        google_drive_backup_label.setTextColor(linkColor)

        google_drive_recovery_holder.setOnClickListener {
            TransitionHelper.startActivityWithTransition(this@SettingsActivity, GoogleDriveDownloader::class.java)
        }
        google_drive_recovery_label.setTextColor(linkColor)
    }

    private fun setupAbout() {
        about_label.setTextColor(linkColor)
        about_holder.setOnClickListener {
            val aboutIntent = Intent(this@SettingsActivity, AboutActivity::class.java).apply {
                putExtra(APP_NAME, getString(R.string.app_name))
                putExtra(APP_VERSION_NAME, BuildConfig.VERSION_NAME)
            }
            TransitionHelper.startActivityWithTransition(this@SettingsActivity, aboutIntent)
        }
    }
    
    private fun getFontSizeText() = getString(when (config.fontSize) {
        FONT_SIZE_SMALL -> R.string.small
        FONT_SIZE_MEDIUM -> R.string.medium
        FONT_SIZE_LARGE -> R.string.large
        else -> R.string.extra_large
    })
}