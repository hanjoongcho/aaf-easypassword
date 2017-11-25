package io.github.hanjoongcho.easypassword.activities

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.adpaters.SecurityCategoryAdapter
import io.github.hanjoongcho.easypassword.databinding.ActivitySecurityEditBinding
import io.github.hanjoongcho.easypassword.helper.EasyPasswordHelper
import io.github.hanjoongcho.easypassword.helper.database
import io.github.hanjoongcho.easypassword.models.Category
import io.github.hanjoongcho.easypassword.models.Security
import io.github.hanjoongcho.utils.AesUtils
import io.github.hanjoongcho.utils.PasswordStrengthUtils

/**
 * Created by CHO HANJOONG on 2017-11-18.
 */

class SecurityEditActivity : CommonActivity() {

    private var mBinding: ActivitySecurityEditBinding? = null
    private var mSecurity: Security? = null
    private var mSequence:Int = -1
    private var mTempStrengthLevel = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security_edit)
        mSequence = intent.getIntExtra(Security.PRIMARY_KEY, -1)
        mSecurity = this@SecurityEditActivity.database().selectSecurityBy(mSequence)
        mTempStrengthLevel = mSecurity?.passwordStrengthLevel ?: 1

        mBinding = DataBindingUtil.setContentView<ActivitySecurityEditBinding>(this,
                R.layout.activity_security_edit)

        setSupportActionBar(mBinding?.toolbarPlayer)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }

        bindEvent()
        initCategorySpinner()
        changeCategoryContainer()
        decryptField()
        EasyPasswordHelper.activityAccountEditBinding(this@SecurityEditActivity, mBinding, mSecurity)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> this@SecurityEditActivity.onBackPressed()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.security_detail, menu)
//        return true
//    }

    private fun bindEvent() {

        mBinding?.let { binding ->
            binding.save.setOnClickListener(View.OnClickListener { _ ->
                val security: Security? = EasyPasswordHelper.getSecurityFromLayout(mBinding, binding.securityCategory.selectedItem as Category, mTempStrengthLevel)
                security?.let {
                    it.sequence = mSequence
                    this@SecurityEditActivity.database().updateSecurity(it)
                    this@SecurityEditActivity.onBackPressed()
                }
            })

//            binding.accountPassword.setOnFocusChangeListener({ _, hasFocus ->
//                if (hasFocus) {
//                    binding.scrollView.post {
//                        binding.scrollView.scrollTo(0, binding.accountPassword.bottom);
//                    }
//                }
//            })
        }
    }

    private fun decryptField() {

        val encryptedPassword = mSecurity?.password
        Thread({
            val decryptedPassword = AesUtils.decryptPassword(this@SecurityEditActivity, encryptedPassword!!)
            Handler(Looper.getMainLooper()).post {
                mBinding?.let { binding ->
                    binding.securityPassword?.setText(decryptedPassword)
                    binding.loadingProgress?.visibility = View.INVISIBLE
                    binding.securityPassword.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            val level = PasswordStrengthUtils.getScore(s.toString())
                            if (level != mTempStrengthLevel) {
                                mTempStrengthLevel = level
                                EasyPasswordHelper.setPasswordStrengthLevel(this@SecurityEditActivity, mTempStrengthLevel, binding.included.level1, binding.included.level2, binding.included.level3, binding.included.level4, binding.included.level5)
                            }
                            Log.i(TAG, level.toString())
                        }
                    })
                }
            }
        }).start()
    }

    private fun initCategorySpinner() {

        val adapter: ArrayAdapter<Category> = SecurityCategoryAdapter(this@SecurityEditActivity, R.layout.item_category, EasyPasswordHelper.listCategory)
        mBinding?.run {
            securityCategory.adapter = adapter
            securityCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    changeCategoryContainer()
                }
            }
            securityCategory?.setSelection(mSecurity?.category?.index ?: 0)
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

    companion object {

        const val TAG = "SecurityEditActivity"

        fun getStartIntent(context: Context, sequence: Int): Intent {
            return Intent(context, SecurityEditActivity::class.java)
                    .apply { putExtra(Security.PRIMARY_KEY, sequence) }
        }
    }
}