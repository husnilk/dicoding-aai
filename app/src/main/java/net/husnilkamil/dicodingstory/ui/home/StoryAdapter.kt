package net.husnilkamil.dicodingstory.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.husnilkamil.dicodingstory.R
import net.husnilkamil.dicodingstory.models.StoryItem

class StoryAdapter : PagingDataAdapter<StoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    private var listStory = ArrayList<StoryItem>()
    private var listener: StoryItemClickListener? = null

    fun setListStory(listStory: List<StoryItem>) {
        this.listStory.clear()
        this.listStory.addAll(listStory)
        Log.d("ADAPTER_DBG", listStory.size.toString())
        this.notifyDataSetChanged()
    }

    fun setListener(listener: StoryItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val item = listStory[position]
        holder.tvItemName.text = item.name
        Glide.with(holder.itemView)
            .load(item.photoUrl)
            .into(holder.ivItemPhoto)
    }

    override fun getItemCount(): Int {
        return listStory.size
    }

    inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivItemPhoto: ImageView
        var tvItemName: TextView

        init {
            ivItemPhoto = itemView.findViewById(R.id.iv_item_photo)
            tvItemName = itemView.findViewById(R.id.tv_item_name)
            itemView.setOnClickListener {
                val story = listStory[adapterPosition]
                listener!!.storyClickListener(story)
            }
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