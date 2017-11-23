package io.github.hanjoongcho.easypassword.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Administrator on 2017-11-23.
 */

open class Security(
    @PrimaryKey var sequence: Int? = null,
    var category: Category? = null,
    var securityItem: SecurityItem? = null
) : RealmObject() {

    companion object {
        const val PRIMARY_KEY = "sequence"
    }
}