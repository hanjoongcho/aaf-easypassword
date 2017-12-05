package io.github.hanjoongcho.utils

import android.content.Context
import com.tozny.crypto.android.AesCbcWithIntegrity
import com.tozny.crypto.android.AesCbcWithIntegrity.CipherTextIvMac
import com.tozny.crypto.android.AesCbcWithIntegrity.keyString
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.activities.PatternLockActivity

/**
 * Created by Administrator on 2017-11-21.
 */

class AesUtils {

    companion object {

        // Define salt string from GenerateSaltString test case
        // Generated salt string must be 172 characters
        const val SALT_STRING = ""

        fun encryptPassword(context: Context, plainText: String): String = when (plainText) {
            "" -> ""
            else -> {
                val savedPattern = CommonUtils.loadStringPreference(context , PatternLockActivity.SAVED_PATTERN, PatternLockActivity.SAVED_PATTERN_DEFAULT)
                val key: AesCbcWithIntegrity.SecretKeys = AesCbcWithIntegrity.generateKeyFromPassword(savedPattern, SALT_STRING)

                // The encryption / storage & display:
                val civ = AesCbcWithIntegrity.encrypt(plainText, AesCbcWithIntegrity.keys(keyString(key)))
                civ.toString()
            }
        }

        fun decryptPassword(context: Context, cipherText: String): String = when (cipherText) {
            "" -> ""
            else -> {
                val savedPattern = CommonUtils.loadStringPreference(context , PatternLockActivity.SAVED_PATTERN, PatternLockActivity.SAVED_PATTERN_DEFAULT)
                decryptPassword(context, cipherText, savedPattern)
            }
        }

        fun decryptPassword(context: Context, cipherText: String, patternString: String): String = when (cipherText) {
            "" -> ""
            else -> {
                val key: AesCbcWithIntegrity.SecretKeys = AesCbcWithIntegrity.generateKeyFromPassword(patternString, SALT_STRING)
                var plainText:String = ""
                try {
                    val cipherTextIvMac = CipherTextIvMac(cipherText)
                    plainText = AesCbcWithIntegrity.decryptString(cipherTextIvMac, AesCbcWithIntegrity.keys(keyString(key)))
                } catch (e: Exception) {
                    plainText = context.getString(R.string.aes_utils_decrypt_error)
                }
                plainText
            }
        }
    }
}
