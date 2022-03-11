package com.institutopacifico.actualidad.modules.pageviewer

import android.app.Activity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast

import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.modules.pageviewer.themes.DarkThemeCssModule
import com.institutopacifico.actualidad.modules.pageviewer.themes.WhiteThemeCssModule
import com.institutopacifico.actualidad.objects.ArticleObject
import com.institutopacifico.actualidad.modules.boletines.utils.StarredArticlesSingleton
import com.institutopacifico.actualidad.objects.UserDataSingleton
import com.koushikdutta.ion.Ion
import com.mancj.materialsearchbar.MaterialSearchBar
import com.orhanobut.logger.Logger

import java.util.Locale

import br.tiagohm.markdownview.MarkdownView
import br.tiagohm.markdownview.css.InternalStyleSheet
import com.google.gson.Gson
import com.institutopacifico.actualidad.objects.RichFolderAndArticleObject

import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_page_viewer.*
import com.institutopacifico.actualidad.modules.pageviewer.utils.TTSHelper


open class PageViewerMagazine2Activity : Activity() {

    lateinit internal var mMarkdownView: MarkdownView
    lateinit internal var css: InternalStyleSheet

    internal var DatePattern = "DATEDATE"
    internal var MarkdownDatePattern = "**$DatePattern**"
    internal var WeblinkPattern = "WEBLINK"
    internal var MarkdownImagePattern = "![Image main]($WeblinkPattern)"
    internal var MarkdownSearchPattern = "========\n"
    internal var MarkdownBreakLine = "\n"
    internal var MarkdownCurrentFile = ""
    internal var PrimalMarkdownCurrentFileWithImage = ""
    internal var TTS_Compatible_MarkdownCurrentFile = ""
    internal var PrimalMarkdownCurrentFile = ""
    internal var string_userObjectAsJson: String = ""
    internal var boolean_tts_is_speacking = false
    internal var currentObject: ArticleObject = ArticleObject()
    internal var richCurrentObject: RichFolderAndArticleObject = RichFolderAndArticleObject()
     private lateinit var tts: TextToSpeech
    private val isSpeaking = false
    //   boolean boolean_text_to_speech_state = true;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_viewer)

        tts = TextToSpeech(this, TextToSpeech.OnInitListener {

        })
        // Speech.init(this)
        val spanish = Locale("spa", "MEX")
        tts.language = spanish

