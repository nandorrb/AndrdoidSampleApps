package com.institutopacifico.actualidad.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.institutopacifico.actualidad.objects.BodyObject
import com.institutopacifico.actualidad.objects.ErrorObject
import com.institutopacifico.actualidad.objects.ResponseObject
import com.institutopacifico.actualidad.utils.CompressHelper
import com.koushikdutta.ion.Ion
import com.orhanobut.logger.Logger
import es.dmoral.toasty.Toasty
import org.greenrobot.eventbus.EventBus
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Created by mobile on 5/24/17.
 * Fernando Rubio Burga
 */

class IonObject {

    // private val ourInstance = IonObject()
    var integer_STATUS_CODE_OK: Int = IonSingleton.integer_STATUS_CODE_OK
    private var integer_STATUS_CODE_FAIL: Int = IonSingleton.integer_STATUS_CODE_FAIL
    private var NO_INTERNET_CONNECTION_ERROR_MESSAGE: String = IonSingleton.NO_INTERNET_CONNECTION_ERROR_MESSAGE
    private var SERVER_ERROR_MESSAGE: String = IonSingleton.SERVER_ERROR_MESSAGE


    var integer_RequestState: Int = 0
    internal var request_result: ResponseObject = ResponseObject()

    internal var baseURL = ApplicationClass.context.resources.getString(R.string.string_api_base_url)
    internal var serviceVersion = ApplicationClass.context.resources.getString(R.string.string_api_version)

