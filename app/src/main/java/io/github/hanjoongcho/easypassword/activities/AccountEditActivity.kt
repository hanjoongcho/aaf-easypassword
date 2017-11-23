package io.github.hanjoongcho.easypassword.activities

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.adpaters.AccountCategoryAdapter
import io.github.hanjoongcho.easypassword.databinding.ActivityAccountEditBinding
import io.github.hanjoongcho.easypassword.helper.database
import io.github.hanjoongcho.easypassword.models.*
import io.github.hanjoongcho.utils.AesUtils
import io.github.hanjoongcho.utils.PasswordStrengthUtils

/**
 * Created by CHO HANJOONG on 2017-11-18.
 */

class AccountEditActivity : AppCompatActivity() {

    private var mBinding: ActivityAccountEditBinding? = null
    private var mSecurity: Security? = null
    private var mSequence:Int = -1
    private var mTempStrengthLevel = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_edit)
        mSequence = intent.getIntExtra(Security.PRIMARY_KEY, -1)
        mSecurity = this@AccountEditActivity.database().selectSecurityBy(mSequence)


        mBinding = DataBindingUtil.setContentView<ActivityAccountEditBinding>(this,
                R.layout.activity_account_edit)

        setSupportActionBar(mBinding?.toolbarPlayer)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }

        bindEvent()
        initCategorySpinner()

        val encryptedPassword = mSecurity?.securityItem?.password

        Thread({
            val decryptedPassword = AesUtils.decryptPassword(this@AccountEditActivity, encryptedPassword!!)
            Handler(Looper.getMainLooper()).post {
                mBinding?.accountPassword?.setText(decryptedPassword)
                mBinding?.loadingProgress?.visibility = View.INVISIBLE
            }
        }).start()

        mSecurity?.let { security ->
            mTempStrengthLevel = security.securityItem?.passwordStrengthLevel ?: 1
            mBinding?.let { binding ->
                security?.securityItem?.let { item ->
                    if (item is Account) {
                        binding.accountId.setText(item.id)
                        binding.accountSummary.setText(item.summary)
                        binding.accountManageTarget.setText(item.title)
                        AccountAddActivity.setPasswordStrengthLevel(this@AccountEditActivity, item.passwordStrengthLevel, binding.included.level1, binding.included.level2, binding.included.level3, binding.included.level4, binding.included.level5)
                    }
                }

            }
        }
    }

    private fun bindEvent() {

        mBinding?.let { binding ->
            binding.save.setOnClickListener(View.OnClickListener { _ ->
                val item: Category = binding.accountManageCategory.selectedItem as Category
                val securityItem: SecurityItem = when (item.index) {
                    0 -> {
                        Account(
                                binding.accountManageTarget.text.toString(),
                                binding.accountId.text.toString(),
                                binding.accountPassword.text.toString(),
                                mTempStrengthLevel,
                                binding.accountSummary.text.toString()
                        )
                    }
                    else -> {
                        Account(
                                binding.accountManageTarget.text.toString(),
                                binding.accountId.text.toString(),
                                binding.accountPassword.text.toString(),
                                mTempStrengthLevel,
                                binding.accountSummary.text.toString()
                        )
                    }
                }

                val security = Security(
                        null,
                        binding.accountManageCategory.selectedItem as Category,
                        securityItem
                )

//                val account: Account = Account(
//                        binding.accountManageTarget?.text.toString(),
//                        binding.accountSummary?.text.toString(),
//                        binding.accountManageCategory?.selectedItem as Category,
//                        binding.accountId?.text.toString(),
//                        binding.accountPassword?.text.toString(),
//                        mTempStrengthLevel,
//                        mAccount?.sequence!!
//                )
                this@AccountEditActivity.database().updateSecurity(security)
                this@AccountEditActivity.onBackPressed()
//                finish()
            })

//            binding.accountPassword.setOnFocusChangeListener({ _, hasFocus ->
//                if (hasFocus) {
//                    binding.scrollView.post {
//                        binding.scrollView.scrollTo(0, binding.accountPassword.bottom);
//                    }
//                }
//            })

            binding.accountPassword.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val level = PasswordStrengthUtils.getScore(s.toString())
                    if (level != mTempStrengthLevel) {
                        mTempStrengthLevel = level
                        AccountAddActivity.setPasswordStrengthLevel(this@AccountEditActivity, mTempStrengthLevel, binding.included.level1, binding.included.level2, binding.included.level3, binding.included.level4, binding.included.level5)
                    }
                    Log.i(TAG, level.toString())
                }
            })
        }
    }

    private fun initCategorySpinner() {
        val adapter: ArrayAdapter<Category> = AccountCategoryAdapter(this@AccountEditActivity, R.layout.item_category, AccountAddActivity.listCategory)
        mBinding?.accountManageCategory?.adapter = adapter
        mBinding?.accountManageCategory?.setSelection(mSecurity?.category?.index ?: 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> this@AccountEditActivity.onBackPressed()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.account_detail, menu)
//        return true
//    }

    companion object {
        const val TAG = "AccountEditActivity"

        fun getStartIntent(context: Context, sequence: Int): Intent {
            return Intent(context, AccountEditActivity::class.java)
                    .apply { putExtra(Security.PRIMARY_KEY, sequence) }
        }
    }
}