package io.github.hanjoongcho.easypassword.utils


import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.tozny.crypto.android.AesCbcWithIntegrity
import com.tozny.crypto.android.AesCbcWithIntegrity.CipherTextIvMac
import com.tozny.crypto.android.AesCbcWithIntegrity.keyString
import io.github.hanjoongcho.utils.AesUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AesUtilsTest {

    companion object {
        val TAG = "EASY_PASSWORD"
        val EXAMPLE_PASSWORD = "PInEApple@#!123!"
        val PLAIN_TEXT = "Hello com.tozny.crypto.android.AesCbcWithIntegrity"
        var cipherText: String? = null
    }

    private val keyString: String by lazy {
        val key: AesCbcWithIntegrity.SecretKeys = AesCbcWithIntegrity.generateKeyFromPassword(EXAMPLE_PASSWORD, AesUtils.SALT_STRING)
        // The encryption / storage & display:
        keyString(key)
    }

    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("io.github.hanjoongcho.easypassword", appContext.packageName)
    }

    @Test
    fun test01Encrypt() {
        assertEquals(69, keyString.length)
    }

    @Test
    fun test02Encrypt() {
        val civ = AesCbcWithIntegrity.encrypt(PLAIN_TEXT, AesCbcWithIntegrity.keys(keyString))
        cipherText = civ.toString()
        Log.i(TAG, cipherText)
        assertTrue(civ != null)
    }

    @Test
    fun test03Decrypt() {
        val cipherTextIvMac = CipherTextIvMac(cipherText)
        val decryptedText = AesCbcWithIntegrity.decryptString(cipherTextIvMac, AesCbcWithIntegrity.keys(keyString))
        assertEquals(decryptedText, PLAIN_TEXT)
    }

    @Test
    fun test05RandomSalt() {
        repeat(5, {
            val saltString = AesCbcWithIntegrity.saltString(AesCbcWithIntegrity.generateSalt())
            Log.i(TAG, "${saltString.length} $saltString")
            assertTrue(saltString.length == 172)
        })
    }
}
