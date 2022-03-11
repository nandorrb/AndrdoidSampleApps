package com.institutopacifico.actualidad.modules.treesfoldersandfiles.magazine.adapters.tree.viewbinder

import android.view.View
import android.widget.TextView

import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.modules.treesfoldersandfiles.magazine.adapters.tree.bean.File

import tellh.com.recyclertreeview_lib.TreeNode
import tellh.com.recyclertreeview_lib.TreeViewBinder

/**
 * Created by mobile on 7/13/17.
 * Fernando Rubio Burga
 */

class FileNodeBinder : TreeViewBinder<FileNodeBinder.ViewHolder>() {
    override fun provideViewHolder(itemView: View): ViewHolder {
        return ViewHolder(itemView)
    }

    override fun bindView(holder: ViewHolder, position: Int, node: TreeNode<*>) {
        val fileNode = node.content as File
        holder.tvName.text = fileNode.fileName
    }

    override fun getLayoutId(): Int {
        return R.layout.treeview_file
    }

    inner class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
        var tvName: TextView

        init {
            this.tvName = rootView.findViewById<View>(R.id.text_view_treeviewdirectory_name) as TextView
        }

    }
}