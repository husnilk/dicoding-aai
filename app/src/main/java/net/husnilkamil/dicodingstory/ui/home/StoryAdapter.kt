package net.husnilkamil.dicodingstory.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.husnilkamil.dicodingstory.databinding.ItemStoryBinding
import net.husnilkamil.dicodingstory.models.StoryItem

class StoryAdapter : PagingDataAdapter<StoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    private var listener: StoryItemClickListener? = null

    fun setListener(listener: StoryItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val item = getItem(position)
        if(item != null) {
            holder.bind(item)
        }
    }

    inner class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryItem){
            binding.tvItemName.text = story.name
            Glide.with(binding.root)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)
        }
    }

    interface StoryItemClickListener {
        fun storyClickListener(story: StoryItem?)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}