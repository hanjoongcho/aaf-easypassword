package io.github.hanjoongcho.easypassword.activities

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.databinding.ActivityAccountDetailBinding
import io.github.hanjoongcho.easypassword.models.Account
import io.github.hanjoongcho.easypassword.helper.database

/**
 * Created by CHO HANJOONG on 2017-11-18.
 */

class AccountDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_detail)
        val sequence:Int = intent.getIntExtra("sequence", -1)
        val account: Account = this@AccountDetailActivity.database().selectAccountBy(sequence)
        val binding = DataBindingUtil
                .setContentView<ActivityAccountDetailBinding>(this,
                        R.layout.activity_account_detail)
        binding.accountId.setText(account.id)
        binding.accountPassword.setText(account.password)
        binding.accountSummary.setText(account.summary)
        binding.accountManageTarget.setText(account.title)

        setSupportActionBar(binding.toolbarPlayer)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        fun getStartIntent(context: Context, account: Account): Intent {
            return Intent(context, AccountDetailActivity::class.java)
                    .apply { putExtra(Account.TAG, account.sequence) }
        }
    }
}