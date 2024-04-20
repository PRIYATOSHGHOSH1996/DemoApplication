package com.example.demoapplication.ui.main

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demoapplication.adapters.PaginationAdapter
import com.example.demoapplication.R
import com.example.demoapplication.api.ApiResponseModel
import com.example.demoapplication.api.Resource
import com.example.demoapplication.databinding.ActivityMainBinding
import com.example.demoapplication.utils.PaginationScrollListener


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    private val PAGE_START = 1
    private var isLoading = false
    private var isLastPage = false

    private lateinit var binding : ActivityMainBinding
    private var TOTAL_PAGES = 0
    private var currentPage = PAGE_START
    private val TAG = "MainActivity"
    lateinit var adapter: PaginationAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    var dataList = ArrayList<ApiResponseModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)
       adapter = PaginationAdapter(this)

        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.mainRecycler.setLayoutManager(linearLayoutManager)

        binding.mainRecycler.setItemAnimator(DefaultItemAnimator())

        binding.mainRecycler.setAdapter(adapter)
        binding.mainRecycler.addOnScrollListener(object :PaginationScrollListener(linearLayoutManager){
            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1
                if (currentPage <= TOTAL_PAGES) // simulating network latency for API call
                    Handler().postDelayed(Runnable { loadNextPage() }, 1000)
            }

            override fun getTotalPageCount(): Int {
                return TOTAL_PAGES;
            }

            override fun isLastPage(): Boolean {
                return isLastPage;
            }

            override fun isLoading(): Boolean {
                return isLoading;
            }

        })
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        observers()
        viewModel.getPostData()
    }

    private fun observers() {
        viewModel.getPostLiveData().observe(this){
            when(it){
                is Resource.Loading ->{
                    Log.e("load","og")
                    binding.shimmerViewContainer.visibility = View.VISIBLE
                }
                is Resource.Success ->{
                    dataList = it.data as ArrayList<ApiResponseModel>
                    TOTAL_PAGES = dataList.size/10
                    loadFirstPage()
                }
                is Resource.Error ->{
                    it.throwable?.printStackTrace()
                }
            }
        }
    }

    private fun loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ")

        binding.shimmerViewContainer.visibility = View.GONE
                adapter.addAll(dataList.slice(0..((currentPage*10)-1)))
                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter() else isLastPage = true

    }
    private fun loadNextPage() {
        Log.d(TAG, "loadNextPage: $currentPage")
        adapter.removeLoadingFooter()
                isLoading = false
                adapter.addAll(dataList.slice((((currentPage-1)*10))..((currentPage*10)-1)))
                if (currentPage !== TOTAL_PAGES) adapter.addLoadingFooter() else isLastPage = true


    }
}