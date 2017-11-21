package io.github.hanjoongcho.utils

import android.content.Context
import android.util.Log
import io.github.hanjoongcho.easypassword.activities.PatternLockActivity
import com.tozny.crypto.android.AesCbcWithIntegrity
import com.tozny.crypto.android.AesCbcWithIntegrity.CipherTextIvMac
import com.tozny.crypto.android.AesCbcWithIntegrity.keyString
/**
 * Created by Administrator on 2017-11-21.
 */

class AesUtils {

    companion object {

        fun encryptPassword(context: Context, plainText: String): String {
            val salt = AesCbcWithIntegrity.saltString(AesCbcWithIntegrity.generateSalt())
            val savedPattern = CommonUtils.loadStringPreference(context , PatternLockActivity.SAVED_PATTERN, PatternLockActivity.SAVED_PATTERN_DEFAULT)
            val key: AesCbcWithIntegrity.SecretKeys = AesCbcWithIntegrity.generateKeyFromPassword(savedPattern, salt)

            // The encryption / storage & display:
            val civ = AesCbcWithIntegrity.encrypt(plainText, AesCbcWithIntegrity.keys(keyString(key)))
            return civ.toString()
        }

        fun decryptPassword(context: Context, cipherText: String): String {
            val salt = AesCbcWithIntegrity.saltString(AesCbcWithIntegrity.generateSalt())
            val savedPattern = CommonUtils.loadStringPreference(context , PatternLockActivity.SAVED_PATTERN, PatternLockActivity.SAVED_PATTERN_DEFAULT)
            val key: AesCbcWithIntegrity.SecretKeys = AesCbcWithIntegrity.generateKeyFromPassword(savedPattern, salt)

            Log.i("*keyC*", cipherText);
            Log.i("*keyS*", savedPattern );

            val cipherTextIvMac = CipherTextIvMac(cipherText)
            return AesCbcWithIntegrity.decryptString(cipherTextIvMac, AesCbcWithIntegrity.keys(keyString(key)))
        }
    }
}
