package com.institutopacifico.actualidad.modules.graph.objects

import com.google.gson.annotations.SerializedName

/**
 * Created by mobile on 10/12/17 at 11:10.
 * Fernando Rubio Burga
 */
open class IndicadoresFinancierosObject {
    @SerializedName("idtipocambio")
    var string_id: String = ""

    @SerializedName("id_moneda")
    var string_main_coin: String = ""

    @SerializedName("id_moneda_cambio")
    var string_second_coin: String = ""

    @SerializedName("fechapublicacion")
    var string_publish_date: String = ""

    @SerializedName("fechareg")
    var string_register_date: String = ""

    @SerializedName("compra_igv")
    var double_igv_buy: Double = 0.0

    @SerializedName("venta_igv")
    var double_igv_sell: Double =0.0

    @SerializedName("compra_ir")
    var double_buy_ir: Double = 0.0

    @SerializedName("venta_ir")
    var double_sell_ir: Double =0.0

}