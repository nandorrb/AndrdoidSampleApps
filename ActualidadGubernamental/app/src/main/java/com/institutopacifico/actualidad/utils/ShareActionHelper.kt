package com.institutopacifico.actualidad.utils

import android.content.Context
import android.content.Intent

import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass

/**
 * Created by mobile on 5/18/17.
 */

object ShareActionHelper {

    fun share(context: Context, string_url_to_send: String) {

        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "\n\n")
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, string_url_to_send)
        context.startActivity(Intent.createChooser(sharingIntent, ApplicationClass.context.getString(R.string.message_share)))

    }


}/*  private static final ShareActionHelper ourInstance = new ShareActionHelper();

    public static ShareActionHelper getInstance() {
        return ourInstance;
    }
*/
