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
                Account("Google", "https://www.google.com", Theme.white, "web", "entertainment", "123", 4),
                Account("GitHub", "https://github.com/", Theme.white,"web", "geography", "1234", 4),
                Account("네이버", "https://www.naver.com/", Theme.white, "web", "food", "1234", 2),
                Account("카카오뱅크", "카카오뱅크 체크카드", Theme.white,"credit_card", "geography", "1234", 3),
                Account("회사", "회사현관 출입번호", Theme.white,"home", "geography", "1234", 1)
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