package io.github.hanjoongcho.easypassword.activities

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.adpaters.AccountCategoryAdapter
import io.github.hanjoongcho.easypassword.databinding.ActivityAccountDetailBinding
import io.github.hanjoongcho.easypassword.helper.database
import io.github.hanjoongcho.easypassword.models.Account
import io.github.hanjoongcho.easypassword.models.Category
import io.github.hanjoongcho.utils.AesUtils
import kotlinx.android.synthetic.main.activity_account_detail.view.*

/**
 * Created by CHO HANJOONG on 2017-11-18.
 */

class AccountDetailActivity : AppCompatActivity() {

    private var mBinding: ActivityAccountDetailBinding? = null
    private var mAccount: Account? = null
    private var mSequence:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_detail)
        mSequence = intent.getIntExtra("sequence", -1)


        mBinding = DataBindingUtil
                .setContentView<ActivityAccountDetailBinding>(this,
                        R.layout.activity_account_detail)

        setSupportActionBar(mBinding?.toolbarPlayer)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }

        mBinding?.update?.setOnClickListener(View.OnClickListener { _ ->
            startActivity(AccountEditActivity.getStartIntent(this@AccountDetailActivity, mSequence))
//            finish()
        })

        mBinding?.let { binding ->
            binding.decryptPassword?.setOnClickListener { view ->
                view.visibility = View.INVISIBLE
                binding.decProgress.visibility = View.VISIBLE
                Thread(Runnable {
                    val decryptedPassword = AesUtils.decryptPassword(this@AccountDetailActivity, binding.accountPassword.text.toString())
                    Handler(Looper.getMainLooper()).post(Runnable {
                        binding.accountPassword.text = decryptedPassword
                        binding.decProgress.visibility = View.INVISIBLE
                    })
                }).start()
            }
        }
    }

    private fun initCategorySpinner() {
        val adapter: ArrayAdapter<Category> = AccountCategoryAdapter(this@AccountDetailActivity, R.layout.item_category, AccountAddActivity.listCategory)
        mBinding?.accountManageCategory?.adapter = adapter
        mBinding?.accountManageCategory?.setSelection(mAccount?.category?.index ?: 0)
        mBinding?.accountManageCategory?.isEnabled = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->  this@AccountDetailActivity.onBackPressed()
            R.id.edit -> {
            }
            R.id.save -> {
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.account_detail, menu)
//        return true
//    }

    private fun refreshItem() {
        mAccount = this@AccountDetailActivity.database().selectAccountBy(mSequence)
        mAccount?.let { account ->
            mBinding?.accountId?.text = account.id
            mBinding?.accountPassword?.text = account.password
            mBinding?.accountSummary?.text = account.summary
            mBinding?.accountManageTarget?.text = account.title
            initCategorySpinner()
            mBinding?.let { binding ->
                AccountAddActivity.setPasswordStrengthLevel(this@AccountDetailActivity, account, binding.included.level1, binding.included.level2, binding.included.level3, binding.included.level4, binding.included.level5)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshItem()
    }

    companion object {
        fun getStartIntent(context: Context, account: Account): Intent {
            return Intent(context, AccountDetailActivity::class.java)
                    .apply { putExtra(Account.SEQUENCE, account.sequence) }
        }
    }
}