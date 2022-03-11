package com.institutopacifico.actualidad.adapters

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.signature.ApplicationVersionSignature
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.institutopacifico.actualidad.objects.RichFragmentObject
import com.institutopacifico.actualidad.objects.UserDataSingleton
import com.institutopacifico.actualidad.utils.FragmentTransactionHelper
import com.institutopacifico.actualidad.utils.NotificationsHelper
import es.dmoral.toasty.Toasty

/**
 * Created by mobile on 5/18/17.
 * Fernando Rubio Burga
 */

class MainActivityDashboardRichObjectAdapter(internal var AdapterList: MutableList<RichFragmentObject>, internal var toolbar: Toolbar) : RecyclerView.Adapter<MainActivityDashboardRichObjectAdapter.PlanetViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainActivityDashboardRichObjectAdapter.PlanetViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.activity_main_row_layout, parent, false)
        return PlanetViewHolder(v)
    }

    override fun onBindViewHolder(holder: PlanetViewHolder, position: Int) {

        holder.image.setImageDrawable(AdapterList[position].drawable_resource_icon)
        holder.text.text = AdapterList[position].string_fragment_title
        holder.itemView.setOnClickListener {
            if (AdapterList[position].boolean_its_a_private_service && UserDataSingleton.userData.stringInstitutoPacificoUserId.isEmpty()) {
                NotificationsHelper.PleaseLogInMessage(toolbar.context)

            } else {
                FragmentTransactionHelper.goToFragment(AdapterList[position].fragment_class, toolbar.context)
                toolbar.title = AdapterList[position].string_fragment_title
            }
        }

        holder.itemView.setOnTouchListener { v, _ ->
            v.isFocusable = true
            false
        }
    }

    override fun getItemCount(): Int {
        return AdapterList.size
    }


    class PlanetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView
        var text: TextView

        init {
            image = itemView.findViewById(R.id.image_id)
            text = itemView.findViewById(R.id.text_id)
        }
    }
}