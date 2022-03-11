package com.institutopacifico.actualidad.utils

import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.orhanobut.logger.Logger

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * Created by mobile on 6/1/17.
 */

object DateHelper {

    fun isThisDateFromToday(givenDate: Date): String {

        var returnString = ""

        val date = Date()

        val calendarDate = Calendar.getInstance()
        calendarDate.time = givenDate

        val midnight = Calendar.getInstance()
        midnight.set(Calendar.HOUR_OF_DAY, 0)
        midnight.set(Calendar.MINUTE, 0)
        midnight.set(Calendar.SECOND, 0)
        midnight.set(Calendar.MILLISECOND, 0)

        if (calendarDate.compareTo(midnight) >= 0) {
            val long_how_much_time_has_passed = Math.abs(date.time - givenDate.time)
            val diffHours = long_how_much_time_has_passed / (60 * 60 * 1000)
            returnString = "Hace "
            if (diffHours > 0) {
                returnString += diffHours.toString() + " horas"
            } else {
                val diff_minutes = long_how_much_time_has_passed / (60 * 1000)
                returnString += diff_minutes.toString() + " min"
            }
        } else {
            val formatter = SimpleDateFormat(ApplicationClass.context.getString(R.string.date_format))
            try {
                returnString = formatter.format(givenDate)
            } catch (e: Exception) {
                Logger.e(e.message)
            }

        }
        return returnString
    }
}/* private static final DateHelper ourInstance = new DateHelper();

    public static DateHelper getInstance() {
        return ourInstance;
    }
*/
