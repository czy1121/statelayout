# StateLayout

简单实用无侵入高扩展的页面多状态布局(content,loading,empty,error)


- StateLayout 实现了基础多状态切换功能
- SimpleState 为各种状态提供了配置参数，可随时修改
- SimpleState 实现了 StatePresenter 接口，实现了 content,loading,empty,error 四种状态的呈现逻辑
- SimpleState.Controller 供用户控制状态切换，通过 SimpleState.with() 返回
- SimpleStateView 是默认提供的状态View，包含一个图片，一个文本，一个按钮

## Gradle

``` groovy
repositories {
    maven { url "https://gitee.com/ezy/repo/raw/cosmo/"}
}
dependencies {
    implementation "me.reezy.cosmo:statelayout:0.8.0"
}
```

## 使用

直接在Activity上切换状态

```kotlin
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val state = SimpleState().with(this)

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

        findViewById<TextView>(R.id.txt_empty).setOnClickListener {
            // 显示 empty 状态
            state.showEmpty()
        }
        findViewById<TextView>(R.id.txt_error).setOnClickListener {
            // 显示 error 状态
            state.showError()
        }
    }
}
```


在布局里添加 StateLayout 切换状态

```kotlin

class SimpleActivity : AppCompatActivity(R.layout.activity_simple) {
    private val binding by lazy { ActivitySimpleBinding.bind(findViewById<ViewGroup>(android.R.id.content).getChildAt(0)) }

    private val state by lazy { SimpleState().with(binding.state) }

    ...
}
```

## LICENSE

The Component is open-sourced software licensed under the [Apache license](LICENSE).