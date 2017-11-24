package io.github.hanjoongcho.easypassword.helper

import android.app.Activity
import android.view.View
import io.github.hanjoongcho.easypassword.activities.AccountAddActivity
import io.github.hanjoongcho.easypassword.databinding.ActivityAccountAddBinding
import io.github.hanjoongcho.easypassword.databinding.ActivityAccountDetailBinding
import io.github.hanjoongcho.easypassword.databinding.ActivityAccountEditBinding
import io.github.hanjoongcho.easypassword.models.Account
import io.github.hanjoongcho.easypassword.models.Category
import io.github.hanjoongcho.easypassword.models.CreditCard
import io.github.hanjoongcho.easypassword.models.Security

/**
 * Created by CHO HANJOONG on 2017-11-23.
 */

class SecurityItemBindingHelper {

    companion object {

        fun activityAccountDetailBinding(activity: Activity, binding: ActivityAccountDetailBinding?, security: Security?) {
            val category = binding?.securityCategory?.selectedItem as Category
            security?.let { security ->
                binding?.let { binding ->
                    binding.securityPassword.text = security.password
                    binding.securitySummary.text = security.summary
                    binding.securityTitle.text = security.title
                    AccountAddActivity.setPasswordStrengthLevel(activity, security.passwordStrengthLevel, binding.included.level1, binding.included.level2, binding.included.level3, binding.included.level4, binding.included.level5)

                    when (category.index) {
                        0 -> binding.accountId?.text = security.account?.id
                        else -> {
                            binding.creditCardSerial.text = security.creditCard?.serial
                            binding.creditCardExpireDate.text = security.creditCard?.expireDate
                        }
                    }
                }
            }
        }

        fun activityAccountEditBinding(activity: Activity, binding: ActivityAccountEditBinding?, security: Security?) {
            security?.let { security ->
                binding?.let { binding ->
                    binding.accountId?.setText(security.account?.id)
                    binding.accountPassword?.setText(security.password)
                    binding.accountSummary?.setText(security.summary)
                    binding.accountManageTarget?.setText(security.title)
                    AccountAddActivity.setPasswordStrengthLevel(activity, security.passwordStrengthLevel, binding.included.level1, binding.included.level2, binding.included.level3, binding.included.level4, binding.included.level5)
                }
            }
        }

        fun getSecurityFromLayout(binding: ActivityAccountEditBinding?, category: Category, strengthLevel: Int): Security? {
            var security: Security? = null
            binding?.let {
                security = when (category.index) {
                    0 -> {
                        Security(
                                null,
                                binding.accountManageTarget.toString(),
                                binding.accountPassword.toString(),
                                strengthLevel,
                                binding.accountSummary.toString(),
                                binding.accountManageCategory.selectedItem as Category,
                                Account(binding.accountId.toString()),
                                null
                        )
                    }
                    else -> {
                        Security(
                                null,
                                binding.accountManageTarget.toString(),
                                binding.accountPassword.toString(),
                                strengthLevel,
                                binding.accountSummary.toString(),
                                binding.accountManageCategory.selectedItem as Category,
                                Account(binding.accountId.toString()),
                                null
                        )
                    }
                }
            }
            return security
        }

        fun getSecurityFromLayout(binding: ActivityAccountAddBinding?, category: Category, strengthLevel: Int): Security? {
            var security: Security? = null
            binding?.let {
                security = when (category.index) {
                    0 -> {
                        Security(
                                null,
                                binding.securityTitle.text.toString(),
                                binding.securityPassword.text.toString(),
                                strengthLevel,
                                binding.securitySummary.text.toString(),
                                binding.securityCategory.selectedItem as Category,
                                Account(binding.accountId.text.toString()),
                                null
                        )
                    }
                    else -> {
                        Security(
                                null,
                                binding.securityTitle.text.toString(),
                                binding.securityPassword.text.toString(),
                                strengthLevel,
                                binding.securitySummary.text.toString(),
                                binding.securityCategory.selectedItem as Category,
                                null,
                                CreditCard(binding.creditCardSerial.toString(),binding.creditCardExpireDate.text.toString())
                        )
                    }
                }
            }
            return security
        }

    }
}