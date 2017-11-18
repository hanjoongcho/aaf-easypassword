package io.github.hanjoongcho.easypassword.models

/**
 * Created by Administrator on 2017-11-15.
 */
data class Account(
        val title: String,
        val summary: String,
        val theme: Theme,
        val category: String,
        val id: String,
        val password: String,
        val passwordStrengthLevel: Int = 1
) {
    // web
    // credit_card
    // home

}