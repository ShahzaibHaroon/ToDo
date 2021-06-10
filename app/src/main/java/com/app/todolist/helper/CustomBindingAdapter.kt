package com.app.todolist.helper

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * Develop By Messagemuse
 */
class CustomBindingAdapter {
    companion object {
        @BindingAdapter("app:visibleGone")
        @JvmStatic
        fun showHide(view: View, show: Boolean) {
            view.visibility = if (show) View.VISIBLE else View.GONE
        }

        @BindingAdapter("app:visibleInVisible")
        @JvmStatic
        fun showInvisible(view: View, show: Boolean) {
            view.visibility = if (show) View.VISIBLE else View.INVISIBLE
        }
    }


}