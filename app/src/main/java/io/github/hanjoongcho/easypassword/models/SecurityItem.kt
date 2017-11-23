package io.github.hanjoongcho.easypassword.models

import io.realm.RealmModel

/**
 * Created by Administrator on 2017-11-23.
 */

open class SecurityItem(
        var title: String = "",
        var password: String = "",
        var passwordStrengthLevel: Int = -1,
        var summary: String = ""
) : RealmModel {

    companion object {
        const val SEQUENCE = "sequence"
    }
}