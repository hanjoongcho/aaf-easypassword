package io.github.hanjoongcho.easypassword.activities

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.databinding.ActivitySecuritySelectionBinding
import io.github.hanjoongcho.easypassword.fragment.SecuritySelectionFragment
import io.github.hanjoongcho.easypassword.helper.TransitionHelper
import io.github.hanjoongcho.easypassword.helper.findFragmentById
import io.github.hanjoongcho.easypassword.helper.replaceFragment

/**
 * Created by Administrator on 2017-11-15.
 */

class SecuritySelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil
                .setContentView<ActivitySecuritySelectionBinding>(this,
                        R.layout.activity_security_selection)

        setUpToolbar()

        if (savedInstanceState == null) {
            attachCategoryGridFragment()
        } else {
            setProgressBarVisibility(View.GONE)
        }
        supportPostponeEnterTransition()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.security_selection, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                TransitionHelper.startSettingActivityWithTransition(this@SecuritySelectionActivity, SettingActivity::class.java)
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpToolbar() {
        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar_player))
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun attachCategoryGridFragment() {
        replaceFragment(R.id.category_container,
                findFragmentById(R.id.category_container) ?: SecuritySelectionFragment())
        setProgressBarVisibility(View.GONE)
    }

    private fun setProgressBarVisibility(visibility: Int) {
        findViewById<View>(R.id.progress).visibility = visibility
    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private fun startSettingActivityWithTransition() {
//
//        val animationBundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this@SecuritySelectionActivity,
//                *TransitionHelper.createSafeTransitionParticipants(this@SecuritySelectionActivity,
//                        false)).toBundle()
//
//        // Start the activity with the participants, animating from one to the other.
//        val intent = Intent(this, SettingActivity::class.java)
//        ActivityCompat.startActivity(this@SecuritySelectionActivity, intent, animationBundle)
//    }

    companion object {
        fun start(activity: Activity) {
            ActivityCompat.startActivity(activity,
                    Intent(activity, SecuritySelectionActivity::class.java),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle())
        }
    }

}