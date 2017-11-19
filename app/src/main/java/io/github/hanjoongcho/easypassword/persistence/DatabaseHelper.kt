package io.github.hanjoongcho.easypassword.persistence

import android.content.Context
import io.github.hanjoongcho.easypassword.activities.AccountAddActivity
import io.github.hanjoongcho.easypassword.models.Account
import io.github.hanjoongcho.easypassword.models.Category
import io.github.hanjoongcho.easypassword.models.Theme
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.Sort
import java.util.ArrayList

/**
 * Created by Administrator on 2017-11-17.
 */

class DatabaseHelper private constructor(
        context: Context
) {

    private var realmConfiguration: RealmConfiguration? = null

    private val realmInstance: Realm
        get() {
            if (realmConfiguration == null) {
                realmConfiguration = RealmConfiguration.Builder()
                        .name("easypassword.realm")
                        .schemaVersion(1)
                        .migration(DatabaseMigration())
                        .modules(Realm.getDefaultModule())
                        .build()
            }
            return Realm.getInstance(realmConfiguration)
        }

    fun initDatabase() {
        if (countAccounts() < 1) {
            AccountAddActivity.listDummyAccount.map {
                insertAccount(it)
            }
        }
    }

    private fun countAccounts() = realmInstance.where(Account::class.java).count().toInt()

    fun insertAccount(account: Account) {
        realmInstance.beginTransaction()
        var sequence = 1
        realmInstance.where(Account::class.java)?.max("sequence")?.let { max ->
            sequence = max.toInt() + 1
        }
        account.sequence = sequence
        realmInstance.insert(account)
        realmInstance.commitTransaction()
    }

    fun selectAccountAll(): ArrayList<Account> {
        val realmResults = realmInstance.where(Account::class.java).findAllSorted("title", Sort.ASCENDING)
        val list = ArrayList<Account>()
        list.addAll(realmResults.subList(0, realmResults.size))
        return list
    }

    fun updateAccount(account: Account) {
        realmInstance.executeTransaction(Realm.Transaction { realm -> realm.insertOrUpdate(account) })
    }

    fun selectAccountBy(sequence: Int): Account = realmInstance.where(Account::class.java).equalTo("sequence", sequence).findFirst()

    companion object {

        private var _instance: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            return _instance ?: synchronized(DatabaseHelper::class) {
                DatabaseHelper(context).also { _instance = it }
            }
        }
    }
}