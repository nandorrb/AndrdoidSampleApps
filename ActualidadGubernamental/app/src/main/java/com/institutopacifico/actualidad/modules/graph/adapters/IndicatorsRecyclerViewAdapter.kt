package com.institutopacifico.actualidad.modules.graph.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.modules.graph.objects.IndicadoresFinancierosObject
import com.jjoe64.graphview.series.Series

/**
 * Created by mobile on 10/18/17 at 11:25.
 * Fernando Rubio Burga
 */
class IndicatorsRecyclerViewAdapter
()//  this.mContext = context;
    : RecyclerView.Adapter<IndicatorsRecyclerViewAdapter.CustomViewHolder>() {

    var feedItemList: List<IndicadoresFinancierosObject> = listOf()


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.layout_recyclerview_graph_item, null)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(customViewHolder: CustomViewHolder, i: Int) {

        customViewHolder.textViewFecha.text = feedItemList[i].string_publish_date
        customViewHolder.textViewIGVCompra.text = feedItemList[i].double_igv_buy.toString()
        customViewHolder.textViewIGVVenta.text = feedItemList[i].double_igv_sell.toString()
        customViewHolder.textViewIRCompra.text = feedItemList[i].double_buy_ir.toString()
        customViewHolder.textViewIRventa.text = feedItemList[i].double_sell_ir.toString()
    }

    override fun getItemCount(): Int {
        return feedItemList.size
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var mainLinearLayout: LinearLayout = view.findViewById<View>(R.id.linearlayout_layout_recycler_view_graph_main) as LinearLayout
        var textViewFecha: TextView = view.findViewById<View>(R.id.textview_layout_recycler_view_graph_date) as TextView
        var textViewIGVCompra: TextView = view.findViewById<View>(R.id.textview_layout_recycler_view_graph_item_igv_compra) as TextView
        var textViewIGVVenta: TextView = view.findViewById<View>(R.id.textview_layout_recycler_view_graph_item_igv_venta) as TextView
        var textViewIRCompra: TextView = view.findViewById<View>(R.id.textview_layout_recycler_view_graph_item_ir_compra) as TextView
        var textViewIRventa: TextView = view.findViewById<View>(R.id.textview_layout_recycler_view_graph_item_ir_venta) as TextView

    }
}