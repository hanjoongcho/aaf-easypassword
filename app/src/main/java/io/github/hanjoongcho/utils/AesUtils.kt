package io.github.hanjoongcho.utils

import android.content.Context
import android.util.Log
import io.github.hanjoongcho.easypassword.activities.PatternLockActivity
import com.tozny.crypto.android.AesCbcWithIntegrity
import com.tozny.crypto.android.AesCbcWithIntegrity.CipherTextIvMac
import com.tozny.crypto.android.AesCbcWithIntegrity.keyString
import io.github.hanjoongcho.easypassword.R

/**
 * Created by Administrator on 2017-11-21.
 */

class AesUtils {

    companion object {

        private const val SALT_STRING = "RjaXrZURG40sMzDlVjaRKIdCT7vfok1u8gAOmwnaedDUpyAENeDTCWnc62y33seezkdqzXhAnDqzrTi+mvvDIRHYdLVllQmhXmUbFAnwyG9jkWgWkfk49ieM6QsM7LcsFU79auMK84ELHRQT1pj0ABJnDFVokePKA3C6wysd6P8="

        fun encryptPassword(context: Context, plainText: String): String {

            val savedPattern = CommonUtils.loadStringPreference(context , PatternLockActivity.SAVED_PATTERN, PatternLockActivity.SAVED_PATTERN_DEFAULT)
            val key: AesCbcWithIntegrity.SecretKeys = AesCbcWithIntegrity.generateKeyFromPassword(savedPattern, SALT_STRING)

            // The encryption / storage & display:
            val civ = AesCbcWithIntegrity.encrypt(plainText, AesCbcWithIntegrity.keys(keyString(key)))
            return civ.toString()
        }

        fun decryptPassword(context: Context, cipherText: String): String {

            val savedPattern = CommonUtils.loadStringPreference(context , PatternLockActivity.SAVED_PATTERN, PatternLockActivity.SAVED_PATTERN_DEFAULT)
            return decryptPassword(context, cipherText, savedPattern)
        }

        fun decryptPassword(context: Context, cipherText: String, patternString: String): String {

            val key: AesCbcWithIntegrity.SecretKeys = AesCbcWithIntegrity.generateKeyFromPassword(patternString, SALT_STRING)
            var plainText:String = ""
            try {
                val cipherTextIvMac = CipherTextIvMac(cipherText)
                plainText = AesCbcWithIntegrity.decryptString(cipherTextIvMac, AesCbcWithIntegrity.keys(keyString(key)))
            } catch (e: Exception) {
                plainText = context.getString(R.string.aes_utils_decrypt_error)
            }
            return plainText
        }
    }
}
