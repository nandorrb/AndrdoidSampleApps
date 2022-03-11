package com.institutopacifico.actualidad.modules.pageviewer.fragments

import android.content.Intent
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import br.tiagohm.markdownview.css.InternalStyleSheet
import com.google.gson.Gson
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.customviews.MarkdownView2
import com.institutopacifico.actualidad.modules.boletines.utils.StarredArticlesSingleton
import com.institutopacifico.actualidad.modules.pageviewer.themes.DarkThemeCssModule
import com.institutopacifico.actualidad.modules.pageviewer.themes.WhiteThemeCssModule
import com.institutopacifico.actualidad.modules.pageviewer.utils.TTSHelper
import com.institutopacifico.actualidad.objects.ArticleObject
import com.institutopacifico.actualidad.objects.RichFolderAndArticleObject
import com.institutopacifico.actualidad.objects.UserDataSingleton
import com.koushikdutta.ion.Ion
import com.mancj.materialsearchbar.MaterialSearchBar
import com.orhanobut.logger.Logger
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_markdownview.*
import java.util.*


/**
 * Created by mobile on 8/4/17.
 * Fernando Rubio Burga
 */
// Instances of this class are fragments representing a single
// object in our collection.
class MarkdownFragment : android.support.v4.app.Fragment() {


    companion object {
        val ARG_OBJECT = "object"
    }

    lateinit private var mMarkdownView: MarkdownView2
    lateinit private var css: InternalStyleSheet

    private var DatePattern = "DATEDATE"
    private var MarkdownDatePattern = "**$DatePattern**"
    private var WeblinkPattern = "WEBLINK"
    private var MarkdownImagePattern = "![Image main]($WeblinkPattern)"
    private var MarkdownSearchPattern = "========\n"
    private var MarkdownBreakLine = "\n"
    internal var MarkdownCurrentFile = ""
    private var PrimalMarkdownCurrentFileWithImage = ""
    private lateinit var tts: TextToSpeech
    private var TTS_Compatible_MarkdownCurrentFile = ""
    internal var PrimalMarkdownCurrentFile = ""
    private var string_userObjectAsJson: String = ""
    private var boolean_tts_is_speacking = false
    internal var currentObject: ArticleObject = ArticleObject()
    private var richCurrentObject: RichFolderAndArticleObject = RichFolderAndArticleObject()
    //   boolean boolean_text_to_speech_state = true;

    override fun onCreateView(inflater: android.view.LayoutInflater?, container: android.view.ViewGroup?, savedInstanceState: android.os.Bundle?): android.view.View? {
        val ViewGroup_root = inflater?.inflate(com.institutopacifico.actualidad.R.layout.fragment_markdownview, null)!! as android.view.ViewGroup

        richCurrentObject = Gson().fromJson(
                arguments.getString("richCurrentObject"),
                RichFolderAndArticleObject::class.java
        )
        tts = TextToSpeech(ViewGroup_root.context, TextToSpeech.OnInitListener {

        })
        // Speech.init(this)v
        val spanish = Locale("spa", "MEX")
        tts.language = spanish
        //Do Stuff
        setListenersAndOtherStuffForViews()
        setCurrentObject()

        return ViewGroup_root
    }


    private fun setPageSearch() {
        activity_page_viewer_search_bar?.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
            override fun onSearchStateChanged(enabled: Boolean) {
                if (!enabled) {
                    MarkdownCurrentFile = PrimalMarkdownCurrentFile
                    ifPossibleSetImageWebSource()
                    LoadMarkdown()
                }
            }

            override fun onSearchConfirmed(text: CharSequence) {
                var ToastyActivityToast: Toast = Toasty.info(activity, "", Toast.LENGTH_SHORT)

                ToastyActivityToast.cancel()

                MarkdownCurrentFile = PrimalMarkdownCurrentFile
                if (text.isNotEmpty()) {
                    var integer_numberOfMatches = 0
                    var index = MarkdownCurrentFile.indexOf(text.toString())
                    while (index >= 0) {
                        integer_numberOfMatches++
                        index = MarkdownCurrentFile.indexOf(text.toString(), index + 1)
                    }
                    ToastyActivityToast = Toasty.info(activity, getString(R.string.message_we_have_found_n_matches) + integer_numberOfMatches.toString(), Toast.LENGTH_SHORT)
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
        //  richCurrentObject = Gson().fromJson(string_userObjectAsJson, RichFolderAndArticleObject::class.java)
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
            setMarkdownCurrentFile("Error " + e.message)
        }

        if (currentObject.boolean_isThisArticleStarredStarred) {
            //    image_button_activity_page_viewer_star?.setImageResource(R.drawable.ic_star_black_24dp)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.stop()
        // Speech.getInstance().shutdown()
    }

    override fun onPause() {
        super.onPause()
        tts.stop()
    }

    override fun onResume() {
        super.onResume()

        //  Speech.init(activity)
        //  val spanish = Locale("spa", "MEX")
        //  Speech.getInstance().setLocale(spanish)
        //  Speech.getInstance().stopTextToSpeech()

        //Do Stuff
        setListenersAndOtherStuffForViews()
        // setCurrentObject()
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

        if (
        MarkdownCurrentFile.isNotEmpty()
        //  false
                ) {
            try {
                scroll_view_activity_page_viewer?.removeView(mMarkdownView)
                css = if (UserDataSingleton.themePreference) {
                    DarkThemeCssModule()
                } else {
                    WhiteThemeCssModule()
                }
                mMarkdownView.clearStyleSheets()
                mMarkdownView.addStyleSheet(css)
                mMarkdownView.loadMarkdown(MarkdownCurrentFile)
                scroll_view_activity_page_viewer?.addView(mMarkdownView)
            } catch (e: Exception) {
                Logger.e(e.message)
            }

            //  image_view_activity_page_viewer_please_download_pdf?.visibility = View.GONE
        }
    }


    private fun setListenersAndOtherStuffForViews() {

        mMarkdownView = MarkdownView2(activity)



        image_button_activity_page_viewer_star?.visibility = View.GONE
        image_button_activity_page_viewer_download?.visibility = View.VISIBLE
        image_button_activity_page_viewer_share?.visibility = View.GONE

        image_view_1_at_right_of_image_button_activity_page_viewer_text_to_speech?.visibility = View.VISIBLE
        image_view_2_at_right_of_image_button_activity_page_viewer_share?.visibility = View.GONE
        image_view_3_at_right_of_image_button_activity_page_viewer_download?.visibility = View.VISIBLE
        image_view_4_at_right_of_image_button_activity_page_viewer_star?.visibility = View.GONE

        image_button_activity_page_viewer_dark_toggle?.setOnClickListener {
            UserDataSingleton.toggleThemePreference()
            LoadMarkdown()
        }

        image_button_activity_page_viewer_back?.setOnClickListener {
            activity.finish()
        }

        val onClickListener_PDF_Downloader = View.OnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(currentObject.string_url_LinkToFileInFormatPDF))
            startActivity(browserIntent)
        }

        image_button_activity_page_viewer_download?.setOnClickListener(onClickListener_PDF_Downloader)

        image_button_activity_page_viewer_text_to_speech?.setOnClickListener {

            tts.stop()
            if (!boolean_tts_is_speacking) {
                TTSHelper.TTSSmartSpeak(tts,PrimalMarkdownCurrentFile)
            }
            boolean_tts_is_speacking = !boolean_tts_is_speacking
        }
        setPageSearch()
    }
}