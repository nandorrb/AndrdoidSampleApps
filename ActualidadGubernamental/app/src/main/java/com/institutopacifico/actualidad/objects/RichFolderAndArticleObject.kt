package com.institutopacifico.actualidad.objects

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * Created by mobile on 6/27/17.
 * Fernando Rubio Burga
 */
open class RichFolderAndArticleObject {

    @SerializedName("string_id")
    var string_object_id: String = ""

    @SerializedName("string_name_or_title")
    var string_object_name_or_title: String = ""

    @SerializedName("array_of_sub_items")
    var list_of_objects: Array<RichFolderAndArticleObject> = arrayOf()

    @SerializedName("string_description")
    var string_description: String = ""

    @SerializedName("string_url_link_to_image_resource")
    var string_url_ImageWebSource: String = ""

    @SerializedName("string_url_link_to_web")
    var string_url_LinkToWeb: String = ""

    @SerializedName("string_url_link_to_content_in_markdown")
    var string_url_LinkToMarkdown: String = ""

    @SerializedName("string_url_link_to_content_in_pdf")
    var string_url_LinkToFileInFormatPDF: String = ""

    @SerializedName("string_date_added")
    var string_DateAdded: String = ""

    @SerializedName("string_date_when_article_is_starred")
    var string_date_when_article_is_starred: String = ""

    @SerializedName("string_category")
    var string_article_category: String = ""

    @SerializedName("string_organismo")
    var string_emisor_organismo: String = ""

    @SerializedName("string_entidad")
    var string_emisor_entidad: String = ""

    // Level
    var integer_current_level: Int = 0


  /*  fun getChildrenList(): List<SmartItem> {
        var a: MutableList<SmartItem> = mutableListOf()
        for (RichFolder: RichFolderAndArticleObject in this.list_of_objects) {
            a.add(SmartItem.child(RichFolder.string_object_name_or_title, Gson().toJson(RichFolder)))
        }
        return a.toList()
    }

    fun getParenWithChildrenList(): List<SmartItem> {
        var output: MutableList<SmartItem> = mutableListOf()
        for (RichFolder: RichFolderAndArticleObject in this.list_of_objects) {
            output.add(SmartItem.parent(RichFolder.string_object_name_or_title, "open", RichFolder.list_of_objects[0].getChildrenList()))
        }
        return output
    }
    */
}