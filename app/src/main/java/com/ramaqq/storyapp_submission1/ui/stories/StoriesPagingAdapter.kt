package com.ramaqq.storyapp_submission1.ui.stories

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.ramaqq.storyapp_submission1.R
import com.ramaqq.storyapp_submission1.data.response.ListStoryItem
import com.ramaqq.storyapp_submission1.databinding.ListStoriesBinding
import com.ramaqq.storyapp_submission1.ui.detail.DetailStoryActivity

class StoriesPagingAdapter: PagingDataAdapter<ListStoryItem, StoriesPagingAdapter.StoriesViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        val binding = ListStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoriesViewHolder(binding)
    }
    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null)  holder.bin(data)
    }

    class StoriesViewHolder(private val binding: ListStoriesBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bin(data: ListStoryItem){

            Glide.with(itemView.context)
                .load(data.photoUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_round_image_24)
                )
                .into(binding.imgStories)

            binding.tvDesc.text = data.description
            binding.tvUpload.text = itemView.context.getString(R.string.desc_upload, data.name)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.ID_STORY, data.id)

                val optionCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.imgStories, "image"),
                        Pair(binding.tvDesc, "desc"),
                        Pair(binding.tvUpload, "uploader"),
                        Pair(binding.tvDate, "date"),
                    )

                itemView.context.startActivity(
                    intent, optionCompat.toBundle()
                )
            }
        }
    }

    companion object{
         val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

}