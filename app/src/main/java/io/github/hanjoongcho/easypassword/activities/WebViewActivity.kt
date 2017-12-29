package io.github.hanjoongcho.easypassword.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.github.hanjoongcho.easypassword.R
import kotlinx.android.synthetic.main.activity_web_view.*

/**
 * Created by CHO HANJOONG on 2017-11-24.
 */

class WebViewActivity : SimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
//        webView.getSettings().setJavaScriptEnabled(true)
        webView.loadUrl(intent.getStringExtra(OPEN_URL_INFO))
        finish.setOnClickListener {
            this@WebViewActivity.onBackPressed()
        }
    }

    companion object {
        const val OPEN_URL_INFO = "open_url_info"

        fun getStartIntent(context: Context, openUrlInfo: String): Intent {
            return Intent(context, WebViewActivity::class.java)
                    .apply { putExtra(OPEN_URL_INFO, openUrlInfo) }
        }
    }
}