package io.github.hanjoongcho.easypassword.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Administrator on 2017-11-15.
 */
open class Account(
        var id: String = ""
) : RealmObject()