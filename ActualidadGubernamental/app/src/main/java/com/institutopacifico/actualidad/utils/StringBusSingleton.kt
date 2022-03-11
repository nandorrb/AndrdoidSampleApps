package com.institutopacifico.actualidad.utils

/**
 * Created by mobile on 6/27/17.
 * Fernando Rubio Burga
 */
class StringBusSingleton private constructor() {
    init {
        println("This ($this) is a singleton")
    }

    private object Holder {
        val INSTANCE = StringBusSingleton()
    }

    companion object {
        val instance: StringBusSingleton by lazy { Holder.INSTANCE }
    }

    var string_holder: String = ""
    var string_holder2: String = ""
}