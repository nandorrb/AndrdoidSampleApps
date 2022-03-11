package com.institutopacifico.actualidad.modules.boletines.utils

/**
 * Created by mobile on 6/13/17.
 */

class BoletinUltimateRecyclerViewDisableLoadMoreSingleton private constructor() {
    var boolean_disable_more_data = false

    companion object {
        val instance = com.institutopacifico.actualidad.modules.boletines.utils.BoletinUltimateRecyclerViewDisableLoadMoreSingleton()
    }
}
