package com.institutopacifico.actualidad.modules.calendar.objects

import com.google.gson.annotations.SerializedName
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

/**
 * Created by mobile on 9/27/17 at 11:07.
 * Fernando Rubio Burga
 */
open class RichCalendarDateObject {
    @SerializedName("calendar_id")
    var string_id: String=""

    @SerializedName("calendar_category")
    var string_category: String=""

    @SerializedName("calendar_description")
    var string_description: String=""

    @SerializedName("calendar_date")
    var string_date: String = ""
}