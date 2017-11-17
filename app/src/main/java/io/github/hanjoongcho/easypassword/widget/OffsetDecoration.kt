package io.github.hanjoongcho.easypassword.widget

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by Administrator on 2017-11-17.
 */

class OffsetDecoration(private val offset: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State?) {
        outRect.set(offset, offset, offset, offset)
    }
}