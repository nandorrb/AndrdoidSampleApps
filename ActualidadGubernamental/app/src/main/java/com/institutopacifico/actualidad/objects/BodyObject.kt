package com.institutopacifico.actualidad.objects

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BodyObject {

    @SerializedName("step")
    @Expose
    var string_step: String?

    @SerializedName("request")
    @Expose
    var string_request: String

    @SerializedName("reference_object")
    @Expose
    var articleObject_referenceObject: ArticleObject?

    @SerializedName("user_data_object")
    @Expose
    var userObject_UserDataObject: UserObject

    @SerializedName("string_additional_parameter")
    @Expose
    var string_additional_parameter: String?

    @SerializedName("revista")
    @Expose
    var string_revista: String

    @SerializedName("compressed")
    @Expose
    var boolean_compressed: Boolean

    init {
        string_step = null
        string_request = ""
        articleObject_referenceObject = null
        userObject_UserDataObject = UserObject()
        string_additional_parameter = null
        string_revista = ""
        boolean_compressed=false
    }



}