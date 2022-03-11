package com.institutopacifico.actualidad.objects

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.orhanobut.logger.Logger

/**
* Created by mobile on 5/2/17 at 11:44.
* Fernando Rubio Burga
*/

object UserDataSingleton   {
    private var Gson: Gson = Gson()
    private var SharedPreferences: SharedPreferences = ApplicationClass.context.getSharedPreferences(ApplicationClass.context.getString(R.string.app_shared_preferences_key), Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = SharedPreferences.edit()


     var UserObject_UserData: UserObject = UserObject()
     var userObject_TemporalUserDataForLogin: UserObject = UserObject()


    private var string_WholeUserAndAppDataAsJson: String = ""
    private var boolean_is_dark_theme_activated = false

    init {

    }

    var userData: UserObject
        get() {
            string_WholeUserAndAppDataAsJson = SharedPreferences.getString(ApplicationClass.context.getString(R.string.user_data_object_as_json_key), Gson().toJson(UserObject()))
            UserObject_UserData = Gson.fromJson(string_WholeUserAndAppDataAsJson, UserObject::class.java) //as UserObject
            return UserObject_UserData
        }
        set(Object) {
            editor.putString(ApplicationClass.context.getString(R.string.user_data_object_as_json_key), Gson().toJson(Object))
            editor.commit()
        }

    fun clearAppAndUserData() {
        try {
            editor.remove(ApplicationClass.context.getString(R.string.user_data_object_as_json_key))
            editor.commit()
            userData = UserObject()
        } catch (e: Exception) {
            Logger.e(e.message)
        }

    }

    fun toggleThemePreference() {
        themePreference = !boolean_is_dark_theme_activated
    }

    //Theme preferences
     var themePreference: Boolean
        get() = SharedPreferences.getBoolean(ApplicationClass.context.getString(R.string.boolean_is_dark_theme_activated_key), boolean_is_dark_theme_activated)
        set(boolean_is_dark_theme_activated) {
            editor.putBoolean(ApplicationClass.context.getString(R.string.boolean_is_dark_theme_activated_key), boolean_is_dark_theme_activated)
            editor.commit()
            this.boolean_is_dark_theme_activated = boolean_is_dark_theme_activated
        }

    /*companion object {
        private var SharedPreferences: SharedPreferences = ApplicationClass.context.getSharedPreferences(ApplicationClass.context.getString(R.string.app_shared_preferences_key), Context.MODE_PRIVATE)
        private var editor: SharedPreferences.Editor = SharedPreferences.edit()

       // private val ourInstance = UserDataSingleton()

        val instance: UserDataSingleton
            get() {
             //   SharedPreferences = ApplicationClass.context.getSharedPreferences(ApplicationClass.context.getString(R.string.app_shared_preferences_key), Context.MODE_PRIVATE)
             //   editor = SharedPreferences.edit()
                return ourInstance
            }

    }
    */

}