package io.github.hanjoongcho.easypassword.helper

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.IdRes
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.util.Pair
import android.view.View
import android.widget.ImageView
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.databinding.ActivitySecurityAddBinding
import io.github.hanjoongcho.easypassword.databinding.ActivitySecurityDetailBinding
import io.github.hanjoongcho.easypassword.databinding.ActivitySecurityEditBinding
import io.github.hanjoongcho.easypassword.models.Account
import io.github.hanjoongcho.easypassword.models.Category
import io.github.hanjoongcho.easypassword.models.CreditCard
import io.github.hanjoongcho.easypassword.models.Security

/**
 * Created by CHO HANJOONG on 2017-11-18.
 */

object EasyPasswordHelper {
    private var listCategory: MutableList<Category>? = null

    fun getCategories(context: Context): MutableList<Category> {
        val categories = context.resources.getStringArray(R.array.array_category_name)
        return listCategory ?: mutableListOf(
                Category(0, categories[0], "web"),
                Category(1, categories[1], "credit_card"),
                Category(2, categories[2], "home"),
                Category(3, categories[3], "lock"),
                Category(4, categories[4], "folder"),
                Category(5, categories[5], "password")
        )
    }

    fun getSampleSecurities(context: Context): MutableList<Security> {
        var categories = getCategories(context)
        val titles = context.resources.getStringArray(R.array.array_sample_security_title)
        var summaries = context.resources.getStringArray(R.array.array_sample_security_summary)
        return mutableListOf(
                Security(
                        null,
                        titles[0],
                        "google!@123",
                        3,
                        summaries[0],
                        categories[0],
                        Account("bulbasaur@mail.com"),
                        null
                ),
                Security(
                        null,
                        titles[1],
                        "1901",
                        1,
                        summaries[1],
                        categories[1],
                        null,
                        CreditCard("132-3574-123-09","12/2025", "901")
                ),
                Security(
                        null,
                        titles[2],
                        "157809",
                        1,
                        summaries[2],
                        categories[2],
                        null,
                        null
                ),
                Security(
                        null,
                        titles[3],
                        "0000",
                        2,
                        summaries[3],
                        categories[3],
                        null,
                        null
                ),
                Security(
                        null,
                        titles[4],
                        "proDoc12@!2",
                        5,
                        summaries[4],
                        categories[4],
                        null,
                        null
                ),
                Security(
                        null,
                        titles[5],
                        "9999",
                        1,
                        summaries[5],
                        categories[5],
                        null,
                        null
                ),
                Security(
                        null,
                        titles[6],
                        "google!@123",
                        4,
                        summaries[6],
                        categories[0],
                        Account("bulbasaur@mail.com"),
                        null
                )
        )
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun addViewById(activity: Activity,
                            @IdRes viewId: Int,
                            participants: ArrayList<Pair<View, String>>) {
        val view = activity.window.decorView.findViewById<View>(viewId)
        view?.transitionName?.let { participants.add(Pair(view, it)) }
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

    private fun setStrengthColor(view: ImageView, colorId: Int) {
        view.setBackgroundColor(colorId)
    }

    /**
     * Convenience method for color loading.

     * @param colorRes The resource id of the color to load.
     *
     * @return The loaded color.
     */
    fun getColor(@ColorRes colorRes: Int, activity: Activity) = ContextCompat.getColor(activity, colorRes)

    fun activityAccountDetailBinding(activity: Activity, binding: ActivitySecurityDetailBinding?, security: Security?) {
        security?.let { safetySecurity ->
            binding?.let { it ->
                val category = it.securityCategory?.selectedItem as Category
                it.securityPassword.text = safetySecurity.password
                it.securitySummary.text = safetySecurity.summary
                it.securityTitle.text = safetySecurity.title
                EasyPasswordHelper.setPasswordStrengthLevel(activity, safetySecurity.passwordStrengthLevel, it.included.level1, it.included.level2, it.included.level3, it.included.level4, it.included.level5)

                when (category.index) {
                    0 -> it.accountId?.text = safetySecurity.account?.id
                    1 -> {
                        it.creditCardSerial.text = safetySecurity.creditCard?.serial
                        it.creditCardExpireDate.text = safetySecurity.creditCard?.expireDate
                        it.creditCardCvc.text = safetySecurity.creditCard?.cardValidationCode
                    }
                    else -> {

                    }
                }
            }
        }
    }

    fun activityAccountEditBinding(activity: Activity, binding: ActivitySecurityEditBinding?, security: Security?) {
        security?.let { safetySecurity ->
            binding?.let { it ->
                val category = it.securityCategory?.selectedItem as Category
                it.securityPassword.setText(safetySecurity.password)
                it.securitySummary.setText(safetySecurity.summary)
                it.securityTitle.setText(safetySecurity.title)
                EasyPasswordHelper.setPasswordStrengthLevel(activity, safetySecurity.passwordStrengthLevel, it.included.level1, it.included.level2, it.included.level3, it.included.level4, it.included.level5)

                when (category.index) {
                    0 -> it.accountId?.setText(safetySecurity.account?.id)
                    1 -> {
                        it.creditCardSerial.setText(safetySecurity.creditCard?.serial)
                        it.creditCardExpireDate.setText(safetySecurity.creditCard?.expireDate)
                        it.creditCardCvc.setText(safetySecurity.creditCard?.cardValidationCode)
                    }
                    else -> {

                    }
                }
            }
        }
    }

    fun getSecurityFromLayout(binding: ActivitySecurityEditBinding?, category: Category, strengthLevel: Int): Security? {
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
                1 -> {
                    Security(
                            null,
                            it.securityTitle.text.toString(),
                            it.securityPassword.text.toString(),
                            strengthLevel,
                            it.securitySummary.text.toString(),
                            it.securityCategory.selectedItem as Category,
                            null,
                            CreditCard(
                                    it.creditCardSerial.text.toString(),
                                    it.creditCardExpireDate.text.toString(),
                                    it.creditCardCvc.text.toString()
                            )
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
                            null
                    )
                }
            }
        }
        return security
    }

    fun getSecurityFromLayout(binding: ActivitySecurityAddBinding?, category: Category, strengthLevel: Int): Security? {
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
                1 -> {
                    Security(
                            null,
                            it.securityTitle.text.toString(),
                            it.securityPassword.text.toString(),
                            strengthLevel,
                            it.securitySummary.text.toString(),
                            it.securityCategory.selectedItem as Category,
                            null,
                            CreditCard(
                                    it.creditCardSerial.text.toString(),
                                    it.creditCardExpireDate.text.toString(),
                                    it.creditCardCvc.text.toString()
                            )
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
                            null
                    )
                }
            }
        }
        return security
    }
}