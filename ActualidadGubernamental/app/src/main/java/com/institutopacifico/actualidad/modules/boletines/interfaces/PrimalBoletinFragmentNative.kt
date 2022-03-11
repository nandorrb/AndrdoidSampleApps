package com.institutopacifico.actualidad.modules.boletines.interfaces

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.institutopacifico.actualidad.modules.boletines.utils.BoletinUltimateRecyclerViewConfigurationHelper
import com.institutopacifico.actualidad.objects.ArticleObject
import com.institutopacifico.actualidad.objects.ResponseObject
import kotlinx.android.synthetic.main.fragment_default_native_recycler_view_with_floating_view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

open class PrimalBoletinFragmentNative : Fragment() {


    lateinit internal var Helper: BoletinUltimateRecyclerViewConfigurationHelper
    open var API: String = ApplicationClass.context.resources.getString(R.string.string_api_boletin_diario)
    open var FRAGMENT_TITLE: String = ApplicationClass.context.resources.getString(R.string.fragment_title_boletin_diario)
    open var BOLETIN_FILTER: String = ""
    lateinit var root: ViewGroup


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater!!.inflate(R.layout.fragment_default_native_recycler_view_with_floating_view, null) as ViewGroup
        //  FragmentTransactionHelper.toolbar.title = FRAGMENT_TITLE
        EventBus.getDefault().register(this)

        Helper = BoletinUltimateRecyclerViewConfigurationHelper(activity, API, BOLETIN_FILTER, lambda_starredItemUpdater, lambda_AsynchronousResponseProcessor)
        Helper.setLoadMoreToRecyclerView(recycler_view_simple_default_fragment_with_floating_view)
        setListeners()
        return root
    }

    private fun setListeners() {
      //  fab_production_native_recycler_view_floating_view?.setOnClickListener {
         //   fab_production_native_recycler_view_floating_view?.expand()
     //   }
        floating_view_show_starred_articles?.setOnClickListener {
            Helper.showStarredArticles()
        }
        floating_view_search_articles?.setOnClickListener {
            Helper.searchArticles()
        }

        //floating_view_calendar_search?.setOnClickListener {
        //      Helper.calendarSearch()
        //}
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun starredItemUpdater(currentObject: ArticleObject) {
        Helper.updateStarredItem(currentObject)
    }


    val lambda_starredItemUpdater = { currentObject: ArticleObject ->
        Helper.updateStarredItem(currentObject)
    }


    /*  @Subscribe
      fun AsynchronousResponseProcessor(ResponseObject_AsynchronousResponse: ResponseObject) {
          Helper.processAsynchronousRequest(ResponseObject_AsynchronousResponse,recycler_view_simple_default_fragment_with_floating_view)
          setTableLayoutListeners()
      }
  */

    val lambda_AsynchronousResponseProcessor = { ResponseObject_AsynchronousResponse: ResponseObject ->
        if(ResponseObject_AsynchronousResponse.bodyObject_WhatWeHaveRequested.string_request != Helper.DataLoader.STRING_MORE_DATA) {
            Helper.setLoadMoreToRecyclerView(recycler_view_simple_default_fragment_with_floating_view)
        }
        Helper.processAsynchronousRequest(ResponseObject_AsynchronousResponse, recycler_view_simple_default_fragment_with_floating_view)
        //  setTableLayoutListeners()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        val refWatcher = ApplicationClass.getRefWatcher(activity)
        refWatcher!!.watch(this)
        EventBus.getDefault().unregister(this)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        //Helper
        setListeners()
    }

}