package net.husnilkamil.dicodingstory

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import net.husnilkamil.dicodingstory.adapters.StoryAdapter
import net.husnilkamil.dicodingstory.databinding.ActivityHomeBinding
import net.husnilkamil.dicodingstory.datamodels.StoryItem
import net.husnilkamil.dicodingstory.helpers.Constant

class HomeActivity : AppCompatActivity(), StoryAdapter.StoryItemClickListener {
    private val appBarConfiguration: AppBarConfiguration? = null
    private var binding: ActivityHomeBinding? = null
    private var isLoggedIn = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setSupportActionBar(binding!!.toolbar)
        binding!!.fab.setOnClickListener {
            val addIntent = Intent(this@HomeActivity, AddStoryActivity::class.java)
            startActivity(addIntent)
        }
        val adapter = StoryAdapter()
        adapter.setListener(this)
        binding!!.rvListCerita.adapter = adapter
        val layoutManager = GridLayoutManager(this, 2)
        binding!!.rvListCerita.layoutManager = layoutManager
        isLoggedInCheck
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
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        val preferences = getSharedPreferences(Constant.PREF_KEY_FILE_NAME, MODE_PRIVATE)
        val editor = preferences.edit()
        editor.clear()
        editor.commit()
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }

    override fun storyClickListener(story: StoryItem?) {
        val storyIntent = Intent(this, StoryActivity::class.java)
        storyIntent.putExtra("STORY_PARCELABLE", story)
        startActivity(storyIntent)
    }
}