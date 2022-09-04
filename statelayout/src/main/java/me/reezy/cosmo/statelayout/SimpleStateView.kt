package me.reezy.cosmo.statelayout

import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedImageDrawable
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.updateLayoutParams

@Suppress("MemberVisibilityCanBePrivate", "LeakingThis")
open class SimpleStateView(context: Context) : FrameLayout(context) {

    companion object {
        var createButton: (Context) -> TextView = { AppCompatButton(it) }
        var setupStyle: SimpleStateView.() -> Unit = { }
    }

    val vLayout = LinearLayoutCompat(context)
    val vImage = AppCompatImageView(context)
    val vText = AppCompatTextView(context)
    val vButton: TextView = createButton(context)

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        vLayout.orientation = LinearLayoutCompat.VERTICAL
        vLayout.gravity = Gravity.CENTER

        vLayout.addView(vImage, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER))
        vLayout.addView(Space(context), LayoutParams(LayoutParams.WRAP_CONTENT, (resources.displayMetrics.density * 10).toInt()))
        vLayout.addView(vText, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER))
        vLayout.addView(Space(context), LayoutParams(LayoutParams.WRAP_CONTENT, (resources.displayMetrics.density * 10).toInt()))
        vLayout.addView(vButton, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER))

        addView(vLayout, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER))

        setupStyle()
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


    fun setImage(resId: Int) {
        vImage.setImageResource(resId)
    }

    fun setText(text: String, onClick: (View) -> Unit = {}) {
        vText.text = text
        vText.setOnClickListener(onClick)
    }

    fun setText(resId: Int, onClick: (View) -> Unit = {}) {
        vText.text = resources.getString(resId)
        vText.setOnClickListener(onClick)
    }


    fun setButton(visible: Boolean, text: String = "", onClick: OnClickListener? = null) {
        vButton.visibility = if (visible) View.VISIBLE else View.GONE
        vButton.text = text
        vButton.setOnClickListener(onClick)
    }

    fun setButton(visible: Boolean, resId: Int, onClick: OnClickListener? = null) {
        vButton.visibility = if (visible) View.VISIBLE else View.GONE
        vButton.text = if (resId > 0) resources.getString(resId) else ""
        vButton.setOnClickListener(onClick)
    }
}