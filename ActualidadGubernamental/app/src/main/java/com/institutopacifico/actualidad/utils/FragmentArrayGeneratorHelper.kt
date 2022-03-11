package com.institutopacifico.actualidad.utils

import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.institutopacifico.actualidad.fragments.SettingsFragment
import com.institutopacifico.actualidad.modules.account.MainAccountFragment
import com.institutopacifico.actualidad.modules.boletines.fragments.BoletinDiarioFragment
import com.institutopacifico.actualidad.modules.boletines.fragments.BoletinNormasLegalesFragment
import com.institutopacifico.actualidad.modules.calendar.VencimientosYObligacionesFragment
import com.institutopacifico.actualidad.modules.graph.TipoDeCambioFragment
import com.institutopacifico.actualidad.modules.treesfoldersandfiles.services.MainCodigos
import com.institutopacifico.actualidad.modules.treesfoldersandfiles.services.MainMagazineFragment
import com.institutopacifico.actualidad.modules.treesfoldersandfiles.services.MainPionner
import com.institutopacifico.actualidad.modules.treesfoldersandfiles.services.MainSistemaInformacionFragment
import com.institutopacifico.actualidad.objects.RichFragmentObject

/**
 * Created by mobile on 6/21/17.
 * Fernando Rubio Burga
 */
class FragmentArrayGeneratorHelper {
    //   var fragments_packages_names = ApplicationClass.context.resources.getStringArray(R.array.fragmets_packages_names)
    //  var string_array_fragment_titles = ApplicationClass.context.resources.getStringArray(R.array.fragmets_names_or_titles)


