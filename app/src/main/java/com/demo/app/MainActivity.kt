package com.demo.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load
import me.reezy.cosmo.statelayout.LoadingStateView
import me.reezy.cosmo.statelayout.SimpleState

class MainActivity : AppCompatActivity(R.layout.activity_main) {


    init {
        SimpleState.defaultShowLoading = {
            it.showStateView<LoadingStateView>().apply {
                vImage.load(R.mipmap.loading)
            }
        }
    }

    private val state = SimpleState().apply {
        errorImageResId = R.mipmap.img_no_network
        emptyImageResId = R.mipmap.img_no_record
    }.with(this)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        state.presenter.retryListener = View.OnClickListener {
            window.decorView.postDelayed({
                // 显示 content
                state.showContent()
            }, 1000)
        }


        // 显示 loading 状态
        state.showLoading()

        window.decorView.postDelayed({
            // 显示 content
            state.showContent()
        }, 2000)

        findViewById<TextView>(R.id.txt_hello).setOnClickListener {
            startActivity(Intent(this, SimpleActivity::class.java))
        }
        findViewById<TextView>(R.id.txt_empty).setOnClickListener {
            // 显示 empty 状态
            state.showEmpty()
        }
        findViewById<TextView>(R.id.txt_error).setOnClickListener {
            // 显示 error 状态
            state.showError()
        }
    }

    override fun onBackPressed() {

        state.showContent()
//        super.onBackPressed()
    }
}