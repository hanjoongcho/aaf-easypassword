package io.github.hanjoongcho.easypassword.adpaters

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import io.github.hanjoongcho.easypassword.R
import io.github.hanjoongcho.easypassword.models.Category

/**
 * Created by CHO HANJOONG on 2017-11-19.
 */

class SecurityCategoryAdapter(
        private val mContext: Context, private val mLayoutResourceId: Int, private val mList: List<Category>
) : ArrayAdapter<Category>(mContext, mLayoutResourceId, mList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? = initRow(position, convertView, parent)

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? = initRow(position, convertView, parent)

    private fun initRow(position: Int, convertView: View?, parent: ViewGroup): View? {

        var row = convertView
        val holder: ViewHolder
        if (row == null) {
            val inflater = (this.mContext as Activity).layoutInflater
            row = inflater.inflate(this.mLayoutResourceId, parent, false)
            holder = ViewHolder()
            holder.textView = row?.findViewById(R.id.textView)
            holder.imageView = row?.findViewById(R.id.imageView)
            row.tag = holder
        } else {
            holder = row.tag as ViewHolder
        }

        holder.textView?.text = mList[position].name
        if (mList[position].name == "") {
            holder.imageView?.visibility = View.GONE
        } else {
            holder.imageView?.visibility = View.VISIBLE
        }
        initCategoryIcon(holder.imageView, mList[position].resourceName)

        return row
    }

    private fun initCategoryIcon(imageView: ImageView?, resourceName: String) {

        imageView?.let {
            when (resourceName) {
                "" -> it.setImageResource(0)
                else -> {
                    val imageRes = mContext.resources.getIdentifier("icon_$resourceName", "drawable", mContext.packageName)
                    it.setImageResource(imageRes)
                }
            }
        }
    }

    private class ViewHolder {
        internal var textView: TextView? = null
        internal var imageView: ImageView? = null
    }

}
