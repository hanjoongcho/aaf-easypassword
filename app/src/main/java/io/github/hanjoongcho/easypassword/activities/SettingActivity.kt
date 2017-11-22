package io.github.hanjoongcho.easypassword.activities

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.telecom.Call
import android.view.KeyEvent
import android.view.MenuItem
import com.google.android.gms.drive.GoogleDriveDownloader
import com.google.android.gms.drive.GoogleDriveUploader
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.helper.TransitionHelper
import kotlinx.android.synthetic.main.activity_setting.*
import android.view.KeyEvent.KEYCODE_BACK



/**
 * Created by CHO HANJOONG on 2017-11-21.
 */

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            title = getString(R.string.setting_title)
            setDisplayHomeAsUpEnabled(true)
        }

        initPreference()
        bindEvent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this@SettingActivity.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindEvent() {
        pref1.setOnClickListener {
            val intent = Intent(this, PatternLockActivity::class.java)
            intent.putExtra(PatternLockActivity.MODE, PatternLockActivity.SETTING_LOCK)
            TransitionHelper.startSettingActivityWithTransition(this@SettingActivity, intent)
        }

        pref2.setOnClickListener {
            TransitionHelper.startSettingActivityWithTransition(this@SettingActivity, GoogleDriveUploader::class.java)
        }

        pref3.setOnClickListener {
            TransitionHelper.startSettingActivityWithTransition(this@SettingActivity, GoogleDriveDownloader::class.java)
        }
    }

    private fun initPreference() {
        prefSummary8.text = "Easy Diary v ${getPackageVersion()}"
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