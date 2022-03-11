package com.institutopacifico.actualidad.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.google.gson.Gson
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.institutopacifico.actualidad.objects.AppDataObject
import com.institutopacifico.actualidad.objects.UserDataSingleton
import com.institutopacifico.actualidad.objects.UserObject
import com.vansuita.materialabout.builder.AboutBuilder
import com.vansuita.materialabout.views.AboutView


class SettingsFragment : android.support.v4.app.Fragment() {

  //   var ViewGroup_root: ViewGroup =  View(ApplicationClass.context) as ViewGroup
     var UserObject_UserData: com.institutopacifico.actualidad.objects.UserObject = com.institutopacifico.actualidad.objects.UserObject()
     var AppDataObject_AppData: com.institutopacifico.actualidad.objects.AppDataObject = com.institutopacifico.actualidad.objects.AppDataObject()
     var gson: com.google.gson.Gson = com.google.gson.Gson()


    override fun onCreateView(inflater: android.view.LayoutInflater?, container: android.view.ViewGroup?, savedInstanceState: android.os.Bundle?): android.view.View? {
     val   ViewGroup_root = inflater!!.inflate(com.institutopacifico.actualidad.R.layout.fragment_session, null) as android.view.ViewGroup
        gson = com.google.gson.Gson()

        loadAppAndUserData()
        loadAbout(ViewGroup_root)

        return ViewGroup_root
    }


    private fun loadAppAndUserData() {
        UserObject_UserData = com.institutopacifico.actualidad.objects.UserDataSingleton.userData
        AppDataObject_AppData = com.institutopacifico.actualidad.objects.AppDataObject()

    }

    private fun loadAbout(ViewGroup_root: android.view.ViewGroup) {
        val flHolder = ViewGroup_root.findViewById<android.widget.LinearLayout>(com.institutopacifico.actualidad.R.id.layout_fragment_session_about_me_holder)

        var builder: com.vansuita.materialabout.builder.AboutBuilder = com.vansuita.materialabout.builder.AboutBuilder.with(ViewGroup_root.context as android.content.Context)
                .setAppIcon(com.institutopacifico.actualidad.R.mipmap.ic_launcher)
                .setAppName(com.institutopacifico.actualidad.R.string.app_name)
                .setPhoto(com.institutopacifico.actualidad.R.mipmap.profile_picture)
                .setCover(com.institutopacifico.actualidad.R.mipmap.profile_cover)
                .setLinksAnimated(true)
                .setDividerDashGap(13)
                .setName(UserObject_UserData.stringInstitutoPacificoAboutUserName)
                .setSubTitle("ID: " + UserObject_UserData.stringInstitutoPacificoUserId + "\nMembresia: " + UserObject_UserData.stringInstitutoPacificoAboutMembership)
                .setLinksColumnsCount(4)
                .setBrief(AppDataObject_AppData.stringInstitutoPacificoAboutContactUsTitle)
                .addFacebookLink(AppDataObject_AppData.stringInstitutoPacificoAboutLinkFacebook)
                .addYoutubeChannelLink(AppDataObject_AppData.stringInstitutoPacificoAboutLinkYoutubeChannels)
                .addEmailLink(AppDataObject_AppData.stringInstitutoPacificoAboutLinkEmail)
                .addWebsiteLink(AppDataObject_AppData.stringInstitutoPacificoAboutLinkWebsite)
                .addFiveStarsAction()
                .setVersionNameAsAppSubTitle()
                .addShareAction(com.institutopacifico.actualidad.R.string.app_name)
                .addUpdateAction()
                .setActionsColumnsCount(2)
                .addFeedbackAction(AppDataObject_AppData.stringInstitutoPacificoLinkFeedback)
                .setWrapScrollView(true)
                .setShowAsCard(true)


        var view: com.vansuita.materialabout.views.AboutView = builder.build()
        flHolder.addView(view)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        val refWatcher = com.institutopacifico.actualidad.application.ApplicationClass.Companion.getRefWatcher(activity)
        refWatcher?.watch(this)
    }



}
