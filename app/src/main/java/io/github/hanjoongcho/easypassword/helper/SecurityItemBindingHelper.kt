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

            security?.let { safetySecurity ->
                binding?.let { it ->
                    val category = it.securityCategory?.selectedItem as Category
                    it.securityPassword.text = safetySecurity.password
                    it.securitySummary.text = safetySecurity.summary
                    it.securityTitle.text = safetySecurity.title
                    AccountAddActivity.setPasswordStrengthLevel(activity, safetySecurity.passwordStrengthLevel, it.included.level1, it.included.level2, it.included.level3, it.included.level4, it.included.level5)

                    when (category.index) {
                        0 -> it.accountId?.text = safetySecurity.account?.id
                        else -> {
                            it.creditCardSerial.text = safetySecurity.creditCard?.serial
                            it.creditCardExpireDate.text = safetySecurity.creditCard?.expireDate
                        }
                    }
                }
            }
        }

        fun activityAccountEditBinding(activity: Activity, binding: ActivityAccountEditBinding?, security: Security?) {
            security?.let { safetySecurity ->
                binding?.let { it ->
                    val category = it.securityCategory?.selectedItem as Category
                    it.securityPassword.setText(safetySecurity.password)
                    it.securitySummary.setText(safetySecurity.summary)
                    it.securityTitle.setText(safetySecurity.title)
                    AccountAddActivity.setPasswordStrengthLevel(activity, safetySecurity.passwordStrengthLevel, it.included.level1, it.included.level2, it.included.level3, it.included.level4, it.included.level5)

                    when (category.index) {
                        0 -> it.accountId?.setText(safetySecurity.account?.id)
                        else -> {
                            it.creditCardSerial.setText(safetySecurity.creditCard?.serial)
                            it.creditCardExpireDate.setText(safetySecurity.creditCard?.expireDate)
                        }
                    }
                }
            }
        }

        fun getSecurityFromLayout(binding: ActivityAccountEditBinding?, category: Category, strengthLevel: Int): Security? {
            var security: Security? = null
            binding?.let { it ->
                security = when (category.index) {
                    0 -> {
                        Security(
                                null,
                                it.securityTitle.text.toString(),
                                it.securityPassword.text.toString(),
                                strengthLevel,
                                it.securitySummary.text.toString(),
                                it.securityCategory.selectedItem as Category,
                                Account(it.accountId.text.toString()),
                                null
                        )
                    }
                    else -> {
                        Security(
                                null,
                                it.securityTitle.text.toString(),
                                it.securityPassword.text.toString(),
                                strengthLevel,
                                it.securitySummary.text.toString(),
                                it.securityCategory.selectedItem as Category,
                                null,
                                CreditCard(it.creditCardSerial.text.toString(), it.creditCardExpireDate.text.toString())
                        )
                    }
                }
            }
            return security
        }

        fun getSecurityFromLayout(binding: ActivityAccountAddBinding?, category: Category, strengthLevel: Int): Security? {
            var security: Security? = null
            binding?.let { it ->
                security = when (category.index) {
                    0 -> {
                        Security(
                                null,
                                it.securityTitle.text.toString(),
                                it.securityPassword.text.toString(),
                                strengthLevel,
                                it.securitySummary.text.toString(),
                                it.securityCategory.selectedItem as Category,
                                Account(it.accountId.text.toString()),
                                null
                        )
                    }
                    else -> {
                        Security(
                                null,
                                it.securityTitle.text.toString(),
                                it.securityPassword.text.toString(),
                                strengthLevel,
                                it.securitySummary.text.toString(),
                                it.securityCategory.selectedItem as Category,
                                null,
                                CreditCard(it.creditCardSerial.toString(),it.creditCardExpireDate.text.toString())
                        )
                    }
                }
            }
            return security
        }

    }
}