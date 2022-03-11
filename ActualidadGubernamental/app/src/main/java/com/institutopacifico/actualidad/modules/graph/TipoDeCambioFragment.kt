package com.institutopacifico.actualidad.modules.graph

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.institutopacifico.actualidad.modules.graph.adapters.IndicatorsRecyclerViewAdapter
import com.institutopacifico.actualidad.network.IonObject
import com.institutopacifico.actualidad.objects.BodyObject
import com.institutopacifico.actualidad.objects.ResponseObject
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.DataPointInterface
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.Series
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_default_graph.*
import kotlinx.android.synthetic.main.layout_recyclerview_graph_item.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by mobile on 9/22/17 at 15:51.
 * Fernando Rubio Burga
 */
open class TipoDeCambioFragment : Fragment() {

    lateinit var root: ViewGroup
    private val format = SimpleDateFormat("yyyy-MM-dd") // HH:mm:ss")
    val STRING_USD = "USD"
    val STRING_PEN = "PEN"
    val STRING_EUR = "EUR"

    val recyclerviewAdapter = IndicatorsRecyclerViewAdapter()

    // set Data
    var series_compra_igv = LineGraphSeries(arrayOf())
    var series_venta_igv = LineGraphSeries(arrayOf())
    var series_compra_ir = LineGraphSeries(arrayOf())
    var series_venta_ir = LineGraphSeries(arrayOf())


    private val setTableLayoutListeners = {

        /*
                //Set Colors
                series_compra_igv.color = Color.RED
                series_venta_igv.color = Color.BLUE
                series_compra_ir.color = Color.BLACK
                series_venta_ir.color = Color.GREEN

                //Set Draw Data Points
                series_compra_igv.isDrawDataPoints = true
                series_venta_igv.isDrawDataPoints = true
                series_compra_ir.isDrawDataPoints = true
                series_venta_ir.isDrawDataPoints = true

                //RadiusDraw Data Points
                val radius = 15F
                series_compra_igv.dataPointsRadius = radius
                series_venta_igv.dataPointsRadius = radius
                series_compra_ir.dataPointsRadius = radius
                series_venta_ir.dataPointsRadius = radius

                //Set Colors
                series_compra_igv.title = "Precio de compra IGV"
                series_venta_igv.title = "Precio de venta IGV"
                series_compra_ir.title = "Precio de compra IR"
                series_venta_ir.title = "Precio de venta IR"

                val lambdaDataPointListener = { series: Series<*>, point: DataPointInterface?, tv: TextView? ->
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = point?.x?.toLong() ?: 0
                    val datePublish: Date = calendar.time
                    val stringPublishDate: String = format.format(datePublish)
                    tv?.text = (series.title + "\n"
                            + stringPublishDate + "\n"
                            + " S/ " + point?.y
                            )
                    //   tv?.setTextColor(series.color)
                }


                //Set Colors
                series_compra_igv.setOnDataPointTapListener { series, datapoint ->
                    lambdaDataPointListener(series, datapoint, textView_fragment_default_graph_value)
                }

                series_venta_igv.setOnDataPointTapListener { series, datapoint ->
                    lambdaDataPointListener(series, datapoint, textView_fragment_default_graph_value)
                }

                series_compra_ir.setOnDataPointTapListener { series, datapoint ->
                    lambdaDataPointListener(series, datapoint, textView_fragment_default_graph_value)
                }

                series_venta_ir.setOnDataPointTapListener { series, datapoint ->
                    lambdaDataPointListener(series, datapoint, textView_fragment_default_graph_value)
                }
        */

        // TABLE LAYOUT
        Logger.d("Set Listener")

        tablelayout_fragment_default_graph?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                requestValues(
                        if (tab.position == 0) {
                            STRING_USD
                        } else {
                            STRING_EUR
                        }
                        , STRING_PEN)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        // RECYCLERVIEW
        recyclerview_fragment_default_graph_allvalues?.adapter = recyclerviewAdapter
        recyclerview_fragment_default_graph_allvalues?.layoutManager = LinearLayoutManager(root.context)
/*
        //GRAPHVIEW
        graphview_fragment_default_graph?.gridLabelRenderer?.labelFormatter = DateAsXAxisLabelFormatter(root.context)
        graphview_fragment_default_graph?.gridLabelRenderer?.isHumanRounding = true
        graphview_fragment_default_graph?.gridLabelRenderer?.horizontalAxisTitle = "FECHA"
        graphview_fragment_default_graph?.gridLabelRenderer?.verticalAxisTitle = "S/."
        //  graphview_fragment_default_graph?.gridLabelRenderer?.numHorizontalLabels = 3

        // set manual x bounds to have nice steps
        graphview_fragment_default_graph?.viewport?.isXAxisBoundsManual = true

        //Legend
        graphview_fragment_default_graph?.legendRenderer?.isVisible = true
        graphview_fragment_default_graph?.legendRenderer?.align = LegendRenderer.LegendAlign.TOP

        // SCROLLABLE AND ZOOMABLE
        graphview_fragment_default_graph?.viewport?.isScrollable = true // enables horizontal scrolling
        graphview_fragment_default_graph?.viewport?.setScrollableY(true) // enables vertical scrolling
        graphview_fragment_default_graph?.viewport?.isScalable = true // enables horizontal zooming and scrolling
        graphview_fragment_default_graph?.viewport?.setScalableY(true) // enables vertical zooming and scrolling
*/
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater?.inflate(R.layout.fragment_default_graph, null) as ViewGroup
        requestData()
        return root
    }

