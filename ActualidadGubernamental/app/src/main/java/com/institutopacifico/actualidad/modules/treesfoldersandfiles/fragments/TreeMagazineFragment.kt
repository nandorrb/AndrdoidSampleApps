package com.institutopacifico.actualidad.modules.treesfoldersandfiles.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.application.ApplicationClass
import com.institutopacifico.actualidad.modules.pageviewer.PageViewerSwipeActivity
import com.institutopacifico.actualidad.modules.treesfoldersandfiles.adapters.tree.viewbinder.DirectoryNodeBinder
import com.institutopacifico.actualidad.modules.treesfoldersandfiles.magazine.adapters.tree.bean.Dir
import com.institutopacifico.actualidad.modules.treesfoldersandfiles.magazine.adapters.tree.bean.File
import com.institutopacifico.actualidad.modules.treesfoldersandfiles.magazine.adapters.tree.viewbinder.FileNodeBinder
import com.institutopacifico.actualidad.network.IonObject
import com.institutopacifico.actualidad.network.IonSingleton
import com.institutopacifico.actualidad.objects.BodyObject
import com.institutopacifico.actualidad.objects.ResponseObject
import com.institutopacifico.actualidad.objects.RichFolderAndArticleObject
import com.institutopacifico.actualidad.objects.UserDataSingleton
import com.institutopacifico.actualidad.utils.RecyclerTreeViewHelper
import com.institutopacifico.actualidad.utils.StringBusSingleton
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.treeview_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import tellh.com.recyclertreeview_lib.TreeNode
import tellh.com.recyclertreeview_lib.TreeViewAdapter
import java.util.*


/**
 * Created by mobile on 4/6/17.
 * Fernando Rubio Burga
 */

