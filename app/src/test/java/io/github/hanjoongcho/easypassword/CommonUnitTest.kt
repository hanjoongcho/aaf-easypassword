package test.io.github.hanjoongcho.easypassword

import org.junit.Test

import org.junit.Assert.*
import me.gosimple.nbvcxz.Nbvcxz
import me.gosimple.nbvcxz.scoring.Result
import me.gosimple.nbvcxz.scoring.TimeEstimate


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CommonUnitTest {

    @Test
    fun test01Estimate() {
        var result: Result
        val nbvcxz = Nbvcxz()

        result = nbvcxz.estimate("correcthorsebatterystaple");
        assertEquals("13 hours", TimeEstimate.getTimeToCrackFormatted(result, "ONLINE_THROTTLED"))
        assertEquals("16 minutes", TimeEstimate.getTimeToCrackFormatted(result, "ONLINE_UNTHROTTLED"))
        assertEquals("13 minutes", TimeEstimate.getTimeToCrackFormatted(result, "OFFLINE_BCRYPT_14"))
        assertEquals("3 minutes", TimeEstimate.getTimeToCrackFormatted(result, "OFFLINE_BCRYPT_12"))
        assertEquals("50 seconds", TimeEstimate.getTimeToCrackFormatted(result, "OFFLINE_BCRYPT_10"))
    }

}