    fun getFragmentArray(): MutableList<RichFragmentObject> {
        var fragmentList: MutableList<RichFragmentObject> = mutableListOf()
        var integer_drawable_id: Int
        var newRichFragment: RichFragmentObject

        //BOLETIN DIARIO
        newRichFragment = RichFragmentObject()
        newRichFragment.fragment_class = BoletinDiarioFragment()
        newRichFragment.string_fragment_title = ApplicationClass.context.getString(R.string.fragment_title_boletin_diario)
        integer_drawable_id = ApplicationClass.context.resources.getIdentifier(
                ApplicationClass.context.getString(R.string.fragment_drawable_boletin_diario)
                , "drawable", ApplicationClass.context.packageName);
        newRichFragment.drawable_resource_icon = ApplicationClass.context.resources.getDrawable(integer_drawable_id)
        fragmentList.add(newRichFragment)

        //------------------------
        //BOLETIN DE NORMAS LEGALES
        newRichFragment = RichFragmentObject()
        newRichFragment.fragment_class = BoletinNormasLegalesFragment()
        newRichFragment.string_fragment_title = ApplicationClass.context.getString(R.string.fragment_title_fragment_boletin_normas_legales)
        integer_drawable_id = ApplicationClass.context.resources.getIdentifier(
                ApplicationClass.context.getString(R.string.fragment_drawable_fragment_boletin_normas_legales)
                , "drawable", ApplicationClass.context.packageName)
        newRichFragment.drawable_resource_icon = ApplicationClass.context.resources.getDrawable(integer_drawable_id)
        fragmentList.add(newRichFragment)

        //------------------------
        //REVISTA
        newRichFragment = RichFragmentObject()
        newRichFragment.fragment_class = MainMagazineFragment()
        newRichFragment.string_fragment_title = ApplicationClass.context.getString(R.string.fragment_title_fragment_magazine)
        newRichFragment.boolean_its_a_private_service = true
        integer_drawable_id = ApplicationClass.context.resources.getIdentifier(
                ApplicationClass.context.getString(R.string.fragment_drawable_fragment_magazine)
                , "drawable", ApplicationClass.context.packageName)
        newRichFragment.drawable_resource_icon = ApplicationClass.context.resources.getDrawable(integer_drawable_id)
        fragmentList.add(newRichFragment)

        //------------------------
        //SISTEMA DE INFORMACION
        newRichFragment = RichFragmentObject()
        newRichFragment.fragment_class = MainSistemaInformacionFragment()
        newRichFragment.string_fragment_title = ApplicationClass.context.getString(R.string.fragment_title_fragment_sistema_de_informacion)
        newRichFragment.boolean_its_a_private_service = true
        integer_drawable_id = ApplicationClass.context.resources.getIdentifier(
                ApplicationClass.context.getString(R.string.fragment_drawable_fragment_sistema_de_informacion)
                , "drawable", ApplicationClass.context.packageName)
        newRichFragment.drawable_resource_icon = ApplicationClass.context.resources.getDrawable(integer_drawable_id)
        fragmentList.add(newRichFragment)

        //------------------------
        //COMPENDIO
        newRichFragment = RichFragmentObject()
        newRichFragment.fragment_class = MainCodigos()
        newRichFragment.string_fragment_title = ApplicationClass.context.getString(R.string.fragment_title_fragment_codigos)
        newRichFragment.boolean_its_a_private_service = true
        integer_drawable_id = ApplicationClass.context.resources.getIdentifier(
                ApplicationClass.context.getString(R.string.fragment_drawable_fragment_magazine)
                , "drawable", ApplicationClass.context.packageName)
        newRichFragment.drawable_resource_icon = ApplicationClass.context.resources.getDrawable(integer_drawable_id)
        // if (ApplicationClass.context.getString(R.string.revista_api_code) != "AG") {
        fragmentList.add(newRichFragment)
        // }

        //------------------------
        //PIONNER
        newRichFragment = RichFragmentObject()
        newRichFragment.fragment_class = MainPionner()
        newRichFragment.string_fragment_title = "PIONNER"
        newRichFragment.boolean_its_a_private_service = true
        integer_drawable_id = ApplicationClass.context.resources.getIdentifier(
                ApplicationClass.context.getString(R.string.fragment_drawable_fragment_magazine)
                , "drawable", ApplicationClass.context.packageName)
        newRichFragment.drawable_resource_icon = ApplicationClass.context.resources.getDrawable(integer_drawable_id)
        if (ApplicationClass.context.getString(R.string.revista_api_code) == "AE") {
            fragmentList.add(newRichFragment)
        }

        //------------------------
        //Vencimientos y Obligaciones
        newRichFragment = RichFragmentObject()
        newRichFragment.fragment_class = VencimientosYObligacionesFragment()
        newRichFragment.string_fragment_title = "VENCIMIENTOS Y OBLIGACIONES"
        newRichFragment.boolean_its_a_private_service = false
        integer_drawable_id = ApplicationClass.context.resources.getIdentifier(
                ApplicationClass.context.getString(R.string.fragment_drawable_fragment_magazine)
                , "drawable", ApplicationClass.context.packageName)
        newRichFragment.drawable_resource_icon = ApplicationClass.context.resources.getDrawable(integer_drawable_id)
        if (ApplicationClass.context.getString(R.string.revista_api_code) == "AG" ||
                ApplicationClass.context.getString(R.string.revista_api_code) == "AE"
                ) {
            fragmentList.add(newRichFragment)
        }

        //------------------------
        //Indicadores Financieros
        newRichFragment = RichFragmentObject()
        newRichFragment.fragment_class = TipoDeCambioFragment()
        newRichFragment.string_fragment_title = "TIPO DE CAMBIO"
        newRichFragment.boolean_its_a_private_service = false
        integer_drawable_id = ApplicationClass.context.resources.getIdentifier(
                ApplicationClass.context.getString(R.string.fragment_drawable_fragment_magazine)
                , "drawable", ApplicationClass.context.packageName)
        newRichFragment.drawable_resource_icon = ApplicationClass.context.resources.getDrawable(integer_drawable_id)
        if (ApplicationClass.context.getString(R.string.revista_api_code) == "AG" ||
                ApplicationClass.context.getString(R.string.revista_api_code) == "AE"
                ) {
            fragmentList.add(newRichFragment)
        }


        //------------------------
        //SPIJ
        /*newRichFragment = RichFragmentObject()
        newRichFragment.fragment_class = WebViewerFragment()
        newRichFragment.string_fragment_title = ApplicationClass.context.getString(R.string.fragment_title_fragment_spij)
        integer_drawable_id = ApplicationClass.context.resources.getIdentifier(
                ApplicationClass.context.getString(R.string.fragment_drawable_fragment_newspaper)
                , "drawable", ApplicationClass.context.packageName);
        newRichFragment.drawable_resource_icon = ApplicationClass.context.resources.getDrawable(integer_drawable_id)
        fragmentList.add(newRichFragment)
        */

        //------------------------
        //CONFIGURACION
        newRichFragment = RichFragmentObject()
        newRichFragment.fragment_class = SettingsFragment()
        newRichFragment.string_fragment_title = ApplicationClass.context.getString(R.string.fragment_title_fragment_settings)
        integer_drawable_id = ApplicationClass.context.resources.getIdentifier(
                ApplicationClass.context.getString(R.string.fragment_drawable_fragment_settings)
                , "drawable", ApplicationClass.context.packageName);
        newRichFragment.drawable_resource_icon = ApplicationClass.context.resources.getDrawable(integer_drawable_id)
        fragmentList.add(newRichFragment)

        //------------------------
        //CUENTA
        newRichFragment = RichFragmentObject()
        newRichFragment.fragment_class = MainAccountFragment()
        newRichFragment.string_fragment_title = ApplicationClass.context.getString(R.string.fragment_title_fragment_account)
        integer_drawable_id = ApplicationClass.context.resources.getIdentifier(
                ApplicationClass.context.getString(R.string.fragment_drawable_fragment_account)
                , "drawable", ApplicationClass.context.packageName);
        newRichFragment.drawable_resource_icon = ApplicationClass.context.resources.getDrawable(integer_drawable_id)
        fragmentList.add(newRichFragment)

        //------------------------
        //TEST
        /*   newRichFragment = RichFragmentObject()
           newRichFragment.fragment_class = TestButtonFragment()
           newRichFragment.string_fragment_title = ApplicationClass.context.getString(R.string.fragment_title_fragment_account)
           integer_drawable_id = ApplicationClass.context.resources.getIdentifier(
                   ApplicationClass.context.getString(R.sting.fragment_drawable_fragment_account)
                   , "drawable", ApplicationClass.context.packageName);
           newRichFragment.drawable_resource_icon = ApplicationClass.context.resources.getDrawable(integer_drawable_id)
           fragmentList.add(newRichFragment)
           */

        return fragmentList
    }


}