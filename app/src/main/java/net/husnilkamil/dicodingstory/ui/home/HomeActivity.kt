package net.husnilkamil.dicodingstory.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import net.husnilkamil.dicodingstory.R
import net.husnilkamil.dicodingstory.databinding.ActivityHomeBinding
import net.husnilkamil.dicodingstory.models.StoryItem
import net.husnilkamil.dicodingstory.ui.ViewModelFactory
import net.husnilkamil.dicodingstory.ui.addstory.AddStoryActivity
import net.husnilkamil.dicodingstory.ui.detailstory.DetailStoryActivity
import net.husnilkamil.dicodingstory.ui.login.LoginActivity
import net.husnilkamil.dicodingstory.ui.maps.MapsActivity
import net.husnilkamil.dicodingstory.utils.Constant


class HomeActivity : AppCompatActivity(), StoryAdapter.StoryItemClickListener {

    private lateinit var binding: ActivityHomeBinding
    private var isLoggedIn = false
    private lateinit var adapter: StoryAdapter
    private val storyListViewModel: StoryListViewModel by viewModels { ViewModelFactory(this)    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        StrictMode.setVmPolicy(
            VmPolicy.Builder(StrictMode.getVmPolicy())
                .detectLeakedClosableObjects()
                .build()
        )

        isLoggedInCheck

        binding.fab.setOnClickListener {
            val addIntent = Intent(this@HomeActivity, AddStoryActivity::class.java)
            startActivity(addIntent)
        }


        binding.rvListCerita.layoutManager = GridLayoutManager(this, 2)

        getData()

    }

    private fun getData(){
        adapter = StoryAdapter()
        binding.rvListCerita.adapter = adapter.withLoadStateFooter(
            footer = LoadingAdapter{
                adapter.retry()
            }
        )

        storyListViewModel.stories.observe(this, { stories ->
            adapter.submitData(lifecycle, stories)
        })

        adapter.setListener(this)
    }

    private val isLoggedInCheck: Unit
        get() {
            val preferences = getSharedPreferences(Constant.PREF_KEY_FILE_NAME, MODE_PRIVATE)
            val token = preferences.getString(Constant.PREF_KEY_TOKEN, null)
            isLoggedIn = token != null
            if (!isLoggedIn) {
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
                return
            }
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> logout()
            R.id.action_map -> loadmap()
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadmap() {
        val mapIntent = Intent(this, MapsActivity::class.java)
        startActivity(mapIntent)
    }

    private fun logout() {
        val preferences = getSharedPreferences(Constant.PREF_KEY_FILE_NAME, MODE_PRIVATE)
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }

    override fun storyClickListener(story: StoryItem?) {
        val storyIntent = Intent(this, DetailStoryActivity::class.java)
        storyIntent.putExtra("STORY_PARCELABLE", story)
        startActivity(storyIntent)
    }

}


