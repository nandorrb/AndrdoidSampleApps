package com.institutopacifico.actualidad.modules.pageviewer.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.google.gson.Gson
import com.institutopacifico.actualidad.modules.pageviewer.fragments.MarkdownFragment
import com.institutopacifico.actualidad.objects.RichFolderAndArticleObject


/**
 * Created by mobile on 8/4/17.
 * Fernando Rubio Burga
 */
// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
class PageViewerAdapter(fm: FragmentManager, internal var RootObject: RichFolderAndArticleObject, internal var position: Int) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int {
        return RootObject.list_of_objects.size
    }

    override fun getItem(i: Int): Fragment {

        val fragment = MarkdownFragment()
        val args = Bundle()
        // Our object is just an integer :-P
        args.putInt(MarkdownFragment.ARG_OBJECT, i + 1)
        args.putString( "richCurrentObject", Gson().toJson( RootObject.list_of_objects[i] ))
        fragment.arguments = args
        return fragment
    }


    override fun getPageTitle(position: Int): CharSequence {
        return "Pagina " + (position + 1)
    }
}