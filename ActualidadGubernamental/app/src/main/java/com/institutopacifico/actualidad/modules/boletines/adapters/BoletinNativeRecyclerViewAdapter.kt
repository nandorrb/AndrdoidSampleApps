package com.institutopacifico.actualidad.modules.boletines.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.institutopacifico.actualidad.modules.boletines.utils.SortingHelpers
import com.institutopacifico.actualidad.modules.boletines.utils.StarredArticlesSingleton
import com.institutopacifico.actualidad.modules.pageviewer.PageViewerActivity
import com.institutopacifico.actualidad.objects.ArticleObject
import com.institutopacifico.actualidad.objects.UserDataSingleton
import com.institutopacifico.actualidad.utils.DateHelper
import com.institutopacifico.actualidad.utils.NotificationsHelper
import com.institutopacifico.actualidad.utils.ShareActionHelper
import com.koushikdutta.ion.Ion
import com.orhanobut.logger.Logger
import java.util.*

/**
 * Created by mobile on 8/31/17.
 * Fernando Rubio Burga
 */

class BoletinNativeRecyclerViewAdapter// Provide a suitable constructor (depends on the kind of dataset)
(var articleObjects: List<ArticleObject>, val lambda_starredItemUpdater: (ArticleObject) -> Unit) : RecyclerView.Adapter<BoletinNativeRecyclerViewAdapter.ViewHolder>() {

    var boletinItemsViews: MutableList<ArticleObject> = mutableListOf()

    private var biasItemsViews: MutableList<ArticleObject> = mutableListOf()
    var currentPosition: Int = 0
    // internal var ViewHolder_MainHolder: BoletinUltimateRecyclerViewAdapter.ViewHolder? = null

    init {
        this.boletinItemsViews = mutableListOf()
        if (articleObjects.isNotEmpty()) {
            this.boletinItemsViews = articleObjects as MutableList<ArticleObject>
        }
    }

    fun insert(viewObject: ArticleObject, position: Int) {
        // Logger.d("Inserted Item");
        defendFromUnsuportedOperationException()
        if (getPositionOf(viewObject) < 0) {
            //This is the line that matters
            boletinItemsViews.add(position, viewObject)
            // Logger.d("inserted Item "+position)
            // insertInternal(boletinItemsViews, viewObject, position)
        }
    }

    fun sort() {
        try {
            defendFromUnsuportedOperationException()

            var b = Arrays.copyOf<ArticleObject, Any>(boletinItemsViews.toTypedArray(), boletinItemsViews.toTypedArray().size, Array<ArticleObject>::class.java)
            b = SortingHelpers.sortDataByDate(b)

            boletinItemsViews = Arrays.asList(*b)
        } catch (e: Exception) {
            Logger.e(e.message)
        }

    }

    fun sortNewsFirst() {
        try {
            defendFromUnsuportedOperationException()

            var b = Arrays.copyOf<ArticleObject, Any>(boletinItemsViews.toTypedArray(), boletinItemsViews.toTypedArray().size, Array<ArticleObject>::class.java)
            b = SortingHelpers.sortDataNewsFirst(b)

            boletinItemsViews = Arrays.asList(*b)
        } catch (e: Exception) {
            Logger.e(e.message)
        }

    }

    private fun defendFromUnsuportedOperationException() {
        //Must create new ArrayList to prevent UnsupportedOperationException
        biasItemsViews = mutableListOf() //ArrayList<ArticleObject>()
        biasItemsViews.addAll(boletinItemsViews)
        boletinItemsViews = mutableListOf() //ArrayList<ArticleObject>()
        boletinItemsViews = biasItemsViews
    }

    fun remove(position: Int) {

        defendFromUnsuportedOperationException()
        boletinItemsViews.removeAt(position)
        // removeInternal(boletinItemsViews, position)
        //   Logger.d("removed Item "+position)
    }

    fun update(objectToUpdate: ArticleObject, position: Int) {
        defendFromUnsuportedOperationException()
        boletinItemsViews[position]=objectToUpdate
    }
    fun update_toggleIsStarred(position: Int) {
        defendFromUnsuportedOperationException()
    //    boletinItemsViews[position].string_title="test test test"
        boletinItemsViews[position].toggleImageButton_isStarred()
    }

    fun clear() {
        Logger.d("cleared RecyclerView")
        defendFromUnsuportedOperationException()
        boletinItemsViews.clear()
        //   clearInternal(boletinItemsViews)
    }


    private fun getItem(position: Int): ArticleObject? {
        var position_bias: Int = position

        /*  if (customHeaderView != null)
              position_bias--
          */
        // URLogs.d("position----"+position);
        if (position_bias >= 0 && position_bias < boletinItemsViews.size)
            return boletinItemsViews[position_bias]
        else
            return null
    }

    fun updateStarredItem(currentObject: ArticleObject) {
        val position = getPositionOf(currentObject)
        // Context context = getContext();
        //    Logger.i("Position: " + position + " Object: " + currentObject.getThisObjectAsJson());
       /* remove(position)
        currentObject.toggleImageButton_isStarred()
        */
        update_toggleIsStarred(position)
        notifyItemChanged(position)

/*
         insert(currentObject, position)
         StarredArticlesSingleton.instance.addItemTo_List_BoletinItemViewObject_BoletinStarredArticles(currentObject)
         StarredArticlesSingleton.instance.sendListToServer(ApplicationClass.context)
         */
        notifyDataSetChanged()
    }

    private fun getPositionOf(`object`: ArticleObject): Int {
        var indexOfThatObject = boletinItemsViews.indexOf(`object`)
        if (indexOfThatObject < 0) {
            for (obj in boletinItemsViews) {
                if (obj.string_url_LinkToWeb.equals(`object`.string_url_LinkToWeb, ignoreCase = true) &&
                        obj.string_DateAdded.equals(`object`.string_DateAdded, ignoreCase = true) &&
                        obj.string_title.equals(`object`.string_title, ignoreCase = true)) {
                    indexOfThatObject = boletinItemsViews.indexOf(obj)
                }
            }
        }
        return indexOfThatObject
    }

    /* constructor(articleObjects: List<ArticleObject>) : this() {
         this.boletinItemsViews = ArrayList<ArticleObject>()
         if (articleObjects.isNotEmpty()) {
             this.boletinItemsViews = articleObjects
         }
     }*/


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    inner class ViewHolder(// each data item is just a string in this case
            mTextView: View) : RecyclerView.ViewHolder(mTextView), View.OnClickListener, View.OnFocusChangeListener {

        var linearLayout_BoletinDiario: LinearLayout
        var linearLayout_BoletinDeNormasLegales: LinearLayout

        var textView_category: TextView
        var textView_organismo: TextView
        var textView_entidad: TextView

        var textViewTitle: TextView
        var textViewDescription: TextView
        //   TextView textView_ObjectAsJson;

        var imageView_MainImage: ImageView
        var imageButton_Share: ImageButton
        var imageButton_Star: ImageButton
        var imageButton_Share2: ImageButton
        var imageButton_Star2: ImageButton
        var textView_date_time_boletin_diario: TextView
        var textView_date_time_normas_legales: TextView
        //   TextView textView_dateTime;
        var progressBarSample: ProgressBar
        var item_view: View

        init {

            linearLayout_BoletinDiario = itemView.findViewById<View>(R.id.linear_layout_production_ultimate_recycler_view_adapter_boletin_diario_bar) as LinearLayout
            linearLayout_BoletinDeNormasLegales = itemView.findViewById<View>(R.id.linear_layout_production_ultimate_recycler_view_adapter_normas_legales_bar) as LinearLayout

            textView_category = itemView.findViewById<View>(R.id.textView_category) as TextView
            textView_organismo = itemView.findViewById<View>(R.id.textView_production_ultimate_recycler_view_adapter_organismo) as TextView
            textView_entidad = itemView.findViewById<View>(R.id.textView_production_ultimate_recycler_view_adapter_entidad) as TextView

            textViewTitle = itemView.findViewById<View>(R.id.title_article_boletin_item_utlimaterecyclerview) as TextView
            textViewDescription = itemView.findViewById<View>(R.id.description_article_boletin_item_utlimaterecyclerview) as TextView


            imageView_MainImage = itemView.findViewById<View>(R.id.imageview_article_boletin_item_utlimaterecyclerview) as ImageView
            imageButton_Share = itemView.findViewById<View>(R.id.imageButton_share_article_boletin_item_UltimateRecyclerView) as ImageButton
            imageButton_Star = itemView.findViewById<View>(R.id.imageButton_star_article_boletin_item_UltimateRecyclerView) as ImageButton
            imageButton_Share2 = itemView.findViewById<View>(R.id.imageButton_share_article_boletin_item_UltimateRecyclerView_2) as ImageButton
            imageButton_Star2 = itemView.findViewById<View>(R.id.imageButton_star_article_boletin_item_UltimateRecyclerView_2) as ImageButton

            textView_date_time_boletin_diario = itemView.findViewById<View>(R.id.textView_uploaded_date_boletin_diario_bar) as TextView
            textView_date_time_normas_legales = itemView.findViewById<View>(R.id.textView_uploaded_date_normas_legales_bar) as TextView
            progressBarSample = itemView.findViewById<View>(R.id.progressbar) as ProgressBar
            progressBarSample.visibility = View.GONE
            item_view = itemView.findViewById<View>(R.id.itemview)

            itemView.setOnClickListener(this)
            itemView.onFocusChangeListener = this


            imageButton_Share.setOnClickListener { ShareActionHelper.share(itemView.context, boletinItemsViews[currentPosition].string_url_LinkToWeb) }
            imageButton_Share2.setOnClickListener { imageButton_Share.performClick() }

            imageButton_Star.setOnClickListener {
                if (!UserDataSingleton.userData.stringInstitutoPacificoUserId.isEmpty()) {
                    val currentObject: ArticleObject = getObjectFromDateTime(textView_date_time_boletin_diario.text.toString(), textViewTitle.text.toString())
                    //.loadObjectFromJson(textView_ObjectAsJson.getText().toString());

                    if (currentObject.boolean_isThisArticleStarredStarred) {
                        imageButton_Star.setImageResource(R.drawable.ic_instituto_pacifico_favoritoblank_black_24dp)
                        imageButton_Star2.setImageResource(R.drawable.ic_instituto_pacifico_favoritoblank_black_24dp)
                    } else {
                        imageButton_Star.setImageResource(R.drawable.ic_star_black_24dp)
                        imageButton_Star2.setImageResource(R.drawable.ic_star_black_24dp)
                    }
                    currentObject.toggleImageButton_isStarred()
                    StarredArticlesSingleton.instance.addItemTo_List_BoletinItemViewObject_BoletinStarredArticles(currentObject)
                    StarredArticlesSingleton.instance.sendListToServer(itemView.context)
                } else {
                    NotificationsHelper.PleaseLogInMessage(itemView.context)
                }
            }

            imageButton_Star2.setOnClickListener { imageButton_Star.performClick() }

        }

        private fun getObjectFromDateTime(DateTime: String, Title: String): ArticleObject {
            var BoletinItemViewObject_thisObject = ArticleObject()
            for (obj in boletinItemsViews) {
                if (obj.string_title.equals(Title, ignoreCase = true) || obj.string_DateAdded.equals(DateTime, ignoreCase = true)) {
                    BoletinItemViewObject_thisObject = obj
                }
            }
            return BoletinItemViewObject_thisObject
        }


        override fun onClick(v: View) {
            val i = Intent(itemView.context, PageViewerActivity::class.java)
            val currentObject = getObjectFromDateTime(textView_date_time_boletin_diario.text.toString(), textViewTitle.text.toString())

            //  i.putExtra("", lambda_starredItemUpdater)
            StarredArticlesSingleton.instance.string_CurrentArticleJson = Gson().toJson(currentObject)
            itemView.clearFocus()
            itemView.context.startActivity(i)
        }


        override fun onFocusChange(v: View, hasFocus: Boolean) {
            if (hasFocus) {
                v.performClick()
            }
        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): BoletinNativeRecyclerViewAdapter.ViewHolder {
        // create a new view
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.production_ultimate_recycler_view_adapter, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //  holder.mTextView.setText(mDataset[position]);
        if (position < itemCount && (position <= boletinItemsViews.size) && (true)) {
            //   Logger.i(String.valueOf(position));

            // ViewHolder_MainHolder = holder as ViewHolder
            currentPosition = position // if (customHeaderView != null) position - 1 else position
            holder.textViewTitle.text = boletinItemsViews[currentPosition].string_title

            //((ViewHolder) holder).textView_ObjectAsJson.setText((BoletinItemsViews.get(currentPosition)).getThisObjectAsJson());
            if (boletinItemsViews[currentPosition].string_article_category.isNotEmpty()) {
                holder.linearLayout_BoletinDiario.visibility = View.VISIBLE
                holder.textView_category.text = boletinItemsViews[currentPosition].string_article_category
            } else {
                holder.linearLayout_BoletinDiario.visibility = View.GONE
            }

            if (boletinItemsViews[currentPosition].string_emisor_entidad.isNotEmpty()) {
                holder.linearLayout_BoletinDeNormasLegales.visibility = View.VISIBLE
                holder.textView_entidad.text = boletinItemsViews[currentPosition].string_emisor_entidad
                holder.textView_organismo.text = boletinItemsViews[currentPosition].string_emisor_organismo
                holder.textViewDescription.visibility = View.VISIBLE
                holder.textViewDescription.text = boletinItemsViews[currentPosition].getTextView_description()
            } else {
                holder.textViewDescription.visibility = View.GONE
                holder.linearLayout_BoletinDeNormasLegales.visibility = View.GONE
            }

            if (boletinItemsViews[currentPosition].string_url_ImageWebSource.isNotEmpty()) {
                holder.imageView_MainImage.visibility = View.VISIBLE

                //   Glide.with(holder.textViewTitle.context).load(boletinItemsViews[currentPosition].string_url_ImageWebSource).centerCrop().into(holder.imageView_MainImage)
                Ion.with(holder.textViewTitle.context)
                        .load(boletinItemsViews[currentPosition].string_url_ImageWebSource)
                        .withBitmap()
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .error(R.drawable.ic_cancel_red_a700_24dp)
                        .intoImageView(holder.imageView_MainImage)
            } else {
                holder.imageView_MainImage.visibility = View.GONE
            }

            if (boletinItemsViews[currentPosition].boolean_isThisArticleStarredStarred) {
                holder.imageButton_Star.setImageResource(R.drawable.ic_star_black_24dp)
                holder.imageButton_Star2.setImageResource(R.drawable.ic_star_black_24dp)
                StarredArticlesSingleton.instance.addItemTo_List_BoletinItemViewObject_BoletinStarredArticles(boletinItemsViews[currentPosition])
            }else{
                holder.imageButton_Star.setImageResource(R.drawable.ic_instituto_pacifico_favoritoblank_black_24dp)
                holder.imageButton_Star2.setImageResource(R.drawable.ic_instituto_pacifico_favoritoblank_black_24dp)
                StarredArticlesSingleton.instance.removeItemFrom_List_BoletinItemViewObject_BoletinStarredArticles(boletinItemsViews[currentPosition])
            }


            //  ((ViewHolder) holder).textView_dateTime.setText(DateHelper.isThisDateFromToday(BoletinItemsViews.get(currentPosition).getDate_boletin_articles_date_added()));
            holder.textView_date_time_boletin_diario.text = DateHelper.isThisDateFromToday(boletinItemsViews[currentPosition].date_boletin_articles_date_added)
            holder.textView_date_time_normas_legales.text = DateHelper.isThisDateFromToday(boletinItemsViews[currentPosition].date_boletin_articles_date_added)

        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return boletinItemsViews.size
    }
}

