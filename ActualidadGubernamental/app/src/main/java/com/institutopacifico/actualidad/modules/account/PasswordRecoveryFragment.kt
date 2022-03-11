package com.institutopacifico.actualidad.modules.account

import android.accounts.Account
import android.accounts.AccountManager
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v13.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.R.drawable.ic_check_green_a700_24dp
import com.institutopacifico.actualidad.application.ApplicationClass
import com.institutopacifico.actualidad.network.IonSingleton
import com.institutopacifico.actualidad.objects.BodyObject
import com.institutopacifico.actualidad.objects.UserObject
import com.institutopacifico.actualidad.utils.FragmentTransactionHelper
import com.orhanobut.logger.Logger
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_z_other_password_recovery.*


class PasswordRecoveryFragment : Fragment() {


    internal var accounts: Array<Account> = arrayOf()
    lateinit internal var root: ViewGroup
    internal var emailPattern = Patterns.EMAIL_ADDRESS
    internal var possibleEmail = ""
    internal var boolean_EmailIsOK = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater!!.inflate(R.layout.fragment_z_other_password_recovery, null) as ViewGroup
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val refWatcher = ApplicationClass.getRefWatcher(activity)
        refWatcher?.watch(this)
    }

    private fun getAccounts() {
        val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(root.context,
                android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    android.Manifest.permission.GET_ACCOUNTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        arrayOf(android.Manifest.permission.GET_ACCOUNTS),
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS)
            }
        }

        try {
            //   possibleEmail += "************* Get Registered Gmail Account *************\n\n";
            accounts = AccountManager.get(root.context).accounts

            for (account in accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    possibleEmail = account.name
                }
            }
        } catch (e: Exception) {
            Logger.e(e.message)
        }
    }

    override fun onResume() {
        super.onResume()
        setListeners()
        getAccounts()
        validateData()
    }

    fun setListeners() {

        button_cancelar_fragment_other_password_recovery.setOnClickListener {
            FragmentTransactionHelper.goToPreviousFragment()
        }

        button_ok_fragment_other_password_recovery.setOnClickListener {
            if (boolean_EmailIsOK) {
                sendUserObjectForPasswordRecovery()
                Toasty.normal(context, getString(R.string.message_successfull_password_recovery), ic_check_green_a700_24dp).show()
                FragmentTransactionHelper.goToPreviousFragment()
            } else {
                Toasty.normal(context, getString(R.string.message_fragment_z_other_password_recovery_bad_email)).show()
            }
        }

    }

    fun sendUserObjectForPasswordRecovery() {
        var UserObject_BuiltUSer: UserObject = UserObject()
        UserObject_BuiltUSer.stringInstitutoPacificoAboutUserName = ""
        UserObject_BuiltUSer.stringInstitutoPacificoUserEmail = edit_text_fragment_other_password_recovery_user_email?.text.toString()


        var Body: BodyObject = BodyObject()
        Body.string_request = getString(R.string.request_i_forgot_my_data)
        Body.userObject_UserDataObject = UserObject_BuiltUSer
        IonSingleton.instance.makeAsynchronousRequest(root.context, getString(R.string.string_api_session), Body)
    }


    private fun ItHasAValidEmail(string_email: String): Boolean {
        var IsEmailOk = false
        if (emailPattern.matcher(string_email).matches()) {
            IsEmailOk = true
        }
        return IsEmailOk
    }


    private fun validateData() {
        text_view_fragment_other_password_recovery_bad_email_message?.visibility = View.GONE

        edit_text_fragment_other_password_recovery_user_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (ItHasAValidEmail(edit_text_fragment_other_password_recovery_user_email?.text.toString())) {
                    text_view_fragment_other_password_recovery_bad_email_message?.visibility = View.GONE
                    boolean_EmailIsOK = true
                } else {
                    text_view_fragment_other_password_recovery_bad_email_message?.visibility = View.VISIBLE
                    boolean_EmailIsOK = false
                }
            }
        })
    }
}


