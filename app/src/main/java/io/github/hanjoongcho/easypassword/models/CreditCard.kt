package io.github.hanjoongcho.easypassword.models

import io.realm.RealmObject

/**
 * Created by Administrator on 2017-11-23.
 */

open class CreditCard(
        var serial: String = "",
        var expireDate: String = "",
        var expireDateMillis: Long = 0
) : RealmObject()