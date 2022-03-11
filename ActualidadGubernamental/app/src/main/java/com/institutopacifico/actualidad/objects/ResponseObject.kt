package com.institutopacifico.actualidad.objects

//http://stackoverflow.com/questions/19169754/parsing-nested-json-data-using-gson

import com.google.gson.annotations.SerializedName
import com.institutopacifico.actualidad.modules.boletines.utils.SortingHelpers
import com.institutopacifico.actualidad.modules.calendar.objects.RichCalendarDateObject
import com.institutopacifico.actualidad.modules.graph.objects.IndicadoresFinancierosObject

class ResponseObject {
    @SerializedName("estado")
    val integer_request_state: Int = 0

    @SerializedName("bad_request_message")
    val string_bad_request_message: String = ""

    @SerializedName("datos")
     var receivedArticlesObject: Array<ArticleObject>

    @SerializedName("user_data_object")
    var userObject_CurrentUser: UserObject

    @SerializedName("body_object_what_we_have_requested")
    var bodyObject_WhatWeHaveRequested: BodyObject

    @SerializedName("array_of_sub_items")
    var array_of_rich_folder_and_article_objects: Array<RichFolderAndArticleObject>

    @SerializedName("array_of_rich_calendar_date_objects")
    var array_of_dates: Array<RichCalendarDateObject>

    @SerializedName("array_indicadores_tipocambio")
    var array_indicadores_tipocambio: Array<IndicadoresFinancierosObject>

    init {
        receivedArticlesObject = arrayOf()
        userObject_CurrentUser = UserDataSingleton.userData
        bodyObject_WhatWeHaveRequested = BodyObject()
        array_of_rich_folder_and_article_objects = arrayOf()
        array_of_dates = arrayOf()
        array_indicadores_tipocambio = arrayOf()
    }

    val receivedData: Array<ArticleObject>
        get() {
            receivedArticlesObject = SortingHelpers.sortDataByDate(receivedArticlesObject)
            return receivedArticlesObject
        }
}
