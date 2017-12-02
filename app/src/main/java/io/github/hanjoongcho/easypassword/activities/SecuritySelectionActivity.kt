package io.github.hanjoongcho.easypassword.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.databinding.ActivitySecuritySelectionBinding
import io.github.hanjoongcho.easypassword.fragment.SecuritySelectionFragment
import io.github.hanjoongcho.easypassword.helper.EasyPasswordHelper
import io.github.hanjoongcho.easypassword.helper.findFragmentById
import io.github.hanjoongcho.easypassword.helper.replaceFragment
import kotlinx.android.synthetic.main.activity_security_selection.*
import android.widget.EditText



/**
 * Created by Administrator on 2017-11-15.
 */

class SecuritySelectionActivity : CommonActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        DataBindingUtil
                .setContentView<ActivitySecuritySelectionBinding>(this,
                        R.layout.activity_security_selection)

        setSupportActionBar(toolbar)
        supportActionBar?.run {
            title = getString(R.string.security_selection_title)
        }

        attachCategoryGridFragment()
        supportPostponeEnterTransition()

        toggleToolBar.setOnClickListener({
            toolbar.visibility = View.VISIBLE
            searchViewContainer.visibility = View.GONE
            val focusView = this.currentFocus
            if (focusView != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(focusView.windowToken, 0)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.security_selection, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.setting -> {
                EasyPasswordHelper.startSettingActivityWithTransition(this@SecuritySelectionActivity, SettingActivity::class.java)
            }
            R.id.search -> {
                toolbar.visibility = View.GONE
                searchViewContainer.visibility = View.VISIBLE
                searchView.requestFocus()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT)
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        Log.i(IntroActivity.TAG, "SecuritySelectionActivity")
        super.onResume()
    }

    private fun attachCategoryGridFragment() {
        replaceFragment(R.id.category_container,
                findFragmentById(R.id.category_container) ?: SecuritySelectionFragment())
    }

    companion object {

        fun start(activity: Activity) {
            ActivityCompat.startActivity(activity,
                    Intent(activity, SecuritySelectionActivity::class.java),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle())
        }
    }
}