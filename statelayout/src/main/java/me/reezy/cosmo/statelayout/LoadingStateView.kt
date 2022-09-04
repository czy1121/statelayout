package me.reezy.cosmo.statelayout

import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.updateLayoutParams

@Suppress("MemberVisibilityCanBePrivate")
class LoadingStateView(context: Context) : FrameLayout(context) {

    val vImage = AppCompatImageView(context)

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        addView(vImage, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER))
    }

    fun setHeightMatchParent() {
        updateLayoutParams {
            height = LayoutParams.MATCH_PARENT
        }
    }

    fun setHeight(px: Int) {
        updateLayoutParams {
            height = px
        }
    }

    fun setHeight(dp: Float) {
        updateLayoutParams {
            height = (resources.displayMetrics.density * dp).toInt()
        }
    }
}