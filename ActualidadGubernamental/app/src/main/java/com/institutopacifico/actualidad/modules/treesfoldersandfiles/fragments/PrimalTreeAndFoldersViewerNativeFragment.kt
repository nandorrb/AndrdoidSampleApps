package com.institutopacifico.actualidad.modules.treesfoldersandfiles.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.institutopacifico.actualidad.modules.treesfoldersandfiles.adapters.grid.GridAdapterSimpleRecyclerView
import com.institutopacifico.actualidad.network.IonObject
import com.institutopacifico.actualidad.objects.BodyObject
import com.institutopacifico.actualidad.objects.ResponseObject
import com.institutopacifico.actualidad.objects.RichFolderAndArticleObject
import com.institutopacifico.actualidad.objects.UserDataSingleton
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_default_native_recycler_view_with_floating_view.*


/**
 * Created by mobile on 4/6/17.
 * Fernando Rubio Burga
 */

open class PrimalTreeAndFoldersViewerNativeFragment : Fragment() {
    open var string_request_api=""
    open var int_drawable: Int = R.drawable.ic_folder_open_black_24dp
    var body: BodyObject = BodyObject()
    open var columns: Int = 3
    lateinit var root: ViewGroup

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater?.inflate(R.layout.fragment_default_native_recycler_view_with_floating_view, null) as ViewGroup
     //   EventBus.getDefault().register(this)
        requestData()
        fab_production_native_recycler_view_floating_view?.visibility=View.GONE
      //  string_request_api= ApplicationClass.context.resources.getString(R.string.request_tree_of_folders_and_articles_revista)
        return root
    }

    override fun onResume() {
        super.onResume()
        fab_production_native_recycler_view_floating_view?.visibility=View.GONE
        // requestData()
    }

    private fun requestData() {
        body = BodyObject()
        body.userObject_UserDataObject = UserDataSingleton.UserObject_UserData
        body.string_additional_parameter = "SHOW_ALL_ROOTS"
        body.string_request = string_request_api
        body.string_revista = ApplicationClass.context.resources.getString(R.string.revista_api_code)
        if (UserDataSingleton.userData.stringInstitutoPacificoUserId.isEmpty()) {
            Toasty.info(root.context, UserDataSingleton.userData.stringInstitutoPacificoAboutMembership).show()
        } else {
         //   IonSingleton.instance.makeAsynchronousCompressedRequest(root.context, ApplicationClass.context.resources.getString(R.string.string_api_tree_of_folders_and_articles), body)
            IonObject().makeAsynchronousCompressedRequest(root.context, ApplicationClass.context.resources.getString(R.string.string_api_tree_of_folders_and_articles), body,AsynchronousResponse)
            //TODO: Add loading icon
        }
    }

  /*  @Subscribe
    fun AsynchronousResponse(ResponseObject_AsynchronousResponse: ResponseObject) {
        // if (ResponseObject_AsynchronousResponse.array_of_rich_folder_and_article_objects.isNotEmpty()){
        var ResponseObject_LocalResponse: RichFolderAndArticleObject = RichFolderAndArticleObject()
        ResponseObject_LocalResponse.list_of_objects = ResponseObject_AsynchronousResponse.array_of_rich_folder_and_article_objects

        var mGridAdapter: GridAdapter = GridAdapter(ResponseObject_LocalResponse.list_of_objects.toList(), body, int_drawable)
        dimension_columns()
        mGridAdapter.setSpanColumns(columns)
        var mGridLayoutManager = BasicGridLayoutManager(root.context, columns, mGridAdapter)
        ultimate_recycler_view_default_fragment.layoutManager = mGridLayoutManager
        ultimate_recycler_view_default_fragment.setHasFixedSize(true)
        ultimate_recycler_view_default_fragment.isSaveEnabled = true
        ultimate_recycler_view_default_fragment.clipToPadding = false
        ultimate_recycler_view_default_fragment.setLoadMoreView(R.layout.production_ultimate_recycler_view_custom_bottom_progressbar)
        ultimate_recycler_view_default_fragment.setAdapter(mGridAdapter)
        ultimate_recycler_view_default_fragment.disableLoadmore()
        //  }
    }
*/
    val AsynchronousResponse={ResponseObject_AsynchronousResponse: ResponseObject ->

        var ResponseObject_LocalResponse: RichFolderAndArticleObject = RichFolderAndArticleObject()
        ResponseObject_LocalResponse.list_of_objects = ResponseObject_AsynchronousResponse.array_of_rich_folder_and_article_objects

        var mGridAdapter = GridAdapterSimpleRecyclerView(root.context,ResponseObject_LocalResponse.list_of_objects.toList(), body, int_drawable)

        dimension_columns()
        /*
        mGridAdapter.setSpanColumns(columns)
        var mGridLayoutManager = BasicGridLayoutManager(root.context, columns, mGridAdapter)
        */
      recycler_view_simple_default_fragment_with_floating_view.layoutManager = GridLayoutManager(root.context, columns)

      recycler_view_simple_default_fragment_with_floating_view.setHasFixedSize(true)
      recycler_view_simple_default_fragment_with_floating_view.isSaveEnabled = true
      recycler_view_simple_default_fragment_with_floating_view.clipToPadding = false
    //  recycler_view_simple_default_fragment_with_floating_view.setLoadMoreView(R.layout.production_ultimate_recycler_view_custom_bottom_progressbar)
      recycler_view_simple_default_fragment_with_floating_view.adapter = mGridAdapter
   ////   recycler_view_simple_default_fragment_with_floating_view.disableLoadmore()
        //  }
    }


    override fun onDestroyView() {
        super.onDestroyView()
      //  EventBus.getDefault().unregister(this)
        ApplicationClass.getRefWatcher(activity)?.watch(this)
    }


    private fun dimension_columns() {
        val display = activity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val density = resources.displayMetrics.density
        val dpWidth = outMetrics.widthPixels / density
        //  columns = Math.round(dpWidth / 100)
        if (columns < 1) {
            columns = 1
        }
    }
}