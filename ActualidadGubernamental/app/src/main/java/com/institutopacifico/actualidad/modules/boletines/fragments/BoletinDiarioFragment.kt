package com.institutopacifico.actualidad.modules.boletines.fragments

import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.institutopacifico.actualidad.modules.boletines.interfaces.PrimalBoletinFragmentNative


class BoletinDiarioFragment : PrimalBoletinFragmentNative() {
    override var API: String = ApplicationClass.context.resources.getString(R.string.string_api_boletin_diario)
    override var FRAGMENT_TITLE: String = ApplicationClass.context.resources.getString(R.string.fragment_title_boletin_diario)
}