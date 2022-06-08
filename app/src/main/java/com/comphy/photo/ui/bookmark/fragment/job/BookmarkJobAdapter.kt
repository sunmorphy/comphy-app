package com.comphy.photo.ui.bookmark.fragment.job

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.job.bookmark.BookmarkJobResponseContentItem
import com.comphy.photo.databinding.ItemJobBinding
import splitties.resources.str

class BookmarkJobAdapter(
    private val onClick: (content: BookmarkJobResponseContentItem) -> Unit
) : RecyclerView.Adapter<BookmarkJobAdapter.ViewHolder>() {

    private val jobs = mutableListOf<BookmarkJobResponseContentItem>()

    fun setData(jobs: List<BookmarkJobResponseContentItem>) {
        this.jobs.clear()
        this.jobs.addAll(jobs)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkJobAdapter.ViewHolder {
        val view = ItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookmarkJobAdapter.ViewHolder, position: Int) {
        val job = jobs[position]

        with(holder.binding) {
            Glide.with(holder.itemView)
                .load("")
                .placeholder(R.drawable.ic_placeholder_people)
                .error(R.drawable.ic_placeholder_people)
                .centerCrop()
                .into(imgJobLogo)

            txtJobTitle.text = job.jobVacancy.title
            txtJobCompany.text = job.jobVacancy.companyName
            txtJobLocation.text = job.jobVacancy.location
            txtJobType.text =
                if (job.jobVacancy.isFulltime) holder.itemView.str(R.string.job_type_full)
                else holder.itemView.str(R.string.job_type_part)
        }
        holder.itemView.setOnClickListener { onClick(job) }
    }

    override fun getItemCount(): Int = jobs.size

    inner class ViewHolder(var binding: ItemJobBinding) : RecyclerView.ViewHolder(binding.root)
}