package com.institutopacifico.actualidad.modules.boletines.utils

import android.content.Context

import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.institutopacifico.actualidad.network.IonSingleton
import com.institutopacifico.actualidad.objects.BodyObject
import com.institutopacifico.actualidad.objects.ArticleObject
import com.institutopacifico.actualidad.objects.UserDataSingleton
import com.orhanobut.logger.Logger

import java.util.ArrayList

/**
 * Created by mobile on 5/19/17.
 */

class StarredArticlesSingleton private constructor() {
    internal var list_ArticleObject_BoletinStarredArticles: MutableList<ArticleObject> = ArrayList()
    internal var articleObject_currentObject: ArticleObject = ArticleObject()
    var string_CurrentArticleJson = ""
    internal var SAVE_STARRED_ARTICLES = "save_starred_articles"

    //list_ArticleObject_BoletinStarredArticles
    fun addItemTo_List_BoletinItemViewObject_BoletinStarredArticles(articleObject_BoletinStarredArticle: ArticleObject) {

        articleObject_currentObject = articleObject_BoletinStarredArticle
    }


    fun clearList_BoletinItemViewObject_BoletinStarredArticles() {
        articleObject_currentObject = ArticleObject()
    }

    fun removeItemFrom_List_BoletinItemViewObject_BoletinStarredArticles(articleObject: ArticleObject) {
        try {
            list_ArticleObject_BoletinStarredArticles.remove(articleObject)
            articleObject_currentObject = ArticleObject()
        } catch (e: Exception) {
            Logger.e(e.message)
        }
    }

    fun sendListToServer(context: Context) {
        val body: BodyObject = BodyObject()
        body.string_request = SAVE_STARRED_ARTICLES
        body.string_revista = ApplicationClass.context.getString(R.string.revista_api_code)
        body.userObject_UserDataObject = UserDataSingleton.userData
        body.articleObject_referenceObject = articleObject_currentObject

        val web_service = ApplicationClass.context.getString(R.string.string_api_starred_articles)

        IonSingleton.instance.makeAsynchronousRequest(context, web_service, body)

    }

    companion object {
        val instance = StarredArticlesSingleton()
    }
}
