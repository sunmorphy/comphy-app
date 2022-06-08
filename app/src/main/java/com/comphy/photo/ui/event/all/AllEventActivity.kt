package com.comphy.photo.ui.event.all

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.databinding.ActivityAllEventBinding
import com.comphy.photo.ui.event.detail.EventDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.activities.start

@AndroidEntryPoint
class AllEventActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_EVENT = "extra_event"
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityAllEventBinding.inflate(layoutInflater)
    }
    private val viewModel: AllEventViewModel by viewModels()
    private var allEventAdapter: AllEventAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupWidgets()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.searchEventResponse.observe(this) {
            allEventAdapter!!.setData(it)
            binding.rvEvent.apply {
                layoutManager = LinearLayoutManager(this@AllEventActivity)
                adapter = allEventAdapter
            }
        }
    }

    private fun setupWidgets() {
        allEventAdapter = AllEventAdapter {
            start<EventDetailActivity> { putExtra(EXTRA_EVENT, it) }
        }
        with(binding) {
            btnBack.setOnClickListener { onBackPressed() }
            edtSearch.doAfterTextChanged {
                lifecycleScope.launch { viewModel.getEventsByName(edtSearch.text.toString()) }
            }
        }
    }
}