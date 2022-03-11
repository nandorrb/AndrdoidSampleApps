package com.institutopacifico.actualidad.modules.pageviewer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import br.tiagohm.markdownview.MarkdownView
import br.tiagohm.markdownview.css.InternalStyleSheet
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.modules.boletines.utils.StarredArticlesSingleton
import com.institutopacifico.actualidad.modules.pageviewer.themes.DarkThemeCssModule
import com.institutopacifico.actualidad.modules.pageviewer.themes.WhiteThemeCssModule
import com.institutopacifico.actualidad.modules.pageviewer.utils.TTSHelper
import com.institutopacifico.actualidad.objects.ArticleObject
import com.institutopacifico.actualidad.objects.UserDataSingleton
import com.institutopacifico.actualidad.utils.NotificationsHelper
import com.institutopacifico.actualidad.utils.ShareActionHelper
import com.koushikdutta.ion.Ion
import com.mancj.materialsearchbar.MaterialSearchBar
import com.orhanobut.logger.Logger
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_page_viewer.*
import org.greenrobot.eventbus.EventBus
import java.util.*


open class PageViewerActivity : Activity() {

    lateinit private var mMarkdownView: MarkdownView
    lateinit private var css: InternalStyleSheet

    private var DatePattern = "DATEDATE"
    private var MarkdownDatePattern = "**$DatePattern**"
    private var WeblinkPattern = "WEBLINK"
    private var MarkdownImagePattern = "![Image main]($WeblinkPattern)"
    private var MarkdownSearchPattern = "========\n"
    private var MarkdownBreakLine = "\n"
    internal var MarkdownCurrentFile = ""
    private var PrimalMarkdownCurrentFileWithImage = ""
    internal var PrimalMarkdownCurrentFile = ""
    private var string_userObjectAsJson: String = ""
    private var boolean_tts_is_speacking = false
    internal var currentObject: ArticleObject = ArticleObject()
    private lateinit var tts: TextToSpeech
    //   boolean boolean_text_to_speech_state = true;


    internal open fun LoadMarkdown() {
        if (MarkdownCurrentFile.isNotEmpty()) {

            scroll_view_activity_page_viewer?.removeView(mMarkdownView)

            css = if (UserDataSingleton.themePreference) {
                DarkThemeCssModule()
            } else {
                WhiteThemeCssModule()
            }
            mMarkdownView.addStyleSheet(css)
            mMarkdownView.loadMarkdown(MarkdownCurrentFile)
            scroll_view_activity_page_viewer?.addView(mMarkdownView)
            if (currentObject.string_article_category.isEmpty() || MarkdownCurrentFile.length < 500) {
                image_view_activity_page_viewer_please_download_pdf?.visibility = View.VISIBLE
            } else {
                image_view_activity_page_viewer_please_download_pdf?.visibility = View.GONE
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_viewer)
        tts = TextToSpeech(this, TextToSpeech.OnInitListener {

        })
        // Speech.init(this)
        val spanish = Locale("spa", "MEX")
        tts.language = spanish
        //      Speech.getInstance().setLocale(spanish)

        //Do Stuff
        setListenersAndOtherStuffForViews()
        setCurrentObject()
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
                var ToastyActivityToast: Toast = Toasty.info(this@PageViewerActivity, "", Toast.LENGTH_SHORT)

                ToastyActivityToast.cancel()

                MarkdownCurrentFile = PrimalMarkdownCurrentFile
                if (text.isNotEmpty()) {
                    var integer_numberOfMatches = 0
                    var index = MarkdownCurrentFile.indexOf(text.toString())
                    while (index >= 0) {
                        integer_numberOfMatches++
                        index = MarkdownCurrentFile.indexOf(text.toString(), index + 1)
                    }
                    ToastyActivityToast = Toasty.info(this@PageViewerActivity, getString(R.string.message_we_have_found_n_matches) + integer_numberOfMatches.toString(), Toast.LENGTH_SHORT)
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
        try {
            currentObject.loadObjectFromJson(string_userObjectAsJson)
            //  IonSingleton.instance.setThrustManagers(this)
            Logger.d("Cargando: " + currentObject.string_url_LinkToMarkdown.replace("https", "http"))
            setMarkdownCurrentFile("Cargando...")
            image_view_activity_page_viewer_please_download_pdf?.visibility = View.GONE
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
        tts.shutdown()
    }

    override fun onPause() {
        super.onPause()
        tts.stop()
    }

    override fun onResume() {
        super.onResume()

    }

    open fun setListenersAndOtherStuffForViews() {

        /*image_view_1_at_right_of_image_button_activity_page_viewer_text_to_speech?.visibility = View.VISIBLE
        image_view_2_at_right_of_image_button_activity_page_viewer_share?.visibility = View.VISIBLE
        image_view_3_at_right_of_image_button_activity_page_viewer_download?.visibility = View.VISIBLE
        image_view_4_at_right_of_image_button_activity_page_viewer_star?.visibility = View.VISIBLE
        */

        mMarkdownView = MarkdownView(this@PageViewerActivity)

        image_button_activity_page_viewer_share?.setOnClickListener {
            ShareActionHelper.share(this, currentObject.string_url_LinkToWeb)
        }

        image_button_activity_page_viewer_dark_toggle?.setOnClickListener {
            UserDataSingleton.toggleThemePreference()
            LoadMarkdown()
        }

        image_button_activity_page_viewer_back?.setOnClickListener {
            finish()
        }

        val onClickListener_PDF_Downloader = View.OnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(currentObject.string_url_LinkToFileInFormatPDF))
            try {
                startActivity(browserIntent)
            } catch (e: Exception) {
                Logger.e(e.message)
            }
        }

        image_button_activity_page_viewer_download?.setOnClickListener(onClickListener_PDF_Downloader)
        image_view_activity_page_viewer_please_download_pdf?.setOnClickListener(onClickListener_PDF_Downloader)

        image_button_activity_page_viewer_text_to_speech?.setOnClickListener {
            tts.stop()
            if (!boolean_tts_is_speacking) {
                TTSHelper.TTSSmartSpeak(tts,PrimalMarkdownCurrentFile)
            }
            boolean_tts_is_speacking = !boolean_tts_is_speacking
        }

        image_button_activity_page_viewer_star?.setOnClickListener {

            if (UserDataSingleton.userData.stringInstitutoPacificoUserId.isNotEmpty()) {


                if (!currentObject.boolean_isThisArticleStarredStarred) {
                    image_button_activity_page_viewer_star?.setImageResource(R.drawable.ic_star_black_24dp)
                    Logger.d("boolean_isThisArticleStarredStarred true")
                } else {
                    image_button_activity_page_viewer_star?.setImageResource(R.drawable.ic_instituto_pacifico_favoritoblank_black_24dp)
                    Logger.d("boolean_isThisArticleStarredStarred false")
                }
                EventBus.getDefault().post(currentObject)
                currentObject.toggleImageButton_isStarred()
            } else {
                NotificationsHelper.PleaseLogInMessage(this)
            }
        }

        setPageSearch()
    }

    private fun setMarkdownCurrentFile(MarkdownCurrentFile2: String) {
        var MarkdownCurrentFile = MarkdownCurrentFile2
        if (MarkdownCurrentFile.isEmpty()) {
            MarkdownCurrentFile = "Error 500"
        }
        this.MarkdownCurrentFile = MarkdownCurrentFile
        PrimalMarkdownCurrentFile = this.MarkdownCurrentFile
        PrimalMarkdownCurrentFileWithImage = PrimalMarkdownCurrentFile
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
}
