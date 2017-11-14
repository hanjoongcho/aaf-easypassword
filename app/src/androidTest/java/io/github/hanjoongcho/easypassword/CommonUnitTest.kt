package io.github.hanjoongcho.easypassword


import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
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
        val KEY_STRING_DEVICE1 = "sKmUCoW/4Vnr0wcwtTukBQ==:GaETP9FEo4wddvNxX5KaOEW+HtHpCcojzZCPT2LJHN0="
        val EXAMPLE_PASSWORD = "always use passphrases for passwords wherever possible!"
        val PLAIN_TEXT = "Testing shows the presence, not the absence of bugs.\n\n  Edsger W. Dijkstra"
        var cipherText: String? = null
    }

    private val keyString: String by lazy {
        var key: AesCbcWithIntegrity.SecretKeys?
        val salt = AesCbcWithIntegrity.saltString(AesCbcWithIntegrity.generateSalt())
        key = AesCbcWithIntegrity.generateKeyFromPassword(EXAMPLE_PASSWORD, salt);

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
        assertTrue(civ != null)
    }

    @Test
    fun test03Decrypt() {
        val cipherTextIvMac = CipherTextIvMac(cipherText)
        val decryptedText = AesCbcWithIntegrity.decryptString(cipherTextIvMac, AesCbcWithIntegrity.keys(KEY_STRING_DEVICE1))
        assertEquals(decryptedText, PLAIN_TEXT)
    }

}