    override fun onResume() {
        super.onResume()
        requestData()
    }

    private fun requestData() {
        requestValues(STRING_USD, STRING_PEN)
        setTableLayoutListeners()
    }

    fun requestValues(from: String, to: String) {
        val body = BodyObject()
        body.boolean_compressed = true
        body.string_request = from
        body.string_additional_parameter = to
        IonObject().makeAsynchronousCompressedRequest(root.context, "indicadores_tipocambio", body, AsynchronousResponse)
        if (imageview_fragment_default_graph_1 != null && imageview_fragment_default_graph_2 != null) {
            Glide.with(root.context)
                    .load(if (from == STRING_EUR) {
                        "http://aempresarial.com/imagenes/euro-flag.png"
                    } else {
                        "http://aempresarial.com/imagenes/dollar-flag.png"
                    })
                    .fitCenter()
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .into(imageview_fragment_default_graph_1)


            Glide.with(root.context)
                    .load("http://aempresarial.com/imagenes/peru-flag.png")
                    .fitCenter()
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .into(imageview_fragment_default_graph_2)

            textview_from__imageview_fragment_default_graph_1?.text = from
            textview_from__imageview_fragment_default_graph_2?.text = to
        }
    }


    val AsynchronousResponse = { ResponseObject_AsynchronousResponse: ResponseObject ->

        if (ResponseObject_AsynchronousResponse.array_indicadores_tipocambio.isNotEmpty()) {


            val mutableList_compra_igv: MutableList<DataPoint> = mutableListOf()
            val mutableList_venta_igv: MutableList<DataPoint> = mutableListOf()
            val mutableList_compra_ir: MutableList<DataPoint> = mutableListOf()
            val mutableList_venta_ir: MutableList<DataPoint> = mutableListOf()

            for (a in ResponseObject_AsynchronousResponse.array_indicadores_tipocambio.reversed()) {


                //Prevent day 0 spike
                if (a.double_buy_ir <= 0.0 || a.double_sell_ir <= 0.0) {
                    a.double_buy_ir = a.double_igv_buy
                    a.double_sell_ir = a.double_igv_sell
                }


                val date: Date = format.parse(a.string_publish_date)
                //     Logger.d(date.time - date0.time)

                mutableList_compra_igv.add(DataPoint(((date.time)).toDouble(), a.double_igv_buy))
                mutableList_venta_igv.add(DataPoint(((date.time)).toDouble(), a.double_igv_sell))
                mutableList_compra_ir.add(DataPoint(((date.time)).toDouble(), a.double_buy_ir))
                mutableList_venta_ir.add(DataPoint(((date.time)).toDouble(), a.double_sell_ir))
                //  i++
            }


            // set Data
            series_compra_igv = LineGraphSeries(mutableList_compra_igv.toTypedArray())
            series_venta_igv = LineGraphSeries(mutableList_venta_igv.toTypedArray())
            series_compra_ir = LineGraphSeries(mutableList_compra_ir.toTypedArray())
            series_venta_ir = LineGraphSeries(mutableList_venta_ir.toTypedArray())

            //Set Colors
            series_compra_igv.color = Color.RED
            series_venta_igv.color = Color.BLUE
            series_compra_ir.color = Color.BLACK
            series_venta_ir.color = Color.GREEN

            //Set Draw Data Points
            series_compra_igv.isDrawDataPoints = true
            series_venta_igv.isDrawDataPoints = true
            series_compra_ir.isDrawDataPoints = true
            series_venta_ir.isDrawDataPoints = true

            //RadiusDraw Data Points
            val radius = 15F
            series_compra_igv.dataPointsRadius = radius
            series_venta_igv.dataPointsRadius = radius
            series_compra_ir.dataPointsRadius = radius
            series_venta_ir.dataPointsRadius = radius

            //Set Colors
            series_compra_igv.title = "Precio de compra IGV"
            series_venta_igv.title = "Precio de venta IGV"
            series_compra_ir.title = "Precio de compra IR"
            series_venta_ir.title = "Precio de venta IR"


            val lambdaDataPointListener = { series: Series<*>, point: DataPointInterface?, tv: TextView? ->
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = point?.x?.toLong() ?: 0
                val datePublish: Date = calendar.time
                val stringPublishDate: String = format.format(datePublish)
                tv?.text = (series.title + "\n"
                        + stringPublishDate + "\n"
                        + " S/ " + point?.y
                        )
                //   tv?.setTextColor(series.color)
            }

            //Set Colors
            series_compra_igv.setOnDataPointTapListener { series, datapoint ->
                lambdaDataPointListener(series, datapoint, textView_fragment_default_graph_value)
            }

            series_venta_igv.setOnDataPointTapListener { series, datapoint ->
                lambdaDataPointListener(series, datapoint, textView_fragment_default_graph_value)
            }

            series_compra_ir.setOnDataPointTapListener { series, datapoint ->
                lambdaDataPointListener(series, datapoint, textView_fragment_default_graph_value)
            }

            series_venta_ir.setOnDataPointTapListener { series, datapoint ->
                lambdaDataPointListener(series, datapoint, textView_fragment_default_graph_value)
            }


            //Clear
            graphview_fragment_default_graph?.removeAllSeries()

            // Add Series
            graphview_fragment_default_graph?.addSeries(series_compra_igv)
            graphview_fragment_default_graph?.addSeries(series_venta_igv)
            graphview_fragment_default_graph?.addSeries(series_compra_ir)
            graphview_fragment_default_graph?.addSeries(series_venta_ir)

            graphview_fragment_default_graph?.gridLabelRenderer?.labelFormatter = DateAsXAxisLabelFormatter(root.context)
            graphview_fragment_default_graph?.gridLabelRenderer?.isHumanRounding = true
            graphview_fragment_default_graph?.gridLabelRenderer?.horizontalAxisTitle = "FECHA"
            graphview_fragment_default_graph?.gridLabelRenderer?.verticalAxisTitle = "S/."
            //  graphview_fragment_default_graph?.gridLabelRenderer?.numHorizontalLabels = 3

            // set manual x bounds to have nice steps
            graphview_fragment_default_graph?.viewport?.setMinX((format.parse(ResponseObject_AsynchronousResponse.array_indicadores_tipocambio.reversed()[0].string_publish_date).time - 86400000).toDouble())
            graphview_fragment_default_graph?.viewport?.setMaxX(format.parse(ResponseObject_AsynchronousResponse.array_indicadores_tipocambio[0].string_publish_date).time.toDouble())
            graphview_fragment_default_graph?.viewport?.isXAxisBoundsManual = true

            //Legend
            graphview_fragment_default_graph?.legendRenderer?.isVisible = true
            graphview_fragment_default_graph?.legendRenderer?.align = LegendRenderer.LegendAlign.TOP

            // SCROLLABLE AND ZOOMABLE
            graphview_fragment_default_graph?.viewport?.isScrollable = true // enables horizontal scrolling
            graphview_fragment_default_graph?.viewport?.setScrollableY(false) // enables vertical scrolling
            graphview_fragment_default_graph?.viewport?.isScalable = true // enables horizontal zooming and scrolling
            graphview_fragment_default_graph?.viewport?.setScalableY(false) // enables vertical zooming and scrolling


            textview_layout_recycler_view_graph_date.text = ResponseObject_AsynchronousResponse.array_indicadores_tipocambio[0].string_publish_date
            textview_layout_recycler_view_graph_item_igv_compra.text = ResponseObject_AsynchronousResponse.array_indicadores_tipocambio[0].double_igv_buy.toString()
            textview_layout_recycler_view_graph_item_igv_venta.text = ResponseObject_AsynchronousResponse.array_indicadores_tipocambio[0].double_igv_sell.toString()
            textview_layout_recycler_view_graph_item_ir_compra.text = ResponseObject_AsynchronousResponse.array_indicadores_tipocambio[0].double_buy_ir.toString()
            textview_layout_recycler_view_graph_item_ir_venta.text = ResponseObject_AsynchronousResponse.array_indicadores_tipocambio[0].double_sell_ir.toString()

            recyclerviewAdapter.feedItemList = ResponseObject_AsynchronousResponse.array_indicadores_tipocambio.toList()

            recyclerviewAdapter.notifyDataSetChanged()


            //    setTableLayoutListeners()

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        ApplicationClass.getRefWatcher(activity)?.watch(this)
    }
}