package com.institutopacifico.actualidad.objects

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by mobile on 7/21/17.
 * Fernando Rubio Burga
 */
class ErrorObject {

    @SerializedName("string_datetime")
    @Expose
    var string_datetime: String = Calendar.getInstance().time.toString()

    @SerializedName("string_servicio")
    @Expose
    var string_service_or_api: String = ""

    @SerializedName("object_requested_body")
    @Expose
    var BodyObject_RequestedBody: BodyObject = BodyObject()

    @SerializedName("object_user")
    @Expose
    var UserObject_CurrentUser: UserObject = UserDataSingleton.UserObject_UserData

    @SerializedName("string_error_message")
    @Expose
    var string_error_message: String = ""

}