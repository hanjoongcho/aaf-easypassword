package test.io.github.hanjoongcho.easypassword

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

    val configuration: Configuration = ConfigurationBuilder().
            setGuessTypes(mapOf(
                    "OFFLINE_SIMPLE_LOCK" to 1L,
                    "ONLINE_THROTTLED" to 2L,
                    "ONLINE_UNTHROTTLED" to 100L,
                    "OFFLINE_BCRYPT_14" to 125L,
                    "OFFLINE_BCRYPT_12" to 500L,
                    "OFFLINE_BCRYPT_10" to 2000L,
                    "OFFLINE_BCRYPT_5" to 64000L,
                    "OFFLINE_SHA512" to 5000000000L,
                    "OFFLINE_SHA1" to 37336000000L,
                    "OFFLINE_MD5" to 115840000000L
            )).
            createConfiguration()

    val nbvcxz: Nbvcxz by lazy {
        Nbvcxz(configuration)
    }

    @Test
    fun test01Estimate_01_ONLINE_THROTTLED() {
        var result: Result
        result = nbvcxz.estimate("easypassword");
        assertEquals("9 minutes", TimeEstimate.getTimeToCrackFormatted(result, "ONLINE_THROTTLED"))
    }

    @Test
    fun test01Estimate_02_ONLINE_THROTTLED() {
        var result: Result
        result = nbvcxz.estimate("joong12#GG");
        assertEquals("41 centuries", TimeEstimate.getTimeToCrackFormatted(result, "ONLINE_THROTTLED"))
    }

    @Test
    fun test02Estimate_ONLINE_UNTHROTTLED() {
        var result: Result
        result = nbvcxz.estimate("easypassword");
        assertEquals("11 seconds", TimeEstimate.getTimeToCrackFormatted(result, "ONLINE_UNTHROTTLED"))
    }

    @Test
    fun test03Estimate_OFFLINE_BCRYPT_14() {
        var result: Result
        result = nbvcxz.estimate("easypassword");
        assertEquals("8 seconds", TimeEstimate.getTimeToCrackFormatted(result, "OFFLINE_BCRYPT_14"))
    }

    @Test
    fun test04Estimate_OFFLINE_BCRYPT_12() {
        var result: Result
        result = nbvcxz.estimate("easypassword");
        assertEquals("2 seconds", TimeEstimate.getTimeToCrackFormatted(result, "OFFLINE_BCRYPT_12"))
    }

    @Test
    fun test05Estimate_OFFLINE_BCRYPT_10() {
        var result: Result
        result = nbvcxz.estimate("easypassword");
        assertEquals("instant", TimeEstimate.getTimeToCrackFormatted(result, "OFFLINE_BCRYPT_10"))
    }

//    @Test
//    fun test06Estimate_OFFLINE_SIMPLE_LOCK() {
////        result.getGuesses()
//        val result = nbvcxz.estimate("9301150920");
//        assertEquals("40 seconds", TimeEstimate.getTimeToCrackFormatted(result, "OFFLINE_BCRYPT_10"))
//    }

}