class TreeMagazineFragment : Fragment() {
    open var string_request_api = ApplicationClass.context.resources.getString(R.string.request_tree_of_folders_and_articles_revista)
    lateinit var root: ViewGroup
    var inflater: LayoutInflater? = null
    var currentObject: RichFolderAndArticleObject = RichFolderAndArticleObject()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater?.inflate(R.layout.treeview_layout, null) as ViewGroup
        this.inflater = inflater
     //   EventBus.getDefault().register(this)
        refreshObject()
        return root
    }

    override fun onResume() {
        super.onResume()

        //refreshObject()
    }

    private fun refreshObject() {
        var body: BodyObject = Gson().fromJson(StringBusSingleton.instance.string_holder, BodyObject::class.java)
        if (UserDataSingleton.userData.stringInstitutoPacificoUserId.isEmpty()) {
            Toasty.info(root.context, UserDataSingleton.userData.stringInstitutoPacificoAboutMembership).show()
        } else {
          //  IonSingleton.instance.makeAsynchronousCompressedRequest(root.context, ApplicationClass.context.resources.getString(R.string.string_api_tree_of_folders_and_articles), body)
            IonObject().makeAsynchronousCompressedRequest(root.context, ApplicationClass.context.resources.getString(R.string.string_api_tree_of_folders_and_articles), body,lambda_AsynchronousResponseProcessor)
            //TODO: Add loading icon
        }


        if (currentObject.string_object_name_or_title.isEmpty()) {
            currentObject = Gson().fromJson(StringBusSingleton.instance.string_holder, RichFolderAndArticleObject::class.java)
            currentObject = RecyclerTreeViewHelper.setLevels(currentObject)
        }
      //  initData()
    }

  /*  @Subscribe
    fun AsynchronousResponse(ResponseObject_AsynchronousResponse: ResponseObject) {
        // if (ResponseObject_AsynchronousResponse.array_of_rich_folder_and_article_objects.isNotEmpty()){
        // if (currentObject.string_object_name_or_title.isEmpty()) {
        currentObject = RichFolderAndArticleObject()
        currentObject.list_of_objects = ResponseObject_AsynchronousResponse.array_of_rich_folder_and_article_objects
      //  currentObject = Gson().fromJson(StringBusSingleton.instance.string_holder, RichFolderAndArticleObject::class.java)
        currentObject = RecyclerTreeViewHelper.setLevels(currentObject)
        //   }
        initData()

    }
    */
  private val  lambda_AsynchronousResponseProcessor={ ResponseObject_AsynchronousResponse: ResponseObject ->
        currentObject = RichFolderAndArticleObject()
        currentObject.list_of_objects = ResponseObject_AsynchronousResponse.array_of_rich_folder_and_article_objects
        //  currentObject = Gson().fromJson(StringBusSingleton.instance.string_holder, RichFolderAndArticleObject::class.java)
        currentObject = RecyclerTreeViewHelper.setLevels(currentObject)
        //   }
        initData()

    }

    private fun initData() {
        /*   val nodes: MutableList<TreeNode<*>> = arrayListOf()
           val app = TreeNode(Dir("app"))
           nodes.add(app)
           app.addChild(
                   TreeNode(Dir("manifests"))
                           .addChild(TreeNode(File("AndroidManifest.xml")))
           )
           app.addChild(
                   TreeNode(Dir("java")).addChild(
                           TreeNode(Dir("tellh")).addChild(
                                   TreeNode(Dir("com")).addChild(
                                           TreeNode(Dir("recyclertreeview"))
                                                   .addChild(TreeNode(File("Dir")))
                                                   .addChild(TreeNode(File("DirectoryNodeBinder")))
                                                   .addChild(TreeNode(File("File")))
                                                   .addChild(TreeNode(File("FileNodeBinder")))
                                                   .addChild(TreeNode(File("TreeViewBinder")))
                                   )
                           )
                   )
           )
           val res = TreeNode(Dir("res"))
           nodes.add(res)
           res.addChild(
                   TreeNode(Dir("layout"))
                           .addChild(TreeNode(File("activity_main.xml")))
                           .addChild(TreeNode(File("item_dir.xml")))
                           .addChild(TreeNode(File("item_file.xml")))
           )
           res.addChild(
                   TreeNode(Dir("mipmap"))
                           .addChild(TreeNode(File("ic_launcher.png")))
           )
           */
        recycler_tree_view_default_fragment?.layoutManager = LinearLayoutManager(root.context)
        // Logger.i(Gson().toJson(nodes))
        // nodes=  currentObject.list_of_objects as MutableList<TreeNode<*>>
        var adapter = TreeViewAdapter(RecyclerTreeViewHelper.convert(currentObject), Arrays.asList(FileNodeBinder(), DirectoryNodeBinder(currentObject)))
        // whether collapse child nodes when their parent node was close.
        //        adapter.ifCollapseChildWhileCollapseParent(true);
        adapter.setOnTreeNodeListener(object : TreeViewAdapter.OnTreeNodeListener {
            override fun onClick(node: TreeNode<*>, holder: RecyclerView.ViewHolder): Boolean {
                if (!node.isLeaf) {
                    //Update and toggle the node.
                    onToggle(!node.isExpand, holder)
                    //                    if (!node.isExpand())
                    //                        adapter.collapseBrotherNode(node);
                } else {
                    try {
                        var SelectedObject = (node.content as File).currentObject
                        var currentObjectRoot = (node.parent.content as Dir).currentObject
                        var i: Intent
                        // i= Intent(root.context, PageViewerMagazine2Activity::class.java)
                        i = Intent(root.context, PageViewerSwipeActivity::class.java)
                        //  StarredArticlesSingleton.instance.string_CurrentArticleJson = Gson().toJson(currentObject)
                        i.putExtra("RootObject", Gson().toJson(currentObjectRoot))
                        val integer_position = getPosition(currentObjectRoot, SelectedObject)
                        i.putExtra("integer_position", integer_position)
                        root.context.startActivity(i)
                    } catch (e: Exception) {
                        //    Logger.e("Can't load: " + Gson().toJson(currentObject))
                        IonSingleton.instance.saveErrorToDB("Carpeta Vacía", root.context, BodyObject(), "Can't load: " + Gson().toJson(currentObject))
                    }
                }
                return false
            }

            fun getPosition(Root: RichFolderAndArticleObject, Leaf: RichFolderAndArticleObject): Int {
                var position = 0
                for (a in Root.list_of_objects) {
                    if (a.string_url_LinkToMarkdown == Leaf.string_url_LinkToMarkdown) {
                        break
                    }
                    position++
                }

                return position
            }

            override fun onToggle(isExpand: Boolean, holder: RecyclerView.ViewHolder) {
                try {
                    val dirViewHolder = holder as DirectoryNodeBinder.ViewHolder
                    val ivArrow = dirViewHolder.ivArrow
                    val rotateDegree: Int
                    if (isExpand) {
                        rotateDegree = 90
                    } else {
                        rotateDegree = -90
                    }
                    ivArrow.animate().rotationBy(rotateDegree.toFloat())
                            .start()
                } catch (e: Exception) {
                    Toasty.error(context, e.message!!).show()
                }
            }
        })
        recycler_tree_view_default_fragment.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
    //    EventBus.getDefault().unregister(this)
        ApplicationClass.getRefWatcher(activity)?.watch(this)
    }
}