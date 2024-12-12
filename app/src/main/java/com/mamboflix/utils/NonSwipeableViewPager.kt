package com.mamboflix.utils

import android.content.Context

import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

open class NonSwipeableViewPager : ViewPager {
    var isPagingEnabled: Boolean = false

    constructor(context: Context) : super(context) {
        this.isPagingEnabled = true
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.isPagingEnabled = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return isPagingEnabled && super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return isPagingEnabled && super.onInterceptTouchEvent(event)
    }

    fun setSwipePagingEnabled(isPagingEnabled: Boolean) {
        this.isPagingEnabled = isPagingEnabled
    }
}