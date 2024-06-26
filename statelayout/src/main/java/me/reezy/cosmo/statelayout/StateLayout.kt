package me.reezy.cosmo.statelayout

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.annotation.RestrictTo

@Suppress("UNCHECKED_CAST")
class StateLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : FrameLayout(context, attrs, defStyle) {


    private var vContent: View? = null

    private val views = mutableMapOf<Any, View>()

    init {
        val transition = LayoutTransition()

        transition.setAnimator(LayoutTransition.APPEARING,  ObjectAnimator.ofFloat(null, View.ALPHA, 0f, 1f))
        transition.setAnimator(LayoutTransition.DISAPPEARING,  ObjectAnimator.ofFloat(null, View.ALPHA, 1f, 0f))

        layoutTransition = transition

        if (layoutParams == null) {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }
    }

    inline fun <reified V : View> showStateView(key: Any = V::class.java): V? {
        return showStateView(V::class.java, key)
    }

    fun <V : View> showStateView(clazz: Class<V>, key: Any = clazz): V? {
        return showStateView(key) {
            clazz.getConstructor(Context::class.java).newInstance(context)
        } as? V
    }

     fun <V : View> showStateView(@LayoutRes layoutResId: Int, key: Any = layoutResId): V? {
        return showStateView(key) {
            LayoutInflater.from(context).inflate(layoutResId, this, false)
        } as? V
    }

    private fun showStateView(key: Any, create: () -> View): View {


        visibility = View.VISIBLE
        vContent?.visibility = View.GONE

        val newView = views.getOrPut(key, create)
        val oldView = if (childCount != 0) getChildAt(0) else null


        if (newView != oldView) {
            if (vContent?.parent == this) {
                removeViews(1, childCount - 1)
            } else {
                removeAllViews()
            }
            addView(newView)
        }
        return newView
    }

    fun showContent() {
        if (vContent?.parent == this) {
            removeViews(1, childCount - 1)
        } else {
            removeAllViews()
            if (vContent?.parent == null) {
                addView(vContent)
            } else {
                visibility = View.GONE
            }
        }
        vContent?.visibility = View.VISIBLE
    }

    fun setContent(view: View) {
        if (vContent?.parent == this) {
            removeView(vContent)
        }
        vContent = view
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount == 0) {
            return
        }
        if (childCount > 1) {
            removeViews(1, childCount - 1)
        }
        vContent = getChildAt(0)
    }

}