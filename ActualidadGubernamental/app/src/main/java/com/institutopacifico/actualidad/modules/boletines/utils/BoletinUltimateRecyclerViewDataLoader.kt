package com.institutopacifico.actualidad.modules.boletines.utils

import com.institutopacifico.actualidad.network.IonObject
import com.institutopacifico.actualidad.objects.ArticleObject
import com.institutopacifico.actualidad.objects.ResponseObject

class BoletinUltimateRecyclerViewDataLoader(private val API: String, private val CurrentActivity: android.app.Activity, val lambda_AsynchronousResponseProcessor: (ResponseObject) -> Unit) {
    val LOAD_INITIAL_VALUES = "load_initial_values"
    val STRING_MORE_DATA = "more_data"
    val REFRESH = "REFRESH"
    private var FILTERS: String = ""
    private val integer_StatusCodeOK: Int
    private val integer_StatusCodeFAIL: Int
    private var body: com.institutopacifico.actualidad.objects.BodyObject = com.institutopacifico.actualidad.objects.BodyObject()
    internal var gson: com.google.gson.Gson
    var arrayList_BoletinValues: List<String>


    internal val int_LoadMoreStep: Int
    internal val int_InitialValuesStep: Int
    internal val int_RefreshStep: Int

    init {
        arrayList_BoletinValues = arrayListOf()

        gson = com.google.gson.Gson()

        int_RefreshStep = CurrentActivity.resources.getInteger(com.institutopacifico.actualidad.R.integer.integer_RecyclerViewRefreshStep)
        int_LoadMoreStep = CurrentActivity.resources.getInteger(com.institutopacifico.actualidad.R.integer.integer_RecyclerViewRefreshStep)
        int_InitialValuesStep = CurrentActivity.resources.getInteger(com.institutopacifico.actualidad.R.integer.integer_RecyclerViewInitialValuesStep)

        integer_StatusCodeOK = CurrentActivity.resources.getInteger(com.institutopacifico.actualidad.R.integer.integer_network_status_code_ok)
        integer_StatusCodeFAIL = CurrentActivity.resources.getInteger(com.institutopacifico.actualidad.R.integer.integer_failure_message)


    }


    fun loadInitialBoletinValues() {
        body = com.institutopacifico.actualidad.objects.BodyObject()
        body.string_step = com.institutopacifico.actualidad.application.ApplicationClass.Companion.context.getString(com.institutopacifico.actualidad.R.string.integer_RecyclerViewInitialValuesStep).toString()
        body.string_request = LOAD_INITIAL_VALUES
        body.string_revista = com.institutopacifico.actualidad.application.ApplicationClass.Companion.context.getString(com.institutopacifico.actualidad.R.string.revista_api_code)
        body.userObject_UserDataObject = com.institutopacifico.actualidad.objects.UserDataSingleton.userData
        if (FILTERS.isNotEmpty()) {
            body.string_additional_parameter = FILTERS
        }
        // ListRequest(body);
        AsynchronousListRequest(body)
    }


    fun moreData(currentLastObject: com.institutopacifico.actualidad.objects.ArticleObject) {
        body = com.institutopacifico.actualidad.objects.BodyObject()
        body.string_step = com.institutopacifico.actualidad.application.ApplicationClass.Companion.context.getString(com.institutopacifico.actualidad.R.string.integer_RecyclerViewInitialValuesStep).toString()
        body.string_request = STRING_MORE_DATA
        body.string_revista = com.institutopacifico.actualidad.application.ApplicationClass.Companion.context.getString(com.institutopacifico.actualidad.R.string.revista_api_code)
        body.userObject_UserDataObject = com.institutopacifico.actualidad.objects.UserDataSingleton.userData
        body.articleObject_referenceObject = currentLastObject
        if (FILTERS.isNotEmpty()) {
            body.string_additional_parameter = FILTERS
        }
        AsynchronousListRequest(body)
    }

    fun refresh_AKA_NewData(currentFirstObject: com.institutopacifico.actualidad.objects.ArticleObject) {
        body = com.institutopacifico.actualidad.objects.BodyObject()
        body.string_request = REFRESH
        body.string_revista = com.institutopacifico.actualidad.application.ApplicationClass.Companion.context.getString(com.institutopacifico.actualidad.R.string.revista_api_code)
        body.userObject_UserDataObject = com.institutopacifico.actualidad.objects.UserDataSingleton.userData
        body.articleObject_referenceObject = currentFirstObject
        if (FILTERS.isNotEmpty()) {
            body.string_additional_parameter = FILTERS
        }
        AsynchronousListRequest(body)
    }

    fun setFILTERS(FILTERS: String) {
        com.orhanobut.logger.Logger.d("Filter set to: " + FILTERS)
        this.FILTERS = FILTERS
    }

    fun clearFILTERS() {
        this.FILTERS = ""
    }

    private fun AsynchronousListRequest(body: com.institutopacifico.actualidad.objects.BodyObject) {
        val web_service = API
       // com.institutopacifico.actualidad.network.IonSingleton.Companion.instance.makeAsynchronousCompressedRequest(CurrentActivity, web_service, body)
        IonObject().makeAsynchronousCompressedRequest(CurrentActivity, web_service, body,lambda_AsynchronousResponseProcessor)
    }
}



