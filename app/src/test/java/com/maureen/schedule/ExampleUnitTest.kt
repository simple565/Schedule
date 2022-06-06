package com.maureen.schedule

import com.maureen.schedule.utils.convertToDate
import org.junit.Assert
import org.junit.Test
import java.lang.Exception
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Throws

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    @Throws(Exception::class)
    fun addition_isCorrect() {
        Assert.assertEquals(4, (2 + 2).toLong())
        //println(Date().toString())
        //print(DateFormat.getDateInstance().format(Date()))
        println(SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA).format(System.currentTimeMillis()))
        println(formatTime(System.currentTimeMillis()))
        //1636892145074
        println(convertToStamp("2021-11-14 20:15:45"))
    }

    fun formatTime(time: Long): String {
        val times: String = if (time.toString().length > 10) {
            // 13位的秒级别的时间戳
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(time)
        } else {
            // 10位的秒级别的时间戳
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date(time * 1000))
        }
        return times
    }

    fun convertToStamp(time: String): Long {
        var timestamp = 0L
        try {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = format.parse(time)
            date?.let {
                timestamp = it.time / 1000
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return timestamp
    }
}