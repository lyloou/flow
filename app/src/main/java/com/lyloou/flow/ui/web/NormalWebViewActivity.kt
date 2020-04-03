package com.lyloou.flow.ui.web

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.HttpAuthHandler
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.lyloou.flow.R
import com.lyloou.flow.common.toast
import com.lyloou.flow.util.Udialog
import com.lyloou.flow.util.Usystem
import com.lyloou.flow.util.Uview

/**
 * Created by lyloou on 2019-04-19 14:24:23
 * https://blog.csdn.net/chu_cheng/article/details/78084728
 * https://www.jianshu.com/p/2adaa6a5f85f
 */
class NormalWebViewActivity : AppCompatActivity() {
    private lateinit var mAgentWeb: AgentWeb
    private lateinit var mWebView: WebView
    private lateinit var mContext: Activity

    private lateinit var mToolbar: Toolbar
    private var mUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        mContext = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal_web)

        initData()
        initViewForTop()
        initViewForWeb()
    }

    private fun initData() {
        val data = intent
        mUrl = data.getStringExtra(EXTRA_URL)
        if (TextUtils.isEmpty(mUrl)) {
            mUrl = "http://lyloou.com"
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initViewForWeb() {
        val view = findViewById<View>(R.id.llyt_container)
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent((view as LinearLayout), LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator(ContextCompat.getColor(this, R.color.colorAccent), 2)
            .setWebViewClient(webViewClient)
            .setWebChromeClient(webChromeClient)
            .createAgentWeb()
            .ready()
            .go(mUrl)
        mWebView = mAgentWeb.webCreator.webView
        // https://www.jianshu.com/p/14ca454ab3d1
        val webSettings = mAgentWeb.agentWebSettings.webSettings
        webSettings.useWideViewPort = true
        webSettings.javaScriptEnabled = true
        webSettings.loadWithOverviewMode = true
    }

    private val webChromeClient: WebChromeClient
        get() = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                mToolbar.title = title
            }

            override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
                super.onReceivedIcon(view, icon)
            }
        }


    private val webViewClient: WebViewClient
        get() = object : WebViewClient() {
            override fun onReceivedHttpAuthRequest(
                view: WebView,
                handler: HttpAuthHandler,
                host: String,
                realm: String
            ) {
                Udialog.showHttpAuthRequest(view, handler)
            }
        }

    private fun initViewForTop() {
        mToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)
        mToolbar.title = ""
        // doubleClickToolbar scrollToTop
        Uview.setDoubleClickRunnable(mToolbar) { mWebView.scrollTo(0, 0) }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        mToolbar.setNavigationOnClickListener { onBackPressed() }
    }


    override fun onBackPressed() {
        if (mAgentWeb.back()) {
            return
        }
        super.onBackPressed()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_normal_webview, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_close -> finish()
            R.id.menu_open_with_browser -> {
                val uri = Uri.parse(mAgentWeb.webCreator.webView.url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
            R.id.menu_refresh -> mAgentWeb.urlLoader.reload()
            R.id.menu_copy_link -> {
                val url = mAgentWeb.webCreator.webView.url
                Usystem.copyString(mContext, url)
                toast("复制成功")
            }
            R.id.menu_toggle_orientation -> {
                requestedOrientation =
                    if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

        }
        return super.onOptionsItemSelected(item)
    }


    companion object {
        const val EXTRA_URL = "url"

        fun getMineIntent(context: Context, url: String): Intent {
            val intent = Intent(context, NormalWebViewActivity::class.java)
            intent.putExtra(EXTRA_URL, url)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            return intent
        }

        fun start(context: Context, url: String) {
            context.startActivity(getMineIntent(context, url))
        }

    }
}