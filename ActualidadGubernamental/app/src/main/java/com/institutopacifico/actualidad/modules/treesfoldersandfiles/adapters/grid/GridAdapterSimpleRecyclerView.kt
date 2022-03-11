package com.institutopacifico.actualidad.modules.treesfoldersandfiles.adapters.grid

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.modules.treesfoldersandfiles.fragments.TreeMagazineFragment
import com.institutopacifico.actualidad.objects.BodyObject
import com.institutopacifico.actualidad.objects.RichFolderAndArticleObject
import com.institutopacifico.actualidad.utils.FragmentTransactionHelper
import com.institutopacifico.actualidad.utils.StringBusSingleton

/**
 * Created by mobile on 8/31/17.
 * Fernando Rubio Burga
 */

class GridAdapterSimpleRecyclerView// data is passed into the constructor
(context: Context, data: List<RichFolderAndArticleObject>, internal var body: BodyObject, internal var drawable: Int) : RecyclerView.Adapter<GridAdapterSimpleRecyclerView.ViewHolder>() {

    private var mData:List<RichFolderAndArticleObject> = listOf()
    private val mInflater: LayoutInflater
    private var mClickListener: ItemClickListener? = null

    init {
        this.mInflater = LayoutInflater.from(context)
        this.mData = data
    }

    // inflates the cell layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.magazine_grid_item, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the textview in each cell
    override fun onBindViewHolder(b: ViewHolder, position: Int) {
        val richFolderAndArticleObject = mData[position]

        b.textViewSample.text = richFolderAndArticleObject.string_object_name_or_title
        //     b.imageViewSample.setImageResource(R.drawable.ic_instituto_pacifico_libro_dinamic_24dp)
        if (richFolderAndArticleObject.string_url_ImageWebSource.isNotEmpty()) {
            Glide.with(b.item_view.context )
                    .load(richFolderAndArticleObject.string_url_ImageWebSource)
                    .centerCrop()
                    .placeholder(drawable)
                    .error(R.drawable.ic_image_black_24dp)
                    .into(b.imageViewSample)


            /*   Ion.with(b.context)
                       .load(richFolderAndArticleObject.string_url_ImageWebSource)
                       .withBitmap()
                       .placeholder(R.drawable.ic_image_black_24dp)
                       .error(R.drawable.ic_image_black_24dp)
                       .intoImageView(b.imageViewSample)*/
        } else {
            b.imageViewSample.setImageResource(drawable)
        }
        b.item_view.setOnClickListener {
            body.string_additional_parameter = richFolderAndArticleObject.string_object_id
            StringBusSingleton.instance.string_holder = Gson().toJson(body)
            //   StringBusSingleton.instance.string_holder2 = Gson().toJson(body)
            FragmentTransactionHelper.goToFragment(TreeMagazineFragment(), b.item_view .context)
            // FragmentTransactionHelper.goToFragment(ExpandableMagazineFragment())
        }

       // holder.myTextView!!.text = animal
    }

    // total number of cells
    override fun getItemCount(): Int {
        return mData.size
    }


    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var textViewSample: android.widget.TextView
        var imageViewSample: android.widget.ImageView
        var item_view: android.view.View

        init {

            // myTextView = (TextView) itemView.findViewById(R.id.info_text);
            textViewSample = itemView.findViewById(com.institutopacifico.actualidad.R.id.example_row_tv_title)
            imageViewSample = itemView.findViewById(com.institutopacifico.actualidad.R.id.example_row_iv_image)
            item_view = itemView.findViewById(com.institutopacifico.actualidad.R.id.planview)
          //  item_view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
        }
    }

    // convenience method for getting data at click position
    fun getItem(id: Int): RichFolderAndArticleObject {
        return mData[id]
    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener) {
        this.mClickListener = itemClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View, position: Int){

        }
    }
}