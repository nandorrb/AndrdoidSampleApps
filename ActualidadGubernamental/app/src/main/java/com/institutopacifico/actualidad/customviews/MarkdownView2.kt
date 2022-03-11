package com.institutopacifico.actualidad.customviews

import android.content.Context
import android.content.res.TypedArray
import android.os.AsyncTask
import android.text.TextUtils
import android.util.AttributeSet
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.FrameLayout

import com.orhanobut.logger.Logger
import com.vladsch.flexmark.Extension
import com.vladsch.flexmark.ast.AutoLink
import com.vladsch.flexmark.ast.FencedCodeBlock
import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.ast.Image
import com.vladsch.flexmark.ast.Link
import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.ast.util.TextCollectingVisitor
import com.vladsch.flexmark.ext.abbreviation.Abbreviation
import com.vladsch.flexmark.ext.abbreviation.AbbreviationExtension
import com.vladsch.flexmark.ext.autolink.AutolinkExtension
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughSubscriptExtension
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.html.AttributeProvider
import com.vladsch.flexmark.html.CustomNodeRenderer
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.html.HtmlWriter
import com.vladsch.flexmark.html.IndependentAttributeProviderFactory
import com.vladsch.flexmark.html.renderer.AttributablePart
import com.vladsch.flexmark.html.renderer.LinkType
import com.vladsch.flexmark.html.renderer.NodeRenderer
import com.vladsch.flexmark.html.renderer.NodeRendererContext
import com.vladsch.flexmark.html.renderer.NodeRendererFactory
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler
import com.vladsch.flexmark.html.renderer.ResolvedLink
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.superscript.SuperscriptExtension
import com.vladsch.flexmark.util.html.Attributes
import com.vladsch.flexmark.util.html.Escaping
import com.vladsch.flexmark.util.options.DataHolder
import com.vladsch.flexmark.util.options.MutableDataHolder
import com.vladsch.flexmark.util.options.MutableDataSet

import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection
import java.util.Arrays
import java.util.HashSet
import java.util.LinkedHashSet
import java.util.LinkedList

import br.tiagohm.markdownview.MarkdownView
import br.tiagohm.markdownview.Utils
import br.tiagohm.markdownview.css.ExternalStyleSheet
import br.tiagohm.markdownview.css.StyleSheet
import br.tiagohm.markdownview.ext.button.ButtonExtension
import br.tiagohm.markdownview.ext.emoji.EmojiExtension
import br.tiagohm.markdownview.ext.kbd.Keystroke
import br.tiagohm.markdownview.ext.kbd.KeystrokeExtension
import br.tiagohm.markdownview.ext.label.LabelExtension
import br.tiagohm.markdownview.ext.localization.LocalizationExtension
import br.tiagohm.markdownview.ext.mark.Mark
import br.tiagohm.markdownview.ext.mark.MarkExtension
import br.tiagohm.markdownview.ext.mathjax.MathJax
import br.tiagohm.markdownview.ext.mathjax.MathJaxExtension
import br.tiagohm.markdownview.ext.twitter.TwitterExtension
import br.tiagohm.markdownview.ext.video.VideoLinkExtension
import br.tiagohm.markdownview.js.ExternalScript
import br.tiagohm.markdownview.js.JavaScript

/**
 * Created by mobile on 9/12/17.
 * Fernando Rubio Burga
 */

