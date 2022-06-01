package com.comphy.photo.ui.comment.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.databinding.FragmentCommentDetailBinding
import com.comphy.photo.ui.comment.detail.CommentSecondLevelAdapter
import splitties.toast.toast

class CommentDetailFragment : Fragment() {

    private var _binding: FragmentCommentDetailBinding? = null
    private val binding get() = _binding!!

    private var isReplyVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCommentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.rvCommentSecondLevel.apply {
//            layoutManager = LinearLayoutManager(view.context)
//            adapter = CommentSecondLevelAdapter({ toast("THIS IS SECOND LEVEL") },
//                { toast("THIS IS THIRD LEVEL") })
//        }
        binding.btnBack.setOnClickListener { activity?.onBackPressed() }
        binding.layoutReply.setOnClickListener {
            if (isReplyVisible) {
                binding.layoutReplyContent.visibility = View.GONE
            } else {
                binding.layoutReplyContent.visibility = View.VISIBLE
            }
            isReplyVisible = !isReplyVisible
        }
    }
}