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
import io.github.hanjoongcho.easypassword.models.Category

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
                    mBinding?.accountManageCategory?.selectedItem as Category,
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
        val adapter: ArrayAdapter<Category> = AccountCategoryAdapter(this@AccountAddActivity, R.layout.item_category, listCategory)
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
        val listCategory: List<Category> = mutableListOf(
                Category(0, "웹사이트", "web"),
                Category(1, "신용카드", "credit_card"),
                Category(2, "도어락", "home"),
                Category(3, "자물쇠", "lock"),
                Category(4, "전자문서", "folder"),
                Category(5, "미분류", "password")
        )
        val listDummyAccount: List<Account> = mutableListOf(
                Account("Google", "https://www.google.com", Category(0, "웹사이트", "web"), "entertainment", "123", 4),
                Account("GitHub", "https://github.com/", Category(0, "웹사이트", "web"), "geography", "1234", 4),
                Account("네이버", "https://www.naver.com/", Category(0, "웹사이트", "web"), "food", "1234", 2),
                Account("카카오뱅크", "카카오뱅크 체크카드", Category(1, "신용카드", "credit_card"), "geography", "1234", 3),
                Account("회사", "회사현관 출입번호", Category(2, "도어락", "home"), "geography", "1234", 1)
        )
    }
}