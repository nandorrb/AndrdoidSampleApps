package com.institutopacifico.actualidad.utils

import android.content.Context
import android.content.DialogInterface
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.institutopacifico.actualidad.modules.boletines.utils.StarredArticlesSingleton
import com.koushikdutta.ion.Ion
import com.orhanobut.logger.Logger
import com.yarolegovich.slidingrootnav.SlidingRootNav
import es.dmoral.toasty.Toasty
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.view.LayoutInflater
import android.widget.Button


/**
 * Created by mobile on 5/18/17.
 * Fernando Rubio Burga
 */

object FragmentTransactionHelper {

    lateinit internal var slidingRootNav: SlidingRootNav
    lateinit internal var fragmentManager: FragmentManager
    internal var boolean_isItAnOldVersion: Boolean = false
    lateinit var string_current_version: String
    lateinit var string_internet_version: String


    fun setTransactionData(context: Context, slidingRootNav: SlidingRootNav, fragmentManager: FragmentManager) {

        this.slidingRootNav = slidingRootNav
        this.fragmentManager = fragmentManager //(ApplicationClass.context as Activity).fragmentManager as FragmentManager
        findOutIfItsAnOldVersion(context)
    }

    private fun findOutIfItsAnOldVersion(context: Context) {
        string_current_version = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        string_internet_version = string_current_version
        val url = "https://play.google.com/store/apps/details?id=" + context.packageName
        Logger.d("Checking version on: " + url)
        Ion.with(context).load(url).asString().setCallback { e, result ->
            if (e == null) {
                //  Logger.d(result)
                try {
                    string_internet_version = result.substring(result.indexOf("softwareVersion"))
                    string_internet_version = string_internet_version.substring(string_internet_version.indexOf(">") + 1, string_internet_version.indexOf("<")).trim()

                    Logger.d("Current version:$string_current_version New Version:$string_internet_version")
                    if (string_current_version != string_internet_version) {
                        Logger.d("Versions are different")
                        val a = string_current_version.substring(0, string_current_version.indexOf("-")).toFloat()
                        val b = string_internet_version.substring(0, string_internet_version.indexOf("-")).toFloat()
                        Logger.d(a.toString() + " " + b.toString())
                        if (a < b) {
                            boolean_isItAnOldVersion = true
                            Logger.d("It is an Old Version")
                        } else {
                            Logger.d("Version is OK")
                        }
                    } else {
                        Logger.d("Versions are the same")
                        boolean_isItAnOldVersion = false
                    }
                } catch (e: Exception) {
                    Logger.e(e.message)
                }
            } else {
                Logger.e(e.message)
            }
        }

    }


    fun goToFragment(targetFragment: Fragment, context: Context) {
        if (boolean_isItAnOldVersion) {
            var lambda_dismiss_dialog = {}

            //Show Update Message
            val message =
                    "Instituto Pacifico ha desarrollado una nueva version de esta Aplicación, por favor actualize en el PlayStore de $string_current_version hacia la versión $string_internet_version para gozar nuestras últimas mejoras."
            val title = "Nueva Versión!"
            //     Toasty.info(context, "Por favor actualize su aplicación en el PlayStore de $string_current_version hacia la versión $string_internet_version").show()
            val builder = AlertDialog.Builder(context)
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_custom_update_message, null)
            builder.setView(dialogView)

            builder.setMessage(message)
            builder.setTitle(title)

            val listener_onOkClick = DialogInterface.OnClickListener { _: DialogInterface, _: Int ->
                //    Toasty.info(context, message).show()
                val appPackageName = context.packageName // getPackageName() from Context or Activity object
                try {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)))
                } catch (anfe: android.content.ActivityNotFoundException) {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)))
                }

            }

            dialogView.findViewById<Button>(R.id.button_dialog_update_message_ok).text = context.getString(R.string.message_fragment_transaction_helper_actualizar)
            dialogView.findViewById<Button>(R.id.button_dialog_update_message_ok).setOnClickListener {
                val appPackageName = context.packageName // getPackageName() from Context or Activity object
                try {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)))
                } catch (anfe: android.content.ActivityNotFoundException) {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)))
                }
            }
            dialogView.findViewById<Button>(R.id.button_dialog_update_message_cancel).text = context.getString(R.string.message_fragment_transaction_helper_cancelar)

            dialogView.findViewById<Button>(R.id.button_dialog_update_message_cancel).setOnClickListener {
                lambda_dismiss_dialog()
            }


            val dialog: AlertDialog
            dialog = builder.create()

            lambda_dismiss_dialog = {
                dialog.dismiss()
            }

            dialog.show()


            //    findOutIfItsAnOldVersion(context)
        } else {
            //Go to Fragment
            val tx = fragmentManager.beginTransaction()
            try {
                tx?.replace(R.id.main, targetFragment)
                tx?.addToBackStack("MainStack")
                tx?.commit()
                slidingRootNav.closeMenu()

                StarredArticlesSingleton.instance.clearList_BoletinItemViewObject_BoletinStarredArticles()
            } catch (e: Exception) {
                Logger.e(e.message)
            }
        }
        findOutIfItsAnOldVersion(context)
    }


    fun goToPreviousFragment() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            slidingRootNav.openMenu()
        }
    }


    private fun IsThisFragmentCurrentlyInDevelopment(integer_service: Int): Boolean {
        val bad_positions = ApplicationClass.context.resources.getIntArray(R.array.integer_fragments_in_development_position)

        var boolean_this_fragment_is_in_development = false
        for (integer_current_service in bad_positions) {
            if (integer_current_service == integer_service) {
                boolean_this_fragment_is_in_development = true
                break
            }
        }
        return boolean_this_fragment_is_in_development
    }

}
