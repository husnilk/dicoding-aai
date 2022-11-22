package net.husnilkamil.dicodingstory.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import net.husnilkamil.dicodingstory.*
import net.husnilkamil.dicodingstory.databinding.ActivityHomeBinding
import net.husnilkamil.dicodingstory.data.networks.Response.GetStoryResponse
import net.husnilkamil.dicodingstory.models.StoryItem
import net.husnilkamil.dicodingstory.utils.Constant
import net.husnilkamil.dicodingstory.utils.getToken
import net.husnilkamil.dicodingstory.data.networks.NetworkConfig
import net.husnilkamil.dicodingstory.ui.ViewModelFactory
import net.husnilkamil.dicodingstory.ui.addstory.AddStoryActivity
import net.husnilkamil.dicodingstory.ui.detailstory.DetailStoryActivity
import net.husnilkamil.dicodingstory.ui.login.LoginActivity
import net.husnilkamil.dicodingstory.ui.maps.MapsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity(), StoryAdapter.StoryItemClickListener {

    private lateinit var binding: ActivityHomeBinding
    private var isLoggedIn = false
    private lateinit var adapter: StoryAdapter
    private lateinit var storyListViewModel: StoryListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setSupportActionBar(binding!!.toolbar)

        binding!!.progressLoading.visibility = View.GONE
        binding!!.fab.setOnClickListener {
            val addIntent = Intent(this@HomeActivity, AddStoryActivity::class.java)
            startActivity(addIntent)
        }

        adapter = StoryAdapter()
        adapter!!.setListener(this)
        binding!!.rvListCerita.adapter = adapter
        val layoutManager = GridLayoutManager(this, 2)
        binding!!.rvListCerita.layoutManager = layoutManager
        isLoggedInCheck

        val factory = ViewModelFactory.getInstance(this@HomeActivity)
        storyListViewModel = ViewModelProvider(this, factory).get(StoryListViewModel::class.java)
        binding.progressLoading.visibility = View.VISIBLE
        storyListViewModel.stories.observe(this) { stories ->
            binding.progressLoading.visibility = View.GONE
            adapter.submitData(lifecycle, stories)
        }
    }

//    override fun onStart() {
//        super.onStart()
//    }

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
        val mapIntent = Intent(this@HomeActivity, MapsActivity::class.java)
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

//    fun getStories(){
//        binding!!.progressLoading.visibility = View.VISIBLE
//        val service = NetworkConfig.service
//        val response = service.getAllStories(getToken(this), 1)
//        response.enqueue(object : Callback<GetStoryResponse?>{
//
//            override fun onResponse(call: Call<GetStoryResponse?>, response: Response<GetStoryResponse?>) {
//                var getStoryResponse : GetStoryResponse? = response.body()
//                if(getStoryResponse != null){
//                    val listStory = getStoryResponse.listStory
//                    adapter?.setListStory(listStory as List<StoryItem>);
//                }
//                binding!!.progressLoading.visibility = View.GONE
//            }
//
//            override fun onFailure(call: Call<GetStoryResponse?>, t: Throwable) {
//                Toast.makeText(this@HomeActivity, "Terjadi kendala teknis", Toast.LENGTH_SHORT).show()
//                binding!!.progressLoading.visibility = View.GONE
//            }
//
//        })
//    }
}


