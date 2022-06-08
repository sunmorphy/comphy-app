package com.comphy.photo.ui.article

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.event.EventResponseContentItem
import com.comphy.photo.databinding.ActivityArticleDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import splitties.activities.start
import splitties.resources.drawable

@AndroidEntryPoint
class ArticleDetailActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_ARTICLE = "extra_article"
        private const val EXTRA_NEXT_ARTICLE = "extra_next_article"
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityArticleDetailBinding.inflate(layoutInflater)
    }
    private var extraArticle: EventResponseContentItem? = null
    private var extraNextArticle: EventResponseContentItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        extraArticle = intent.getParcelableExtra(EXTRA_ARTICLE)
        extraNextArticle = intent.getParcelableExtra(EXTRA_NEXT_ARTICLE)

        if (extraArticle == null) finish()
        else setupWidgets()
    }

    private fun setupWidgets() {
        with(binding) {
            btnBack.setOnClickListener { onBackPressed() }
            Glide.with(this@ArticleDetailActivity)
                .load(extraArticle?.linkBanner)
                .placeholder(drawable(R.drawable.img_banner_placeholder))
                .error(drawable(R.drawable.img_banner_placeholder))
                .into(imgArticle)
            txtArticleTitle.text = extraArticle?.eventName
            txtArticleContent.text = extraArticle?.description
            txtArticleSourceContent.text = extraArticle?.linkEvent
            btnNextArticle.apply {
                if (extraNextArticle != null) {
                    visibility = View.VISIBLE
                    text = extraNextArticle?.eventName!!
                    setOnClickListener { start<ArticleDetailActivity> {
                        putExtra(EXTRA_ARTICLE, extraNextArticle)
                    } }
                } else {
                    visibility = View.GONE
                }
            }
        }
    }
}