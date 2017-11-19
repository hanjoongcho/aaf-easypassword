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
import io.github.hanjoongcho.easypassword.databinding.ActivityAccountAddBinding
import io.github.hanjoongcho.easypassword.helper.database
import io.github.hanjoongcho.easypassword.models.Account

/**
 * Created by CHO HANJOONG on 2017-11-18.
 */

class AccountAddActivity : AppCompatActivity() {

    private var mBinding: ActivityAccountAddBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_add)

        mBinding = DataBindingUtil.setContentView<ActivityAccountAddBinding>(this,
                R.layout.activity_account_add)

        setSupportActionBar(mBinding?.toolbarPlayer)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }

        mBinding?.save?.setOnClickListener(View.OnClickListener { _ ->
            val account: Account = Account(
                    mBinding?.accountManageTarget?.text.toString(),
                    mBinding?.accountSummary?.text.toString(),
                    "web",
                    mBinding?.accountId?.text.toString(),
                    mBinding?.accountPassword?.text.toString(),
                    4
            )
            this@AccountAddActivity.database().insertAccount(account)
            finish()
        })

        initCategorySpinner()
    }

    private fun initCategorySpinner() {
        val listCategory: List<String> = mutableListOf(
                Account.CATEGORY_CREDIT_CARD,
                Account.CATEGORY_HOME,
                Account.CATEGORY_WEB
        )
        val adapter: ArrayAdapter<String> = AccountCategoryAdapter(this@AccountAddActivity, R.layout.item_category, listCategory)
        mBinding?.accountManageCategory?.adapter = adapter
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
        fun getStartIntent(context: Context): Intent = Intent(context, AccountAddActivity::class.java)
    }
}