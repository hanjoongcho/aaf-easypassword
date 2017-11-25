package io.github.hanjoongcho.easypassword.persistence

import android.content.Context
import io.github.hanjoongcho.easypassword.helper.EasyPasswordHelper
import io.github.hanjoongcho.easypassword.models.Security
import io.github.hanjoongcho.utils.AesUtils
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.Sort
import java.util.*

/**
 * Created by Administrator on 2017-11-17.
 */

class DatabaseHelper private constructor(
        val context: Context
) {

    private var realmConfiguration: RealmConfiguration? = null

    private val realmInstance: Realm
        get() {
            if (realmConfiguration == null) {
                realmConfiguration = RealmConfiguration.Builder()
                        .name(DIARY_DB_NAME)
                        .schemaVersion(1)
                        .migration(DatabaseMigration())
                        .modules(Realm.getDefaultModule())
                        .build()
            }
            return Realm.getInstance(realmConfiguration)
        }

    fun getDatabasePath(): String = realmInstance.path

    fun getMimeType(): String {
        return "text/aaf_v" + realmInstance.version
    }

    fun getMimeTypeAll(): Array<String?> {
        val currentVersion = realmInstance.version.toInt()
        val easyDiaryMimeType = arrayOfNulls<String>(currentVersion)
        for (i in 0 until currentVersion) {
            easyDiaryMimeType[i] = "text/aaf_v" + (i + 1)
        }
        return easyDiaryMimeType
    }

    fun initDatabase() {
        if (countSecurity() < 1) {
            EasyPasswordHelper.listDummySecurity.map {
                insertSecurity(it)
            }
        }
    }

    fun countSecurity() = realmInstance.where(Security::class.java).count().toInt()

    fun insertSecurity(security: Security) {

        realmInstance.beginTransaction()
        var sequence = 1
        realmInstance.where(Security::class.java)?.max(Security.PRIMARY_KEY)?.let { max ->
            sequence = max.toInt() + 1
        }
        security.sequence = sequence
        security.password = AesUtils.encryptPassword(context, security.password)
        realmInstance.insert(security)
        realmInstance.commitTransaction()
    }

    fun selectSecurityAll(): ArrayList<Security> {
        val realmResults = realmInstance.where(Security::class.java).findAllSorted(SECURITY_TITLE, Sort.ASCENDING)
        val list = ArrayList<Security>()
        list.addAll(realmResults.subList(0, realmResults.size))
        return list
    }

    fun updateSecurity(security: Security) {
        realmInstance.executeTransaction(Realm.Transaction { realm ->
            security.password = AesUtils.encryptPassword(context, security.password)
            realm.insertOrUpdate(security)
        })
    }

    fun selectSecurityBy(sequence: Int): Security {
//        val security: Security = realmInstance.where(Security::class.java).equalTo(Security.PRIMARY_KEY, sequence).findFirst()
//        account.password = AesUtils.decryptPassword(context, account.password)
        return realmInstance.where(Security::class.java).equalTo(Security.PRIMARY_KEY, sequence).findFirst()
    }

    fun deleteSecurityBy(sequence: Int) {
        beginTransaction()
        realmInstance.where(Security::class.java).equalTo(Security.PRIMARY_KEY, sequence).findFirst()?.let {
            it.deleteFromRealm()
        }
        commitTransaction()
    }

    fun beginTransaction() {
        realmInstance.beginTransaction()
    }

    fun commitTransaction() {
        realmInstance.commitTransaction()
    }

    companion object {

        const val SECURITY_TITLE = "title"
        const val DIARY_DB_NAME = "easy-password.realm"

        private var _instance: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            return _instance ?: synchronized(DatabaseHelper::class) {
                DatabaseHelper(context).also { _instance = it }
            }
        }
    }
}