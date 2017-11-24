package io.github.hanjoongcho.easypassword.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.adpaters.SecurityCategoryAdapter
import io.github.hanjoongcho.easypassword.databinding.ActivitySecurityAddBinding
import io.github.hanjoongcho.easypassword.helper.SecurityItemBindingHelper
import io.github.hanjoongcho.easypassword.helper.database
import io.github.hanjoongcho.easypassword.models.Account
import io.github.hanjoongcho.easypassword.models.Category
import io.github.hanjoongcho.easypassword.models.CreditCard
import io.github.hanjoongcho.easypassword.models.Security
import io.github.hanjoongcho.utils.PasswordStrengthUtils

/**
 * Created by CHO HANJOONG on 2017-11-18.
 */

class SecurityAddActivity : CommonActivity() {

    private var mBinding: ActivitySecurityAddBinding? = null
    private var mTempStrengthLevel = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security_add)

        mBinding = DataBindingUtil.setContentView<ActivitySecurityAddBinding>(this,
                R.layout.activity_security_add)

        setSupportActionBar(mBinding?.toolbarPlayer)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }

        bindEvent()
        initCategorySpinner()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> this@SecurityAddActivity.onBackPressed()
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
                val security: Security? = SecurityItemBindingHelper.getSecurityFromLayout(mBinding, binding.securityCategory.selectedItem as Category, mTempStrengthLevel)
                security?.let {
                    this@SecurityAddActivity.database().insertSecurity(it)
                    this@SecurityAddActivity.onBackPressed()
                }
            })

            binding.securityPassword.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val level = PasswordStrengthUtils.getScore(s.toString())
                    if (level != mTempStrengthLevel) {
                        mTempStrengthLevel = level
                        SecurityAddActivity.setPasswordStrengthLevel(
                                this@SecurityAddActivity,
                                mTempStrengthLevel,
                                binding.included.level1,
                                binding.included.level2,
                                binding.included.level3,
                                binding.included.level4,
                                binding.included.level5
                        )
                    }
                }
            })
        }
    }

    private fun changeCategoryContainer() {
        mBinding?.let { binding ->
            val item: Category = binding.securityCategory.selectedItem as Category
            when (item.index) {
                0 -> {
                    binding.accountContainer.visibility = View.VISIBLE
                    binding.creditCardContainer.visibility = View.GONE
                }
                1 -> {
                    binding.accountContainer.visibility = View.GONE
                    binding.creditCardContainer.visibility = View.VISIBLE
                }
                else -> {
                    binding.accountContainer.visibility = View.GONE
                    binding.creditCardContainer.visibility = View.GONE
                }
            }
        }
    }

    private fun initCategorySpinner() {
        val adapter: ArrayAdapter<Category> = SecurityCategoryAdapter(this@SecurityAddActivity, R.layout.item_category, listCategory)
        mBinding?.let { binding ->
            binding.securityCategory.adapter = adapter
            binding.securityCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    changeCategoryContainer()
                }
            }
        }
    }

    companion object {
        fun getStartIntent(context: Context): Intent = Intent(context, SecurityAddActivity::class.java)
        val listCategory: List<Category> = mutableListOf(
                Category(0, "웹사이트", "web"),
                Category(1, "신용카드", "credit_card"),
                Category(2, "도어락", "home"),
                Category(3, "자물쇠", "lock"),
                Category(4, "전자문서", "folder"),
                Category(5, "미분류", "password")
        )
        val listDummySecurity: List<Security> = mutableListOf(
                Security(
                        null,
                        "Google",
                        "google!@123",
                        3,
                        "https://www.google.com",
                        listCategory[0],
                        Account("bulbasaur@mail.com"),
                        null
                ),
                Security(
                        null,
                        "카카오뱅크",
                        "1901",
                        1,
                        "카뱅 체크카드",
                        listCategory[1],
                        null,
                        CreditCard("132-3574-123-09","12/2025")
                ),
                Security(
                        null,
                        "회사현관",
                        "157809",
                        1,
                        "회사 현관 출입번호",
                        listCategory[2],
                        null,
                        null
                ),
                Security(
                        null,
                        "여행가방",
                        "0000",
                        2,
                        "노랑색 캐리어",
                        listCategory[1],
                        null,
                        null
                ),
                Security(
                        null,
                        "프로젝트 기획서",
                        "proDoc12@!2",
                        5,
                        "2018 모바일 프로젝트 화면정의서",
                        listCategory[4],
                        null,
                        null
                ),
                Security(
                        null,
                        "VOD 인증번호",
                        "9999",
                        1,
                        "VOD 결재 인증번호",
                        listCategory[5],
                        null,
                        null
                )
        )

        /**
         * Convenience method for color loading.

         * @param colorRes The resource id of the color to load.
         *
         * @return The loaded color.
         */
        fun getColor(@ColorRes colorRes: Int, activity: Activity) = ContextCompat.getColor(activity, colorRes)

        fun setStrengthColor(view: ImageView, colorId: Int) {
            view.setBackgroundColor(colorId)
        }

        fun setPasswordStrengthLevel(activity: Activity, passwordStrengthLevel: Int, level1: ImageView, level2: ImageView, level3: ImageView, level4: ImageView, level5: ImageView) {
            when (passwordStrengthLevel) {
                1 -> {
                    setStrengthColor(level1, getColor(R.color.strength_bad, activity))
                    setStrengthColor(level2, getColor(R.color.strength_default, activity))
                    setStrengthColor(level3, getColor(R.color.strength_default, activity))
                    setStrengthColor(level4, getColor(R.color.strength_default, activity))
                    setStrengthColor(level5, getColor(R.color.strength_default, activity))
                }
                2 -> {
                    setStrengthColor(level1, getColor(R.color.strength_bad, activity))
                    setStrengthColor(level2, getColor(R.color.strength_bad, activity))
                    setStrengthColor(level3, getColor(R.color.strength_default, activity))
                    setStrengthColor(level4, getColor(R.color.strength_default, activity))
                    setStrengthColor(level5, getColor(R.color.strength_default, activity))
                }
                3 -> {
                    setStrengthColor(level1, getColor(R.color.strength_good, activity))
                    setStrengthColor(level2, getColor(R.color.strength_good, activity))
                    setStrengthColor(level3, getColor(R.color.strength_good, activity))
                    setStrengthColor(level4, getColor(R.color.strength_default, activity))
                    setStrengthColor(level5, getColor(R.color.strength_default, activity))
                }
                4 -> {
                    setStrengthColor(level1, getColor(R.color.strength_good, activity))
                    setStrengthColor(level2, getColor(R.color.strength_good, activity))
                    setStrengthColor(level3, getColor(R.color.strength_good, activity))
                    setStrengthColor(level4, getColor(R.color.strength_good, activity))
                    setStrengthColor(level5, getColor(R.color.strength_default, activity))
                }
                5 -> {
                    setStrengthColor(level1, getColor(R.color.strength_good, activity))
                    setStrengthColor(level2, getColor(R.color.strength_good, activity))
                    setStrengthColor(level3, getColor(R.color.strength_good, activity))
                    setStrengthColor(level4, getColor(R.color.strength_good, activity))
                    setStrengthColor(level5, getColor(R.color.strength_good, activity))
                }
            }
        }
    }
}

