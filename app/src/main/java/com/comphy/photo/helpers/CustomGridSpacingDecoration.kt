package com.comphy.photo.helpers

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CustomGridSpacingDecoration(
    spacingHorizontal: Int = 0,
    spacingVertical: Int = 0
) : RecyclerView.ItemDecoration() {

    private val halfSpacingHorizontal = spacingHorizontal / 2
    private val halfSpacingVertical = spacingVertical / 2

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

//        if (parent.paddingLeft != halfSpace) {
//            parent.setPadding(halfSpacingHorizontal, halfSpacingVertical, halfSpacingHorizontal, halfSpacingVertical)
//            parent.clipToPadding = false
//        }

        outRect.top = halfSpacingVertical
        outRect.bottom = halfSpacingVertical
        outRect.left = halfSpacingHorizontal
        outRect.right = halfSpacingHorizontal

//        val position: Int = parent.getChildAdapterPosition(view)
//        val column = position % spanCount
//
//        if (includeEdge) {
//            outRect.left =
//                spacingLeft - column * spacingLeft / spanCount
//            outRect.right =
//                (column + 1) * spacingLeft / spanCount
//
//            if (position < spanCount) outRect.top = spacingTop
//
//            outRect.bottom = spacingTop
//
//        } else {
//            outRect.left = column * spacingLeft / spanCount
//            outRect.right = spacingLeft - (column + 1) * spacingLeft / spanCount
//
//            if (position >= spanCount) outRect.top = spacingTop
//        }
    }
}