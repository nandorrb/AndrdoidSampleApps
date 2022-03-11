package com.institutopacifico.actualidad.utils

import android.content.Context

import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass

import es.dmoral.toasty.Toasty

/**
 * Created by mobile on 5/29/17.
 */

object NotificationsHelper {
    fun PleaseLogInMessage(context: Context) {
        Toasty.info(context, ApplicationClass.context.getString(R.string.message_please_sign_in)).show()
    }
}
