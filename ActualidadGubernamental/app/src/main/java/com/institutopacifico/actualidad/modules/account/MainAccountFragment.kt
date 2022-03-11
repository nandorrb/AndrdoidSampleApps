package com.institutopacifico.actualidad.modules.account

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v13.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.institutopacifico.actualidad.network.IonSingleton
import com.institutopacifico.actualidad.objects.BodyObject
import com.institutopacifico.actualidad.objects.ResponseObject
import com.institutopacifico.actualidad.objects.UserDataSingleton
import com.institutopacifico.actualidad.objects.UserObject
import com.institutopacifico.actualidad.utils.FragmentTransactionHelper
import com.orhanobut.logger.Logger
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_z_other_sign_up.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe



class MainAccountFragment : Fragment() {

   // lateinit internal var config: SmartLoginConfig
  //  lateinit internal var smartLogin: SmartLogin
    lateinit internal var root: ViewGroup
    internal var emailPattern = Patterns.EMAIL_ADDRESS
    internal var possibleEmail = ""
    internal var accounts: Array<Account> = arrayOf()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater?.inflate(R.layout.fragment_account, null) as ViewGroup
        EventBus.getDefault().register(this)
     //   config = SmartLoginConfig(activity, this)

        /* getAccounts()
         setTableLayoutListeners()
         GetUserObjectResponse(UserDataSingleton.userObject_TemporalUserDataForLogin)
         refreshLayout()
 */
        setListeners()
        GetUserObjectResponse(UserDataSingleton.userObject_TemporalUserDataForLogin)
        refreshLayout()


        return root
    }

    @Subscribe
    fun AsynchronousResponse(ResponseObject_AsynchronousResponse: ResponseObject) {
        UserDataSingleton.userObject_TemporalUserDataForLogin = UserObject()
        UserDataSingleton.userData = ResponseObject_AsynchronousResponse.userObject_CurrentUser
        refreshLayout()
        Toasty.normal(root.context, ResponseObject_AsynchronousResponse.string_bad_request_message).show()
    }


    fun GetUserObjectResponse(UserObject_BuiltUserObject: UserObject) {
        if (UserObject_BuiltUserObject.stringInstitutoPacificoUserPhoneNumber == getString(R.string.USER_OBJECT_PHONE_NUMBER_KEY)) {
            requestSignup(UserObject_BuiltUserObject)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val refWatcher = ApplicationClass.getRefWatcher(activity)
        refWatcher!!.watch(this)
        EventBus.getDefault().unregister(this)
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

      //  getAccounts()
        setListeners()
        GetUserObjectResponse(UserDataSingleton.userObject_TemporalUserDataForLogin)
        refreshLayout()

    }

    private fun refreshLayout() {
        //   smartUser_currentUser = UserSessionManager.getCurrentUser(root!!.context)
        EventBus.getDefault().post(UserDataSingleton.userData)
        edit_text_fragment_account_password?.setText("")

        if (UserDataSingleton.userData.stringInstitutoPacificoUserId.isNotEmpty()) {
            //   Logger.i("Logged in user: " + UserDataSingleton.getInstance().getUserData().getStringInstitutoPacificoUserId());
            text_view_fragment_account_account_state?.text = ApplicationClass.context.getString(R.string.message_user_is_signed_in)
            button_fragment_account_sign_in?.visibility = View.GONE
            button_fragment_account_sign_up?.visibility = View.GONE
            edit_text_fragment_account_email?.visibility = View.GONE
            edit_text_fragment_account_password?.visibility = View.GONE
            text_view_fragment_account_help_url?.visibility = View.GONE
            button_fragment_account_logout?.visibility = View.VISIBLE
        } else {
            text_view_fragment_account_account_state?.text = ApplicationClass.context.getString(R.string.message_please_sign_in)
            button_fragment_account_sign_in?.visibility = View.VISIBLE
            button_fragment_account_sign_up?.visibility = View.VISIBLE
            edit_text_fragment_account_email?.visibility = View.VISIBLE
            edit_text_fragment_account_password?.visibility = View.VISIBLE
            text_view_fragment_account_help_url?.visibility = View.VISIBLE
            button_fragment_account_logout?.visibility = View.GONE

            //User is Not Logged In So he wont botter if we clean its data
            UserDataSingleton.userData = UserObject()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

       // smartLogin.onActivityResult(requestCode, resultCode, data, config)

    }

    private fun setListeners() {

        button_fragment_account_sign_in?.setOnClickListener {
            if (ItHasAValidEmail(edit_text_fragment_account_email?.text.toString()) && ItHasAValidPassword(edit_text_fragment_account_password?.text.toString())) {
                // Perform custom sign in
                val request = getString(R.string.string_api_account_fragment_sign_in)
                signIn(request)

       //         smartLogin = SmartLoginFactory.build(LoginType.CustomLogin)
        //        smartLogin.login(config)
            } else {
                Toast.makeText(root.context, ApplicationClass.context.getString(R.string.message_login_data_is_not_ok), Toast.LENGTH_SHORT).show()
            }
        }

        button_fragment_account_sign_up?.setOnClickListener {
       //     smartLogin = SmartLoginFactory.build(LoginType.CustomLogin)
       //     smartLogin.signup(config)
            FragmentTransactionHelper.goToFragment(SignUpFragment(), context)
        }

        button_fragment_account_logout?.setOnClickListener {
            if (UserDataSingleton.userData.stringInstitutoPacificoUserId.isNotEmpty()) {

      //          smartLogin = SmartLoginFactory.build(LoginType.CustomLogin)

                UserDataSingleton.clearAppAndUserData()

                refreshLayout()

                Toast.makeText(root.context, "User logged out successfully", Toast.LENGTH_SHORT).show()

            }
        }

        text_view_fragment_account_help_url?.setOnClickListener {
            FragmentTransactionHelper.goToFragment(PasswordRecoveryFragment(),context)
        }

        edit_text_dialog_custom_sign_up_user_email?.setText(possibleEmail)
    }




    private fun signIn(request: String) {
        UserDataSingleton.clearAppAndUserData()

        var UserObject_cheese: UserObject = UserDataSingleton.userData
        UserObject_cheese.stringInstitutoPacificoUserEmail = edit_text_fragment_account_email?.text.toString()
        UserObject_cheese.stringInstitutoPacificoUserPassword = edit_text_fragment_account_password?.text.toString()
        UserDataSingleton.userData = UserObject_cheese


        val body = BodyObject()
        body.string_request = request
        body.string_revista = ApplicationClass.context.getString(R.string.revista_api_code)
        body.userObject_UserDataObject = UserDataSingleton.userData
        val web_service = getString(R.string.string_api_session)

        IonSingleton.instance.makeAsynchronousRequest(root.context, web_service, body)

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





    private fun requestSignup(UserObject_cheese: UserObject) {

        val request = getString(R.string.string_api_account_fragment_sign_up)

        UserDataSingleton.clearAppAndUserData()

        UserDataSingleton.userData = UserObject_cheese


        val body = BodyObject()
        body.string_request = request
        body.string_revista = ApplicationClass.context.getString(R.string.revista_api_code)
        body.userObject_UserDataObject = UserDataSingleton.userData
        val web_service = getString(R.string.string_api_session)

        refreshLayout()

        IonSingleton.instance.makeAsynchronousRequest(root.context, web_service, body)
    }
}


