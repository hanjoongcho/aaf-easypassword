package test.io.github.hanjoongcho.easypassword

import io.github.hanjoongcho.utils.PasswordStrengthUtils
import org.junit.Test

import org.junit.Assert.*
import me.gosimple.nbvcxz.Nbvcxz
import me.gosimple.nbvcxz.resources.Configuration
import me.gosimple.nbvcxz.resources.ConfigurationBuilder
import me.gosimple.nbvcxz.scoring.Result
import me.gosimple.nbvcxz.scoring.TimeEstimate
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
//@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class CommonUnitTest {

    @Test
    fun test01Estimate_01_ONLINE_THROTTLED() {
        assertEquals(
                "9 minutes",
                PasswordStrengthUtils.getTimeToCrackFormatted("easypassword", PasswordStrengthUtils.ONLINE_THROTTLED)
        )
    }

    @Test
    fun test01Estimate_02_ONLINE_THROTTLED() {
        assertEquals(
                "1 days",
                PasswordStrengthUtils.getTimeToCrackFormatted("easypassWORD", PasswordStrengthUtils.ONLINE_THROTTLED)
        )
    }

    @Test
    fun test01Estimate_03_ONLINE_THROTTLED() {
        assertEquals(
                "2 days",
                PasswordStrengthUtils.getTimeToCrackFormatted("easypassword!#", PasswordStrengthUtils.ONLINE_THROTTLED)
        )
    }

    @Test
    fun test01Estimate_04_ONLINE_THROTTLED() {
        assertEquals(
                "11 months",
                PasswordStrengthUtils.getTimeToCrackFormatted("easypassWORD!#", PasswordStrengthUtils.ONLINE_THROTTLED)
        )
    }

    @Test
    fun test01Estimate_05_ONLINE_THROTTLED() {
        assertEquals(
                "9 years",
                PasswordStrengthUtils.getTimeToCrackFormatted("easypassWORD!#1", PasswordStrengthUtils.ONLINE_THROTTLED)
        )
    }

    @Test
    fun test01Estimate_06_ONLINE_THROTTLED() {
        assertEquals(
                "4 years",
                PasswordStrengthUtils.getTimeToCrackFormatted("eAsypassWORD!#", PasswordStrengthUtils.ONLINE_THROTTLED)
        )
    }

    @Test
    fun test02Estimate_ONLINE_UNTHROTTLED() {
        assertEquals(
                "11 seconds",
                PasswordStrengthUtils.getTimeToCrackFormatted("easypassword", PasswordStrengthUtils.ONLINE_UNTHROTTLED)
        )
    }

    @Test
    fun test03Estimate_OFFLINE_BCRYPT_14() {
        assertEquals(
                "8 seconds",
                PasswordStrengthUtils.getTimeToCrackFormatted("easypassword", PasswordStrengthUtils.OFFLINE_BCRYPT_14)
        )
    }

    @Test
    fun test04Estimate_OFFLINE_BCRYPT_12() {
        assertEquals(
                "2 seconds",
                PasswordStrengthUtils.getTimeToCrackFormatted("easypassword", PasswordStrengthUtils.OFFLINE_BCRYPT_12)
        )
    }

    @Test
    fun test05Estimate_OFFLINE_BCRYPT_10() {
        assertEquals(
                "instant",
                PasswordStrengthUtils.getTimeToCrackFormatted("easypassword", PasswordStrengthUtils.OFFLINE_BCRYPT_10)
        )
    }

    @Test
    fun test06Estimate_OFFLINE_BCRYPT_12() {
        assertTrue(116L == PasswordStrengthUtils.getTimeToCrack("AeasypassworD", PasswordStrengthUtils.OFFLINE_BCRYPT_12))
    }


//    @Test
//    fun test06Estimate_OFFLINE_SIMPLE_LOCK() {
////        result.getGuesses()
//        val result = nbvcxz.estimate("9301150920");
//        assertEquals("40 seconds", TimeEstimate.getTimeToCrackFormatted(result, "OFFLINE_BCRYPT_10"))
//    }

}
