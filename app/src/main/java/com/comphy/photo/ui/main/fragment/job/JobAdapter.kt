package com.comphy.photo.ui.main.fragment.job

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.job.list.JobResponseContentItem
import com.comphy.photo.databinding.ItemJobBinding
import splitties.resources.colorSL
import splitties.resources.str

class JobAdapter(
    private val jobs: List<JobResponseContentItem>,
    private val onBookmarkClick: (position: Int, jobId: Int, isBookmarked: Boolean) -> Unit,
    private val onClick: (jobId: Int) -> Unit
) : RecyclerView.Adapter<JobAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobAdapter.ViewHolder {
        val view = ItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobAdapter.ViewHolder, position: Int) {
        val job = jobs[position]

        with(holder.binding) {
            Glide.with(holder.itemView)
                .load(job.linkPhotoCompany)
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
            btnBookmark.apply {
                imageTintList = if (job.isBookmarked) colorSL(R.color.primary_orange)
                else colorSL(R.color.neutral_black)
                setOnClickListener {
                    onBookmarkClick(position, job.id, job.isBookmarked)
                    job.isBookmarked = !job.isBookmarked
                }
            }
        }
        holder.itemView.setOnClickListener { onClick(job.id) }
    }

    override fun getItemCount(): Int = jobs.size

    inner class ViewHolder(var binding: ItemJobBinding) : RecyclerView.ViewHolder(binding.root)

}