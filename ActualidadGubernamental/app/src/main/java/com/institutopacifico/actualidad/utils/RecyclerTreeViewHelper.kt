package com.institutopacifico.actualidad.utils

import com.institutopacifico.actualidad.modules.treesfoldersandfiles.magazine.adapters.tree.bean.Dir
import com.institutopacifico.actualidad.modules.treesfoldersandfiles.magazine.adapters.tree.bean.File
import com.institutopacifico.actualidad.objects.RichFolderAndArticleObject
import tellh.com.recyclertreeview_lib.TreeNode

/**
 * Created by mobile on 7/13/17.
 * Fernando Rubio Burga
 */
object RecyclerTreeViewHelper {


    fun convert(richFolderAndArticleObject: RichFolderAndArticleObject): MutableList<TreeNode<*>> {
        var nodes: MutableList<TreeNode<*>> = mutableListOf()

        nodes.add(createNode(richFolderAndArticleObject))
        while (nodes.size == 1 && nodes[0].childList != null) {
                nodes = nodes[0].childList
        }
        return nodes
    }

    fun createNode(richFolderAndArticleObject: RichFolderAndArticleObject): TreeNode<*> {
        var app: TreeNode<*>

        if (richFolderAndArticleObject.string_url_LinkToMarkdown.isNotEmpty()) {
            app = TreeNode(File(richFolderAndArticleObject))
        } else {
            app = TreeNode(Dir(richFolderAndArticleObject))
            if (richFolderAndArticleObject.list_of_objects.isNotEmpty()) {
                for (ForderLevel1: RichFolderAndArticleObject in richFolderAndArticleObject.list_of_objects) {
                    app.addChild(createNode(ForderLevel1))
                }
            }
        }
        return app
    }

    fun getNumberOfGenerations(richFolderAndArticleObject: RichFolderAndArticleObject): Int {
        var output = 0

        if (richFolderAndArticleObject?.list_of_objects?.isNotEmpty()) {
            var maxNumber = 0
            var currentNumber: Int
            for (ForderLevel1: RichFolderAndArticleObject in richFolderAndArticleObject.list_of_objects) {
                currentNumber = getNumberOfGenerations(ForderLevel1)
                if (maxNumber < currentNumber) {
                    maxNumber = currentNumber
                }
            }
            output = 1 + maxNumber
        }
        return output
    }

    fun getNumberOfGenerations(node: TreeNode<*>): Int {
        var output = 0
        if (node.childList != null) {

            if (node.childList?.isNotEmpty()!!) {
                var maxNumber = 0
                for (ForderLevel1: TreeNode<*> in node.childList) {
                    if (maxNumber < getNumberOfGenerations(ForderLevel1)) {
                        maxNumber = getNumberOfGenerations(ForderLevel1)
                    }
                }
                output = 1 + maxNumber
            }
        }

        return output
    }

    fun setLevels(richFolderAndArticleObject: RichFolderAndArticleObject): RichFolderAndArticleObject {
        return setLevels(richFolderAndArticleObject, 0)
    }

    fun setLevels(richFolderAndArticleObject: RichFolderAndArticleObject, InitialLevel: Int): RichFolderAndArticleObject {

        richFolderAndArticleObject.integer_current_level = InitialLevel

        for (ForderLevel1: RichFolderAndArticleObject in richFolderAndArticleObject.list_of_objects) {
            setLevels(ForderLevel1, InitialLevel + 1)
        }
        return richFolderAndArticleObject
    }

}