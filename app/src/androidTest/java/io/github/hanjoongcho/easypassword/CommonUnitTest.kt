package io.github.hanjoongcho.easypassword


import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.tozny.crypto.android.AesCbcWithIntegrity
import com.tozny.crypto.android.AesCbcWithIntegrity.CipherTextIvMac
import com.tozny.crypto.android.AesCbcWithIntegrity.keyString
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
class CommonUnitTest {

    companion object {
        val TAG = "EASY_PASSWORD"
        val KEY_STRING_DEVICE1 = "sKmUCoW/4Vnr0wcwtTukBQ==:GaETP9FEo4wddvNxX5KaOEW+HtHpCcojzZCPT2LJHN0="
        val EXAMPLE_PASSWORD = "always use passphrases for passwords wherever possible!"
        val PLAIN_TEXT = "Testing shows the presence, not the absence of bugs.\n\n  Edsger W. Dijkstra"
        val listEncryptString = listOf<String>(
                "/osPmYj0mwb29sapBreEcA==:zBkha1+cSkkxUMxCHILZSAIXLg5Mvr1wsJhPFwA67zk=:7eVawiQ737JJWULtqRrA6IiUUP2sbuBx4P7MdJ7xLcJpWrS1JivrAyLjmd37vSFtLJtaHq2wKbxFUOd9tLPhqDILAsg39bKUIzmlYuqrbXQ="
                , "rLdkCgAfIzyOKyKktqM/jQ==:seJwDOTJonhCL5J3YOy4zjBMwRCQ7RmzpLHdNlTRc/U=:KuI/ByM2pimopgM+XA2IXAp9wpeQBjJSucqzuRNerLaWypVewjAhJn96+aSI4HVII0lZ55/OY9GCAmnVcCHe+bL9AmaMGculGB2q0AAnOos="
        )

        var cipherText: String? = null
    }

    private val keyString: String by lazy {
        val salt = AesCbcWithIntegrity.saltString(AesCbcWithIntegrity.generateSalt())
        val key: AesCbcWithIntegrity.SecretKeys = AesCbcWithIntegrity.generateKeyFromPassword(EXAMPLE_PASSWORD, salt)
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
        val civ = AesCbcWithIntegrity.encrypt(PLAIN_TEXT, AesCbcWithIntegrity.keys(KEY_STRING_DEVICE1))
        cipherText = civ.toString()
        Log.i(TAG, cipherText)
        assertTrue(civ != null)
    }

    @Test
    fun test03Decrypt() {
        val cipherTextIvMac = CipherTextIvMac(cipherText)
        val decryptedText = AesCbcWithIntegrity.decryptString(cipherTextIvMac, AesCbcWithIntegrity.keys(KEY_STRING_DEVICE1))
        assertEquals(decryptedText, PLAIN_TEXT)
    }

    @Test
    fun test04Decrypt() {
        listEncryptString.map { encryptString ->
            val cipherTextIvMac = CipherTextIvMac(encryptString)
            assertEquals(AesCbcWithIntegrity.decryptString(cipherTextIvMac, AesCbcWithIntegrity.keys(KEY_STRING_DEVICE1)), PLAIN_TEXT)
        }
    }

}
