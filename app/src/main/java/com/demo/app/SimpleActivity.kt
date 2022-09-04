package com.demo.app

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.demo.app.databinding.ActivitySimpleBinding
import me.reezy.cosmo.statelayout.SimpleState

class SimpleActivity : AppCompatActivity(R.layout.activity_simple) {


    private val binding by lazy { ActivitySimpleBinding.bind(findViewById<ViewGroup>(android.R.id.content).getChildAt(0)) }

    private val state by lazy { SimpleState().with(binding.state) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }


        state.presenter.apply {
            errorImageResId = R.mipmap.img_no_network
            emptyImageResId = R.mipmap.img_no_record
            retryListener = View.OnClickListener {
                state.showLoading()
                findViewById<View>(android.R.id.content).postDelayed({
                    state.showContent()
                }, 1000)
            }
        }

        binding.refresh.setOnRefreshListener {
            state.showContent()
            it.finishRefresh()
        }
        binding.txtEmpty.setOnClickListener {
            state.showEmpty()
        }
        binding.txtError.setOnClickListener {
            state.showError()
        }
        binding.txtLoading.setOnClickListener {
            state.showLoading()
        }
    }

    override fun onBackPressed() {

        state.showContent()
//        super.onBackPressed()
    }
}