package io.github.hanjoongcho.easypassword.persistence

import android.content.Context
import io.github.hanjoongcho.easypassword.models.Account
import io.github.hanjoongcho.easypassword.models.Theme

/**
 * Created by Administrator on 2017-11-17.
 */

class DatabaseHelper private constructor(
        context: Context
) {

    fun getAccounts(): List<Account> {
        return mutableListOf<Account>(
                Account(Theme.blue, "entertainment", "123"),
                Account(Theme.green, "food", "1234"),
                Account(Theme.purple, "geography", "1234")
        )
    }

    companion object {

        private var _instance: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            return _instance ?: synchronized(DatabaseHelper::class) {
                DatabaseHelper(context).also { _instance = it }
            }
        }
    }
}