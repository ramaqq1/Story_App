package com.ramaqq.storyapp_submission1.ui.stories

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.ramaqq.storyapp_submission1.R
import com.ramaqq.storyapp_submission1.data.response.ListStoryItem
import com.ramaqq.storyapp_submission1.ui.detail.DetailStoryActivity
import com.ramaqq.storyapp_submission1.ui.detail.DetailStoryActivity.Companion.ID_STORY
import com.ramaqq.storyapp_submission1.ui.getTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class StoriesAdapter: RecyclerView.Adapter<StoriesAdapter.StoriesViewHolder>() {
    private val listData: MutableList<ListStoryItem> = ArrayList()

    fun setListData(listData: List<ListStoryItem>?){
        if (listData == null)return
        this.listData.clear()
        this.listData.addAll(listData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_stories, parent, false)
        return StoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        val data = listData[position]
        holder.bin(data)
    }

    override fun getItemCount(): Int = listData.size


    class StoriesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var imagePhoto: ImageView = itemView.findViewById(R.id.img_stories)
        private var tvDesc: TextView = itemView.findViewById(R.id.tv_desc)
        private var tvUpload: TextView = itemView.findViewById(R.id.tv_upload)
        private var tvDate: TextView = itemView.findViewById(R.id.tv_date)

        fun bin(data: ListStoryItem) {
            Glide.with(itemView.context)
                .load(data.photoUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_round_image_24)
                )
                .into(imagePhoto)

            tvDesc.text = data.description
            tvUpload.text = itemView.context.getString(R.string.desc_upload, data.name)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra(ID_STORY, data.id)

                val optionCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(imagePhoto, "image"),
                        Pair(tvDesc, "desc"),
                        Pair(tvUpload, "uploader"),
                        Pair(tvDate, "date"),
                    )

                itemView.context.startActivity(
                    intent, optionCompat.toBundle()
                )
            }

            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            try {
                val date1 = input.parse(data.createdAt) // dipake
                val tanggal1 = getTime(date1, outputDate)
                tvDate.text = ". $tanggal1"
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

    }
}