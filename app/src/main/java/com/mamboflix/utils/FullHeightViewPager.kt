package com.mamboflix.utils

import android.content.Context

import android.util.AttributeSet
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager

class FullHeightViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        // super has to be called in the beginning so the child views can be
        // initialized.
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (childCount <= 0)
            return

        // Check if the selected layout_height mode is set to wrap_content
        // (represented by the AT_MOST constraint).
        val wrapHeight = View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.AT_MOST

        val width = measuredWidth

        val firstChild = getChildAt(0)

        // Initially set the height to that of the first child - the
        // PagerTitleStrip (since we always know that it won't be 0).
        val height = firstChild.measuredHeight

        if (wrapHeight) {

            // Keep the current measured width.
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)

        }

        val fragmentHeight: Int
        fragmentHeight = measureFragment((adapter!!.instantiateItem(this, currentItem) as Fragment).view)

        // Just add the height of the fragment:
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height + fragmentHeight,
                View.MeasureSpec.EXACTLY)

        // super has to be called again so the new specs are treated as
        // exact measurements.
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun measureFragment(view: View?): Int {
        if (view == null)
            return 0

        view.measure(0, 0)
        return view.measuredHeight
    }
}