package com.institutopacifico.actualidad.modules.pageviewer.utils

import android.os.Build
import android.speech.tts.TextToSpeech
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.orhanobut.logger.Logger

/**
 * Created by mobile on 7/12/17.
 * Fernando Rubio Burga
 */

object TTSHelper {
    private val MAX_NUMBER_OF_STRINGS = 500

    private fun smartTruncate(TTSbias: String): String {
        var returnValue = ""
        //Default value
        var maxNunberofSubstring = MAX_NUMBER_OF_STRINGS

        //getMaxSpeechInputLength
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            maxNunberofSubstring = TextToSpeech.getMaxSpeechInputLength()
            Logger.d("getMaxSpeechInputLength:" + maxNunberofSubstring)
        }
        //Prevent overflow
        if (TTSbias.length <= maxNunberofSubstring) {
            maxNunberofSubstring = TTSbias.length - 1
        }

        try {
            Logger.d("TTSbias will be truncated from:" + TTSbias.length + " to " + maxNunberofSubstring)
            returnValue = TTSbias.substring(0, maxNunberofSubstring)
        } catch (e: Exception) {
            Logger.e(e.message)
        }
        return returnValue
    }

    private fun arraySplitter(string_GivenString: String): MutableList<String> {
        var returnValue: MutableList<String> = mutableListOf()
        //Default value
        var maxNunberofSubstring = MAX_NUMBER_OF_STRINGS

        //getMaxSpeechInputLength
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            maxNunberofSubstring = TextToSpeech.getMaxSpeechInputLength()
            Logger.d("getMaxSpeechInputLength:" + maxNunberofSubstring)
        }
        //Prevent overflow
        if (string_GivenString.length <= maxNunberofSubstring) {
            maxNunberofSubstring = string_GivenString.length - 1
        }

        try {
            Logger.d("TTSbias will be splitted in: " + string_GivenString.length / maxNunberofSubstring + " parts")
            (0..(string_GivenString.length - 1) step maxNunberofSubstring).mapTo(returnValue) {
                string_GivenString.substring(it,
                        if (it + maxNunberofSubstring <= string_GivenString.length - 1) {
                            it + maxNunberofSubstring
                        } else {
                            string_GivenString.length - 1
                        }
                )
            }

        } catch (e: Exception) {
            Logger.e(e.message)
        }
        return returnValue
    }

    fun TTSSmartSpeak(tts:TextToSpeech , source:String){
        var map: HashMap<String, String>
        for (a in TTSHelper.TTSArrayPrepare(source)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(a, TextToSpeech.QUEUE_ADD, null, null)
            } else {
                map = HashMap()
                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                tts.speak(a, TextToSpeech.QUEUE_ADD, map)
            }
        }
    }

    private fun cleanMarkdown(TTSbias: String): String {

        return TTSbias.replace("|", "")
                .replace("*", "")
                .replace("=", "")
                .replace("#", "")
        //  .replace("(", " ")
        //  .replace(")", " ")
        //  .replace(".jpg", " ")
    }

    private fun cleanImages(TTSbias: String): String {
        return TTSbias.replace(
                Regex("!\\[.+]\\(.+\\)")
                , "")
    }

    fun TTSPrepare(TTSbias: String): String {
        var returnValue: String = TTSbias
        if (returnValue.isNotEmpty()) {
            returnValue = TTSHelper.cleanImages(returnValue)
            returnValue = TTSHelper.cleanMarkdown(returnValue)
            returnValue = TTSHelper.smartTruncate(returnValue)
        } else {
            returnValue = ApplicationClass.context.getString(R.string.message_default_tts_message)
        }
        Logger.d("TTS speech :" + returnValue)

        return returnValue
    }

    private fun TTSArrayPrepare(TTSbias: String): MutableList<String> {

        var returnValue: String = TTSbias
        if (returnValue.isNotEmpty()) {
            returnValue = TTSHelper.cleanImages(returnValue)
            returnValue = TTSHelper.cleanMarkdown(returnValue)
        } else {
            returnValue = ApplicationClass.context.getString(R.string.message_default_tts_message)
        }

        //  Logger.d("TTS speech :" + returnValue)

        return TTSHelper.arraySplitter(returnValue)
    }


}