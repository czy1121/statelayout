package me.reezy.cosmo.statelayout

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.app.ComponentActivity
import androidx.core.view.children
import me.reezy.cosmo.R


class SimpleState(
    var loadingTextResId: Int = R.string.state_default_loading,
    var loadingImageResId: Int = defaultLoadingImageResId,
    var emptyTextResId: Int = R.string.state_default_empty,
    var emptyImageResId: Int = defaultEmptyImageResId,
    var errorTextResId: Int = R.string.state_default_error,
    var errorImageResId: Int = defaultErrorImageResId,
    var retryTextResId: Int = R.string.state_default_btn_retry,
    var retryListener: View.OnClickListener? = null,
    var retryVisible: Boolean = true,
    var showLoading: ((StateLayout) -> Unit)? = defaultShowLoading,
    var showEmpty: ((StateLayout) -> Unit)? = defaultShowEmpty,
    var showError: ((StateLayout) -> Unit)? = defaultShowError,
) : StatePresenter {

    companion object {
        const val STATE_CONTENT = 0
        const val STATE_LOADING = 1
        const val STATE_ERROR = 2
        const val STATE_EMPTY = 3

        var defaultLoadingImageResId: Int = 0
        var defaultEmptyImageResId: Int = 0
        var defaultErrorImageResId: Int = 0

        var defaultShowLoading: ((StateLayout) -> Unit)? = null
        var defaultShowEmpty: ((StateLayout) -> Unit)? = null
        var defaultShowError: ((StateLayout) -> Unit)? = null

    }


    fun with(activity: ComponentActivity): Controller = ActivityController(activity, this)
    fun with(layout: StateLayout): Controller = StateLayoutController(layout, this)


    override fun show(layout: StateLayout, state: Int) {
        when (state) {
            // image, text
            STATE_LOADING -> if (showLoading == null) {
                layout.showStateView<SimpleStateView>().apply {
                    setHeightMatchParent()
                    setText(loadingTextResId)
                    setImage(loadingImageResId)
                    setButton(false)
                }
            } else {
                showLoading?.invoke(layout)
            }
            // image, text, retry
            STATE_ERROR -> if (showError == null) {
                layout.showStateView<SimpleStateView>().apply {
                    setHeightMatchParent()
                    setText(errorTextResId)
                    setImage(errorImageResId)
                    setButton(retryVisible, retryTextResId, retryListener)
                }
            } else {
                showError?.invoke(layout)
            }
            // image, text
            STATE_EMPTY -> if (showEmpty == null) {
                layout.showStateView<SimpleStateView>().apply {
                    setHeightMatchParent()
                    setText(emptyTextResId)
                    setImage(emptyImageResId)
                    setButton(false)
                }
            } else {
                showEmpty?.invoke(layout)
            }
            STATE_CONTENT -> layout.showContent()
        }
    }


    interface Controller {

        val presenter: SimpleState

        fun showLoading()

        fun showError()

        fun showEmpty()

        fun showContent()
    }

    private class StateLayoutController(private val layout: StateLayout, override val presenter: SimpleState) : Controller {
        override fun showLoading() {
            presenter.show(layout, STATE_LOADING)
        }

        override fun showError() {
            presenter.show(layout, STATE_ERROR)
        }

        override fun showEmpty() {
            presenter.show(layout, STATE_EMPTY)
        }

        override fun showContent() {
            presenter.show(layout, STATE_CONTENT)
        }
    }

    private class ActivityController(private val activity: ComponentActivity, override val presenter: SimpleState) : Controller {

        private val container: FrameLayout by lazy {
            activity.findViewById<FrameLayout>(android.R.id.content).apply {
                if (layoutTransition == null) {
                    val transition = LayoutTransition()
                    transition.setAnimator(LayoutTransition.APPEARING, ObjectAnimator.ofFloat(null, View.ALPHA, 0f, 1f))
                    transition.setAnimator(LayoutTransition.DISAPPEARING, ObjectAnimator.ofFloat(null, View.ALPHA, 1f, 0f))
                    layoutTransition = transition
                }
            }
        }

        private fun findStateLayout(): StateLayout? {
            for (view in container.children) {
                if (view is StateLayout) {
                    return view
                }
            }
            return null
        }

        private fun ensureStateLayout(): StateLayout {
            return findStateLayout() ?: StateLayout(container.context).apply {
                setContent(container.getChildAt(0))
                container.addView(this, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            }
        }

        override fun showLoading() {
            presenter.show(ensureStateLayout(), STATE_LOADING)
        }

        override fun showError() {
            presenter.show(ensureStateLayout(), STATE_ERROR)
        }

        override fun showEmpty() {
            presenter.show(ensureStateLayout(), STATE_EMPTY)
        }

        override fun showContent() {
            findStateLayout()?.showContent()
        }
    }
}