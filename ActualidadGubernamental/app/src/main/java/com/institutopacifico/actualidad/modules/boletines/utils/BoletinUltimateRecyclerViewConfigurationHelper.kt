package com.institutopacifico.actualidad.modules.boletines.utils

import android.app.Activity
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.institutopacifico.actualidad.modules.boletines.adapters.BoletinNativeRecyclerViewAdapter
import com.institutopacifico.actualidad.modules.boletines.adapters.EndlessRecyclerViewScrollListener
import com.institutopacifico.actualidad.modules.boletines.interfaces.PrimalBoletinFragmentNative
import com.institutopacifico.actualidad.objects.ArticleObject
import com.institutopacifico.actualidad.objects.ResponseObject
import com.institutopacifico.actualidad.objects.UserDataSingleton
import com.institutopacifico.actualidad.utils.FragmentTransactionHelper
import com.institutopacifico.actualidad.utils.NotificationsHelper
import com.orhanobut.logger.Logger
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.*


class BoletinUltimateRecyclerViewConfigurationHelper
(val CurrentActivity: Activity, val API: String, val BOLETIN_FILTER: String, lambda_starredItemUpdater: (ArticleObject) -> Unit, lambda_AsynchronousResponseProcessor: (ResponseObject) -> Unit) {
    private var simpleRecyclerViewAdapter: BoletinNativeRecyclerViewAdapter
    var DataLoader: BoletinUltimateRecyclerViewDataLoader
    private var linearLayoutManager: LinearLayoutManager
    internal var simplifiedDateFormat: SimpleDateFormat
    internal var QueryDateFormat: SimpleDateFormat
    //  internal var JulianDateFormat: SimpleDateFormat
    internal var string_search_query: String = ""
    internal var calendarDatePicker: Calendar
    //internal val headersDecor: StickyRecyclerHeadersDecoration
    internal var Boolean_LoadMoreAlreadyRequested: Boolean = false

    var edit_text_dialog_custom_search_query: EditText? = null
    var EditText_dialog_custom_search_first_date: EditText? = null
    var EditText_dialog_custom_search_second_date: EditText? = null
    var CheckBox_Activate_AdvancedSearch: EditText? = null
    var AdvancedSearch: EditText? = null


    fun updateStarredItem(currentObject: ArticleObject) {
        simpleRecyclerViewAdapter.updateStarredItem(currentObject)
    }

    fun showStarredArticles() {
        if (UserDataSingleton.userData.stringInstitutoPacificoUserId.isNotEmpty()) {
            //   DataLoader.clearFILTERS()
            //   DataLoader.setFILTERS(ApplicationClass.context.getString(R.string.constant_starred_articles_query))
            //    DataLoader.loadInitialBoletinValues()
            SetFiltersAndGoToFragment(ApplicationClass.context.getString(R.string.constant_starred_articles_query))

        } else {
            NotificationsHelper.PleaseLogInMessage(CurrentActivity)
        }

    }

    fun searchArticles() {
        string_search_query = ""

        val dialogBuilder = AlertDialog.Builder(CurrentActivity)

        val inflater = CurrentActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_custom_search, null)
        dialogBuilder.setView(dialogView)

        //Set Listeners
        val edit_text_dialog_custom_search_query = dialogView.findViewById<View>(R.id.edit_text_dialog_custom_search_query) as EditText
        val EditText_dialog_custom_search_first_date = dialogView.findViewById<View>(R.id.edit_text_dialog_custom_search_first_date) as EditText
        val EditText_dialog_custom_search_second_date = dialogView.findViewById<View>(R.id.edit_text_dialog_custom_search_second_date) as EditText
        val CheckBox_Activate_AdvancedSearch = dialogView.findViewById<View>(R.id.check_box_dialog_custom_search_advanced_search) as CheckBox
        val AdvancedSearch = dialogView.findViewById<View>(R.id.linear_layout_dialog_custom_search_advanced_search) as LinearLayout

        EditText_dialog_custom_search_first_date.setOnClickListener {
            calendarDatePickerRequester(EditText_dialog_custom_search_first_date)
        }

        EditText_dialog_custom_search_second_date.setOnClickListener {
            calendarDatePickerRequester(EditText_dialog_custom_search_second_date)
        }

        CheckBox_Activate_AdvancedSearch.visibility = View.VISIBLE

        CheckBox_Activate_AdvancedSearch.setOnClickListener {
            if (CheckBox_Activate_AdvancedSearch.isChecked) {
                AdvancedSearch.visibility = View.VISIBLE
            } else {
                AdvancedSearch.visibility = View.GONE
            }
        }

        dialogBuilder.setPositiveButton("OK") { _, _ ->
            if (CheckBox_Activate_AdvancedSearch.isChecked) {
                try {
                    /*   val gregorianCalendar_FirstDate = GregorianCalendar()
                       gregorianCalendar_FirstDate.set(GregorianCalendar.DAY_OF_MONTH, simplifiedDateFormat.parse(EditText_dialog_custom_search_first_date.text.toString()).day)
                       gregorianCalendar_FirstDate.set(GregorianCalendar.MONTH, simplifiedDateFormat.parse(EditText_dialog_custom_search_first_date.text.toString()).month)
                       gregorianCalendar_FirstDate.set(GregorianCalendar.YEAR, simplifiedDateFormat.parse(EditText_dialog_custom_search_first_date.text.toString()).year)

                       val gregorianCalendar_SecondDate = GregorianCalendar()
                       gregorianCalendar_SecondDate.set(GregorianCalendar.DAY_OF_MONTH, simplifiedDateFormat.parse(EditText_dialog_custom_search_second_date.text.toString()).day)
                       gregorianCalendar_SecondDate.set(GregorianCalendar.MONTH, simplifiedDateFormat.parse(EditText_dialog_custom_search_second_date.text.toString()).month)
                       gregorianCalendar_SecondDate.set(GregorianCalendar.YEAR, simplifiedDateFormat.parse(EditText_dialog_custom_search_second_date.text.toString()).year)
   **/
                    // var a: Calendar = gregorianCalendar_FirstDate
                    //    a.time
                    string_search_query = edit_text_dialog_custom_search_query.text.toString() +
                            " " + ApplicationClass.context.getString(R.string.constant_date_query) + EditText_dialog_custom_search_first_date.text.toString() +
                            /* JulianDatesHelper.toJulian(simplifiedDateFormat.parse(EditText_dialog_custom_search_first_date.text.toString()).year,
                                     simplifiedDateFormat.parse(EditText_dialog_custom_search_first_date.text.toString()).month,
                                     simplifiedDateFormat.parse(EditText_dialog_custom_search_first_date.text.toString()).day
                             )*/

                            "-" + EditText_dialog_custom_search_second_date.text.toString()
                    /*   JulianDatesHelper.gregorianToJd(simplifiedDateFormat.parse(EditText_dialog_custom_search_second_date.text.toString()).year,
                               simplifiedDateFormat.parse(EditText_dialog_custom_search_second_date.text.toString()).month,
                               simplifiedDateFormat.parse(EditText_dialog_custom_search_second_date.text.toString()).day)*/
                } catch (e: Exception) {
                    Logger.e(e.message)
                }

            } else {
                string_search_query = edit_text_dialog_custom_search_query.text.toString()
            }

            //   DataLoader.clearFILTERS()
            //  DataLoader.setFILTERS(string_search_query)
            // DataLoader.loadInitialBoletinValues()

            SetFiltersAndGoToFragment(string_search_query)


        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        val alertDialog = dialogBuilder.create()

        alertDialog.show()
    }

    private fun SetFiltersAndGoToFragment(NEW_FILTER: String) {

        val aa = PrimalBoletinFragmentNative()
        aa.BOLETIN_FILTER = NEW_FILTER
        aa.API = this.API
        FragmentTransactionHelper.goToFragment(aa, CurrentActivity)
    }

    /* fun calendarSearch() {
         string_search_query = ""
         // string_search_query = ApplicationClass.Companion.getContext().getString(R.string.constant_date_query) + JulianDateFormat.format(cal.getTime());
         calendarDatePickerRequester(CurrentActivity.getString(R.string.SEARCH_PATTERN_CALENDAR_SINGLE_DATE_SEARCH))
     }

     private fun calendarDatePickerRequester(string_the_identity_of_the_requester: String) {
         val dpd = DatePickerDialog.newInstance(
                 object : DatePickerDialog.OnDateSetListener {
                     override fun onDateSet(datePickerDialog: DatePickerDialog, i: Int, i1: Int, i2: Int) {

                         val cal = GregorianCalendar()
                         cal.set(Calendar.YEAR, i)
                         cal.set(Calendar.MONTH, i1)
                         cal.set(Calendar.DAY_OF_MONTH, i2)

                         processrequestedDate(cal.time, string_the_identity_of_the_requester)

                     }

                     private fun processrequestedDate(dateTime: Date, identity: String) {
                         if (identity === CurrentActivity.getString(R.string.SEARCH_PATTERN_CALENDAR_SINGLE_DATE_SEARCH)) {
                             string_search_query = ""
                             string_search_query = ApplicationClass.context.getString(R.string.constant_date_query) + QueryDateFormat.format(dateTime)
                             // DataLoader.clearFILTERS()
                             //   DataLoader.setFILTERS(string_search_query)
                             // DataLoader.loadInitialBoletinValues()
                             SetFiltersAndGoToFragment(string_search_query)

                         } else if (identity === CurrentActivity.getString(R.string.SEARCH_PATTERN_CALENDAR_DUAL_DATE_SEARCH_FIRST_DATE)) {
                             EditText_dialog_custom_search_first_date?.setText(QueryDateFormat.format(dateTime))

                         } else if (identity === CurrentActivity.getString(R.string.SEARCH_PATTERN_CALENDAR_DUAL_DATE_SEARCH_SECOND_DATE)) {
                             EditText_dialog_custom_search_second_date?.setText(QueryDateFormat.format(dateTime))
                         } else {
                             Logger.w("Warning, unknown Identity:" + identity)
                         }

                     }
                 },
                 calendarDatePicker.get(Calendar.YEAR),
                 calendarDatePicker.get(Calendar.MONTH),
                 calendarDatePicker.get(Calendar.DAY_OF_MONTH)
         )
         dpd.show(CurrentActivity.fragmentManager, CurrentActivity.getString(R.string.DATE_PICKER_DIALOG_TAG))
     }
 */
    private fun calendarDatePickerRequester(EditText_IdentityOfRequester: EditText) {
        val dpd = DatePickerDialog.newInstance(
                object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(datePickerDialog: DatePickerDialog, i: Int, i1: Int, i2: Int) {

                        val cal = GregorianCalendar()
                        cal.set(Calendar.YEAR, i)
                        cal.set(Calendar.MONTH, i1)
                        cal.set(Calendar.DAY_OF_MONTH, i2)

                        processrequestedDate(cal.time, EditText_IdentityOfRequester)

                    }

                    private fun processrequestedDate(dateTime: Date, identity: EditText) {

                        //  string_search_query = ""
                        //    string_search_query = ApplicationClass.context.getString(R.string.constant_date_query) + QueryDateFormat.format(dateTime)
                        //   DataLoader.clearFILTERS()
                        //   DataLoader.setFILTERS(string_search_query)
                        //  DataLoader.loadInitialBoletinValues()
                        // SetFiltersAndGoToFragment(string_search_query)

                        identity.setText(QueryDateFormat.format(dateTime))
                    }
                },
                calendarDatePicker.get(Calendar.YEAR),
                calendarDatePicker.get(Calendar.MONTH),
                calendarDatePicker.get(Calendar.DAY_OF_MONTH)
        )
        dpd.show(CurrentActivity.fragmentManager, CurrentActivity.getString(R.string.DATE_PICKER_DIALOG_TAG))
    }

    /* fun processAsynchronousRequest(responseObject_asynchronousResponse: ResponseObject, ultimateRecyclerView: UltimateRecyclerView?) {
         //   ultimateRecyclerView?.disableLoadmore();
         //  ultimateRecyclerView?.disableLoadmore();
         ultimateRecyclerView?.setHasFixedSize(false)
         ultimateRecyclerView?.layoutManager = linearLayoutManager
         ultimateRecyclerView?.addItemDecoration(headersDecor)
         ultimateRecyclerView?.setLoadMoreView(LayoutInflater.from(CurrentActivity)
                 .inflate(R.layout.production_ultimate_recycler_view_custom_bottom_progressbar, null))
         ultimateRecyclerView?.setDefaultOnRefreshListener {
             //  ultimateRecyclerView?.reenableLoadmore();
             if (simpleRecyclerViewAdapter.boletinItemsViews.isNotEmpty()) {
                 DataLoader.refresh_AKA_NewData(simpleRecyclerViewAdapter?.boletinItemsViews[0])
             } else {

                 DataLoader.clearFILTERS()
                 if (BOLETIN_FILTER.isNotEmpty()) {
                     DataLoader.setFILTERS(BOLETIN_FILTER)
                 }
                 DataLoader.loadInitialBoletinValues()
             }
         }
         ultimateRecyclerView?.reenableLoadmore()
         ultimateRecyclerView?.setAdapter(simpleRecyclerViewAdapter)
         ultimateRecyclerView?.setOnLoadMoreListener { itemsCount, maxLastVisiblePosition ->
             // ultimateRecyclerView?.disableLoadmore();
             if (simpleRecyclerViewAdapter.boletinItemsViews.isNotEmpty()) {
                 //      Logger.i(String.valueOf(simpleRecyclerViewAdapter.getBoletinItemsViews().size() - 1));
                 DataLoader.moreData(simpleRecyclerViewAdapter.boletinItemsViews[simpleRecyclerViewAdapter.boletinItemsViews.size - 1])
             } else {

                 DataLoader.clearFILTERS()
                 if (BOLETIN_FILTER.isNotEmpty()) {
                     DataLoader.setFILTERS(BOLETIN_FILTER)
                 }
                 DataLoader.loadInitialBoletinValues()
             }
             //   ultimateRecyclerView?.disableLoadmore();
         }
         ultimateRecyclerView?.showFloatingButtonView()

         if (responseObject_asynchronousResponse.receivedData.isNotEmpty()) {
             //DISABLE LOAD MORE
             ultimateRecyclerView?.disableLoadmore()

             if (responseObject_asynchronousResponse.bodyObject_WhatWeHaveRequested.string_request == DataLoader.LOAD_INITIAL_VALUES) {

                 simpleRecyclerViewAdapter.clear()


                 for (boletin_view in responseObject_asynchronousResponse.receivedData) {
                     simpleRecyclerViewAdapter.insert(boletin_view, 0)
                 }
                 linearLayoutManager.scrollToPosition(0)

             } else if (responseObject_asynchronousResponse.bodyObject_WhatWeHaveRequested.string_request == DataLoader.STRING_MORE_DATA) {

                 for (boletin_view in responseObject_asynchronousResponse.receivedData) {
                     simpleRecyclerViewAdapter.insert(boletin_view, simpleRecyclerViewAdapter.adapterItemCount)
                 }

                 //Prevents Exception
                 ultimateRecyclerView?.post { simpleRecyclerViewAdapter.notifyItemInserted(simpleRecyclerViewAdapter.itemCount - 1) }

             } else if (responseObject_asynchronousResponse.bodyObject_WhatWeHaveRequested.string_request == DataLoader.REFRESH) {

                 for (boletin_view in responseObject_asynchronousResponse.receivedData) {
                     simpleRecyclerViewAdapter.insert(boletin_view, 0)
                 }

                 linearLayoutManager.scrollToPosition(0)
             }

             //Ordenamiento por fecha
             simpleRecyclerViewAdapter.sort()

             //Ordenamiento por noticia
             if (responseObject_asynchronousResponse.bodyObject_WhatWeHaveRequested.string_request == DataLoader.LOAD_INITIAL_VALUES && !responseObject_asynchronousResponse.receivedData[0].string_article_category.isEmpty()) {
                 simpleRecyclerViewAdapter.sortNewsFirst()
             }
             simpleRecyclerViewAdapter.notifyDataSetChanged()

             //REENABLE LOAD MORE
             ultimateRecyclerView?.reenableLoadmore()
         } else {
             if (responseObject_asynchronousResponse.bodyObject_WhatWeHaveRequested.string_request == DataLoader.LOAD_INITIAL_VALUES ||
                     responseObject_asynchronousResponse.bodyObject_WhatWeHaveRequested.string_request == DataLoader.STRING_MORE_DATA ||
                     responseObject_asynchronousResponse.bodyObject_WhatWeHaveRequested.string_request == DataLoader.REFRESH
                     ) {
                 Toasty.normal(CurrentActivity, "Lista Vacía").show()
             }
         }


         ultimateRecyclerView?.setRefreshing(false)


         //ExceptionPreventer
         if ((responseObject_asynchronousResponse.bodyObject_WhatWeHaveRequested.string_request == DataLoader.STRING_MORE_DATA || responseObject_asynchronousResponse.bodyObject_WhatWeHaveRequested.string_request == DataLoader.LOAD_INITIAL_VALUES) && responseObject_asynchronousResponse.receivedData.size < ApplicationClass.context.resources.getInteger(R.integer.integer_RecyclerViewInitialValuesStep)) {
             //There isn't more data! It's over!
             BoletinUltimateRecyclerViewDisableLoadMoreSingleton.Companion.instance.boolean_disable_more_data = true
         }

         if (BoletinUltimateRecyclerViewDisableLoadMoreSingleton.Companion.instance.boolean_disable_more_data) {
             ultimateRecyclerView?.disableLoadmore()
         }

     }
 */
    fun processAsynchronousRequest(responseObject_asynchronousResponse: ResponseObject, recyclerView: RecyclerView?) {
        /* recyclerView?.setHasFixedSize(false)
         recyclerView?.layoutManager = linearLayoutManager
         //   recyclerView?.addItemDecoration(headersDecor)

         val scrollListener: EndlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener() {
             override
             fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                 // Triggered only when new data needs to be appended to the list
                 // Add whatever code is needed to append new items to the bottom of the listx
                 if (Boolean_LoadMoreAlreadyRequested.not()) {
                     if (simpleRecyclerViewAdapter.boletinItemsViews.isNotEmpty()) {
                        // Logger.i("number of items:"+simpleRecyclerViewAdapter.boletinItemsViews.size.toString())
                         DataLoader.moreData(simpleRecyclerViewAdapter.boletinItemsViews[simpleRecyclerViewAdapter.boletinItemsViews.size - 1])

                     } else {
                         DataLoader.clearFILTERS()
                         if (BOLETIN_FILTER.isNotEmpty()) {
                             DataLoader.setFILTERS(BOLETIN_FILTER)
                         }
                         DataLoader.loadInitialBoletinValues()
                     }

                     Boolean_LoadMoreAlreadyRequested = true
                     //  Toast.makeText(this, "Presione atras de nuevo para salir.", Toast.LENGTH_SHORT).show()

                     Handler().postDelayed({ Boolean_LoadMoreAlreadyRequested = false }, 1500)

                 }

             }
         }
         scrollListener.mLayoutManager = linearLayoutManager

         recyclerView?.addOnScrollListener(scrollListener)
 */
        /* recyclerView?.setLoadMoreView(LayoutInflater.from(CurrentActivity).inflate(R.layout.production_ultimate_recycler_view_custom_bottom_progressbar, null))
           recyclerView?.setDefaultOnRefreshListener {
               //  recyclerView?.reenableLoadmore();
               if (simpleRecyclerViewAdapter.boletinItemsViews.isNotEmpty()) {
                   DataLoader.refresh_AKA_NewData(simpleRecyclerViewAdapter?.boletinItemsViews[0])
               } else {

                   DataLoader.clearFILTERS()
                   if (BOLETIN_FILTER.isNotEmpty()) {
                       DataLoader.setFILTERS(BOLETIN_FILTER)
                   }
                   DataLoader.loadInitialBoletinValues()
               }
           }
           recyclerView?.reenableLoadmore()
        recyclerView?.adapter = simpleRecyclerViewAdapter
        */
        /*recyclerView?.setOnLoadMoreListener { itemsCount, maxLastVisiblePosition ->
            // recyclerView?.disableLoadmore();
            if (simpleRecyclerViewAdapter.boletinItemsViews.isNotEmpty()) {
                //      Logger.i(String.valueOf(simpleRecyclerViewAdapter.getBoletinItemsViews().size() - 1));
                DataLoader.moreData(simpleRecyclerViewAdapter.boletinItemsViews[simpleRecyclerViewAdapter.boletinItemsViews.size - 1])
            } else {

                DataLoader.clearFILTERS()
                if (BOLETIN_FILTER.isNotEmpty()) {
                    DataLoader.setFILTERS(BOLETIN_FILTER)
                }
                DataLoader.loadInitialBoletinValues()
            }
            //   recyclerView?.disableLoadmore();
        }
        recyclerView?.showFloatingButtonView()*/

        if (responseObject_asynchronousResponse.receivedData.isNotEmpty()) {
            //DISABLE LOAD MORE
            // recyclerView?.disableLoadmore()

            if (responseObject_asynchronousResponse.bodyObject_WhatWeHaveRequested.string_request == DataLoader.LOAD_INITIAL_VALUES) {

                simpleRecyclerViewAdapter.clear()


                for (boletin_view in responseObject_asynchronousResponse.receivedData) {
                    simpleRecyclerViewAdapter.insert(boletin_view, 0)
                }
                linearLayoutManager.scrollToPosition(0)


            } else if (responseObject_asynchronousResponse.bodyObject_WhatWeHaveRequested.string_request == DataLoader.STRING_MORE_DATA) {

                for (boletin_view in responseObject_asynchronousResponse.receivedData) {
                    simpleRecyclerViewAdapter.insert(boletin_view, simpleRecyclerViewAdapter.itemCount)
                }

                //Prevents Exception
                recyclerView?.post { simpleRecyclerViewAdapter.notifyItemInserted(simpleRecyclerViewAdapter.itemCount - 1) }

            } else if (responseObject_asynchronousResponse.bodyObject_WhatWeHaveRequested.string_request == DataLoader.REFRESH) {

                for (boletin_view in responseObject_asynchronousResponse.receivedData) {
                    simpleRecyclerViewAdapter.insert(boletin_view, 0)
                }

                linearLayoutManager.scrollToPosition(0)

            }

            //Ordenamiento por fecha
            simpleRecyclerViewAdapter.sort()

            //Ordenamiento por noticia
            if (responseObject_asynchronousResponse.bodyObject_WhatWeHaveRequested.string_request == DataLoader.LOAD_INITIAL_VALUES && !responseObject_asynchronousResponse.receivedData[0].string_article_category.isEmpty()) {
                simpleRecyclerViewAdapter.sortNewsFirst()
            }
            simpleRecyclerViewAdapter.notifyDataSetChanged()

            //REENABLE LOAD MORE
            //  recyclerView?.reenableLoadmore()
        } else {
            if (responseObject_asynchronousResponse.bodyObject_WhatWeHaveRequested.string_request == DataLoader.LOAD_INITIAL_VALUES
            //    responseObject_asynchronousResponse.bodyObject_WhatWeHaveRequested.string_request == DataLoader.STRING_MORE_DATA ||
            //   responseObject_asynchronousResponse.bodyObject_WhatWeHaveRequested.string_request == DataLoader.REFRESH
                    ) {
                Toasty.normal(CurrentActivity, "Lista Vacía").show()
            }
        }


        //   recyclerView?.setRefreshing(false)


        //ExceptionPreventer
        if ((responseObject_asynchronousResponse.bodyObject_WhatWeHaveRequested.string_request == DataLoader.STRING_MORE_DATA || responseObject_asynchronousResponse.bodyObject_WhatWeHaveRequested.string_request == DataLoader.LOAD_INITIAL_VALUES) && responseObject_asynchronousResponse.receivedData.size < ApplicationClass.context.resources.getInteger(R.integer.integer_RecyclerViewInitialValuesStep)) {
            //There isn't more data! It's over!
            BoletinUltimateRecyclerViewDisableLoadMoreSingleton.Companion.instance.boolean_disable_more_data = true
        }

        if (BoletinUltimateRecyclerViewDisableLoadMoreSingleton.Companion.instance.boolean_disable_more_data) {
            //     recyclerView?.disableLoadmore()
        }


    }

    fun setLoadMoreToRecyclerView(recyclerView: RecyclerView?) {
        recyclerView?.setHasFixedSize(false)
        recyclerView?.layoutManager = linearLayoutManager
        //   recyclerView?.addItemDecoration(headersDecor)

        val scrollListener: EndlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener() {
            override
            fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                if (Boolean_LoadMoreAlreadyRequested.not()) {
                    if (simpleRecyclerViewAdapter.boletinItemsViews.isNotEmpty()) {
                        // Logger.i("number of items:"+simpleRecyclerViewAdapter.boletinItemsViews.size.toString())
                        DataLoader.moreData(simpleRecyclerViewAdapter.boletinItemsViews[simpleRecyclerViewAdapter.boletinItemsViews.size - 1])

                    } else {
                        DataLoader.clearFILTERS()
                        if (BOLETIN_FILTER.isNotEmpty()) {
                            DataLoader.setFILTERS(BOLETIN_FILTER)
                        }
                        DataLoader.loadInitialBoletinValues()
                    }

                    Boolean_LoadMoreAlreadyRequested = true
                    //  Toast.makeText(this, "Presione atras de nuevo para salir.", Toast.LENGTH_SHORT).show()

                    Handler().postDelayed({ Boolean_LoadMoreAlreadyRequested = false }, 1500)
                }
            }
        }
        scrollListener.mLayoutManager = linearLayoutManager

        recyclerView?.addOnScrollListener(scrollListener)
