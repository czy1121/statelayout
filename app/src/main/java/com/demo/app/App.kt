package com.demo.app

import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        val app = this
        Coil.setImageLoader(ImageLoader.Builder(app).components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
            // add(SvgDecoder.Factory())
        }.memoryCache {
            MemoryCache.Builder(app)
                .maxSizePercent(0.25)
                .build()
        }.diskCache {
            DiskCache.Builder()
                .directory(app.cacheDir.resolve("image_cache"))
                .maxSizePercent(0.02)
                .build()
        }.fallback(ColorDrawable(Color.DKGRAY)).build())




        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            ClassicsHeader(context).apply {
                setEnableLastTime(false)
            }
        }
    }


}