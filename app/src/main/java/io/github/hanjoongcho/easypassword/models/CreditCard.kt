package io.github.hanjoongcho.easypassword.models

/**
 * Created by Administrator on 2017-11-23.
 */

open class CreditCard : SecurityItem {

    var serial: String? = null
    var expireDate: String? = null
    var expireDateMillis: Long? = null

    constructor(
            serial: String,
            expireDate: String,
            expireDateMillis: Long,
            title: String,
            password: String,
            passwordStrengthLevel: Int,
            summary: String
    ) : super(title, password, passwordStrengthLevel, summary) {
        this.serial = serial
        this.expireDate = expireDate
        this.expireDateMillis = expireDateMillis
    }

}