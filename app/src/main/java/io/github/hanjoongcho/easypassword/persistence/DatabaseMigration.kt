package io.github.hanjoongcho.easypassword.persistence

import android.content.Context
import android.util.Log
import io.github.hanjoongcho.easypassword.activities.IntroActivity
import io.github.hanjoongcho.utils.AesUtils
import io.realm.DynamicRealm
import io.realm.DynamicRealmObject
import io.realm.RealmMigration
import io.realm.RealmObjectSchema

/**
 * Created by CHO HANJOONG on 2017-11-18.
 */

class DatabaseMigration constructor(
        val context: Context
) : RealmMigration {

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {

        val schema = realm.schema
        var version = oldVersion.toInt()

        if (version == 1) {
            val diarySchema = schema.get("CreditCard")
            diarySchema
                    .addField("cardValidationCode", String::class.java)
                    .transform({
                        it.set("cardValidationCode", "")
                    })
                    .transform({
                        val cipherSerial = AesUtils.encryptPassword(context, it.getString("serial"))
                        it.set("serial", cipherSerial)

                    })
                    .transform({
                        val expireDate = AesUtils.encryptPassword(context, it.getString("expireDate"))
                        it.set("expireDate", expireDate)
                    })
            version++
        }
    }
}