package com.comphy.photo.ui.comment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.R
import com.comphy.photo.databinding.ActivityCommentBinding
import com.comphy.photo.ui.comment.fragment.CommentFragment

class CommentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(R.id.frameComment, CommentFragment(), CommentFragment::class.java.simpleName)
            .commit()
    }
}