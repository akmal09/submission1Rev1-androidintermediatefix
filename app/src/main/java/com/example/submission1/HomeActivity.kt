package com.example.submission1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission1.adapter.StoriesAdapter
import com.example.submission1.databinding.ActivityHomeBinding
import com.example.submission1.model.LoginSession
import com.example.submission1.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_RESULT="extra_person"
    }
    private lateinit var mHomeViewModel:HomeViewModel
    private lateinit var mStoriesAdapter: StoriesAdapter
    private lateinit var mSession: SessionPreference

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginSession = intent.getParcelableExtra<LoginSession>(EXTRA_RESULT) as LoginSession

        supportActionBar?.title = "list story"
        val layoutManager = LinearLayoutManager(this)
        binding.listStories.layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.listStories.addItemDecoration(itemDecoration)

        Log.d("HomeActivity", "token : ${loginSession.token}")
        mHomeViewModel = ViewModelProvider(this@HomeActivity).get(HomeViewModel::class.java)

        mHomeViewModel.isLoading.observe(this,{
            showLoading(it)
        })
        mHomeViewModel.setStories(loginSession.token)
        setStoriesAdapter()
        mHomeViewModel.getStories().observe(this, {
            mStoriesAdapter.setData(it)
            showLoading(false)
        })

        binding.addStory.setOnClickListener{
            val move = Intent(this@HomeActivity, AddStoryActivity::class.java)
            move.putExtra(AddStoryActivity.LOGIN_SESSION, loginSession)
            startActivity(move)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        itemResponse(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun itemResponse(selectedItem: Int) {
        when (selectedItem) {
            R.id.logout ->{
                logOut()
            }
        }
    }

    private fun logOut() {
        mSession = SessionPreference(this)
        mSession.deleteSession()
        Log.d(".HomeActivity", "lihat : ${mSession.getSession()}")
        val moveToLogin = Intent(this@HomeActivity, LoginActivity::class.java)
        startActivity(moveToLogin)
        finish()
    }

    private fun setStoriesAdapter() {
        binding.listStories.layoutManager = LinearLayoutManager(this)
        mStoriesAdapter = StoriesAdapter()
        mStoriesAdapter.notifyDataSetChanged()
        binding.listStories.adapter = mStoriesAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingBar.visibility = View.VISIBLE
        }else{
            binding.loadingBar.visibility = View.GONE
        }
    }
}