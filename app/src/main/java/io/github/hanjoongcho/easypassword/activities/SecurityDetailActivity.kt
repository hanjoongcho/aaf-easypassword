package io.github.hanjoongcho.easypassword.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import io.github.hanjoongcho.utils.PasswordStrengthUtils
import kotlinx.android.synthetic.main.activity_setting.*

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

        setSupportActionBar(toolbar)
        supportActionBar?.run {
            title = getString(R.string.security_detail_title)
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
            R.id.decrypt_field -> {
                decryptField()
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

    private fun decryptField() {
        mBinding?.loadingProgress?.visibility = View.VISIBLE
        val password = mSecurity?.password ?: ""
        val serial = mSecurity?.creditCard?.serial ?: ""
        val expireDate = mSecurity?.creditCard?.expireDate ?: ""
        val cardValidationCode = mSecurity?.creditCard?.cardValidationCode ?: ""

        Thread({

            val decryptedPassword = AesUtils.decryptPassword(this@SecurityDetailActivity, password)
            val decryptedSerial = AesUtils.decryptPassword(this@SecurityDetailActivity, serial)
            val decryptedExpireDate = AesUtils.decryptPassword(this@SecurityDetailActivity, expireDate)
            val decryptedCardValidationCode = AesUtils.decryptPassword(this@SecurityDetailActivity, cardValidationCode)

            Handler(Looper.getMainLooper()).post {
                mBinding?.run{
                    securityPassword?.text = decryptedPassword
                    creditCardSerial?.text = decryptedSerial
                    creditCardExpireDate?.text = decryptedExpireDate
                    creditCardCvc?.text = decryptedCardValidationCode
                    loadingProgress?.visibility = View.INVISIBLE
                }
            }
        }).start()
    }

    private fun refreshItem() {

        mSecurity = this@SecurityDetailActivity.database().selectSecurityBy(mSequence)
        initCategorySpinner()
        changeCategoryContainer()
        EasyPasswordHelper.activityAccountDetailBinding(this@SecurityDetailActivity, mBinding, mSecurity)
    }

    private fun bindEvent() {

        mBinding?.run {
            update?.setOnClickListener(View.OnClickListener { _ ->
                EasyPasswordHelper.startSettingActivityWithTransition(this@SecurityDetailActivity, SecurityEditActivity.getStartIntent(this@SecurityDetailActivity, mSequence))
            })
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