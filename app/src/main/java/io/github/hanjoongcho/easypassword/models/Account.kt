package io.github.hanjoongcho.easypassword.models

import io.realm.annotations.PrimaryKey

/**
 * Created by Administrator on 2017-11-15.
 */
open class Account : SecurityItem {

    constructor(
            id: String,
            title: String,
            password: String,
            passwordStrengthLevel: Int,
            summary: String
    ) : super(title, password, passwordStrengthLevel, summary)
}