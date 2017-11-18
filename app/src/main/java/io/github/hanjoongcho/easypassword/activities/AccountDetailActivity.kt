package io.github.hanjoongcho.easypassword.activities

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.databinding.ActivityAccountDetailBinding
import io.github.hanjoongcho.easypassword.models.Account
import io.github.hanjoongcho.easypassword.helper.database

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
        mAccount = this@AccountDetailActivity.database().selectAccountBy(mSequence)

        mBinding = DataBindingUtil
                .setContentView<ActivityAccountDetailBinding>(this,
                        R.layout.activity_account_detail)

        mBinding?.accountId?.setText(mAccount?.id)
        mBinding?.accountPassword?.setText(mAccount?.password)
        mBinding?.accountSummary?.setText(mAccount?.summary)
        mBinding?.accountManageTarget?.setText(mAccount?.title)

        setSupportActionBar(mBinding?.toolbarPlayer)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.edit -> {
            }
            R.id.save -> {
                val account: Account = Account(
                        mBinding?.accountManageTarget?.text.toString(),
                        mBinding?.accountSummary?.text.toString(),
                        "web",
                        mBinding?.accountId?.text.toString(),
                        mBinding?.accountPassword?.text.toString(),
                        4,
                        mAccount?.sequence!!
                )
                this@AccountDetailActivity.database().updateAccount(account)
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.account_detail, menu)
        return true
    }

    companion object {

        fun getStartIntent(context: Context, account: Account): Intent {
            return Intent(context, AccountDetailActivity::class.java)
                    .apply { putExtra(Account.TAG, account.sequence) }
        }
    }
}