class MarkdownView2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    var string_generated_html: String = ""
    private val OPTIONS: DataHolder
    private val mStyleSheets: MutableList<StyleSheet>
    private val mScripts: HashSet<JavaScript>
    private val mWebView: WebView
    private var mEscapeHtml: Boolean = false
    private var mOnElementListener: MarkdownView.OnElementListener? = null

    init {
        this.OPTIONS = MutableDataSet().set(FootnoteExtension.FOOTNOTE_REF_PREFIX, "[").set(FootnoteExtension.FOOTNOTE_REF_SUFFIX, "]").set(HtmlRenderer.FENCED_CODE_LANGUAGE_CLASS_PREFIX, "").set(HtmlRenderer.FENCED_CODE_NO_LANGUAGE_CLASS, "nohighlight")
        this.mStyleSheets = mutableListOf()
        this.mScripts = HashSet()
        this.mEscapeHtml = true
        this.mWebView = WebView(context, null as AttributeSet?, 0)
        this.mWebView.layoutParams = FrameLayout.LayoutParams(-1, -1)
        (this.OPTIONS as MutableDataHolder).set(LocalizationExtension.LOCALIZATION_CONTEXT, context)

        try {
            this.mWebView.webChromeClient = WebChromeClient()
            this.mWebView.settings.javaScriptEnabled = true
            this.mWebView.settings.loadsImagesAutomatically = true
            this.mWebView.addJavascriptInterface(this.EventDispatcher(), "android")
        } catch (var6: Exception) {
            var6.printStackTrace()
        }

        try {
            val attr = this.context.obtainStyledAttributes(attrs, br.tiagohm.markdownview.R.styleable.MarkdownView)
            this.mEscapeHtml = attr.getBoolean(br.tiagohm.markdownview.R.styleable.MarkdownView_escapeHtml, true)
            attr.recycle()
        } catch (var5: Exception) {
            var5.printStackTrace()
        }

        this.addView(this.mWebView)
        this.addJavascript(JQUERY_3)
    }

    fun setOnElementListener(listener: MarkdownView.OnElementListener) {
        this.mOnElementListener = listener
    }

    fun setEscapeHtml(flag: Boolean): MarkdownView2 {
        this.mEscapeHtml = flag
        return this
    }

    fun setEmojiRootPath(path: String): MarkdownView2 {
        (this.OPTIONS as MutableDataHolder).set(EmojiExtension.ROOT_IMAGE_PATH, path)
        return this
    }

    fun setEmojiImageExtension(ext: String): MarkdownView2 {
        (this.OPTIONS as MutableDataHolder).set(EmojiExtension.IMAGE_EXT, ext)
        return this
    }

    fun addStyleSheet(s: StyleSheet?): MarkdownView2 {
        if (s != null && !this.mStyleSheets.contains(s)) {
            this.mStyleSheets.add(s)
        }

        return this
    }

    fun replaceStyleSheet(oldStyle: StyleSheet, newStyle: StyleSheet?): MarkdownView2 {
        if (oldStyle !== newStyle) {
            if (newStyle == null) {
                this.mStyleSheets.remove(oldStyle)
            } else {
                val index = this.mStyleSheets.indexOf(oldStyle)
                if (index >= 0) {
                    this.mStyleSheets[index] = newStyle
                } else {
                    this.addStyleSheet(newStyle)
                }
            }
        }

        return this
    }

    fun clearStyleSheets(): MarkdownView2 {
        this.mStyleSheets.clear()
        return this
    }

    fun removeStyleSheet(s: StyleSheet): MarkdownView2 {
        this.mStyleSheets.remove(s)
        return this
    }

    fun addJavascript(js: JavaScript): MarkdownView2 {
        this.mScripts.add(js)
        return this
    }

    fun removeJavaScript(js: JavaScript): MarkdownView2 {
        this.mScripts.remove(js)
        return this
    }

    private fun parseBuildAndRender(text: String): String {
        val parser = Parser.builder(this.OPTIONS).extensions(EXTENSIONS).build()
        val renderer = HtmlRenderer.builder(this.OPTIONS).escapeHtml(this.mEscapeHtml).attributeProviderFactory(object : IndependentAttributeProviderFactory() {
            override fun create(context: NodeRendererContext): AttributeProvider {
                return this@MarkdownView2.CustomAttributeProvider()
            }
        }).nodeRendererFactory(MarkdownView.NodeRendererFactoryImpl()).extensions(EXTENSIONS).build()
        return renderer.render(parser.parse(text))
    }

    fun loadMarkdown(text: String) {
        var html = this.parseBuildAndRender(text)
        val sb = StringBuilder()
        sb.append("<html>\n")
        sb.append("<head>\n")
        var var4: Iterator<*> = this.mStyleSheets.iterator()

        while (var4.hasNext()) {
            val s = var4.next() as StyleSheet
            sb.append(s.toHTML())
        }

        var4 = this.mScripts.iterator()

        while (var4.hasNext()) {
            sb.append(var4.next().toHTML())
        }

        sb.append("</head>\n")
        sb.append("<body>\n")
        sb.append("<div class=\"container\">\n")
        sb.append(html)
        sb.append("</div>\n")
        sb.append("</body>\n")
        sb.append("</html>")
        html = sb.toString()
        html = EliminateAsterisk(html)
        string_generated_html = html
        Logger.d(html)
        this.mWebView.loadDataWithBaseURL("", html, "text/html", "UTF-8", "")
    }

    private fun EliminateAsterisk(html: String): String {
        return html.replace("**", "").replace("  ", " ").replace("_", "")
    }

    fun loadMarkdownFromAsset(path: String) {
        this.loadMarkdown(Utils.getStringFromAssetFile(this.context.assets, path))
    }

    fun loadMarkdownFromFile(file: File) {
        this.loadMarkdown(Utils.getStringFromFile(file))
    }

    fun loadMarkdownFromUrl(url: String) {
        this.LoadMarkdownUrlTask().execute(url)
    }

    protected inner class EventDispatcher {

        @JavascriptInterface
        fun onButtonTap(id: String) {
            if (this@MarkdownView2.mOnElementListener != null) {
                this@MarkdownView2.mOnElementListener!!.onButtonTap(id)
            }

        }

        @JavascriptInterface
        fun onCodeTap(lang: String, code: String) {
            if (this@MarkdownView2.mOnElementListener != null) {
                this@MarkdownView2.mOnElementListener!!.onCodeTap(lang, code)
            }

        }

        @JavascriptInterface
        fun onHeadingTap(level: Int, text: String) {
            if (this@MarkdownView2.mOnElementListener != null) {
                this@MarkdownView2.mOnElementListener!!.onHeadingTap(level, text)
            }

        }

        @JavascriptInterface
        fun onImageTap(src: String, width: Int, height: Int) {
            if (this@MarkdownView2.mOnElementListener != null) {
                this@MarkdownView2.mOnElementListener!!.onImageTap(src, width, height)
            }

        }

        @JavascriptInterface
        fun onMarkTap(text: String) {
            if (this@MarkdownView2.mOnElementListener != null) {
                this@MarkdownView2.mOnElementListener!!.onMarkTap(text)
            }

        }

        @JavascriptInterface
        fun onKeystrokeTap(key: String) {
            if (this@MarkdownView2.mOnElementListener != null) {
                this@MarkdownView2.mOnElementListener!!.onKeystrokeTap(key)
            }

        }

        @JavascriptInterface
        fun onLinkTap(href: String, text: String) {
            if (this@MarkdownView2.mOnElementListener != null) {
                this@MarkdownView2.mOnElementListener!!.onLinkTap(href, text)
            }

        }
    }

    private inner class LoadMarkdownUrlTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String): String {
            val url = params[0]
            var `is`: InputStream? = null

            var var5: String
            try {

                val connection = URL(url).openConnection()
                connection.readTimeout = 5000
                connection.connectTimeout = 5000
                connection.setRequestProperty("Accept-Charset", "UTF-8")
                `is` = connection.getInputStream()
                var5 = Utils.getStringFromInputStream(`is`)
                return var5
            } catch (var15: Exception) {
                var15.printStackTrace()
                var5 = ""
            } finally {
                if (`is` != null) {
                    try {
                        `is`.close()
                    } catch (var14: IOException) {
                        var14.printStackTrace()
                    }

                }

            }

            return var5
        }

        override fun onPostExecute(s: String) {
            this@MarkdownView2.loadMarkdown(s)
        }
    }

    inner class CustomAttributeProvider : AttributeProvider {

        override fun setAttributes(node: Node, part: AttributablePart, attributes: Attributes) {
            if (node is FencedCodeBlock) {
                if (part.name == "NODE") {
                    val language = node.info.toString()
                    if (!TextUtils.isEmpty(language) && language != "nohighlight") {
                        this@MarkdownView2.addJavascript(MarkdownView.HIGHLIGHTJS)
                        this@MarkdownView2.addJavascript(MarkdownView.HIGHLIGHT_INIT)
                        attributes.addValue("language", language)
                        attributes.addValue("onclick", String.format("javascript:android.onCodeTap('%s', this.textContent);", language))
                    }
                }
            } else if (node is MathJax) {
                this@MarkdownView2.addJavascript(MarkdownView.MATHJAX)
                this@MarkdownView2.addJavascript(MarkdownView.MATHJAX_CONFIG)
            } else if (node is Abbreviation) {
                this@MarkdownView2.addJavascript(MarkdownView.TOOLTIPSTER_JS)
                this@MarkdownView2.addStyleSheet(MarkdownView.TOOLTIPSTER_CSS)
                this@MarkdownView2.addJavascript(MarkdownView.TOOLTIPSTER_INIT)
                attributes.addValue("class", "tooltip")
            } else if (node is Heading) {
                attributes.addValue("onclick", String.format("javascript:android.onHeadingTap(%d, '%s');", *arrayOf<Any>(Integer.valueOf(node.level), node.text)))
            } else if (node is Image) {
                attributes.addValue("onclick", String.format("javascript: android.onImageTap(this.src, this.clientWidth, this.clientHeight);", *arrayOfNulls<Any>(0)))
            } else if (node is Mark) {
                attributes.addValue("onclick", String.format("javascript: android.onMarkTap(this.textContent)", *arrayOfNulls<Any>(0)))
            } else if (node is Keystroke) {
                attributes.addValue("onclick", String.format("javascript: android.onKeystrokeTap(this.textContent)", *arrayOfNulls<Any>(0)))
            } else if (node is Link || node is AutoLink) {
                attributes.addValue("onclick", String.format("javascript: android.onLinkTap(this.href, this.textContent)", *arrayOfNulls<Any>(0)))
            }

        }
    }

    class NodeRendererFactoryImpl : NodeRendererFactory {

        override fun create(options: DataHolder): NodeRenderer {
            return NodeRenderer {
                val set = HashSet<NodeRenderingHandler<*>>()
                set.add(NodeRenderingHandler(Image::class.java, CustomNodeRenderer { node, context, html ->
                    if (!context.isDoNotRenderLinks) {
                        val altText = TextCollectingVisitor().collectAndGetText(node)
                        val resolvedLink = context.resolveLink(LinkType.IMAGE, node.url.unescape(), null as Boolean?)
                        var url = resolvedLink.url
                        if (!node.urlContent.isEmpty) {
                            val content = Escaping.percentEncodeUrl(node.urlContent).replace("+", "%2B").replace("%3D", "=").replace("%26", "&amp;")
                            url += content
                        }

                        val index = url.indexOf("@")
                        if (index >= 0) {
                            val dimensions = url.substring(index + 1, url.length).split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            url = url.substring(0, index)
                            if (dimensions.size == 2) {
                                val width = if (TextUtils.isEmpty(dimensions[0])) "auto" else dimensions[0]
                                val height = if (TextUtils.isEmpty(dimensions[1])) "auto" else dimensions[1]
                                html.attr("style", "width: $width; height: $height")
                            }
                        }

                        html.attr("src", url)
                        html.attr("alt", altText)
                        if (node.title.isNotNull) {
                            html.attr("title", node.title.unescape())
                        }

                        html.srcPos(node.chars).withAttr(resolvedLink).tagVoid("img")
                    }
                }))
                set
            }
        }
    }

    interface OnElementListener {
        fun onButtonTap(var1: String)

        fun onCodeTap(var1: String, var2: String)

        fun onHeadingTap(var1: Int, var2: String)

        fun onImageTap(var1: String, var2: Int, var3: Int)

        fun onLinkTap(var1: String, var2: String)

        fun onKeystrokeTap(var1: String)

        fun onMarkTap(var1: String)
    }

    companion object {
        val JQUERY_3: JavaScript = ExternalScript("file:///android_asset/js/jquery-3.1.1.min.js", false, false)
        val HIGHLIGHTJS: JavaScript = ExternalScript("file:///android_asset/js/highlight.js", false, true)
        val MATHJAX: JavaScript = ExternalScript("https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS_CHTML", false, true)
        val HIGHLIGHT_INIT: JavaScript = ExternalScript("file:///android_asset/js/highlight-init.js", false, true)
        val MATHJAX_CONFIG: JavaScript = ExternalScript("file:///android_asset/js/mathjax-config.js", false, true)
        val TOOLTIPSTER_JS: JavaScript = ExternalScript("file:///android_asset/js/tooltipster.bundle.min.js", false, true)
        val TOOLTIPSTER_INIT: JavaScript = ExternalScript("file:///android_asset/js/tooltipster-init.js", false, true)
        val TOOLTIPSTER_CSS: StyleSheet = ExternalStyleSheet("file:///android_asset/css/tooltipster.bundle.min.css")
        private val EXTENSIONS = Arrays.asList(*arrayOf(TablesExtension.create(), TaskListExtension.create(), AbbreviationExtension.create(), AutolinkExtension.create(), MarkExtension.create(), StrikethroughSubscriptExtension.create(), SuperscriptExtension.create(), KeystrokeExtension.create(), MathJaxExtension.create(), FootnoteExtension.create(), EmojiExtension.create(), VideoLinkExtension.create(), TwitterExtension.create(), LabelExtension.create(), ButtonExtension.create(), LocalizationExtension.create()))
    }
}
