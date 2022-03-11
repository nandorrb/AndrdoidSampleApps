package adapters


/**
 * Created by mobile on 6/26/17.
 * Fernando Rubio Burga
 */

class GridAdapter /* (hand: List<RichFolderAndArticleObject>, internal var body: BodyObject, internal var drawable: Int) : UltimateGridLayoutAdapter<RichFolderAndArticleObject, itemGridCellBinder>(hand) {

    /**
     * the layout id for the normal data

     * @return the ID
     */
    override fun getNormalLayoutResId(): Int {
        return itemGridCellBinder.Companion.layout
    }

    /**
     * this is the Normal View Holder initiation

     * @param view view
     * *
     * @return holder
     */
    override fun newViewHolder(view: View): itemGridCellBinder {
        return itemGridCellBinder(view, true)
    }


    override fun generateHeaderId(position: Int): Long {
        return 0
    }

    /**
     * binding normal view holder

     * @param holder   holder class
     * *
     * @param data     data
     * *
     * @param position position
     */
    override fun withBindHolder(holder: itemGridCellBinder, data: RichFolderAndArticleObject, position: Int) {

    }

    override fun bindNormal(b: itemGridCellBinder, richFolderAndArticleObject: RichFolderAndArticleObject, position: Int) {
        b.textViewSample.text = richFolderAndArticleObject.string_object_name_or_title
        //     b.imageViewSample.setImageResource(R.drawable.ic_instituto_pacifico_libro_dinamic_24dp)
        if (richFolderAndArticleObject.string_url_ImageWebSource.isNotEmpty()) {
            Glide.with(b.context)
                    .load(richFolderAndArticleObject.string_url_ImageWebSource)
                    .centerCrop()
                    .placeholder(drawable)
                    .into(b.imageViewSample)


         /*   Ion.with(b.context)
                    .load(richFolderAndArticleObject.string_url_ImageWebSource)
                    .withBitmap()
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_image_black_24dp)
                    .intoImageView(b.imageViewSample)*/
        } else {
            b.imageViewSample.setImageResource(drawable)
        }
        b.item_view.setOnClickListener {
            body.string_additional_parameter = richFolderAndArticleObject.string_object_id
            StringBusSingleton.instance.string_holder = Gson().toJson(body)
            //   StringBusSingleton.instance.string_holder2 = Gson().toJson(body)
            FragmentTransactionHelper.goToFragment(TreeMagazineFragment(), b.context)
            // FragmentTransactionHelper.goToFragment(ExpandableMagazineFragment())
        }
    }


    override fun onCreateHeaderViewHolder(parent: ViewGroup): UltimateRecyclerviewViewHolder<RichFolderAndArticleObject> {
        return UltimateRecyclerviewViewHolder(parent)
    }

    override fun newFooterHolder(view: View?): itemGridCellBinder {
        return itemGridCellBinder(view!!, false)
    }

    override fun newHeaderHolder(view: View?): itemGridCellBinder {
        return itemGridCellBinder(view!!, false)
    }
}
*/