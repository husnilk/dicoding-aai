package net.husnilkamil.dicodingstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import net.husnilkamil.dicodingstory.databinding.ActivityDetailStoryBinding
import net.husnilkamil.dicodingstory.datamodels.StoryItem

class DetailStoryActivity : AppCompatActivity() {

    var name: String? = null
    var photo: String? = null
    var description: String? = null
    var date: String? = null
    var binding: ActivityDetailStoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val storyIntent = intent
        if (storyIntent != null) {
            val story = storyIntent.getParcelableExtra<StoryItem>("STORY_PARCELABLE")
            loadStoryDetail(story)
        }
    }

    private fun loadStoryDetail(story: StoryItem?) {
        name = story!!.name
        photo = story.photoUrl
        description = story.description
        date = story.createdAt
        binding!!.tvDetailDescription.text = description
        binding!!.tvDetailName.text = name
        binding!!.tvDetailDate.text = date
        Glide.with(this)
            .load(story.photoUrl)
            .into(binding!!.ivDetailPhoto)
    }
}