    fun setThrustManagers(context: Context) {
        Ion.getDefault(context).httpClient.sslSocketMiddleware.setTrustManagers(arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }))
    }

    fun makeAsynchronousRequest(context: Context, web_service: String, Body: BodyObject) {
        Body.boolean_compressed = false

        request_result = ResponseObject()
        integer_RequestState = -1

        val string_URL = baseURL + serviceVersion + web_service
        //    Logger.d("Ion is requesting: " + GsonHelper.objectToJson(Body));
        Logger.d("Ion is requesting to: " + string_URL +
                "\n this JsonObject: " + Gson().toJson(Body))
        val JsonObject_Body = JsonParser().parse(Gson().toJson(Body)).asJsonObject

        //SetThrustManagers
        //  setThrustManagers(context)

        Ion.with(context)
                .load(string_URL)
                .setJsonObjectBody(JsonObject_Body)
                .asJsonObject()
                .setCallback { e, result ->
                    // do stuff with the result or error

                    if (e == null) {
                        integer_RequestState = integer_STATUS_CODE_OK
                        //  request_result = GsonHelper.objectFromJson(result.toString(), ResponseObject::class.java)
                        try {
                            request_result = Gson().fromJson(
                                    Gson().toJson(
                                            Gson().fromJson(
                                                    result.toString(),
                                                    ResponseObject::class.java)
                                    ), ResponseObject::class.java)
                            request_result.bodyObject_WhatWeHaveRequested = Body
                            makeNotifications()
                            Logger.d("Ion Result: " + Gson().toJson(request_result))
                            EventBus.getDefault().post(request_result)
                        } catch (ParseException: Exception) {
                            Logger.e(ParseException.message)
                            saveErrorToDB(ParseException.message, context, Body, string_URL)
                        }
                    } else {
                        integer_RequestState = integer_STATUS_CODE_FAIL
                        Logger.e(e.message)
                        Toasty.error(context, context.getString(R.string.message_internet_connection_error)).show()
                    }
                }
    }

    fun saveErrorToDB(message: String?, context: Context, Body: BodyObject, servicio: String) {
        Toasty.info(context, message!! //context.getString(R.string.message_server_error)
        ).show()
        val web_service = ApplicationClass.context.resources.getString(R.string.string_api_error_service)
        val string_URL = baseURL + serviceVersion + web_service
        val errorObject: ErrorObject = ErrorObject()
        errorObject.BodyObject_RequestedBody = Body
        errorObject.string_error_message = message
        errorObject.string_service_or_api = servicio
        val JsonObject_Body = JsonParser().parse(Gson().toJson(errorObject)).asJsonObject

        var a = "Sending " + message + " Error to: " + string_URL +
                "\n this JsonObject: " + Gson().toJson(errorObject)
        Logger.e(a)
        a = CompressHelper.compress(a)


        //  FirebaseCrash.log(a)

        Ion.with(context)
                .load(string_URL)
                .setJsonObjectBody(JsonObject_Body)
                .asJsonObject()
                .setCallback { e, _ ->
                    if (e != null) {
                        //       Logger.e("Error reportando el error!!" + e.message)
                    }
                }
    }

    val makeAsynchronousCompressedRequest = { context: Context, web_service: String, Body: BodyObject, asynchronousResponse: (ResponseObject) -> Unit ->

        Body.boolean_compressed = true

        request_result = ResponseObject()
        integer_RequestState = -1

        val jsonParser = JsonParser()
        val string_URL = baseURL + serviceVersion + web_service
        //    Logger.d("Ion is requesting: " + GsonHelper.objectToJson(Body));
        Logger.d("Ion is requesting to: " + string_URL +
                "\n this JsonObject: " + Gson().toJson(Body))
        val JsonObject_Body = JsonParser().parse(Gson().toJson(Body)).asJsonObject

        //SetThrustManagers
        //  setThrustManagers(context)

        Ion.with(context)
                .load(string_URL)
                .setJsonObjectBody(JsonObject_Body)
                .asString()
                .setCallback { e, result ->
                    // do stuff with the result or error

                    if (e == null) {
                        integer_RequestState = integer_STATUS_CODE_OK
                        //  request_result = GsonHelper.objectFromJson(result.toString(), ResponseObject::class.java)
                        try {
                            // Logger.d(result)
                            val uncompressedResult = CompressHelper.decompress(result)
                            Logger.d(uncompressedResult)
                            request_result = ParseStringAsResponseObject(uncompressedResult, Body, asynchronousResponse)
                          /*  request_result = Gson().fromJson(
                                    Gson().toJson(
                                            Gson().fromJson(
                                                    uncompressedResult,
                                                    ResponseObject::class.java)
                                    ), ResponseObject::class.java)
                            request_result.bodyObject_WhatWeHaveRequested = Body
                            makeNotifications()
                            Logger.v("Ion Result: " + Gson().toJson(request_result))
                            EventBus.getDefault().post(request_result)
                            asynchronousResponse(request_result)
                            */
                        } catch (ParseException: Exception) {
                            Logger.e(ParseException.message)
                            Logger.e("Se Recibió: $result ")
                        /*    try {
                                request_result = ParseStringAsResponseObject(result, Body, asynchronousResponse)
                            } catch (ParseException: Exception) {
                                Logger.e("Segundo parseo erroneo, no se intentará más: " + ParseException.message)
                            }
                            */

                            //        saveErrorToDB(ParseException.message, context, Body, string_URL)
                        }
                    } else {
                        integer_RequestState = integer_STATUS_CODE_FAIL
                        Logger.e(e.message)
                        Toasty.error(context, context.getString(R.string.message_internet_connection_error)).show()
                    }
                }
    }

    private fun ParseStringAsResponseObject(result_as_string: String, Body: BodyObject, asynchronousResponse: (ResponseObject) -> Unit): ResponseObject {
        request_result = Gson().fromJson(
                Gson().toJson(
                        Gson().fromJson(
                                result_as_string,
                                ResponseObject::class.java)
                ), ResponseObject::class.java)
        request_result.bodyObject_WhatWeHaveRequested = Body
        makeNotifications()
        Logger.v("Ion Result: " + Gson().toJson(request_result))
        EventBus.getDefault().post(request_result)
        asynchronousResponse(request_result)
        return request_result
    }

    private fun makeNotifications() {
        if (integer_RequestState != integer_STATUS_CODE_OK) {
            // Toasty.error(getActivity(), "Error " + integer_RequestState).show();
            Logger.e("Error " + integer_RequestState)
            if (integer_RequestState == integer_STATUS_CODE_FAIL) {
                Toasty.error(ApplicationClass.context, NO_INTERNET_CONNECTION_ERROR_MESSAGE).show()
                Logger.e(NO_INTERNET_CONNECTION_ERROR_MESSAGE)
            } else {
                Toasty.error(ApplicationClass.context, SERVER_ERROR_MESSAGE).show()
                Logger.e(SERVER_ERROR_MESSAGE)
            }
        }
    }

    /*  companion object {
          private val ourInstance = IonObject()
          var integer_STATUS_CODE_OK: Int = 0
          private var integer_STATUS_CODE_FAIL: Int = 0
          private var NO_INTERNET_CONNECTION_ERROR_MESSAGE: String = ""
          private var SERVER_ERROR_MESSAGE: String = ""

          integer_STATUS_CODE_OK = IonSingleton.integer_STATUS_CODE_OK //ApplicationClass.context.resources.getInteger(R.integer.integer_network_status_code_ok)
          integer_STATUS_CODE_FAIL = IonSingleton.integer_STATUS_CODE_FAIL // ApplicationClass.context.resources.getInteger(R.integer.integer_failure_message)
          NO_INTERNET_CONNECTION_ERROR_MESSAGE =IonSingleton.NO_INTERNET_CONNECTION_ERROR_MESSAGE // ApplicationClass.context.resources.getString(R.string.message_thread_error)
          SERVER_ERROR_MESSAGE = IonSingleton.SERVER_ERROR_MESSAGE

          val instance: IonObject
              get() {

                  integer_STATUS_CODE_OK = IonSingleton.integer_STATUS_CODE_OK //ApplicationClass.context.resources.getInteger(R.integer.integer_network_status_code_ok)
                  integer_STATUS_CODE_FAIL = IonSingleton.integer_STATUS_CODE_FAIL // ApplicationClass.context.resources.getInteger(R.integer.integer_failure_message)
                  NO_INTERNET_CONNECTION_ERROR_MESSAGE =IonSingleton.NO_INTERNET_CONNECTION_ERROR_MESSAGE // ApplicationClass.context.resources.getString(R.string.message_thread_error)
                  SERVER_ERROR_MESSAGE = IonSingleton.SERVER_ERROR_MESSAGE //ApplicationClass.context.resources.getString(R.string.message_server_error)
                  return ourInstance
              }
      }
      */
}