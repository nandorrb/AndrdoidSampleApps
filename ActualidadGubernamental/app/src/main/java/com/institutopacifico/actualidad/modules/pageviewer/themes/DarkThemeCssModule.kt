package com.institutopacifico.actualidad.modules.pageviewer.themes

/**
 * Created by mobile on 5/17/17.
 */

class DarkThemeCssModule : PrimalThemeCssModule() {
    init {
        this.addRule("body", *arrayOf("font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif", "font-size: 14px", "line-height: 1.42857143", "color: #FFF", "background-color: #161418", "margin: 0"))
    }
}