//Set Adapter
        recyclerView?.adapter = simpleRecyclerViewAdapter
    }

    init {
        DataLoader = BoletinUltimateRecyclerViewDataLoader(API, CurrentActivity, lambda_AsynchronousResponseProcessor)
        DataLoader.clearFILTERS()
        if (BOLETIN_FILTER.isNotEmpty()) {
            DataLoader.setFILTERS(BOLETIN_FILTER)
        }
        DataLoader.loadInitialBoletinValues()
        BoletinUltimateRecyclerViewDisableLoadMoreSingleton.Companion.instance.boolean_disable_more_data = false
        simpleRecyclerViewAdapter = BoletinNativeRecyclerViewAdapter(listOf(), lambda_starredItemUpdater)
        //    headersDecor = StickyRecyclerHeadersDecoration(simpleRecyclerViewAdapter)
        linearLayoutManager = LinearLayoutManager(CurrentActivity)
        /*    ultimateRecyclerView?.setHasFixedSize(false)
            ultimateRecyclerView?.layoutManager = linearLayoutManager
            ultimateRecyclerView?.addItemDecoration(headersDecor)
            ultimateRecyclerView?.setLoadMoreView(LayoutInflater.from(CurrentActivity)
                    .inflate(R.layout.production_ultimate_recycler_view_custom_bottom_progressbar, null))
            ultimateRecyclerView?.setDefaultOnRefreshListener {
                //  ultimateRecyclerView?.reenableLoadmore();
                if (simpleRecyclerViewAdapter.boletinItemsViews.isNotEmpty()) {
                    DataLoader.refresh_AKA_NewData(simpleRecyclerViewAdapter?.boletinItemsViews[0])
                } else {
                    DataLoader.loadInitialBoletinValues()
                }
            }
            ultimateRecyclerView?.reenableLoadmore()
            ultimateRecyclerView?.setAdapter(simpleRecyclerViewAdapter)
            ultimateRecyclerView?.setOnLoadMoreListener { itemsCount, maxLastVisiblePosition ->
                // ultimateRecyclerView?.disableLoadmore();
                if (simpleRecyclerViewAdapter.boletinItemsViews.isNotEmpty()) {
                    //      Logger.i(String.valueOf(simpleRecyclerViewAdapter.getBoletinItemsViews().size() - 1));
                    DataLoader.moreData(simpleRecyclerViewAdapter.boletinItemsViews[simpleRecyclerViewAdapter.boletinItemsViews.size - 1])
                } else {
                    DataLoader.loadInitialBoletinValues()
                }
                //   ultimateRecyclerView?.disableLoadmore();
            }
            ultimateRecyclerView?.showFloatingButtonView()
            */
        calendarDatePicker = Calendar.getInstance()
        simplifiedDateFormat = SimpleDateFormat(ApplicationClass.context.getString(R.string.date_format_simplified))
        QueryDateFormat = SimpleDateFormat(ApplicationClass.context.getString(R.string.date_format_for_query))
        //JulianDateFormat = SimpleDateFormat(ApplicationClass.context.getString(R.string.date_format_gregorian))
    }


}
