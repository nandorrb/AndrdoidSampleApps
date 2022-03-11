package com.institutopacifico.actualidad.objects

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.orhanobut.logger.Logger

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by mobile on 5/12/17.
 */

open class ArticleObject {

    @SerializedName("boletin_articles_title")
    var string_title: String=""

    @SerializedName("boletin_articles_description")
    var string_description: String=""

    @SerializedName("boletin_articles_image_url")
     var string_url_ImageWebSource: String=""

    @SerializedName("boletin_articles_url")
    var string_url_LinkToWeb: String=""

    @SerializedName("boletin_articles_description_md_url")
     var string_url_LinkToMarkdown: String=""

    @SerializedName("boletin_articles_pdf")
     var string_url_LinkToFileInFormatPDF: String=""

    @SerializedName("boletin_articles_is_starred")
    var boolean_isThisArticleStarredStarred: Boolean = false

    @SerializedName("boletin_articles_date_added")
    var string_DateAdded: String=""

    @SerializedName("boletin_articles_date_when_article_is_starred")
     var string_date_when_article_is_starred: String=""

    @SerializedName("boletin_articles_category")
     var string_article_category: String=""

    @SerializedName("boletin_articles_organismo")
     var string_emisor_organismo: String=""

    @SerializedName("boletin_articles_entidad")
     var string_emisor_entidad: String=""


    constructor() {
        this.string_title = ""
        this.string_description = ""
        this.string_url_ImageWebSource = ""
        this.boolean_isThisArticleStarredStarred = false
        this.string_DateAdded = ""
        this.string_url_LinkToWeb = ""
        this.string_article_category = ""
        this.string_url_LinkToFileInFormatPDF = ""
    }
    init {
        this.string_title = ""
        this.string_description = ""
        this.string_url_ImageWebSource = ""
        this.boolean_isThisArticleStarredStarred = false
        this.string_DateAdded = ""
        this.string_url_LinkToWeb = ""
        this.string_article_category = ""
        this.string_emisor_organismo = ""
        this.string_emisor_entidad = ""
        this.string_url_LinkToFileInFormatPDF = ""
    }

    constructor(textView_title: String, textView_description: String, imageView_ImageWebSource: String, imageButton_isStarred: Boolean, string_DateAdded: String, url_LinkToWeb: String, string_article_category: String, url_LinkToFileInFormatPDF: String) {

        this.string_title = textView_title
        this.string_description = textView_description
        this.string_url_ImageWebSource = imageView_ImageWebSource
        this.boolean_isThisArticleStarredStarred = imageButton_isStarred
        this.string_DateAdded = string_DateAdded
        this.string_url_LinkToWeb = url_LinkToWeb
        this.string_article_category = string_article_category
        this.string_url_LinkToFileInFormatPDF = url_LinkToFileInFormatPDF

    }

    fun getTextView_description(): String {
        if (string_description.contains("\r\n")) {
            string_description = string_description.replace("\r\n".toRegex(), " ")
        }
        return string_description
    }



    fun toggleImageButton_isStarred() {
        boolean_isThisArticleStarredStarred = !boolean_isThisArticleStarredStarred
    }

    val date_boletin_articles_date_added: Date
        get() {
            var date_boletin_articles_date_added: Date
            date_boletin_articles_date_added = Date()

            val format = SimpleDateFormat(ApplicationClass.context.getString(R.string.date_format))
            try {
                date_boletin_articles_date_added = format.parse(string_DateAdded)
            } catch (e: ParseException) {
                Logger.e(e.message)
            }

            return date_boletin_articles_date_added

        }



    val thisObjectAsJson: String
        get() {
            return Gson().toJson(this)
        }

    fun loadObjectFromJson(jsonObject: String) {
        var NewObject:ArticleObject
        try {
            NewObject = Gson().fromJson(jsonObject, ArticleObject::class.java)
        }catch (e:Exception){
            NewObject =ArticleObject()
        }
        this.string_title = NewObject.string_title
        this.string_description = NewObject.getTextView_description()
        this.string_url_ImageWebSource = NewObject.string_url_ImageWebSource
        this.boolean_isThisArticleStarredStarred = NewObject.boolean_isThisArticleStarredStarred
        this.string_DateAdded = NewObject.string_DateAdded
        this.string_url_LinkToWeb = NewObject.string_url_LinkToWeb
        this.string_article_category = NewObject.string_article_category
        this.string_url_LinkToFileInFormatPDF = NewObject.string_url_LinkToFileInFormatPDF
        this.string_url_LinkToMarkdown = NewObject.string_url_LinkToMarkdown
        this.string_emisor_entidad = NewObject.string_emisor_entidad
        this.string_emisor_organismo = NewObject.string_emisor_organismo
    }

}
