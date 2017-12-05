package test.io.github.hanjoongcho.easypassword.utils

import io.github.hanjoongcho.utils.PasswordStrengthUtils
import org.junit.Assert.assertEquals
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
//@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class PasswordStrengthUtilsTest {

    companion object {
        val EXAMPLE_PASSWORD = "PInEApple#!1"
    }

    @Test
    fun test01() {
        assertEquals(
                "infinite (>100000 centuries)",
                PasswordStrengthUtils.getTimeToCrackFormatted(EXAMPLE_PASSWORD, PasswordStrengthUtils.ONLINE_THROTTLED)
        )
    }

    @Test
    fun test02() {
        assertEquals(
                "infinite (>100000 centuries)",
                PasswordStrengthUtils.getTimeToCrackFormatted(EXAMPLE_PASSWORD, PasswordStrengthUtils.ONLINE_UNTHROTTLED)
        )
    }

    @Test
    fun test03() {
        assertEquals(
                "infinite (>100000 centuries)",
                PasswordStrengthUtils.getTimeToCrackFormatted(EXAMPLE_PASSWORD, PasswordStrengthUtils.OFFLINE_BCRYPT_14)
        )
    }

    @Test
    fun test04() {
        assertEquals(
                "38019 centuries",
                PasswordStrengthUtils.getTimeToCrackFormatted(EXAMPLE_PASSWORD, PasswordStrengthUtils.OFFLINE_BCRYPT_12)
        )
    }

    @Test
    fun test05() {
        assertEquals(
                "9504 centuries",
                PasswordStrengthUtils.getTimeToCrackFormatted(EXAMPLE_PASSWORD, PasswordStrengthUtils.OFFLINE_BCRYPT_10)
        )
    }

    @Test
    fun test06() {
        assertEquals(118254590128096, PasswordStrengthUtils.getTimeToCrack(EXAMPLE_PASSWORD, PasswordStrengthUtils.OFFLINE_BCRYPT_12))
    }

    @Test
    fun test07() {
        assertEquals(4, PasswordStrengthUtils.getScore(EXAMPLE_PASSWORD))
    }
}
