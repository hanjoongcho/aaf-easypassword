package io.github.hanjoongcho.easypassword.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.models.Account

/**
 * Created by CHO HANJOONG on 2017-11-18.
 */

class AccountDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_detail)
    }

    companion object {

        fun getStartIntent(context: Context, account: Account): Intent {
            return Intent(context, AccountDetailActivity::class.java)
                    .apply { putExtra(Account.TAG, account.sequence) }
        }
    }
}