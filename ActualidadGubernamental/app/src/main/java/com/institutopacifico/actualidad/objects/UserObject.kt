package com.institutopacifico.actualidad.objects

import com.google.gson.annotations.SerializedName
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass

/**
 * Created by mobile on 5/12/17.
 */
class UserObject {
    //USER DATA
    @SerializedName("about_user_name")
    var stringInstitutoPacificoAboutUserName: String

    @SerializedName("about_membership")
    var stringInstitutoPacificoAboutMembership: String

    @SerializedName("user_id")
    var stringInstitutoPacificoUserId: String

    @SerializedName("user_email")
    var stringInstitutoPacificoUserEmail: String

    @SerializedName("about_phone_number")
    var stringInstitutoPacificoUserPhoneNumber: String

    @SerializedName("about_address")
    var stringInstitutoPacificoUserAddress: String

    @SerializedName("user_password")
    var stringInstitutoPacificoUserPassword: String

    init {
        //USER DATA

        stringInstitutoPacificoAboutUserName = ApplicationClass.context.resources.getString (R.string.default_institutoPacifico_About_userName)
        stringInstitutoPacificoAboutMembership = ApplicationClass.context.resources.getString(R.string.default_institutoPacifico_About_Membership)
        stringInstitutoPacificoUserId = ApplicationClass.context.resources.getString(R.string.default_institutoPacifico_guest_user_id)
        stringInstitutoPacificoUserEmail = ApplicationClass.context.resources.getString(R.string.default_institutoPacifico_user_email)
        stringInstitutoPacificoUserPhoneNumber = ApplicationClass.context.resources.getString(R.string.default_institutoPacifico_user_phone_number)
        stringInstitutoPacificoUserAddress = ApplicationClass.context.resources.getString(R.string.default_institutoPacifico_user_address)
        stringInstitutoPacificoUserPassword = ApplicationClass.context.resources.getString(R.string.default_institutoPacifico_user_password)
    }


}