package io.github.hanjoongcho.easypassword.utils

import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.tozny.crypto.android.AesCbcWithIntegrity
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GenerateSaltString {

    companion object {
        const val TAG = "GenerateSaltString-testGenerateSaltString"
    }

    @Test
    @Throws(Exception::class)
    fun testGenerateSaltString() {
        val salt = AesCbcWithIntegrity.saltString(AesCbcWithIntegrity.generateSalt())
        Log.i(TAG, "your key is $salt")
        Assert.assertEquals(172, salt.length)
    }
}