        //Do Stuff
        setListenersAndOtherStuffForViews()
        setCurrentObject()
    }


    internal fun setPageSearch() {
        activity_page_viewer_search_bar?.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
            override fun onSearchStateChanged(enabled: Boolean) {
                if (!enabled) {
                    MarkdownCurrentFile = PrimalMarkdownCurrentFile
                    ifPossibleSetImageWebSource()
                    LoadMarkdown()
                }
            }

            override fun onSearchConfirmed(text: CharSequence) {
                var ToastyActivityToast: Toast = Toasty.info(this@PageViewerMagazine2Activity, "", Toast.LENGTH_SHORT)

                ToastyActivityToast.cancel()

                MarkdownCurrentFile = PrimalMarkdownCurrentFile
                if (text.isNotEmpty()) {
                    var integer_numberOfMatches = 0
                    var index = MarkdownCurrentFile.indexOf(text.toString())
                    while (index >= 0) {
                        integer_numberOfMatches++
                        index = MarkdownCurrentFile.indexOf(text.toString(), index + 1)
                    }
                    ToastyActivityToast = Toasty.info(this@PageViewerMagazine2Activity, getString(R.string.message_we_have_found_n_matches) + integer_numberOfMatches.toString(), Toast.LENGTH_SHORT)
                    ToastyActivityToast.show()
                    MarkdownCurrentFile = MarkdownCurrentFile.replace(text.toString().toRegex(), "`" + text.toString() + "`")
                }
                ifPossibleSetImageWebSource()
                Logger.d("New Markdown After Search: " + MarkdownCurrentFile)
                LoadMarkdown()
            }

            override fun onButtonClicked(buttonCode: Int) {
                // Logger.i("onButtonClicked");
            }
        })
    }

    private fun setCurrentObject() {
        string_userObjectAsJson = StarredArticlesSingleton.instance.string_CurrentArticleJson
        //  Logger.i("Current Object is: " + string_userObjectAsJson);
        currentObject = ArticleObject()
        richCurrentObject = Gson().fromJson(string_userObjectAsJson, RichFolderAndArticleObject::class.java)
        currentObject.string_url_LinkToMarkdown = richCurrentObject.string_url_LinkToMarkdown
        currentObject.string_url_LinkToFileInFormatPDF = richCurrentObject.string_url_LinkToFileInFormatPDF
        currentObject.string_url_LinkToWeb = richCurrentObject.string_url_LinkToWeb
        try {
            //  IonSingleton.instance.setThrustManagers(this)
            Logger.d("Loading: " + currentObject.string_url_LinkToMarkdown.replace("https", "http"))
            setMarkdownCurrentFile("Loading...")
            Ion.with(this)
                    .load(currentObject.string_url_LinkToMarkdown.replace("https", "http"))
                    .asString()
                    .setCallback { e, result ->
                        // do stuff with the result or error
                        if (e == null) {
                            Logger.d("Got MarkdownCurrentFile: " + result)
                            setMarkdownCurrentFile(result)
                        } else {
                            Logger.e(e.message)
                        }
                    }
        } catch (e: Exception) {
            Logger.e(e.message)
            setMarkdownCurrentFile("Error" + e.message)
        }

        if (currentObject.boolean_isThisArticleStarredStarred) {
            image_button_activity_page_viewer_star?.setImageResource(R.drawable.ic_star_black_24dp)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
       tts.stop()
    }

    override fun onPause() {
        super.onPause()
        tts.stop()
    }

    override fun onResume() {
        super.onResume()

    }


    private fun setMarkdownCurrentFile(MarkdownCurrentFile: String) {
        this.MarkdownCurrentFile = MarkdownCurrentFile.replace("![]()", "  ")
        PrimalMarkdownCurrentFile = this.MarkdownCurrentFile
        PrimalMarkdownCurrentFileWithImage = this.MarkdownCurrentFile
        LoadMarkdown()
    }

    private fun ifPossibleSetImageWebSource() {
        if (this.MarkdownCurrentFile.isNotEmpty()
                && currentObject.string_url_ImageWebSource.isNotEmpty()
                && !this.MarkdownCurrentFile.contains(currentObject.string_url_ImageWebSource)) {
            this.MarkdownCurrentFile = this.MarkdownCurrentFile.replace(
                    MarkdownSearchPattern,
                    MarkdownSearchPattern +
                            MarkdownDatePattern.replace(
                                    DatePattern,
                                    currentObject.string_DateAdded) +
                            MarkdownBreakLine +
                            MarkdownImagePattern.replace(
                                    WeblinkPattern,
                                    currentObject.string_url_ImageWebSource) +
                            MarkdownBreakLine)
        }
    }

    fun LoadMarkdown() {
        //  MarkdownCurrentFile = "| ----- |\r\n|  **Costos de la Corrupci\u00DBn como porcentaje del PBI y del gasto p\u02D9blico** |  \r\n|  **Periodo** |  **1830-1839** |  **1840-1849** |  **1850-1859** |  **1860-1869** |  **1870-1879** |  **1880-1889** |  **1890-1899** |  **1900-1909** |  **1910-1919** |  **1920-1929** |  **1930-1939** |  **1940-1949** |  **1950-1959** |  **1960-1969** |  **1970-1979** |  **1980-1989** |  **1990-1999** |  \r\n|  % Gasto p\u02D9blico |  79 |  42 |  63 |  32 |  20 |  41 |  47 |  25 |  25 |  72 |  31 |  42 |  46 |  31 |  42 |  35 |  50 |  \r\n|  % PBI |  4.1 |  4.2 |  4.3 |  3.5 |  4.6 |  5 |  2.2 |  1 |  1.1 |  3.8 |  3.1 |  3.3 |  3.6 |  3.7 |  4.9 |  3.9 |  4.5 |  \r\n|  Fuente: Historia de la corrupci\u00DBn en el Per\u02D9, Alfonso Quiroz (2013) |   | | | | | | | | | | | | | | | | |"

        if (MarkdownCurrentFile.isNotEmpty()) {

            scroll_view_activity_page_viewer?.removeView(mMarkdownView)

            if (UserDataSingleton.themePreference) {
                css = DarkThemeCssModule()
            } else {
                css = WhiteThemeCssModule()
            }
            mMarkdownView.addStyleSheet(css)
            mMarkdownView.loadMarkdown(MarkdownCurrentFile)
            scroll_view_activity_page_viewer?.addView(mMarkdownView)

            //  image_view_activity_page_viewer_please_download_pdf?.visibility = View.GONE
        }
    }


    fun setListenersAndOtherStuffForViews() {

        mMarkdownView = MarkdownView(this@PageViewerMagazine2Activity)


        image_button_activity_page_viewer_star?.visibility = View.GONE
        image_button_activity_page_viewer_download?.visibility = View.GONE
        image_view_activity_page_viewer_please_download_pdf?.visibility = View.GONE
        image_button_activity_page_viewer_share?.visibility = View.GONE

        image_view_1_at_right_of_image_button_activity_page_viewer_text_to_speech?.visibility = View.GONE
        image_view_2_at_right_of_image_button_activity_page_viewer_share?.visibility = View.GONE
        image_view_3_at_right_of_image_button_activity_page_viewer_download?.visibility = View.GONE
        image_view_4_at_right_of_image_button_activity_page_viewer_star?.visibility = View.GONE

        image_button_activity_page_viewer_dark_toggle?.setOnClickListener {
            UserDataSingleton.toggleThemePreference()
            LoadMarkdown()
        }

        image_button_activity_page_viewer_back?.setOnClickListener {
            finish()
        }

        image_button_activity_page_viewer_text_to_speech?.setOnClickListener {
            //     Speech.getInstance().shutdown()
            //  Speech.init(this@PageViewerMagazine2Activity)
          tts.stop()
            if (!boolean_tts_is_speacking) {
                TTSHelper.TTSSmartSpeak(tts,PrimalMarkdownCurrentFile)
            }
            boolean_tts_is_speacking = !boolean_tts_is_speacking
        }

        //   scroll_view_activity_page_viewer

        setPageSearch()
    }


}
