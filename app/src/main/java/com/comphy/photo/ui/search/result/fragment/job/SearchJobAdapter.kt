package com.comphy.photo.ui.search.result.fragment.job

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.job.list.JobResponseContentItem
import com.comphy.photo.databinding.ItemJobBinding
import splitties.resources.str

class SearchJobAdapter(
    private val onClick: (content: JobResponseContentItem) -> Unit
) : RecyclerView.Adapter<SearchJobAdapter.ViewHolder>() {

    private val jobs = mutableListOf<JobResponseContentItem>()

    fun setData(jobs: List<JobResponseContentItem>) {
        this.jobs.clear()
        this.jobs.addAll(jobs)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchJobAdapter.ViewHolder {
        val view = ItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchJobAdapter.ViewHolder, position: Int) {
        val job = jobs[position]

        with(holder.binding) {
            Glide.with(holder.itemView)
                .load("")
                .placeholder(R.drawable.ic_placeholder_people)
                .error(R.drawable.ic_placeholder_people)
                .centerCrop()
                .into(imgJobLogo)

            txtJobTitle.text = job.title
            txtJobCompany.text = job.companyName
            txtJobLocation.text = job.location
            txtJobType.text =
                if (job.isFulltime) holder.itemView.str(R.string.job_type_full)
                else holder.itemView.str(R.string.job_type_part)
        }
        holder.itemView.setOnClickListener { onClick(job) }
    }

    override fun getItemCount(): Int = jobs.size

    inner class ViewHolder(var binding: ItemJobBinding) : RecyclerView.ViewHolder(binding.root)
}