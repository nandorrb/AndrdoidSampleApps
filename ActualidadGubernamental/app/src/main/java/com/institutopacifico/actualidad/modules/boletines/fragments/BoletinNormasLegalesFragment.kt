package com.institutopacifico.actualidad.modules.boletines.fragments

import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.institutopacifico.actualidad.modules.boletines.interfaces.PrimalBoletinFragmentNative


class BoletinNormasLegalesFragment : PrimalBoletinFragmentNative() {
    override var API: String = ApplicationClass.context.resources.getString(R.string.string_api_boletin_normas_legales)
    override var FRAGMENT_TITLE: String = ApplicationClass.context.resources.getString(R.string.fragment_title_fragment_boletin_normas_legales)
}