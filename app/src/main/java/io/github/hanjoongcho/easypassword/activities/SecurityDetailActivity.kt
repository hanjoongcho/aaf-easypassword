package io.github.hanjoongcho.easypassword.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.adpaters.SecurityCategoryAdapter
import io.github.hanjoongcho.easypassword.databinding.ActivitySecurityDetailBinding
import io.github.hanjoongcho.easypassword.helper.EasyPasswordHelper
import io.github.hanjoongcho.easypassword.helper.database
import io.github.hanjoongcho.easypassword.models.Category
import io.github.hanjoongcho.easypassword.models.Security
import io.github.hanjoongcho.utils.AesUtils

/**
 * Created by CHO HANJOONG on 2017-11-18.
 */

class SecurityDetailActivity : CommonActivity() {

    private var mBinding: ActivitySecurityDetailBinding? = null
    private var mSecurity: Security? = null
    private var mSequence:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security_detail)
        mSequence = intent.getIntExtra(Security.PRIMARY_KEY, -1)


        mBinding = DataBindingUtil
                .setContentView<ActivitySecurityDetailBinding>(this,
                        R.layout.activity_security_detail)

        setSupportActionBar(mBinding?.toolbarPlayer)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }

        bindEvent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home ->  this@SecurityDetailActivity.onBackPressed()
            R.id.delete -> {
                val confirmBuilder: AlertDialog.Builder = AlertDialog.Builder(this@SecurityDetailActivity).apply {
                    setMessage(getString(R.string.security_item_delete_confirm))
                    setPositiveButton("OK", DialogInterface.OnClickListener({ _, _ ->
                        this@SecurityDetailActivity.database().deleteSecurityBy(mSequence);
                        this@SecurityDetailActivity.onBackPressed()
                    }))
                    setNegativeButton("CANCEL", null)
                    setCancelable(false)
                }
                confirmBuilder.create().show()
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        refreshItem()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.security_detail, menu)
        return true
    }

    private fun refreshItem() {

        mSecurity = this@SecurityDetailActivity.database().selectSecurityBy(mSequence)
        mBinding?.decryptPassword?.visibility = View.VISIBLE
        initCategorySpinner()
        changeCategoryContainer()
        EasyPasswordHelper.activityAccountDetailBinding(this@SecurityDetailActivity, mBinding, mSecurity)
    }

    private fun bindEvent() {

        mBinding?.run {
            update?.setOnClickListener(View.OnClickListener { _ ->
                EasyPasswordHelper.startSettingActivityWithTransition(this@SecurityDetailActivity, SecurityEditActivity.getStartIntent(this@SecurityDetailActivity, mSequence))
            })

            decryptPassword?.setOnClickListener { view ->
                view.visibility = View.INVISIBLE
                decProgress.visibility = View.VISIBLE
                Thread(Runnable {
                    val decryptedPassword = AesUtils.decryptPassword(this@SecurityDetailActivity, securityPassword.text.toString())
                    Handler(Looper.getMainLooper()).post(Runnable {
                        securityPassword.text = decryptedPassword
                        decProgress.visibility = View.INVISIBLE
                    })
                }).start()
            }
        }
    }

    private fun changeCategoryContainer() {

        mBinding?.run {
            val item: Category = securityCategory.selectedItem as Category
            when (item.index) {
                0 -> {
                    accountContainer.visibility = View.VISIBLE
                    creditCardContainer.visibility = View.GONE
                }
                1 -> {
                    accountContainer.visibility = View.GONE
                    creditCardContainer.visibility = View.VISIBLE
                }
                else -> {
                    accountContainer.visibility = View.GONE
                    creditCardContainer.visibility = View.GONE
                }
            }
        }
    }

    private fun initCategorySpinner() {

        val adapter: ArrayAdapter<Category> = SecurityCategoryAdapter(this@SecurityDetailActivity, R.layout.item_category, EasyPasswordHelper.listCategory)
        mBinding?.run {
            securityCategory.adapter = adapter
            securityCategory.setSelection(mSecurity?.category?.index ?: 0)
            securityCategory.isEnabled = false
        }
    }

    companion object {

        fun getStartIntent(context: Context, security: Security): Intent {
            return Intent(context, SecurityDetailActivity::class.java)
                    .apply { putExtra(Security.PRIMARY_KEY, security.sequence) }
        }
    }
}