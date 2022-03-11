package com.institutopacifico.actualidad.modules.treesfoldersandfiles.adapters.tree.viewbinder

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.institutopacifico.actualidad.modules.treesfoldersandfiles.magazine.adapters.tree.bean.Dir
import com.institutopacifico.actualidad.objects.RichFolderAndArticleObject
import com.institutopacifico.actualidad.utils.RecyclerTreeViewHelper
import com.orhanobut.logger.Logger

import tellh.com.recyclertreeview_lib.TreeNode
import tellh.com.recyclertreeview_lib.TreeViewBinder


/**
 * Created by mobile on 7/13/17.
 * Fernando Rubio Burga
 */

class DirectoryNodeBinder(richFolderAndArticleObject: RichFolderAndArticleObject) : TreeViewBinder<DirectoryNodeBinder.ViewHolder>() {
    var RootTreeLenght = RecyclerTreeViewHelper.getNumberOfGenerations(richFolderAndArticleObject).toFloat()

    override fun provideViewHolder(itemView: View): ViewHolder {
        return ViewHolder(itemView)
    }

    override fun bindView(holder: ViewHolder, position: Int, node: TreeNode<*>) {
        //   var nodeLenght = RecyclerTreeViewHelper.getNumberOfGenerations(node).toFloat()
        holder.ivArrow.rotation = 0f
        //  holder.ivArrow.setImageResource(R.drawable.ic_app_logo_ag);
        val rotateDegree = if (node.isExpand) 90 else 0
        holder.ivArrow.rotation = rotateDegree.toFloat()
        val dirNode = node.content as Dir
        holder.tvName.text = dirNode.dirName
        holder.tvName.setTextColor(ContextCompat.getColor(ApplicationClass.context, R.color.defaut_background_color))
        holder.LinearLayout_Root.setBackgroundColor(
                setAlpha(
                        "#" + Integer.toHexString(ContextCompat.getColor(ApplicationClass.context, R.color.colorPrimaryDark)),
                        (255 * (1 - ((dirNode.currentObject.integer_current_level * 1.0 - 1.0) / RootTreeLenght) * 0.9)).toInt()
                )
        )


        if (node.isLeaf) {
            holder.ivArrow.visibility = View.INVISIBLE
        } else {
            holder.ivArrow.visibility = View.VISIBLE
        }
    }

    private fun setAlpha(color: String, gradient: Int): Int {
        var alpha = Integer.toHexString(gradient)
        if (alpha.length > 2) {
            alpha = alpha.substring(0, 2)
        }
        var output: String
        if (color.length == 5) {
            output = color.replace("#", "#" + alpha)
        } else {
            output = "#" + alpha + color.substring(3)
        }

        //  Logger.i(output)

        var ColorOutput: Int
        try {
            ColorOutput = Color.parseColor(output)
        } catch (e: Exception) {
            Logger.e(e.message)
            ColorOutput = Color.parseColor(color)
        }



        return ColorOutput
    }


    override fun getLayoutId(): Int {
        return R.layout.treeview_directory
    }

    class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
        val ivArrow: ImageView
        val tvName: TextView
        val LinearLayout_Root: LinearLayout

        init {
            this.ivArrow = rootView.findViewById<ImageView>(R.id.image_view_treeviewdirectory_arrow)
            this.tvName = rootView.findViewById<TextView>(R.id.text_view_treeviewdirectory_name)
            this.LinearLayout_Root = rootView.findViewById<LinearLayout>(R.id.linear_layout_treeview_directory_root)
        }
    }
}