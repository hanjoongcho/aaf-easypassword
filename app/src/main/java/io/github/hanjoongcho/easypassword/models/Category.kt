package io.github.hanjoongcho.easypassword.models

import io.realm.RealmObject

/**
 * Created by CHO HANJOONG on 2017-11-19.
 */

open class Category(
        var index: Int = -1,
        var name: String = "",
        var resourceName: String = ""
) : RealmObject()