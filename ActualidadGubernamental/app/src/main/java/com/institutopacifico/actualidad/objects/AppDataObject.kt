package com.institutopacifico.actualidad.objects

import com.google.gson.annotations.SerializedName
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass

class AppDataObject// userData=  new UserObject();

//INSTITUTO PACIFICO DATA
{

    @SerializedName("string_institutoPacifico_About_contactUs_Title")
    var stringInstitutoPacificoAboutContactUsTitle: String=""

    @SerializedName("string_institutoPacifico_About_Link_playStore")
    var stringInstitutoPacificoAboutLinkPlayStore: String=""

    @SerializedName("string_institutoPacifico_About_Link_facebook")
    var stringInstitutoPacificoAboutLinkFacebook: String=""

    @SerializedName("string_institutoPacifico_About_Link_twitter")
    var stringInstitutoPacificoAboutLinkTwitter: String=""

    @SerializedName("string_institutoPacifico_About_Link_google_plus")
    var stringInstitutoPacificoAboutLinkGooglePlus: String=""

    @SerializedName("string_institutoPacifico_About_Link_youtube_channels")
    var stringInstitutoPacificoAboutLinkYoutubeChannels: String=""

    @SerializedName("string_institutoPacifico_About_Link_linkedin")
    var stringInstitutoPacificoAboutLinkLinkedin: String=""

    @SerializedName("string_institutoPacifico_About_Link_email")
    var stringInstitutoPacificoAboutLinkEmail: String=""

    @SerializedName("string_institutoPacifico_About_whatsapp_name")
    var stringInstitutoPacificoAboutWhatsappName: String=""

    @SerializedName("string_institutoPacifico_About_whatsapp_phone_number")
    var stringInstitutoPacificoAboutWhatsappPhoneNumber: String=""

    @SerializedName("string_institutoPacifico_About_Link_skype")
    var stringInstitutoPacificoAboutLinkSkype: String=""

    @SerializedName("string_institutoPacifico_About_Link_google")
    var stringInstitutoPacificoAboutLinkGoogle: String=""

    @SerializedName("string_institutoPacifico_About_Link_website")
    var stringInstitutoPacificoAboutLinkWebsite: String=""

    @SerializedName("string_institutoPacifico_About_Link_moreAppsByUs")
    var stringInstitutoPacificoAboutLinkMoreAppsByUs: String=""

    @SerializedName("string_institutoPacifico_Link_feedback")
    var stringInstitutoPacificoLinkFeedback: String=""

   // @SerializedName("user_object")
   // var userData: UserObject=UserObject

    init {
        stringInstitutoPacificoAboutContactUsTitle = ApplicationClass.context.getString(R.string.default_institutoPacifico_About_contactUs_Title)
        stringInstitutoPacificoAboutLinkPlayStore = ApplicationClass.context.resources.getString(R.string.default_institutoPacifico_About_Link_playStore)
        stringInstitutoPacificoAboutLinkFacebook = ApplicationClass.context.resources.getString(R.string.default_institutoPacifico_About_Link_facebook)
        stringInstitutoPacificoAboutLinkTwitter = ApplicationClass.context.resources.getString(R.string.default_institutoPacifico_About_Link_twitter)
        stringInstitutoPacificoAboutLinkGooglePlus = ApplicationClass.context.resources.getString(R.string.default_institutoPacifico_About_Link_google_plus)
        stringInstitutoPacificoAboutLinkYoutubeChannels = ApplicationClass.context.resources.getString(R.string.default_institutoPacifico_About_Link_youtube_channels)
        stringInstitutoPacificoAboutLinkLinkedin = ApplicationClass.context.resources.getString(R.string.default_institutoPacifico_About_Link_linkedin)
        stringInstitutoPacificoAboutLinkEmail = ApplicationClass.context.resources.getString(R.string.default_institutoPacifico_About_email)
        stringInstitutoPacificoAboutWhatsappName = ApplicationClass.context.resources.getString(R.string.default_institutoPacifico_About_whatsapp_name)
        stringInstitutoPacificoAboutWhatsappPhoneNumber = ApplicationClass.context.resources.getString(R.string.default_institutoPacifico_About_whatsapp_phone_number)
        stringInstitutoPacificoAboutLinkSkype = ApplicationClass.context.resources.getString(R.string.default_institutoPacifico_About_Link_skype)
        stringInstitutoPacificoAboutLinkGoogle = ApplicationClass.context.resources.getString(R.string.default_institutoPacifico_About_Link_google)
        stringInstitutoPacificoAboutLinkWebsite = ApplicationClass.context.resources.getString(R.string.default_institutoPacifico_About_Link_website)
        stringInstitutoPacificoAboutLinkMoreAppsByUs = ApplicationClass.context.resources.getString(R.string.default_institutoPacifico_About_Link_moreAppsByUs)
        stringInstitutoPacificoLinkFeedback = ApplicationClass.context.resources.getString(R.string.default_institutoPacifico_email_feedback)

    }

}


