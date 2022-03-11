package com.institutopacifico.actualidad.objects

import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import com.google.gson.annotations.SerializedName
import com.institutopacifico.actualidad.modules.boletines.fragments.BoletinDiarioFragment

/**
* Created by mobile on 6/23/17.
* Fernando Rubio Burga
*/
class RichFragmentObject {
    var fragment_class: Fragment = Fragment()
    var string_fragment_title: String = ""
    var boolean_its_a_private_service: Boolean = false
    var drawable_resource_icon: Drawable? = null
}