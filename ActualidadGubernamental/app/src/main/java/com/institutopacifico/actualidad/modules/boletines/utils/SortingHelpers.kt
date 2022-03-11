package com.institutopacifico.actualidad.modules.boletines.utils

import com.institutopacifico.actualidad.objects.ArticleObject
import java.util.*

/**
 * Created by mobile on 6/5/17.
 */

class SortingHelpers {
    companion object {

        fun sortDataNewsFirst(dataForSorting: Array<ArticleObject>): Array<ArticleObject> {

            var BoletinItemViewObject_SortedData = dataForSorting
            val List_SortedData = Arrays.asList(*BoletinItemViewObject_SortedData)

            if (dataForSorting.isNotEmpty()) {

                Collections.sort(List_SortedData) { o1, o2 -> o1.string_article_category.length - o2.string_article_category.length }

                BoletinItemViewObject_SortedData = List_SortedData.toTypedArray() as Array<ArticleObject>

                if (BoletinItemViewObject_SortedData[0].string_article_category != "Noticias") {
                    BoletinItemViewObject_SortedData = reverseData(BoletinItemViewObject_SortedData)
                }

            }

            return BoletinItemViewObject_SortedData

        }

        fun sortDataByDate(dataForSorting: Array<ArticleObject>): Array<ArticleObject> {

            var BoletinItemViewObject_SortedData = dataForSorting
            val List_SortedData = Arrays.asList(*BoletinItemViewObject_SortedData)

            if (dataForSorting.isNotEmpty()) {

                Collections.sort(List_SortedData) { o1, o2 -> o1.date_boletin_articles_date_added.compareTo(o2.date_boletin_articles_date_added) }

                BoletinItemViewObject_SortedData = List_SortedData.toTypedArray() as Array<ArticleObject>

                if (BoletinItemViewObject_SortedData[0].date_boletin_articles_date_added.before(BoletinItemViewObject_SortedData[BoletinItemViewObject_SortedData.size - 1].date_boletin_articles_date_added)) {
                    BoletinItemViewObject_SortedData = reverseData(BoletinItemViewObject_SortedData)
                }
            }

            return BoletinItemViewObject_SortedData

        }

        fun reverseData(dataForSorting: Array<ArticleObject>): Array<ArticleObject> {
            var BoletinItemViewObject_ReversedData = dataForSorting
            val List_SortedData = Arrays.asList(*BoletinItemViewObject_ReversedData)

            if (dataForSorting.isNotEmpty()) {
                Collections.reverse(List_SortedData)
                BoletinItemViewObject_ReversedData = List_SortedData.toTypedArray() as Array<ArticleObject>
            }

            return BoletinItemViewObject_ReversedData
        }
    }
}
