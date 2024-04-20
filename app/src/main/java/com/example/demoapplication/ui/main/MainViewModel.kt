package com.example.demoapplication.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapplication.api.ApiResponseModel
import com.example.demoapplication.api.Resource
import com.example.demoapplication.utils.SingleLiveEvent
import com.example.demoapplication.utils.asLiveData
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {
    private val repository = MainRepository()

    private val _postData by lazy { SingleLiveEvent<Resource<List<ApiResponseModel>?>>() }

    fun getPostLiveData() = _postData.asLiveData()


    fun getPostData(){
        viewModelScope.launch{
            runCatching {
                _postData.postValue(Resource.loading())
                repository.getData()
            }.onSuccess {
                _postData.postValue(Resource.success(it))
            }.onFailure {
                _postData.postValue(Resource.error(it.message,it))
            }
        }

    }
}