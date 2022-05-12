package com.comphy.photo.ui.comment.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.R
import com.comphy.photo.databinding.FragmentCommentBinding
import com.comphy.photo.ui.comment.CommentAdapter
import splitties.toast.toast

class CommentFragment : Fragment() {

    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvComment.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = CommentAdapter{ navigateToDetail() }
        }
    }

    private fun navigateToDetail() {
        parentFragmentManager.beginTransaction().apply {
            replace(
                R.id.frameComment,
                CommentDetailFragment(),
                CommentDetailFragment::class.java.simpleName
            )
            addToBackStack(null)
            commit()
        }
    }
}