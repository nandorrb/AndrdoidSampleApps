package com.institutopacifico.actualidad.modules.webviewer

import adapters.GridAdapter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.institutopacifico.actualidad.network.IonSingleton
import com.institutopacifico.actualidad.objects.*
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_default_ultimate_recycler_view.*
import kotlinx.android.synthetic.main.fragment_default_web_view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


/**
 * Created by mobile on 4/6/17.
 * Fernando Rubio Burga
 */

open class WebViewerFragment : Fragment() {
    open var url = "http://spij.minjus.gob.pe/libre/main.asp"
    lateinit var root: ViewGroup

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater?.inflate(R.layout.fragment_default_web_view, null) as ViewGroup
        return root
    }

    override fun onResume() {
        super.onResume()
        web_view_default_fragment.loadUrl(url)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        ApplicationClass.getRefWatcher(activity)?.watch(this)
    }


}
