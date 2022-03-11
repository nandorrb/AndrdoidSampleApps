package com.institutopacifico.actualidad.fragments

import com.institutopacifico.actualidad.utils.JulianDatesHelper
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_button_test.*

/**
 * Created by mobile on 7/12/17.
 * Fernando Rubio Burga
 */

internal class TestButtonFragment : android.support.v4.app.Fragment() {


    override fun onCreateView(inflater: android.view.LayoutInflater?, container: android.view.ViewGroup?, savedInstanceState: android.os.Bundle?): android.view.View? {
        val ViewGroup_root = inflater!!.inflate(com.institutopacifico.actualidad.R.layout.fragment_button_test, null) as android.view.ViewGroup


        return ViewGroup_root
    }


    override fun onResume() {
        super.onResume()
        button_fragment_button_test.setOnClickListener {
            var year: Int = 0
            val string_year: String = EditText_year_fragment_button_test.text.toString()
            if (string_year.isNotEmpty()) {
                try {
                    year = Integer.valueOf(string_year)
                } catch (e: Exception) {
                    Logger.e(e.message)
                }
            }
            var month: Int = 0
            val string_month: String = EditText_month_fragment_button_test.text.toString()
            if (string_month.isNotEmpty()) {
                try {
                    month = Integer.valueOf(string_month)
                } catch (e: Exception) {
                    Logger.e(e.message)
                }
            }
            var day: Int = 0
            val string_day: String = EditText_day_fragment_button_test.text.toString()
            if (string_day.isNotEmpty()) {
                try {
                    day = Integer.valueOf(string_day)
                } catch (e: Exception) {
                    Logger.e(e.message)
                }
            }
            Logger.i(" year:" + year.toString() +
                    " month:" + month.toString() +
                    " day:" + day.toString() +
                    " result: " + JulianDatesHelper.toJulian(year, month, day).toString()
            )

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val refWatcher = com.institutopacifico.actualidad.application.ApplicationClass.Companion.getRefWatcher(activity)
        refWatcher?.watch(this)
    }


}
