package com.institutopacifico.actualidad.modules.pageviewer

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v4.app.FragmentActivity
import com.google.gson.Gson
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.modules.pageviewer.adapter.PageViewerAdapter
import com.institutopacifico.actualidad.objects.RichFolderAndArticleObject
import com.koushikdutta.ion.Ion
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_page_viewer_swipe.*

import java.util.*


open class PageViewerSwipeActivity : FragmentActivity() {

    lateinit internal var RootObject: RichFolderAndArticleObject
    internal var position: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_viewer_swipe)
        val Bundle = intent.extras
        RootObject = Gson().fromJson(Bundle.getString("RootObject"), RichFolderAndArticleObject::class.java)
        position = Bundle.getInt("integer_position")
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        //setCurrentObject()
        // ListOfMarkDown= LoadCurrentObjectArray()
        viewpager_activity_page_viewer_swipe?.adapter = PageViewerAdapter(supportFragmentManager, RootObject, position)
        viewpager_activity_page_viewer_swipe?.currentItem = position
    }


    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()

    }


}
