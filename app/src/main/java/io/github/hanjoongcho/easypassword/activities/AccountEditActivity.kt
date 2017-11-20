package io.github.hanjoongcho.easypassword.activities

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.adpaters.AccountCategoryAdapter
import io.github.hanjoongcho.easypassword.databinding.ActivityAccountEditBinding
import io.github.hanjoongcho.easypassword.helper.database
import io.github.hanjoongcho.easypassword.models.Account
import io.github.hanjoongcho.easypassword.models.Category

/**
 * Created by CHO HANJOONG on 2017-11-18.
 */

class AccountEditActivity : AppCompatActivity() {

    private var mBinding: ActivityAccountEditBinding? = null
    private var mAccount: Account? = null
    private var mSequence:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_edit)
        mSequence = intent.getIntExtra(Account.SEQUENCE, -1)
        mAccount = this@AccountEditActivity.database().selectAccountBy(mSequence)

        mBinding = DataBindingUtil.setContentView<ActivityAccountEditBinding>(this,
                R.layout.activity_account_edit)

        mAccount?.let { account ->
            mBinding?.let { binding ->
                binding.accountId.setText(account.id)
                binding.accountPassword.setText(account.password)
                binding.accountSummary.setText(account.summary)
                binding.accountManageTarget.setText(account.title)
                AccountAddActivity.setPasswordStrengthLevel(this@AccountEditActivity, account, binding.included.level1, binding.included.level2, binding.included.level3, binding.included.level4, binding.included.level5)
            }
        }

        setSupportActionBar(mBinding?.toolbarPlayer)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }

        mBinding?.save?.setOnClickListener(View.OnClickListener { _ ->
            val account: Account = Account(
                    mBinding?.accountManageTarget?.text.toString(),
                    mBinding?.accountSummary?.text.toString(),
                    mBinding?.accountManageCategory?.selectedItem as Category,
                    mBinding?.accountId?.text.toString(),
                    mBinding?.accountPassword?.text.toString(),
                    4,
                    mAccount?.sequence!!
            )
            this@AccountEditActivity.database().updateAccount(account)
            finish()
        })
        initCategorySpinner()
    }

    private fun initCategorySpinner() {
        val adapter: ArrayAdapter<Category> = AccountCategoryAdapter(this@AccountEditActivity, R.layout.item_category, AccountAddActivity.listCategory)
        mBinding?.accountManageCategory?.adapter = adapter
        mBinding?.accountManageCategory?.setSelection(mAccount?.category?.index ?: 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
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
        fun getStartIntent(context: Context, sequence: Int): Intent {
            return Intent(context, AccountEditActivity::class.java)
                    .apply { putExtra(Account.SEQUENCE, sequence) }
        }
    }
}