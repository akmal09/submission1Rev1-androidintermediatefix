package com.example.submission1.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission1.DetailStoryActivity
import com.example.submission1.api.ListStory
import com.example.submission1.databinding.ListStoriesAdapterBinding
import androidx.core.util.Pair

class StoriesAdapter: RecyclerView.Adapter<StoriesAdapter.ListViewHolder>() {

    var mStories = ArrayList<ListStory>()

    fun setData(listStory: List<ListStory>) {
        mStories.clear()
        mStories.addAll(listStory)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val bindingLayer = ListStoriesAdapterBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(bindingLayer)
    }

    override fun onBindViewHolder(holder: StoriesAdapter.ListViewHolder, position: Int) {
        holder.bind(mStories[position])
    }

    override fun getItemCount(): Int = mStories.size

    inner class ListViewHolder(private val binding: ListStoriesAdapterBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(listStory: ListStory) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(listStory.photoUrl)
                    .into(storiesPhoto)
                userName.text = listStory.name

                val compat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(binding.storiesPhoto, "photoStory"),
                    Pair(binding.userName, "username")
                )

                itemView.setOnClickListener{
                    val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                    intent.putExtra(DetailStoryActivity.OBJECT, listStory)
                    itemView.context.startActivity(intent, compat.toBundle())
                }
            }
        }
    }

}