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
import io.github.hanjoongcho.easypassword.helper.SecurityItemBindingHelper
import io.github.hanjoongcho.easypassword.helper.TransitionHelper
import io.github.hanjoongcho.easypassword.helper.database
import io.github.hanjoongcho.easypassword.models.Account
import io.github.hanjoongcho.easypassword.models.Category
import io.github.hanjoongcho.easypassword.models.Security
import io.github.hanjoongcho.utils.AesUtils
import kotlinx.android.synthetic.main.activity_account_detail.view.*

/**
 * Created by CHO HANJOONG on 2017-11-18.
 */

class AccountDetailActivity : AppCompatActivity() {

    private var mBinding: ActivityAccountDetailBinding? = null
    private var mSecurity: Security? = null
    private var mSequence:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_detail)
        mSequence = intent.getIntExtra(Security.PRIMARY_KEY, -1)


        mBinding = DataBindingUtil
                .setContentView<ActivityAccountDetailBinding>(this,
                        R.layout.activity_account_detail)

        setSupportActionBar(mBinding?.toolbarPlayer)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }

        mBinding?.update?.setOnClickListener(View.OnClickListener { _ ->
            startActivity(AccountEditActivity.getStartIntent(this@AccountDetailActivity, mSequence))
//            TransitionHelper.startSettingActivityWithTransition(this@AccountDetailActivity, AccountEditActivity.getStartIntent(this@AccountDetailActivity, mSequence))
        })

        mBinding?.let { binding ->
            binding.decryptPassword?.setOnClickListener { view ->
                view.visibility = View.INVISIBLE
                binding.decProgress.visibility = View.VISIBLE
                Thread(Runnable {
                    val decryptedPassword = AesUtils.decryptPassword(this@AccountDetailActivity, binding.securityPassword.text.toString())
                    Handler(Looper.getMainLooper()).post(Runnable {
                        binding.securityPassword.text = decryptedPassword
                        binding.decProgress.visibility = View.INVISIBLE
                    })
                }).start()
            }
        }

        changeCategoryContainer()
    }

    private fun initCategorySpinner() {
        val adapter: ArrayAdapter<Category> = AccountCategoryAdapter(this@AccountDetailActivity, R.layout.item_category, AccountAddActivity.listCategory)
        mBinding?.securityCategory?.adapter = adapter
        mBinding?.securityCategory?.setSelection(mSecurity?.category?.index ?: 0)
        mBinding?.securityCategory?.isEnabled = false
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
        mSecurity = this@AccountDetailActivity.database().selectSecurityBy(mSequence)
        SecurityItemBindingHelper.activityAccountDetailBinding(this@AccountDetailActivity, mBinding, mSecurity)
        initCategorySpinner()
    }

    override fun onResume() {
        super.onResume()
        refreshItem()
    }

    private fun changeCategoryContainer() {
        mBinding?.let { binding ->
            val item: Category = binding.securityCategory.selectedItem as Category
            when (item.index) {
                0 -> {
                    binding.accountContainer.visibility = View.VISIBLE
                    binding.creditCardContainer.visibility = View.GONE
                }
                else -> {
                    binding.accountContainer.visibility = View.GONE
                    binding.creditCardContainer.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        fun getStartIntent(context: Context, security: Security): Intent {
            return Intent(context, AccountDetailActivity::class.java)
                    .apply { putExtra(Security.PRIMARY_KEY, security.sequence) }
        }
    }
}