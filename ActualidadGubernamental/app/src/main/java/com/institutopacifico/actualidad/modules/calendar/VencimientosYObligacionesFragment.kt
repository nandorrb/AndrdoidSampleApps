package com.institutopacifico.actualidad.modules.calendar

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.institutopacifico.actualidad.modules.calendar.objects.EventDecorator
import com.institutopacifico.actualidad.network.IonObject
import com.institutopacifico.actualidad.objects.BodyObject
import com.institutopacifico.actualidad.objects.ResponseObject
import com.institutopacifico.actualidad.objects.UserDataSingleton
import com.institutopacifico.actualidad.objects.UserObject
import com.orhanobut.logger.Logger
import com.prolificinteractive.materialcalendarview.CalendarDay
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_default_calendar.*
import java.text.SimpleDateFormat

/**
 * Created by mobile on 9/22/17 at 15:51.
 * Fernando Rubio Burga
 */
open class VencimientosYObligacionesFragment : Fragment() {
    open var int_drawable: Int = R.drawable.ic_instituto_pacifico_calendario_dinamic_24dp
    var body: BodyObject = BodyObject()
    open var columns: Int = 3
    lateinit var root: ViewGroup
    private var SharedPreferences: SharedPreferences = ApplicationClass.context.getSharedPreferences(ApplicationClass.context.getString(R.string.app_shared_preferences_key), Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = SharedPreferences.edit()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater?.inflate(R.layout.fragment_default_calendar, null) as ViewGroup
        requestData()
        return root
    }

    override fun onResume() {
        super.onResume()
        //    requestData()
    }

    fun requestData() {
        val body: BodyObject = BodyObject()
        body.boolean_compressed = true
        IonObject().makeAsynchronousCompressedRequest(root.context, "calendar", body, AsynchronousResponse)
    }

    fun getDates(ResponseObject_AsynchronousResponse: ResponseObject): MutableList<CalendarDay> {
        var a: MutableList<CalendarDay> = mutableListOf()
        //   var fechas: MutableList<Int> = mutableListOf()
        var date: CalendarDay
        val formatter = SimpleDateFormat("yyyy-MM-dd");

        for (RichCalendarDateObject_RichDate in ResponseObject_AsynchronousResponse.array_of_dates) {
            date = CalendarDay.from(formatter.parse(RichCalendarDateObject_RichDate.string_date))
            a.add(date)
        }

        return a
    }


    val AsynchronousResponse = { ResponseObject_AsynchronousResponse: ResponseObject ->

        val a = getDates(ResponseObject_AsynchronousResponse)

        MaterialCalendarView_default_view.addDecorator(EventDecorator(resources.getColor(R.color.colorPrimaryDark), a, 20))
        MaterialCalendarView_default_view.setOnDateChangedListener { _, date, _ ->
            var b = ""
            if (a.contains(date)) {

                for ((position, fechas) in a.withIndex()) {
                    if (fechas.date.compareTo(date.date) == 0) {
                        b += " * " + ResponseObject_AsynchronousResponse.array_of_dates[position].string_category +
                                " " +
                                ResponseObject_AsynchronousResponse.array_of_dates[position].string_description + " \n"
                    }
                }
            }
            textView_fragment_defaul_calendar_description?.text = b
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        //  EventBus.getDefault().unregister(this)
        ApplicationClass.getRefWatcher(activity)?.watch(this)
    }


}