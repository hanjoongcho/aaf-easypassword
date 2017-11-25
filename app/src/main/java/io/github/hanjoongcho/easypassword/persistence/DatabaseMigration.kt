package io.github.hanjoongcho.easypassword.persistence

import io.realm.DynamicRealm
import io.realm.RealmMigration

/**
 * Created by CHO HANJOONG on 2017-11-18.
 */

class DatabaseMigration : RealmMigration {

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {

        var currentVersion = oldVersion as Int
        val schema = realm.schema
        if (currentVersion == 1) {
            val diarySchema = schema.get("Security")
            currentVersion++
        }
    }
}