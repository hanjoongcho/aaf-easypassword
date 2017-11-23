package io.github.hanjoongcho.easypassword.helper

import android.app.Activity
import io.github.hanjoongcho.easypassword.activities.AccountAddActivity
import io.github.hanjoongcho.easypassword.databinding.ActivityAccountDetailBinding
import io.github.hanjoongcho.easypassword.models.Security

/**
 * Created by CHO HANJOONG on 2017-11-23.
 */

class SecurityItemBindingHelper {

    companion object {

        fun activityAccountDetailBinding(activity: Activity, binding: ActivityAccountDetailBinding?, security: Security?) {
            security?.let { security ->
                binding?.let { binding ->
                    binding.accountId?.text = security.account?.id
                    binding.accountPassword?.text = security.password
                    binding.accountSummary?.text = security.summary
                    binding.accountManageTarget?.text = security.title
                    AccountAddActivity.setPasswordStrengthLevel(activity, security.passwordStrengthLevel, binding.included.level1, binding.included.level2, binding.included.level3, binding.included.level4, binding.included.level5)
                }
            }
        }
    }
}