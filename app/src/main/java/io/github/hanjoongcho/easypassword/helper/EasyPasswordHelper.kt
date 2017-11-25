package io.github.hanjoongcho.easypassword.helper

import android.annotation.TargetApi
import android.app.Activity
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
import io.github.hanjoongcho.easypassword.activities.SecurityAddActivity
import io.github.hanjoongcho.easypassword.activities.SettingActivity
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
     * Create the transition participants required during a activity transition while
     * avoiding glitches with the system UI.

     * @param activity The activity used as start for the transition.
     *
     * @param includeStatusBar If false, the status bar will not be added as the transition
     * participant.
     *
     * @return All transition participants.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun createSafeTransitionParticipants(activity: Activity,
                                         includeStatusBar: Boolean,
                                         vararg others: Pair<View, String>
    ): Array<Pair<View, String>> {
        // Avoid system UI glitches as described here:
        // https://plus.google.com/+AlexLockwood/posts/RPtwZ5nNebb

        return ArrayList<Pair<View, String>>(3).apply {
            if (includeStatusBar) {
                addViewById(activity, android.R.id.statusBarBackground, this)
            }
            addViewById(activity, android.R.id.navigationBarBackground, this)
            addAll(others.toList())

        }.toTypedArray()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun addViewById(activity: Activity,
                            @IdRes viewId: Int,
                            participants: ArrayList<Pair<View, String>>) {
        val view = activity.window.decorView.findViewById<View>(viewId)
        view?.transitionName?.let { participants.add(Pair(view, it)) }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun startSettingActivityWithTransition(activity: Activity, clazz: Class<*>) {

        val animationBundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                *createSafeTransitionParticipants(activity,
                        false)).toBundle()

        // Start the activity with the participants, animating from one to the other.
        val intent = Intent(activity, clazz)
        ActivityCompat.startActivity(activity, intent, animationBundle)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun startSettingActivityWithTransition(activity: Activity, intent: Intent) {

        val animationBundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                *createSafeTransitionParticipants(activity,
                        false)).toBundle()

        ActivityCompat.startActivity(activity, intent, animationBundle)
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
                            CreditCard(it.creditCardSerial.text.toString(), it.creditCardExpireDate.text.toString())
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
                            CreditCard(it.creditCardSerial.toString(),it.creditCardExpireDate.text.toString())
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