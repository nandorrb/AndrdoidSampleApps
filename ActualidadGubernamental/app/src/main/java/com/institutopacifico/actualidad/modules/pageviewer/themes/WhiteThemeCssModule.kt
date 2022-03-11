package com.institutopacifico.actualidad.modules.pageviewer.themes

/**
 * Created by mobile on 5/17/17.
 */

class WhiteThemeCssModule : PrimalThemeCssModule() {
    init {
        this.addRule("body", *arrayOf("font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif", "font-size: 14px", "line-height: 1.42857143", "color: #000", "background-color: #FFF", "margin: 0"))
    }
}
