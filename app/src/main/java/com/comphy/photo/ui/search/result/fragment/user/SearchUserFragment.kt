package com.comphy.photo.ui.search.result.fragment.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.databinding.FragmentSearchUserBinding
import com.comphy.photo.ui.search.result.SearchResultActivity
import com.comphy.photo.ui.search.result.SearchResultViewModel
import kotlinx.coroutines.launch
import splitties.toast.toast

class SearchUserFragment : Fragment() {

    private var _binding: FragmentSearchUserBinding? = null
    val binding get() = _binding!!

    private val viewModel: SearchResultViewModel by activityViewModels()
    private var searchUserAdapter: SearchUserAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch { viewModel.getFilteredUsers((activity as SearchResultActivity).extraKey!!) }

//        setupWidgets()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.usersResponse.observe(viewLifecycleOwner) {
            searchUserAdapter = SearchUserAdapter { pos, userId ->
                toast(" THIS FEATURE IS NOT READY YET ")
                // TODO: FOLLOW OR UNFOLLOW USER
            }
            searchUserAdapter!!.setData(it)
            with(binding) {
                rvUser.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = searchUserAdapter
                }
            }
        }
    }

    private fun setupWidgets() {

        with(binding) {
            rvUser.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = searchUserAdapter
            }
        }
    }
}