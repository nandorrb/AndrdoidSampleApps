package com.institutopacifico.actualidad.modules.account

import kotlinx.android.synthetic.main.fragment_z_other_sign_up.*


class SignUpFragment : android.support.v4.app.Fragment() {


    internal var accounts: Array<android.accounts.Account> = arrayOf()
    internal var root: android.view.ViewGroup? = null
    internal var emailPattern = android.util.Patterns.EMAIL_ADDRESS
    internal var possibleEmail = ""
    internal var boolean_nameIsOK = false
    internal var boolean_EmailIsOK = false
    internal var boolean_password_1_IsOk = false
    internal var boolean_password_2_IsOk = false

    private fun SendUserObject() {
        val UserObject_BuiltUSer = com.institutopacifico.actualidad.objects.UserObject()
        UserObject_BuiltUSer.stringInstitutoPacificoAboutUserName = edit_text_dialog_custom_sign_up_user_name!!.text.toString()
        UserObject_BuiltUSer.stringInstitutoPacificoUserEmail = edit_text_dialog_custom_sign_up_user_email!!.text.toString()
        UserObject_BuiltUSer.stringInstitutoPacificoUserPassword = edit_text_dialog_custom_sign_up_pasword_1!!.text.toString()
        UserObject_BuiltUSer.stringInstitutoPacificoUserPhoneNumber = getString(com.institutopacifico.actualidad.R.string.USER_OBJECT_PHONE_NUMBER_KEY)

        org.greenrobot.eventbus.EventBus.getDefault().post(UserObject_BuiltUSer)
        com.institutopacifico.actualidad.objects.UserDataSingleton.userObject_TemporalUserDataForLogin = UserObject_BuiltUSer
    }

    override fun onCreateView(inflater: android.view.LayoutInflater?, container: android.view.ViewGroup?, savedInstanceState: android.os.Bundle?): android.view.View? {
        root = inflater!!.inflate(com.institutopacifico.actualidad.R.layout.fragment_z_other_sign_up, null) as android.view.ViewGroup
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val refWatcher = com.institutopacifico.actualidad.application.ApplicationClass.Companion.getRefWatcher(activity)
        refWatcher!!.watch(this)
    }

    private fun getAccounts() {
        val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0
        // Here, thisActivity is the current activity
        if (android.support.v4.content.ContextCompat.checkSelfPermission(root!!.context,
                android.Manifest.permission.GET_ACCOUNTS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (android.support.v13.app.ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    android.Manifest.permission.GET_ACCOUNTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                android.support.v13.app.ActivityCompat.requestPermissions(activity,
                        arrayOf(android.Manifest.permission.GET_ACCOUNTS),
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS)
            }
        }

        try {
            //   possibleEmail += "************* Get Registered Gmail Account *************\n\n";
            accounts = android.accounts.AccountManager.get(root!!.context).accounts

            for (account in accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    possibleEmail = account.name
                    //    possibleEmail += " --> " + account.name + " : " + account.type + " , \n";
                    //  possibleEmail += " \n\n";
                }

            }
        } catch (e: Exception) {
            com.orhanobut.logger.Logger.e(e.message)
        }

        //   Logger.i("mails: " + possibleEmail);
    }

    override fun onResume() {
        super.onResume()
        setListeners()
        getAccounts()
        validateData()
    }

    fun setListeners() {

        button_cancelar_fragment_other_sign_up.setOnClickListener {
            com.institutopacifico.actualidad.utils.FragmentTransactionHelper.goToPreviousFragment()
        }

        button_ok_fragment_other_sign_up.setOnClickListener {
            if (boolean_nameIsOK && boolean_EmailIsOK && boolean_password_1_IsOk && boolean_password_2_IsOk) {
                SendUserObject()
                com.institutopacifico.actualidad.utils.FragmentTransactionHelper.goToPreviousFragment()
            } else {
                es.dmoral.toasty.Toasty.normal(context, "Datos Insuficientes! por favor revise sus datos nuevamente.").show()
            }
        }
    }


    private fun ItHasAValidEmail(string_email: String): Boolean {
        var IsEmailOk = false
        if (emailPattern.matcher(string_email).matches()) {
            IsEmailOk = true
        }
        return IsEmailOk
    }

    private fun ItHasAValidPassword(string_password: String): Boolean {
        var IsPasswordOk = false
        if (string_password.length > 5) {
            IsPasswordOk = true
        }
        return IsPasswordOk
    }

    private fun ItHasAValidName(string_name: String): Boolean {
        var IsThisNameOk = false
        if (string_name.length > 5 && ThisStringContainsManyWords(string_name)) {
            IsThisNameOk = true
        }
        return IsThisNameOk
    }

    private fun ThisStringContainsManyWords(string_name: String): Boolean {
        var booleanReturnVariable = false
        val a = string_name.split("".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (a.size > 2) {
            booleanReturnVariable = true
        }
        return booleanReturnVariable
    }


    private fun validateData() {

        text_view_dialog_custom_sign_up_bad_name_message?.visibility = android.view.View.GONE
        text_view_dialog_custom_sign_up_bad_name_message?.visibility = android.view.View.GONE
        text_view_dialog_custom_sign_up_bad_password_message1?.visibility = android.view.View.GONE
        text_view_dialog_custom_sign_up_bad_password_message2?.visibility = android.view.View.GONE

        edit_text_dialog_custom_sign_up_user_name?.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: android.text.Editable) {
                if (ItHasAValidName(edit_text_dialog_custom_sign_up_user_name?.text.toString())) {
                    //NAme is OK
                    text_view_dialog_custom_sign_up_bad_name_message?.visibility = android.view.View.GONE
                    boolean_nameIsOK = true
                } else {
                    text_view_dialog_custom_sign_up_bad_name_message?.visibility = android.view.View.VISIBLE
                    boolean_nameIsOK = false
                }

            }
        })

        edit_text_dialog_custom_sign_up_user_email?.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: android.text.Editable) {
                if (ItHasAValidEmail(edit_text_dialog_custom_sign_up_user_email?.text.toString())) {
                    text_view_dialog_custom_sign_up_bad_email_message?.visibility = android.view.View.GONE
                    boolean_EmailIsOK = true
                } else {
                    text_view_dialog_custom_sign_up_bad_email_message?.visibility = android.view.View.VISIBLE
                    boolean_EmailIsOK = false
                }
            }
        })

        edit_text_dialog_custom_sign_up_pasword_1?.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: android.text.Editable) {
                if (ItHasAValidPassword(edit_text_dialog_custom_sign_up_pasword_1?.text.toString())) {
                    text_view_dialog_custom_sign_up_bad_password_message1?.visibility = android.view.View.GONE
                    boolean_password_1_IsOk = true
                } else {
                    text_view_dialog_custom_sign_up_bad_password_message1?.visibility = android.view.View.VISIBLE
                    boolean_password_1_IsOk = false
                }
            }
        })

        edit_text_dialog_custom_sign_up_pasword_2?.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: android.text.Editable) {
                if (ItHasAValidPassword(s.toString())) {
                    val a = edit_text_dialog_custom_sign_up_pasword_2?.text.toString()
                    val b = s.toString()
                    if (a == b) {
                        text_view_dialog_custom_sign_up_bad_password_message2?.visibility = android.view.View.GONE
                        boolean_password_2_IsOk = true
                    }
                } else {
                    text_view_dialog_custom_sign_up_bad_password_message2?.visibility = android.view.View.VISIBLE
                    boolean_password_2_IsOk = false
                }
            }
        })
    }


}


