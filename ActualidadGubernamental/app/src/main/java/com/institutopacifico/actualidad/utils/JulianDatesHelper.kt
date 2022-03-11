package com.institutopacifico.actualidad.utils

import java.text.DecimalFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.TimeZone

/**
 * Created by mobile on 7/7/17.
 * Fernando Rubio Burga
 */

object JulianDatesHelper {
    internal var daytab = intArrayOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    internal var dayleap = intArrayOf(0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    /**
     * From a year, month, and day of the month
     * calculate the Julian day of year.  Parts stolen from K&R's C book.

     * @param ymd The year, month day as an array
     * *
     * @return The day of the year
     */
    fun doy_from_ymd(ymd: IntArray): Int {
        val yr = sanitizeYear(ymd[0])
        val leap = yr % 4 == 0 && yr % 100 != 0 || yr % 400 == 0 /* is it a leap year */
        /* printf("Mon=%s month=%d leap=%d\n",mn,month,leap);*/
        var day = ymd[2]
        if (leap)
            for (i in 1..ymd[1] - 1) day += dayleap[i] /* add up all the full months */
        else
            for (i in 1..ymd[1] - 1) day += daytab[i] /* add up all the full months */
        return day
    }

    /**
     * From a year, month, and day of the month
     * calculate the Julian day of year.  Parts stolen from K&R's C book.

     * @param yr  The year
     * *
     * @param mon a three didget Man (first 3 letters of english month
     * *
     * @param day day of the monthe
     * *
     * @return The day of the year
     */
    fun doy_from_ymd(yr: Int, mon: Int, day: Int): Int {
        var yr = yr
        var day = day
        yr = sanitizeYear(yr)
        val leap = yr % 4 == 0 && yr % 100 != 0 || yr % 400 == 0 /* is it a leap year */
        /* printf("Mon=%s month=%d leap=%d\n",mn,month,leap);*/
        if (leap)
            for (i in 1..mon - 1) day += dayleap[i] /* add up all the full months */
        else
            for (i in 1..mon - 1) day += daytab[i] /* add up all the full months */
        return day
    }

    /**
     * From a Unix 3 character month in ascii, a day of month, and year,
     * calculate the Julian day of year.  Parts stolen from K&R's C book.

     * @param yr  The year
     * *
     * @param mon a three didget Man (first 3 letters of english month
     * *
     * @param day day of the monthe
     * *
     * @return The day of the year
     */
    fun doy_from_ymd(yr: Int, mon: String, day: Int): Int {

        var month = -1
        if (mon.equals("Jan", ignoreCase = true)) month = 1    /* calculate month of year */
        if (mon.equals("Feb", ignoreCase = true)) month = 2
        if (mon.equals("Mar", ignoreCase = true)) month = 3
        if (mon.equals("Apr", ignoreCase = true)) month = 4
        if (mon.equals("May", ignoreCase = true)) month = 5
        if (mon.equals("Jun", ignoreCase = true)) month = 6
        if (mon.equals("Jul", ignoreCase = true)) month = 7
        if (mon.equals("Aug", ignoreCase = true)) month = 8
        if (mon.equals("Sep", ignoreCase = true)) month = 9
        if (mon.equals("Oct", ignoreCase = true)) month = 10
        if (mon.equals("Nov", ignoreCase = true)) month = 11
        if (mon.equals("Dec", ignoreCase = true)) month = 12
        return doy_from_ymd(yr, month, day)
    }

    /**
     * convert a year and day of year to an array in yr,mon,day order

     * @param yr  The year
     * *
     * @param doy The day of the year
     * *
     * @return an array in yr, mon, day
     * *
     * @throws RuntimeException ill formed especially doy being too big.
     */
    @Throws(RuntimeException::class)
    fun ymd_from_doy(yr: Int, doy: Int): IntArray {
        var yr = yr
        var j: Int
        var sum: Int
        yr = sanitizeYear(yr)
        val leap = yr % 4 == 0 && yr % 100 != 0 || yr % 400 == 0 /* is it a leap year */
        sum = 0
        val ymd = IntArray(3)
        ymd[0] = yr
        if (leap) {
            j = 1
            while (j <= 12) {
                if (sum < doy && sum + dayleap[j] >= doy) {
                    ymd[1] = j
                    ymd[2] = doy - sum
                    return ymd
                }
                sum += dayleap[j]
                j++
            }
        } else {
            j = 1
            while (j <= 12) {
                if (sum < doy && sum + daytab[j] >= doy) {
                    ymd[1] = j
                    ymd[2] = doy - sum
                    return ymd
                }
                sum += daytab[j]
                j++
            }
        }
        println("ymd_from_doy: impossible drop through!   yr=$yr doy=$doy")
        throw RuntimeException("ymd_from_DOY : impossible yr=$yr doy=$doy")

    }

    /**
     * sanitize year using the rule of 60, two digit ears >=60 = 1900+yr
     * years less than 60 are 2000+yr.  If its 4 digits already, just return it.

     * @param yr The year to sanitize
     * *
     * @return the year sanitized by rule of 60.
     */
    private fun sanitizeYear(yr: Int): Int {
        if (yr >= 100) return yr
        if (yr >= 60 && yr < 100)
            return yr + 1900
        else if (yr < 60 && yr >= 0) return yr + 2000
        println("Illegal year to sanitize =" + yr)
        return -1
    }

    /**
     * Returns the Julian day number that begins at noon of
     * this day, Positive year signifies A.D., negative year B.C.
     * Remember that the year after 1 B.C. was 1 A.D.  This is good
     * for translating day differences because it will calculate the
     * calendar days correctly.
     *
     *
     * ref :
     * Numerical Recipes in C, 2nd ed., Cambridge University Press 1992
     */

    // Gregorian Calendar adopted Oct. 15, 1582 (2299161)
    private val JGREG = 15 + 31 * (10 + 12 * 1582)
    private val HALFSECOND = 0.5

    /**
     * Given a year and day of year, return the true julian day

     * @param year The year, this will be run through sanitizeYear()
     * *
     * @param doy  The day of the year
     * *
     * @return The julian day
     */
    fun toJulian(year: Int, doy: Int): Int {
        val ymd = ymd_from_doy(year, doy)
        return toJulian(ymd[0], ymd[1], ymd[2])
    }

    /**
     * create the julian day from a GregorianCalendar

     * @param now the GregorianCalendar to manipulate
     * *
     * @return the julian day
     */
    fun toJulian(now: GregorianCalendar): Int {
        return toJulian(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1,
                now.get(Calendar.DAY_OF_MONTH))
    }

    /**
     * Convert a Date to a Julian date.

     * @param date the date to convert
     * *
     * @return the Julian date
     */
    fun toJulian(date: Date): Int {
        val cal: GregorianCalendar

        cal = GregorianCalendar(TimeZone.getTimeZone("GMT+0000"))
        cal.time = date

        return toJulian(cal)
    }

    /**
     * Convert a year, month, and day of month to a julian date

     * @param year  The year - it will be satitizedYear()
     * *
     * @param month The month
     * *
     * @param day   The day of the month.
     * *
     * @return the julian day
     */
    fun toJulian(year: Int, month: Int, day: Int): Int {

        var julianYear = sanitizeYear(year)
        if (year < 0) julianYear++
        var julianMonth = month
        if (month > 2) {
            julianMonth++
        } else {
            julianYear--
            julianMonth += 12
        }

        var julian: Double = 0.0
        julian += java.lang.Math.floor(365.25 * julianYear)
        julian += java.lang.Math.floor(30.6001 * julianMonth)
        julian += day
        julian += 1720982.0

        /*if (day + 31 * (month + 12 * year) >= JGREG) {
            // change over to Gregorian calendar
            int ja = (int)(0.01 * julianYear);
            julian += 2 - ja + (0.25 * ja);
        }
        */

        return (java.lang.Math.floor(julian.toDouble()) + .000001).toInt()
    }

    fun gregorianToJd(y: Int, m: Int, d: Int): Double {
        var y = y
        var m = m
        if (y < -4712) throw IllegalArgumentException("year ($y) < -4712")

        if (m <= 2) {
            m += 12
            y -= 1
        }
        val b: Double
        if (y > 1582 || y == 1582 && (m > 10 || m == 10 && d >= 15)) {
            // first gregorian is 15-oct-1582
            val a = Math.floor(y / 100.0)
            b = 2 + Math.floor(a / 4.0) - a
        } else { // invalid dates (5-14) are also considered as julian
            b = 0.0
        }
        val abs_jd = 1720994.5 + Math.floor(365.25 * y) + Math.floor(30.6001 * (m + 1))
        +d.toDouble() + b
        return abs_jd
    }

    /**
     * Converts a Julian day to a calendar date in a ymd array
     * ref :
     * Numerical Recipes in C, 2nd ed., Cambridge University Press 1992

     * @param injulian The julian date (a big number!)
     * *
     * @return An array of 3 ints representing the year, month and day in that order
     */
    fun fromJulian(injulian: Int): IntArray {

        val jalpha: Int
        var ja: Int
        val jb: Int
        val jc: Int
        val jd: Int
        val je: Int
        var year: Int
        var month: Int
        val day: Int
        val julian = injulian.toDouble() + HALFSECOND / 86400.0

        ja = injulian.toInt()
        if (ja >= JGREG) {
            jalpha = ((ja - 1867216 - 0.25) / 36524.25).toInt()
            ja = ja + 1 + jalpha - jalpha / 4
        }

        jb = ja + 1524
        jc = (6680.0 + (jb - 2439870 - 122.1) / 365.25).toInt()
        jd = 365 * jc + jc / 4
        je = ((jb - jd) / 30.6001).toInt()
        day = jb - jd - (30.6001 * je).toInt()
        month = je - 1
        if (month > 12) month = month - 12
        year = jc - 4715
        if (month > 2) year--
        if (year <= 0) year--

        return intArrayOf(year, month, day)
    }

    /**
     * for a julian day, create the file stub yyyy_doy

     * @param julian day
     * *
     * @return a string with yyyy_ddd
     */
    fun fileStub(julian: Int): String {
        val ymd = fromJulian(julian)
        val doy = doy_from_ymd(ymd[0], ymd[1], ymd[2])
        return fileStub(ymd[0], doy)
    }

    /**
     * for a year a doy, create the file stub yyyy_doy

     * @param year It will be put through rule of 60(sanitizeYear)
     * *
     * @param jday The day of  year
     * *
     * @return a string with yyyy_ddd
     */
    fun fileStub(year: Int, jday: Int): String {
        val df4 = DecimalFormat("0000")
        return df4.format(sanitizeYear(year).toLong()) + "_" + df4.format(jday.toLong()).substring(1, 4)
    }

    /**
     * Unit testing main routine - not used operationally

     * @param args - command line args
     */
    /* @JvmStatic fun main(args: Array<String>) {
         // FIRST TEST reference point
         println("Julian date for May 23, 1968 : " + toJulian(1968, 5, 23))
         // output : 2440000
         var results = fromJulian(toJulian(1968, 5, 23))
         println
         "... back to calendar : " + results[0] + " "
                 + results[1] + " " + results[2]

         println("Julian date for Oct 15, 1582 : " + toJulian(1582, 10, 15))
         // output : 2440000
         results = fromJulian(toJulian(1582, 10, 15))
         println
         "... back to calendar : " + results[0] + " "
                 + results[1] + " " + results[2]

         // SECOND TEST today
         val today = Calendar.getInstance()
         val todayJulian = toJulian(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1,
                 today.get(Calendar.DATE))
         println("Julian date for today : " + todayJulian)
         results = fromJulian(todayJulian)
         println
         "... back to calendar : " + results[0] + " " + results[1]
                 + " " + results[2]
         var jday = doy_from_ymd(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1,
                 today.get(Calendar.DATE))
         println("Today is julian day " + jday)
         println("Today is julian day " + toJulian(today.get(Calendar.YEAR), jday))
         // THIRD TEST
         val date1 = toJulian(2005, 1, 1)
         val date2 = toJulian(2005, 1, 31)
         println("Between 2005-01-01 and 2005-01-31 : "
                 + (date2 - date1) + " days")
         /*
      expected output :

      Julian date for May 23, 1968 : 2440000.0
      ... back to calendar 1968 5 23
      Julian date for today : 2453487.0
      ... back to calendar 2005 4 26
      Between 2005-01-01 and 2005-01-31 : 30.0 days
     */
         jday = doy_from_ymd(2005, 12, 1)
         println("2005-12-1 is julday=" + jday)
         jday = doy_from_ymd(2004, 12, 1)
         println("2004-12-1 is julday=" + jday)
         var ymd: IntArray
         ymd = ymd_from_doy(2005, 335)
         println("2005-335 is " + ymd[0] + "/" + ymd[1] + "/" + ymd[2])
         ymd = ymd_from_doy(2004, 335)
         println("2004-335 is " + ymd[0] + "/" + ymd[1] + "/" + ymd[2])
         println("2004-35 is file=" + fileStub(2004, 35))
     }
     */
}
